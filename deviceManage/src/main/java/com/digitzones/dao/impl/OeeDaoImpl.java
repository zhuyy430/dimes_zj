package com.digitzones.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IOeeDao;
import com.digitzones.model.Classes;
import com.digitzones.model.DeviceSite;
@Repository
public class OeeDaoImpl  implements IOeeDao {
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public Session getSession() {
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return this.hibernateTemplate.getSessionFactory();
	}
	@SuppressWarnings("deprecation")
	@Override
	public Integer queryLostTimeByDeviceSiteId(Date today,Long deviceSiteId,Classes  classes) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long beginTime = classes.getStartTime().getTime();
		long endTime = classes.getEndTime().getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day  = c.get(Calendar.DATE);
		String sql = "select SUM(case when endTime is null then datediff(minute,beginTime,GETDATE()) else datediff(minute,beginTime,endTime) end) from LOSTTIMERECORD record where record.planHalt=0 "
				+ "  and year(record.lostTimeTime)=?0 and month(record.lostTimeTime)=?1"
				+ " and day(record.lostTimeTime)=?2 "
				+ " and record.deleted=0 and record.DEVICESITE_ID=?3 " ;
		if(beginTime>endTime) {
			sql +=" and (CONVERT(varchar(100),record.lostTimeTime,108)>=?4 and CONVERT(varchar(100),record.lostTimeTime,108)<='23:59:59')"
					+ " or (CONVERT(varchar(100),record.lostTimeTime,108)<=?5 and CONVERT(varchar(100),record.lostTimeTime,108)>='00:00:00')";
		}else {
			sql +=" and  CONVERT(varchar(100),record.lostTimeTime,108)>=?4 and CONVERT(varchar(100),record.lostTimeTime,108)<=?5";
		}

		Integer result = (Integer) getSession().createSQLQuery(sql)
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2,day)
				.setParameter(3,deviceSiteId)
				.setParameter(4, format.format(classes.getStartTime()))
				.setParameter(5, format.format(classes.getEndTime()))
				.uniqueResult();
		if(result == null) {
			return 0;
		}
		return result;
	}
	@SuppressWarnings("deprecation")
	@Override
	public Float queryProcessingBeatByWorkpieceIdAndProcessIdAndDeviceSiteId(Long workpieceId, Long processId,
			Long deviceSiteId) {
		String sql = "select wpd.processingBeat from WORKPIECEPROCESS_DEVICESITE wpd inner join WORKPIECE_PROCESS wp"
				+ " on wpd.WORKPIECEPROCESS_ID=wp.id where wp.PROCESS_ID=?0 and wp.WORKPIECE_ID=?1 and wpd.DEVICESITE_ID=?2";
		Object result = getSession().createSQLQuery(sql).setParameter(0,processId)
				.setParameter(1, workpieceId)
				.setParameter(2,deviceSiteId)
				.uniqueResult();
		if(result==null) {
			return 0f;
		}
		return ((Double) result).floatValue(); 
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Object[]> queryNGInfo4CurrentDay(Date today, Long deviceSiteId) {
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day  = c.get(Calendar.DATE);
		String sql = "select deviceSiteid,deviceSiteName,processId,workpiececode,SUM(case  when method.ngCount is null then 0 else method.ngCount end) from NGRECORD record inner join NGPROCESSMETHOD method on record.id=method.ngrecord_id "
				+ " where method.processMethod!=?0 and record.deleted=0 and year(record.inputDate)=?1 and month(record.inputDate)=?2"
				+ " and day(record.inputDate)=?3 and deviceSiteId=?4  group by deviceSiteid,deviceSiteName,processId,workpiececode ";
		return getSession().createSQLQuery(sql)
				.setParameter(0, Constant.ProcessRecord.COMPROMISE)
				.setParameter(1, year)
				.setParameter(2, month)
				.setParameter(3,day)
				.setParameter(4, deviceSiteId)
				.list();
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Object[]> queryNGInfo4CurrentMonth(Long deviceSiteId) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.DATE, 1);

		Date dateBegin = c.getTime();
		String hql = "select deviceSiteid,deviceSiteName,processId,workpiececode,SUM(case  when method.ngCount is null then 0 else method.ngCount end) from NGRECORD record inner join NGPROCESSMETHOD method on record.id=method.ngrecord_id where "
				+ " method.processMethod!=?0 and record.deleted=0"
				+ " and record.inputDate between ?1 and ?2  and deviceSiteid=?3 " + 
				" group by deviceSiteid,deviceSiteName,processId,workpiececode ";
		return getSession().createSQLQuery(hql)
				.setParameter(0, Constant.ProcessRecord.COMPROMISE)
				.setParameter(1,dateBegin)
				.setParameter(2, now)
				.setParameter(3,deviceSiteId)
				.list();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Object[]> queryNGInfo4PreMonth(Long deviceSiteId) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DATE, 1);
		Date dateBegin = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		Date dateEnd = c.getTime();
		String hql = "select deviceSiteid,deviceSiteName,processId,workpiececode,SUM(case  when method.ngCount is null then 0 else method.ngCount end) from NGRECORD record inner join NGPROCESSMETHOD method on record.id=method.ngrecord_id where "
				+ " method.processMethod!=?0 and record.deleted=0"
				+ " and record.inputDate between ?1 and ?2  and deviceSiteid=?3 " + 
				" group by deviceSiteid,deviceSiteName,processId,workpiececode ";
		return getSession().createSQLQuery(hql)
				.setParameter(0, Constant.ProcessRecord.COMPROMISE)
				.setParameter(1,dateBegin)
				.setParameter(2, dateEnd)
				.setParameter(3,deviceSiteId)
				.list();
	}
	@Override
	public double queryOee4CurrentClass(Classes c, DeviceSite ds, Date date) {
		double result = 0;
		Connection conn = null;
		CallableStatement state = null;
		try {
			conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
			state = conn.prepareCall("{call computeRealTimeOee(?,?,?,?)}");
			state.setLong(1, c.getId());
			state.setLong(2, ds.getId());
			state.setTimestamp(3,new Timestamp(date.getTime()));
			state.registerOutParameter(4, Types.DOUBLE);
			state.execute();
			result = state.getDouble(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(state!=null) {
					state.close();
				}
				
				if(conn!=null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
