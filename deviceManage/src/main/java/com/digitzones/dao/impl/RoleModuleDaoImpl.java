package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IRoleModuleDao;
import com.digitzones.model.RoleModule;
@Repository
public class RoleModuleDaoImpl extends CommonDaoImpl<RoleModule> implements IRoleModuleDao {
	public RoleModuleDaoImpl() {
		super(RoleModule.class);
	}
}
