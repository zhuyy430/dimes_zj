package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IPressLightDao;
import com.digitzones.model.Pager;
import com.digitzones.model.PressLight;
import com.digitzones.service.IPressLightService;
@Service
public class PressLightServiceImpl implements IPressLightService {
	private IPressLightDao pressLightDao;
	@Autowired
	public void setPressLightDao(IPressLightDao pressLightDao) {
		this.pressLightDao = pressLightDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return pressLightDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(PressLight obj) {
		pressLightDao.update(obj);
	}

	@Override
	public PressLight queryByProperty(String name, String value) {
		return pressLightDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(PressLight obj) {
		return pressLightDao.save(obj);
	}

	@Override
	public PressLight queryObjById(Serializable id) {
		return pressLightDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		pressLightDao.deleteById(id);
	}

	@Override
	public List<PressLight> queryAllPressLightByTypeId(Long typeId) {
		return pressLightDao.findByHQL("from PressLight pl where pl.pressLightType.id=?0", new Object[] {typeId});
	}

	@Override
	public List<PressLight> queryPressLightByBasicCode(String basicCode) {
		return pressLightDao.findByHQL("from PressLight pl where pl.pressLightType.basicCode=?0 or pl.pressLightType.code=?0",new Object[] {basicCode});
	}

	@Override
	public List<PressLight> queryAllPressLight() {
		return pressLightDao.findByHQL("from PressLight pt",new Object[] {});
	}
}
