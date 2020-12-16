package com.digitzones.devmgr.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.TransferRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.ITransferRecordService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
/**
 * 转单控制器
 * @author zdq
 * 2019年3月10日
 */
@RestController
@RequestMapping("TransferRecordController")
public class TransferRecordController {
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
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryTransferRecord.do")
	public ModelMap queryTransferRecord(boolean status,@RequestParam(defaultValue="10")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from TransferRecord ";
		Pager<TransferRecord> pager = new Pager<>();
		if(status){
			hql+=" order by transferDate";
			pager = transferRecordService.queryObjs(hql, page, rows, new Object[] {});
		}else{
			hql+=" where status=?0 order by transferDate";
			pager = transferRecordService.queryObjs(hql, page, rows, new Object[] {status});
		}
		
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 新增转单记录
	 * @param transferRecord
	 * @return
	 */
	@RequestMapping("/addTransferRecordRecord.do")
	public ModelMap addTransferRecordRecord(TransferRecord transferRecord,Principal principal) {
		ModelMap modelMap = new ModelMap();
		String userName = principal.getName();
		if(userName.equals("admin")){
			transferRecord.setTransferDate(new Date());
			transferRecordService.addObj(transferRecord);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg","转单成功!");
			return modelMap;
		}else{
			User user = userService.queryUserByUsername(userName);
			if(user.getEmployee()!=null){
				if(user.getEmployee().getName().equals(transferRecord.getTransferName())){
					transferRecord.setTransferDate(new Date());
					transferRecordService.addObj(transferRecord);
					modelMap.addAttribute("success", true);
					modelMap.addAttribute("msg","转单成功!");
					return modelMap;
				}
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg","该报修单非登录人所有，不可操作，请重新选择!");
				return modelMap;
			}
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("msg","登录人没有关联员工，不可操作!");
		return modelMap;
		
	}
	/**
	 * 分配转单
	 * @return
	 */
	@RequestMapping("/assignTransferRecordRecord.do")
	public ModelMap assignTransferRecordRecord(Long  employeeIds,Long transId,Principal principal) {
		ModelMap modelMap = new ModelMap();
		String userName = principal.getName();
		User user = userService.queryUserByUsername(userName);
		if(user.getEmployee()!=null){
			userName = user.getEmployee().getName();
		}
		MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryObjById(employeeIds);
		TransferRecord transferRecord = transferRecordService.queryObjById(transId);
		transferRecordService.updateTransferRecordRecord(maintenanceStaff,transferRecord,userName);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg","转单成功!");
		return modelMap;
		
	}
	
}
