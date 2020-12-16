package com.digitzones.devmgr.dao;
import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.MaintenanceUser;
/**
 * 保养人员dao
 * @author zdq
 * 2019年1月5日
 */
public interface IMaintenanceUserDao extends ICommonDao<MaintenanceUser> {
/**
 * 根据保养计划记录id删除责任人
 * @param id
 */
	void deleteByMaintenancePlanRecordId(Long id);

}
