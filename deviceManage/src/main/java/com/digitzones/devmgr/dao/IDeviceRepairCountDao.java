package com.digitzones.devmgr.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.DeviceRepairCount;
/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
public interface IDeviceRepairCountDao extends ICommonDao<DeviceRepairCount> {
	
	/**
	 * 获取当天数据的信息
	 * @return
	 */
	public List<DeviceRepairCount> findDeviceRepairCount();
	/**
	 * 获取最后一条数据的信息
	 * @return
	 */
	public List<DeviceRepairCount> findLastDeviceRepairCount();
}
