package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ISparepartRecordDao;
import com.digitzones.devmgr.model.SparepartRecord;
@Repository
public class SparepartRecordDaoImpl extends CommonDaoImpl<SparepartRecord> implements ISparepartRecordDao {
	public SparepartRecordDaoImpl() {
		super(SparepartRecord.class);
	}
}
