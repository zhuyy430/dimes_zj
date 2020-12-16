package com.digitzones.devmgr.dao;

import java.util.Date;
import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.CheckingPlanRecord;
public interface ICheckingPlanRecordDao extends ICommonDao<CheckingPlanRecord> {
	/**
	 * 用于定时任务，检查该记录是否已点检，如果未点检，则更改其状态
	 */
	public void updateStatus2Uncomplete();
	/**
	 * 根据年月和设备编码查询设备点检记录
	 * @param deviceCode
	 * @param year
	 * @param month
	 * @return 
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndMonth(String deviceCode,int year,int month);
	/**
	 * 通过时间查询未点检的设备
	 */
	public List<Object[]> queryNotSpotcheckDeviceBytime(String startTime,String endTime);
	/**
	 * 通过时间和查询未点检记录
	 */
	public List<Object[]> queryNotSpotcheckRecordBytime(String startTime,String endTime);
	/**
	 * 根据日期查询最大点检单号
	 * @param date
	 * @return
	 */
	public String queryMaxNoByDate(Date date);
	
	/**
	 * 统计数据
	 * @param from 起始日期
	 * @param to 终止日期
	 * @param cycle 统计周期
	 * @return
	 */
	public List<String[]> queryStatisticsData(String from, String to, String cycle);
	/**
	 * 查询设备点检统计记录
	 * @param from
	 * @param to
	 * @return
	 */
	public List<String[]> queryOverviewData(String from, String to);
}
