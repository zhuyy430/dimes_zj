package com.digitzones.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IWorkFlowDao;
import com.digitzones.service.IWorkFlowService;
@Service
public class WorkFlowServiceImpl implements IWorkFlowService {
	private IWorkFlowDao workFlowDao;
	@Autowired
	public void setWorkFlowDao(IWorkFlowDao workFlowDao) {
		this.workFlowDao = workFlowDao;
	}

	@Override
	public List<Object[]> queryMyTasks(List<String> roleNames) {
		return workFlowDao.queryMyTasks(roleNames);
	}
}
