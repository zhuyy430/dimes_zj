package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IWorkflowRecordDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkflowRecord;
import com.digitzones.service.IWorkflowRecordService;
@Service
public class WorkflowRecordServiceImpl implements IWorkflowRecordService {
	@Autowired
	private IWorkflowRecordDao workflowRecordDao;
	@Override
	public Pager<WorkflowRecord> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workflowRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(WorkflowRecord obj) {
		workflowRecordDao.update(obj);
	}

	@Override
	public WorkflowRecord queryByProperty(String name, String value) {
		return workflowRecordDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(WorkflowRecord obj) {
		return workflowRecordDao.save(obj);
	}

	@Override
	public WorkflowRecord queryObjById(Serializable id) {
		return workflowRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		workflowRecordDao.deleteById(id);
	}
}
