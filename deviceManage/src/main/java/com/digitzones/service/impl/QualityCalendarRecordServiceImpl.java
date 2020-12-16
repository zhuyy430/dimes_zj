package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IQualityCalendarRecordDao;
import com.digitzones.model.Pager;
import com.digitzones.model.QualityCalendarRecord;
import com.digitzones.service.IQualityCalendarRecordService;
@Service
public class QualityCalendarRecordServiceImpl implements IQualityCalendarRecordService {
	private IQualityCalendarRecordDao qualityCalendarRecordDao;
	@Autowired
	public void setQualityCalendarRecordDao(IQualityCalendarRecordDao qualityCalendarRecordDao) {
		this.qualityCalendarRecordDao = qualityCalendarRecordDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return qualityCalendarRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(QualityCalendarRecord obj) {
		qualityCalendarRecordDao.update(obj);
	}
	@Override
	public QualityCalendarRecord queryByProperty(String name, String value) {
		return qualityCalendarRecordDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(QualityCalendarRecord obj) {
		return qualityCalendarRecordDao.save(obj);
	}
	@Override
	public QualityCalendarRecord queryObjById(Serializable id) {
		return qualityCalendarRecordDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		qualityCalendarRecordDao.deleteById(id);
	}
	@Override
	public Integer queryCountByDayAndTypeId(Date day, Long typeId) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int date = c.get(Calendar.DATE);
		return qualityCalendarRecordDao.queryCountByDayAndTypeId(year, month, date, typeId);
	}
}
