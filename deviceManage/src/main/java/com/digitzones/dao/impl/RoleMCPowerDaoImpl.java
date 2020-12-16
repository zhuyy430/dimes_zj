package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IRoleMCPowerDao;
import com.digitzones.model.RoleMCPower;
@Repository
public class RoleMCPowerDaoImpl extends CommonDaoImpl<RoleMCPower> implements IRoleMCPowerDao{

	public RoleMCPowerDaoImpl() {
		super(RoleMCPower.class);
	}

	@Override
	public void deleteByRoleId(Long roleId) {
		getSession().createQuery("delete from RoleMCPower p where p.role.id=:roleId").setParameter("roleId", roleId).executeUpdate();
	}

}
