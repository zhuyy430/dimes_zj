package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.service.ICommonService;

/**
 * 设备项目标准记录接口
 */
public interface IDeviceProjectRecordService extends ICommonService<DeviceProjectRecord>{
	/**
	 * 根据设备id查找设备项目(点检，润滑等)的标准值记录
	 * @param deviceId
	 * @return
	 */
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceId(Long deviceId);
	
	/**
	 * 设备id通过和项目type查找关联项目
	 */
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceIdAndtype(Long deviceId,String type);
	/**
	 * 设备id通过和项目type和班次代码查找关联项目
	 */
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceIdAndtypeAndClassesCode(Long deviceId,String type,String classesCode);
}
