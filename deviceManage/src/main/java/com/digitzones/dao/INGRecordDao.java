package com.digitzones.dao;

import java.util.Date;
import java.util.List;

import com.digitzones.model.Classes;
import com.digitzones.model.NGRecord;
/**
 * 不合格品记录dao
 * @author zdq
 * 2018年7月8日
 */
public interface INGRecordDao extends ICommonDao<NGRecord> {
	/**
	 * 根据工序id和日期（天）查找报废品数量 
	 * @param date
	 * @param processId
	 * @return
	 */
	public Integer queryScrapCountByDateAndProcessId(Date date,Long processId);
	/**
	 * 查询工序id和日期（天）查找报废品数量(优化) 
	 * @param date
	 * @param processId
	 * @return
	 */
	public List<?> queryScrapCountByDateAndProcessId(Date date);
	/**
	 * 根据设备站点id查询当天ng数量
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryNgCountByDeviceSiteId(Long deviceSiteId,Date today);
	/**
	 * 查找当前班，该设备上的实时NG数,当天的实时数
	 * @param classes
	 * @param date
	 * @return
	 */
	public List<Object[]> queryNgCount4DeviceSiteShow(List<Long> deviceSiteIdList ,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数
	 * @param classes
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryNgCount4Class(Classes classes ,Long deviceSiteId,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数,当天的实时数
	 * @param classes
	 * @param deviceSiteId
	 * @return
	 */
	public List<Object[]> queryNgCount4ClassToday(Classes classes,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数
	 * @param classes
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryNgCount4ClassByClassesAndProductionUnit(Classes classes ,Long productionUnitId,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数
	 * @param classes
	 * @param deviceSiteId
	 * @return
	 */
	public List<Object[]> queryNgCount4Class(Classes classes, Date date);
	/**
	 * 查询从月初到给定时间的NG数
	 * @param date
	 * @return
	 */
	public Integer queryNgCountFromBeginOfMonthUntilTheDate(Date date);
	/**
	 * 查询指定日期的ng记录数
	 * @param date
	 * @return
	 */
	public Long queryNgCount4TheDate(Date date);
	/**
	 * 根据ngrecord id查询不合格品信息
	 * @param id
	 * @return
	 */
	public Object[] queryNGRecordById(Long id);
	/**
	 * 根据ngrecord id查询不合格品信息
	 * @param id
	 * @return
	 */
	public List<NGRecord> queryNgRecordByDeviceSiteId(Long deviceSiteId);
	/**
	 * 查询当前班次，该设备站点上从开班到当前时间的NG数
	 * @param classes
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryNgCount4ClassFromClassBegin2now(Classes classes ,Long deviceSiteId);
	/**
	 * 查询日期区间的不合格品数量
	 * @param fromDate 开始时间
	 * @param toDate 结束时间
	 * @param productionIdList 生产单元id列表
	 * @return
	 */
	List<String[]> queryNgRecordGroupByCategory(Date fromDate,Date toDate, List<Long> productionIdList);
	/**
	 * 查询日期区间的不合格品数量
	 * @param monthDate
	 * @param productionIdList 生产单元id列表
	 * @return
	 */
	List<String[]> queryNgRecord4MonthGroupByCategory(Date monthDate, List<Long> productionIdList);
	
	public String queryMaxWarehouseNo();
}
