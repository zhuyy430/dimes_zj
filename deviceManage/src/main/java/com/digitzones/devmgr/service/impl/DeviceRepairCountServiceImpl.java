package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceRepairCountDao;
import com.digitzones.devmgr.model.DeviceRepairCount;
import com.digitzones.devmgr.service.IDeviceRepairCountService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class DeviceRepairCountServiceImpl implements IDeviceRepairCountService {

	@Autowired
	IDeviceRepairCountDao deviceRepairCountDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceRepairCountDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceRepairCount obj) {
		deviceRepairCountDao.update(obj);
	}

	@Override
	public DeviceRepairCount queryByProperty(String name, String value) {
		return deviceRepairCountDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceRepairCount obj) {
		return deviceRepairCountDao.save(obj);
	}

	@Override
	public DeviceRepairCount queryObjById(Serializable id) {
		return deviceRepairCountDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceRepairCountDao.deleteById(id);
	}

	@Override
	public DeviceRepairCount queryDeviceRepairCount() {
		List<DeviceRepairCount> dlist = deviceRepairCountDao.findDeviceRepairCount();
		if(!dlist.isEmpty()&&dlist.size()==1){
			return dlist.get(0);
		}
		return null;
	}

	@Override
	public DeviceRepairCount queryLastDeviceRepairCount() {
		List<DeviceRepairCount> dlist = deviceRepairCountDao.findLastDeviceRepairCount();
		if(!dlist.isEmpty()&&dlist.size()==1){
			return dlist.get(0);
		}
		return null;
	}

}
