package com.digitzones.devmgr.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.DeviceSparepartRecord;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.IDeviceSparepartRecordService;
import com.digitzones.devmgr.service.ISparepartService;
import com.digitzones.model.Device;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceService;
@RestController
@RequestMapping("/deviceSparepartRecord")
public class DeviceSparepartRecordController {
	@Autowired
	private IDeviceSparepartRecordService deviceSparepartRecordService;
	@Autowired
	private ISparepartService sparepartService;
	@Autowired
	private IDeviceService deviceService;
	/**
	 * 根据工件类别id查找工件信息
	 * @return
	 */
	@RequestMapping("/queryDeviceSparepartRecordsByDeviceId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryDeviceSparepartRecordsByDeviceId(Long deviceId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<DeviceSparepartRecord> pager = deviceSparepartRecordService.queryObjs("select d from DeviceSparepartRecord d where d.deviceId=?0 and d.unbindDate is null", page, rows, new Object[] {deviceId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
	
	/**
	 * 添加备件信息
	 * @param sparepart
	 * @return
	 */
	@RequestMapping("/addDeviceSparepartRecord.do")
	public ModelMap addDeviceSparepartRecord(Long deviceId,String sparepartIds) {
		ModelMap modelMap = new ModelMap();
		if(sparepartIds!=null) {
			if(sparepartIds.contains("[")) {
				sparepartIds = sparepartIds.replace("[", "");
			}
			if(sparepartIds.contains("]")) {
				sparepartIds = sparepartIds.replace("]", "");
			}

			String[] idArray = sparepartIds.split(",");
			Device device = deviceService.queryObjById(deviceId);
			for(String id:idArray){
				Sparepart sparepart = sparepartService.queryObjById(Long.parseLong(id));
				
				DeviceSparepartRecord deviceSparepartRecord = new DeviceSparepartRecord();
				
				deviceSparepartRecord.setSparepartId(sparepart.getId());
				deviceSparepartRecord.setSparepartCode(sparepart.getCode());
				deviceSparepartRecord.setSparepartName(sparepart.getName());
				deviceSparepartRecord.setUnitType(sparepart.getUnitType());
				deviceSparepartRecord.setMeasurementUnit(sparepart.getMeasurementUnit());
				deviceSparepartRecord.setDeviceId(device.getId());
				deviceSparepartRecord.setDeviceCode(device.getCode());
				deviceSparepartRecord.setDeviceName(device.getName());
				deviceSparepartRecord.setBindDate(new Date());
				deviceSparepartRecordService.addObj(deviceSparepartRecord);
			}
		}
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDeviceSparepartRecordById.do")
	public DeviceSparepartRecord queryDeviceSparepartRecordById(Long id) {
		return deviceSparepartRecordService.queryObjById(id);
	}
	/**
	 * 更新备件信息
	 * @param sparepart
	 * @return
	 */
	@RequestMapping("/updateDeviceSparepartRecord.do")
	public ModelMap updateDeviceSparepartRecord(DeviceSparepartRecord deviceSparepartRecord) {
		ModelMap modelMap = new ModelMap();
		DeviceSparepartRecord dsr = deviceSparepartRecordService.queryObjById(deviceSparepartRecord.getId());
		dsr.setBindDate(new Date());
		dsr.setUseCount(deviceSparepartRecord.getUseCount());
		dsr.setNote(deviceSparepartRecord.getNote());
		deviceSparepartRecordService.updateObj(dsr);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 根据id删除备品备件 信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDeviceSparepartRecord.do")
	@ResponseBody
	public ModelMap deleteDeviceSparepartRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();

		DeviceSparepartRecord deviceSparepartRcord = deviceSparepartRecordService.queryObjById(Long.valueOf(id));
		deviceSparepartRcord.setUnbindDate(new Date());
		deviceSparepartRecordService.updateObj(deviceSparepartRcord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	
	
}
