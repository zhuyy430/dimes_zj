package com.digitzones.app.controller;

import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.TransferRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.ITransferRecordService;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
/**
 * 转单控制器
 */
@Controller
@RequestMapping("AppTransferRecord")
public class AppTransferRecordController {
	@Autowired
	ITransferRecordService transferRecordService;
	@Autowired
	IUserService userService;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IMaintenanceStaffService maintenanceStaffService;
	/**
	 * 查询转单记录
	 */
	@RequestMapping("/queryAllTransferRecord.do")
	@ResponseBody
	public List<TransferRecord> queryAllTransferRecord(boolean status){
		return transferRecordService.queryAllTransferRecord(status);
	}
	
	
	/**
	 * 分配转单
	 * @return
	 */
	@RequestMapping("/assignTransferRecordRecord.do")
	@ResponseBody
	public ModelMap assignTransferRecordRecord(String employeeCode,Long transId,String userName) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryUserByUsername(userName);
		if(user.getEmployee()!=null){
			userName = user.getEmployee().getName();
		}
		MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", employeeCode);
		TransferRecord transferRecord = transferRecordService.queryObjById(transId);
		transferRecordService.updateTransferRecordRecord(maintenanceStaff,transferRecord,userName);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg","转单成功!");
		return modelMap;
	}
	
	/**
	 * 根据id查询
	 */
	@RequestMapping("/queryTransferRecordById.do")
	@ResponseBody
	public TransferRecord queryTransferRecordById(Long transId) {
		return transferRecordService.queryObjById(transId);
	}
}

