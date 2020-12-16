package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface IMaintenanceStaffService extends ICommonService<MaintenanceStaff>{
	
	/**
	 * 切换状态
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateStatus(Long id,String status,String userName);
	/**
	 * 切换状态
	 * @param m
	 * @param status
	 * @param userName
	 * @return
	 */
	public boolean updateStatus(MaintenanceStaff m, String status,String userName);
	/**
	 * 根据生产单元和员工在岗状态获取数据
	 * @param productionUnitId
	 * @param status
	 * @return
	 */
	public List<MaintenanceStaff> queryListByProductionUnitIdAndStatus(Long productionUnitId,String status);
	/**
	 * 根据生产单元和员工在岗状态获取数据
	 * @param productionUnitId
	 * @param status
	 * @return
	 */
	public List<MaintenanceStaff> queryListByStatus(String status);
	/**
	 * 查询所有维修人员
	 * @return
	 */
	public List<MaintenanceStaff> queryAllMaintenanceStaff();
	/**
	 * 切换工作状态
	 * @param m
	 * @param status
	 * @param userName
	 * @return
	 */
	public boolean updateWorkStatus(MaintenanceStaff m, String status,String userName);
	/**
	 * 查询维修人员在岗人数(维修，在岗，保养)
	 * @return
	 */
	public Long queryCountOnPosition();
}
