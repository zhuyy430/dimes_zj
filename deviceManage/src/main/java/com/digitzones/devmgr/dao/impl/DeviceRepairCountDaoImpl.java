package com.digitzones.devmgr.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceRepairCountDao;
import com.digitzones.devmgr.model.DeviceRepairCount;
@Repository
public class DeviceRepairCountDaoImpl extends CommonDaoImpl<DeviceRepairCount> implements IDeviceRepairCountDao {
	public DeviceRepairCountDaoImpl() {
		super(DeviceRepairCount.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<DeviceRepairCount> findDeviceRepairCount() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		String sql = "select * from DeviceRepairCount d where year(d.createTime)=?0 and month(d.createTime)=?1"
				+ " and day(d.createTime)=?2";
		return getSession().createSQLQuery(sql)	.setParameter(0, calendar.get(Calendar.YEAR))
												.setParameter(1, calendar.get(Calendar.MONTH)+1)
												.setParameter(2, calendar.get(Calendar.DATE))
				.addEntity(DeviceRepairCount.class).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<DeviceRepairCount> findLastDeviceRepairCount() {
		// TODO Auto-generated method stub
		String sql = "select * from DeviceRepairCount d order by d.id desc";
		return getSession().createSQLQuery(sql).addEntity(DeviceRepairCount.class).list();
	}
}
