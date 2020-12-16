package com.digitzones.app.dao;

import java.util.List;

import com.digitzones.app.model.MytaskView;
import com.digitzones.dao.ICommonDao;

public interface IAppMytaskViewDao extends ICommonDao<MytaskView>{
	/**
	 * 根据用户编码查找发出的任务
	 */
	public List<MytaskView> queryMySendOutTaskByUsercode(String code);
	
	/**
	 * 根据用户编码查找接收的任务
	 */
	public List<MytaskView> queryMyReceiveTaskByUsercode(String code);
	
	/**
	 * 根据用户编码查找已完成的任务
	 */
	public List<MytaskView> queryMyCompleteTaskByUsercode(String code);
}
