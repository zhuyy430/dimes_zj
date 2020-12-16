package com.digitzones.devmgr.dao;

import java.util.Date;
import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.DeviceOperationalRecord;
/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
public interface IDeviceOperationalRecordDao extends ICommonDao<DeviceOperationalRecord> {
	
	/**
	 * 获取所有记录
	 * @return
	 */
	public Double queryAllObj(Long deviceId);
	/**
	 * 获取所有记录
	 * @return
	 */
	public List<?> queryDeviceOperationalRecordForDeverId(Long deviceId,String classesCode,Date date);
}
