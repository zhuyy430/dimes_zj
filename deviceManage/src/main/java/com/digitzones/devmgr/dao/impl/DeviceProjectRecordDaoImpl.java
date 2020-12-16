package com.digitzones.devmgr.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceProjectRecordDao;
import com.digitzones.devmgr.model.DeviceProjectRecord;
@Repository
public class DeviceProjectRecordDaoImpl extends CommonDaoImpl<DeviceProjectRecord> implements IDeviceProjectRecordDao{
	public DeviceProjectRecordDaoImpl() {
		super(DeviceProjectRecord.class);
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceIdAndtype(Long deviceId, String type) {
		String sql = "select dpr.* from DEVICEPROJECTRECORD dpr where dpr.device.id=?0 and dpr.type=?1 "
				+ " and dpr.id in(select max(d.id) DEVICEPROJECTRECORD d group by d.id)";
		List<DeviceProjectRecord> result = getSession().createNativeQuery(sql)
				.setParameter(0, deviceId)
				.setParameter(1, type)
				.addEntity(DeviceProjectRecord.class)
				.list();
		return result;
	}*/
}
