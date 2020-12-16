package com.digitzones.dao;
/**
 * 工作流dao
 * @author Administrator
 *
 */

import java.util.List;

public interface IWorkFlowDao {
	/**
	 * 查询我的任务
	 * @param roleNames
	 * @return
	 */
	public List<Object[]> queryMyTasks(List<String> roleNames);
}
