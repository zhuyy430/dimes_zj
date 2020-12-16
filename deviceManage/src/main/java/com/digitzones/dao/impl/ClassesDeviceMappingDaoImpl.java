package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IClassesDeviceMappingDao;
import com.digitzones.model.ClassesDeviceMapping;
@Repository
public class ClassesDeviceMappingDaoImpl extends CommonDaoImpl<ClassesDeviceMapping> implements IClassesDeviceMappingDao {

	public ClassesDeviceMappingDaoImpl() {
		super(ClassesDeviceMapping.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteByClassIdAndDeviceId(Long classesId, Long deviceId) {
		this.getSession().createSQLQuery("delete from CLASSES_DEVICE where classes_id=?0 and device_id=?1")
		.setParameter(0, classesId)
		.setParameter(1,deviceId)
		.executeUpdate();
	}
}
