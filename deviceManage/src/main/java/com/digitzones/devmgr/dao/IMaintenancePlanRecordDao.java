package com.digitzones.devmgr.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
public interface IMaintenancePlanRecordDao extends ICommonDao<MaintenancePlanRecord> {
	/**
	 * 用于定时任务，检查该记录是否已点检，如果未点检，则更改其状态
	 */
	public void updateStatus2Uncomplete();
	/**
	 * 根据年月和设备编码查询设备保养记录
	 * @param deviceCode
	 * @param year
	 * @param month
	 * @return 
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByDeviceCodeAndMonth(String deviceCode, int year,
			int month);
	/**
	 * 指定批量责任人
	 * @param params
	 */
	public void update4AssignBatchPersonInCharge(Map<String, Object> params);
	/**
	 * 根据设备id,人员姓名，查询当天的保养记录
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(Long deviceId,int year,int month,int day,String name,String classCode);
	/**
	 * 根据设备id,人员姓名，查询当天的保养记录
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployCodeAll(Long deviceId,int year,int month,int day,String usercode);
	
	/**
	 * 根据日期 查找最大保养单号
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
	 * 根据保养单状态(+用户)查询保养计划
	 */
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatus(String status,String user);
	/**
	 * 获取当前人员的待接单保养单
	 */
	public List<MaintenancePlanRecord> queryReceiptMPRWithUser(String usercode);
	/**
	 * 获取当前人员的维修中保养单
	 */
	public List<MaintenancePlanRecord> queryMaintenanceMPRWithUser(String usercode);
	/**
	 * 统计数据
	 * @param from 起始日期
	 * @param to 终止日期
	 * @param cycle 统计周期
	 * @return
	 */
	public List<String[]> queryStatisticsData(String from, String to, String cycle);
	public List<String[]> queryOverviewData(String from, String to);
}
