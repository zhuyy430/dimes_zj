package com.digitzones.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.model.Classes;
import com.digitzones.model.LostTimeRecord;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
/**
 * 损时记录service
 * @author zdq
 * 2018年6月11日
 */
public interface ILostTimeRecordService extends ICommonService<LostTimeRecord> {
	/**
	 * 根据年和月份查询当前月的损时记录
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Object[]> queryLostTimeRecordByYearAndMonth(Integer year,Integer month);

	/**
	 * 根据生产单元和时间查询损时记录
	 */
	public List<Object[]> queryLostTimeRecordByProductionIdsAndYearAndMonth(Integer year,Integer month,Integer day,String productionUnitIds);

	/**
	 * 根据年查询所有月的损时记录
	 * @param year
	 * @return
	 */
	public List<Object[]> queryAllMonthLostTimeRecordFor1Year(Integer year);
	/**
	 * 根据年月信息查询当月的总共的损时数
	 * @param year
	 * @param month
	 * @return
	 */
	public Double queryHoursOfLostTimeRecordByYearAndMonth(Integer year,Integer month) ;
	/**
	 * 根据年月信息查询当月的总共的计划停机数
	 * @param year
	 * @param month
	 * @return
	 */
	public Double queryHoursOfPlanHaltByYearAndMonth(Integer year,Integer month) ;
	/**
	 * 查询当天损时时间
	 * @param c
	 * @param deviceSiteId
	 * @param date 损时日期
	 * @return
	 */
	public Double queryLostTime(Classes c,Long deviceSiteId,Date date);
	/**
	 * 查询当天损时时间
	 * @param c
	 * @param date 损时日期
	 * @return 0:设备站点id  1：损时数
	 */
	public List<Object[]> queryLostTime(Classes c,Date date);
	/**
	 * @param deviceSiteId
	 * @param date 损时日期
	 * @return
	 */
	public Double queryLostTimeByClassesAndProductionUnit(Classes c,Long productionUnitId,Date date);
	/**
	 * 查询当前班次到该月目前为止的损时时间和计划停机时间
	 * @param c
	 * @param now
	 * @return 0:日期 1：损时时间  2：计划停机时间
	 */
	public List<Object[]> queryLostTimeAndPlanHaltTime(Classes c , Date now);
	/**
	 * 查询给定月份的每个班次，每天的损时信息
	 * @param date
	 * @return 0:班次代码  	1：日期  	2：损时时间(分钟)
	 */
	public List<Object[]> queryOneMonthLostTime(Date date);
	/**
	 * 查询当天计划停机时间
	 * @param c
	 * @param deviceSiteId
	 * @return
	 */
	public Double queryPlanHaltTime(Classes c,Long deviceSiteId,Date date);
	/**
	 * 查询当天计划停机时间
	 * @param c
	 * @param date
	 * @return 0:设备站点id  1：计划停机数
	 */
	public List<Object[]> queryPlanHaltTime(Classes c,Date date);
	/**
	 * @param deviceSiteId
	 * @return
	 */
	public Double queryPlanHaltTimeByClassesAndProductionUnit(Classes c,Long productionUnitId,Date date);
	/**
	 * 查询给定日期月份的计划停机数
	 * @param date
	 * @return
	 */
	public List<Object[]> queryOneMonthPlanHaltTime(Date date);
	/**
	 * 每天损时
	 * @param c
	 * @param deviceSiteId
	 * @param date
	 * @return
	 */
	public Double queryLostTime4PerDay(Classes c,Long deviceSiteId,Date date);
	/**
	 * 每天损时
	 * @param c
	 * @param deviceSiteId
	 * @param date
	 * @return
	 */
	public Double queryLostTime4PerDay(Classes c,Date date);
	/**
	 * 每月损时
	 * @param c
	 * @param date
	 * @return 0:日期  1：损时数
	 */
	public List<Object[]> queryLostTime4PerMonth(Classes c,Date date);
	/**
	 * @param c
	 * @param deviceSiteId
	 * @param date
	 * @return
	 */
	public Integer queryLostTimeForPerDay(Classes c, Date date, Long productionUnitId);
	/**
	 * 根据年月日时分查询损时时间
	 * @param date
	 * @return
	 */
	public Integer queryLostTime4RealTime(Date date);
	/**
	 * 向前查询十二小时损时记录
	 * @return
	 */
	public List<LostTimeRecord> queryLostTime4RealTime();
	/**
	 * 损时确认
	 * @param lostTimeRecord
	 */
	public void confirm(LostTimeRecord lostTimeRecord,User user,Map<String,Object> args);
	/**
	 * 添加损时记录
	 * @param lostTimeRecord
	 * @param user
	 * @return
	 */
	public Serializable addLostTimeRecord(LostTimeRecord lostTimeRecord,User user,Map<String,Object> args);
	/**
	 * 设置删除标志
	 * @param lostTimeRecord
	 */
	public void deleteLostTimeRecord(LostTimeRecord lostTimeRecord);
	/**
	 * 查询从月初到给定时间的总损时(工厂级)
	 * @param date
	 * @param halt null:查询所有   true:只查询计划停机   false：只查询非计划停机
	 * @return
	 */
	public Integer queryLostTimeFromBeginOfMonthUntilTheDate(Date date,Boolean halt);
	/**
	 * 查询指定日期(年月日)的损时记录数
	 * @param date
	 * @return
	 */
	public Long queryLostTime4TheDate(Date date);
	/**
	 * 查询每种损时类型的损时数
	 * @param c
	 * @param date
	 * @return
	 */
	public List<Object[]> queryLostTimePerLosttimeType(Classes c,String deviceSiteCode);
	/**
	 * 查询当前生产单元下每种损时类型的损时数
	 * @param c
	 * @return
	 */
	public List<Object[]> queryLostTimePerLosttimeType4ProductionUnit(Classes c,Long productionUnitId);
	/**
	 * 查询当前班次的损时时间
	 * @param c
	 * @param deviceSiteId
	 * @param date 损时日期
	 * @return
	 */
	public Double queryLostTime4Classes(Classes c,String deviceSiteCode);
	/**
	 * 查询当前班次的,当前产线的损时时间
	 * @param c
	 * @param productionUnitId
	 * @return
	 */
	public Double queryLostTime4ProductionUnit(Classes c,Long productionUnitId);
	/**
	 * 查询 班次的未处理损时数
	 * @param c
	 * @return
	 */
	public long queryUnhandledLostTimeRecordCount4Classes(Classes c); 
	/**
	 * 查询当前设备站点下最后一条没有结束的损时记录
	 * @param deviceSiteId
	 * @return
	 */
	public LostTimeRecord queryUnEndLastLostTimeRecord(Long deviceSiteId);
	/**
	 * 损时时间汇总表
	 * @param params
	 * @return
	 */
	public Pager<List<Object[]>> queryLostTimeCountReport(Map<String,String> params,int rows,int page);
	/**
	 * 查询损时类别详情
	 * @param typeName 类别名称(父类别)
	 * @param month 月份
	 * @return
	 */
	public List<Object[]> queryLostTimeDetail(String typeName,int month);
	/**
	 * 查询每月的损时
	 * @param year
	 * @return
	 */
	public List<?> queryHoursOfLostTimeRecord(Integer year);
	/**
	 * 根据设备站点id查找所有未结束的损时记录
	 * @param deviceSiteId
	 * @return
	 */
	public List<LostTimeRecord> queryUnEndLastLostTimeRecords(Long deviceSiteId);
}
