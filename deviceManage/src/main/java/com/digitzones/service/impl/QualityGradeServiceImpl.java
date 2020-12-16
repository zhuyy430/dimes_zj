package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IQualityGradeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.QualityGrade;
import com.digitzones.service.IQualityGradeService;
@Service
public class QualityGradeServiceImpl implements IQualityGradeService {
	private IQualityGradeDao qualityGradeDao;
	@Autowired
	public void setQualityGradeDao(IQualityGradeDao qualityGradeDao) {
		this.qualityGradeDao = qualityGradeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return qualityGradeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(QualityGrade obj) {
		qualityGradeDao.update(obj);
	}
	@Override
	public QualityGrade queryByProperty(String name, String value) {
		return qualityGradeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(QualityGrade obj) {
		return qualityGradeDao.save(obj);
	}
	@Override
	public QualityGrade queryObjById(Serializable id) {
		return qualityGradeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		qualityGradeDao.deleteById(id);
	}
	@Override
	public List<QualityGrade> queryAllQualityGrades() {
		return qualityGradeDao.findAll();
	}
}
