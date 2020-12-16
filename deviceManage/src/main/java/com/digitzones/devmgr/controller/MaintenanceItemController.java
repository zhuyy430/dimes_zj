package com.digitzones.devmgr.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.MaintenanceItem;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.service.IMaintenanceItemService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;

@RestController
@RequestMapping("/maintenanceItem")
public class MaintenanceItemController {
	@Autowired
	private IMaintenanceItemService maintenanceItemService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;

	/**
	 * 根据保养计划记录id查找备品备件信息
	 * @param recordId
	 * @param rows
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMaintenanceItemByMaintenancePlanRecordId.do")
	public ModelMap queryMaintenanceItemByMaintenancePlanRecordId(Long recordId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) throws ParseException {
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceItem> pager = maintenanceItemService.queryObjs("from MaintenanceItem ms where ms.maintenancePlanRecord.id=?0", page, rows, new Object[] {recordId});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据保养计划记录id查找备品备件信息
	 * @param recordId
	 * @return
	 */
	@RequestMapping("/queryMaintenanceItemById.do")
	public List<MaintenanceItem> queryMaintenanceItemById(Long recordId) {
		 return maintenanceItemService.queryMaintenanceItemByMaintenancePlanRecordId(recordId);
		 
	}
	/**
	 * 添加保养备件记录
	 * @param maintenancePlanRecordId
	 * @return
	 */
	@RequestMapping("/addMaintenanceItems.do")
	public ModelMap addMaintenanceItems(Long maintenancePlanRecordId,String itemIds,String recordTypeCode,String recordTypeName) {
		ModelMap modelMap = new ModelMap();
		if(!StringUtils.isEmpty(itemIds)) {
			if(itemIds.contains("[")) {
				itemIds = itemIds.replace("[", "");
			}
			if(itemIds.contains("]")) {
				itemIds = itemIds.replace("]", "");
			}
		}
		
		maintenanceItemService.addMaintenanceItemsByType(maintenancePlanRecordId,itemIds,recordTypeCode);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 根据id查找保养备件
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryById.do")
	public MaintenanceItem queryById(Long id) {
		return maintenanceItemService.queryObjById(id);
	}
	/**
	 * 更新保养备件信息
	 * @param maintenanceItem
	 * @return
	 */
	@RequestMapping("/updateMaintenanceItem.do")
	public ModelMap updateMaintenanceItem(MaintenanceItem maintenanceItem,String result) {
		ModelMap modelMap = new ModelMap();
		MaintenanceItem maintenance = maintenanceItemService.queryObjById(maintenanceItem.getId());
		maintenance.setNote(maintenanceItem.getNote());
		maintenance.setFrequency(maintenanceItem.getFrequency());
		maintenance.setStandard(maintenanceItem.getStandard());
		maintenance.setMethod(maintenanceItem.getMethod());
		maintenance.setRemarks(maintenanceItem.getRemarks());
		if(result.equals("OK")){
			maintenance.setMaintenanceDate(new Date());
		}else{
			maintenance.setMaintenanceDate(null);
		}
		maintenanceItemService.updateObj(maintenance);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	
	/**
	 * 删除设备项目信息
	 * @return
	 */
	@RequestMapping("/deleteMaintenanceItem.do")
	public ModelMap deleteMaintenanceItem(String id){
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		MaintenanceItem item = maintenanceItemService.queryObjById(Long.valueOf(id));
		if(item.getConfirmDate()!=null) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该记录已确认，无法删除!");
			return modelMap;
		}
		maintenanceItemService.deleteObj(Long.parseLong(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
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
		MaintenanceItem item = maintenanceItemService.queryObjById(Long.valueOf(id));
		if(item.getConfirmDate()!=null) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "不允许重复确认!");
			return modelMap;
		}
		User user = userService.queryByProperty("username", principal.getName());
		//Employee employee = user.getEmployee();
		if(user.getEmployee()!=null) {
			item.setConfirmCode(user.getEmployee().getCode());
			item.setConfirmUser(user.getEmployee().getName());
		}else {
			item.setConfirmCode(user.getUsername());
			item.setConfirmUser(user.getUsername());
		}
		item.setConfirmDate(new Date());
		maintenanceItemService.confirm(item);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已确认!");
		return modelMap;
	}
	/**
	 * 设置保养日期
	 * @param id
	 * @return
	 */
	@RequestMapping("/setMaintenanceDate.do")
	public ModelMap setMaintenanceDate(Long id) {
		ModelMap modelMap = new ModelMap();
		MaintenanceItem item = maintenanceItemService.queryObjById(id);
		item.setMaintenanceDate(new Date());
		if(item.getConfirmDate()!=null) {
			item.setConfirmCode(null);
			item.setConfirmUser(null);
			item.setConfirmDate(null);
			MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(item.getMaintenancePlanRecord().getId());
			record.setConfirmCode(null);
			record.setConfirmDate(null);
			record.setConfirmName(null);
			maintenancePlanRecordService.updateObj(record);
		}
		maintenanceItemService.updateObj(item);
		return modelMap;
	}
	/**
	 * 设置保养日期为null
	 * @param id
	 * @return
	 */
	@RequestMapping("/setMaintenanceDate2Null.do")
	public ModelMap setMaintenanceDate2Null(Long id) {
		ModelMap modelMap = new ModelMap();
		MaintenanceItem item = maintenanceItemService.queryObjById(id);
		item.setMaintenanceDate(null);
		if(item.getConfirmDate()!=null) {
			item.setConfirmCode(null);
			item.setConfirmUser(null);
			item.setConfirmDate(null);
			MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(item.getMaintenancePlanRecord().getId());
			record.setConfirmCode(null);
			record.setConfirmDate(null);
			record.setConfirmName(null);
			maintenancePlanRecordService.updateObj(record);
		}
		maintenanceItemService.updateObj(item);
		return modelMap;
	}
}
