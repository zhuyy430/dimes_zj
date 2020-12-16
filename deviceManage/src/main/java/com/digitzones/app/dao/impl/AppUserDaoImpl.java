package com.digitzones.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.app.dao.IAppUserDao;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.model.User;
@Repository
public class AppUserDaoImpl  extends CommonDaoImpl<User> implements IAppUserDao  {

	public AppUserDaoImpl() {
		super(User.class);
	}
}
