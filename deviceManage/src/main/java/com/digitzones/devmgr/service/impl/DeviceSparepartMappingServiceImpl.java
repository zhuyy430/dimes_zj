package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IDeviceDao;
import com.digitzones.devmgr.dao.IDeviceSparepartMappingDao;
import com.digitzones.devmgr.dao.ISparepartDao;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.IDeviceSparepartMappingService;
import com.digitzones.model.Device;
import com.digitzones.model.Pager;
@Service
public class DeviceSparepartMappingServiceImpl implements IDeviceSparepartMappingService {
	@Autowired
	private IDeviceSparepartMappingDao deviceSparepartMappingDao;
	@Autowired
	private IDeviceDao deviceDao;
	@Autowired
	private ISparepartDao sparepartDao;
	@Override
	public Pager<DeviceSparepartMapping> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceSparepartMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceSparepartMapping obj) {
		deviceSparepartMappingDao.update(obj);
	}

	@Override
	public DeviceSparepartMapping queryByProperty(String name, String value) {
		return deviceSparepartMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceSparepartMapping obj) {
		return deviceSparepartMappingDao.save(obj);
	}

	@Override
	public DeviceSparepartMapping queryObjById(Serializable id) {
		return deviceSparepartMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceSparepartMappingDao.deleteById(id);
	}

	@Override
	public void addDevices4Sparepart(Sparepart sparepart, String[] deviceIds) {
		for(int i = 0;i<deviceIds.length;i++) {
			Device device = deviceDao.findById(Long.valueOf(deviceIds[i]));
			
			DeviceSparepartMapping mapping = new DeviceSparepartMapping();
			mapping.setDevice(device);
			mapping.setSparepart(sparepart);
			deviceSparepartMappingDao.save(mapping);
		}
	}

	@Override
	public void addSpareparts4Device(Device device, String[] sparepartIds) {
		for(int i = 0;i<sparepartIds.length;i++) {
			Sparepart sparepart = sparepartDao.findById(Long.valueOf(sparepartIds[i]));
			DeviceSparepartMapping mapping = new DeviceSparepartMapping();
			mapping.setDevice(device);
			mapping.setSparepart(sparepart);
			deviceSparepartMappingDao.save(mapping);
		}
	}
	
	@Override
	public List<DeviceSparepartMapping> queryDeviceSparepartMappingByDeviceId(Long deviceId) {
		return deviceSparepartMappingDao.findByHQL("from DeviceSparepartMapping d where d.device.id=?0"
				+ " and d.device.isDeviceManageUse=?1", new Object[] {deviceId,true});
	}
	@Override
	public DeviceSparepartMapping queryBySparePartIDAndDeviceId(Long sparepartId, Long deviceId) {
		String hql="from DeviceSparepartMapping ds where ds.device.id=?0 and ds.sparepart.id=?1 and ds.device.isDeviceManageUse=?2";
		List<DeviceSparepartMapping> list = deviceSparepartMappingDao.findByHQL(hql, new Object[] {deviceId,sparepartId,true});
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}
