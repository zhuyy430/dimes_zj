package com.digitzones.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.constants.Constant;
import com.digitzones.constants.Constant.DeviceRepairStatus;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IMaintenanceStaffRecordService;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
import com.digitzones.util.PushtoAPP;

@Controller
@RequestMapping("/AppMaintenanceStaffRecord")
public class AppMaintenanceStaffRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IUserService userService; 
	@Autowired
	private IMaintenanceStaffRecordService maintenanceStaffRecordService;
	@Autowired
	private IEmployeeService employeeService;
	
	/**
	 * 查询所有
	 */
	@RequestMapping("/queryAllMaintenanceStaffRecord.do")
	@ResponseBody
	public List<MaintenanceStaffRecord> queryAllMaintenanceStaffRecord(Long orderId){
		return maintenanceStaffRecordService.queryByOrderId(orderId);
	}
	/**
	 * 查询责任人
	 */
	@RequestMapping("/queryMaintenanceStaffRecordPersonLiableByorderId.do")
	@ResponseBody
	public List<MaintenanceStaffRecord> queryMaintenanceStaffRecordPersonLiableByorderId(Long orderId){
		return maintenanceStaffRecordService.queryMaintenanceStaffRecordPersonLiableByorderId(orderId);
	}
	/**
	 * 查询协助人
	 */
	@RequestMapping("/queryMaintenanceStaffRecordHelpByorderId.do")
	@ResponseBody
	public List<MaintenanceStaffRecord> queryMaintenanceStaffRecordHelpByorderId(Long orderId){
		return maintenanceStaffRecordService.queryMaintenanceStaffRecordHelpByorderId(orderId);
	}
	/**
	 * 新增维修人员
	 * @return
	 */
	@RequestMapping("/addMaintainProject.do")
	@ResponseBody
	public ModelMap addMaintenanceStaffRecord(String username,String code,Long deviceRepair_id,String assignTime){
		ModelMap modelMap = new ModelMap();
		Date d = null;
		try {
			d = format.parse(assignTime);
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<String> clientIdsList = new ArrayList<>();
		User user = userService.queryByProperty("username", username);
		Employee assist=employeeService.queryEmployeeByCode(code);
		DeviceRepair deviceRepair1 = new DeviceRepair();
		deviceRepair1.setId(deviceRepair_id);
		MaintenanceStaffRecord maintenanceStaffRecord=new MaintenanceStaffRecord();
		maintenanceStaffRecord.setName(assist.getName());
		maintenanceStaffRecord.setCode(code);
		maintenanceStaffRecord.setDeviceRepair(deviceRepair1);
		maintenanceStaffRecord.setAssignTime(d);
		maintenanceStaffRecord.setReceiveType("协助");
		Employee employee=employeeService.queryByProperty("code", code);
		
		List<MaintenanceStaffRecord> exist = maintenanceStaffRecordService.queryByOrderId(maintenanceStaffRecord.getDeviceRepair().getId());
		
		if(exist==null){
			maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ARTIFICIALGASSIGN);
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			DeviceRepair deviceRepair = deviceRepairOrderService.queryObjById(deviceRepair_id);
			if(null!=employee){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, employee.getName(), DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, DeviceRepairStatus.WAITINCOMFIRM);
			}
		}else{
			DeviceRepair deviceRepair = deviceRepairOrderService.queryObjById(deviceRepair_id);
			for(MaintenanceStaffRecord m:exist){
				if(m.getCode().equals(maintenanceStaffRecord.getCode())){
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", "该人员已存在!");
					return modelMap;
				}
			}
			if(user.getEmployee()!=null) {
				maintenanceStaffRecord.setAssignCode(user.getEmployee().getCode());
				maintenanceStaffRecord.setAssignName(user.getEmployee().getName());
			}else {
				maintenanceStaffRecord.setAssignCode(user.getUsername());
				maintenanceStaffRecord.setAssignName(user.getUsername());
			}
			maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ASSIST);
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			if(null!=employee){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, employee.getName(), DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, DeviceRepairStatus.WAITINCOMFIRM);
			}
		}
		
		clientIdsList.add(user.getId()+"");
		try {
			PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
}
