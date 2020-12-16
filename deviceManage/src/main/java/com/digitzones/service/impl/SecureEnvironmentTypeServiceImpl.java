package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.ISecureEnvironmentTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.SecureEnvironmentType;
import com.digitzones.service.ISecureEnvironmentTypeService;
@Service
public class SecureEnvironmentTypeServiceImpl implements ISecureEnvironmentTypeService {
	private ISecureEnvironmentTypeDao secureEnvironmentTypeDao;
	@Autowired 
	public void setSecureEnvironmentTypeDao(ISecureEnvironmentTypeDao secureEnvironmentTypeDao) {
		this.secureEnvironmentTypeDao = secureEnvironmentTypeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return secureEnvironmentTypeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(SecureEnvironmentType obj) {
		secureEnvironmentTypeDao.update(obj);
	}
	@Override
	public SecureEnvironmentType queryByProperty(String name, String value) {
		return secureEnvironmentTypeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(SecureEnvironmentType obj) {
		return secureEnvironmentTypeDao.save(obj);
	}
	@Override
	public SecureEnvironmentType queryObjById(Serializable id) {
		return secureEnvironmentTypeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		secureEnvironmentTypeDao.deleteById(id);
	}
	@Override
	public List<SecureEnvironmentType> queryAllSecureEnvironmentTypes() {
		return secureEnvironmentTypeDao.findAll();
	}
	@Override
	public Integer queryCountByDayAndTypeId(Date day, Long typeId) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int date = c.get(Calendar.DATE);
		return secureEnvironmentTypeDao.queryCountByDayAndTypeId(year, month, date, typeId);
	}
}
