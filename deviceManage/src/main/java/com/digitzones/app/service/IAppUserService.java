package com.digitzones.app.service;

import java.util.List;

import com.digitzones.model.User;
import com.digitzones.service.ICommonService;

public interface IAppUserService extends ICommonService<User>{

	public List<User> queryAllUser();
}
