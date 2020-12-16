package com.digitzones.app.dao;

import java.util.List;

import com.digitzones.app.model.UserTask;
import com.digitzones.dao.ICommonDao;

public interface IAppUserTaskDao extends ICommonDao<UserTask>{

	/**
	 * 根据任务查询抄送人
	 * @param status
	 * @param userId
	 * @return
	 */
	public List<UserTask> queryByTaskId(Long taskId);
	/**
	 * 根据任务id和userId查询是否已经存在
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public List<UserTask> queryByTaskIdAndUserId(Long taskId, Long userId);
}
