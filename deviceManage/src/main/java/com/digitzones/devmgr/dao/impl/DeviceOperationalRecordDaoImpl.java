package com.digitzones.devmgr.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceOperationalRecordDao;
import com.digitzones.devmgr.model.DeviceOperationalRecord;
@Repository
public class DeviceOperationalRecordDaoImpl extends CommonDaoImpl<DeviceOperationalRecord> implements IDeviceOperationalRecordDao {
	public DeviceOperationalRecordDaoImpl() {
		super(DeviceOperationalRecord.class);
	}
	@Override
	public Double queryAllObj(Long deviceId) {
		String sql = "select sum(d.runTime) as runtimes from DEVICEOPERATIONALRECORD d where d.DEVICE_ID=?0";
		Double result = (Double) getSession().createNativeQuery(sql).setParameter(0, deviceId)
				.uniqueResult();
		return result==null?0:result.doubleValue();
	}
	@Override
	public List<?> queryDeviceOperationalRecordForDeverId(Long deviceId, String classesCode,Date date) {
		Calendar calendar = Calendar.getInstance();
		if(date==null){
			date = new Date();
		}
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		
		Date begin = calendar.getTime();
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(begin);
		calendar2.add(Calendar.MONTH,1);
		calendar2.add(Calendar.DATE,-1);
		
		Date end = calendar2.getTime();
		String sql = "select convert(varchar(10),date,120) as data,SUM(runTime) as val from DEVICEOPERATIONALRECORD dor "
				+ " left join DEVICE d on dor.DEVICE_ID=d.id left join CLASSES c on dor.CLASSES_ID=c.id "
				+ " where dor.date between ?2 and ?3 and c.code =?0 and d.id=?1  and d.isDeviceManageUse=1"
				+ " group by convert(varchar(10),date,120)";
		 List<?> result = getSession().createNativeQuery(sql).setParameter(0, classesCode)
				.setParameter(1, deviceId)
				.setParameter(2, begin)
				.setParameter(3, end)
				.list();
		return result;
	}
}
