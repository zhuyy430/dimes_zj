package com.digitzones.devmgr.controller;

import java.security.Principal;

import com.digitzones.service.IEmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.dto.MaintenancePlanDto;
import com.digitzones.devmgr.model.MaintenancePlan;
import com.digitzones.devmgr.service.IMaintenancePlanService;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;
/**
 * 保养计划控制器
 * @author zdq
 * 2018年12月20日
 */
@RestController
@RequestMapping("/maintenancePlan")
public class MaintenancePlanController {
	@Autowired
	private IMaintenancePlanService maintenancePlanService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 生成保养计划
	 * @param maintenancePlanDto
	 * @return
	 */
	@RequestMapping("/generateMaintenancePlan.do")
	public ModelMap generateMaintenancePlan(MaintenancePlanDto maintenancePlanDto,Principal principal) {
		ModelMap modelMap = new ModelMap();
		MaintenancePlan  maintenancePlan = new MaintenancePlan();
		BeanUtils.copyProperties(maintenancePlanDto, maintenancePlan);
		User user = userService.queryByProperty("username", principal.getName());
		Employee employee = user.getEmployee();
		if(employee == null) {
			employee = new Employee();
			employee.setCode(user.getUsername());
			employee.setName(user.getUsername());
		}
		try{
			maintenancePlanService.addMaintenancePlan(maintenancePlan, maintenancePlanDto.getDeviceCodes(),employee);
		}catch(RuntimeException re) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", re.getMessage());
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "保养计划已生成!");
		return modelMap;
	}
}
