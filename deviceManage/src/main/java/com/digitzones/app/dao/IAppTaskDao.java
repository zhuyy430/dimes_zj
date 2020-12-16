package com.digitzones.app.dao;

import java.io.Serializable;
import java.util.List;

import com.digitzones.app.model.Task;
import com.digitzones.dao.ICommonDao;

public interface IAppTaskDao extends ICommonDao<Task> {
	/**
	 * 添加任务
	 * @param task
	 * @param pic
	 * @return
	 */
	public Serializable addTask(Task task);
	
	/**
	 * 根据状态查询任务
	 * @param status RECEIVE,COMPLETE
	 * @param userId
	 * @return
	 */
	public List<Task> queryTaskByStatusAndUserId(String status, Long userId);
	/**
	 * 查询创建的任务
	 * @param status
	 * @param userId
	 * @return
	 */
	public List<Task> queryTaskByCreateUserid(Long userId);
	/**
	 * 查询处理的任务
	 * @param status
	 * @param userId
	 * @return
	 */
	public List<Task> queryTaskByTreatUserid(Long userId);
	/**
	 * 查询收到的任务
	 * @param status
	 * @param userId
	 * @return
	 *//*
	public List<Task> queryTaskByUserid(String status,Long userId);*/
}
