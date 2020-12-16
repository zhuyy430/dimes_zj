package com.digitzones.devmgr.service;

import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.service.ICommonService;

import java.util.List;

public interface ICheckingPlanRecordService extends ICommonService<CheckingPlanRecord> {
	/**
	 * 查找所有点检记录
	 * @return
	 */
	public List<CheckingPlanRecord> queryAllCheckingPlanRecords();
	/**
	 * 批量删除点检记录
	 * @param idArray
	 */
	public void deleteChekingPlanRecords(String[] idArray);
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
	 * 设备点检
	 * @param record 设备点检记录对象
	 * @param itemIds 点检项id，多个点检项以逗号间隔
	 * @param results 点检结果组成的字符串，多个结果以逗号间隔
	 * @param notes 备注组成的字符串，多个结果以,@间隔
	 */
	public void updateCheckingPlanRecord(CheckingPlanRecord record, String itemIds, String results,String notes,String checkValue);
	
	/**
	 * 根据设备编码查询点检记录
	 * @param deviceCode
	 * @return
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCode(String deviceCode);
	/**
	 * 根据设备和年月日查询当前班次的设备点检记录
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndDateTime(String deviceCode,int year,int month,int day,String classCode);
	/**
	 * 根据设备和年月日查询当前班次的设备点检记录
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndDate(String deviceCode,int year,int month,int day);
	/**
	 * 根据生产单元和年月日查询当前班次的设备点检记录
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByProductionUnitIdAndDate(String ProductionUnitId,int year,int month,int day);
	/**
	 * 设备点检（单个点检项）
	 */
	public void updateCheckingPlanRecordByOne(String result,Long id,String username,String note,String checkValue);
	/**
	 * 根据设备和年月日班次查询设备点检记录
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndSelectTime(String deviceCode,int year,int month,int day,String classCode,String status);
	/**
	 * 通过时间查询未点检的设备
	 */
	public List<Object[]> queryNotSpotcheckDeviceBytime(String startTime,String endTime);
	/**
	 * 通过时间和查询未点检记录
	 */
	public List<Object[]> queryNotSpotcheckRecordBytime(String startTime,String endTime);
	/**
	 * 查找生产单元下的点检记录
	 * @param productionUnitId
	 * @return
	 */
	public List<CheckingPlanRecord> queryCheckingPlanRecordByProductionUnitId(Long productionUnitId);
	
	/**
	 * 统计数据
	 * @param from 起始日期
	 * @param to 终止日期
	 * @param cycle 统计周期
	 * @return
	 */
	public List<String[]> queryStatisticsData(String from,String to,String cycle);
	/**
	 * 查询设备点检数据
	 * @param from
	 * @param to
	 * @return
	 */
	public List<String[]> queryOverviewData(String from, String to);
	
	
}
