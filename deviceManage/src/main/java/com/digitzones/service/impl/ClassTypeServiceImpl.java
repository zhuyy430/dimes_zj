package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IClassTypeDao;
import com.digitzones.model.ClassType;
import com.digitzones.model.Pager;
import com.digitzones.service.IClassesTypeService;
@Service
public class ClassTypeServiceImpl implements IClassesTypeService {
	private IClassTypeDao classTypeDao;
	@Autowired
	public void setClassTypeDao(IClassTypeDao classTypeDao) {
		this.classTypeDao = classTypeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return classTypeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ClassType obj) {
		classTypeDao.update(obj);
	}

	@Override
	public ClassType queryByProperty(String name, String value) {
		return classTypeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ClassType obj) {
		return classTypeDao.save(obj);
	}

	@Override
	public ClassType queryObjById(Serializable id) {
		return classTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		classTypeDao.deleteById(id);
	}

	@Override
	public List<ClassType> queryAllClassTypes() {
		return classTypeDao.findAll();
	}
}
