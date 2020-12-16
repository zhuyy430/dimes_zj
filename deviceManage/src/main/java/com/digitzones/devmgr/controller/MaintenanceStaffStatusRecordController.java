package com.digitzones.devmgr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.MaintenanceStaffStatusRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffStatusRecordService;
import com.digitzones.model.Pager;
@RestController
@RequestMapping("/maintenanceStaffStatusRecord")
public class MaintenanceStaffStatusRecordController {
	@Autowired
	private IMaintenanceStaffStatusRecordService maintenanceStaffStatusRecordService;
	
	/**
	 * 分页查询维修人员切换记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllMaintenanceStaffStatusRecord.do")
	@ResponseBody
	public ModelMap queryAllMaintenanceStaffStatusRecord(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,String maintenanceStaffCode,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceStaffStatusRecord> pager = maintenanceStaffStatusRecordService.queryObjs("from MaintenanceStaffStatusRecord m where m.code=?0 order by m.changeDate DESC ", page, rows, new Object[] {maintenanceStaffCode});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	
}
