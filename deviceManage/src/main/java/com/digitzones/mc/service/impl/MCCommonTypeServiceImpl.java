package com.digitzones.mc.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCCommonTypeDao;
import com.digitzones.mc.model.MCCommonType;
import com.digitzones.mc.service.IMCCommonTypeService;
import com.digitzones.model.Pager;
@Service
public class MCCommonTypeServiceImpl implements IMCCommonTypeService {
	@Autowired
	private IMCCommonTypeDao mcCommonTypeDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return mcCommonTypeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MCCommonType obj) {
		mcCommonTypeDao.update(obj);
		
	}

	@Override
	public MCCommonType queryByProperty(String name, String value) {
		return mcCommonTypeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MCCommonType obj) {
		return mcCommonTypeDao.save(obj);
	}

	@Override
	public MCCommonType queryObjById(Serializable id) {
		return mcCommonTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		mcCommonTypeDao.deleteById(id);
	}

}
