package com.digitzones.devmgr.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
public interface IMaintenanceStaffRecordDao extends ICommonDao<MaintenanceStaffRecord> {

	/**
	 * 根据报修单ID查询维修记录
	 * @param orderId
	 * @return
	 */
	public List<MaintenanceStaffRecord> findByOrderId(Long orderId);
}
