package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IRolePowerDao;
import com.digitzones.model.Pager;
import com.digitzones.model.RolePower;
import com.digitzones.service.IRolePowerService;
@Service
public class RolePowerServiceImpl implements IRolePowerService {
	private IRolePowerDao  rolePowerDao;
	@Autowired
	public void setRolePowerDao(IRolePowerDao rolePowerDao) {
		this.rolePowerDao = rolePowerDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return rolePowerDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(RolePower obj) {
		rolePowerDao.update(obj);
	}
	@Override
	public RolePower queryByProperty(String name, String value) {
		return rolePowerDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(RolePower obj) {
		return rolePowerDao.save(obj);
	}
	@Override
	public RolePower queryObjById(Serializable id) {
		return rolePowerDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		rolePowerDao.deleteById(id);
	}
	@Override
	public Long queryCountByPowerId(Long powerId) {
		return rolePowerDao.findCount("from RolePower rp where rp.power.id=?0", new Object[] {powerId});
	}
}
