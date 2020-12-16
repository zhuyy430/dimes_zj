package com.digitzones.dao.impl;

import com.digitzones.dao.IProcessParameterRecordDao;
import com.digitzones.model.ProcessParameterRecord;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessParameterRecordDaoImpl extends CommonDaoImpl<ProcessParameterRecord> implements IProcessParameterRecordDao {
	public ProcessParameterRecordDaoImpl() {
		super(ProcessParameterRecord.class);
	}
}
