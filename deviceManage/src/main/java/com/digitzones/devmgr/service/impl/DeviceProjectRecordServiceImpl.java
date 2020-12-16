package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceProjectRecordDao;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.model.Pager;

@Service
public class DeviceProjectRecordServiceImpl implements IDeviceProjectRecordService{
	@Autowired
	private IDeviceProjectRecordDao deviceProjectRecordDao;
	@Override
	public Pager<DeviceProjectRecord> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceProjectRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceProjectRecord obj) {
		deviceProjectRecordDao.update(obj);
	}

	@Override
	public DeviceProjectRecord queryByProperty(String name, String value) {
		return deviceProjectRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceProjectRecord obj) {
		return deviceProjectRecordDao.save(obj);
	}

	@Override
	public DeviceProjectRecord queryObjById(Serializable id) {
		return deviceProjectRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceProjectRecordDao.deleteById(id);
	}

	@Override
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceId(Long deviceId) {
		return deviceProjectRecordDao.findByHQL("from DeviceProjectRecord record where record.device.id=?0", new Object[] {deviceId});
	}

	@Override
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceIdAndtype(Long deviceId, String type) {
		return deviceProjectRecordDao.findByHQL("from DeviceProjectRecord dpr where dpr.device.id=?0 and dpr.type=?1 "
				+ " and dpr.id in(select max(d.id) from DeviceProjectRecord d group by d.code)", new Object[] {deviceId,type});
	}
	@Override
	public List<DeviceProjectRecord> queryDeviceProjectRecordByDeviceIdAndtypeAndClassesCode(Long deviceId, String type,String classesCode) {
		return deviceProjectRecordDao.findByHQL("from DeviceProjectRecord dpr where dpr.device.id=?0 and dpr.type=?1 and dpr.classesCode=?2", new Object[] {deviceId,type,classesCode});
	}

}
