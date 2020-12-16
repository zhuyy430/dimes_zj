package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkflowRecordDao;
import com.digitzones.model.WorkflowRecord;
@Repository
public class WorkflowRecordDaoImpl extends CommonDaoImpl<WorkflowRecord> implements IWorkflowRecordDao {
	public WorkflowRecordDaoImpl() {
		super(WorkflowRecord.class);
	}
}
