package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkflowTaskDao;
import com.digitzones.model.WorkflowTask;
@Repository
public class WorkflowTaskDaoImpl extends CommonDaoImpl<WorkflowTask> implements IWorkflowTaskDao {
	public WorkflowTaskDaoImpl() {
		super(WorkflowTask.class);
	}
}
