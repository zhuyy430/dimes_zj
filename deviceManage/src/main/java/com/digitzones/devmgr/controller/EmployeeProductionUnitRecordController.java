package com.digitzones.devmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.EmployeeProductionUnitRecord;
import com.digitzones.devmgr.service.IEmployeeProductionUnitRecordService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IProductionUnitService;
/**
 * 原因排班控制器
 * @author zdq
 * 2019年3月10日
 */
@RestController
@RequestMapping("employeeProductionUnitRecord")
public class EmployeeProductionUnitRecordController {
	@Autowired
	IEmployeeProductionUnitRecordService employeeProductionUnitRecordService;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IProductionUnitService productionUnitService;
	/**
	 * 根据维修人员编码查找生产单元
	 * @param maintenanceStaffCode
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryEmployeeProductionUnitRecordByMaintenanceStaffCode.do")
	public ModelMap queryEmployeeProductionUnitRecordByMaintenanceStaffCode(String maintenanceStaffCode,@RequestParam(defaultValue="10")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		@SuppressWarnings("unchecked")
		Pager<EmployeeProductionUnitRecord> pager = employeeProductionUnitRecordService.queryObjs("from EmployeeProductionUnitRecord er where er.employeeCode=?0", page, rows, new Object[] {maintenanceStaffCode});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 新增生产单元关联
	 * @param employeeSchedulingDto
	 * @return
	 */
	@RequestMapping("/addEmployeeProductionUnitRecord.do")
	public ModelMap addEmployeeProductionUnitRecord(String employeeCode,String id) {
		if(id!=null && !id.equals("")) {
			id = id.substring(1, id.length()-1);
		}
		String[] ids = id.split(",");
		Employee emp = employeeService.queryEmployeeByCode(employeeCode);
		for(String i:ids){
			ProductionUnit pro = productionUnitService.queryObjById(Long.parseLong(i));
			EmployeeProductionUnitRecord er = new EmployeeProductionUnitRecord();
			er.setEmployeeCode(emp.getCode());
			er.setEmployeeName(emp.getName());
			er.setProductionUnit(pro);
			employeeProductionUnitRecordService.addObj(er);
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg","添加成功!");
		return modelMap;
	}
	
	/**
	 * 根据id删除生产单元关联记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEmployeeProductionUnitRecord.do")
	@ResponseBody
	public ModelMap deleteEmployeeProductionUnitRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			employeeProductionUnitRecordService.deleteObj(Long.valueOf(id));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		}catch(RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", e.getMessage());
		}
		return modelMap;
	}
}
