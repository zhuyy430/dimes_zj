package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IClassesDeviceMappingDao;
import com.digitzones.model.ClassesDeviceMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IClassesDeviceMappingService;
@Service
public class ClassesDeviceMappingServiceImpl implements IClassesDeviceMappingService {
	private IClassesDeviceMappingDao classesDeviceMappingDao;
	@Autowired
	public void setClassesDeviceMappingDao(IClassesDeviceMappingDao classesDeviceMappingDao) {
		this.classesDeviceMappingDao = classesDeviceMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return classesDeviceMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ClassesDeviceMapping obj) {
		classesDeviceMappingDao.update(obj);
	}

	@Override
	public ClassesDeviceMapping queryByProperty(String name, String value) {
		return classesDeviceMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ClassesDeviceMapping obj) {
		return classesDeviceMappingDao.save(obj);
	}

	@Override
	public ClassesDeviceMapping queryObjById(Serializable id) {
		return classesDeviceMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		classesDeviceMappingDao.deleteById(id);
	}

	@Override
	public void deleteByClassesIdAndDeviceId(Long classesId, Long deviceId) {
		classesDeviceMappingDao.deleteByClassIdAndDeviceId(classesId, deviceId);
	}
}
