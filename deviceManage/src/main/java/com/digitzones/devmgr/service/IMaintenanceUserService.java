package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.model.Employee;
import com.digitzones.service.ICommonService;

public interface IMaintenanceUserService extends ICommonService<MaintenanceUser> {
	/**
	 * 根据保养计划记录id查找责任人
	 * @param maintenancePlanRecordId
	 * @return
	 */
	public MaintenanceUser queryPersonInChargeByMaintenancePlanRecordId(Long maintenancePlanRecordId);
	/**
	 * 根据保养计划记录id查找责任人
	 * @param maintenancePlanRecordId
	 * @return
	 */
	public MaintenanceUser queryPersonInChargeByMaintenancePlanRecordIdAndEmployeeName(Long maintenancePlanRecordId,String employeeName);
	/**
	 *  添加保养人员：责任人或协助人员
	 * @param maintenanceUser
	 * @param employee
	 */
	public void addMaintenanceUser(MaintenanceUser maintenanceUser, Employee employee);
	/**
	 * 根据保养记录ID获取保养人员信息
	 * @param id
	 */
	public List<MaintenanceUser> queryMaintenanceUserByRecordId(Long id);
	/**
	 * 根据保养计划记录id和name查找保养人
	 * @param maintenancePlanRecordId
	 * @return
	 */
	public MaintenanceUser queryPersonInChargeByMaintenancePlanRecordIdAndName(Long maintenancePlanRecordId,String name);
	/**
	 * 根据保养计划记录id查找责任人
	 * @param maintenancePlanRecordId
	 * @return
	 */
	public List<MaintenanceUser> queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(Long maintenancePlanRecordId);
	/**
	 * 根据保养计划记录id查找协助人
	 * @param maintenancePlanRecordId
	 * @return
	 */
	public List<MaintenanceUser> queryHelpMaintenanceUserByMaintenancePlanRecordId(Long maintenancePlanRecordId);
	/**
	 * 根据保养记录状态查找责任人
	 * @param status
	 * @return
	 */
	public List<MaintenanceUser> queryWithMaintenancePlanRecordStatus(String status);
	/**
	 * 根据保养记录状态查找责任人
	 * @param status
	 * @param employeeCode
	 * @return
	 */
	public List<MaintenanceUser> queryWithMaintenancePlanRecordStatus(String status,String employeeCode);
}
