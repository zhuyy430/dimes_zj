package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCLostTimeDao;
import com.digitzones.model.LostTimeRecord;

@Repository
public class MCLostTimeDaoImpl extends CommonDaoImpl<LostTimeRecord> implements IMCLostTimeDao{
	public MCLostTimeDaoImpl() {
		super(LostTimeRecord.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<LostTimeRecord> findLostTimelist(String hql) {
		// TODO Auto-generated method stub
	   return  (List<LostTimeRecord>) this.getHibernateTemplate().find(hql);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Double getAllLostTimelist(String untreated, String notSure, String historicalRecords, Long deviceSiteid) {
		String sql="select sum(ltr.sumOfLostTime)as sumtime from LostTimeRecord ltr where ltr.deleted=0 and ltr.deviceSite_id="+deviceSiteid;
		if(untreated.equals("untreated")) {
			sql+=" and (ltr.reason is null or ltr.reason='') ";
		}else if(notSure.equals("notSure")) {
			sql+=" and ltr.lostTimeTypeName is not null and ltr.lostTimeTypeName !='' and ltr.confirmUserId is null ";
		}else if(historicalRecords.equals("historicalRecords")){
			//查询两天内确认的历史记录
			//hql+= " and ltr.confirmTime >= '" + start +"' and ltr.confirmTime <= '"+end+"'"+" and ltr.confirmUserId is not null order by ltr.beginTime desc";
			sql+= " and  DATEDIFF(dd,ltr.confirmTime,getDate())<=2 and ltr.confirmUserId is not null ";
		}
		return (Double) getSession().createSQLQuery(sql).uniqueResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getCountLostTimelist(String untreated, String notSure, String historicalRecords, Long deviceSiteid) {
		String sql="select count(ltr.id) as count from LostTimeRecord ltr where ltr.deleted=0 and ltr.deviceSite_id="+deviceSiteid;
		if(untreated.equals("untreated")) {
			sql+=" and (ltr.reason is null or ltr.reason='') ";
		}else if(notSure.equals("notSure")) {
			sql+=" and ltr.lostTimeTypeName is not null and ltr.lostTimeTypeName !='' and ltr.confirmUserId is null ";
		}else if(historicalRecords.equals("historicalRecords")){
			//查询两天内确认的历史记录
			//hql+= " and ltr.confirmTime >= '" + start +"' and ltr.confirmTime <= '"+end+"'"+" and ltr.confirmUserId is not null order by ltr.beginTime desc";
			sql+= " and  DATEDIFF(dd,ltr.confirmTime,getDate())<=2 and ltr.confirmUserId is not null ";
		}
		return (int) getSession().createSQLQuery(sql).uniqueResult();
	}
}
