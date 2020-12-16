package com.digitzones.service;

import java.util.List;

import com.digitzones.model.WorkflowTask;

public interface IWorkflowTaskService extends ICommonService<WorkflowTask> {
	/**
	 * 根据业务键查找任务节点
	 * @param businessKey
	 * @return
	 */
	List<WorkflowTask> queryByBusinessKey(String businessKey);

}
