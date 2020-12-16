package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.INGMaintainRecordDao;
import com.digitzones.devmgr.model.NGMaintainRecord;
@Repository
public class NGMaintainRecordDaoImpl extends CommonDaoImpl<NGMaintainRecord> implements INGMaintainRecordDao {
	public NGMaintainRecordDaoImpl() {
		super(NGMaintainRecord.class);
	}
}
