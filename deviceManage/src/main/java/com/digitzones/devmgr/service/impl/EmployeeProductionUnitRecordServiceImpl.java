package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IEmployeeProductionUnitRecordDao;
import com.digitzones.devmgr.model.EmployeeProductionUnitRecord;
import com.digitzones.devmgr.service.IEmployeeProductionUnitRecordService;
import com.digitzones.model.Pager;
@Service
public class EmployeeProductionUnitRecordServiceImpl implements IEmployeeProductionUnitRecordService {
	@Autowired
	private IEmployeeProductionUnitRecordDao employeeProductionUnitRecordDao ;
	@Override
	public Pager<EmployeeProductionUnitRecord> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return employeeProductionUnitRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EmployeeProductionUnitRecord obj) {
		employeeProductionUnitRecordDao.update(obj);
	}

	@Override
	public EmployeeProductionUnitRecord queryByProperty(String name, String value) {
		return employeeProductionUnitRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EmployeeProductionUnitRecord obj) {
		return employeeProductionUnitRecordDao.save(obj);
	}

	@Override
	public EmployeeProductionUnitRecord queryObjById(Serializable id) {
		return employeeProductionUnitRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		employeeProductionUnitRecordDao.deleteById(id);
	}
}
