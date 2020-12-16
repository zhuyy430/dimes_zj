package com.digitzones.dao.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.ILostTimeRecordDao;
import com.digitzones.model.Classes;
import com.digitzones.model.LostTimeRecord;
import com.digitzones.model.Pager;
@SuppressWarnings("deprecation")
@Repository
public class LostTimeRecordDaoImpl extends CommonDaoImpl<LostTimeRecord> implements ILostTimeRecordDao {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	
	public LostTimeRecordDaoImpl() {
		super(LostTimeRecord.class);
	}

	@Override
	public Double queryHoursOfLostTimeRecordByYearAndMonth(Integer year, Integer month) {
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?2)/60.0 else datediff(minute,beginTime,endTime)/60.0 end) "
				+ " from LostTimeRecord ltr inner join deviceSite ds on ltr.devicesite_id=ds.id where "
				+ "year(ltr.beginTime)=?0 and month(ltr.beginTime)=?1  and ltr.deleted=0 "
				+ " and ltr.planHalt=0 and ds.bottleneck=1";
		BigDecimal result = (BigDecimal) getSession().createNativeQuery(sql).setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, new Date())
				.uniqueResult();
		return result==null?0:result.doubleValue();
	}

	@Override
	public Double queryHoursOfPlanHaltByYearAndMonth(Integer year, Integer month) {
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?2)/60.0 else datediff(minute,beginTime,endTime)/60.0 end) "
				+ "from LostTimeRecord ltr inner join deviceSite ds on ltr.devicesite_id=ds.id where year(ltr.beginTime)=?0 and month(ltr.beginTime)=?1  and ltr.deleted=0 "
				+ " and  ltr.planHalt=0 and ds.bottleneck=1";
		BigDecimal result = (BigDecimal) getSession().createNativeQuery(sql).setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, new Date())
				.uniqueResult();
		return result==null?0:result.doubleValue();
	}

	@Override
	public Double queryLostTime(Classes c,Long deviceSiteId,Date now) {
		String sql = "select isnull(SUM(case endTime when null then datediff(minute,beginTime,?3) "
				+ "else datediff(minute,beginTime,endTime) end),0) from LostTimeRecord ltr where "
				+ " ltr.forLostTimeRecordDate=?0 "
				+ " and ltr.deviceSite_id=?1 and ltr.deleted=0 and ltr.classesCode=?2";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		query.setParameter(0, yyyyMMdd.format(now));
		query.setParameter(1, deviceSiteId);
		query.setParameter(2, c.getCode());
		query.setParameter(3, new Date());
		Integer result =  query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}
