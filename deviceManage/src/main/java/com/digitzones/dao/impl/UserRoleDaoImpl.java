package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IUserRoleDao;
import com.digitzones.model.UserRole;
@Repository
public class UserRoleDaoImpl extends CommonDaoImpl<UserRole> implements IUserRoleDao {
	public UserRoleDaoImpl() {
		super(UserRole.class);
	}
}
