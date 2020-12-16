package com.digitzones.mc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.digitzones.service.IEmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dto.MaintenancePlanRecordDto;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.devmgr.vo.MaintenancePlanRecordVO;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.User;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IUserService;
import com.digitzones.util.DateStringUtil;
/**
 * 保养计划记录控制器
 * @author zdq
 * 2018年12月20日
 */
@RestController
@RequestMapping("mcMaintenancePlanRecord")
public class MCMaintenancePlanRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private IMaintenancePlanRecordService  maintenancePlanRecordService;
	@Autowired
	private IRelatedDocumentService raletedDocumentService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 查询所有保养计划记录
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryAllMaintenancePlanRecords.do")
	@SuppressWarnings("unchecked")
	public ModelMap queryAllMaintenancePlanRecords(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "select record from MaintenancePlanRecord record left join  record.device  d left join  d.productionUnit"
				+ " left join  d.projectType";
		Pager<MaintenancePlanRecord> pager = maintenancePlanRecordService.queryObjs(hql, page, rows, new Object[] {});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 查询所有保养计划记录
	 * @return
	 */
	@RequestMapping("/queryAllMaintenancePlanRecordsByDeviceCode.do")
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceCode(String deviceCode) {
		return maintenancePlanRecordService.queryAllMaintenancePlanRecordsByDeviceCode(deviceCode);
	}
	/**
	 * 查询所有保养计划记录
	 * @return
	 */
	@RequestMapping("/queryAllMaintenancePlanRecordsByDeviceSiteCode.do")
	public List<MaintenancePlanRecordVO> queryAllMaintenancePlanRecordsByDeviceSiteCode(String deviceSiteCode) {
		List<MaintenancePlanRecordVO> VOList = new ArrayList<MaintenancePlanRecordVO>();
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
		List<MaintenancePlanRecord> maintenancePlanRecordList = maintenancePlanRecordService.queryAllMaintenancePlanRecordsByDeviceId(deviceSite.getDevice().getId());
		for(MaintenancePlanRecord m:maintenancePlanRecordList){
			MaintenancePlanRecordVO VO = model2vo(m);
			VOList.add(VO);
		}
		return VOList;
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
		int i = 0;
		List<Object> paramList = new ArrayList<>();
		String search_from = params.get("search_from");
		String search_to = params.get("search_to");
		String search_class = params.get("search_class");
		String search_status = params.get("search_status");
		String hql = "from MaintenancePlanRecord record  where 1=1 ";

		if(!StringUtils.isEmpty(deviceCode)) {
			hql += " and record.deviceCode=?" + (i++);
			paramList.add(deviceCode);
		}

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
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
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
	 * 执行保养计划记录
	 * @return
	 */
	@RequestMapping("/updateMaintenancePlanRecord.do")
	public ModelMap updateMaintenancePlanRecord(MaintenancePlanRecord maintenancePlanRecord,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		
		String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		User user = userService.queryUserByUsername(mcUser.getUsername());
		Employee employee = user.getEmployee();
		MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(maintenancePlanRecord.getId());
		record.setMaintenancedDate(maintenancePlanRecord.getMaintenancedDate());
		record.setConfirmName(maintenancePlanRecord.getConfirmName());
		record.setConfirmCode(maintenancePlanRecord.getConfirmCode());
		record.setStatus(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED);
		maintenancePlanRecordService.confirmMaintenance(record, employee.getName(), Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED);
		maintenancePlanRecordService.updateObj(record);
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
		modelMap.addAttribute("records", list);
		modelMap.addAttribute("days", days);
		return modelMap;
	}
	/**
	 * 根据保养记录id查询保养记录信息
	 * @param id 
	 * @return
	 */
	@GetMapping("/queryMaintenancePlanRecordById.do")
	public MaintenancePlanRecordVO queryMaintenancePlanRecordById(Long id,HttpServletRequest request) {
		MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(id);
		MaintenancePlanRecordVO vo = new MaintenancePlanRecordVO();
		BeanUtils.copyProperties(record, vo);
		
		String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		User user = userService.queryUserByUsername(mcUser.getUsername());
		
		if(user.getEmployee()!=null){
			vo.setEmployeeName(user.getEmployee().getName());
			vo.setEmployeeCode(user.getEmployee().getCode());
		}else{
			vo.setEmployeeName(user.getUsername());
			vo.setEmployeeCode(user.getUsername());
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
		return vo;
	}
	/**
	 * 指定责任人
	 * @param record
	 * @return
	 */
	@RequestMapping("/assignPersonInCharge.do")
	public ModelMap assignPersonInCharge(MaintenancePlanRecordDto record,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		if(StringUtils.isEmpty(record.getEmployeeCode()) || StringUtils.isEmpty(record.getEmployeeName())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请指定责任人!");
			return modelMap;
		}
		//更新保养计划状态
		MaintenancePlanRecord planRecord = maintenancePlanRecordService.queryObjById(record.getId());
		planRecord.setStatus("已派单");
		maintenancePlanRecordService.updateObj(planRecord);
		//添加保养责任人
		MCUser user = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		MaintenanceUser maintenanceUser = new MaintenanceUser();
		maintenanceUser.setDispatchUsercode(user.getEmployeeCode());
		maintenanceUser.setDispatchUsername(user.getEmployeeName());
		maintenanceUser.setMaintenancePlanRecord(planRecord);
		maintenanceUser.setCode(record.getEmployeeCode());
		maintenanceUser.setName(record.getEmployeeName());
		maintenanceUser.setDispatchDate(new Date());
		maintenanceUser.setOrderType("人工派单");
		maintenanceUserService.addObj(maintenanceUser);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 批量责任人
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/assignBatchPersonInCharge.do")
	public ModelMap assignBatchPersonInCharge(@RequestParam Map<String,Object> params,HttpServletRequest request) throws ParseException {
		ModelMap modelMap = new ModelMap();
		MCUser user = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		if(user==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请登录!");
			return modelMap;
		}
		
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
		
		
		maintenancePlanRecordService.update4AssignBatchPersonInCharge4Mc(params,user);
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
	public ModelMap confirm(String id,HttpServletRequest request){
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
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		if(mcUser==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请登录!");
			return modelMap;
		}
		User user = userService.queryUserByUsername(mcUser.getUsername());
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
	 * model转vo
	 * @param record
	 * @return
	 */
	public MaintenancePlanRecordVO model2vo(MaintenancePlanRecord record) {
		format.applyPattern("yyyy-MM-dd");
		MaintenancePlanRecordVO vo = new MaintenancePlanRecordVO();
		BeanUtils.copyProperties(record, vo);
		MaintenanceUser u = maintenanceUserService.queryPersonInChargeByMaintenancePlanRecordId(record.getId());
		if(u!=null) {
			vo.setEmployeeCode(u.getCode());
			vo.setEmployeeName(u.getName());
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
		return vo;
	}
}
