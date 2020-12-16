package com.digitzones.paperless.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.model.Device;
import com.digitzones.model.Employee;
import com.digitzones.service.IDeviceService;
/**
 * 点检记录
 * @author zdq
 * 2019年3月8日
 */
@RestController
@RequestMapping("paperlessDeviceRepair")
public class PaperlessDeviceRepairController {
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService; 
	/**
	 * 根据设备代码查找点检记录
	 * @param deviceCode
	 * @return List<CheckingPlanRecord>
	 */
	@RequestMapping("/queryPaperLessDeviceRepairOrderByDeviceCode.do")
	public List<DeviceRepair> queryPaperLessDeviceRepairOrderByDeviceCode(String deviceCode,Long productionUnitId){
		if(!StringUtils.isEmpty(deviceCode)) {
			return deviceRepairOrderService.queryDeviceRepairOrderByDeviceCode2(deviceCode);
		}
		return deviceRepairOrderService.queryDeviceRepairOrderByProductionUnitId(productionUnitId);
	}
}
