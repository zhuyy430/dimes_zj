package com.digitzones.dao;

import java.util.List;
import java.util.Map;

import com.digitzones.model.Power;

/**
 * 权限管理dao
 * @author zdq
 * 2018年6月11日
 */
public interface IPowerDao extends ICommonDao<Power> {
	/**
	 * 查询所有权限模块
	 * @return
	 */
	public List<Map<String,Object>> queryAllGroup();
}
