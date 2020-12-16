package com.digitzones.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.model.SparepartRecord;
import com.digitzones.devmgr.service.ISparepartRecordService;

@Controller
@RequestMapping("/AppSparepartRecord")
public class AppSparepartRecordController {
	@Autowired
	private ISparepartRecordService sparepartRecordService;
	
	/**
	 * 根据报修单id查询备件
	 */

	@RequestMapping("/querySparepartRecordBydeviceRepairOrderId.do")
	@ResponseBody
	public List<SparepartRecord> querySparepartRecordBydeviceRepairOrderId(Long deviceRepairOrderId,HttpServletRequest request){
		return sparepartRecordService.querySparepartRecordBydeviceRepairOrderId(deviceRepairOrderId);
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addSparepartRecord.do")
	@ResponseBody
	public ModelMap addSparepartRecord(Long deviceRepair_id,Long sparepart_id,Long quantity){
		ModelMap modelMap = new ModelMap();
		SparepartRecord sparepartRecord=new SparepartRecord();
		DeviceRepair deviceRepair=new DeviceRepair();
		Sparepart sparepart=new Sparepart();
		deviceRepair.setId(deviceRepair_id);
		sparepart.setId(sparepart_id);
		
		sparepartRecord.setDeviceRepair(deviceRepair);
		sparepartRecord.setSparepart(sparepart);
		sparepartRecord.setCreateDate(new Date());
		sparepartRecord.setQuantity(quantity);
		sparepartRecordService.addObj(sparepartRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
}
