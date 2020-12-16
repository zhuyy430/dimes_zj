package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IUserDao;
import com.digitzones.dao.IUserRoleDao;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;
import com.digitzones.service.IUserRoleService;
@Service
public class UserRoleServiceImpl implements IUserRoleService {
	@Autowired
	private IUserDao userDao;
	private IUserRoleDao userRoleDao;
	@Autowired
	public void setUserRoleDao(IUserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return userRoleDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(UserRole obj) {
		userRoleDao.update(obj);
	}
	@Override
	public UserRole queryByProperty(String name, String value) {
		return userRoleDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(UserRole obj) {
		return userRoleDao.save(obj);
	}
	@Override
	public UserRole queryObjById(Serializable id) {
		return userRoleDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		userRoleDao.deleteById(id);
	}
	@Override
	public Long queryCountByRoleId(Long roleId) {
		return userRoleDao.findCount("from UserRole ur where ur.role.id=?0", new Object[] {roleId});
	}
	@Override
	public List<User> queryUsersByRoleId(Long roleId) {
		return userDao.findByHQL("select distinct ur.user from UserRole ur where ur.role.id=?0 ", new Object[] {roleId});
	}
}
