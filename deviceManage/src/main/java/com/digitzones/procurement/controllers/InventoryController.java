package com.digitzones.procurement.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.controllers.InspectionRecordDetailController;
import com.digitzones.dto.InspectionRecordDetailDto;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.Employee;
import com.digitzones.model.InspectionRecord;
import com.digitzones.model.InspectionRecordDetail;
import com.digitzones.model.Pager;
import com.digitzones.model.Processes;
import com.digitzones.model.ProcessesParametersMapping;
import com.digitzones.model.User;
import com.digitzones.model.WorkSheet;
import com.digitzones.model.WorkSheetDetail;
import com.digitzones.model.WorkpieceProcessParameterMapping;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.model.InventoryProcessMapping;
import com.digitzones.procurement.service.IInventoryProcessMappingService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IInspectionRecordService;
import com.digitzones.service.IProcessesParametersMappingService;
import com.digitzones.service.IProcessesService;
import com.digitzones.service.IUserService;
import com.digitzones.service.IWorkSheetDetailService;
import com.digitzones.service.IWorkSheetService;
import com.digitzones.service.IWorkpieceProcessDeviceSiteMappingService;
import com.digitzones.service.IWorkpieceProcessParameterMappingService;
import com.digitzones.util.StringUtil;
import com.digitzones.vo.InspectionRecordVo;

