package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceSparepartRecordDao;
import com.digitzones.devmgr.model.DeviceSparepartRecord;
import com.digitzones.devmgr.service.IDeviceSparepartRecordService;
import com.digitzones.model.Pager;
@Service
public class DeviceSparepartRecordServiceImpl implements IDeviceSparepartRecordService {
	@Autowired
	private IDeviceSparepartRecordDao deviceSparepartRecordDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceSparepartRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceSparepartRecord obj) {
		deviceSparepartRecordDao.update(obj);
	}

	@Override
	public DeviceSparepartRecord queryByProperty(String name, String value) {
		return deviceSparepartRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceSparepartRecord obj) {
		return deviceSparepartRecordDao.save(obj);
	}

	@Override
	public DeviceSparepartRecord queryObjById(Serializable id) {
		return deviceSparepartRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceSparepartRecordDao.deleteById(id);
	}

	@Override
	public List<DeviceSparepartRecord> queryDeviceSparepartRecordByDeviceId(Long deviceId) {
		return deviceSparepartRecordDao.findListByProperty("deviceId", deviceId);
	}
}
