package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IDispatchedLevelDao;
import com.digitzones.model.DispatchedLevel;
import com.digitzones.model.Pager;
import com.digitzones.service.IDispatchedLevelService;
@Service
public class DispatchedLevelServiceImpl implements IDispatchedLevelService {
	@Autowired
	private IDispatchedLevelDao orderLevelDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return orderLevelDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DispatchedLevel obj) {
		orderLevelDao.update(obj);
	}

	@Override
	public DispatchedLevel queryByProperty(String name, String value) {
		return orderLevelDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DispatchedLevel obj) {
		return orderLevelDao.save(obj);
	}

	@Override
	public DispatchedLevel queryObjById(Serializable id) {
		return orderLevelDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		orderLevelDao.deleteById(id);
		
	}

	@Override
	public List<DispatchedLevel> queryDispatchedLevelWithStatus(String status) {
		return orderLevelDao.queryDispatchedLevelWithStatus(status);
	}
}
