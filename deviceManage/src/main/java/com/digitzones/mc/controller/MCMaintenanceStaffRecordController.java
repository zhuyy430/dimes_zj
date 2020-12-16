package com.digitzones.mc.controller;

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
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
import com.digitzones.util.PushtoAPP;
@RestController
@RequestMapping("/mcMaintenanceStaffRecord")
public class MCMaintenanceStaffRecordController {
	@Autowired
	@Qualifier("deviceRepairOrderServiceImpl")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IMaintenanceStaffRecordService maintenanceStaffRecordService;
	@Autowired
	private IUserService userService; 
	@Autowired
	private IMCUserService mcUserService; 
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
	public ModelMap addMaintenanceStaffRecord(MaintenanceStaffRecord maintenanceStaffRecord,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		List<String> clientIdsList = new ArrayList<>();
		
		String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		User user = userService.queryUserByUsername(mcUser.getUsername());
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		user.setUsername(employee.getName());
		
		List<MaintenanceStaffRecord> exist = maintenanceStaffRecordService.queryByOrderId(maintenanceStaffRecord.getDeviceRepair().getId());
		
		if(exist==null){
			maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ARTIFICIALGASSIGN);
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			DeviceRepair deviceRepair = deviceRepairOrderService.queryObjById(maintenanceStaffRecord.getDeviceRepair().getId());
			deviceRepair.setMaintainName(maintenanceStaffRecord.getName());
			deviceRepair.setMaintainCode(maintenanceStaffRecord.getCode());
			deviceRepair.setStatus(Constant.DeviceRepairStatus.WAITINCOMFIRM);
			//deviceRepairOrderService.addDeviceRepair(deviceRepair, user, modelMap);
			deviceRepairOrderService.updateObj(deviceRepair);
			
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
	public ModelMap updateMaintenanceStaffRecord(MaintenanceStaffRecord maintenanceStaffRecord){
		ModelMap modelMap = new ModelMap();
		MaintenanceStaffRecord m = maintenanceStaffRecordService.queryObjById(maintenanceStaffRecord.getId());
		m.setCode(maintenanceStaffRecord.getCode());
		m.setName(maintenanceStaffRecord.getName());
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
