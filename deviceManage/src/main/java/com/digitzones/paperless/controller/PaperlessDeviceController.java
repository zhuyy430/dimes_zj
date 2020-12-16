package com.digitzones.paperless.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Device;
import com.digitzones.service.IDeviceService;

@Controller
@RequestMapping("/paperlessDevice")
public class PaperlessDeviceController {
	@Autowired
	private IDeviceService deviceService;
	/**
	 * 根据设备code查找设备
	 */
	@RequestMapping("/queryDeviceByCode.do")
	@ResponseBody
	public Device queryDeviceByCode(String name,String value) {
		return deviceService.queryByProperty(name, value);
	}
}
