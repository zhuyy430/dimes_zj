package com.digitzones.dao;

import com.digitzones.model.Classes;
import com.digitzones.model.ProcessRecord;

import java.util.Date;
import java.util.List;

/**
 * 加工记录dao
 * @author zdq
 * 2018年6月20日
 */
public interface IProcessRecordDao extends ICommonDao<ProcessRecord> {
	/**
	 * 根据设备站点id查询当天的加工数量
	 * @param deviceSiteId
	 * @return 
	 */
	public Long queryCountByDeviceSiteIdAndNotNg(Long deviceSiteId);
	/**
	 * 查询当天的加工数量
	 * @param deviceSiteId
	 * @return
	 */
	public Long queryCurrentDayCountByDeviceSiteId(Long deviceSiteId);
	/**
	 * 查询当前月工序、工件、设备站点下的,当前班次的，该状态的加工记录数
	 * @param deviceSiteId
	 * @param status
	 * @return List<Long[]> :工件id，工序id，设备站点id,班次id，记录数
	 */
	public List<Long[]> queryCurrentMonthDeviceSiteIdAndStatus(Long deviceSiteId, String status);
	/**
	 * 根据日期(天)查询工序、工件、设备站点id及该状态下的记录数
	 * @param deviceSiteId
	 * @param status
	 * @param day
	 * @return
	 */
	public List<Long[]> queryByDay(Long deviceSiteId, String status, Date day);
	/**
	 * 查找当前月下的人员产量
	 * @param year
	 * @param month
	 * @param empId
	 * @return
	 */
	public Integer queryOutput4EmployeePerMonth(int year, int month, Long empId);
	/**
	 * 查找当年每月份的人员产量
	 * @param date
	 * @param empId
	 * @return
	 */
	public List<Object[]> queryAllMonthOutput4EmployeePerYear(int year);
	/**
	 * 查找当前月下的工序产量
	 * @param year
	 * @param month
	 * @param processId
	 * @return
	 */
	public Integer queryOutput4ProcessPerMonth(int year, int month, Long processId);
	/**
	 * 查找当年每月的工序产量
	 * @param date
	 * @param processId
	 * @return
	 */
	public List<Object[]> queryAllMonthOutput4ProcessPerYear(int year);
	/**
	 * 查找当前月下的设备站点产量
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer queryOutput4DeviceSitePerMonth(int year, int month, Long deviceSiteId);
	/**
	 * 查找当年每月的站点产量
	 * @param date
	 * @return
	 */
	public List<Object[]> queryAllMonthOutput4DeviceSitePerYear(int year);
	/**
	 * 查找当前月 下的不合格数
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer queryWorkSheetNGCountPerMonth(int year, int month);
	/**
	 * 查找当月 每天的不合格数
	 * @param date
	 * @return
	 */
	public List<Object[]> queryAllDayWorkSheetNGCountPerByMonth(int year, int month);
	/**
	 * 查找报废品数量
	 * @param year
	 * @param month
	 * @param ngTypeId
	 * @return
	 */
	public Integer queryWorkSheetScrapCountPerMonth(int year, int month, Long ngTypeId);
	/**
	 * 查找当月所有类别的报废数量
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer queryWorkSheetScrapCountPerMonth(int year, int month);
	/**
	 * 查找当年所有的报废数量，按月份分组
	 * @param date
	 * @return 0:日期  1：报废数量
	 */
	public List<Object[]> queryWorkSheetScrapCount4Year(Date date);
	/**
	 * 查找当前月下的合格数
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer queryWorkSHeetNotNGCountPerMonth(int year, int month);
	/**
	 * 查找当前天，当前班次的不合格数
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(int year, int month, int day, Long classId, Long productionUnitId);
	/**
	 * 查询给定日期的生产数
	 * @param date
	 * @return
	 */
	public Integer queryCountTheDate(Date date);
	/**
	 * 查询给定月每日的生产数
	 * @param date
	 * @return
	 */
	public List<Object[]> queryAllDayCountByTheMonth(int year, int month);
	/**
	 * 查找当天当前班次的总数
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer querySumCountPerClasses4ProductionUnit(int year, int month, int day, Long classId, Long productionUnitId);
	/**
	 * 根据班次id和日查找产量
	 * @param classesId
	 * @param day
	 * @return
	 */
	public Integer queryCountByClassesIdAndDay(Classes c, Date day, Long productionUnitId);
	/**
	 * 根据班次id查找当月每日产量
	 * @param classesId
	 * @param month
	 * @return
	 */
	public List<Object[]> queryCountByClassesIdAndMonth(Long classesId, Date day, Long productionUnitId);
	/**
	 * 查询当前班次下的给定设备站点的加工数量、总标准节拍、总短停机时间
	 * @param c
	 * @param deviceSiteId
	 * @return 0:生产数量 1:总标准节拍 2:总短停机时间
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes c, Long deviceSiteId, Date date);
	/**
	 * 查询当前班次下的给定设备站点的加工数量、总标准节拍、总短停机时间
	 * @param c
	 * @param deviceSiteId
	 * @return 0:生产数量 1:总标准节拍 2:总短停机时间
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassByClassesAndProductionUnit(Classes c, Long productionUnitId, Date date);
	/**
	 * 查询当前班次下的给定设备站点的加工数量、总标准节拍、总短停机时间
	 * @param c
	 * @param date
	 * @return
	 */
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4RealTime(Classes c, Date date);

