package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.ISecureEnvironmentGradeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.SecureEnvironmentGrade;
import com.digitzones.service.ISecureEnvironmentGradeService;
@Service
public class SecureEnvironmentGradeServiceImpl implements ISecureEnvironmentGradeService {
	private ISecureEnvironmentGradeDao secureEnvironmentGradeDao;
	@Autowired
	public void setSecureEnvironmentGradeDao(ISecureEnvironmentGradeDao secureEnvironmentGradeDao) {
		this.secureEnvironmentGradeDao = secureEnvironmentGradeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return secureEnvironmentGradeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(SecureEnvironmentGrade obj) {
		secureEnvironmentGradeDao.update(obj);
	}
	@Override
	public SecureEnvironmentGrade queryByProperty(String name, String value) {
		return secureEnvironmentGradeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(SecureEnvironmentGrade obj) {
		return secureEnvironmentGradeDao.save(obj);
	}
	@Override
	public SecureEnvironmentGrade queryObjById(Serializable id) {
		return secureEnvironmentGradeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		secureEnvironmentGradeDao.deleteById(id);
	}
	@Override
	public List<SecureEnvironmentGrade> queryAllSecureEnvironmentGrades() {
		return secureEnvironmentGradeDao.findAll();
	}
}
