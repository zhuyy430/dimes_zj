package com.digitzones.procurement.service.impl;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.ICraftsRouteDao;
import com.digitzones.procurement.model.CraftsRoute;
import com.digitzones.procurement.service.ICraftsRouteService;
@Service
public class CraftsRouteServiceImpl implements ICraftsRouteService {
	@Autowired
	private ICraftsRouteDao craftsRouteDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return craftsRouteDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(CraftsRoute obj) {
		craftsRouteDao.update(obj);
	}

	@Override
	public CraftsRoute queryByProperty(String name, String value) {
		return craftsRouteDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(CraftsRoute obj) {
		return craftsRouteDao.save(obj);
	}

	@Override
	public CraftsRoute queryObjById(Serializable id) {
		return craftsRouteDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		craftsRouteDao.deleteById(id);
	}

	@Override
	public void deleteObjById(String id) {
		craftsRouteDao.deleteById(id);		
	}
}
