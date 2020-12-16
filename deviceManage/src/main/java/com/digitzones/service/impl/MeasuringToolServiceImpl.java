package com.digitzones.service.impl;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IMeasuringToolDao;
import com.digitzones.model.Equipment;
import com.digitzones.model.Pager;
import com.digitzones.service.IMeasuringToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
@Service
public class MeasuringToolServiceImpl implements IMeasuringToolService {
	private IMeasuringToolDao equipmentDao;
	@Autowired
	public void setEquipmentDao(IMeasuringToolDao equipmentDao) {
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
	public List<Equipment> queryAllMeasuringTools() {
		return equipmentDao.findByHQL("from Equipment e where e.baseCode=?0", new Object[] {Constant.EquipmentType.MEASURINGTOOL});
	}

	@Override
	public List<Equipment> queryMeasuringToolsByCodeOrNameOrUnity(String value) {
		String hql = "from Equipment e where (e.code like ?0 or e.equipmentType.unitType like ?0) and e.baseCode=?1";
		return equipmentDao.findByHQL(hql,new Object[] {"%" + value + "%",Constant.EquipmentType.MEASURINGTOOL});
	}

	@Override
	public List<Equipment> queryMeasuringToolsByCode(String code) {
		String hql = "from Equipment e where e.code = ?0 and e.baseCode=?1";
		return equipmentDao.findByHQL(hql,new Object[] {code,Constant.EquipmentType.MEASURINGTOOL});
	}

	@Override
	public Serializable addMeasuringTool(Equipment equipment, File pic) {
		return equipmentDao.addMeasuringTool(equipment, pic);
	}

	@Override
	public void updateMeasuringTool(Equipment equipment, File pic) {
		equipmentDao.updateMeasuringTool(equipment, pic);
	}

	@Override
	public void deleteMeasuringTools(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			deleteObj(id);
		}
	}

	@Override
	public void disableMeasuringTools(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			Equipment d = queryObjById(Long.valueOf(id));
			/*if(d.getDisabled()!=null) {
				d.setDisabled(!d.getDisabled());
			}*/
			updateObj(d);
		}
	}
	@Override
	public List<Equipment> queryAllMeasuringToolsByNotStopUse() {
		return equipmentDao.findByHQL("from Equipment e where e.baseCode=?0 and disabled=0", new Object[] {Constant.EquipmentType.MEASURINGTOOL});
	}
}
