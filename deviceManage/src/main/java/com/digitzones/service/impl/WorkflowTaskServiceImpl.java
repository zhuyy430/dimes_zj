package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IWorkflowTaskDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkflowTask;
import com.digitzones.service.IWorkflowTaskService;
@Service
public class WorkflowTaskServiceImpl implements IWorkflowTaskService {
	@Autowired
	private IWorkflowTaskDao workflowTaskDao;
	@Override
	public Pager<WorkflowTask> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workflowTaskDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(WorkflowTask obj) {
		workflowTaskDao.update(obj);
	}
	@Override
	public WorkflowTask queryByProperty(String name, String value) {
		return workflowTaskDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(WorkflowTask obj) {
		return workflowTaskDao.save(obj);
	}
	@Override
	public WorkflowTask queryObjById(Serializable id) {
		return workflowTaskDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		workflowTaskDao.deleteById(id);
	}
	@Override
	public List<WorkflowTask> queryByBusinessKey(String businessKey) {
		return workflowTaskDao.findByHQL("from WorkflowTask task where task.businessKey=?0", new Object[] {businessKey});
	}
}
