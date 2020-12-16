package com.digitzones.controllers;

import com.alibaba.fastjson.JSON;
import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.service.*;
import com.digitzones.threads.CheckLostTimeThread;
import com.digitzones.vo.ProcessRecordVO;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 加工记录控制器
 * @author zdq
 */
@Controller
@RequestMapping("/processRecord")
public class ProcessRecordController {
	ExecutorService executor = Executors.newCachedThreadPool();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private IProcessRecordService processRecordService;
	private IDeviceSiteService deviceSiteService;
	private IWorkSheetService workSheetService;
	private IWorkSheetDetailService workSheetDetailService;
	private IClassesService classService;
	@Autowired
	private IInventoryService workpieceService;
	private IWorkpieceProcessDeviceSiteMappingService workpieceProcessDeviceSiteMappingService;
	@Autowired
	private ILostTimeRecordService lostTimeRecordService;
	/*public void setLostTimeRecordService(@Qualifier("lostTimeRecordServiceProxy") ILostTimeRecordService lostTimeRecordService) {
		this.lostTimeRecordService = lostTimeRecordService;
	}*/
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	public void setWorkpieceProcessDeviceSiteMappingService(
			IWorkpieceProcessDeviceSiteMappingService workpieceProcessDeviceSiteMappingService) {
		this.workpieceProcessDeviceSiteMappingService = workpieceProcessDeviceSiteMappingService;
	}
	@Autowired
	public void setClassService(IClassesService classService) {
		this.classService = classService;
	}
	@Autowired
	public void setWorkSheetDetailService(IWorkSheetDetailService workSheetDetailService) {
		this.workSheetDetailService = workSheetDetailService;
	}
	@Autowired
	public void setWorkSheetService(IWorkSheetService workSheetService) {
		this.workSheetService = workSheetService;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}
	@Autowired
	public void setProcessRecordService(IProcessRecordService processRecordService) {
		this.processRecordService = processRecordService;
	}
	/**
	 * 根据设备站点id查询加工信息
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProcessRecordByDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryProcessRecordByDeviceSiteId(Long deviceSiteId, @RequestParam(value="rows",defaultValue="20") Integer rows, @RequestParam(defaultValue="1") Integer page){
		Pager<ProcessRecord> pager = processRecordService.queryObjs("from ProcessRecord pr where pr.deviceSiteId=?0 and pr.deleted=?1 and pr.realRecord=?2 order by pr.collectionDate desc", page, rows, new Object[] {deviceSiteId,false,true});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据设备站点id查询加工信息（搜索）
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProcessRecordByDeviceSiteIdandSearch.do")
	@ResponseBody
	public ModelMap queryProcessRecordByDeviceSiteIdandSearch(Long deviceSiteId, @RequestParam Map<String,String> params, @RequestParam(value="rows",defaultValue="20") Integer rows, @RequestParam(defaultValue="1") Integer page){
		String hql="from ProcessRecord pr where pr.deviceSiteId=?0 and pr.deleted=?1 and pr.realRecord=?2";
		String searchText=params.get("searchText");
	 	String searchChange=params.get("searchChange");
	 	String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		List<Object> list=new ArrayList<Object>();
		list.add(deviceSiteId);
		list.add(false);
		list.add(true);
		int i=list.size()-1;
		try {
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
					i++;
					hql+=" and pr.collectionDate>=?"+i;	
					list.add(format.parse(beginDateStr));
			}
			if(endDateStr!=null && !"".equals(endDateStr)) {
				i++;
				hql+=" and pr.collectionDate<=?"+i;
				list.add(format.parse(endDateStr));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(searchText!=null && !"".equals(searchText)&&searchChange!=null && !"".equals(searchChange)) {
			hql+=" and pr."+searchChange+" like '%'+?"+(i+1)+"+'%'";
			list.add(searchText);
		}
		hql+=" order by pr.collectionDate desc";
		Object[] obj =  list.toArray(new Object[1]);
		Pager<ProcessRecord> pager = processRecordService.queryObjs(hql, page, rows, obj);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 添加加工记录
	 * @param processRecord
	 * @return
	 */
	@RequestMapping("/addProcessRecord.do")
	@ResponseBody
	public ModelMap addProcessRecord(ProcessRecord processRecord, HttpServletRequest request, Principal principal) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryByProperty("code", processRecord.getDeviceSiteCode());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}
		processRecord.setDeviceSiteId(ds.getId());
		processRecord.setDeviceSiteName(ds.getName());
		ProcessRecord c4code = processRecordService.queryByProductNumAndDeviceSiteCode(processRecord.getProductNum(), processRecord.getDeviceSiteCode());;
		if(c4code!=null && !c4code.getId().equals(processRecord.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "生产序号已被使用");
		}else  {
			User user = userService.queryUserByUsername(principal.getName());
			if(user!=null) {
				Employee employee = user.getEmployee();
				if(employee!=null) {
					//processRecord.setProductUserId(employee.getId());
					processRecord.setProductUserCode(employee.getCode());
					processRecord.setProductUserName(employee.getName());
					processRecord.setDocumentMaker(employee.getName());
				}
			}
			//根据采集时间(生产时间)查找班次信息
			Date collectionDate = processRecord.getCollectionDate();
			Classes c = classService.queryClassesByTime(collectionDate);
			if(c!=null) {
				processRecord.setClassesCode(c.getCode());
				processRecord.setClassesId(c.getId());
				processRecord.setClassesName(c.getName());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(collectionDate);

				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(c.getStartTime());
				startCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				startCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
				startCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(c.getEndTime());
				endCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				endCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
				endCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
				//跨天
				if(startCalendar.after(endCalendar)) {
					calendar.add(Calendar.DATE, -1);
					processRecord.setForProductionDate(yyyyMMdd.format(calendar.getTime()));
				}else {
					processRecord.setForProductionDate(yyyyMMdd.format(calendar.getTime()));
				}
			}
			
			if(processRecord.getWorkPieceCode()!=null) {
				//根据工件编码查找工件
				Inventory workpiece = workpieceService.queryByProperty("code", processRecord.getWorkPieceCode());
				if(workpiece!=null) {
					//processRecord.setWorkPieceId(workpiece.getId());
					processRecord.setWorkPieceCode(workpiece.getCode());
				}
			}
			
			//添加执行记录的操作人员信息
			String clientIp = request.getRemoteAddr();
			MCUser mcUser = mcUserService.queryLoginUserByClientIp(clientIp);
			if(mcUser!=null) {
				Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
				//processRecord.setProductUserId(employee.getCode());
				processRecord.setProductUserCode(employee.getCode());
				processRecord.setProductUserName(employee.getName());
			}
			processRecord.setMakeDocumentDate(new Date());
			//查找标准节拍
			Float processingBeat = workpieceProcessDeviceSiteMappingService.queryProcessingBeat(processRecord.getWorkPieceCode(), processRecord.getProcessId(), processRecord.getDeviceSiteId());
			processRecord.setStandardBeat((processingBeat==null||processingBeat==0)?ds.getDevice().getProductionUnit().getBeat():processingBeat);
			//processRecord.setStandardBeat((processingBeat==null)?0:processingBeat);
			Map<String,Object> args = new HashMap<>();
			args.put("businessKey", workFlowKeyConfig.getLostTimeWorkflowKey());
			processRecordService.addProcessRecord(processRecord, user, args);
			//启动一个线程，延迟阈值时间间隔运行，查询当前设备是否有新的执行记录，如果没有，则添加损时记录;否则，什么也不做
			CheckLostTimeThread checkLostTimeThread = new CheckLostTimeThread(processRecord.getDeviceSiteId(), ds.getDevice().getProductionUnit().getThreshold(), processRecord.getCollectionDate(), user, args, processRecord.getStandardBeat());
			//CheckLostTimeThread checkLostTimeThread = new CheckLostTimeThread(processRecord.getDeviceSiteId(), sysConfig.getThreshold(), processRecord.getCollectionDate(), user, args, processRecord.getStandardBeat());
			checkLostTimeThread.setClassService(classService);
			checkLostTimeThread.setLostTimeRecordService(lostTimeRecordService);
			checkLostTimeThread.setProcessRecordService(processRecordService);
			executor.execute(checkLostTimeThread);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}

	/**
	 * 添加加工记录(外部调用)
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/addProcessRecordWithExternal.do")
	@ResponseBody
	public ModelMap addProcessRecordWithExternal(String jsonObject, HttpServletRequest request, Principal principal) {
		ProcessRecord processRecord= JSON.parseObject(jsonObject,ProcessRecord.class);
		ModelMap modelMap = new ModelMap();
		if(processRecord==null){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "添加失败!");
			return modelMap;
		}

		DeviceSite ds = deviceSiteService.queryByProperty("code", processRecord.getDeviceSiteCode());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}
		processRecord.setDeviceSiteId(ds.getId());
		processRecord.setDeviceSiteName(ds.getName());
		ProcessRecord c4code = processRecordService.queryByProductNumAndDeviceSiteCode(processRecord.getProductNum(), processRecord.getDeviceSiteCode());;
		if(c4code!=null && !c4code.getId().equals(processRecord.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "生产序号已被使用");
		}else  {
			User user = userService.queryUserByUsername(principal.getName());
			if(user!=null) {
				Employee employee = user.getEmployee();
				if(employee!=null) {
					//processRecord.setProductUserId(employee.getId());
					processRecord.setProductUserCode(employee.getCode());
					processRecord.setProductUserName(employee.getName());
					processRecord.setDocumentMaker(employee.getName());
				}
			}
			//根据采集时间(生产时间)查找班次信息
			Date collectionDate = processRecord.getCollectionDate();
			Classes c = classService.queryClassesByTime(collectionDate);
			if(c!=null) {
				processRecord.setClassesCode(c.getCode());
				processRecord.setClassesId(c.getId());
				processRecord.setClassesName(c.getName());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(collectionDate);

				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(c.getStartTime());
				startCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				startCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
				startCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(c.getEndTime());
				endCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				endCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
				endCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
				//跨天
				if(startCalendar.after(endCalendar)) {
					calendar.add(Calendar.DATE, -1);
					processRecord.setForProductionDate(yyyyMMdd.format(calendar.getTime()));
				}else {
					processRecord.setForProductionDate(yyyyMMdd.format(calendar.getTime()));
				}
			}

			if(processRecord.getWorkPieceCode()!=null) {
				//根据工件编码查找工件
				Inventory workpiece = workpieceService.queryByProperty("code", processRecord.getWorkPieceCode());
				if(workpiece!=null) {
					//processRecord.setWorkPieceId(workpiece.getId());
					processRecord.setWorkPieceCode(workpiece.getCode());
				}
			}

			//添加执行记录的操作人员信息
			String clientIp = request.getRemoteAddr();
			MCUser mcUser = mcUserService.queryLoginUserByClientIp(clientIp);
			if(mcUser!=null) {
				Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
				//processRecord.setProductUserId(employee.getCode());
				processRecord.setProductUserCode(employee.getCode());
				processRecord.setProductUserName(employee.getName());
			}
			processRecord.setMakeDocumentDate(new Date());
			//查找标准节拍
			Float processingBeat = workpieceProcessDeviceSiteMappingService.queryProcessingBeat(processRecord.getWorkPieceCode(), processRecord.getProcessId(), processRecord.getDeviceSiteId());
			processRecord.setStandardBeat((processingBeat==null||processingBeat==0)?ds.getDevice().getProductionUnit().getBeat():processingBeat);
			//processRecord.setStandardBeat((processingBeat==null)?0:processingBeat);
			Map<String,Object> args = new HashMap<>();
			args.put("businessKey", workFlowKeyConfig.getLostTimeWorkflowKey());
			processRecordService.addProcessRecord(processRecord, user, args);
			//启动一个线程，延迟阈值时间间隔运行，查询当前设备是否有新的执行记录，如果没有，则添加损时记录;否则，什么也不做
			CheckLostTimeThread checkLostTimeThread = new CheckLostTimeThread(processRecord.getDeviceSiteId(), ds.getDevice().getProductionUnit().getThreshold(), processRecord.getCollectionDate(), user, args, processRecord.getStandardBeat());
			//CheckLostTimeThread checkLostTimeThread = new CheckLostTimeThread(processRecord.getDeviceSiteId(), sysConfig.getThreshold(), processRecord.getCollectionDate(), user, args, processRecord.getStandardBeat());
			checkLostTimeThread.setClassService(classService);
			checkLostTimeThread.setLostTimeRecordService(lostTimeRecordService);
			checkLostTimeThread.setProcessRecordService(processRecordService);
			executor.execute(checkLostTimeThread);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 更新加工记录
	 * @return
	 */
	@RequestMapping("/updateProcessRecord.do")
	@ResponseBody
	public ModelMap updateProcessRecord(ProcessRecord processRecord) {
		ModelMap modelMap = new ModelMap();
		ProcessRecord c4code = processRecordService.queryByProductNumAndDeviceSiteCode(processRecord.getProductNum(), processRecord.getDeviceSiteCode());
		if(c4code!=null&&!c4code.getId().equals(processRecord.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "生产序号已被使用");
		}else  {
			ProcessRecord pr = processRecordService.queryObjById(processRecord.getId());
			pr.setBatchNumber(processRecord.getBatchNumber());
			pr.setCollectionDate(processRecord.getCollectionDate());
			pr.setCustomerGraphNumber(processRecord.getCustomerGraphNumber());
			pr.setGraphNumber(processRecord.getGraphNumber());
			pr.setNo(processRecord.getNo());
			pr.setWorkSheetId(processRecord.getWorkSheetId());
			pr.setWorkPieceCode(processRecord.getWorkPieceCode());
			pr.setWorkPieceId(processRecord.getWorkPieceId());
			pr.setWorkPieceName(processRecord.getWorkPieceName());
			pr.setVersion(processRecord.getVersion());
			pr.setUnitType(processRecord.getUnitType());
			pr.setProcessCode(processRecord.getProcessCode());
			pr.setProcessId(processRecord.getProcessId());
			pr.setStoveNumber(processRecord.getStoveNumber());
			pr.setStatus(processRecord.getStatus());
			pr.setRealBeat(processRecord.getRealBeat());
			processRecordService.updateObj(processRecord);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询加工记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryProcessRecordById.do")
	@ResponseBody
	public ProcessRecordVO queryProcessRecordById(Long id) {
		return model2vo(processRecordService.queryObjById(id));
	}
	
	private ProcessRecordVO model2vo(ProcessRecord pr) {
		if(pr == null) {
			return null;
		}
		ProcessRecordVO vo = new ProcessRecordVO();
		vo.setId(pr.getId());
		vo.setNo(pr.getNo());
		vo.setBatchNumber(pr.getBatchNumber());
		vo.setClassesCode(pr.getClassesCode());
		vo.setClassesId(pr.getClassesId());
		vo.setClassesName(pr.getClassesName());
		if(pr.getCollectionDate()!=null) {
			vo.setCollectionDate(format.format(pr.getCollectionDate()));
		}
		vo.setCustomerGraphNumber(pr.getCustomerGraphNumber());
		vo.setDeviceSiteCode(pr.getDeviceSiteCode());
		vo.setDeviceSiteId(pr.getDeviceSiteId());
		vo.setDeviceSiteName(pr.getDeviceSiteName());
		vo.setDocumentMaker(pr.getDocumentMaker());
		if(pr.getMakeDocumentDate()!=null) {
			vo.setMakeDocumentDate(format.format(pr.getMakeDocumentDate()));
		}
		vo.setGraphNumber(pr.getGraphNumber());
		vo.setVersion(pr.getVersion());
		vo.setWorkPieceCode(pr.getWorkPieceCode());
		vo.setWorkPieceId(pr.getWorkPieceId());
		vo.setWorkPieceName(pr.getWorkPieceName());
		vo.setWorkSheetId(pr.getWorkSheetId());
		if(pr.getManufactureDate()!=null) {
			vo.setManufactureDate(format.format(pr.getManufactureDate()));
		}
		vo.setUnitType(pr.getUnitType());
		vo.setStoveNumber(pr.getStoveNumber());
		vo.setStandardBeat(pr.getStandardBeat());
		vo.setStatus(pr.getStatus());
		vo.setRealBeat(pr.getRealBeat());
		vo.setRealRecord(pr.getRealRecord());
		vo.setDeleted(pr.getDeleted());
		vo.setProcessCode(pr.getProcessCode());
		vo.setProcessId(pr.getProcessId());
		vo.setProcessName(pr.getProcessName());
		vo.setProductUserName(pr.getProductUserName());
		vo.setProductUserCode(pr.getProductUserCode());
		vo.setProductCount(pr.getProductCount());
		vo.setProductUserId(pr.getProductUserId());
		vo.setShortHalt(pr.getShortHalt());
		vo.setOpcNo(pr.getOpcNo());
		vo.setProductNum(pr.getProductNum());
		return vo;
	}
	
	/**
	 * 根据id删除加工记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteProcessRecord.do")
	@ResponseBody
	public ModelMap deleteProcessRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		ProcessRecord pr = processRecordService.queryObjById(Long.valueOf(id));
		pr.setDeleted(true);
		try {
			processRecordService.deleteObj(Long.valueOf(id));
		}catch(RuntimeException re) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", re.getMessage());
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 根据设备站点id查询当前设备站点下的订单
	 * @param deviceSiteId 设备站点id
	 * @return
	 */
	@RequestMapping("/queryWorkSheetByDeviceSiteId.do")
	@ResponseBody
	public List<WorkSheet> queryWorkSheetByDeviceSiteId(Long deviceSiteId, String q){
		if(q==null) {
			return workSheetService.queryWorkSheetByDeviceSiteId(deviceSiteId);
		}else {
			return workSheetService.queryWorkSheetsByDeviceSiteIdAndConditions(deviceSiteId, q);
		}
	}
	/**
	 * 根据工单id查找工单 详情
	 * @param workSheetId
	 * queryWorkSheetDetailsByWorkSheetId
	 * @return
	 */
	@RequestMapping("/queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId.do")
	@ResponseBody
	public List<WorkSheetDetail> queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId(Long workSheetId, Long deviceSiteId){
		List<WorkSheetDetail> list = workSheetDetailService.queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId(workSheetId,deviceSiteId);
		Map<Long, WorkSheetDetail> map = new HashMap<>();
		for(WorkSheetDetail detail : list) {
			map.put(detail.getProcessId(), detail);
		}
		Collection<WorkSheetDetail> c = map.values();
		Iterator<WorkSheetDetail> it = c.iterator();
		List<WorkSheetDetail> details = new ArrayList<>();
		while(it.hasNext()) {
			details.add(it.next());
		}
		return details;
	}
	/**
	 * 根据工单详情查询
	 * @param workSheetId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProcessRecordByWorkSheetId.do")
	@ResponseBody
	public ModelMap queryProcessRecordByWorkSheetId(Long workSheetId, @RequestParam(value="rows",defaultValue="20") Integer rows, @RequestParam(defaultValue="1") Integer page) {
		Pager<ProcessRecord> pager = processRecordService.queryObjs("from ProcessRecord pr where pr.workSheetId=?0 and pr.deleted=?1", page, rows, new Object[] {workSheetId,false});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
} 
