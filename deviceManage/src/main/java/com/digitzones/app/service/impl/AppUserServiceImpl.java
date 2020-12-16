package com.digitzones.app.service.impl;

import com.digitzones.app.dao.IAppUserDao;
import com.digitzones.app.service.IAppUserService;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
@Service
public class AppUserServiceImpl implements IAppUserService {
	@Autowired
	private IAppUserDao userDao;

	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateObj(User obj) {
		userDao.update(obj);
	}

	@Override
	public User queryByProperty(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable addObj(User obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User queryObjById(Serializable id) {
		return userDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {

	}


	@Override
	public List<User> queryAllUser() {
		return userDao.findAll();
	}
	
	
}
