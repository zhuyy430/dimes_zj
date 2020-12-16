package com.digitzones.service;

import com.digitzones.model.Classes;
import com.digitzones.model.ProcessRecord;
import com.digitzones.model.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 加工信息service
 * @author zdq
 * 2018年6月20日
 */
public interface IProcessRecordService extends ICommonService<ProcessRecord> {
	/**
	 * 查询当天的加工数量
	 * @param deviceSiteId
	 * @return
	 */
	public Long queryCurrentDayCountByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据日期(天)查询工序、工件、设备站点id及该状态下的记录数
	 * @param deviceSiteId
	 * @param status
	 * @param now
	 * @return
	 */
	public List<Long[]> queryByDay(Long deviceSiteId, String status, Date now) ;
	/**
	 * 查找当前月份的人员产量
	 * @param date
	 * @param empId
	 * @return
	 */
	public Integer queryOutput4EmployeePerMonth(Date date, Long empId);


	/**
	 * 查找当年每月份的人员产量
	 * @param date
	 * @param empId
	 * @return
	 */
	public List<Object[]> queryAllMonthOutput4EmployeePerYear(Date date);



	/**
	 * 查找当前月下的工序产量
	 * @param date
	 * @param processId
	 * @return
	 */
	public Integer queryOutput4ProcessPerMonth(Date date, Long processId);
	/**
	 * 查找当年每月的工序产量
	 * @param date
	 * @param processId
	 * @return
	 */
	public List<Object[]> queryAllMonthOutput4ProcessPerYear(Date date);


	/**
	 * 查找当前月下的站点产量
	 * @param date
	 * @return
	 */
	public int queryOutput4DeviceSitePerMonth(Date date, Long deviceSiteId);


	/**
	 * 查找当年每月的站点产量
	 * @param date
	 * @return
	 */
	public List<Object[]> queryAllMonthOutput4DeviceSitePerYear(Date date);


	/**
	 * 查找当前月 下的不合格数
	 * @param date
	 * @return
	 */
	public Integer queryWorkSheetNGCountPerMonth(Date date);
	/**
	 * 查找当月 每天的不合格数
	 * @param date
	 * @return
	 */
	public List<Object[]> queryAllDayWorkSheetNGCountPerByMonth(Date date);
	/**
	 * 查找报废品数量
	 * @param date
	 * @param ngTypeId
	 * @return
	 */
	public Integer queryWorkSheetScrapCountPerMonth(Date date, Long ngTypeId);
	/**
	 * 查找当月所有的报废数量
	 * @param date
	 * @return
	 */
	public Integer queryWorkSheetScrapCountPerMonth(Date date);
	/**
	 * 查找当年所有的报废数量，按月份分组
	 * @param date
	 * @return 0:日期  1：报废数量
	 */
	public List<Object[]> queryWorkSheetScrapCount4Year(Date date);

	/**
	 * 查找当前班次，当前生产单元下的不合格品数
	 * @param now  当前时间
	 * @param classId 班次id
	 * @param productionUnitId 生产单元id
	 * @return
	 */
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(Date now, Classes classes, Long productionUnitId);
/*	*//**
	 * 查找当前天，当前班次的不合格数：产线级
	 * @param date
	 * @param classId
	 * @param productionUnitId
	 * @return
	 *//*
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(Date date,Long classId,Long productionUnitId);
*/	/**
	 * 查找当前月下的合格数
	 * @param date
	 * @return
	 */
	public Integer queryWorkSHeetNotNGCountPerMonth(Date date);
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
	public List<Object[]> queryAllDayCountByTheMonth(Date date);
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
	 * 查找当月到目前为止当前班次的合格品数：产线级
	 * @param date
	 * @param classId
	 * @param productionUnitId
	 * @return
	 */
	public Integer querySumCountPerClasses4ProductionUnit(Date date, Classes classes, Long productionUnitId);
/*	*//**
	 * 查找当前天，当前班次的合格数：产线级
	 * @param date
	 * @param classId
	 * @param productionUnitId
	 * @return
	 *//*
	public Integer querySumCountPerClasses4ProductionUnit(Date date,Long classId,Long productionUnitId);
*/	/**
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
	 * 根据班次id查找瓶颈设备当月每日产量
	 * @param classesId
	 * @param month
	 * @return
	 */
	public List<Object[]> queryBottleneckCountByClassesIdAndMonth(Long classesId, Date now, Long productionUnitId);

