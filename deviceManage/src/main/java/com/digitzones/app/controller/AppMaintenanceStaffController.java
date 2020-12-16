package com.digitzones.app.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.MaintenanceStaffStatusRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffService;

@Controller
@RequestMapping("/AppMaintenanceStaff")
public class AppMaintenanceStaffController {
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	
	/**
	 * 通过人员代码查找维修人员在岗状态
	 * @param employeeCode
	 * @return
	 */
	@RequestMapping("/queryMaintenanceStaffByCode.do")
	@ResponseBody
	public MaintenanceStaff queryMaintenanceStaffByCode(String employeeCode) {
		return maintenanceStaffService.queryByProperty("code", employeeCode);
	}
	
	@RequestMapping("/updateWorkStatus.do")
	@ResponseBody
	public ModelMap updateOnDutyStatus(String employeeCode) {
		ModelMap modelMap=new ModelMap();
		MaintenanceStaff  maintenanceStaff=maintenanceStaffService.queryByProperty("code", employeeCode);
		if(maintenanceStaff!=null) {
	
				if(maintenanceStaff.getWorkStatus().equals(Constant.MaintenanceStaffStatus.REST)) {
					maintenanceStaffService.updateWorkStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY, maintenanceStaff.getName());
					modelMap.addAttribute("status", "在岗");
					modelMap.addAttribute("success", true);
				}else if(!maintenanceStaff.getWorkStatus().equals(Constant.MaintenanceStaffStatus.REST)) {	
					maintenanceStaffService.updateWorkStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.REST, maintenanceStaff.getName());
					modelMap.addAttribute("status", "休息");
					modelMap.addAttribute("success", true);
				}
			
		}
		
		return modelMap;
	}
	
}
