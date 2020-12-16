package com.digitzones.service.impl;

import com.digitzones.dao.IEquipmentRepairRecordDao;
import com.digitzones.model.EquipmentRepairRecord;
import com.digitzones.model.Pager;
import com.digitzones.service.IEquipmentRepairRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class EquipmentRepairRecordServiceImpl implements IEquipmentRepairRecordService {
	@Autowired
	private IEquipmentRepairRecordDao equipmentRepairRecordDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return equipmentRepairRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EquipmentRepairRecord obj) {
		equipmentRepairRecordDao.update(obj);
	}

	@Override
	public EquipmentRepairRecord queryByProperty(String name, String value) {
		return equipmentRepairRecordDao.findSingleByProperty(name,value);
	}

	@Override
	public Serializable addObj(EquipmentRepairRecord obj) {
		return equipmentRepairRecordDao.save(obj);
	}

	@Override
	public EquipmentRepairRecord queryObjById(Serializable id) {
		return equipmentRepairRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		equipmentRepairRecordDao.deleteById(id);
	}

	@Override
	public List<EquipmentRepairRecord> queryAllEquipmentRepairRecords() {
		return equipmentRepairRecordDao.findAll();
	}

	@Override
	public boolean deleteEquipmentRepairRecords(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			deleteObj(id);
		}
		return true;
	}
}
