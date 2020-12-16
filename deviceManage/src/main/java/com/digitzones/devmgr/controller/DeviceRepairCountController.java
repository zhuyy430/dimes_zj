package com.digitzones.devmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.DeviceRepairCount;
import com.digitzones.devmgr.service.IDeviceRepairCountService;
@RestController
@RequestMapping("/deviceRepairOrderCount")
public class DeviceRepairCountController {
	@Autowired
	private IDeviceRepairCountService deviceRepairCountService;
	
	@RequestMapping("/queryDeviceRepairCount.do")
	@ResponseBody
	public DeviceRepairCount queryDeviceRepairCount() {
		return deviceRepairCountService.queryDeviceRepairCount();
	}
}
