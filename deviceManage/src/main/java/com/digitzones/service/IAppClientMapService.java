package com.digitzones.service;

import java.util.List;

import com.digitzones.model.AppClientMap;

public interface IAppClientMapService extends ICommonService<AppClientMap> {
	/**
	 * 根据用户名 查询clientID
	 * @param userNames
	 * @return
	 */
	public List<String> queryCids(List<String> userNames);
}
