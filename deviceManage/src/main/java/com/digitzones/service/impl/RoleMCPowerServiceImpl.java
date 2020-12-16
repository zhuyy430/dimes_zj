package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IRoleMCPowerDao;
import com.digitzones.model.Pager;
import com.digitzones.model.RoleMCPower;
import com.digitzones.service.IRoleMCPowerService;
@Service
public class RoleMCPowerServiceImpl implements IRoleMCPowerService {
	@Autowired
	private IRoleMCPowerDao roleMCPowerDao;
	@Override
	public Pager<RoleMCPower> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return roleMCPowerDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(RoleMCPower obj) {
		roleMCPowerDao.update(obj);
	}

	@Override
	public RoleMCPower queryByProperty(String name, String value) {
		return roleMCPowerDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(RoleMCPower obj) {
		return roleMCPowerDao.save(obj);
	}

	@Override
	public RoleMCPower queryObjById(Serializable id) {
		return roleMCPowerDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		roleMCPowerDao.deleteById(id);
	}

	@Override
	public List<RoleMCPower> queryByRoleId(Long roleId) {
		return roleMCPowerDao.findByHQL("from RoleMCPower p where p.role.id=?0", new Object[] {roleId});
	}

	@Override
	public void deleteByRoleId(Long roleId) {
		roleMCPowerDao.deleteByRoleId(roleId);
	}
}
