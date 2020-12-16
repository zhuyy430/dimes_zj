package com.digitzones.paperless.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;

@RestController
@RequestMapping("/paperlessDeviceRepair")
public class PaperlessDeviceRepairOrderController {
	@Autowired
	private IUserService userService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping("/addDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap addDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, Principal principal,String employeeCode,
			HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Boolean b=false;
		try{
			b = deviceRepairOrderService.addPaperLessDeviceRepairOrder(deviceRepairOrder, idList, employeeCode, request);
		}catch(Exception e){
			modelMap.addAttribute("statusCode", 110);
			modelMap.addAttribute("title", "操作提示!");
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", e.getMessage());
			return modelMap;
		}
		
		if(b){
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "新增成功!");
			return modelMap;
		}else{
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "新增失败!");
			return modelMap;
		}
	}
	
	
	/**
	 * 车间确认或维修完成(接收报修单)
	 * 
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrderStatusById.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrderStatusById(String id, String status, HttpServletRequest request) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Employee employee=(Employee)request.getSession().getAttribute("employee");
		
		User user=userService.queryUserByEmployeeCode(employee.getCode());
		String username = user.getUsername();
		Boolean b = false;
		try {
			b = deviceRepairOrderService.updateDeviceRepairOrderStatusById(Long.parseLong(id), status, username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			modelMap.addAttribute("statusCode", 110);
			modelMap.addAttribute("title", "操作提示!");
			modelMap.addAttribute("message", e.getMessage());
			e.printStackTrace();
			return modelMap;
		}
		if(b){
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "操作成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
}
