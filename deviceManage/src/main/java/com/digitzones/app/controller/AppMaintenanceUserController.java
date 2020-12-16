package com.digitzones.app.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;

@Controller
@RequestMapping("/AppMaintenanceUser")
public class AppMaintenanceUserController {
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMaintenancePlanRecordService  maintenancePlanRecordService;
	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 添加责任人
	 * @return
	 */
	@RequestMapping("/addMaintenanceUser.do")
	@ResponseBody
	public ModelMap addMaintenanceUser(String username,String code,Long maintenancePlanRecord_id) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		MaintenanceUser maintenanceUser=new MaintenanceUser();
		MaintenancePlanRecord maintenancePlanRecord=maintenancePlanRecordService.queryObjById(maintenancePlanRecord_id);
		Employee assist=employeeService.queryEmployeeByCode(code);
		Employee employee = user.getEmployee();
		if(employee==null) {
			employee = new Employee();
			employee.setCode(user.getUsername());
			employee.setName(user.getUsername());
		}
		maintenanceUser.setMaintenancePlanRecord(maintenancePlanRecord);
		maintenanceUser.setDispatchUsercode(employee.getCode());
		maintenanceUser.setDispatchUsername(employee.getName());
		maintenanceUser.setDispatchDate(new Date());
		maintenanceUser.setCode(code);
		maintenanceUser.setName(assist.getName());
		maintenanceUser.setOrderType("协助");
		List<MaintenanceUser> muList=maintenanceUserService.queryMaintenanceUserByRecordId(maintenancePlanRecord_id);
		if(muList!=null) {
			for(MaintenanceUser mu:muList) {
				if(mu.getCode().equals(code)){
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", "该人员已存在!");
					return modelMap;
				}
			}
		}
		
		
		
		try {
		maintenanceUserService.addMaintenanceUser(maintenanceUser,employee);
		}catch(RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", e.getMessage());
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 根据维修单id查询责任人
	 */
	@RequestMapping("/queryResponsibilityMaintenanceUserByMaintenancePlanRecordId.do")
	@ResponseBody
	public List<MaintenanceUser> queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(Long recordId){
		return maintenanceUserService.queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(recordId);
	}
	/**
	 * 根据维修单id查询协助人
	 */
	@RequestMapping("/queryHelpMaintenanceUserByMaintenancePlanRecordId.do")
	@ResponseBody
	public List<MaintenanceUser> queryHelpMaintenanceUserByMaintenancePlanRecordId(Long recordId) {
		return maintenanceUserService.queryHelpMaintenanceUserByMaintenancePlanRecordId(recordId);
	}
}
