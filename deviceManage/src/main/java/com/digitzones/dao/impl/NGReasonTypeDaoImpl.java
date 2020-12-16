package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.INGReasonTypeDao;
import com.digitzones.model.NGReasonType;
@Repository
public class NGReasonTypeDaoImpl extends CommonDaoImpl<NGReasonType> implements INGReasonTypeDao {
	public NGReasonTypeDaoImpl() {
		super(NGReasonType.class);
	}
}
