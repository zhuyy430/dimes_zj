package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IConfigDao;
import com.digitzones.devmgr.model.Config;
import com.digitzones.devmgr.service.IConfigService;
import com.digitzones.model.Pager;
@Service
public class ConfigServiceImpl implements IConfigService {
	@Autowired
	private IConfigDao ConfigDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return ConfigDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Config obj) {
		ConfigDao.update(obj);
	}

	@Override
	public Config queryByProperty(String name, String value) {
		return ConfigDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Config obj) {
		return ConfigDao.save(obj);
	}

	@Override
	public Config queryObjById(Serializable id) {
		return ConfigDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		ConfigDao.deleteById(id);
	}
}
