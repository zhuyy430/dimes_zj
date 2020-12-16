package com.digitzones.devmgr.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenancePlanRecordDao;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
@Repository
public class MaintenancePlanRecordDaoImpl extends CommonDaoImpl<MaintenancePlanRecord>
		implements IMaintenancePlanRecordDao {

	public MaintenancePlanRecordDaoImpl() {
		super(MaintenancePlanRecord.class);
	}
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public void updateStatus2Uncomplete() {
		String sql = "update MaintenancePlanRecord set status=?0 where maintenancedDate is null "
				+ " and maintenanceDate<?1 and status=?2";
		getSession().createNativeQuery(sql)
		.setParameter(0, Constant.Status.MAINTENANCEPLAN_UNCOMPLETE)
		.setParameter(1, new Date())
		.setParameter(2, Constant.Status.MAINTENANCEPLAN_PLAN).executeUpdate();		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByDeviceCodeAndMonth(String deviceCode, int year,
			int month) {
		String sql = "select * from "
				+ " MaintenancePlanRecord record where year(maintenanceDate)=?0 and month(maintenanceDate)=?1 and deviceCode=?2";
		return getSession().createNativeQuery(sql)
				.addEntity(CheckingPlanRecord.class)
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, deviceCode)
				.list();
	}
	@Override
	public void update4AssignBatchPersonInCharge(Map<String, Object> params) {
		String sql = "update MaintenancePlanRecord set employeeCode=?0,employeeName=?1 "
				+ " where maintenanceDate between ?2 and ?3 and maintenanceType=?4 and deviceCode=?5";
		getSession().createNativeQuery(sql)
					.setParameter(0, params.get("employeeCode"))
					.setParameter(1, params.get("employeeName"))
					.setParameter(2, params.get("from"))
					.setParameter(3, params.get("to"))
					.setParameter(4, params.get("maintenanceType"))
					.setParameter(5, params.get("deviceCode"))
					.executeUpdate();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(Long deviceId, int year,
			int month, int day, String name,String classCode) {
		String sql = "select * from MaintenancePlanRecord m where EXISTS  (select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.name=?0) and m.DEVICE_ID=?1 and  year(m.maintenanceDate)=?2 and month(m.maintenanceDate)=?3 and day(m.maintenanceDate)=?4 and (m.classCode is null or m.classCode=?5)";
		return getSession().createNativeQuery(sql)
				.addEntity(MaintenancePlanRecord.class)
				.setParameter(0, name)
				.setParameter(1, deviceId)
				.setParameter(2, year)
				.setParameter(3, month)
				.setParameter(4, day)
				.setParameter(5, classCode)
				.list();
	}
	@Override
	public String queryMaxNoByDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		return (String) getSession().createNativeQuery("select max(no) from MaintenancePlanRecord record "
				+ " where year(maintenanceDate)=?0 and month(maintenanceDate)=?1 and day(maintenanceDate)=?2")
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, day)
				.getSingleResult();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Object[]> queryAllMaintenancePlanRecordmaintianDateByToday(String name,Long deviceId) {
		String sql="select m.maintenanceDate from MaintenancePlanRecord m where EXISTS  (select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.name=:name) and m.maintenanceDate<=:time and m.DEVICE_ID=:deviceId group by m.maintenanceDate";
		NativeQuery dataQueryObj = getSession().createNativeQuery(sql);
		dataQueryObj.setParameter("name", name);
	    dataQueryObj.setParameter("time", new Date());
	    dataQueryObj.setParameter("deviceId", deviceId);
		List<Object[]> list=dataQueryObj.list();
		return list;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Object[]> queryNotMaintenanceDeviceBytime(String startTime, String endTime) {
		String sql="select d.code as deviceCode,d.name as deviceName,p.code,p.name from MaintenancePlanRecord c inner join DEVICE d on c.DEVICE_ID=d.id inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id where c.status='未完成' and d.isDeviceManageUse=1";
		 if(startTime!=null && !"".equals(startTime)) {
	         sql+=" and c.maintenanceDate >=:beginTime ";
	      }

	      if(endTime!=null && !"".equals(endTime)) {
	         sql+=" and c.maintenanceDate <=:endTime ";
	      }
		
	      sql+=" group by d.code,d.name,p.name,p.code ";
	      NativeQuery dataQueryObj = getSession().createNativeQuery(sql);
	      if(startTime!=null && !"".equals(startTime)) {
		         try {
		            System.out.println(format.parse(startTime));
		            System.out.println(format.parse(startTime));
		            dataQueryObj.setParameter("beginTime", format.parse(startTime));
		         } catch (ParseException e) {
		            e.printStackTrace();
		         }
		      }
	      if(endTime!=null && !"".equals(endTime)) {
	         try {
	            dataQueryObj.setParameter("endTime", format.parse(endTime));
	         } catch (ParseException e) {
	            e.printStackTrace();
	         }
	      }
		     
		List<Object[]> list=dataQueryObj.list();
		return list;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Object[]> queryNotMaintenanceRecordBytime(String startTime, String endTime) {
		String sql="select d.code as deviceCode,d.name as deviceName,p.code,p.name,c.maintenanceDate,c.classCode from MaintenancePlanRecord c inner join DEVICE d on c.DEVICE_ID=d.id inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id where c.status='未完成' and d.isDeviceManageUse=1"; 
		 if(startTime!=null && !"".equals(startTime)) {
	         sql+=" and c.maintenanceDate >=:beginTime ";
	      }

	      if(endTime!=null && !"".equals(endTime)) {
	         sql+=" and c.maintenanceDate <=:endTime ";
	      }
		
	      sql+="  group by d.code,d.name,p.name,p.code,c.maintenanceDate,c.classCode";
	      NativeQuery dataQueryObj = getSession().createNativeQuery(sql);
	      if(startTime!=null && !"".equals(startTime)) {
		         try {
		            System.out.println(format.parse(startTime));
		            System.out.println(format.parse(startTime));
		            dataQueryObj.setParameter("beginTime", format.parse(startTime));
		         } catch (ParseException e) {
		            e.printStackTrace();
		         }
		      }
		      if(endTime!=null && !"".equals(endTime)) {
		         try {
		            dataQueryObj.setParameter("endTime", format.parse(endTime));
		         } catch (ParseException e) {
		            e.printStackTrace();
		         }
		      }
		List<Object[]> list=dataQueryObj.list();
		return list;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatus(String status, String user) {
		String sql="select * from MaintenancePlanRecord m where 1=1 "; 
		if(!user.equals("")&&user!=null) {
			sql+=" and exists(select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.name=:user) ";
		}
		if(!status.equals("")&&status!=null) {
			sql+=" and m.status=:status";
		}
	      NativeQuery dataQueryObj = getSession().createNativeQuery(sql);
	      if(!user.equals("")&&user!=null) {
		        dataQueryObj.setParameter("user", user);
		      
		   }
	      if(!status.equals("")&&status!=null) {
	            dataQueryObj.setParameter("status", status);
	      }
		return dataQueryObj.addEntity(MaintenancePlanRecord.class).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployCodeAll(Long deviceId, int year,
			int month, int day, String usercode) {
		String sql = "select * from MaintenancePlanRecord m where EXISTS  (select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.code=?0) and m.DEVICE_ID=?1 and  year(m.maintenanceDate)=?2 and month(m.maintenanceDate)=?3 and day(m.maintenanceDate)=?4 and m.status='保养中'";
		return getSession().createNativeQuery(sql)
				.addEntity(MaintenancePlanRecord.class)
				.setParameter(0, usercode)
				.setParameter(1, deviceId)
				.setParameter(2, year)
				.setParameter(3, month)
				.setParameter(4, day)
				.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenancePlanRecord> queryReceiptMPRWithUser(String usercode) {
		String sql = "select * from MaintenancePlanRecord m where exists(select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.code=?0 and u.receiptDate is null and u.completeDate is null) and m.status!='待确认' order by m.maintenanceDate asc";
		return getSession().createNativeQuery(sql).setParameter(0, usercode)
				.addEntity(MaintenancePlanRecord.class).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenancePlanRecord> queryMaintenanceMPRWithUser(String usercode) {
		String sql = "select * from MaintenancePlanRecord m where exists(select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.code=?0 and u.receiptDate is not null and u.completeDate is null) and m.status!='待确认' order by m.maintenanceDate desc";
		return getSession().createNativeQuery(sql).setParameter(0, usercode)
				.addEntity(MaintenancePlanRecord.class).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> queryStatisticsData(String from, String to, String cycle) {
		//获取周期表达式
		String groupByCycle = getGroupByCycle(cycle);
		if( StringUtils.isEmpty(groupByCycle)) {
			return new ArrayList<String[]>();
		}
		String sql = "select pu.name," +  groupByCycle + ",count(1) \"计划次数\","
				+ " sum(case when cpr.status='已完成' then 1 else 0 end) \"完成次数\","
				+ " sum(case when maintenancedDate>maintenanceDate then 1 else 0 end) \"逾期次数\","
				+ " sum(case when (cpr.status!='已完成' and getDate()>maintenanceDate) then 1 else 0 end) \"逾期未完成\""
				+ " from MaintenancePlanRecord cpr inner join Device d on cpr.device_id=d.id inner join productionUnit pu on d.PRODUCTIONUNIT_ID=pu.id " +
				" where CONVERT(varchar(20),maintenanceDate,23) between '" + from + "' and '" + to + "' group by pu.name,"  + groupByCycle;
		return getSession().createNativeQuery(sql).list();
	}

	/**
	 * 获取查询周期分组字符串
	 * @param cycle
	 * @return
	 */
	private String getGroupByCycle(String cycle) {
		switch(cycle) {
		case "年":return "year(maintenanceDate)";
		case "月":return "substring(convert(varchar(20),maintenanceDate,23),1,7)";
		case "周":return "cast(year(maintenanceDate) as varchar)+'-' + cast(datepart(week,maintenanceDate) as varchar)";
		case "日":return "CONVERT(varchar(20),maintenanceDate,23)";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> queryOverviewData(String from, String to) {
		String sql = "select pu.name,count(1) \"计划次数\","
				+ " sum(case when cpr.status='已完成' then 1 else 0 end) \"完成次数\","
				+ " sum(case when maintenancedDate>maintenanceDate then 1 else 0 end) \"逾期次数\","
				+ " sum(case when (cpr.status!='已完成' and getDate()>maintenanceDate) then 1 else 0 end) \"逾期未完成\""
				+ " from MaintenancePlanRecord cpr inner join Device d on cpr.device_id=d.id inner join productionUnit pu on d.PRODUCTIONUNIT_ID=pu.id " +
				" where CONVERT(varchar(20),maintenanceDate,23) between '" + from + "' and '" + to + "' group by pu.name";
		return getSession().createNativeQuery(sql).list();
	}
}