/*	@Override
	public Double queryLostTime(Classes c,Long deviceSiteId,Date now) {
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
		//班次结束时间
		Calendar endCalendar = Calendar.getInstance();
		if(c!=null) {
			endCalendar.setTime(c.getEndTime());
			endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
			endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
			endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		}
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		//午夜十二点
		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(now);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select isnull(SUM(case endTime when null then datediff(minute,beginTime,?3) "
				+ "else datediff(minute,beginTime,endTime) end),0) from LostTimeRecord ltr where "
				+ " ltr.beginTime>=?0 and ltr.endTime<=?1"
				+ " and ltr.deviceSite_id=?2 and ltr.deleted=0 and ltr.classesCode=?4";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<endDate.getTime()) {
			query.setParameter(0, startDate)
			.setParameter(1, endDate);
		}else {//开始时间》结束时间，说明跨天
			if(now.getTime()>startDate.getTime() && now.getTime()<=midnightDate.getTime()) {
				query.setParameter(0, startDate)
				.setParameter(1, now);
			}else //if(now.getTime()<endDate.getTime() && now.getTime()>midnightDate.getTime()) {
			{
				query.setParameter(0, midnightDate)
				.setParameter(1,now);
			}
		}
		
		query.setParameter(2, deviceSiteId);
		query.setParameter(3, new Date());
		query.setParameter(4, c.getCode());
		Integer result =  query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}
*/	@Override
	public List<Object[]> queryLostTime(Classes c, Date now) {
		//当前时间
				Calendar nowCalendar = Calendar.getInstance();
				nowCalendar.setTime(now);
				//班次开始时间
				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(c.getStartTime());
				startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
				startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
				startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
				//班次结束时间
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(c.getEndTime());
				endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
				endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
				endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));

				Date startDate = startCalendar.getTime();
				Date endDate = endCalendar.getTime();
				//午夜十二点
				Calendar midnightCalendar = Calendar.getInstance();
				midnightCalendar.setTime(now);
				midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
				midnightCalendar.set(Calendar.MINUTE, 59);
				midnightCalendar.set(Calendar.SECOND, 59);

				Date midnightDate = midnightCalendar.getTime();
				String sql = "select ltr.DEVICESITE_ID,SUM(case endTime when null then datediff(minute,beginTime,?3) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr "
						+ " inner join deviceSite ds on ltr.devicesite_id=ds.id where ds.bottleneck=1 and "
						+ " ltr.beginTime>=?0 and ltr.endTime<=?1"
						+ " and ltr.deleted=0 and ltr.classesCode=?2 group by ltr.DEVICESITE_ID";
				@SuppressWarnings("unchecked")
				NativeQuery<Object[]> query = getSession().createNativeQuery(sql);
				//班次开始时间小于结束时间
				if(startDate.getTime()<endDate.getTime()) {
					query.setParameter(0, startDate)
					.setParameter(1, endDate);
				}else {//开始时间》结束时间，说明跨天
					if(now.getTime()>startDate.getTime() && now.getTime()<=midnightDate.getTime()) {
						query.setParameter(0, startDate)
						.setParameter(1, now);
					}else{
						query.setParameter(0, midnightDate)
						.setParameter(1,now);
					}
				}
				query.setParameter(2, c.getCode());
				query.setParameter(3, now);
		return query.list();
	}
	public Double queryLostTimeByClassesAndProductionUnit(Classes c,Long productionUnitId,Date now) {
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		//班次开始时间
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(c.getStartTime());
		startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
		startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		//班次结束时间
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(c.getEndTime());
		endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
		endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		//午夜十二点
		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(now);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select SUM(case ltr.endTime when null then datediff(minute,ltr.beginTime,?3) else datediff(minute,ltr.beginTime,ltr.endTime) end) " 
					+ " from LostTimeRecord ltr inner join DEVICESITE ds on ltr.DEVICESITE_ID = ds.id inner join DEVICE d on ds.DEVICE_ID = d.id "
					+ " where d.PRODUCTIONUNIT_ID=?2 and "
					+ " ltr.beginTime>=?0 and ltr.endTime<=?1 "
					+ " and ltr.deleted=0 and d.isDimesUse=1 and ds.bottleneck=1";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<endDate.getTime()) {
			query.setParameter(0, startDate)
			.setParameter(1, now);
		}else {//开始时间》结束时间，说明跨天
			if(now.getTime()>startDate.getTime() && now.getTime()<=midnightDate.getTime()) {
				query.setParameter(0, startDate)
				.setParameter(1, now);
			}else 
			{
				query.setParameter(0, midnightDate)
				.setParameter(1,now);
			}
		}
		
		query.setParameter(2, productionUnitId);
		query.setParameter(3, new Date());
		Integer result =  query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}
	@Override
	public Double queryLostTime4PerDay(Classes c,Long deviceSiteId,Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);

		date = calendar.getTime();
		Calendar c2 = Calendar.getInstance();
		c2.setTime(c.getStartTime());
		c2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		c2.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
		c2.set(Calendar.DATE,calendar.get(Calendar.DATE));
		Date begin = c2.getTime();
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?1) else datediff(minute,beginTime,endTime) end) "
				+ " from LostTimeRecord ltr where  ltr.beginTime>=?0 and year(ltr.beginTime)=?4 and month(ltr.beginTime)=?5"
				+ " and day(ltr.beginTime)=?6"
				+ " and ltr.deviceSite_id=?2 and ltr.deleted=0 and ltr.classesCode=?3";
		Integer result = (Integer) getSession().createNativeQuery(sql).setParameter(0, begin)
				.setParameter(1, new Date())
				.setParameter(2, deviceSiteId)
				.setParameter(3, c.getCode())
				.setParameter(4, calendar.get(Calendar.YEAR))
				.setParameter(5, calendar.get(Calendar.MONTH)+1)
				.setParameter(6, calendar.get(Calendar.DATE))
				.uniqueResult();
		return result==null?0:result.doubleValue();
	}

	@Override
	public Double queryPlanHaltTime(Classes c, Long deviceSiteId ,Date now) {
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?4) else datediff(minute,beginTime,endTime) end) from "
				+ " LostTimeRecord ltr where  "
				+ " ltr.forLostTimeRecordDate=?0 and ltr.planHalt=?3 and ltr.deleted=0"
				+ " and ltr.deviceSite_id=?2 and ltr.classesCode=?1";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		query.setParameter(0, yyyyMMdd.format(now));
		query.setParameter(2, deviceSiteId);
		query.setParameter(3,true);
		query.setParameter(4, new Date());
		query.setParameter(1, c.getCode());
		Integer result = (Integer) query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}
