package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IQualityCalendarTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.QualityCalendarType;
import com.digitzones.service.IQualityCalendarTypeService;
@Service
public class QualityCalendarTypeServiceImpl implements IQualityCalendarTypeService {
	private IQualityCalendarTypeDao qualityCalendarTypeDao;
	@Autowired
	public void setQualityCalendarTypeDao(IQualityCalendarTypeDao qualityCalendarTypeDao) {
		this.qualityCalendarTypeDao = qualityCalendarTypeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return qualityCalendarTypeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(QualityCalendarType obj) {
		qualityCalendarTypeDao.update(obj);
	}
	@Override
	public QualityCalendarType queryByProperty(String name, String value) {
		return qualityCalendarTypeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(QualityCalendarType obj) {
		return qualityCalendarTypeDao.save(obj);
	}
	@Override
	public QualityCalendarType queryObjById(Serializable id) {
		return qualityCalendarTypeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		qualityCalendarTypeDao.deleteById(id);
	}
	@Override
	public List<QualityCalendarType> queryAllQualityCalendarTypes() {
		return qualityCalendarTypeDao.findAll();
	}
}
