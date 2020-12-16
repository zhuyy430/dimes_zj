package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IParameterTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ParameterType;
import com.digitzones.service.IParameterTypeService;
@Service
public class ParameterTypeServiceImpl implements IParameterTypeService {
	private IParameterTypeDao parameterTypeDao;
	@Autowired
	public void setParameterTypeDao(IParameterTypeDao parameterTypeDao) {
		this.parameterTypeDao = parameterTypeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return parameterTypeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ParameterType obj) {
		parameterTypeDao.update(obj);
	}

	@Override
	public ParameterType queryByProperty(String name, String value) {
		return parameterTypeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ParameterType obj) {
		return parameterTypeDao.save(obj);
	}

	@Override
	public ParameterType queryObjById(Serializable id) {
		return parameterTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		parameterTypeDao.deleteById(id);
	}

	@Override
	public List<ParameterType> queryTopParameterType() {
		return  parameterTypeDao.findByHQL("from ParameterType p where p.parent is null", new Object[] {});
	}

	@Override
	public Long queryCountOfSubParameterType(Serializable pid) {
		return parameterTypeDao.findCount("from ParameterType d inner join d.parent p where p.id=?0", new Object[] {pid});
	}

	@Override
	public Long queryCountOfParameter(Serializable typeId) {
		return  parameterTypeDao.findCount("from Parameters d inner join d.parameterType p where p.id=?0", new Object[] {typeId});
	}
}
