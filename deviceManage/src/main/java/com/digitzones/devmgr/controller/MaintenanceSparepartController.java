package com.digitzones.devmgr.controller;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.MaintenanceSparepart;
import com.digitzones.devmgr.service.IMaintenanceSparepartService;
import com.digitzones.model.Pager;

@RestController
@RequestMapping("/maintenanceSparepart")
public class MaintenanceSparepartController {
	@Autowired
	private IMaintenanceSparepartService maintenanceSparepartService;
	/**
	 * 根据保养计划记录id查找备品备件信息
	 * @param recordId
	 * @param rows
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMaintenanceSparepartByMaintenancePlanRecordId.do")
	public ModelMap queryMaintenanceSparepartByMaintenancePlanRecordId(Long recordId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) throws ParseException {
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceSparepart> pager = maintenanceSparepartService.queryObjs("from MaintenanceSparepart ms where ms.maintenancePlanRecord.id=?0", page, rows, new Object[] {recordId});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据保养计划记录id查找备品备件信息
	 * @param recordId
	 * @return
	 */
	@RequestMapping("/queryMaintenanceSparepartByRecordId.do")
	public List<MaintenanceSparepart> queryMaintenanceSparepartByRecordId(Long recordId){
		//Pager<MaintenanceSparepart> pager = maintenanceSparepartService.queryObjs("from MaintenanceSparepart ms where ms.maintenancePlanRecord.id=?0", page, rows, new Object[] {recordId});
		return maintenanceSparepartService.queryMaintenanceSparepartByRecordId(recordId);
	}
	/**
	 * 添加保养备件记录
	 * @param maintenancePlanRecordId
	 * @param sparepartIds
	 * @return
	 */
	@RequestMapping("/addMaintenanceSpareparts.do")
	public ModelMap addMaintenanceSpareparts(Long maintenancePlanRecordId,String sparepartIds,String count) {
		ModelMap modelMap = new ModelMap();
		if(!StringUtils.isEmpty(sparepartIds)) {
			if(sparepartIds.contains("[")) {
				sparepartIds = sparepartIds.replace("[", "");
			}
			if(sparepartIds.contains("]")) {
				sparepartIds = sparepartIds.replace("]", "");
			}
		}
		maintenanceSparepartService.addMaintenanceSpareparts(maintenancePlanRecordId,sparepartIds,count);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 根据id查找保养备件
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryById.do")
	public MaintenanceSparepart queryById(Long id) {
		return maintenanceSparepartService.queryObjById(id);
	}
	/**
	 * 更新保养备件信息
	 * @param maintenanceSparepart
	 * @return
	 */
	@RequestMapping("/updateMaintenanceSparepart.do")
	public ModelMap updateMaintenanceSparepart(MaintenanceSparepart maintenanceSparepart) {
		ModelMap modelMap = new ModelMap();
		MaintenanceSparepart sparepart = maintenanceSparepartService.queryObjById(maintenanceSparepart.getId());
		sparepart.setCount(maintenanceSparepart.getCount());
		sparepart.setUseDate(maintenanceSparepart.getUseDate());
		sparepart.setNote(maintenanceSparepart.getNote());
		maintenanceSparepartService.updateObj(sparepart);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	
	/**
	 * 删除设备项目信息
	 * @return
	 */
	@RequestMapping("/deleteMaintenanceSparepart.do")
	@ResponseBody
	public ModelMap deleteMaintenanceSparepart(String id){
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		maintenanceSparepartService.deleteObj(Long.parseLong(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
}
