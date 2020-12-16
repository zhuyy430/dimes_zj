package com.digitzones.devmgr.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dto.MaintenancePlanRecordDto;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.devmgr.vo.MaintenancePlanRecordVO;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.RelatedDocumentType;
import com.digitzones.model.User;
import com.digitzones.model.WorkflowTask;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IRelatedDocumentTypeService;
import com.digitzones.service.IUserService;
import com.digitzones.service.IWorkflowTaskService;
import com.digitzones.util.DateStringUtil;
import com.digitzones.util.ExcelUtil;
/**
 * 保养计划记录控制器
 * @author zdq
 * 2018年12月20日
 */
@RestController
@RequestMapping("maintenancePlanRecord")
public class MaintenancePlanRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private IMaintenancePlanRecordService  maintenancePlanRecordService;
	@Autowired
	private IRelatedDocumentService raletedDocumentService;
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IWorkflowTaskService workflowTaskService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	private IRelatedDocumentTypeService relatedDocumentTypeService;
	/**
	 * 查询所有保养计划记录
	 * @param rows
	 * @param page
	 * @param condition 设备代码或设备名称或规格型号
	 * @param maintainType  保养类别代码
	 * @param maintainStatus 保养状态
	 * @param employeeName 责任人
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/queryAllMaintenancePlanRecords.do")
	@SuppressWarnings("unchecked")
	public ModelMap queryAllMaintenancePlanRecords(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String condition,String maintainType,String maintainStatus,String employeeName,String search_from,String search_to) throws ParseException {
		ModelMap modelMap = new ModelMap();
		String hql = "select distinct record from MaintenancePlanRecord record left join  record.device  d left join  d.productionUnit"
				+ " left join  d.projectType inner join MaintenanceUser u on record.id=u.maintenancePlanRecord.id  where 1=1 ";

		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(condition)) {
			hql += "and (d.code like ?" + i;
			hql +=" or d.name like ?" + i;
			hql +=" or d.unitType like ?" + i + ")";
			i++;
			args.add("%" + condition + "%");
		}


		if(!StringUtils.isEmpty(maintainType)) {
			hql+=" and record.maintenanceType.code=?" + (i++);
			args.add(maintainType);
		}

		if(!StringUtils.isEmpty(maintainStatus)) {
			hql += " and record.status =?" + (i++);
			args.add(maintainStatus);
		}

		if(!StringUtils.isEmpty(search_from)) {
			hql += " and record.maintenancedDate >=?" + (i++);
			args.add(format.parse(search_from));
		}

		if(!StringUtils.isEmpty(search_to)) {
			hql += " and record.maintenancedDate <=?" + (i++);
			args.add(format.parse(search_to));
		}

		if(!StringUtils.isEmpty(employeeName)) {
			hql += " and u.name like ?" + (i++);
			args.add("%" + employeeName + "%");
		}
		hql +="  order by record.maintenancedDate,record.maintenanceDate";
		Pager<MaintenancePlanRecord> pager = maintenancePlanRecordService.queryObjs(hql, page, rows, args.toArray());
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager.getData())) {
			for(MaintenancePlanRecord record : pager.getData()) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * 查询当年保养计划记录
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryAllMaintenancePlanRecords4CurrentYear.do")
	@SuppressWarnings("unchecked")
	public ModelMap queryAllMaintenancePlanRecords4CurrentYear(Long deviceId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "select record from MaintenancePlanRecord record left join  record.device  d left join  d.productionUnit"
				+ " left join  d.projectType where record.maintenancedDate between ?0 and ?1 and d.id=?2";

		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		Pager<MaintenancePlanRecord> pager = maintenancePlanRecordService.queryObjs(hql, page, rows,new Object[] {c.getTime(),now,deviceId});
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager.getData())) {
			for(MaintenancePlanRecord record : pager.getData()) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * 查询当年保养计划记录
	 * @return
	 */
	@RequestMapping("/queryAllMaintenancePlanRecords4CurrentYearNoPager.do")
	public void exportAllMaintenancePlanRecords4CurrentYearNoPager(Long deviceId,HttpServletResponse response) {
		List<MaintenancePlanRecord> recordList = maintenancePlanRecordService.queryAllMaintenancePlanRecords4CurrentYear(deviceId);
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(recordList)) {
			for(MaintenancePlanRecord record : recordList) {
				list.add(model2vo(record));
			}
		}

		//设置Excel中的title
		String[] titles = {"计划日期","保养日期","保养类别","责任人","保养状态","完成时间","确认时间","确认人"};
		//存储所有数据
		List<String[]> dataList = new ArrayList<>();
		for(MaintenancePlanRecordVO vo : list) {
			String[] data = {
					vo.getMaintenanceDate(),
					vo.getMaintenancedDate(),
					vo.getMaintenanceType(),
					vo.getEmployeeName(),
					vo.getStatus(),
					vo.getCompleteDate(),
					vo.getConfirmDate(),
					vo.getConfirmName()
			};

			dataList.add(data);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		HSSFWorkbook workBook = ExcelUtil.getHSSFWorkbook("保养记录", titles, dataList);
		try {
			this.setResponseHeader(response, "设备保养记录"+sdf.format(new Date())+".xls");
			OutputStream os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * model转vo
	 * @param record
	 * @return
	 */
	public MaintenancePlanRecordVO model2vo(MaintenancePlanRecord record) {
		format.applyPattern("yyyy-MM-dd");
		MaintenancePlanRecordVO vo = new MaintenancePlanRecordVO();
		BeanUtils.copyProperties(record, vo);
		MaintenanceUser u  = maintenanceUserService.queryPersonInChargeByMaintenancePlanRecordId(record.getId());
		if(u!=null) {
			vo.setEmployeeCode(u.getCode());
			vo.setEmployeeName(u.getName());
			if(u.getCompleteDate()!=null)
				vo.setCompleteDate(format.format(u.getCompleteDate()));
		}
		if(record.getMaintenanceDate()!=null) {
			vo.setMaintenanceDate(format.format(record.getMaintenanceDate()));
		}
		if(record.getMaintenancedDate()!=null) {
			vo.setMaintenancedDate(format.format(record.getMaintenancedDate()));
		}
		if(record.getMaintenanceType()!=null) {
			vo.setMaintenanceType(record.getMaintenanceType().getName());
			vo.setMaintenanceTypeCode(record.getMaintenanceType().getCode());
		}
		vo.setExpectTime(record.getExpectTime());
		return vo;
	}
	/**
	 * 根据设备编码查找保养计划记录
	 * @param deviceCode
	 * @param rows
	 * @param page
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMaintenancePlanRecordByDeviceCode.do")
	public ModelMap queryMaintenancePlanRecordByDeviceCode(String deviceCode,@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) throws ParseException {
		ModelMap modelMap = new ModelMap();
		int i = 1;
		List<Object> paramList = new ArrayList<>();
		String search_from = params.get("search_from");
		String search_to = params.get("search_to");
		String search_class = params.get("search_class");
		String search_status = params.get("search_status");
		String hql = "from MaintenancePlanRecord record  where record.device.code=?0";

		paramList.add(deviceCode);
		if(!StringUtils.isEmpty(search_from)) {
			hql += " and maintenanceDate>=?" + (i++);
			paramList.add(format.parse(search_from));
		}
		if(!StringUtils.isEmpty(search_to)) {
			hql += " and maintenanceDate<=?" + (i++);
			paramList.add(format.parse(search_to));
		}
		if(!StringUtils.isEmpty(search_class)) {
			hql += " and (classCode like ?" + (i) + " or className like ?" + (i++) + ") ";
			paramList.add("%" + search_class + "%");
		}
		if(!StringUtils.isEmpty(search_status)) {
			hql += " and status=?" + i;
			paramList.add(search_status);
		}
		Pager<MaintenancePlanRecord> pager = maintenancePlanRecordService.queryObjs(hql, page, rows, paramList.toArray());
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager.getData())) {
			for(MaintenancePlanRecord record : pager.getData()) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * 删除保养计划记录
	 * @param ids
	 * @return
	 */
	@RequestMapping("/deleteMaintenancePlanRecord.do")
	public ModelMap deleteMaintenancePlanRecord(String ids) {
		ModelMap modelMap = new ModelMap();
		if(!StringUtils.isEmpty(ids)) {
			if(ids.contains("'")) {
				ids = ids.replace("'", "");
			}
			String[] idArray = ids.split(",");
			maintenancePlanRecordService.deleteMaintenancePlanRecords(idArray);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "提示");
			modelMap.addAttribute("message", "操作成功!");
		}else {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "提示");
			modelMap.addAttribute("message", "操作失败!");
		}
		return modelMap;
	}
	/**
	 * 根据设备编码和月份查找设备保养记录
	 * @param deviceCode
	 * @param month
	 * @return
	 */
	@RequestMapping("/queryMaintenancePlanRecordByDeviceCodeAndMonth.do")
	public ModelMap queryMaintenancePlanRecordByDeviceCodeAndMonth(String deviceCode,String month) {
		ModelMap modelMap = new ModelMap();
		List<MaintenancePlanRecord> list = null;
		List<Date> days = null;
		if(StringUtils.isEmpty(month)) {
			Calendar c = Calendar.getInstance();
			list = maintenancePlanRecordService.queryMaintenancePlanRecordByDeviceCodeAndMonth(deviceCode, c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1));
			days =  new DateStringUtil().generateOneMonthDay(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1));
		}else {
			String[] dates = month.split("-");
			list = maintenancePlanRecordService.queryMaintenancePlanRecordByDeviceCodeAndMonth(deviceCode, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
			days =  new DateStringUtil().generateOneMonthDay(month);
		}
		List<MaintenancePlanRecordVO> voList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(list)) {
			for(MaintenancePlanRecord record :list) {
				voList.add(model2vo(record));
			}
		}
		modelMap.addAttribute("records", voList);
		modelMap.addAttribute("days", days);
		return modelMap;
	}
	/**
	 * 根据保养记录id查询保养记录信息
	 * @param id 
	 * @return
	 */
	@GetMapping("/queryMaintenancePlanRecordById.do")
	public MaintenancePlanRecordVO queryMaintenancePlanRecordById(Long id) {
		MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(id);
		return model2vo(record);
	}
	/**
	 * 指定责任人
	 * @param record
	 * @return
	 */
	@RequestMapping("/assignPersonInCharge.do")
	public ModelMap assignPersonInCharge(MaintenancePlanRecordDto record,Principal principal) {
		ModelMap modelMap = new ModelMap();
		if(StringUtils.isEmpty(record.getEmployeeCode()) || StringUtils.isEmpty(record.getEmployeeName())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请指定责任人!");
			return modelMap;
		}
		//更新保养计划状态
		MaintenancePlanRecord planRecord = maintenancePlanRecordService.queryObjById(record.getId());
		
		if(!Constant.Status.MAINTENANCEPLAN_PLAN.equals(planRecord.getStatus())
				&&!Constant.Status.MAINTENANCEPLAN_RECEIPT.equals(planRecord.getStatus())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "【"+ planRecord.getStatus() + "】状态下无法更改责任人!");
			return modelMap;
		}
		
		planRecord.setStatus(Constant.Status.MAINTENANCEPLAN_RECEIPT);
		planRecord.setMaintenanceDate(record.getMaintenanceDate());
		maintenancePlanRecordService.updateObj(planRecord);
		//添加保养责任人
		User user = userService.queryByProperty("username", principal.getName());
		Employee employee = user.getEmployee();
		MaintenanceUser maintenanceUser = new MaintenanceUser();
		if(employee==null) {
			employee = new Employee();
			employee.setCode(user.getUsername());
			employee.setName(user.getUsername());
		}
		maintenanceUser.setDispatchUsercode(employee.getCode());
		maintenanceUser.setDispatchUsername(employee.getName());
		maintenanceUser.setMaintenancePlanRecord(planRecord);
		maintenanceUser.setCode(record.getEmployeeCode());
		maintenanceUser.setName(record.getEmployeeName());
		maintenanceUser.setDispatchDate(new Date());
		maintenanceUser.setOrderType("人工派单");
		maintenanceUserService.addMaintenanceUser(maintenanceUser,employee);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 批量责任人
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/assignBatchPersonInCharge.do")
	public ModelMap assignBatchPersonInCharge(@RequestParam Map<String,Object> params,Principal principal) throws ParseException {
		ModelMap modelMap = new ModelMap();
		String from = (String) params.get("from");
		String to = (String) params.get("to");
		String employeeCode = (String) params.get("employeeCode");
		String employeeName = (String) params.get("employeeName");
		String maintenanceType = (String) params.get("maintenanceType");
		if(StringUtils.isEmpty(from)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择开始日期!");
			return modelMap;
		}
		if(StringUtils.isEmpty(to)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择结束日期!");
			return modelMap;
		}
		if(StringUtils.isEmpty(employeeCode) || StringUtils.isEmpty(employeeName)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择责任人!");
			return modelMap;
		}
		if(StringUtils.isEmpty(maintenanceType)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择保养类型!");
			return modelMap;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.put("from", sdf.parse(from + " 00:00:00"));
		params.put("to", sdf.parse(to + " 23:59:59"));
		maintenancePlanRecordService.update4AssignBatchPersonInCharge(params,userService.queryByProperty("username", principal.getName()));
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 查找设备分布图(保养图片)
	 * @param deviceId 设备id(raletedId)
	 * @param docTypeCode 与文档类别编码对应的保养类别编码(文档类别编码一定要与保养类别编码相同，在添加时手动设定)
	 * @return
	 */
	@RequestMapping("/queryMaintenanceImages.do")
	public List<RelatedDocument> queryMaintenanceImages(Long deviceId,String docTypeCode){
		return raletedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, docTypeCode, deviceId);
	}
	/**
	 * 确认
	 * @return
	 */
	@RequestMapping("/confirm.do")
	public ModelMap confirm(String id,Principal principal){
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		MaintenancePlanRecord item = maintenancePlanRecordService.queryObjById(Long.valueOf(id));
		if(item.getConfirmDate()!=null) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "不允许重复确认!");
			return modelMap;
		}
		User user = userService.queryByProperty("username", principal.getName());
		Employee employee = user.getEmployee();
		if(employee==null) {
			employee = new Employee();
			employee.setCode(user.getUsername());
			employee.setName(user.getUsername());

		}
		item.setConfirmDate(new Date());
		maintenancePlanRecordService.confirm(item,employee);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已确认!");
		return modelMap;
	}
	/**
	 * 根据业务键查找流程信息
	 * @param businessKey
	 * @return
	 */
	@RequestMapping("/queryWorkflow.do")
	@ResponseBody
	public ModelMap queryWorkflow(String businessKey) {
		ModelMap modelMap = new ModelMap();
		businessKey+=":"+MaintenancePlanRecord.class.getName();
		List<Map<String, Object>> data = new ArrayList<>();

		List<WorkflowTask> taskList = workflowTaskService.queryByBusinessKey(businessKey);

		if(taskList!=null && taskList.size()>0) {
			Map<String,Object> map = new HashMap<>();
			String year = "";
			List<Map<String,Object>> list = new ArrayList<>();
			for(WorkflowTask task : taskList) {
				Date date = task.getCreateDate();
				if(date!=null ) {
					format.applyPattern("yyyy");
					if(!year.equals(format.format(date))) {
						map = new HashMap<>();
						list = new ArrayList<>();
						year = format.format(date);
						map.put("year",format.format(date));
						data.add(map);
					}
				}

				Map<String,Object> m = new HashMap<>();
				format.applyPattern("MM-dd HH:mm");
				m.put("date", format.format(date));
				m.put("intro", task.getName());
				/*if(task.getEndTime()!=null) {
					m.put("highlight", "highlight");
				}*/
				m.put("version","测试");
				list.add(m);
				map.put("list", list);

				List<String> more = new ArrayList<>();
				more.add(task.getDescription());
				m.put("more",more);
			}
		}
		modelMap.addAttribute("data", data);
		return modelMap;
	}

	/**
	 * 车间确认或维修完成(接收报修单)
	 * 
	 * @return
	 */
	@RequestMapping("/updateMaintenancePlanRecordStatusById.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrder(String id, String status, Principal principal) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();

		String username = principal.getName();
		User user = userService.queryByProperty("username", username);

		// 接收报修单
		if (status.equals(Constant.Status.MAINTENANCEPLAN_RECEIPT)||status.equals(Constant.Status.MAINTENANCEPLAN_PLAN)) {// 报修单为"保养中"
			MaintenanceUser mu = maintenanceUserService
					.queryObjById(Long.parseLong(id));

			//Employee employee = employeeService.queryEmployeeByCode(maintenanceUser.getCode());
			MaintenancePlanRecord record = mu.getMaintenancePlanRecord();
			
			if(user.getEmployee()==null){
				modelMap.addAttribute("message", "只能接派给您的保养单！");
				modelMap.addAttribute("statusCode", 110);
				modelMap.addAttribute("title", "操作提示!");
				return modelMap;
			}else if(!(record.getStatus().equals("待接单")||record.getStatus().equals("保养中"))){
				modelMap.addAttribute("message", "只能在状态为待接单或保养中时能接单!");
				modelMap.addAttribute("statusCode", 110);
				modelMap.addAttribute("title", "操作提示!");
				return modelMap;
			}else {
				if(user.getEmployee().getCode().equals(mu.getCode())) {
					if(mu.getReceiptDate()!=null&&!mu.getReceiptDate().equals("")) {
						modelMap.addAttribute("message", "您已接单，无需重复接单");
						modelMap.addAttribute("statusCode", 110);
						modelMap.addAttribute("title", "操作提示!");
						return modelMap;
					}else {
						mu.setReceiptDate(new Date());
						record.setStatus(Constant.Status.MAINTENANCEPLAN_MAINTENANCING);
						maintenanceUserService.updateObj(mu);
						maintenancePlanRecordService.confirmMaintenance(record, user.getEmployee().getName(), Constant.Status.MAINTENANCEPLAN_MAINTENANCING);
						MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code",user.getEmployee().getCode());
						maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.MAINTAIN,
								user.getEmployee().getName());
					}
				}else {
					modelMap.addAttribute("message", "只能接派给您的保养单！");
					modelMap.addAttribute("statusCode", 110);
					modelMap.addAttribute("title", "操作提示!");
					return modelMap;
				}			
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "操作成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}

		MaintenancePlanRecord MaintenancePlan = maintenancePlanRecordService.queryObjById(Long.parseLong(id));
		// 车间确认
		if (MaintenancePlan.getStatus().equals(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED)
				&& status.equals(Constant.Status.MAINTENANCEPLAN_COMPLETE)) {
			MaintenancePlan.setStatus(status);
			MaintenancePlan.setMaintenancedDate(new Date());
			if(null!=user.getEmployee()){
				maintenancePlanRecordService.confirmMaintenance(MaintenancePlan, user.getEmployee().getName(), status);
			}else{
				maintenancePlanRecordService.confirmMaintenance(MaintenancePlan,username, status);
			}
			List<MaintenanceUser> rList = maintenanceUserService.queryMaintenanceUserByRecordId(Long.parseLong(id));
			for(MaintenanceUser  list:rList){
				list.setCompleteDate(new Date());
				maintenanceUserService.updateObj(list);
			}

			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "操作成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		} else if (MaintenancePlan.getStatus().equals(Constant.Status.MAINTENANCEPLAN_MAINTENANCING)
				&& status.equals(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED)) {  //维修完成
			MaintenancePlan.setStatus(status);
			if(null!=user.getEmployee()){
				maintenancePlanRecordService.confirmMaintenance(MaintenancePlan, user.getEmployee().getName(), status);
			}else{
				maintenancePlanRecordService.confirmMaintenance(MaintenancePlan, username, status);
			}
			List<MaintenanceUser> mList = maintenanceUserService.queryMaintenanceUserByRecordId(Long.parseLong(id));
			for (MaintenanceUser m : mList) {
				//m.setCompleteDate(new Date());
				if(m.getReceiptDate()!=null){
					Double MaintenanceTime =(double) Math.round(((double)(new Date().getTime()-m.getReceiptDate().getTime())/60000) * 100) / 100; 
					m.setMaintenanceTime(MaintenanceTime);
				}
				maintenanceUserService.updateObj(m);
				MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", m.getCode());
				maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY,
						Constant.ReceiveType.SYSTEMGASSIGN);
			}
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "操作成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("message", "该记录不可完成或确认!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 返修
	 */
	@RequestMapping("/updateMaintenancePlanRecordConfirmAndReword.do")
	@ResponseBody
	public ModelMap updateDeviceRepairForConfirmAndReword(String id,String status,Principal principal) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		MaintenancePlanRecord MaintenancePlan = maintenancePlanRecordService.queryObjById(Long.parseLong(id));
		if(!MaintenancePlan.getStatus().equals(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED)){
			modelMap.addAttribute("message", "该记录不可返修!");
			modelMap.addAttribute("success", false);
			return modelMap;
		}
		String username = principal.getName();
		User user=userService.queryUserByUsername(username);
		if(status.equals(Constant.ReceiveType.REWORK)){
			MaintenancePlan.setStatus(Constant.Status.MAINTENANCEPLAN_RECEIPT);
		}
		MaintenancePlan.setConfirmDate(null);
		if(null!=user.getEmployee()){
			maintenancePlanRecordService.confirmMaintenance(MaintenancePlan, user.getEmployee().getName(), Constant.ReceiveType.REWORK);
		}else{
			maintenancePlanRecordService.confirmMaintenance(MaintenancePlan, username, Constant.ReceiveType.REWORK);
		}

		//查询协助人
		List<MaintenanceUser> maintenanceUserList = maintenanceUserService.queryHelpMaintenanceUserByMaintenancePlanRecordId(Long.parseLong(id));
		//清空协助人
		if(maintenanceUserList!=null&&maintenanceUserList.size()>0) {
			for(MaintenanceUser m:maintenanceUserList) {
				maintenanceUserService.deleteObj(m.getId());
			}
		}
		//查询负责人
		List<MaintenanceUser> mList = maintenanceUserService.queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(Long.parseLong(id));
		if(mList!=null&&mList.size()>0) {
			for(MaintenanceUser msr:mList) {
				msr.setReceiptDate(null);
				maintenanceUserService.updateObj(msr);
			}
		}
		modelMap.addAttribute("message", "操作成功");
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 设备保养接单
	 * @param maintenancePlanRecordId
	 * @param principal
	 * @return
	 */
	@RequestMapping("receipt.do")
	public ModelMap receipt(Long maintenancePlanRecordId,Principal principal) {
		String username = principal.getName();
		User user = userService.queryByProperty("username", username);
		Employee employee = user.getEmployee();
		MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(maintenancePlanRecordId);
		record.setStatus(Constant.Status.MAINTENANCEPLAN_MAINTENANCING);
		ModelMap modelMap = new ModelMap();
		if(employee==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "只能接派给您的保养单！");
			return modelMap;
		}else {
			List<MaintenanceUser> muList=maintenanceUserService.queryMaintenanceUserByRecordId(maintenancePlanRecordId);
			if(!CollectionUtils.isEmpty(muList)) {
				boolean flag = false;
				for(MaintenanceUser mu : muList) {
					if(mu.getName().equals(employee.getName())) {
						mu.setReceiptDate(new Date());
						maintenanceUserService.updateObj(mu);
						maintenancePlanRecordService.confirmMaintenance(record, user.getEmployee().getName(), Constant.Status.MAINTENANCEPLAN_MAINTENANCING);
						flag=true;
						break;
					}
				}
				//非派给当前用户的单
				if(!flag) {
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", "只能接派给您的保养单！");
					return modelMap;
				}
			}else {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "该单还没有派单!");
				return modelMap;
			}
		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 根据保养ID获取文档类别
	 * @param maintenancePlanRecordId
	 * @return
	 */
	@RequestMapping("/queryDocTypeByMaintenancePlanRecordId.do")
	public RelatedDocumentType queryDocTypeByMaintenancePlanRecordId(Long maintenancePlanRecordId) {
		MaintenancePlanRecord m = maintenancePlanRecordService.queryObjById(maintenancePlanRecordId);
		RelatedDocumentType doctype = relatedDocumentTypeService.queryByProperty("code", m.getMaintenanceType().getCode());
		return doctype;
	}
}
