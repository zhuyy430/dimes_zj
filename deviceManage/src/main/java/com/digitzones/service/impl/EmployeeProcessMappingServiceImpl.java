package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IEmployeeProcessMappingDao;
import com.digitzones.model.EmployeeProcessMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IEmployeeProcessMappingService;
@Service
public class EmployeeProcessMappingServiceImpl implements IEmployeeProcessMappingService {
	private IEmployeeProcessMappingDao employeeProcessMappingDao;
	@Autowired
	public void setEmployeeProcessMappingDao(IEmployeeProcessMappingDao employeeProcessMappingDao) {
		this.employeeProcessMappingDao = employeeProcessMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return employeeProcessMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EmployeeProcessMapping obj) {
		employeeProcessMappingDao.update(obj);
	}

	@Override
	public EmployeeProcessMapping queryByProperty(String name, String value) {
		return employeeProcessMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EmployeeProcessMapping obj) {
		return employeeProcessMappingDao.save(obj);
	}

	@Override
	public EmployeeProcessMapping queryObjById(Serializable id) {
		return employeeProcessMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		employeeProcessMappingDao.deleteById(id);
	}
}
