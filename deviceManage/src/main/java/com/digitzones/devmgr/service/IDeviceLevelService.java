package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.DeviceLevel;
import com.digitzones.service.ICommonService;
/**
 * 设备等级service
 * @author zdq
 * 2019年3月15日
 */
public interface IDeviceLevelService extends ICommonService<DeviceLevel> {
	/**
	 * 查询所有设备，用于树形结构
	 * @return
	 */
	public List<DeviceLevel> queryTopDeviceLevels();
	/**
	 * 查询所有的设备等级(去除最顶层设备等级)
	 * @return
	 */
	public List<DeviceLevel> queryAllDeviceLevels();
}
