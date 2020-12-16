package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IEquipmentDeviceSiteMappingLogDao;
import com.digitzones.model.EquipmentDeviceSiteMappingLog;
import com.digitzones.model.Pager;
import com.digitzones.service.IEquipmentDeviceSiteMappingLogService;
@Service
public class EquipmentDeviceSiteMappingLogServiceImpl implements IEquipmentDeviceSiteMappingLogService {
	private IEquipmentDeviceSiteMappingLogDao equipmentDeviceSiteMappingLogDao;
	@Autowired
	public void setEquipmentDeviceSiteMappingLogDao(IEquipmentDeviceSiteMappingLogDao equipmentDeviceSiteMappingLogDao) {
		this.equipmentDeviceSiteMappingLogDao = equipmentDeviceSiteMappingLogDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return equipmentDeviceSiteMappingLogDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EquipmentDeviceSiteMappingLog obj) {
		equipmentDeviceSiteMappingLogDao.update(obj);
	}

	@Override
	public EquipmentDeviceSiteMappingLog queryByProperty(String name, String value) {
		return equipmentDeviceSiteMappingLogDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EquipmentDeviceSiteMappingLog obj) {
		return equipmentDeviceSiteMappingLogDao.save(obj);
	}

	@Override
	public EquipmentDeviceSiteMappingLog queryObjById(Serializable id) {
		return equipmentDeviceSiteMappingLogDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		equipmentDeviceSiteMappingLogDao.deleteById(id);
	}

}
