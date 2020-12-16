package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IPowerDao;
import com.digitzones.model.Pager;
import com.digitzones.model.Power;
import com.digitzones.service.IPowerService;
@Service
public class PowerServiceImpl implements IPowerService {
	private IPowerDao powerDao;
	@Autowired
	public void setPowerDao(IPowerDao powerDao) {
		this.powerDao = powerDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return powerDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Power obj) {
		powerDao.update(obj);
	}

	@Override
	public Power queryByProperty(String name, String value) {
		return powerDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Power obj) {
		return powerDao.save(obj);
	}

	@Override
	public Power queryObjById(Serializable id) {
		return powerDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		powerDao.deleteById(id);
	}

	@Override
	public List<Power> queryAllPowers() {
		return powerDao.findByHQL("from Power p where p.disable!=?0  or p.disable is null order by p.group", new Object[] {true});
	}
	
	@Override
	public List<Power> queryAllPowersByUserId(Long userId) {
		return powerDao.findByHQL("select distinct rp.power from RolePower rp inner join UserRole ur on rp.role.id=ur.role.id  where ur.user.id=?0", new Object[] {userId});
	}

	@Override
	public List<Power> queryPowersByRoleId(Long roleId) {
		return powerDao.findByHQL("select rp.power from RolePower rp where rp.role.id=?0", new Object[] {roleId});
	}
	@Override
	public List<Power> queryPowersByGroup(String group) {
		return powerDao.findByHQL("select p from Power p where p.group=?0", new Object[] {group});
	}

	@Override
	public List<Map<String, Object>> queryAllGroup() {
		List<Map<String, Object>> list = powerDao.queryAllGroup();
		return list;
	}

	@Override
	public List<Power> queryPowersByRoleIdAndGroup(Long roleId, String group) {
		return powerDao.findByHQL("select rp.power from RolePower rp where rp.role.id=?0 and rp.power.group=?1", new Object[] {roleId,group});
	}
}
