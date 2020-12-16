package com.digitzones.devmgr.service;

import com.digitzones.devmgr.model.DeviceRepairCount;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface IDeviceRepairCountService extends ICommonService<DeviceRepairCount>{
	
	/**
	 * 获取当天数据的信息
	 * @return
	 */
	public DeviceRepairCount queryDeviceRepairCount();
	/**
	 * 获取最后一条数据的信息
	 * @return
	 */
	public DeviceRepairCount queryLastDeviceRepairCount();
	
}
