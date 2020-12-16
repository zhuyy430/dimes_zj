package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IStatusTroubleCodeDao;
import com.digitzones.model.StatusTroubleCode;
@Repository
public class StatusTroubleCodeDaoImpl extends CommonDaoImpl<StatusTroubleCode> implements IStatusTroubleCodeDao {

	public StatusTroubleCodeDaoImpl() {
		super(StatusTroubleCode.class);
	}
}
