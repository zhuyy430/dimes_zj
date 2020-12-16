package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceOperationalRecordDao;
import com.digitzones.devmgr.model.DeviceOperationalRecord;
import com.digitzones.devmgr.service.IDeviceOperationalRecordService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class DeviceOperationalRecordServiceImpl implements IDeviceOperationalRecordService {

	@Autowired
	IDeviceOperationalRecordDao deviceOperationalRecordDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceOperationalRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceOperationalRecord obj) {
		deviceOperationalRecordDao.update(obj);
	}

	@Override
	public DeviceOperationalRecord queryByProperty(String name, String value) {
		return deviceOperationalRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceOperationalRecord obj) {
		return deviceOperationalRecordDao.save(obj);
	}

	@Override
	public DeviceOperationalRecord queryObjById(Serializable id) {
		return deviceOperationalRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceOperationalRecordDao.deleteById(id);
	}

	@Override
	public Double queryAllRunTimeByDeviceId(Long deviceId) {
		return deviceOperationalRecordDao.queryAllObj(deviceId);
	}

	@Override
	public List<?> queryDeviceOperationalRecordForDeverId(Long deviceId,String classesCode, Date date) {
		return deviceOperationalRecordDao.queryDeviceOperationalRecordForDeverId(deviceId,classesCode,date);
	}
}
