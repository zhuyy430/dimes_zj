package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceLevelDao;
import com.digitzones.devmgr.model.DeviceLevel;
import com.digitzones.devmgr.service.IDeviceLevelService;
import com.digitzones.model.Pager;
@Service
public class DeviceLevelServiceImpl implements IDeviceLevelService {
	@Autowired
	private IDeviceLevelDao deviceLevelDao;
	@Override
	public Pager<DeviceLevel> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceLevelDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceLevel obj) {
		deviceLevelDao.update(obj);
	}

	@Override
	public DeviceLevel queryByProperty(String name, String value) {
		return deviceLevelDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceLevel obj) {
		return deviceLevelDao.save(obj);
	}

	@Override
	public DeviceLevel queryObjById(Serializable id) {
		return deviceLevelDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceLevelDao.deleteById(id);
	}

	@Override
	public List<DeviceLevel> queryTopDeviceLevels() {
		return  deviceLevelDao.findByHQL("from DeviceLevel pu where pu.parent is null order by pu.code", new Object[] {});
	}

	@Override
	public List<DeviceLevel> queryAllDeviceLevels() {
		return this.deviceLevelDao.findByHQL("from DeviceLevel pu where  pu.parent is not null and pu not in "
				+ "(select p.parent from DeviceLevel p where p.parent is not null)", new Object[] {});
	}
}
