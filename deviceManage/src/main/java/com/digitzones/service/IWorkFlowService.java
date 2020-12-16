package com.digitzones.service;
import java.util.List;
/**
 * 工作流service
 * @author Administrator
 */
public interface IWorkFlowService {
	/**
	 * 查询我的任务
	 * @param roleNames
	 * @return
	 */
	public List<Object[]> queryMyTasks(List<String> roleNames);
}
