package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IQualityCalendarTypeDao;
import com.digitzones.model.QualityCalendarType;
@Repository
public class QualityCalendarTypeDaoImpl extends CommonDaoImpl<QualityCalendarType> implements IQualityCalendarTypeDao {
	public QualityCalendarTypeDaoImpl() {
		super(QualityCalendarType.class);
	}
}
