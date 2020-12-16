package com.digitzones.dao.impl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.digitzones.constants.Constant;
import com.digitzones.dao.INGRecordDao;
import com.digitzones.model.Classes;
import com.digitzones.model.NGRecord;
@Repository
public class NGRecordDaoImpl extends CommonDaoImpl<NGRecord> implements INGRecordDao {
	public NGRecordDaoImpl() {
		super(NGRecord.class);
	}

	@Override
	public Integer queryScrapCountByDateAndProcessId(Date date, Long processId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		String sql = "select SUM(method.ngCount) from NGRECORD record inner join NGPROCESSMETHOD method on record.id = method.NGRECORD_ID" 
				+ " where year(record.occurDate)=?0 and month(record.occurDate)=?1 and day(occurDate)=?2"
				+ " and method.processMethod=?3 and record.processId=?4";

		Integer result = (Integer) getSession().createNativeQuery(sql)
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, day)
				.setParameter(3, Constant.ProcessRecord.SCRAP)
				.setParameter(4, processId)
				.uniqueResult();
		return result!=null?result:0;
	}
	@Override
	public List<?> queryScrapCountByDateAndProcessId(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		String sql = "select day(occurDate) as day,record.processId as processId,SUM(method.ngCount) as total"
				+ " from NGRECORD record inner join NGPROCESSMETHOD method on record.id = method.NGRECORD_ID" 
				+ " where year(record.occurDate)=?0 and month(record.occurDate)=?1 "
				+ " and method.processMethod=?2 group by day(occurDate),record.processId";
		
		List<?> result = getSession().createNativeQuery(sql)
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, Constant.ProcessRecord.SCRAP)
				.list();
		return result;
	}

	@Override
	public Integer queryNgCountByDeviceSiteId(Long deviceSiteId, Date today) {
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		String sql = "select SUM(method.ngCount) from NGRECORD record inner join NGPROCESSMETHOD method "
				+ " on record.id=method.NGRECORD_ID where record.deviceSiteId=?3 "
				+ "and year(record.occurDate)=?0 and month(record.occurDate)=?1 and day(occurDate)=?2";

		Integer result = (Integer) getSession().createNativeQuery(sql)
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, day)
				.setParameter(3, deviceSiteId)
				.uniqueResult();
		return result!=null?result:0;
	}

	@Override
	public Integer queryNgCount4Class(Classes classes, Long deviceSiteId,Date now) {
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);

		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(now);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		Date dayBegin = nowCalendar.getTime();
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select SUM(method.ngCount) from NGRECORD record inner join NGPROCESSMETHOD method "
				+ " on record.id=method.NGRECORD_ID where record.deviceSiteId=?2 "
				+ " and record.occurDate>=?0 and record.occurDate<?1  and method.processMethod!=?3"
				+ " and record.classesId=?4";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		query.setParameter(0, dayBegin);
		query.setParameter(1, midnightDate);
		query.setParameter(2, deviceSiteId);
		query.setParameter(3, Constant.ProcessRecord.COMPROMISE);
		query.setParameter(4, classes.getId());
		Integer result = (Integer) query.uniqueResult();
		return result!=null?result:0;
	}
	@Override
	public Integer queryNgCount4ClassByClassesAndProductionUnit(Classes classes, Long productionUnitId,Date now) {
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);
		
		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(now);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		Date dayBegin = nowCalendar.getTime();
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select SUM(method.ngCount) from NGRECORD record inner join NGPROCESSMETHOD method "
				 + " on record.id=method.NGRECORD_ID inner join DEVICESITE ds on record.deviceSiteId = ds.id inner join DEVICE d on "
				 + " ds.DEVICE_ID = d.id where d.PRODUCTIONUNIT_ID =?2 "
				 + " and record.occurDate>=?0 and record.occurDate<?1  and method.processMethod!=?3 "
				 + " and record.classesId=?4 and d.isDimesUse=1 and ds.bottleneck=1 ";
		@SuppressWarnings("unchecked")
		NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		query.setParameter(0, dayBegin);
		query.setParameter(1, midnightDate);
		query.setParameter(2, productionUnitId);
		query.setParameter(3, Constant.ProcessRecord.COMPROMISE);
		query.setParameter(4, classes.getId());
		Integer result = (Integer) query.uniqueResult();
		return result!=null?result:0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryNgCount4Class(Classes classes, Date now) {
		//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);
		nowCalendar.set(Calendar.DATE, 1);
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);
		String sql = "select convert(varchar(10),record.occurDate,120) occDate,SUM(method.ngCount) _ngCount from NGRECORD record inner join NGPROCESSMETHOD method "
				+ " on record.id=method.NGRECORD_ID where  "
				+ "record.occurDate between ?0 and ?1  and method.processMethod!=?2"
				+ " and record.classesId=?3 group by convert(varchar(10),record.occurDate,120)";
		List<Object[]> list = getSession().createNativeQuery(sql)
					.setParameter(0, nowCalendar.getTime())
					.setParameter(1, now)
					.setParameter(2,  Constant.ProcessRecord.COMPROMISE)
					.setParameter(3, classes.getId())
					.list();
		return list;
	}
	@Override
	public List<Object[]> queryNgCount4ClassToday(Classes classes, Date now) {
		//当前时间
				Calendar nowCalendar = Calendar.getInstance();
				nowCalendar.setTime(now);
				nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
				nowCalendar.set(Calendar.MINUTE, 0);
				nowCalendar.set(Calendar.SECOND, 0);

				Calendar midnightCalendar = Calendar.getInstance();
				midnightCalendar.setTime(now);
				midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
				midnightCalendar.set(Calendar.MINUTE, 59);
				midnightCalendar.set(Calendar.SECOND, 59);
				Date dayBegin = nowCalendar.getTime();
				Date midnightDate = midnightCalendar.getTime();
				String sql = "select record.deviceSiteId, SUM(method.ngCount) _sum from NGRECORD record inner join NGPROCESSMETHOD method "
						+ " on record.id=method.NGRECORD_ID inner join devicesite ds on record.deviceSiteId=ds.id where "
						+ " record.occurDate>=?0 and record.occurDate<?1  and method.processMethod!=?2"
						+ " and record.classesId=?3 and ds.bottleneck=1 group by record.deviceSiteId";
				@SuppressWarnings("unchecked")
				NativeQuery<Object[]> query = getSession().createNativeQuery(sql);
				query.setParameter(0, dayBegin);
				query.setParameter(1, midnightDate);
				query.setParameter(2, Constant.ProcessRecord.COMPROMISE);
				query.setParameter(3, classes.getId());
				return query.list();
	}
	@Override
	public List<Object[]> queryNgCount4DeviceSiteShow(List<Long> deviceSiteIdList, Date date) {
		/*//当前时间
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(date);
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);

		Calendar midnightCalendar = Calendar.getInstance();
		midnightCalendar.setTime(date);
		midnightCalendar.set(Calendar.HOUR_OF_DAY, 23);
		midnightCalendar.set(Calendar.MINUTE, 59);
		midnightCalendar.set(Calendar.SECOND, 59);
		Date dayBegin = nowCalendar.getTime();
		Date midnightDate = midnightCalendar.getTime();
		String sql = "select record.deviceSiteId, SUM(method.ngCount) _sum from NGRECORD record inner join NGPROCESSMETHOD method "
				+ " on record.id=method.NGRECORD_ID where "
				+ " record.occurDate>=?0 and record.occurDate<?1  and method.processMethod!=?2"
				+ " and record.classesId=?3 group by record.deviceSiteId";
		@SuppressWarnings("unchecked")
		SQLQuery<Object[]> query = getSession().createNativeQuery(sql);
		query.setParameter(0, dayBegin);
		query.setParameter(1, midnightDate);
		query.setParameter(2, Constant.ProcessRecord.COMPROMISE);
		query.setParameter(3, classes.getId());*/
		return null;
	}
	@Override
	public Integer queryNgCountFromBeginOfMonthUntilTheDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		Date beginOfMonth = c.getTime();
		String sql = "select SUM(method.ngCount) from NGRECORD record inner join NGPROCESSMETHOD method "
				+ " on record.id=method.NGRECORD_ID inner join devicesite ds on record.devicesitecode=ds.code "
				+ "where ds.bottleneck=1 and record.occurDate between ?0 and ?1  and method.processMethod!=?2";
		Integer result = (Integer) getSession().createNativeQuery(sql)
				.setParameter(0, beginOfMonth)
				.setParameter(1, date)
				.setParameter(2, Constant.ProcessRecord.COMPROMISE)
				.uniqueResult();
		return result!=null?result:0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long queryNgCount4TheDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DATE);
		String hql = "select count(record.id) from NGRecord record "
				+ " where year(record.occurDate)=?0 and month(record.occurDate)=?1 and day(record.occurDate)=?2";
		return (Long) getHibernateTemplate().find(hql, new Object[] {year,month,day}).get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] queryNGRecordById(Long id) {
		String sql = "select record.id,sum(method.ngCount),record.workpiececode,inputdate,inputusername from ngrecord record inner join ngprocessmethod method "
				+ " on record.id=method.ngrecord_id where record.id=?0 and method.processMethod!=?1 and record.deleted=?2 group by record.id,record.workpiececode,record.inputdate,record.inputusername ";
		List<Object[]> result =getSession().createNativeQuery(sql)
				.setParameter(0, id)
				.setParameter(1, Constant.ProcessRecord.COMPROMISE)
				.setParameter(2,false)
				.list();
		if(result!=null&&result.size()>0) {
			return result.get(0);
		}
		return null;
	}
	 @SuppressWarnings("unchecked")
	@Override
	 public List<NGRecord> queryNgRecordByDeviceSiteId(Long deviceSiteId) {
		 String sql = "select ng.* from NGRECORD ng where ng.deviceSiteId=?0 and ng.deleted=?1";
		 return getSession().createNativeQuery(sql)
				 .setParameter(0, deviceSiteId)
				 .setParameter(1,false)
				 .addEntity(NGRecord.class)
				 .list();
	 }

	 @Override
	 public Integer queryNgCount4ClassFromClassBegin2now(Classes classes, Long deviceSiteId) {
		 //当前时间
		 Date now = new Date();
		 Calendar nowCalendar = Calendar.getInstance();
		 nowCalendar.setTime(now);
		 //班次开始时间
		 Calendar startCalendar = Calendar.getInstance();
		 startCalendar.setTime(classes.getStartTime());
		 startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		 startCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
		 startCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));
		 //班次结束时间
		 Calendar endCalendar = Calendar.getInstance();
		 endCalendar.setTime(classes.getEndTime());
		 endCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		 endCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
		 endCalendar.set(Calendar.DATE, nowCalendar.get(Calendar.DATE));

		 Date startDate = startCalendar.getTime();
		 String sql = "select SUM(method.ngCount) from NGRECORD record inner join NGPROCESSMETHOD method "
				 + " on record.id=method.NGRECORD_ID where record.deviceSiteId=?2 "
				 + "and record.occurDate>=?0 and record.occurDate<?1  and method.processMethod!=?3";

		 @SuppressWarnings("unchecked")
		 NativeQuery<Integer> query = getSession().createNativeQuery(sql);
		 //班次开始时间小于结束时间
		 if(startDate.getTime()<now.getTime()) {

		 }else {//开始时间》结束时间，说明跨天
			 startCalendar.add(Calendar.DATE, -1);
			 startDate = startCalendar.getTime();
		 }
		 query.setParameter(0, startDate);
		 query.setParameter(1, now);
		 query.setParameter(2, deviceSiteId);
		 query.setParameter(3, Constant.ProcessRecord.COMPROMISE);
		 Integer result = (Integer) query.uniqueResult();
		 return result!=null?result:0;
	 }
	/**
	 * 查询日期区间的不合格品数量
	 * @param from             开始时间
	 * @param to               结束时间
	 * @param productionIdList 生产单元id列表
	 * @return
	 */
	@Override
	public List<String[]> queryNgRecordGroupByCategory(Date from, Date to, List<Long> productionIdList) {
		String sql = "select case when category is null or category='' then '无类别' else category end category, isnull(sum(case when ngCount is null then 0 else ngCount end),0) _count " +
                " from ngRecord r inner join worksheet w on r.no = w.no " +
                " inner join ngReason reason on r.ngReasonId = reason.id " +
                " where occurDate between :fromDate and :toDate and w.productionUnitId in :productionUnitIdList"+
                " group by category";
		return getSession().createNativeQuery(sql)
                .setParameter("fromDate",from)
                .setParameter("toDate",to)
                .setParameterList("productionUnitIdList",productionIdList).list();
	}
    /**
     * 根据不合格品大类分组查找不合格品信息
     * @param monthDate        yyyy-MM格式的日期
     * @param productionIdList 生产单元id列表
     * @return
     */
    @Override
    public List<String[]> queryNgRecord4MonthGroupByCategory(Date monthDate, List<Long> productionIdList) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String sql = "select  case when category is null or category='' then '无类别' else category end category, isnull(sum(case when ngCount is null then 0 else ngCount end),0) _count " +
                " from ngRecord r inner join DeviceSite ds  on r.deviceSiteCode = ds.code " +
				" inner join device d on ds.device_id = d.id  " +
                " inner join ngReason reason on r.ngReasonId = reason.id " +
                " where year(occurDate)=:year and month(occurDate)=:month  and d.productionUnit_Id in :productionUnitIdList" +
                " group by category";

        return getSession().createNativeQuery(sql)
                .setParameter("year",year)
                .setParameter("month",month)
                .setParameterList("productionUnitIdList",productionIdList).list();
    }

	@Override
	public String queryMaxWarehouseNo() {
		Date now=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        return (String) getSession().createNativeQuery("select MAX(WarehouseNo) from NGRECORD where year(WarehouseDate)=?0" +
                " and month(WarehouseDate)=?1 and day(WarehouseDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
	}
}
