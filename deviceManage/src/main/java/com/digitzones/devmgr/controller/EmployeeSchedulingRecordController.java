package com.digitzones.devmgr.controller;

import java.text.SimpleDateFormat;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.dto.EmployeeSchedulingDto;
import com.digitzones.devmgr.model.EmployeeSchedulingRecord;
import com.digitzones.devmgr.service.IEmployeeSchedulingRecordService;
import com.digitzones.devmgr.vo.EmployeeSchedulingRecordVO;
import com.digitzones.model.Pager;
/**
 * 原因排班控制器
 * @author zdq
 * 2019年3月10日
 */
@RestController
@RequestMapping("employeeSchedulingRecord")
public class EmployeeSchedulingRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private IEmployeeSchedulingRecordService employeeSchedulingRecordService;
	/**
	 * 根据维修人员编码查找排班记录
	 * @param maintenanceStaffCode
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryEmployeeSchedulingRecordsByMaintenanceStaffCode.do")
	public ModelMap queryEmployeeSchedulingRecordsByMaintenanceStaffCode(String maintenanceStaffCode,@RequestParam(defaultValue="10")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		@SuppressWarnings("unchecked")
		Pager<EmployeeSchedulingRecord> pager = employeeSchedulingRecordService.queryObjs("from EmployeeSchedulingRecord er where er.employeeCode=?0 order by er.schedulingDate", page, rows, new Object[] {maintenanceStaffCode});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 生成人员排班计划
	 * @param employeeSchedulingDto
	 * @return
	 */
	@RequestMapping("/generateEmployeeScheduling.do")
	public ModelMap generateEmployeeScheduling(EmployeeSchedulingDto employeeSchedulingDto) {
		ModelMap modelMap = new ModelMap();
		employeeSchedulingRecordService.addEmployeeSchedulings(employeeSchedulingDto);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg","添加成功!");
		return modelMap;
	}
	
	/**
	 * 根据id删除排班记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEmployeeSchedulingRecord.do")
	@ResponseBody
	public ModelMap deleteEmployeeSchedulingRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			employeeSchedulingRecordService.deleteObj(Long.valueOf(id));
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
	/**
	 * 根据id查找排班记录
	 * @param id
	 */
	@RequestMapping("/queryEmployeeSchedulingRecordById.do")
	public EmployeeSchedulingRecordVO queryEmployeeSchedulingRecordById(Long id) {
		EmployeeSchedulingRecordVO vo = new EmployeeSchedulingRecordVO();
		EmployeeSchedulingRecord record = employeeSchedulingRecordService.queryObjById(id);
		BeanUtils.copyProperties(record, vo);
		if(record.getSchedulingDate()!=null) {
			vo.setSchedulingDate(format.format(record.getSchedulingDate()));
		}
		return vo;
	}
	/**
	 *	更新记录
	 * @param id
	 */
	@RequestMapping("/updateEmployeeSchedulingRecord.do")
	public ModelMap updateEmployeeSchedulingRecord(EmployeeSchedulingRecord employeeSchedulingRecord) {
		EmployeeSchedulingRecord record = employeeSchedulingRecordService.queryObjById(employeeSchedulingRecord.getId());
		record.setClassCode(employeeSchedulingRecord.getClassCode());
		record.setClassName(employeeSchedulingRecord.getClassName());
		employeeSchedulingRecordService.updateObj(record);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
}
