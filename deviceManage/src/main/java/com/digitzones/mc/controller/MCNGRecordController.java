package com.digitzones.mc.controller;
import com.alibaba.druid.util.StringUtils;
import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.constants.Constant;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.model.MCWarehouse;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.mc.service.IMCWarehouseService;
import com.digitzones.model.*;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IWarehouseService;
import com.digitzones.service.*;
import com.digitzones.util.StringUtil;
import com.digitzones.xml.JobBookingSlipUtil;
import com.digitzones.xml.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/mcNGRecord")
public class MCNGRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	@Autowired
	private INGRecordService ngRecordService;
	@Autowired
	private INGReasonService ngReasonService;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IWorkSheetService workSheetService;
	@Autowired
	private JobBookingSlipUtil jobBookingSlipUtil;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMCWarehouseService mcWarehouseService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IEmployeeService employeeService;
	@RequestMapping("/queryNGRecordsByDate.do")
	@ResponseBody
	public List<NGRecord> queryNGRecordsByDate(String deviceSiteCode,String startTime,String endTime,String workNo,String NGCode,String BatchNumber) throws ParseException{
		Date now = new Date();
		if(!StringUtils.isEmpty(endTime)){
			now=format.parse(endTime+" 23:59:59");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		c.add(Calendar.DATE, -10);
		Date start = c.getTime();
		if(!StringUtils.isEmpty(startTime)){
			start=format.parse(startTime+" 00:00:00");
		}
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.add(Calendar.DATE, 10);
		Date end = c.getTime();
		if(!StringUtils.isEmpty(endTime)){
			end=format.parse(endTime+" 23:59:59");
		}
		
		String hql = "from NGRecord record where record.deviceSiteCode=?2 and record.occurDate between ?0 and ?1 and record.deleted=?3";
		List<Object> param = new ArrayList<>();
		param.add(start);
		param.add(end);
		param.add(deviceSiteCode);
		param.add(false);
		int i = 4;
		if(!StringUtils.isEmpty(workNo)) {
			hql += " and no like ?" + i++ ;
			param.add("%" + workNo + "%");
		}
		if(!StringUtils.isEmpty(NGCode)) {
			hql += " and ngReasonCode like ?" + i++;
			param.add("%" + NGCode + "%");
		}
		if(!StringUtils.isEmpty(BatchNumber)) {
			hql += " and batchNumber like ?" + i++;
			param.add("%" + BatchNumber + "%");
		}
		
		hql+= " order by record.occurDate desc";
		List<NGRecord> list = ngRecordService.queryNgRecordsByDate(hql,param);
		return  list;
	}
	/**
	 * 根据ng代码查询ng对象
	 * @param ngReasonCode
	 * @return
	 */
	@RequestMapping("/queryNGReasonByCode.do")
	@ResponseBody
	public NGReason queryNGReasonByCode(String ngReasonCode) {
		return ngReasonService.queryByProperty("ngCode", ngReasonCode);
	}
	/**
	 * 添加不合格品记录
	 * @return
	 */
	@RequestMapping("/addNGRecord.do")
	@ResponseBody
	public ModelMap addNGRecord(NGRecord ngRecord,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryByProperty("code",ngRecord.getDeviceSiteCode());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}else {
			ngRecord.setDeviceSiteId(ds.getId());
			ngRecord.setDeviceSiteName(ds.getName());
		}
		//获取当前登录用户
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		//Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		User user = userService.queryByProperty("username", mcUser.getUsername());
		modelMap.put("businessKey", workFlowKeyConfig.getNgWorkflowKey());
		ngRecordService.addNGRecord(ngRecord, user);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询不合格记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryNGRecordById.do")
	@ResponseBody
	public NGRecord queryNGRecordById(Long id) {
		NGRecord record = ngRecordService.queryObjById(id);
		return record;
	}
	
	/**
	 * 编辑不合格品记录
	 * @return
	 */
	@RequestMapping("/updateNGRecord.do")
	@ResponseBody
	public ModelMap updateNGRecord(NGRecord record,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		NGRecord ng = ngRecordService.queryObjById(record.getId());
		if(ng.getInWarehouse()!=null&&ng.getInWarehouse()){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "该NG记录已入库，不允许修改！");
			return modelMap;
		}
		ng.setNgCount(record.getNgCount());
		/*if(ng.getNgReasonCode()!=null) {
			NGReason ngReason = ngReasonService.queryByProperty("ngCode", record.getNgReasonCode());
			ng.setNgReasonCode(ngReason.getNgCode());
			ng.setNgReasonId(record.getNgReasonId());
		}*/
		ng.setNgReasonCode(record.getNgReasonCode());
		ng.setNgReasonId(record.getNgReasonId());
		ng.setNgReason(record.getNgReason());
		ng.setProcessingMethod(record.getProcessingMethod());
		ngRecordService.updateObj(ng);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	
	/**
	 * 根据ids删除不合格品记录
	 * @param ids
	 * @return
	 */
	@RequestMapping("/deleteNGRecord.do")
	@ResponseBody
	public ModelMap deleteNGRecord(String ids) {
		ModelMap modelMap = new ModelMap();
		if (ids.isEmpty()){
			return null;
		}
		String[] arrIds= ids.split(",");
		for (int i = 0; i < arrIds.length; i++) {
			NGRecord record = ngRecordService.queryObjById(Long.parseLong(arrIds[i]));
			if(record.getInWarehouse()!=null&&record.getInWarehouse()){
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("statusCode", 300);
				modelMap.addAttribute("title", "操作提示");
				modelMap.addAttribute("message", "创建时间为:"+record.getOccurDate()+" 工单单号为: "+ record.getNo()+" NG记录已入库，不允许删除！");
				return modelMap;
			}

			try {
				ngRecordService.deleteNGRecord(record);
			}catch(RuntimeException e) {
				e.printStackTrace();
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("message", e.getMessage());
				return modelMap;
			}
		}


		modelMap.addAttribute("success", true);
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 审核
	 * @return
	 */
	@RequestMapping("/auditNGRecord.do")
	@ResponseBody
	public ModelMap auditNGRecord(String suggestion,NGRecord nr,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		NGRecord record = ngRecordService.queryObjById(nr.getId());
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		User loginUser = userService.queryByProperty("username", mcUser.getUsername());
		
		if(record.getAuditorId()!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该记录已审核!");
			return modelMap;
		}
		
			record.setAuditDate(new Date());
			record.setAuditorId(nr.getAuditorId());
			record.setAuditorName(employee.getName());
		record.setAuditSuggestion(suggestion);
		modelMap.put(Constant.Workflow.SUGGESTION, suggestion);
		ngRecordService.auditNGRecord(record, loginUser, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 复核
	 * @return
	 */
	@RequestMapping("/reviewNGRecord.do")
	@ResponseBody
	public ModelMap reviewNGRecord(String suggestion,NGRecord nr,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		User loginUser = userService.queryByProperty("username", mcUser.getUsername());
		NGRecord record = ngRecordService.queryObjById(nr.getId());

		if(record.getReviewerId()!=null) {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("message", "该记录已复核!");
			return modelMap;
		}

		
			record.setReviewDate(new Date());
			record.setReviewerId(nr.getReviewerId());
			record.setReviewerName(employee.getName());
		record.setReviewSuggestion(suggestion);
		modelMap.put(Constant.Workflow.SUGGESTION, suggestion);
		ngRecordService.reviewNGRecord(record, loginUser, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 确认
	 * @return
	 */
	@RequestMapping("/confirmNGRecord.do")
	@ResponseBody
	public ModelMap confirmNGRecord(String suggestion,NGRecord nr,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		User loginUser = userService.queryByProperty("username", mcUser.getUsername());
		NGRecord record = ngRecordService.queryObjById(nr.getId());

		if(record.getConfirmUserId()!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该记录已确认!");
			return modelMap;
		}

		
			record.setConfirmDate(new Date());
			record.setConfirmUserId(nr.getConfirmUserId());
			record.setConfirmUsername(employee.getName());
		
		record.setConfirmSuggestion(suggestion);
		modelMap.put(Constant.Workflow.SUGGESTION,suggestion);
		ngRecordService.confirmNGRecord(record, loginUser, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已确认!");
		return modelMap;
	}
	/**
	 * 判断员工是否具有某操作权限
	 * @param employeeCode 员工代码
	 * @param operType 操作类型：audit:审核  review：复核  confirm：确认
	 * @return
	 */
	@RequestMapping("/hasThePermission.do")
	@ResponseBody
	public ModelMap hasThePermission(String employeeCode,String operType) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryUserByEmployeeCode(employeeCode);
		if(user==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该员工不存在!");
			return modelMap;
		}
		List<Role> roles = roleService.queryRolesByUserId(user.getId());
		boolean hasThePermission = false;
		
		String roleName = "";
		switch(operType) {
		case "audit":{
			roleName = Constant.Role.NG_AUDIT_ROLE;
			break;
		}
		case "review":{
			roleName = Constant.Role.NG_REVIEW_ROLE;
			break;
		}
		case "confirm":{
			roleName = Constant.Role.NG_CONFIRM_ROLE;
			break;
		}
		}
		
		for(Role role : roles) {
			if(roleName.equals(role.getRoleName())) {
				hasThePermission = true;
				break;
			}
		}
		//没有该权限
		if(!hasThePermission) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "您没有该权限，请联系管理员!");
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("employeeID", user.getId());
		modelMap.addAttribute("employeeName", user.getUsername());
		return modelMap;
	}

	/**
	 * NG记录入库
	 * @param ids NG记录ids
	 * @return
	 */
	@RequestMapping("/inWarehouseByNGRecord.do")
	@ResponseBody
	public ModelMap inWarehouseByNGRecord(String ids, HttpServletRequest request){
        if (ids.isEmpty()){
            return null;
        }

		ModelMap modelMap=new ModelMap();
		String no="";
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());

		MCWarehouse mcWarehouse = mcWarehouseService.queryMCWarehouse(request.getRemoteAddr());
		Warehouse warehouse = warehouseService.queryObjById(mcWarehouse.getcWhCode());

		if(null==warehouse){
			modelMap.addAttribute("msg", "仓库不存在，请重新设置！");
			return modelMap;
		}

        String[] arrIds= ids.split(",");

        NGRecord ngRecord = new NGRecord();
        int ngCount = 0;
        String oldNo = "";
        String oldBatchNumber = "";
        //遍历 判断是否入库 以及是否是同一条  以及计算入库总数量
        for (int i = 0; i < arrIds.length; i++) {
			ngRecord = ngRecordService.queryObjById(Long.parseLong(arrIds[i]));
			//判断当前单号 是否与上一条相同
			if (i>0 && !oldNo.equals(ngRecord.getNo())){
				modelMap.addAttribute("msg"," 当前工单单号为: "+ ngRecord.getNo()+" 与上一单单号不同,请重新选择！");
				return modelMap;
			}

			//判断当前批号 是否与上一条相同
			if (i>0 && !oldBatchNumber.equals(ngRecord.getBatchNumber())){
				modelMap.addAttribute("msg"," 当前批号为: "+ ngRecord.getBatchNumber()+" 与上一单批号不同,请重新选择！");
				return modelMap;
			}

			oldNo = ngRecord.getNo();
			oldBatchNumber = ngRecord.getBatchNumber();
			if(ngRecord.getInWarehouse()!=null&&ngRecord.getInWarehouse()){
                modelMap.addAttribute("msg", "创建时间为:"+ngRecord.getOccurDate()+" 工单单号为: "+ ngRecord.getNo()+" NG记录已经入库！");
                return modelMap;
            }

            ngCount += ngRecord.getNgCount();
        }


		String WarehouseNo=ngRecordService.queryMaxWarehouseNo();
		String requestNo = StringUtil.increment(WarehouseNo);
		if(WarehouseNo!=null&&!no.equals(requestNo)) {
			no=requestNo;
			modelMap.addAttribute("changeNo", "提交成功 ，单号已存在，修改单号为"+requestNo+"。");
		}else{
			no = "NGRK"+ dateFormat.format(new Date()) + "000";
		}
		ngRecord.setInWarehouse(true);
		ngRecord.setWarehouseDate(new Date());
		ngRecord.setWarehouseNo(no);
		ngRecord.setInWarehouseCode(warehouse.getcWhCode());
		ngRecord.setInWarehouseName(warehouse.getcWhName());
		ngRecord.setInWarehouseUserName(employee.getName());
		//生成ERP入库单
		WorkSheet workSheet = workSheetService.queryByProperty("no",ngRecord.getNo());
		if(workSheet!=null &&workSheet.getFromErp()!=null&& workSheet.getFromErp()) {
			Map<String, Object> map = new HashMap<>();
			map.put("warehouseCode", warehouse.getcWhCode());
			map.put("warehouseDate", new Date());
			map.put("warehouseNo", no);
			map.put("detailBatchNumber",ngRecord.getBatchNumber());
			if (employee != null) {
				map.put("makerName", employee.getName());
				map.put("makerCode", employee.getCode());
			}
			map.put("amount", ngCount);
			Result result = new Result();
			try{
				result =  jobBookingSlipUtil.generateNGJobBookingSlip(workSheet, map);
			}catch(Exception e){
				modelMap.addAttribute("msg", "ERP连接异常");
				return modelMap;
			}
			if(result.getStatusCode()!=200){
				modelMap.addAttribute("msg", result.getMessage());
				return modelMap;
			}

		}
		ngRecordService.updateObj(ngRecord);
		modelMap.addAttribute("msg", "NG记录入库成功！");
		return modelMap;
	}
}
