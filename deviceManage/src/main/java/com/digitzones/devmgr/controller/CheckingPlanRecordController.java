package com.digitzones.devmgr.controller;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.devmgr.model.CheckingPlanRecordItemFiles;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.CheckingPlanRecordItem;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemService;
import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.model.Device;
import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.User;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IUserService;
import com.digitzones.util.DateStringUtil;
/**
 * 点检计划记录控制器
 * @author zdq
 * 2018年12月20日
 */
@RestController
@RequestMapping("checkingPlanRecord")
public class CheckingPlanRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private ICheckingPlanRecordService  checkingPlanRecordService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private IDeviceProjectRecordService deviceProjectRecordService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICheckingPlanRecordItemService checkingPlanRecordItemService;
	@Autowired
	private ICheckingPlanRecordItemFilesService checkingPlanRecordItemFilesService;
	/**
	 * 根据设备编码查找点检计划记录
	 * @param deviceCode
	 * @param rows
	 * @param page
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCheckingPlanRecordByDeviceCode.do")
	public ModelMap queryCheckingPlanRecordByDeviceCode(String deviceCode,@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) throws ParseException {
		ModelMap modelMap = new ModelMap();
		int i = 1;
		List<Object> paramList = new ArrayList<>();
		paramList.add(deviceCode);
		String search_from = params.get("search_from");
		String search_to = params.get("search_to");
		String search_class = params.get("search_class");
		String search_status = params.get("search_status");
		String hql = "from CheckingPlanRecord record where record.deviceCode=?0";
		if(!StringUtils.isEmpty(search_from)) {
			hql += " and checkingDate>=?" + (i++);
			paramList.add(format.parse(search_from));
		}
		if(!StringUtils.isEmpty(search_to)) {
			hql += " and checkingDate<=?" + (i++);
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
		hql +=" order by checkingDate";
		Pager<CheckingPlanRecord> pager = checkingPlanRecordService.queryObjs(hql, page, rows, paramList.toArray());
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 删除点检计划记录
	 * @param ids
	 * @return
	 */
	@RequestMapping("/deleteCheckingPlanRecord.do")
	public ModelMap deleteCheckingPlanRecord(String ids) {
		ModelMap modelMap = new ModelMap();
		if(!StringUtils.isEmpty(ids)) {
			if(ids.contains("'")) {
				ids = ids.replace("'", "");
			}
			String[] idArray = ids.split(",");
			checkingPlanRecordService.deleteChekingPlanRecords(idArray);
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
	 * 根据设备编码和月份查找设备点检记录
	 * @param deviceCode
	 * @param month
	 * @return
	 */
	@RequestMapping("/queryCheckingPlanRecordByDeviceCodeAndMonth.do")
	public ModelMap queryCheckingPlanRecordByDeviceCodeAndMonth(String deviceCode,String month) {
		ModelMap modelMap = new ModelMap();
		List<CheckingPlanRecord> list = null;
		List<Date> days = null;
		if(StringUtils.isEmpty(month)) {
			Calendar c = Calendar.getInstance();
			list = checkingPlanRecordService.queryCheckingPlanRecordByDeviceCodeAndMonth(deviceCode, c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1));
			days =  new DateStringUtil().generateOneMonthDay(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1));
		}else {
			String[] dates = month.split("-");
			list = checkingPlanRecordService.queryCheckingPlanRecordByDeviceCodeAndMonth(deviceCode, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
			days =  new DateStringUtil().generateOneMonthDay(month);
		}
		modelMap.addAttribute("records", list);
		modelMap.addAttribute("days", days);
		return modelMap;
	}
	/**
	 * 根据点检记录id查询点检记录信息
	 * @param id 
	 * @return
	 */
	@GetMapping("/queryCheckingPlanRecordById.do")
	public ModelMap queryCheckingPlanRecordById(Long id,Principal principal) {
		ModelMap modelMap = new ModelMap();
		CheckingPlanRecord record = checkingPlanRecordService.queryObjById(id);
		if(record!=null) {
			modelMap.addAttribute("record", record);
			//根据设备编码查询文档信息
			String deviceCode = record.getDeviceCode();
			Device device = deviceService.queryByProperty("code", deviceCode);
			if(device!=null) {
				Long deviceId = device.getId();
				//查找关联文档
				List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, deviceId);
				modelMap.addAttribute("docs", docs);
				//根据设备编码查找点检记录项信息
				List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(id);
				if(CollectionUtils.isEmpty(list)) {
					//查找标准点检项目
					List<DeviceProjectRecord> projectRecords = new ArrayList<>();
					if(record.getClassCode()!=null){
						projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceIdAndtypeAndClassesCode(deviceId,"SPOTINSPECTION",record.getClassCode());
					}else{
						projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceIdAndtype(deviceId,"SPOTINSPECTION");
					}
					modelMap.addAttribute("projectRecords", projectRecords);
				}else {
					modelMap.addAttribute("projectRecords", list);
				}
			}
		}
		if(record.getEmployeeCode()==null) {
			String username = principal.getName();
			User user = userService.queryByProperty("username", username);
			if(user!=null) {
				if(user.getEmployee()!=null) {
					modelMap.addAttribute("checkUsername", user.getEmployee().getName());
					modelMap.addAttribute("checkUsercode", user.getEmployee().getCode());
				}else {
					modelMap.addAttribute("checkUsername", user.getUsername());
					modelMap.addAttribute("checkUsercode", user.getUsername());
				}
			}
		}else {
			modelMap.addAttribute("checkUsercode", record.getEmployeeCode());
			modelMap.addAttribute("checkUsername", record.getEmployeeName());
		}
		return modelMap;
	}
	/**
	 * 设备点检
	 * @param record
	 * @param itemIds
	 * @param results
	 * @return
	 */
	@RequestMapping("/deviceCheck.do")
	public void deviceCheck(CheckingPlanRecord record,String itemIds,String results,String notes,String checkValue) {
		CheckingPlanRecord checkingPlanRecord = checkingPlanRecordService.queryObjById(record.getId());
		checkingPlanRecord.setCheckedDate(record.getCheckedDate());
		checkingPlanRecord.setEmployeeCode(record.getEmployeeCode());
		checkingPlanRecord.setEmployeeName(record.getEmployeeName());
		checkingPlanRecord.setPicPath(record.getPicPath());
		checkingPlanRecordService.updateCheckingPlanRecord(checkingPlanRecord,itemIds,results,notes,checkValue);
	}

	/**
	 * 根据点检项id查找该点检项对应的文件
	 * @param itemId
	 * @return
	 */
	@RequestMapping("queryCheckingPlanRecordItemFilesByItemId.do")
	public List<CheckingPlanRecordItemFiles> queryCheckingPlanRecordItemFilesByItemId(Long itemId){
		return checkingPlanRecordItemFilesService.queryCheckingPlanRecordItemFilesByItemId(itemId);
	}
}
