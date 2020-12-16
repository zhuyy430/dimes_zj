package com.digitzones.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.model.Classes;
import com.digitzones.model.NGRecord;
import com.digitzones.model.User;
/**
 * 不合格记录service
 * @author zdq
 * 2018年7月8日
 */
public interface INGRecordService extends ICommonService<NGRecord> {
	/**
	 * 审核不合格记录，会影响工单详情中的报废和返修数量
	 * @param record
	 */
	public void auditNGRecord(NGRecord record,User user,Map<String,Object> args);
	/**
	 * 复核
	 * @param record
	 * @param user
	 * @param args
	 */
	public void reviewNGRecord(NGRecord record,User user,Map<String,Object> args);
	/**
	 * 确认
	 * @param record
	 * @param user
	 * @param args
	 */
	public void confirmNGRecord(NGRecord record,User user,Map<String,Object> args);
	/**
	 * 设置删除标志
	 * @param record
	 */
	public void deleteNGRecord(NGRecord record);
	
	
	/**
	 * 添加NG记录
	 * @param record
	 * @param user
	 */
	public Serializable addNGRecord(NGRecord record,User user);
	
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
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryNgCount4Class(Classes classes ,Long deviceSiteId,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数,当天的实时数
	 * @param classes
	 * @return
	 */
	public List<Object[]> queryNgCount4ClassToday(Classes classes,Date date);
	public Integer queryNgCount4ClassByClassesAndProductionUnit(Classes classes ,Long productionUnitId,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数,当天的实时数
	 * @param classes
	 * @param date
	 * @return
	 */
	public List<Object[]> queryNgCount4Class(Classes classes ,Date date);
	/**
	 * 查找当前班，该设备上的实时NG数,当天的实时数
	 * @param date
	 * @return
	 */
	public List<Object[]> queryNgCount4DeviceSiteShow(List<Long> deviceSiteIdList ,Date date);
	/**
	 * 查询当前班次，该设备站点上从开班到当前时间的NG数
	 * @param classes
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryNgCount4ClassFromClassBegin2now(Classes classes ,Long deviceSiteId);
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
	 * 根据设备站点id查询NG记录
	 * @param deviceSiteId
	 * @return
	 */
	public List<NGRecord> queryNgRecordByDeviceSiteId(Long deviceSiteId);
	/**
	 * 查询一个时间段内的ng记录
	 * @return
	 */
	public List<NGRecord> queryNgRecordsByDate(String hql,List<Object> list);
    /**
     * 根据不合格品大类分组查找不合格品信息
     * @param monthDate yyyy-MM格式的日期
     * @param productionIdList 生产单元id列表
     * @return
     */
    List<String[]> queryNgRecord4MonthGroupByCategory(Date monthDate, List<Long> productionIdList);
    /**
     * 查询日期区间的不合格品数量
     * @param fromDate 开始时间
     * @param toDate 结束时间
     * @param productionIdList 生产单元id列表
     * @return
     */
    List<String[]> queryNgRecordGroupByCategory(Date fromDate, Date toDate, List<Long> productionIdList);
    
    /**
     * 根据箱号类型查找最大入库单号
     */
    public String queryMaxWarehouseNo();
}
