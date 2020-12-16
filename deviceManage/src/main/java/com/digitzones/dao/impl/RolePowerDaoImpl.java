package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IRolePowerDao;
import com.digitzones.model.RolePower;
@Repository
public class RolePowerDaoImpl extends CommonDaoImpl<RolePower> implements IRolePowerDao {
	public RolePowerDaoImpl() {
		super(RolePower.class);
	}
}
