package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ITransferRecordDao;
import com.digitzones.devmgr.model.TransferRecord;
@Repository
public class TransferRecordDaoImpl extends CommonDaoImpl<TransferRecord>
		implements ITransferRecordDao {
	public TransferRecordDaoImpl() {
		super(TransferRecord.class);
	}
}
