package com.digitzones.devmgr.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.devmgr.model.DeviceRepairPic;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface IDeviceRepairOrderPicService extends ICommonService<DeviceRepairPic>{
	public Serializable addOrderPic(DeviceRepairPic deviceRepairPic,File file);
	public List<DeviceRepairPic> queryListByProperty(Long deviceRepairId) ;
	
	public Serializable addDeviceRepairOrderPic(Long orderId,File file);
	
	/**
	 * 根据属性和属性值获取数据
	 * @param name
	 * @param value
	 * @return
	 */
	public List<DeviceRepairPic> queryListByProperty(String name, String value);
	
}
