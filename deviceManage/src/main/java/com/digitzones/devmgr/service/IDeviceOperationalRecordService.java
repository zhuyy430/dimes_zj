package com.digitzones.devmgr.service;

import java.util.Date;
import java.util.List;

import com.digitzones.devmgr.model.DeviceOperationalRecord;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface IDeviceOperationalRecordService extends ICommonService<DeviceOperationalRecord>{

	/**
	 * 获取最后一条数据
	 * @return
	 */
	public Double queryAllRunTimeByDeviceId(Long deviceId);
	/**
	 * 
	 * @return
	 */
	public List<?> queryDeviceOperationalRecordForDeverId(Long deviceId,String classesCode,Date date);
}
