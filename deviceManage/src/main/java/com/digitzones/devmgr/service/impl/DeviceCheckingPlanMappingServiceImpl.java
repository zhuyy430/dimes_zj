package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceCheckingPlanMappingDao;
import com.digitzones.devmgr.model.DeviceCheckingPlanMapping;
import com.digitzones.devmgr.service.IDeviceCheckingPlanMappingService;
import com.digitzones.model.Pager;
@Service
public class DeviceCheckingPlanMappingServiceImpl implements IDeviceCheckingPlanMappingService {
	@Autowired
	private IDeviceCheckingPlanMappingDao deviceCheckingPlanMappingDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceCheckingPlanMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceCheckingPlanMapping obj) {
		deviceCheckingPlanMappingDao.update(obj);
	}

	@Override
	public DeviceCheckingPlanMapping queryByProperty(String name, String value) {
		return deviceCheckingPlanMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceCheckingPlanMapping obj) {
		return deviceCheckingPlanMappingDao.save(obj);
	}

	@Override
	public DeviceCheckingPlanMapping queryObjById(Serializable id) {
		return deviceCheckingPlanMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceCheckingPlanMappingDao.deleteById(id);
	}
}
