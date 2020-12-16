package com.digitzones.dao.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.dao.IProcessRecordDao;
import com.digitzones.model.Classes;
import com.digitzones.model.ProcessRecord;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
@Repository
public class ProcessRecordDaoImpl extends CommonDaoImpl<ProcessRecord> implements IProcessRecordDao {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public ProcessRecordDaoImpl() {
		super(ProcessRecord.class);
	}
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<Long[]> queryCurrentMonthDeviceSiteIdAndStatus(Long deviceSiteId, String status) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.DATE, 1);

		Date dateBegin = c.getTime();
		String hql = "select pr.workPieceId,pr.processId,pr.deviceSiteId ,pr.classesId, count(*) from ProcessRecord pr  left join Classes c on pr.classesid=c.id where "
				+ " pr.deviceSiteId=?0 and pr.status=?1 and collectionDate  between ?2 and ?3 and pr.realRecord=?4" + 
				" group by pr.workPieceId,pr.processId,pr.deviceSiteId ,pr.classesId";
		return getSession().createNativeQuery(hql)
				.setParameter(0, deviceSiteId)
				.setParameter(1,status)
				.setParameter(2,dateBegin)
				.setParameter(3, now)
				.setParameter(4, true)
				.list();
	}
	@Override
	public Long queryCountByDeviceSiteIdAndNotNg(Long deviceSiteId) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE,0);

		Date begin = c.getTime();
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE,59);
		Date end = c.getTime();

		String hql = "select  count(*) from ProcessRecord pr  left join Classes c on pr.classesid=c.id where pr.realRecord=?4 and "
				+ " pr.deviceSiteId=?0 and pr.status!=?1  and (pr.collectionDate between ?2 and ?3 ) and (c.beginTime<c.endTime and CONVERT(varchar(100),pr.collectionDate,108) >= CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),pr.collectionDate,108)<=CONVERT(varchar(100),c.endTime,108)) or ((c.beginTime>c.endTime )" + 
				" and ((CONVERT(varchar(100),pr.collectionDate,108) >= CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),pr.collectionDate,108)<='23:59')) or (CONVERT(varchar(100),pr.collectionDate,108) >= '00:00' and CONVERT(varchar(100),pr.collectionDate,108)<=CONVERT(varchar(100),c.endTime,108))) ";
		return ((Integer) getSession().createNativeQuery(hql).setParameter(0, deviceSiteId)
				.setParameter(1, Constant.ProcessRecord.NG)
				.setParameter(2, begin)
				.setParameter(3,end)
				.setParameter(4, true)
				.uniqueResult()).longValue();
	}
	@Override
	public Long queryCurrentDayCountByDeviceSiteId(Long deviceSiteId) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE,0);

		Date begin = c.getTime();
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE,59);
		Date end = c.getTime();

		String hql = "select  count(*) from ProcessRecord pr left join Classes c on pr.classesid=c.id where pr.realRecord=?3 and "
				+ " pr.deviceSiteId=?0  and (pr.collectionDate between ?1 and ?2 ) and (c.beginTime<c.endTime and CONVERT(varchar(100),pr.collectionDate,108) >= CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),pr.collectionDate,108)<=CONVERT(varchar(100),c.endTime,108)) or ((c.beginTime>c.endTime ) " + 
				" and ((CONVERT(varchar(100),pr.collectionDate,108) >= CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),pr.collectionDate,108)<='23:59')) or (CONVERT(varchar(100),pr.collectionDate,108) >= '00:00' and CONVERT(varchar(100),pr.collectionDate,108)<=CONVERT(varchar(100),c.endTime,108)))";

		Integer result =  (Integer) getSession().createNativeQuery(hql).setParameter(0, deviceSiteId)
				.setParameter(1, begin)
				.setParameter(2,end)
				.setParameter(3, true)
				.uniqueResult();
		return result.longValue();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Long[]> queryByDay(Long deviceSiteId, String status, Date now) {
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE,0);

		Date begin = c.getTime();
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE,59);
		Date end = c.getTime();

		String hql = "select pr.workPieceId,pr.processId,pr.deviceSiteId , count(*) from ProcessRecord pr  left join Classes c on pr.classesid=c.id"
				+ " where  pr.realRecord=?4 and "
				+ " pr.deviceSiteId=?0 and pr.status=?1  and (pr.collectionDate between ?2 and ?3 ) and " + 
				" (c.beginTime<c.endTime and CONVERT(varchar(100),pr.collectionDate,108) >= CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),pr.collectionDate,108)<=CONVERT(varchar(100),c.endTime,108)) or ((c.beginTime>c.endTime ) " + 
				" and ((CONVERT(varchar(100),pr.collectionDate,108) >= CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),pr.collectionDate,108)<='23:59')) or (CONVERT(varchar(100),pr.collectionDate,108) >= '00:00' and CONVERT(varchar(100),pr.collectionDate,108)<=CONVERT(varchar(100),c.endTime,108))) " + 
				" group by pr.workPieceId,pr.processId,pr.deviceSiteId ";
		return getSession().createNativeQuery(hql).setParameter(0, deviceSiteId).setParameter(1,status)
				.setParameter(2, begin)
				.setParameter(3,end)
				.setParameter(4, true)
				.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Integer queryOutput4EmployeePerMonth(int year, int month, Long empId) {
		String hql = "select  COUNT(id) from ProcessRecord pr inner join devicesite ds on pr.devicesiteid=ds.id "
				+ "where ds.bottleneck=1 and  year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1  and pr.productUserId=?2 and pr.realRecord=?3";
		List<Integer> list = getSession().createNativeQuery(hql)
					.setParameter(0, year)
					.setParameter(1, month)
					.setParameter(2, empId)
					.setParameter(3, true)
					.list();
		if(list!=null&&list.size()>0) {
			return  (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryOutput4ProcessPerMonth(int year, int month, Long processId) {
		String hql = "select  COUNT(id) from ProcessRecord pr "
				+ "where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1 and pr.processId=?2 and pr.realRecord=?3";
		@SuppressWarnings({ "rawtypes"})
        List list = getSession().createNativeQuery(hql)
					.setParameter(0, year)
					.setParameter(1, month)
					.setParameter(2,processId)
					.setParameter(3, true)
					.list();
		if(list!=null&&list.size()>0) {
			return  ((Integer) list.get(0)).intValue();
		}
		return 0;
	}
	@Override
	public Integer queryOutput4DeviceSitePerMonth(int year, int month, Long deviceSiteId) {
		String hql = "select COUNT(id) from ProcessRecord pr "
				+ "where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1 and pr.deviceSiteId=?2 and pr.realRecord=?3 ";
		@SuppressWarnings({ "rawtypes"})
        List list = getSession().createNativeQuery(hql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, deviceSiteId)
								.setParameter(3, true)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryWorkSheetNGCountPerMonth(int year, int month) {
		String sql = "select sum(method.ngCount) from  NGRECORD ng  "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " where year(ng.occurDate)=?0 and month(ng.occurDate)=?1  and method.processMethod!=?2 ";
		@SuppressWarnings("rawtypes")
        List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, Constant.ProcessRecord.COMPROMISE)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryWorkSHeetNotNGCountPerMonth(int year, int month) {
		String sql = "select COUNT(pr.id) from PROCESSRECORD pr inner join WORKSHEET ws on pr.no = ws.no "
				+ " where pr.realRecord=?3 and ws.workSheetType=?2 and pr.no not in " + 
				" (select no from NGRECORD)"
				+ " and year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1";
		@SuppressWarnings("rawtypes")
        List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, Constant.WorkSheet.COMMON)
								.setParameter(3, true)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryCountByClassesIdAndDay(Classes classes, Date day, Long productionUnitId) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(day);
		//班次开始时间设置
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(classes.getStartTime());
		startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次结束时间设置
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(classes.getEndTime());
		endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次跨天
		if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
			endCalendar.add(Calendar.DATE, 1);
		}
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		String sql = "select COUNT(pr.id) from PROCESSRECORD pr inner join DEVICESITE site on pr.deviceSiteId=site.id"
				+ " inner join Device d on site.device_id = d.id "
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1"
				+ " and (pr.collectionDate between ?2 and ?3) and d.productionunit_id=?4 and pr.realRecord=?5 "
				+ " and d.isDimesUse=1";
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		@SuppressWarnings({ "rawtypes"})
        List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, startDate)
								.setParameter(3,endDate)
								.setParameter(4, productionUnitId)
								.setParameter(5, true)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(int year, int month, int day, Long classId,
                                                                  Long productionUnitId) {
		String sql = "select sum(method.ngCount) from  NGRECORD ng "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " inner join DEVICESITE ds on ng.deviceSiteId=ds.id "
				+ " inner join DEVICE d on ds.DEVICE_ID=d.id "
				+ " inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id "  
				+ " where year(ng.occurDate)=?0 and month(ng.occurDate)=?1"
				+ " and day(ng.occurDate)=?2 and ng.classesId=?3 and p.id=?4 "
				+ " and d.isDimesUse=1";
		
		@SuppressWarnings({ "rawtypes"})
        List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, day)
								.setParameter(3,classId)
								.setParameter(4, productionUnitId)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(Date now, Classes classes,
                                                                  Long productionUnitId) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(now);
		//班次开始时间设置
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(classes.getStartTime());
		startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次结束时间设置
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(classes.getEndTime());
		endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次跨天
		if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
			endCalendar.add(Calendar.DATE, 1);
		}
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		String sql = "select sum(method.ngCount) _count from  NGRECORD ng "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " inner join DEVICESITE ds on ng.deviceSiteId=ds.id "
				+ " inner join DEVICE d on ds.DEVICE_ID=d.id "
				+ " inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id "  
				+ " where year(ng.occurDate)=?0 and month(ng.occurDate)=?1"
				+ " and ng.classesId=?2 and p.id=?3 "
				+ " and ng.occurDate between ?4 and ?5 and d.isDimesUse=1";
		
		Integer count = (Integer) getSession().createNativeQuery(sql)
		.setParameter(0, c.get(Calendar.YEAR))
		.setParameter(1, c.get(Calendar.MONTH) + 1)
		.setParameter(2,classes.getId())
		.setParameter(3, productionUnitId)
		.setParameter(4, startDate)
		.setParameter(5, endDate)
		.uniqueResult();
		return count==null?0:count;
	}
	@Override
	public Integer querySumCountPerClasses4ProductionUnit(Date date, Classes classes, Long productionUnitId) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(date);
		//班次开始时间设置
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(classes.getStartTime());
		startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次结束时间设置
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(classes.getEndTime());
		endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次跨天
		if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
			endCalendar.add(Calendar.DATE, 1);
		}
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		String sql = "select COUNT(*) _count from PROCESSRECORD pr "
				+ " inner join DEVICESITE ds on pr.deviceSiteCode=ds.code "
				+ " inner join DEVICE d on ds.DEVICE_ID=d.id "
				+ " inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id "  
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1"
				+ "  and pr.classesId=?2 and p.id=?3 "
				+ " and  pr.realRecord=?4 "
				+ " and pr.collectionDate  between ?5 and ?6  and d.isDimesUse=1";
		
		Integer count = (Integer) getSession().createNativeQuery(sql)
								.setParameter(0, c.get(Calendar.YEAR))
								.setParameter(1, c.get(Calendar.MONTH) + 1)
								.setParameter(2,classes.getId())
								.setParameter(3, productionUnitId)
								.setParameter(4, true)
								.setParameter(5, startDate)
								.setParameter(6, endDate)
								.uniqueResult();
		return count == null?0:count;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Integer querySumCountPerClasses4ProductionUnit(int year, int month, int day, Long classId,
                                                          Long productionUnitId) {
		String sql = "select COUNT(*) from PROCESSRECORD pr "
				+ " inner join DEVICESITE ds on pr.deviceSiteCode=ds.code "
				+ " inner join DEVICE d on ds.DEVICE_ID=d.id "
				+ " inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id "  
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1"
				+ " and day(pr.collectionDate)=?2 and pr.classesId=?3 and p.id=?4 "
				+ " and  pr.realRecord=?5 and d.isDimesUse=1";
		
		List<Integer> list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, day)
								.setParameter(3,classId)
								.setParameter(4, productionUnitId)
								.setParameter(5, true)
								.list();
		if(list!=null&&list.size()>0) {
			return  list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryWorkSheetScrapCountPerMonth(int year, int month, Long ngTypeId) {
		String sql = "select sum(method.ngCount) from PROCESSRECORD pr inner join NGRECORD ng  on pr.no = ng.no "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " inner join WORKSHEET ws on pr.no = ws.no "
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1 "
				+ " and method.processMethod=?2 and ng.ngTypeId=?3 and pr.realRecord=?4";
		@SuppressWarnings("rawtypes")
        List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, Constant.ProcessRecord.SCRAP)
								.setParameter(3,ngTypeId)
								.setParameter(4, true)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@Override
	public Integer queryWorkSheetScrapCountPerMonth(int year, int month) {
		String sql = "select sum(method.ngCount) from  NGRECORD ng "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " where year(ng.occurDate)=?0 and month(ng.occurDate)=?1 "
				+ " and method.processMethod=?2";
		@SuppressWarnings("rawtypes")
        List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, Constant.ProcessRecord.SCRAP)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryWorkSheetScrapCount4Year(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String sql = "select convert(varchar(10),ng.occurDate,120) occDate, sum(method.ngCount) _sum ,ngTypeId from  NGRECORD ng "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " where year(ng.occurDate)=?0 "
				+ " and method.processMethod=?1 group by convert(varchar(10),ng.occurDate,120),ngTypeId";
		List<Object[]> list = getSession().createNativeQuery(sql)
							.setParameter(0, c.get(Calendar.YEAR))
							.setParameter(1, Constant.ProcessRecord.SCRAP)
							.list();
		return list;
	}
	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes classes, Long deviceSiteId, Date date) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(date);
		//班次开始时间设置
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(classes.getStartTime());
		startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次结束时间设置
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(classes.getEndTime());
		endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次跨天
		if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
			endCalendar.add(Calendar.DATE, 1);
		}
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		String sql = "select sum(pp._count) __count,sum(pp.sumStandardBeat) sumStandardBeat,sum(pp._shortHalt)/60 from (select count(pr.id) _count,sum(pr.standardBeat) sumStandardBeat,sum(pr.shortHalt) _shortHalt " +
				" from ProcessRecord pr  where pr.classesId=?1 and pr.deviceSiteId=?0 and pr.collectionDate between ?2 and ?3  and pr.realRecord=1  group by pr.workPieceCode) pp"; 
		Object[] list = (Object[]) getSession().createNativeQuery(sql)
				.setParameter(0, deviceSiteId)
				.setParameter(1,classes.getId())
				.setParameter(2, startDate)
				.setParameter(3,endDate)
				.uniqueResult();
		
		return list;
	}
	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassByClassesAndProductionUnit(Classes classes, Long productionUnitId, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE,0);
		
		Date begin = c.getTime();
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE,59);
		Date end = c.getTime();
		String sql = "select sum(pp._count) __count,sum(pp.sumStandardBeat) sumStandardBeat,sum(pp._shortHalt) "
				 + "from (select count(pr.id) _count,sum(pr.standardBeat) sumStandardBeat,sum(pr.shortHalt) _shortHalt "  
				 + "from ProcessRecord pr inner join DEVICESITE ds on pr.deviceSiteId = ds.id inner join DEVICE d on ds.DEVICE_ID = d.id " 
				 + " where ds.bottleneck=1 and d.PRODUCTIONUNIT_ID = ?0 and pr.classesId=?1  and pr.collectionDate between ?2 "
				 + " and ?3  and pr.realRecord=1 and d.isDimesUse=1  group by pr.workPieceCode) pp"; 
		Object[] list = (Object[]) getSession().createNativeQuery(sql)
				.setParameter(0, productionUnitId)
				.setParameter(1,classes.getId())
				.setParameter(2, begin)
				.setParameter(3,end)
				.uniqueResult();
		
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes classes, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE,0);

		Date begin = c.getTime();
		String sql = " select cdate, sum(pp._count) __count,sum(pp.sumStandardBeat) sumStandardBeat,sum(pp._shortHalt)/60 from ("
				+ "select convert(varchar(10),pr.collectionDate,120) cdate, count(pr.id) _count,sum(pr.standardBeat) sumStandardBeat,sum(pr.shortHalt) _shortHalt " + 
				" from ProcessRecord pr  "
				+ "where pr.classesId=?0  and pr.collectionDate between ?1 and ?2  and pr.realRecord=1  "
				+ " group by pr.workPieceCode,convert(varchar(10),pr.collectionDate,120)) pp "
				+ " group by pp.cdate"; 
		List<Object[]> list = (List<Object[]>) getSession().createNativeQuery(sql)
				.setParameter(0, classes.getId())
				.setParameter(1,begin)
				.setParameter(2, date)
				.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4RealTime(Classes classes, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE,0);

		Date begin = c.getTime();
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE,59);
		Date end = c.getTime();
		String sql = "select pp.deviceSiteId,sum(pp._count) __count,sum(pp.sumStandardBeat) sumStandardBeat,sum(pp._shortHalt)/60 from ("
				+ " select pr.deviceSiteId deviceSiteId, count(pr.id) _count,sum(pr.standardBeat) sumStandardBeat,sum(pr.shortHalt) _shortHalt " + 
				" from ProcessRecord pr "
				+ " inner join deviceSite ds on pr.deviceSiteId=ds.id where ds.bottleneck=1 and "
				+ "  pr.classesId=?0 and pr.collectionDate between ?1 and ?2  and pr.realRecord=1 "
				+ " group by pr.workPieceCode,pr.deviceSiteId) pp group by pp.deviceSiteId"; 
		List<Object[]> list = (List<Object[]>) getSession().createNativeQuery(sql)
				.setParameter(0,classes.getId())
				.setParameter(1, begin)
				.setParameter(2,end)
				.list();
		return list;
	}
	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHaltFromBeginOfMonthUntilTheDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		
		Date beginOfMonth = c.getTime();
		String sql = "select  sum(_count) __count,sum(sumStandardBeat) _sumStandardBeat,sum(_shortHalt) from "
				+ "(select count(pr.id) _count,sum(pr.standardBeat) sumStandardBeat,sum(pr.shortHalt) _shortHalt "
				+ " from ProcessRecord pr  inner join devicesite ds on pr.devicesiteid=ds.id "
				+ "where ds.bottleneck=1 and pr.collectionDate between ?0 and ?1  and pr.realRecord=?2  group by pr.workPieceCode) pp"; 
		Object[] list = (Object[]) getSession().createNativeQuery(sql)
												.setParameter(0, beginOfMonth)
												.setParameter(1, date)
												.setParameter(2, true)
												.uniqueResult();
		
		return list;
	}
	@SuppressWarnings({ "unchecked"})
	@Override
	public ProcessRecord queryLastProcessRecord(Long deviceSiteId) {
		String hql = "from ProcessRecord pr where pr.id in (select max(p.id) from ProcessRecord p "
				+ "where p.deviceSiteId=?0 and p.realRecord=?1)";
		
		List<ProcessRecord> list = (List<ProcessRecord>) this.getHibernateTemplate().find(hql, new Object[] {deviceSiteId,true});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ProcessRecord queryLastProcessRecord(String deviceSiteCode) {
		String hql = "from ProcessRecord pr where pr.id in (select max(p.id) from ProcessRecord p "
				+ "where p.deviceSiteCode=?0 and  p.realRecord=?1)";
		
		List<ProcessRecord> list = (List<ProcessRecord>) this.getHibernateTemplate().find(hql, new Object[] {deviceSiteCode,true});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public Integer queryShortHalt(Classes c, String deviceSiteCode) {
		Date now = new Date();
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		//班次开始时间
		Calendar startCalendar = Calendar.getInstance();
		if(c!=null) {
			startCalendar.setTime(c.getStartTime());
			startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
			startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
			startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		}
		Date startDate = startCalendar.getTime();

		String sql = "select sum(shortHalt) from ProcessRecord record where "
				+ " record.collectionDate between ?0 and ?1 and classesId=?2 "
				+ "and shortHalt is not null and record.deviceSiteCode=?3 and record.realRecord=1";
		@SuppressWarnings("unchecked")
		NativeQuery<BigInteger> query = getSession().createNativeQuery(sql);
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2,c.getId())
		.setParameter(3, deviceSiteCode);
		BigInteger result = query.uniqueResult();
		return result==null?0:result.intValue();
	}
	@Override
	public Integer queryCountBetween(Date begin, Date end, Long deviceSiteId) {
		String sql = "select count(id) from ProcessRecord record where " +
				"record.collectionDate >?0 and record.collectionDate<=?1 and deviceSiteId=?2 and record.realRecord=1";
		Integer result = (Integer) this.getSession().createNativeQuery(sql).setParameter(0, begin)
											.setParameter(1, end)
											.setParameter(2, deviceSiteId)
											.uniqueResult();
		return result==null?0:result.intValue();
	}
	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassFromClassesBegin2now(Classes c,
                                                                                                     Long deviceSiteId) {
		Date now = new Date();
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		//班次开始时间
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(c.getStartTime());
		startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));

		Date startDate = startCalendar.getTime();
		String sql = "select  sum(_count) __count,sum(sumStandardBeat) _sumStandardBeat,sum(_shortHalt) from "
				+ "(select count(pr.id) _count,sum(ISNULL(pr.standardBeat,0)) sumStandardBeat,sum(pr.shortHalt)/60 _shortHalt from ProcessRecord pr"
				+ " where pr.collectionDate between ?0 and ?1 and pr.classesId=?2 and pr.deviceSiteId=?3 and pr.deleted=0 and pr.realRecord=1 "
				+ " group by pr.workPieceCode) pp";
		@SuppressWarnings("unchecked")
		NativeQuery<Object[]> query = getSession().createNativeQuery(sql);
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2,c.getId())
		.setParameter(3, deviceSiteId);
		return query.uniqueResult();
	}
	@Override
	public Integer queryCountTheDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		Date dayBegin = c.getTime();
		
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND,59);
		
		Date dayEnd = c.getTime();
		String sql = "select count(pr.id) from ProcessRecord pr "
				+ "where pr.collectionDate between ?0 and ?1 and pr.deleted=0 and pr.realRecord=1";
		
		Integer result = (Integer) getSession().createNativeQuery(sql).setParameter(0, dayBegin)
				.setParameter(1, dayEnd).uniqueResult();
		
		return result==null?0:result;
	}
	@Override
	public Integer queryCountTheMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		Date monthBegin = c.getTime();
		
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND,59);
		c.set(Calendar.DATE, 1);
		c.add(Calendar.MONTH, 1);
		Date monthEnd = c.getTime();
		String sql = "select count(pr.id) from ProcessRecord pr where pr.collectionDate >= ?0 and pr.collectionDate<?1 and pr.deleted=0 and pr.realRecord=1";
		
		Integer result = (Integer) getSession().createNativeQuery(sql).setParameter(0, monthBegin)
				.setParameter(1, monthEnd).uniqueResult();
		
		return result==null?0:result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryCountTheYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String sql = "select convert(varchar(10),pr.collectionDate,120),count(pr.id) _count from ProcessRecord pr "
				+ " where year(pr.collectionDate)=?0  and pr.deleted=0 and pr.realRecord=1"
				+ " group by convert(varchar(10),pr.collectionDate,120)";
		List<Object[]> list = getSession().createNativeQuery(sql)
						.setParameter(0,c.get(Calendar.YEAR))
						.list();
		return list;
	}
	@Override
	public int queryShortHalt4ProductionUnit(Classes c, Long productionUnitId) {
		Date now = new Date();
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		//班次开始时间
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(c.getStartTime());
		startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));

		Date startDate = startCalendar.getTime();

		String sql = "select sum(shortHalt) from ProcessRecord record inner join DEVICESITE ds on record.deviceSiteCode=ds.code " +
				"inner join DEVICE d on ds.DEVICE_ID = d.id inner join PRODUCTIONUNIT pu on pu.id=d.PRODUCTIONUNIT_ID where "
				+ " record.collectionDate between ?0 and ?1 and classesId=?2 and shortHalt is not null and pu.id=?3"
				+ " and d.isDimesUse=1 and record.realRecord=1";
		@SuppressWarnings("unchecked")
		NativeQuery<BigInteger> query = getSession().createNativeQuery(sql);
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2,c.getId())
		.setParameter(3, productionUnitId);
		BigInteger result = query.uniqueResult();
		return result==null?0:result.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryCountByClassesIdAndMonth(Long classesId, Date day, Long productionUnitId) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(day);
		String sql = "select  convert(varchar(10),pr.collectionDate,120) as daytime,COUNT(pr.id) as count  from PROCESSRECORD pr inner join DEVICESITE site on pr.deviceSiteId=site.id"
				+ " inner join Device d on site.device_id = d.id "
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1"
				+ " and pr.classesId=?2 and d.productionunit_id=?3 and pr.realRecord=?4"
				+ " and d.isDimesUse=1 group by convert(varchar(10),pr.collectionDate,120)";
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		List<Object[]> list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2,classesId)
								.setParameter(3, productionUnitId)
								.setParameter(4, true)
								.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryAllMonthOutput4EmployeePerYear(int year) {
		String hql = "select  month(pr.collectionDate),pr.productUserCode,COUNT(pr.id) as count from ProcessRecord pr"
				+ " inner join devicesite ds on pr.devicesiteid=ds.id "
				+ " where ds.bottleneck=1 and year(pr.collectionDate)=?0 and pr.realRecord=?1"
				+ " group by month(pr.collectionDate),pr.productUserCode";
		List<Object[]> list = getSession().createNativeQuery(hql)
					.setParameter(0, year)
					.setParameter(1, true)
					.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryAllMonthOutput4ProcessPerYear(int year) {
		String hql = "select  month(pr.collectionDate),pr.processId,COUNT(id) as count from ProcessRecord pr "
				+ "where year(pr.collectionDate)=?0 and pr.realRecord=?1 "
				+ "group by month(pr.collectionDate),pr.processId";
		List<Object[]> list = getSession().createNativeQuery(hql)
					.setParameter(0, year)
					.setParameter(1, true)
					.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryAllMonthOutput4DeviceSitePerYear(int year) {
		String hql = "select month(pr.collectionDate),pr.deviceSiteId,COUNT(id) as count from ProcessRecord pr "
				+ "where year(pr.collectionDate)=?0 and pr.realRecord=?1 "
				+ "group by month(pr.collectionDate),pr.deviceSiteId";
		List<Object[]> list = getSession().createNativeQuery(hql)
								.setParameter(0, year)
								.setParameter(1, true)
								.list();
		
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryAllDayWorkSheetNGCountPerByMonth(int year, int month) {
		String sql = "select day(ng.occurDate),sum(method.ngCount) as count from  NGRECORD ng "
				+ " inner join NGPROCESSMETHOD method on method.NGRECORD_ID=ng.id "
				+ " where year(ng.occurDate)=?0 and month(ng.occurDate)=?1  and method.processMethod!=?2 "
				+ " group by day(ng.occurDate)";
		 List<Object[]> list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, Constant.ProcessRecord.COMPROMISE)
								.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryAllDayCountByTheMonth(int year, int month) {
		String sql = "select day(pr.collectionDate),count(pr.id) as count from ProcessRecord pr where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1 and pr.deleted=0 and pr.realRecord=1"
					+ " group by day(pr.collectionDate)";
		
		List<Object[]> list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryBottleneckCountByClassesIdAndMonth(Long classesId, Date now, Long productionUnitId) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(now);
		String sql = "select  convert(varchar(10),pr.collectionDate,120) as daytime,COUNT(pr.id) as count  from PROCESSRECORD pr inner join DEVICESITE site on pr.deviceSiteId=site.id"
				+ " inner join Device d on site.device_id = d.id "
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1"
				+ " and pr.classesId=?2 and d.productionunit_id=?3 and pr.realRecord=?4 "
				+ " and site.bottleneck=1 and d.isDimesUse=1 "
				+ "group by convert(varchar(10),pr.collectionDate,120)";
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		List<Object[]> list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2,classesId)
								.setParameter(3, productionUnitId)
								.setParameter(4, true)
								.list();
		return list;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public long queryBottleneckCountByClassesIdAndDay(Classes classes, Date day, Long productionUnitId) {
		String sql = "select COUNT(pr.id) from PROCESSRECORD pr inner join DEVICESITE site on pr.deviceSiteId=site.id"
				+ " inner join Device d on site.device_id = d.id "
				+ " where pr.forProductionDate=?0 and d.productionunit_id=?1 and pr.realRecord=?2 "
				+ " and site.bottleneck=1 and d.isDimesUse=1 and pr.classesCode=?3";
		
		List list = getSession().createNativeQuery(sql)
								.setParameter(0, format.format(day))
								.setParameter(1, productionUnitId)
								.setParameter(2, true)
								.setParameter(3, classes.getCode())
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
/*
	@Override
	public long queryBottleneckCountByClassesIdAndDay(Classes classes, Date day, Long productionUnitId) {
		Calendar c = Calendar.getInstance() ;
		c.setTime(day);
		//班次开始时间设置
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(classes.getStartTime());
		startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次结束时间设置
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(classes.getEndTime());
		endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
		//班次跨天
		if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
			endCalendar.add(Calendar.DATE, 1);
		}

		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		String sql = "select COUNT(pr.id) from PROCESSRECORD pr inner join DEVICESITE site on pr.deviceSiteId=site.id"
				+ " inner join Device d on site.device_id = d.id "
				+ " where year(pr.collectionDate)=?0 and month(pr.collectionDate)=?1"
				+ " and (pr.collectionDate between ?2 and ?3) and d.productionunit_id=?4 and pr.realRecord=?5 "
				+ " and site.bottleneck=1 and d.isDimesUse=1";

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		List list = getSession().createNativeQuery(sql)
								.setParameter(0, year)
								.setParameter(1, month)
								.setParameter(2, startDate)
								.setParameter(3,endDate)
								.setParameter(4, productionUnitId)
								.setParameter(5, true)
								.list();
		if(list!=null&&list.size()>0) {
			return (Integer) list.get(0);
		}
		return 0;
	}
*/
	@Override
	public int queryParameterCount(String parameterCode, String worksheetNo, String batchNum, String minValue, String maxValue) {
		String sql1 = "select count(distinct p.opcNo) "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
				  
		if(parameterCode!=null&&!"".equals(parameterCode)){
			sql1 +=" and pp.parameterCode like '%"+parameterCode+"%'";
		}
		if(worksheetNo!=null&&!"".equals(worksheetNo)){
			sql1 +=" and p.no like '%"+worksheetNo+"%'";
		}
		if(!StringUtils.isEmpty(batchNum)){
			sql1 +=" and p.batchNumber like '%"+batchNum+"%'";
		}
		if(!StringUtils.isEmpty(minValue)) {
			sql1 +=" and pp.parameterValue>=" + Float.parseFloat(minValue);
		}
		if(!StringUtils.isEmpty(maxValue)) {
			sql1 +=" and pp.parameterValue<=" + Float.parseFloat(maxValue);
		}
		return (int) getSession().createNativeQuery(sql1).uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public int queryReverseCount(String serialNo, String batchNumber, String deviceSiteCode) {
		String hql = " select count(distinct record.opcNo) from ProcessRecord record where 1=1 ";
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(batchNumber)) {
			hql += " and record.batchNumber like ?" + (i++);
			args.add("%" + batchNumber + "%");
		}
		if(!StringUtils.isEmpty(serialNo)) {
			hql += " and record.opcNo like ?" + (i++);
			args.add("%" + serialNo + "%");
		}
		if(!StringUtils.isEmpty(deviceSiteCode)) {
			hql += " and record.deviceSiteCode like ?" + (i++);
			args.add("%" + deviceSiteCode + "%");
		}
		NativeQuery<Integer> nativeQuery = getSession().createNativeQuery(hql);
		for(int j = 0;j<args.size();j++) {
			nativeQuery.setParameter(j,args.get(j));
		}
		return nativeQuery.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryCount(String batchNumber, String stoveNumber, String deviceSiteCode, String ng) {
		String hql ="select p.deviceSiteCode,count(distinct p.id) "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where p.realRecord=1 ";
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(batchNumber)) {
			hql += " and p.batchNumber like ?" + (i++);
			args.add("%" + batchNumber + "%");
		}
		if(!StringUtils.isEmpty(stoveNumber)) {
			hql += " and p.stoveNumber like ?" + (i++);
			args.add("%" + stoveNumber + "%");
		}
		if(!StringUtils.isEmpty(deviceSiteCode)) {
			hql += " and p.deviceSiteCode like ?" + (i++);
			args.add("%" + deviceSiteCode + "%");
		}
		if(!StringUtils.isEmpty(ng)) {
			hql += " and (p.status='ng' or p.status='NG')";
		}
		hql += " group by p.deviceSiteCode";
		NativeQuery<Object[]> nativeQuery = getSession().createNativeQuery(hql);
		for(int j = 0;j<args.size();j++) {
			nativeQuery.setParameter(j,args.get(j));
		}
		return nativeQuery.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryDeviceSiteNGCount(String batchNumber, String stoveNumber, String deviceSiteCode) {
		String hql = "select p.deviceSiteCode,count(distinct p.id) "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where p.status='ng' ";
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(batchNumber)) {
			hql += " and p.batchNumber like ?" + (i++);
			args.add("%" + batchNumber + "%");
		}
		if(!StringUtils.isEmpty(stoveNumber)) {
			hql += " and p.stoveNumber like ?" + (i++);
			args.add("%" + stoveNumber + "%");
		}
		if(!StringUtils.isEmpty(deviceSiteCode)) {
			hql += " and p.deviceSiteCode like ?" + (i++);
			args.add("%" + deviceSiteCode + "%");
		}
		hql += " group by p.deviceSiteCode";
		NativeQuery<Object[]> nativeQuery = getSession().createNativeQuery(hql);
		for(int j = 0;j<args.size();j++) {
			nativeQuery.setParameter(j,args.get(j));
		}
		return nativeQuery.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public int queryNGCount(String batchNumber, String stoveNumber, String deviceSiteCode) {
		String hql = " select count(distinct record.opcNo) from ProcessRecord record where record.status='ng' ";
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(batchNumber)) {
			hql += " and record.batchNumber like ?" + (i++);
			args.add("%" + batchNumber + "%");
		}
		if(!StringUtils.isEmpty(stoveNumber)) {
			hql += " and record.stoveNumber like ?" + (i++);
			args.add("%" + stoveNumber + "%");
		}
		if(!StringUtils.isEmpty(deviceSiteCode)) {
			hql += " and record.deviceSiteCode like ?" + (i++);
			args.add("%" + deviceSiteCode + "%");
		}
		NativeQuery<Integer> nativeQuery = getSession().createNativeQuery(hql);
		for(int j = 0;j<args.size();j++) {
			nativeQuery.setParameter(j,args.get(j));
		}
		return nativeQuery.uniqueResult();
	}
	@Override
	public void deleteByOpcNo(String opcNo) {
		getSession().createNativeQuery("delete from processRecord where opcNo=?0").setParameter(0,opcNo).executeUpdate();
	}

	/**
	 * 根据日期，班次，设备站点分组查找产量
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return
	 */
	@Override
	public List<Object[]> queryOutput4DeviceSitePerDay(String begin, String end, List<String> codeList) {
		return getSession().createNativeQuery("select classesCode,deviceSiteCode,deviceSiteName,forProductionDate,count(1) from processRecord " +
				" where forProductionDate between ?0 and ?1 and deviceSiteCode in :codeList and realRecord=1 group by classesCode,deviceSiteCode,deviceSiteName,forProductionDate")
				.setParameter(0,begin)
				.setParameter(1,end)
				.setParameterList("codeList",codeList)
				.list();
	}
}
