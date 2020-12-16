package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IMeasuringToolTypeDao;
import com.digitzones.model.EquipmentType;
import com.digitzones.model.Pager;
import com.digitzones.service.IMeasuringToolTypeService;
@Service
public class MeasuringToolTypeServiceImpl implements IMeasuringToolTypeService {
	private IMeasuringToolTypeDao equipmentTypeDao;
	@Autowired
	public void setEquipmentTypeDao(IMeasuringToolTypeDao equipmentTypeDao) {
		this.equipmentTypeDao = equipmentTypeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return equipmentTypeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EquipmentType obj) {
		equipmentTypeDao.update(obj);
	}

	@Override
	public EquipmentType queryByProperty(String name, String value) {
		return equipmentTypeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EquipmentType obj) {
		return equipmentTypeDao.save(obj);
	}

	@Override
	public EquipmentType queryObjById(Serializable id) {
		return equipmentTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		equipmentTypeDao.deleteById(id);
	}

	@Override
	public List<EquipmentType> queryTopEquipmentType() {
		return  equipmentTypeDao.findByHQL("from EquipmentType p where p.parent is null and p.code=?0", new Object[] {Constant.EquipmentType.MEASURINGTOOL});
	}

	@Override
	public Long queryCountOfSubEquipmentType(Serializable pid) {
		return equipmentTypeDao.findCount("from EquipmentType d inner join d.parent p where p.id=?0", new Object[] {pid});
	}

	@Override
	public Long queryCountOfEquipment(Serializable typeId) {
		return  equipmentTypeDao.findCount("from Equipment d inner join d.equipmentType p where p.id=?0", new Object[] {typeId});
	}

}
