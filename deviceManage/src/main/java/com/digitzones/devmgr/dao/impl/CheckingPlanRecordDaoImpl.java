package com.digitzones.devmgr.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ICheckingPlanRecordDao;
import com.digitzones.devmgr.model.CheckingPlanRecord;
@Repository
public class CheckingPlanRecordDaoImpl extends CommonDaoImpl<CheckingPlanRecord> implements ICheckingPlanRecordDao {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public CheckingPlanRecordDaoImpl() {
		super(CheckingPlanRecord.class);
	}

	@Override
	public void updateStatus2Uncomplete() {
		String sql = "update checkingPlanRecord set status=?0 where checkedDate is null "
				+ " and checkingDate<?1 and status=?2";
		getSession().createNativeQuery(sql)
		.setParameter(0, Constant.Status.CHECKINGPLAN_UNCOMPLETE)
		.setParameter(1, new Date())
		.setParameter(2, Constant.Status.CHECKINGPLAN_PLAN).executeUpdate();
	}
	//0:id 1:设备代码 2：点检日期  3：点检状态 4：班次代码  5：班次名称 6:设备名称
	@SuppressWarnings("unchecked")
	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndMonth(String deviceCode, int year, int month) {
		String sql = "select * from "
				+ " CheckingPlanRecord record where year(checkingDate)=?0 and month(checkingDate)=?1 and deviceCode=?2";
		return getSession().createNativeQuery(sql)
				.addEntity(CheckingPlanRecord.class)
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, deviceCode)
				.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryNotSpotcheckDeviceBytime(String startTime, String endTime) {
		String sql="select c.deviceCode,c.deviceName,p.code,p.name from CheckingPlanRecord c inner join DEVICE d "
				+ "on c.deviceCode=d.code inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id where c.status='未完成' and d.isDeviceManageUse=1 ";
		if(startTime!=null && !"".equals(startTime)) {
			sql+=" and c.checkingDate >=:beginTime ";
		}

		if(endTime!=null && !"".equals(endTime)) {
			sql+=" and c.checkingDate <=:endTime ";
		}

		sql+=" group by c.deviceCode,c.deviceName,p.name,p.code ";
		NativeQuery<Object[]> dataQueryObj = getSession().createNativeQuery(sql);
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

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Object[]> queryNotSpotcheckRecordBytime(String startTime, String endTime) {
		String sql="select c.deviceCode,c.deviceName,p.code,p.name,c.checkingDate,c.className from CheckingPlanRecord c inner join DEVICE d "
				+ "on c.deviceCode=d.code inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID=p.id where c.status='未完成' and d.isDeviceManageUse=1 "; 
		if(startTime!=null && !"".equals(startTime)) {
			sql+=" and c.checkingDate >=:beginTime ";
		}

		if(endTime!=null && !"".equals(endTime)) {
			sql+=" and c.checkingDate <=:endTime ";
		}

		sql+="  group by c.deviceCode,c.deviceName,p.name,p.code,c.checkingDate,c.className";
		NativeQuery<Object[]> dataQueryObj = getSession().createNativeQuery(sql);
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

	@Override
	public String queryMaxNoByDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		return (String) getSession().createNativeQuery("select max(no) from CheckingPlanRecord record "
				+ " where year(checkingDate)=?0 and month(checkingDate)=?1 and day(checkingDate)=?2")
				.setParameter(0, year)
				.setParameter(1, month)
				.setParameter(2, day)
				.getSingleResult();
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
				+ " sum(case when checkedDate>checkingDate then 1 else 0 end) \"逾期次数\","
				+ " sum(case when (cpr.status!='已完成' and getDate()>checkingDate) then 1 else 0 end) \"逾期未完成\""
				+ " from CheckingPlanRecord cpr inner join Device d on cpr.deviceCode=d.code inner join productionUnit pu on d.PRODUCTIONUNIT_ID=pu.id " +
				" where CONVERT(varchar(20),checkingDate,23) between '" + from + "' and '" + to + "' group by pu.name,"  + groupByCycle;
		return getSession().createNativeQuery(sql).list();
	}

	/**
	 * 获取查询周期分组字符串
	 * @param cycle
	 * @return
	 */
	private String getGroupByCycle(String cycle) {
		switch(cycle) {
		case "年":return "year(checkingDate)";
		case "月":return "substring(convert(varchar(20),checkingDate,23),1,7)";
		case "周":return "cast(year(checkingDate) as varchar)+'-' + cast(datepart(week,checkingDate) as varchar)";
		case "日":return "CONVERT(varchar(20),checkingDate,23)";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> queryOverviewData(String from, String to) {
		String sql = "select pu.name,count(1) \"计划次数\","
				+ " sum(case when cpr.status='已完成' then 1 else 0 end) \"完成次数\","
				+ " sum(case when checkedDate>checkingDate then 1 else 0 end) \"逾期次数\","
				+ " sum(case when (cpr.status!='已完成' and getDate()>checkingDate) then 1 else 0 end) \"逾期未完成\""
				+ " from CheckingPlanRecord cpr inner join Device d on cpr.deviceCode=d.code inner join productionUnit pu on d.PRODUCTIONUNIT_ID=pu.id " +
				" where CONVERT(varchar(20),checkingDate,23) between '" + from + "' and '" + to + "' group by pu.name";
		return getSession().createNativeQuery(sql).list();
	}
}
