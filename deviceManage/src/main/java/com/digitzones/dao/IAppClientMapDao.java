package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.AppClientMap;

public interface IAppClientMapDao extends ICommonDao<AppClientMap> {
	/**
	 * 根据用户名查找clientID
	 * @param usernames
	 * @return
	 */
	public List<String> queryCids(List<String> usernames);
}
