package com.digitzones.devmgr.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceRepairOrderPicDao;
import com.digitzones.devmgr.model.DeviceRepairPic;
import com.digitzones.devmgr.service.IDeviceRepairOrderPicService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class DeviceRepairOrderPicServiceImpl implements IDeviceRepairOrderPicService {

	@Autowired
	IDeviceRepairOrderPicDao deviceRepairOrderPicDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceRepairOrderPicDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceRepairPic obj) {
		deviceRepairOrderPicDao.update(obj);
	}

	@Override
	public DeviceRepairPic queryByProperty(String name, String value) {
		return deviceRepairOrderPicDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceRepairPic obj) {
		return deviceRepairOrderPicDao.save(obj);
	}

	@Override
	public DeviceRepairPic queryObjById(Serializable id) {
		return deviceRepairOrderPicDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceRepairOrderPicDao.deleteById(id);
	}

	@Override
	public Serializable addOrderPic(DeviceRepairPic deviceRepairPic, File file) {
		return deviceRepairOrderPicDao.addOrderPic(deviceRepairPic, file);
	}

	@Override
	public List<DeviceRepairPic> queryListByProperty(Long deviceRepairId) {
		return deviceRepairOrderPicDao.findByTaskId(deviceRepairId);
	}
	
	@Override
	public Serializable addDeviceRepairOrderPic(Long orderId,File file) {
		return deviceRepairOrderPicDao.addDeviceRepairOrderPic(orderId,file);
	}
	@Override
	public List<DeviceRepairPic> queryListByProperty(String name, String value) {
		return deviceRepairOrderPicDao.findListByProperty(name, value);
	}
}
