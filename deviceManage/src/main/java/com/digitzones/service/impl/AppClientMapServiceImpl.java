package com.digitzones.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IAppClientMapDao;
import com.digitzones.model.AppClientMap;
import com.digitzones.model.Pager;
import com.digitzones.service.IAppClientMapService;
@Service
public class AppClientMapServiceImpl implements IAppClientMapService {
	@Autowired
	private IAppClientMapDao appClientMapDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return appClientMapDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(AppClientMap obj) {
		appClientMapDao.update(obj);
	}
	@Override
	public AppClientMap queryByProperty(String name, String value) {
		return appClientMapDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(AppClientMap obj) {
		return appClientMapDao.save(obj);
	}

	@Override
	public AppClientMap queryObjById(Serializable id) {
		return appClientMapDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		appClientMapDao.deleteById(id);
	}

	@Override
	public List<String> queryCids(List<String> usernames) {
		return appClientMapDao.queryCids(usernames);
	}
}
