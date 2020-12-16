package com.digitzones.dao.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IPressLightRecordDao;
import com.digitzones.model.PressLightRecord;
@Repository
public class PressLightRecordDaoImpl extends CommonDaoImpl<PressLightRecord> implements IPressLightRecordDao {
	public PressLightRecordDaoImpl() {
		super(PressLightRecord.class);
	}
	@SuppressWarnings("deprecation")
	@Override
	public Long queryCountByPressLightTime(Date pressLightTime) {
		Calendar c = Calendar.getInstance();
		c.setTime(pressLightTime);
		String hql = "select count(*) from PressLightRecord plr where year(plr.pressLightTime)=?0 and month(plr.pressLightTime)=?1 and day(plr.pressLightTime)=?2";
		return (Long) this.getHibernateTemplate().find(hql, new Object[] {c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE)}).get(0);
	}
}