/*	@Override
	public Double queryPlanHaltTime(Classes c, Long deviceSiteId ,Date now) {
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		//班次开始时间
		Calendar startCalendar = Calendar.getInstance();
		if(c!=null) {
			startCalendar.setTime(c.getStartTime());
			startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
			startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH)+1);
			startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		}
		//班次结束时间
		Calendar endCalendar = Calendar.getInstance();
		if(c!=null) {
			endCalendar.setTime(c.getStartTime());
			endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
			endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH)+1);
			endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		}
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		//午夜十二点
		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(now);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?4) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr where  "
				+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and ltr.planHalt=?3 and ltr.deleted=0"
				+ " and ltr.deviceSite_id=?2 and ltr.classesCode=?5";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<endDate.getTime()) {
			query.setParameter(0, startDate)
			.setParameter(1, now);
		}else {//开始时间》结束时间，说明跨天
			if(now.getTime()>startDate.getTime() && now.getTime()<=midnightDate.getTime()) {
				query.setParameter(0, startDate)
				.setParameter(1, now);
			}
			
			if(now.getTime()<endDate.getTime()) {
				query.setParameter(0, midnightDate)
				.setParameter(1,now);
			}
		}
		
		query.setParameter(2, deviceSiteId);
		query.setParameter(3,true);
		query.setParameter(4, new Date());
		query.setParameter(5, c.getCode());
		Integer result = (Integer) query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}
*/	@Override
	public List<Object[]> queryPlanHaltTime(Classes c, Date now) {
		//当前时间
				Calendar nowCalendar = Calendar.getInstance();
				nowCalendar.setTime(now);
				//班次开始时间
				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(c.getStartTime());
				startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
				startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH)+1);
				startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
				//班次结束时间
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(c.getStartTime());
				endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
				endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH)+1);
				endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
				Date startDate = startCalendar.getTime();
				Date endDate = endCalendar.getTime();
				//午夜十二点
				Calendar midnightCalendar = Calendar.getInstance();
				midnightCalendar.setTime(now);
				midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
				midnightCalendar.set(Calendar.MINUTE, 59);
				midnightCalendar.set(Calendar.SECOND, 59);

				Date midnightDate = midnightCalendar.getTime();
				String sql = "select ltr.DEVICESITE_ID, SUM(case endTime when null then datediff(minute,beginTime,?3) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr "
						+ " inner join deviceSite ds on ltr.devicesite_id=ds.id where ds.bottleneck=1 and "
						+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and ltr.planHalt=?2 and ltr.deleted=0"
						+ "  and ltr.classesCode=?4 group by ltr.DEVICESITE_ID";
				@SuppressWarnings("unchecked")
				NativeQuery<Object[]> query = getSession().createNativeQuery(sql);
				//班次开始时间小于结束时间
				if(startDate.getTime()<endDate.getTime()) {
					query.setParameter(0, startDate)
					.setParameter(1, now);
				}else {//开始时间》结束时间，说明跨天
					if(now.getTime()>startDate.getTime() && now.getTime()<=midnightDate.getTime()) {
						query.setParameter(0, startDate)
						.setParameter(1, now);
					}

					if(now.getTime()<endDate.getTime()) {
						query.setParameter(0, midnightDate)
						.setParameter(1,now);
					}
				}
				query.setParameter(2,true);
				query.setParameter(3, new Date());
				query.setParameter(4, c.getCode());
		return query.list();
	}
	public Double queryPlanHaltTimeByClassesAndProductionUnit(Classes c, Long productionUnitId ,Date now) {
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		//班次开始时间
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(c.getStartTime());
		startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH)+1);
		startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		//班次结束时间
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(c.getStartTime());
		endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH)+1);
		endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		
		Date startDate = startCalendar.getTime();
		Date endDate = endCalendar.getTime();
		//午夜十二点
		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(now);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select SUM(case ltr.endTime when null then datediff(minute,ltr.beginTime,?4) else datediff(minute,ltr.beginTime,ltr.endTime) end) " 
					+ " from LostTimeRecord ltr  inner join DEVICESITE ds on ltr.DEVICESITE_ID = ds.id inner join DEVICE d on ds.DEVICE_ID = d.id "
					+ " where d.PRODUCTIONUNIT_ID=?2 and "
					+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and ltr.planHalt=?3 and ltr.deleted=0 "
					+ " and ltr.classesCode=?5 and d.isDimesUse=1 and ds.bottleneck=1 ";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<endDate.getTime()) {
			query.setParameter(0, startDate)
			.setParameter(1, now);
		}else {//开始时间》结束时间，说明跨天
			if(now.getTime()>startDate.getTime() && now.getTime()<=midnightDate.getTime()) {
				query.setParameter(0, startDate)
				.setParameter(1, now);
			}
			
			if(now.getTime()<endDate.getTime()) {
				query.setParameter(0, midnightDate)
				.setParameter(1,now);
			}
		}
		
		query.setParameter(2, productionUnitId);
		query.setParameter(3,true);
		query.setParameter(4, new Date());
		query.setParameter(5, c.getCode());
		Integer result = (Integer) query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}
	@Override
	public Integer queryLostTime4RealTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day  = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		c.add(Calendar.HOUR_OF_DAY, -12);
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?5) else datediff(minute,beginTime,endTime) end) from LOSTTIMERECORD record where record.planHalt=0 "
				+ "  and year(record.lostTimeTime)=?0 and month(record.lostTimeTime)=?1"
				+ " and day(record.lostTimeTime)=?2 and datename(hh,record.lostTimeTime)=?3 and datename(mi,record.lostTimeTime)=?4"
				+ " and record.deleted=0 and record.beginTime>=?6" ;
		Integer result = (Integer) getSession().createNativeQuery(sql)
				.setParameter(0, year)
				.setParameter(1,month)
				.setParameter(2, day)
				.setParameter(3, hour)
				.setParameter(4, minute)
				.setParameter(5, new Date())
				.setParameter(6, c.getTime())
				.uniqueResult();
		if(result == null) {
			return 0;
		}
		return result;
	}

	@Override
	public Integer queryLostTimeFromBeginOfMonthUntilTheDate(Date date,Boolean halt) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);

		Date beginOfMonth = c.getTime();
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?2) else datediff(minute,beginTime,endTime) end) "
				+ "		from LOSTTIMERECORD record inner join DEVICESITE ds on record.DEVICESITE_ID = ds.id where ds.bottleneck=1 and "
				+ "  record.lostTimeTime between ?0 and ?1 "
				+ " and record.deleted=0 " ;
		if(halt!=null) {
			if(halt==true) {
				sql += " and record.planHalt=1";
			}else {
				sql +=" and record.planHalt=0";
			}
		}
		Integer result = (Integer) getSession().createNativeQuery(sql)
				.setParameter(0, beginOfMonth)
				.setParameter(1, date)
				.setParameter(2, new Date())
				.uniqueResult();
		if(result == null) {
			return 0;
		}
		return result;
	}

	@Override
	public Long queryLostTime4TheDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DATE);
		String hql = "select count(*) from LostTimeRecord record where year(record.lostTimeTime)=?0 and month(record.lostTimeTime)=?1 and day(record.lostTimeTime)=?2 and record.deleted=?3";
		return (Long) getHibernateTemplate().find(hql, new Object[] {year,month,day,false}).get(0);
	}

	@Override
	public List<Object[]> queryLostTimePerLosttimeType(Classes c,Long deviceSiteId) {
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

		String sql = "select lostTimeTypeName,SUM(case endTime when null then datediff(minute,beginTime,?3) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr where ltr.DEVICESITE_ID=?2 and  "
				+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and ltr.deleted=0"
				+ " group by lostTimeTypeName";
		@SuppressWarnings("unchecked")
		SQLQuery<Object[]> query = getSession().createNativeQuery(sql);
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2, deviceSiteId)
		.setParameter(3, new Date());

		return query.list();
	}

	@Override
	public Double queryLostTime4Classes(Classes c,Long deviceSiteId) {
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
		String sql = "select  SUM(case endTime when null then datediff(minute,beginTime,?1) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr where  "
				+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and ltr.deviceSite_id=?2 and ltr.deleted=0";  
		@SuppressWarnings("unchecked")
		SQLQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2, deviceSiteId);
		Integer result =  query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}

	@Override
	public long queryUnhandledLostTimeRecordCount4Classes(Classes c) {
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

		String sql = "select count(ltr.id) from LostTimeRecord ltr where"
				+ " ltr.beginTime>=?0  and (ltr.reason is null or ltr.reason='') and ltr.deleted=0";
		@SuppressWarnings("unchecked")
		SQLQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate);
		Integer result =  query.uniqueResult();
		return result!=null?result:0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LostTimeRecord queryUnEndLastLostTimeRecord(Long deviceSiteId) {
		String hql = "from LostTimeRecord record where record.id in (select max(r.id) from LostTimeRecord r where r.deviceSite.id=?0"
				+ " and r.endTime is null and r.deleted=?1) and record.deleted=?1 ";
		List<LostTimeRecord> list = (List<LostTimeRecord>) this.getHibernateTemplate().find(hql, new Object[] {deviceSiteId,false});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryLostTimeRecordByYearAndMonth(Integer year, Integer month) {
		String sql = "select ltr.lostTimeTypeName,SUM(case endTime when null then datediff(minute,beginTime,?2)/60.0 else datediff(minute,beginTime,endTime)/60.0 end)"
				+ " from LostTimeRecord ltr inner join deviceSite ds on ltr.devicesite_id=ds.id where year(ltr.beginTime)=?0 and month(ltr.beginTime)=?1  and ltr.deleted=0 "
				+ "  and ltr.planHalt!=1 and ds.bottleneck=1 "
				+ " group by ltr.lostTimeTypeName";
		return getSession().createNativeQuery(sql).setParameter(0,year)
				.setParameter(1, month)
				.setParameter(2, new Date()).list();
	}

	@Override
	public List<Object[]> queryLostTimeRecordByProductionIdsAndYearAndMonth(Integer year,Integer month,Integer day,String productionUnitIds) {
		String sql = "select ltr.lostTimeTypeName,SUM(case endTime when null then datediff(minute,beginTime,?0)/60.0 else datediff(minute,beginTime,endTime)/60.0 end)"
				+ " from LostTimeRecord ltr inner join deviceSite ds on ltr.devicesite_id=ds.id  inner join device d on ds.device_id=d.id  where year(ltr.beginTime)=?1 and month(ltr.beginTime)=?2 and day(ltr.beginTime)=?3  and ltr.deleted=0 "
				+ "  and ltr.planHalt!=1 and ds.bottleneck=1 ";

		if(productionUnitIds!=null && !productionUnitIds.equals("")) {
			productionUnitIds = productionUnitIds.replace("'", "");
			String[] pIds= productionUnitIds.split(",");
			System.out.println(pIds.length);
			if(pIds.length>0){
				sql+="and d.PRODUCTIONUNIT_ID in (";
				for(int i=0;i<pIds.length;i++){
					if(i==pIds.length-1){
						sql+=pIds[i];
					}else{
						sql+=pIds[i]+",";
					}
				}
				sql+=" )";
			}
		}
		sql+= " group by ltr.lostTimeTypeName";
		return getSession().createNativeQuery(sql).setParameter(0,new Date())
				.setParameter(1, year)
				.setParameter(2, month)
				.setParameter(3, day).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LostTimeRecord> queryLostTime4RealTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, -12);
		String sql = "select * from LOSTTIMERECORD record inner join deviceSite ds on record.devicesite_id=ds.id  where record.planHalt=0 "
				+ " and record.deleted=0 and record.beginTime>=?0 and ds.bottleneck=1" ;
		return getSession().createNativeQuery(sql)
				.setParameter(0, c.getTime()).addEntity(LostTimeRecord.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryOneMonthLostTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		//月初
		Date monthStartDate = c.getTime();

		c.add(Calendar.MONTH, 1);
		//下月初
		Date nextMonthStartDate = c.getTime();
		String sql = "select classesCode,convert(varchar(100),lostTimeTime,23) occDate,"
				+ "SUM(case endTime when null then datediff(minute,beginTime,?0) else datediff(minute,beginTime,endTime) end) "
				+ " from LOSTTIMERECORD ltr inner join deviceSite ds on ltr.devicesite_id=ds.id where ltr.losttimetime is not null "
				+ " and ltr.losttimetime>=?1 and ltr.losttimetime<?2 and ltr.beginTime>=?1 and ltr.deleted=0 and ds.bottleneck=1 "
				+ " group by convert(varchar(100),lostTimeTime,23) ,classesCode";
		return getSession().createNativeQuery(sql)
				.setParameter(0,new Date())
				.setParameter(1, monthStartDate)
				.setParameter(2, nextMonthStartDate)
				.list();
	}

	@Override
	public List<Object[]> queryOneMonthPlanHaltTime(Date date) {
		return null;
	}

	@Override
	public Double queryLostTime4ProductionUnit(Classes c, Long productionUnitId) {
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
		String sql = "select  SUM(case endTime when null then datediff(minute,beginTime,?1) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr "
				+ " inner join DEVICESITE ds on ltr.deviceSite_id=ds.id " + 
				"inner join DEVICE d on ds.DEVICE_ID = d.id inner join PRODUCTIONUNIT pu on pu.id=d.PRODUCTIONUNIT_ID "
				+ "where  ds.bottleneck=1 and "
				+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and pu.id=?2 and ltr.deleted=0 and d.isDimesUse=1";  
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		//班次开始时间小于结束时间
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2, productionUnitId);
		Integer result =  query.uniqueResult();
		return result!=null?result.doubleValue():0;
	}

	@Override
	public List<Object[]> queryLostTimePerLosttimeType4ProductionUnit(Classes c, Long productionUnitId) {
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

		String sql = "select lostTimeTypeName,SUM(case endTime when null then datediff(minute,beginTime,?3) else datediff(minute,beginTime,endTime) end) from LostTimeRecord ltr"
				+ " inner join DEVICESITE ds on ltr.deviceSite_id=ds.id " + 
				"inner join DEVICE d on ds.DEVICE_ID = d.id inner join PRODUCTIONUNIT pu on pu.id=d.PRODUCTIONUNIT_ID"
				+ " where pu.id=?2 and  ds.bottleneck=1 and "
				+ " ltr.beginTime>=?0 and ltr.endTime<=?1 and ltr.deleted=0 and d.isDimesUse=1 "
				+ " group by lostTimeTypeName";
		@SuppressWarnings("unchecked")
		NativeQuery<Object[]> query = getSession().createNativeQuery(sql);
		if(startDate.getTime()<now.getTime()) {

		}else {//开始时间》结束时间，说明跨天
			startCalendar.add(Calendar.DATE, -1);
			startDate = startCalendar.getTime();
		}
		query.setParameter(0, startDate)
		.setParameter(1, now)
		.setParameter(2, productionUnitId)
		.setParameter(3, new Date());

		return query.list();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager<List<Object[]>> queryLostTimeCountReport(Map<String, String> params, int rows, int page) {
		String sql = "	select COUNT(ltr.id) _count, pu.code pu_code,pu.name pu_name,ds.code ds_code,ds.name ds_name,basic.name basic_name,type.name typeName,ltr.reason," + 
				"SUM(case endTime when null then datediff(minute,beginTime,:beginDate) else datediff(minute,beginTime,endTime) end) lostTime from LOSTTIMERECORD ltr inner join PRESSLIGHTTYPE type on ltr.lostTimeTypeCode=type.code" + 
				"	left join PRESSLIGHTTYPE basic on type.basicCode=basic.code" + 
				"	inner join DEVICESITE ds on ltr.DEVICESITE_ID = ds.id " + 
				"	inner join DEVICE d on ds.DEVICE_ID=d.id" + 
				"	inner join PRODUCTIONUNIT pu on d.PRODUCTIONUNIT_ID = pu.id where d.isDimesUse=1 and ds.bottleneck=1 ";


		String groupBy = "	group by pu.code ,pu.name ,ds.code ,ds.name ,basic.name ,type.name ,ltr.reason";

		String productionUnitId = params.get("productionUnitId");
		String deviceSite = params.get("deviceSiteCodes");
		String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			sql += " and pu.id=:productionUnitId";
		}

		if(beginDateStr!=null && !"".equals(beginDateStr)) {
			sql += " and ltr.lostTimeTime >=:beginTime";
		}
		
		if(endDateStr!=null && !"".equals(endDateStr)) {
			sql += " and ltr.lostTimeTime<=:endTime";
		}
		
		if(deviceSite!=null && !"".equals(deviceSite.trim())) {
			sql += " and ds.code in (:deviceSiteCodes)";
		}
		String countQuery = "select count(w.pu_code) from (" + sql + groupBy + " ) w";
		NativeQuery countQueryObj = getSession().createNativeQuery(countQuery);
		countQueryObj.setParameter("beginDate", new Date());
		
		String dataQuery =  sql + groupBy;

		NativeQuery dataQueryObj = getSession().createNativeQuery(dataQuery);
		dataQueryObj.setFirstResult((page-1)*rows)
		.setMaxResults(rows)
		.setParameter("beginDate", new Date());
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			countQueryObj.setParameter("productionUnitId", productionUnitId);
			dataQueryObj.setParameter("productionUnitId", productionUnitId);
		}
		if(beginDateStr!=null && !"".equals(beginDateStr)) {
			try {
				countQueryObj.setParameter("beginTime", format.parse(beginDateStr));
				dataQueryObj.setParameter("beginTime", format.parse(beginDateStr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(endDateStr!=null && !"".equals(endDateStr)) {
			try {
				countQueryObj.setParameter("endTime", format.parse(endDateStr));
				dataQueryObj.setParameter("endTime", format.parse(endDateStr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(deviceSite!=null && !"".equals(deviceSite.trim())) {
			String[] deviceSites = deviceSite.split(",");
			countQueryObj.setParameterList("deviceSiteCodes", deviceSites);
			dataQueryObj.setParameterList("deviceSiteCodes", deviceSites);
		}
		long count = (Integer) countQueryObj.list().get(0);
		if(count<1) {
			return new Pager<List<Object[]>>();
		}

		List list = dataQueryObj.list();
		return new Pager<List<Object[]>>((page-1)*rows, rows, (int)count, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryLostTimeDetail(String typeName, int month) {
		String sql = "select lostTimeTypeName,reason,SUM(case endTime when null then datediff(minute,beginTime,:beginDate) else datediff(minute,beginTime,endTime) end) lostTime from LOSTTIMERECORD record "
				+ " inner join devicesite ds on record.devicesite_id= ds.id "
				+ "where lostTimeTypeName=:lostTimeTypeName"
				+ " and month(record.lostTimeTime)=:month"
				+ " and month(record.beginTime)=:month  and ds.bottleneck=1 group by reason,lostTimeTypeName";
		return getSession().createNativeQuery(sql).setParameter("lostTimeTypeName", typeName)
				.setParameter("month", month)
				.setParameter("beginDate", new Date()).list();
	}

	@Override
	public Double queryLostTime4PerDay(Classes c, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);

		date = calendar.getTime();
		Calendar c2 = Calendar.getInstance();
		c2.setTime(c.getStartTime());
		c2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		c2.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
		c2.set(Calendar.DATE,calendar.get(Calendar.DATE));
		Date begin = c2.getTime();
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?1) else datediff(minute,beginTime,endTime) end) "
				+ " from LostTimeRecord ltr"
				+ " inner join devicesite ds on ltr.devicesite_id= ds.id "
				+ " where  ltr.beginTime>=?0 and year(ltr.beginTime)=?3 and month(ltr.beginTime)=?4"
				+ " and day(ltr.beginTime)=?5"
				+ " and ltr.planHalt=0 and ltr.deleted=0 and ltr.classesCode=?2 and ds.bottleneck=1";
		Integer result = (Integer) getSession().createNativeQuery(sql).setParameter(0, begin)
				.setParameter(1, new Date())
				.setParameter(2, c.getCode())
				.setParameter(3, calendar.get(Calendar.YEAR))
				.setParameter(4, calendar.get(Calendar.MONTH)+1)
				.setParameter(5, calendar.get(Calendar.DATE))
				.uniqueResult();
		return result==null?0:result.doubleValue();
	}
	@Override
	public Integer queryLostTimeForPerDay(Classes classes, Date date,Long productionUnitId) {
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?0) else datediff(minute,beginTime,endTime) end) as val"
				+ " from LostTimeRecord ltr inner join DEVICESITE ds on ltr.DEVICESITE_ID=ds.id "
				+ " inner join DEVICE d on ds.DEVICE_ID=d.id "
				+ " where ds.bottleneck=1 and ltr.forLostTimeRecordDate=?0 and ltr.classesCode =?1 and d.PRODUCTIONUNIT_ID=?2 "
				+ " and d.isDimesUse=1 and ltr.deleted=0 ";
		String dataString = yyyyMMdd.format(date);
		 Integer count = (Integer) getSession().createNativeQuery(sql)
				 .setParameter(0, dataString)
				.setParameter(1, classes.getCode())
				.setParameter(2, productionUnitId)
				.uniqueResult();
		return count == null?0:count;
	}
/*	@Override
	public Integer queryLostTimeForPerDay(Classes classes, Date date,Long productionUnitId) {
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
		String sql = "select SUM(case endTime when null then datediff(minute,beginTime,?0) else datediff(minute,beginTime,endTime) end) as val"
				+ " from LostTimeRecord ltr inner join DEVICESITE ds on ltr.DEVICESITE_ID=ds.id inner join DEVICE d on ds.DEVICE_ID=d.id "
				+ " where ds.bottleneck=1 and ltr.beginTime BETWEEN ?1 AND ?2 and ltr.classesCode =?3 and d.PRODUCTIONUNIT_ID=?4 "
				+ " and d.isDimesUse=1 and ltr.deleted=0 ";
		Integer count = (Integer) getSession().createNativeQuery(sql)
				.setParameter(0, date)
				.setParameter(1, startDate)
				.setParameter(2, endDate)
				.setParameter(3, classes.getCode())
				.setParameter(4, productionUnitId)
				.uniqueResult();
		return count == null?0:count;
	}
*/	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryLostTime4PerMonth(Classes c, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String sql = "select convert(varchar(10),ltr.lostTimeTime,120) ltime,SUM(case endTime when null then datediff(minute,beginTime,?0) else datediff(minute,beginTime,endTime) end) _sumLostTime"
				+ " from LostTimeRecord ltr "
				+ " inner join devicesite ds on record.devicesite_id= ds.id "
				+ "where  year(ltr.beginTime)=?1 and month(ltr.beginTime)=?2"
				+ " and ltr.planHalt=0 and ltr.deleted=0 and ltr.classesCode=?3 and ds.bottleneck=1 "
				+ " group by convert(varchar(10),ltr.lostTimeTime,120)";
		
		List<Object[]> list = getSession().createNativeQuery(sql)
							.setParameter(0, date)
							.setParameter(1, calendar.get(Calendar.YEAR))
							.setParameter(2, calendar.get(Calendar.MONTH)+1)
							.setParameter(3, c.getCode())
							.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryLostTimeAndPlanHaltTime(Classes c, Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date monthBegin = calendar.getTime();
		String sql = "select convert(varchar(10),lostTimeTime,120) ltt, SUM(case endTime when null then datediff(minute,beginTime,?3) else datediff(minute,beginTime,endTime) end) sum01,"
				+ " SUM(case when (endTime is null and planHalt=1)  then datediff(minute,beginTime,?4) " + 
				" when endTime is not null and planHalt=1 then datediff(minute,beginTime,endTime) " + 
				" else planHalt end) tt"
				+ " from LostTimeRecord ltr "
				+ " inner join devicesite ds on record.devicesite_id= ds.id "
				+ "where lostTimeTime between ?0 and ?1 and "
				+ " ltr.classesCode=?2 and ltr.deleted=0 and ds.bottleneck=1  group by convert(varchar(10),lostTimeTime,120)";
		
		List<Object[]> list = getSession().createNativeQuery(sql)
					.setParameter(0, monthBegin)
					.setParameter(1, now)
					.setParameter(2, c.getCode())
					.setParameter(3, now)
					.setParameter(4, now)
					.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryAllMonthLostTimeRecordFor1Year(Integer year) {
		String sql = "select ltr.lostTimeTypeName,SUM(case endTime when null then datediff(minute,beginTime,?1)/60.0 else datediff(minute,beginTime,endTime)/60.0 end),month(ltr.beginTime) as month"
				+ " from LostTimeRecord ltr "
				+ " inner join devicesite ds on ltr.devicesite_id= ds.id "
				+ "where year(ltr.beginTime)=?0 and ltr.deleted=0 "
				+ "  and ltr.planHalt!=1 and ds.bottleneck=1 "
				+ " group by ltr.lostTimeTypeName,month(ltr.beginTime)";
		return getSession().createNativeQuery(sql).setParameter(0,year)
				.setParameter(1, new Date()).list();
	}
	@Override
	public List<?> queryHoursOfLostTimeRecord(Integer year) {
		String sql = "select MONTH(a.data) as mon ,sum(a.time) as losttime  from "
				+ "(select convert(varchar(10),lostTimeTime,120) as data,"
				+ "SUM(case endTime when null then datediff(minute,beginTime,GETDATE())/60.0 else  datediff(minute,beginTime,endTime)/60.0 end) as time "
				+ "from LostTimeRecord ltr "
				+ " inner join devicesite ds on ltr.devicesite_id= ds.id "
				+ "where  year(ltr.beginTime)=?0 and ltr.deleted=0  and ltr.planHalt=0 and ds.bottleneck=1 group by ltr.lostTimeTime) "
				+ "as a group by MONTH(a.data)";
		List<?> result= getSession().createNativeQuery(sql).setParameter(0,year).list();
		return result;
	}
}
