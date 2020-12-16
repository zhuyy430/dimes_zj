package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.DispatchedLevel;
/**
 * 文档关联类别dao
 * @author zdq
 * 2019年1月2日
 */
public interface IDispatchedLevelDao extends ICommonDao<DispatchedLevel> {

	/**
	 * 获取某状态得所有维修单
	 */
	public List<DispatchedLevel> queryDispatchedLevelWithStatus(String status);
}