	/**
	 * 查询当前班次下的给定设备站点的加工数量、总标准节拍、总短停机时间
	 * @param c
	 * @param deviceSiteId
	 * @return
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes c, Long deviceSiteId, Date date);
	/**
	 * 查询当前班次下的给定设备站点的加工数量、总标准节拍、总短停机时间
	 * @param c
	 * @param date
	 * @return
	 */
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4RealTime(Classes c, Date date);
/**	 * @param deviceSiteId
	 * @return
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassByClassesAndProductionUnit(Classes c, Long productionUnitId, Date date);
	/**
	 * 查询当前班次的加工数量、总标准节拍、总短停机时间
	 * @param c 当前班次
	 * @param date  当前日期
	 * @return
	 */
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes c, Date date);
	/**
	 * 查询从月初一直到给定时间的加工总数、总标准节拍和总短停机时间
	 * @param date
	 * @return 0:生产数量 1:总标准节拍 2:总短停机时间
	 */
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHaltFromBeginOfMonthUntilTheDate(Date date);
	/**
	 * 添加执行记录
	 * @param obj
	 * @param user
	 * @param args
	 * @return
	 */
	public Serializable addProcessRecord(ProcessRecord obj, User user, Map<String, Object> args);
	/**
	 * 查询当前班的短停机时间
	 * @param c
	 * @return
	 */
	public Integer queryShortHalt(Classes c, String deviceSiteCode);
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
	 * 查询当前生产单元，当前班次下的短停机
	 * @param c
	 * @param productionUnitId
	 * @return
	 */
	public int queryShortHalt4ProductionUnit(Classes c, Long productionUnitId);
	/**
	 * 查询所有
	 * @return
	 */
	public List<ProcessRecord> queryAll();
	/**
	 * 根据生产序号和设备站点代码查询设备执行记录
	 * @param productNum
	 * @param deviceSiteCode
	 * @return
	 */
	public ProcessRecord queryByProductNumAndDeviceSiteCode(String productNum, String deviceSiteCode);
	/**
	 * 根据opc序号和设备站点代码查询设备执行记录
	 * @param productNum
	 * @param deviceSiteCode
	 * @return
	 */
	public List<ProcessRecord> queryByOpcNoAndDeviceSiteCode(String opcNo, String deviceSiteCode);
	/**
	 * 查询瓶颈设备生产数量
	 * @param c
	 * @param d
	 * @param productionUnitId
	 * @return
	 */
	public long queryBottleneckCountByClassesIdAndDay(Classes c, Date d, Long productionUnitId);
	/**
	 * 根据条件查询ng数
	 * @param batchNumber
	 * @param stoveNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public int queryNGCount(String batchNumber, String stoveNumber, String deviceSiteCode);
	/**
	 * 根据条件查询数量
	 * @param batchNumber
	 * @param stoveNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public List<Object[]> queryCount(String batchNumber, String stoveNumber, String deviceSiteCode, String ng);
	/**
	 * 查找反向追溯二维码数
	 * @param serialNo
	 * @param batchNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public int queryReverseCount(String serialNo, String batchNumber, String deviceSiteCode);
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
	 * 查找设备站点及其ng数
	 * @param batchNumber
	 * @param stoveNumber
	 * @param deviceSiteCode
	 * @return
	 */
	public List<Object[]> queryDeviceSiteNGCount(String batchNumber, String stoveNumber, String deviceSiteCode);
	/**
	 * 根据二维码删除设备执行记录
	 * @param opcNo
	 */
	public void deleteByOpcNo(String opcNo);
	/**
	 * 根据日期，班次，设备站点分组查找产量
	 * @param begin 开始时间
	 * @param end  结束时间
	 * @param codeList  设备站点编码列表
	 * @return
	 */
    List<Object[]> queryOutput4DeviceSitePerDay(String begin, String end, List<String> codeList);
}
