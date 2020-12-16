package com.digitzones.devmgr.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.constants.Constant;
import com.digitzones.constants.Constant.DeviceRepairStatus;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IMaintenanceStaffRecordService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
@RestController
@RequestMapping("/maintenanceStaffRecord")
public class MaintenanceStaffRecordController {
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IMaintenanceStaffRecordService maintenanceStaffRecordService;
	@Autowired
	private IUserService userService; 
	@Autowired
	private IEmployeeService employeeService;
	
	/**
	 * 分页查询设备报修
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMaintenanceStaffRecord.do")
	@ResponseBody
	public ModelMap queryMaintenanceStaffRecord(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceRepairOrderId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceStaffRecord> pager = maintenanceStaffRecordService.queryObjs("select m from MaintenanceStaffRecord m inner join m.deviceRepair d "
				+ "where d.id=?0", page, rows, new Object[] {deviceRepairOrderId});
		List<MaintenanceStaffRecord> data = pager.getData();
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addMaintenanceStaffRecord.do")
	@ResponseBody
	public ModelMap addMaintenanceStaffRecord(MaintenanceStaffRecord maintenanceStaffRecord,Principal principal){
		ModelMap modelMap = new ModelMap();
		List<String> clientIdsList = new ArrayList<>();
		String username = principal.getName();
		User user = userService.queryByProperty("username", username);
		//派单员工
		Employee employee = user.getEmployee();
		if(null!=employee){
			maintenanceStaffRecord.setAssignCode(employee.getCode());
			maintenanceStaffRecord.setAssignName(employee.getName());
		}else{
			maintenanceStaffRecord.setAssignCode(user.getUsername());
			maintenanceStaffRecord.setAssignName(user.getUsername());
		}
		
		//维修员工
		Employee emp = employeeService.queryEmployeeByCode(maintenanceStaffRecord.getCode());
		
		List<MaintenanceStaffRecord> exist = maintenanceStaffRecordService.queryByOrderId(maintenanceStaffRecord.getDeviceRepair().getId());
		
		DeviceRepair deviceRepair = deviceRepairOrderService.queryObjById(maintenanceStaffRecord.getDeviceRepair().getId());
		if(exist==null){
			maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ARTIFICIALGASSIGN);
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			deviceRepair.setMaintainName(maintenanceStaffRecord.getName());
			deviceRepair.setMaintainCode(maintenanceStaffRecord.getCode());
			deviceRepair.setStatus(Constant.DeviceRepairStatus.WAITINCOMFIRM);
			deviceRepair.setDispatchedLevel(null);
			//deviceRepairOrderService.addDeviceRepair(deviceRepair, user, modelMap);
			if(null!=emp){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, emp.getName(), Constant.DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, Constant.DeviceRepairStatus.WAITINCOMFIRM);
			}
		}else{
			for(MaintenanceStaffRecord m:exist){
				if(m.getCode().equals(maintenanceStaffRecord.getCode())){
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", "该人员已存在!");
					return modelMap;
				}
			}
			maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ASSIST);
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			if(null!=emp){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, emp.getName(), Constant.DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, Constant.DeviceRepairStatus.WAITINCOMFIRM);
			}
			
		}
		
		clientIdsList.add(user.getId()+"");
		try {
			//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryMaintainProjectById.do")
	@ResponseBody
	public MaintenanceStaffRecord queryMaintainProjectById(Long id){
		return maintenanceStaffRecordService.queryObjById(id);
	}
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/updateMaintenanceStaffRecord.do")
	@ResponseBody
	public ModelMap updateMaintenanceStaffRecord(MaintenanceStaffRecord maintenanceStaffRecord,Principal principal){
		ModelMap modelMap = new ModelMap();
		
		String username = principal.getName();
		User user = userService.queryByProperty("username", username);
		Employee employee = user.getEmployee();
		MaintenanceStaffRecord m = maintenanceStaffRecordService.queryObjById(maintenanceStaffRecord.getId());
		if(employee!=null){
			m.setAssignCode(employee.getCode());
			m.setAssignName(employee.getName());
		}
		m.setCode(maintenanceStaffRecord.getCode());
		m.setName(maintenanceStaffRecord.getName());
		if(maintenanceStaffRecord.getOccupyTime()!=null){
			m.setOccupyTime((double) Math.round((maintenanceStaffRecord.getOccupyTime().doubleValue()) * 100) / 100);
		}else{
			m.setOccupyTime(null);
		}
		maintenanceStaffRecordService.updateObj(m);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/deleteMaintenanceStaffRecord.do")
	@ResponseBody
	public ModelMap deleteMaintenanceStaffRecord(String id){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		maintenanceStaffRecordService.deleteObj(Long.parseLong(id));
		
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "删除成功!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
}
