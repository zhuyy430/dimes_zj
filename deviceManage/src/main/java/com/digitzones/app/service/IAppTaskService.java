package com.digitzones.app.service;

import java.io.Serializable;
import java.util.List;

import com.digitzones.app.model.Task;
import com.digitzones.service.ICommonService;

public interface IAppTaskService extends ICommonService<Task>{
	
	public Serializable addTask(Task Task);
	
	public List<Task> queryTaskByStatusAndUserId(String status, Long userId);
	/**
	 * 查询创建的任务
	 * @param userId
	 * @return
	 */
	public List<Task> queryTaskByCreateUserid(Long userId);
	/**
	 * 查询处理的任务
	 * @param userId
	 * @return
	 */
	public List<Task> queryTaskByTreatUserid(Long userId);
	
	/**
	 * 根据属性查询
	 * @param name
	 * @param value
	 * @return
	 */
	public List<Task> queryListByProperty(String name, String value);
	/**
	 * 根据条件查询
	 */
	public List<Task> queryTaskByCondition(String startDate, String endDate, String status, String content);
}
