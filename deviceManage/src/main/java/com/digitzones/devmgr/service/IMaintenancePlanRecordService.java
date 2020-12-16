package com.digitzones.devmgr.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.mc.model.MCUser;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.ICommonService;

public interface IMaintenancePlanRecordService extends ICommonService<MaintenancePlanRecord> {
	/**
	 * 用于定时任务，检查该记录是否已保养，如果未保养，则更改其状态
	 */
	public void updateStatus2Uncomplete();
	/**
	 * 设备保养
	 * @param record 设备保养记录对象
	 * @param itemIds 保养项id，多个保养项以逗号间隔
	 * @param results 保养结果组成的字符串，多个结果以逗号间隔
	 *//*
	public void updateMaintenancePlanRecord(MaintenancePlanRecord record, String itemIds,
			String results);*/
	/**
	 *  批量删除保养记录
	 * @param idArray
	 */
	public void deleteMaintenancePlanRecords(String[] idArray);
	/**
	 * 指定批量责任人
	 * @param params
	 */
	public void update4AssignBatchPersonInCharge(Map<String,Object> params,User user);
	/**
	 * 指定批量责任人
	 * @param params
	 */
	public void update4AssignBatchPersonInCharge4Mc(Map<String,Object> params,MCUser user);
	/**
	 * 根据年月和设备编码查询设备保养记录
	 * @param deviceCode
	 * @param year
	 * @param month
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByDeviceCodeAndMonth(String deviceCode,int year,int month);


	/**
	 * 根据保养状态和筛选条件查找保养记录
	 * @param status
	 * @return
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatusForScreen(String status,String productionLineCode,
																				   String maintainerCode,String deviceCode,String maintenanceTypeonCode,String startDate,String endDate);


	/**
	 * 根据设备编码查询设备保养记录
	 * @param deviceCode
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceCode(String deviceCode);
	/**
	 * 根据设备编码查询设备保养记录
	 * @param deviceCode
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceCodeAndUser(String deviceCode);
	/**
	 * 根据设备编码查询设备保养记录
	 * @param deviceCode
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByProductionUnitId(Long ProductionUnitId);
	/**
	 * 根据设备编码查询设备保养记录
	 * @param deviceCode
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByProductionUnitIdAndUser(Long ProductionUnitId);
	/**
	 * 根据设备编码查询设备保养记录
	 * @param deviceCode
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceId(Long deviceId);
	/**
	 * 确认保养记录
	 * @param item
	 */
	public void confirm(MaintenancePlanRecord item,Employee employee);
	/**
	 * 添加保养计划记录
	 * @param item
	 * @param employee
	 * @return
	 */
	public Serializable addMaintenancePlanRecord(MaintenancePlanRecord item,Employee employee);
	/**
	 * 根据设备id查询当天的保养记录
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByDeviceIdAndToday(Long deviceId,int year,int month,int day);
	/**
	 * 根据设备id,人员姓名，查询当天的保养记录
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(Long deviceId,int year,int month,int day,String name,String classCode);
	/**
	 * 根据设备id,人员姓名，查询当天的保养记录
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployCodeAll(Long deviceId,int year,int month,int day,String usercode);
	
	/**
	 * 根据日期查找最大保养单号
	 * @param date
	 * @return
	 */
	public String queryMaxNoByDate(Date date);
	/**
	 * 根据设备代码和用户姓名查询当日之前所有有保养计划的日期
	 */
	public List<Object[]> queryAllMaintenancePlanRecordmaintianDateByToday(String name,Long deviceId);
	/**
	 * 通过时间查询未保养的设备
	 */
	public List<Object[]> queryNotMaintenanceDeviceBytime(String startTime,String endTime);
	/**
	 * 通过时间和查询未保养记录
	 */
	public List<Object[]> queryNotMaintenanceRecordBytime(String startTime,String endTime);
	/**
	 * 根据设备代码和时间段查询保养记录
	 */
	public List<MaintenancePlanRecord> queryMaintenanceRecordByDeviceCodeAndTimeSlot(Long deviceId,String startDate,String endDate);
	/**
	 * 根据时间段、设备类别id和生成单元id查询保养记录
	 * @param startDate
	 * @param endDate
	 * @param productLine
	 * @param deviceType
	 * @return
	 */
	public List<MaintenancePlanRecord> queryMaintenanceRecordByProductLineIdAndDeviceTypeIdAndTimeSlot(String startDate,String endDate,Long productLineId,Long deviceTypeId);
	/**
	 * 根据保养单状态(+用户)查询保养计划
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatus(String status,String usercode);
	/**
	 * 获取当前人员的待接单保养单
	 */
	public List<MaintenancePlanRecord> queryReceiptMPRWithUser(String usercode);
	/**
	 * 获取当前人员的维修中保养单
	 */
	public List<MaintenancePlanRecord> queryMaintenanceMPRWithUser(String usercode);
	
	/**
	 * 根据id查询保养单的保养人员
	 */
	public List<MaintenanceUser> queryMaintenanceUserByRecordId(Long recordId);
	/**
	 * 
	 */
	public void confirmMaintenance(MaintenancePlanRecord item,String username,String status);
	/**
	 * 查询全年的保养记录
	 * @param deviceId
	 * @return
	 */
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecords4CurrentYear(Long deviceId);
	/**
	 * 根据确认人查询待确认保养计划
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByConfirmCodeAndConfirm(String confirmCode);
	/**
	 * 根据日期查找保养记录数
	 * @param date
	 * @return
	 */
	public Long queryMaintenancePlanRecordCountByDate(Date date);
	/**
	 * 根据保养状态查找保养记录
	 * @param status
	 * @return
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatus(String status);
	/**
	 * 统计数据
	 * @param from 起始日期
	 * @param to 终止日期
	 * @param cycle 统计周期
	 * @return
	 */
	public List<String[]> queryStatisticsData(String from,String to,String cycle);
	public List<String[]> queryOverviewData(String from, String to);
}