/**
 * 物料控制器
 * @author zhuyy430
 *
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "QC-";
	@Autowired
	private IInventoryService inventoryService;
	@Autowired
	private IProcessesService processesService;
	@Autowired
	private IInventoryProcessMappingService inventoryProcessMappingServiceImpl;
	@Autowired
	private IWorkpieceProcessDeviceSiteMappingService workpieceProcessDeviceSiteMappingService;
	@Autowired
	private IWorkSheetDetailService workSheetDetailService;
	@Autowired
	private IWorkpieceProcessParameterMappingService workpieceProcessParameterMappingService;
	@Autowired
	private IProcessesParametersMappingService processesParametersMappingService;
	@Autowired
	private IInventoryProcessMappingService inventoryProcessMappingService;
	@Autowired
	private IEmployeeService EmployeeService;
	@Autowired
	private IInspectionRecordService inspectionRecordService;
	@Autowired
	private IWorkSheetService workSheetService;
	@Autowired
	private IMCUserService mcUserService;
	/**
	 * 根据工件id查找工序和参数等信息
	 * @return
	 */
	@RequestMapping("/queryWorkpieceProcessParameterMappingsByWorkpieceCode.do")
	@ResponseBody
	public ModelMap queryWorkpieceProcessParameterMappingsByWorkpieceCode(String InventoryCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {

		Pager<WorkpieceProcessParameterMapping> pager = workpieceProcessParameterMappingService.queryWorkpieceProcessParameterMappingByWorkpieceCode(InventoryCode, rows, page);
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}

    /**
     * 根据id查询'工件工序参数'
     * @param id
     * @return
     */
    @RequestMapping("/queryWorkpieceProcessParameterMappingById.do")
    @ResponseBody
    public WorkpieceProcessParameterMapping queryWorkpieceProcessParameterMappingById(Long id) {
        WorkpieceProcessParameterMapping workpieceProcessParameterMapping = workpieceProcessParameterMappingService.queryObjById(id);
        return workpieceProcessParameterMapping;
    }

	/**
	 * 根据物料编码查找物料信息
	 * @param inventoryCode
	 * @return
	 */
	@RequestMapping("queryByInventoryCode.do")
    public Inventory queryByInventoryCode(String inventoryCode){
    	return inventoryService.queryByProperty("code",inventoryCode);
	}

    /**
     * 更新
     * @return
     */
    @RequestMapping("/updateWorkpieceProcessParameterMapping.do")
    @ResponseBody
    public ModelMap updateWorkpieceProcessParameterMapping(WorkpieceProcessParameterMapping workpieceProcessParameterMapping) {
        ModelMap modelMap = new ModelMap();
        WorkpieceProcessParameterMapping wpdsm = workpieceProcessParameterMappingService.queryObjById(workpieceProcessParameterMapping.getId());
        wpdsm.setLowLine(workpieceProcessParameterMapping.getLowLine());
        wpdsm.setNote(workpieceProcessParameterMapping.getNote());
        wpdsm.setUpLine(workpieceProcessParameterMapping.getUpLine());
        wpdsm.setUnit(workpieceProcessParameterMapping.getUnit());
        wpdsm.setStandardValue(workpieceProcessParameterMapping.getStandardValue());
        workpieceProcessParameterMappingService.updateObj(wpdsm);
        modelMap.addAttribute("success", true);
        modelMap.addAttribute("msg", "编辑成功!");
        return modelMap;
    }
    /**
     * 删除工件，工序和参数的关联
     * @param id
     * @return
     */
    @RequestMapping("/deleteProcessParameterFromWorkpiece.do")
    @ResponseBody
    public ModelMap deleteProcessParameterFromWorkpiece(String id) {
        ModelMap modelMap = new ModelMap();
        if(id.contains("'")) {
            id = id.replace("'", "");
        }
        String[] ids = id.split(",");
        for(String Id:ids){
            workpieceProcessParameterMappingService.deleteObj(Long.valueOf(Id));
        }
        modelMap.addAttribute("message", "删除成功！");
        modelMap.addAttribute("statusCode", 200);
        modelMap.addAttribute("title", "操作提示");
        return modelMap;
    }
	/**
	 * 查询物料信息
	 *
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("queryInventoryByTyClassCode.do")
	public ModelMap queryInventory(String code, @RequestParam(value = "rows", defaultValue = "20") Integer rows, @RequestParam(defaultValue = "1") Integer page,
								   String cInvCode, String cInvName, String cInvStd, String cEngineerFigNo) {
		ModelMap modelMap = new ModelMap();
		String hql="";
		List<Object> paramsList = new ArrayList<>();
		int i;
		if (!StringUtils.isEmpty(code)) {
			hql += "from Inventory i where i.cInvCCode=?0 ";
			paramsList.add(code);
			 i = 1;
		}else{
			hql += "from Inventory i where 1=1 ";
			 i = 0;
		}





		if (!StringUtils.isEmpty(cInvCode)) {
			hql += " and i.code like ?" + (i++);
			paramsList.add("%" + cInvCode + "%");
		}
		if (!StringUtils.isEmpty(cInvName)) {
			hql += " and i.name like ?" + (i++);
			paramsList.add("%" + cInvName + "%");
		}
		if (!StringUtils.isEmpty(cInvStd)) {
			hql += " and i.cInvStd like ?" + (i++);
			paramsList.add("%" + cInvStd + "%");
		}
		if (!StringUtils.isEmpty(cEngineerFigNo)) {
			hql += " and i.cEngineerFigNo like ?" + (i++);
			paramsList.add("%" + cEngineerFigNo + "%");
		}
		Pager<Inventory> pager = inventoryService.queryObjs(hql, page, rows, paramsList.toArray());
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}


	/**
	 * 查询所有物料
	 */
	@RequestMapping("/queryInventorys.do")
	@ResponseBody
	public List<Inventory> queryInventorys(){
		return inventoryService.queryAllInventory();
	}
	/**
	 * 工艺流转
	 */
	@RequestMapping("/queryWorkpieceProcessDeviceSiteMapping.do")
	@ResponseBody
	public List<WorkSheetDetail> queryWorkpieceProcessDeviceSiteMapping(String inventoryCode,String productionUnitCode,int count,HttpServletRequest request){
		HttpSession session = request.getSession();
		List<WorkSheetDetail> workSheetDetails=new ArrayList<>();
		workSheetDetailService.buildWorkSheetDetailListInMemoryByWorkpieceId(count,inventoryCode,productionUnitCode,workSheetDetails);
		if(!workSheetDetails.isEmpty()) {
			session.setAttribute(Constant.WORKSHEETDETAIL,workSheetDetails);
		}
		return workSheetDetails;
	}


	/**
	 * 为物料添加工序
	 * @param InventoryCode
	 * @param processesId
	 * @return
	 */
	@RequestMapping("/addProcesses4Inventory.do")
	@ResponseBody
	public ModelMap addProcesses4Inventory(String InventoryCode,String processesId) {
		ModelMap modelMap = new ModelMap();
		if(processesId!=null) {
			try {
				inventoryProcessMappingServiceImpl.addInventoryProcessMapping(InventoryCode, processesId,false);
			} catch (RuntimeException e) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", e.getMessage());	
				return modelMap;
			}
			
			modelMap.addAttribute("msg", "操作完成！");
			modelMap.addAttribute("success",true);
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 根据工件id查询非当前工件的'工件工序参数'信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryOtherProcessParameters.do")
	@ResponseBody
	public ModelMap queryOtherProcessParameters(String inventoryCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		String hql = "select pdsm from ProcessesParametersMapping pdsm where pdsm.parameters.id not in ("
				+ "select wpdsm.parameter.id from WorkpieceProcessParameterMapping wpdsm"
				+ "  where wpdsm.workpieceProcess.inventory.code=?0) "
				+ " and pdsm.processes.id in (select wpm.process.id from InventoryProcessMapping wpm where wpm.inventory.code=?0 )";
		Pager<ProcessesParametersMapping> pager = inventoryService.queryObjs(hql, page, rows, new Object[] {inventoryCode});
		ModelMap modelMap = new ModelMap();

		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 为工件添加工序和参数信息
	 * @param inventoryCode
	 * @param processDeviceSiteIds
	 * @return
	 */
	@RequestMapping("/addProcessParameter4Workpiece.do")
	@ResponseBody
	public ModelMap addProcessParameter4Workpiece(String inventoryCode,String processDeviceSiteIds) {
		ModelMap modelMap = new ModelMap();
		if(processDeviceSiteIds!=null) {
			if(processDeviceSiteIds.contains("[")) {
				processDeviceSiteIds = processDeviceSiteIds.replace("[", "");
			}
			if(processDeviceSiteIds.contains("]")) {
				processDeviceSiteIds = processDeviceSiteIds.replace("]", "");
			}

			String[] idArray = processDeviceSiteIds.split(",");
			for(int i = 0;i<idArray.length;i++) {

				ProcessesParametersMapping pdsm = processesParametersMappingService.queryObjById(Long.valueOf(idArray[i]));
				InventoryProcessMapping wpm = inventoryProcessMappingService.queryByInventoryCodeAndProcessId(inventoryCode,pdsm.getProcesses().getId());
				WorkpieceProcessParameterMapping wpdsm = new WorkpieceProcessParameterMapping();
				wpdsm.setParameter(pdsm.getParameters());
				wpdsm.setWorkpieceProcess(wpm);

				workpieceProcessParameterMappingService.addObj(wpdsm);
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 为物料添加工艺路线
	 * @param InventoryCode
	 * @return
	 */
	@RequestMapping("/addCraftsRoute4Inventory.do")
	@ResponseBody
	public ModelMap addCraftsRoute4Inventory(String InventoryCode,String craftsRouteId) {
		ModelMap modelMap = new ModelMap();
		List<Processes> plist = processesService.queryProcessesByCraftsId(craftsRouteId);
		if(!CollectionUtils.isEmpty(plist)){
			String processesId= "";
			for(int i=0;i<plist.size();i++){
				processesId+=plist.get(i).getId()+",";
				if(i==plist.size()-1){
					processesId=processesId.substring(0, processesId.length()-1);
					processesId="["+processesId+"]";
				}
			}
				try {
					inventoryProcessMappingServiceImpl.addInventoryProcessMapping(InventoryCode, processesId,true);
				} catch (RuntimeException e) {
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", e.getMessage());	
					return modelMap;
				}
				
				modelMap.addAttribute("msg", "操作完成！");
				modelMap.addAttribute("success",true);
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 删除工序和物料的关联
	 * @param InventoryCode
	 * @param processesId
	 * @return
	 */
	@RequestMapping("/deleteProcessesFromInventory.do")
	@ResponseBody
	public ModelMap deleteProcessesFromInventory(String InventoryCode,String processesId) {
		if(processesId.contains("'")) {
			processesId = processesId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		inventoryProcessMappingServiceImpl.deleteByInventoryCodeAndProcessId(InventoryCode, Long.valueOf(processesId));
		modelMap.addAttribute("message", "删除成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	
	/**
	 * 上移物料工序关联的工艺路线
	 * @param InventoryCode
	 * @param processesId
	 * @return
	 */
	@RequestMapping("/updateShiftUpProcessRoute.do")
	@ResponseBody
	public ModelMap updateShiftUpProcessRoute(String InventoryCode,String processesId) {
		if(processesId.contains("'")) {
			processesId = processesId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			inventoryProcessMappingServiceImpl.updateShiftUpProcessRoute(InventoryCode, Long.valueOf(processesId));
		} catch (RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("title", "操作提示");
			return modelMap;
		}
		
		modelMap.addAttribute("message", "移动成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	
	/**
	 * 下移物料工序关联的工艺路线
	 * @param InventoryCode
	 * @param processesId
	 * @return
	 */
	@RequestMapping("/updateShiftDownProcessRoute.do")
	@ResponseBody
	public ModelMap updateShiftDownProcessRoute(String InventoryCode,String processesId) {
		if(processesId.contains("'")) {
			processesId = processesId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			inventoryProcessMappingServiceImpl.updateShiftDownProcessRoute(InventoryCode, Long.valueOf(processesId));
		} catch (RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", e.getMessage());	
			modelMap.addAttribute("title", "操作提示");
			return modelMap;
		}
		modelMap.addAttribute("message", "移动成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	
	/**
	 * 根据工件id查找所有参数等信息
	 * @return
	 */
	@RequestMapping("/queryAllWorkpieceProcessParameterMappingsByWorkpieceCode.do")
	@ResponseBody
	public List<WorkpieceProcessParameterMapping> queryAllWorkpieceProcessParameterMappingsByWorkpieceCode(String InventoryCode,String processCode,HttpSession session) {
		List<WorkpieceProcessParameterMapping> pager = workpieceProcessParameterMappingService.
				queryAllWorkpieceProcessParameterMappingByWorkpieceCodeAndProcessCode(InventoryCode,processCode);
        return pager;
	}
	/**
	 * 根据条件查询工件信息
	 * @param q
	 * @return
	 */
	@RequestMapping("/queryWorkpieces.do")
	@ResponseBody
	public List<Inventory> queryWorkpieces(String q){
		if(q!=null && !"".equals(q.trim())) {
			return inventoryService.queryAllWorkpieces(q);
		}else {
			return inventoryService.queryAllWorkpieces();
		}
	}
	/**
	 * 根据代码查询工件信息
	 * @param q
	 * @return
	 */
	@RequestMapping("/queryWorkpiecesByCode.do")
	@ResponseBody
	public Inventory queryWorkpiecesByCode(String code){
		return inventoryService.queryByProperty("code", code);
	}
    /**
     * 保存检验单
     * @return
     */
    @RequestMapping("/saveInspectionRecord.do")
    public ModelMap saveInspectionRecord(String no,String workSheetId,String processCode ,String employeeCode,String itemIds,String results,String notes,String checkValue,HttpServletRequest request){
        InspectionRecord form = new InspectionRecord();
        Employee employee = EmployeeService.queryEmployeeByCode(employeeCode);
        
        WorkSheet workSheet = workSheetService.queryObjById(workSheetId);
        
        Processes processes = processesService.queryByProperty("code", processCode);
        
        Date now = new Date();
        String requestNo = inspectionRecordService.queryMaxFormNoByInspectionDate(now);
        String formNo = "";
        if(!StringUtils.isEmpty(requestNo)){
        	formNo = StringUtil.increment(requestNo);
        }else{
        	formNo = FORMNO_PREFIX + format.format(now) + "000";
        }
        List<InspectionRecordDetailDto> details = new ArrayList<>();
        
        List<InspectionRecordDetail> list = InspectionRecordDetailDtoToInspectionRecordDetail(details);
        
        ModelMap  modelMap = checkInspectionRecord(form,list);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }

        //没有检验单号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            InspectionRecord waf = inspectionRecordService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","检验单号已被使用！");
                return modelMap;
            }
            if(employee!=null) {
               form.setInspectorCode(employee.getCode());
               form.setInspectorName(employee.getName());
            }else{
            	String ip = request.getRemoteAddr();
        		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
                form.setInspectorCode(mcUser.getId() + "");
                form.setInspectorName(mcUser.getUsername());
            }
            inspectionRecordService.addInspectionRecord(form,list);
        }else{//有检验单号，说明当前为查看操作
            inspectionRecordService.updateInspectionRecord(form,list);
        }
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }

    /**
     *  保存检验单的校验方法
     * @param form 检验单对象
     * @param list 检验单详情对象
     * @return
     */
    private ModelMap checkInspectionRecord(InspectionRecord form,List<InspectionRecordDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="检验单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(form.getInspectionDate()==null){
            msg = "检验日期不能为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(StringUtils.isEmpty(form.getNo())){
            msg ="工单单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
            msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(InspectionRecordDetail detail : list){
                if(detail.getParameterValue()==null || "".equals(detail.getParameterValue())){
                    msg = "参数值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }

                if(detail.getInspectionResult()==null || "".equals(detail.getInspectionResult())){
                    msg = "请选择检验结果!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
            }
        }
        return modelMap;
    }
    
    
	private List<InspectionRecordDetailDto> WorkpieceProcessParameterMappingToInspectionRecordDetail(List<WorkpieceProcessParameterMapping> pager,List<InspectionRecordDetailDto> list){
		for(WorkpieceProcessParameterMapping wpp:pager){
			InspectionRecordDetailDto detail = new InspectionRecordDetailDto();
			detail.setId(wpp.getId()+"");
			detail.setParameterCode(wpp.getParameter().getCode());
			detail.setParameterName(wpp.getParameter().getName());
			detail.setUnit(wpp.getUnit());
			detail.setUpLine(wpp.getUpLine());
			detail.setLowLine(wpp.getLowLine());
			detail.setStandardValue(wpp.getStandardValue());
			list.add(detail);
		}
		return list;
	}
	private List<InspectionRecordDetail> InspectionRecordDetailDtoToInspectionRecordDetail(List<InspectionRecordDetailDto> list){
		List<InspectionRecordDetail> details = new ArrayList<>();
		for(InspectionRecordDetailDto l:list){
			InspectionRecordDetail detail = new InspectionRecordDetail();
			 BeanUtils.copyProperties(l,detail);
			 details.add(detail);
		}
		return details;
	}
}
