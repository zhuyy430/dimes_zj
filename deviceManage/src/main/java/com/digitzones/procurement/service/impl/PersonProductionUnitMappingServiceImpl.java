package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IPersonProductionUnitMappingDao;
import com.digitzones.procurement.model.PersonProductionUnitMapping;
import com.digitzones.procurement.service.IPersonProductionUnitMappingService;
@Service
public class PersonProductionUnitMappingServiceImpl implements IPersonProductionUnitMappingService {
	@Autowired
	private IPersonProductionUnitMappingDao personProductionUnitMappingDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return personProductionUnitMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(PersonProductionUnitMapping obj) {
		personProductionUnitMappingDao.update(obj);
	}

	@Override
	public PersonProductionUnitMapping queryByProperty(String name, String value) {
		return personProductionUnitMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(PersonProductionUnitMapping obj) {
		return personProductionUnitMappingDao.save(obj);
	}

	@Override
	public PersonProductionUnitMapping queryObjById(Serializable id) {
		return personProductionUnitMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		personProductionUnitMappingDao.deleteById(id);		
	}

	@Override
	public PersonProductionUnitMapping queryByPersonCode(String code) {
		List<PersonProductionUnitMapping> list = personProductionUnitMappingDao.findByHQL("from PersonProductionUnitMapping p where p.person.code=?0", new Object[]{code});
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
