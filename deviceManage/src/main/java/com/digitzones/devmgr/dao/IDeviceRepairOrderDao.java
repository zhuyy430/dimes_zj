package com.digitzones.devmgr.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.DeviceRepair;
/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
public interface IDeviceRepairOrderDao extends ICommonDao<DeviceRepair> {
	
	 /**
	  * 获取新增报警报修单
	  * @param alarmedIds
	  * @return
	  */
	 public List<DeviceRepair> findAlarmsCount(String alarmedIds);
	 /**
	  * 获取未分配的报修单
	  * @return
	  */
	 public List<DeviceRepair> findFirstDeviceRepair();
	 /**
	  * 根据维修状态获取当前用户的维修单
	  */
	 public List<DeviceRepair> queryDeviceRepairWithUserandStatus(String status,String username);
	 
	 /**
	  * 获取当前人员的待接单维修单
	  */
	public List<DeviceRepair>  queryReceiptDeviceRepairWithUser(String code);
	
	/**
	 * 获取当前人员的维修中维修单
	 */
	public List<DeviceRepair>  queryMaintenanceDeviceRepairWithUser(String code);
	/**
	 * 获取某状态得所有维修单
	 */
	public List<DeviceRepair> queryMaintenanceDeviceRepairWithStatus(String status);
	
	/**
	 * 查询角标对应的数据数量
	 */
	public int queryBadgeWithDeviceRepair(String hql);
}
