package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IProcessTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessType;
import com.digitzones.service.IProcessTypeService;
@Service
public class ProcessTypeServiceImpl implements IProcessTypeService {
	private IProcessTypeDao processTypeDao;
	@Autowired
	public void setProcessTypeDao(IProcessTypeDao processTypeDao) {
		this.processTypeDao = processTypeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return processTypeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ProcessType obj) {
		processTypeDao.update(obj);
	}

	@Override
	public ProcessType queryByProperty(String name, String value) {
		return processTypeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ProcessType obj) {
		return processTypeDao.save(obj);
	}

	@Override
	public ProcessType queryObjById(Serializable id) {
		return processTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		processTypeDao.deleteById(id);
	}

	@Override
	public List<ProcessType> queryTopProcessTypes() {
		return processTypeDao.findByHQL("from ProcessType pt where pt.parent is null", new Object[] {});
	}

	@Override
	public List<ProcessType> queryAllProcessTypes() {
		return processTypeDao.findAll();
	}
	
}
