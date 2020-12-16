package com.digitzones.app.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceSparepart;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.IMaintenanceSparepartService;
import com.digitzones.devmgr.service.ISparepartService;

@Controller
@RequestMapping("/AppMaintenanceSparepart")
public class AppMaintenanceSparepartController {
	@Autowired
	private IMaintenanceSparepartService maintenanceSparepartService;
	@Autowired
	private ISparepartService sparepartService;
	
	/**
	 * 根据保养计划记录id查找备品备件信息
	 * 
	 */
	@RequestMapping("/queryMaintenanceSparepartByMaintenancePlanRecordId.do")
	@ResponseBody
	public List<MaintenanceSparepart> queryMaintenanceSparepartByMaintenancePlanRecordId(Long recordId){
		return maintenanceSparepartService.queryMaintenanceSparepartByRecordId(recordId);
	}
	/**
	 * 添加保养备件记录
	 * @param maintenancePlanRecordId
	 * @param sparepartIds
	 * @return
	 */
	@RequestMapping("/addMaintenanceSpareparts.do")
	@ResponseBody
	public ModelMap addMaintenanceSpareparts(Long maintenancePlanRecordId,Long sparepartId,int count) {
		ModelMap modelMap = new ModelMap();
		MaintenanceSparepart sparepart = new MaintenanceSparepart();
		MaintenancePlanRecord maintenancePlanRecord=new MaintenancePlanRecord();
		maintenancePlanRecord.setId(maintenancePlanRecordId);
		
		
		Sparepart part = sparepartService.queryObjById(sparepartId);
		BeanUtils.copyProperties(part, sparepart);
		sparepart.setMaintenancePlanRecord(maintenancePlanRecord);
		sparepart.setUseDate(new Date());
		sparepart.setCount(count);
		maintenanceSparepartService.addObj(sparepart);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
}
