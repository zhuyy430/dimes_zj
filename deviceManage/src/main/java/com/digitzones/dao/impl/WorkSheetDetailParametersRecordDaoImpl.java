package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkSheetDetailParametersRecordDao;
import com.digitzones.model.WorkSheetDetailParametersRecord;
@Repository
public class WorkSheetDetailParametersRecordDaoImpl extends CommonDaoImpl<WorkSheetDetailParametersRecord>
		implements IWorkSheetDetailParametersRecordDao {

	public WorkSheetDetailParametersRecordDaoImpl() {
		super(WorkSheetDetailParametersRecord.class);
	}
}
