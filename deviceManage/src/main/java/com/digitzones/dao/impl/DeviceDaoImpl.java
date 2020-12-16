package com.digitzones.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IDeviceDao;
import com.digitzones.model.Device;
@Repository
public class DeviceDaoImpl extends CommonDaoImpl<Device> implements IDeviceDao {

	public DeviceDaoImpl() {
		super(Device.class);
	}

	@Override
	public Serializable addDevice(Device device, File photo) {
		if(photo!=null && photo.exists()) {
			FileInputStream stream = null;
			try {
				stream = new FileInputStream(photo);
				device.setPhoto(Hibernate.getLobCreator(getSession()).createBlob(stream, stream.available()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return this.save(device);
	}
	@Override
	public void updateDevice(Device device, File photo) {
		if(photo!=null && photo.exists()) {
			FileInputStream stream = null;
			try {
				stream = new FileInputStream(photo);
				device.setPhoto(Hibernate.getLobCreator(getSession()).createBlob(stream, stream.available()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		this.update(device);
	}
	@Override
	public Integer queryDeviceSiteCountByDeviceId(Long deviceId) {
		String sql = "select count(ds.id) from deviceSite ds inner join device d on ds.device_id=d.id where d.id=?0";
		Integer count = (Integer) getSession().createNativeQuery(sql)
					.setParameter(0, deviceId)
					.getSingleResult();
		return count==null?0:count;
	}

	@Override
	public Integer queryClassesCountByDeviceId(Long deviceId) {
		String sql = "select count(id) from CLASSES_DEVICE  where device_id=?0";
		Integer count = (Integer) getSession().createNativeQuery(sql)
					.setParameter(0, deviceId)
					.getSingleResult();
		return count==null?0:count;
	}
}