	/**
	 * 查询当前班次下的给定设备站点的加工数量、总标准节拍、总短停机时间
	 * @param c
	 * @param date
	 * @return 0:生产数量 1:总标准节拍 2:总短停机时间
	 */
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes c, Date date);
	/**
	 * 查询从月初一直到给定时间的加工总数、总标准节拍和总短停机时间
	 * @param date
	 * @return 0:生产数量 1:总标准节拍 2:总短停机时间
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHaltFromBeginOfMonthUntilTheDate(Date date);
	/**
	 * 查询最后一次添加的加工记录
	 * @return
	 */
	public ProcessRecord queryLastProcessRecord(Long deviceSiteId);
	/**
	 * 查询最后一次添加的加工记录
	 * @return
	 */
	public ProcessRecord queryLastProcessRecord(String deviceSiteCode);
	/**
	 * 查询当前班的短停机时间
	 * @param c
	 * @return
	 */
	public Integer queryShortHalt(Classes c, String deviceSiteCode);
	/**
	 * 查询该设备时间段内的加工记录数
	 * @param begin
	 * @param end
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryCountBetween(Date begin, Date end, Long deviceSiteId);
	/**
	 * 查询当前班次，当前设备站点从班次开始到现在的生产数量，总标准节拍和总的短停机时间
	 * @param c
	 * @param deviceSiteId
	 * @return
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassFromClassesBegin2now(Classes c, Long deviceSiteId);
	/**
	 * 查询当月的生产数
	 * @param date
	 * @return
	 */
	public Integer queryCountTheMonth(Date date);
	/**
	 * 查询当年的生产数 按月分组
	 * @param date
	 * @return 0:日期  1：生产数
	 */
	public List<Object[]> queryCountTheYear(Date date);
	/**
	 * 查询当前生产单元，当前班次下的短停机
	 * @param c
	 * @param productionUnitId
	 * @return
	 */
	public int queryShortHalt4ProductionUnit(Classes c, Long productionUnitId);
	/**
	 * 查找当前班次，当前生产单元下的不合格品数
	 * @param now  当前时间
	 * @param classId 班次id
	 * @param productionUnitId 生产单元id
	 * @return
	 */
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(Date now, Classes classes, Long productionUnitId);
	/**
	 * 查找当月到目前为止当前班次的生产数：产线级
	 * @param date
	 * @param classId
	 * @param productionUnitId
	 * @return
	 */
	public Integer querySumCountPerClasses4ProductionUnit(Date date, Classes classes, Long productionUnitId);
	/**
	 * 根据班次id查找瓶颈设备当月每日产量
	 * @param classesId
	 * @param month
	 * @return
	 */
	public List<Object[]> queryBottleneckCountByClassesIdAndMonth(Long classesId, Date now, Long productionUnitId);
	/**
	 * 查询瓶颈设备产量
	 * @param c
	 * @param d
	 * @param productionUnitId
	 * @return
	 */
	public long queryBottleneckCountByClassesIdAndDay(Classes c, Date d, Long productionUnitId);
	/**
	 * 查找参数追溯二维码数
	 * @param parameterCode
	 * @param parameterValue
	 * @param valueUp
	 * @param valueDown
	 * @param workPieceName
	 * @return
	 */
	public int queryParameterCount(String parameterCode, String worksheetNo, String batchNum, String minValue, String maxValue);
	/**
	 * 查找反向追溯二维码数量
	 * @param serialNo
	 * @param batchNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public int queryReverseCount(String serialNo, String batchNumber, String deviceSiteCode);
	/**
	 * 查找正向追溯二维码数量
	 * @param batchNumber
	 * @param stoveNumber
	 * @param deviceSiteCode
	 * @param ng
	 * @return
	 */
	public List<Object[]> queryCount(String batchNumber, String stoveNumber, String deviceSiteCode, String ng);
	/**
	 * 查找设备站点及其ng数
	 * @param batchNumber
	 * @param stoveNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public List<Object[]> queryDeviceSiteNGCount(String batchNumber, String stoveNumber, String deviceSiteCode);
	/**
	 * 正向追溯NG数
	 * @param batchNumber
	 * @param stoveNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public int queryNGCount(String batchNumber, String stoveNumber, String deviceSiteCode);
	/**
	 * 根据二维码删除设备执行记录
	 * @param opcNo
	 */
	public void deleteByOpcNo(String opcNo);
	/**
	 * 根据日期，班次，设备站点分组查找产量
	 * @param begin 开始时间
	 * @param end  结束时间
	 * @return
	 */
    List<Object[]> queryOutput4DeviceSitePerDay(String begin, String end, List<String> codeList);
}
