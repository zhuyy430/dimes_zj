package com.digitzones.devmgr.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.MaintenanceStaff;
/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
public interface IMaintenanceStaffDao extends ICommonDao<MaintenanceStaff> {
	/**
	 * 根据生产单元和员工在岗状态获取数据
	 * @param productionUnitId
	 * @param status
	 * @return
	 */
	public List<MaintenanceStaff> findListByProductionUnitIdAndStatus(Long productionUnitId,String status);
	/**
	 * 根据员工在岗状态获取数据
	 * @param productionUnitId
	 * @param status
	 * @return
	 */
	public List<MaintenanceStaff> findListByStatus(String status);
}
