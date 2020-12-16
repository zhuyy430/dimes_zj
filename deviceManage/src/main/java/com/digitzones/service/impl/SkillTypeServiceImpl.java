package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.ISkillTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.SkillType;
import com.digitzones.service.ISkillTypeService;
@Service
public class SkillTypeServiceImpl implements ISkillTypeService {
	private ISkillTypeDao skillTypeDao;
	@Autowired
	public void setSkillTypeDao(ISkillTypeDao skillTypeDao) {
		this.skillTypeDao = skillTypeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return this.skillTypeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(SkillType obj) {
		skillTypeDao.update(obj);
	}
	@Override
	public SkillType queryByProperty(String name, String value) {
		return skillTypeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(SkillType obj) {
		return skillTypeDao.save(obj);
	}
	@Override
	public SkillType queryObjById(Serializable id) {
		return skillTypeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		skillTypeDao.deleteById(id);
	}
	@Override
	public List<SkillType> queryAllSkillTypes() {
		return skillTypeDao.findAll();
	}
}
