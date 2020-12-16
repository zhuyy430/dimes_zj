package com.digitzones.service.impl;

import com.digitzones.dao.IMeasuringToolDeviceSiteMappingDao;
import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IMeasuringToolDeviceSiteMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
@Service
public class MeasuringToolDeviceSiteMappingServiceImpl implements IMeasuringToolDeviceSiteMappingService {
	private IMeasuringToolDeviceSiteMappingDao equipmentDeviceSiteMappingDao;
	@Autowired
	public void setMeasuringToolDeviceSiteMappingDao(IMeasuringToolDeviceSiteMappingDao equipmentDeviceSiteMappingDao) {
		this.equipmentDeviceSiteMappingDao = equipmentDeviceSiteMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return equipmentDeviceSiteMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EquipmentDeviceSiteMapping obj) {
		equipmentDeviceSiteMappingDao.update(obj);
	}

	@Override
	public EquipmentDeviceSiteMapping queryByProperty(String name, String value) {
		return equipmentDeviceSiteMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EquipmentDeviceSiteMapping obj) {
		return equipmentDeviceSiteMappingDao.save(obj);
	}

	@Override
	public EquipmentDeviceSiteMapping queryObjById(Serializable id) {
		return equipmentDeviceSiteMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		equipmentDeviceSiteMappingDao.deleteById(id);
	}

	@Override
	public EquipmentDeviceSiteMapping queryByNo(String no) {
		List<EquipmentDeviceSiteMapping> list = equipmentDeviceSiteMappingDao.findByHQL("from EquipmentDeviceSiteMapping edsm where edsm.no=?0 and edsm.unbind=?1", new Object[] {no,false});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public EquipmentDeviceSiteMapping queryByEquipmentCode(String code) {
		List<EquipmentDeviceSiteMapping> list = equipmentDeviceSiteMappingDao.findByHQL("from EquipmentDeviceSiteMapping edsm where edsm.equipment.code=?0 and edsm.unbind=?1", new Object[] {code,false});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<EquipmentDeviceSiteMapping> queryByDeviceSiteCode(String deviceSiteCode,String searchText) {
		List<EquipmentDeviceSiteMapping> equList = equipmentDeviceSiteMappingDao.queryByDeviceSiteCode(deviceSiteCode,searchText);
		return equList;
	}

	@Override
	public List<EquipmentDeviceSiteMapping> repalce(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping) {
		this.deleteObj(equipmentDeviceSiteMapping.getId());
		equipmentDeviceSiteMapping.setId(null);
		this.addObj(equipmentDeviceSiteMapping);
		return null;
	}

	@Override
	public Pager<List<Object[]>> queryMeasuringCountReport(Map<String, String> params, int rows, int page) {
		// TODO Auto-generated method stub
		return equipmentDeviceSiteMappingDao.queryMeasuringCountReport(params, rows, page);
	}
}
