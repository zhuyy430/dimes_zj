package com.digitzones.service;

import java.util.List;

import com.digitzones.model.DispatchedLevel;
/**
 * @author zdq
 * 2019年1月2日
 */
public interface IDispatchedLevelService extends ICommonService<DispatchedLevel> {
	
	/**
	 * 根据类别获取派单等级
	 * @param status
	 * @return
	 */
	public List<DispatchedLevel> queryDispatchedLevelWithStatus(String status);
}
