package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface IMaintenanceStaffRecordService extends ICommonService<MaintenanceStaffRecord>{

	/**
	 * 根据报修单查询维修记录
	 * @param orderId
	 * @return
	 */
	public List<MaintenanceStaffRecord> queryByOrderId(Long orderId);
	/**
	 * 根据报修单号和维修员工查询维修记录 
	 */
	public List<MaintenanceStaffRecord> queryByOrderIdandUsercode(Long orderId,String usercode);
	/**
	 *  根据报修单查询责任人维修记录
	 */
	public List<MaintenanceStaffRecord> queryMaintenanceStaffRecordPersonLiableByorderId(Long orderId);
	/**
	 *  根据报修单查询协助人维修记录
	 */
	public List<MaintenanceStaffRecord> queryMaintenanceStaffRecordHelpByorderId(Long orderId);
	/**
	 * 根据维修单状态查找维修记录 
	 * @param status
	 * @return
	 */
	public List<MaintenanceStaffRecord> queryWithDeviceRepairStatus(String status);
	/**
	 * 根据维修单状态和员工编码查找维修记录 
	 * @param status
	 * @param employeeCode
	 * @return
	 */
	public List<MaintenanceStaffRecord> queryWithDeviceRepairStatus(String status,String employeeCode);
}
