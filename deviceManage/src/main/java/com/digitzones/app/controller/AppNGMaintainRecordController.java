package com.digitzones.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.NGMaintainRecord;
import com.digitzones.devmgr.service.IDeviceProjectService;
import com.digitzones.devmgr.service.INGMaintainRecordService;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProjectTypeService;
import com.digitzones.service.impl.ProjectTypeServiceImpl;

@Controller
@RequestMapping("/AppNGMaintainRecord")
public class AppNGMaintainRecordController {
	@Autowired
	private INGMaintainRecordService ngMaintainRecordService;
	@Autowired
	private IProjectTypeService projectTypeService;
	@Autowired
	private IDeviceProjectService deviceProjectService;
	
	/**
	 * 通过设备报修单id查询故障原因
	 * @return
	 */
	@RequestMapping("/queryNGMaintainRecordByDeviceRepairOrderId.do")
	@ResponseBody
	public List<NGMaintainRecord> queryNGMaintainRecordByDeviceRepairOrderId(Long deviceRepairOrderId,HttpServletRequest request){
		return ngMaintainRecordService.queryNGMaintainRecordByDeviceRepairOrderId(deviceRepairOrderId);
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addMaintainProject.do")
	@ResponseBody
	public ModelMap addNGMaintainRecord(Long deviceRepair_id,Long pressLight_id,String method,String note){
		ModelMap modelMap = new ModelMap();
		NGMaintainRecord ngMaintainRecord=new NGMaintainRecord();
		DeviceRepair deviceRepair=new DeviceRepair();
		DeviceProject deviceProject=new DeviceProject();
		deviceRepair.setId(deviceRepair_id);
		deviceProject=deviceProjectService.queryObjById(pressLight_id);
		ngMaintainRecord.setDeviceRepair(deviceRepair);
		ngMaintainRecord.setDeviceProject(deviceProject);
		ngMaintainRecord.setNote(note);
		ngMaintainRecord.setProcessingMethod(method);
		ngMaintainRecord.setCreateDate(new Date());
		ngMaintainRecordService.addObj(ngMaintainRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	
	
}
