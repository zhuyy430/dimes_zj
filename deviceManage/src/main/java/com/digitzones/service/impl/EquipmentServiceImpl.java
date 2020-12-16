package com.digitzones.service.impl;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IEquipmentDao;
import com.digitzones.model.Equipment;
import com.digitzones.model.Pager;
import com.digitzones.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
@Service
public class EquipmentServiceImpl implements IEquipmentService {
	private IEquipmentDao equipmentDao;
	@Autowired
	public void setEquipmentDao(IEquipmentDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return equipmentDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Equipment obj) {
		equipmentDao.update(obj);
	}

	@Override
	public Equipment queryByProperty(String name, String value) {
		return equipmentDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Equipment obj) {
		return equipmentDao.save(obj);
	}

	@Override
	public Equipment queryObjById(Serializable id) {
		return equipmentDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		equipmentDao.deleteById(id);
	}

	@Override
	public List<Equipment> queryAllEquipments() {
		return equipmentDao.findByHQL("from Equipment e where e.baseCode=?0", new Object[] {Constant.EquipmentType.EQUIPMENT});
	}

	@Override
	public List<Equipment> queryEquipmentsByCodeOrNameOrUnity(String value) {
		String hql = "from Equipment e where (e.code like ?0 or e.equipmentType.unitType like ?0) and e.baseCode=?1";
		return equipmentDao.findByHQL(hql,new Object[] {"%" + value + "%",Constant.EquipmentType.EQUIPMENT});
	}
	@Override
	public List<Equipment> queryEquipmentsByCode(String code) {
		String hql = "from Equipment e where e.code = ?0 and e.baseCode=?1";
		return equipmentDao.findByHQL(hql,new Object[] {code,Constant.EquipmentType.EQUIPMENT});
	}

	@Override
	public Serializable addEquipment(Equipment equipment, File pic) {
		return equipmentDao.addEquipment(equipment, pic);
	}

	@Override
	public void updateEquipment(Equipment equipment, File pic) {
		equipmentDao.updateEquipment(equipment, pic);
	}

	@Override
	public boolean deleteEquipments(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			deleteObj(id);
		}
		return true;
	}

	@Override
	public void disableEquipments(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			Equipment d = queryObjById(Long.valueOf(id));
			updateObj(d);
		}
	}
	@Override
	public List<Equipment> queryAllEquipmentsByNotStopUse() {
		return equipmentDao.findByHQL("from Equipment e where e.baseCode=?0 and status='在库'", new Object[] {Constant.EquipmentType.EQUIPMENT});
	}
}
