package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.ISkillLevelDao;
import com.digitzones.model.Pager;
import com.digitzones.model.SkillLevel;
import com.digitzones.service.ISkillLevelService;
@Service
public class SkillLevelServiceImpl implements ISkillLevelService {
	private ISkillLevelDao skillLevelDao;
	@Autowired
	public void setSkillLevelDao(ISkillLevelDao skillLevelDao) {
		this.skillLevelDao = skillLevelDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return skillLevelDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(SkillLevel obj) {
		skillLevelDao.update(obj);
	}
	@Override
	public SkillLevel queryByProperty(String name, String value) {
		return skillLevelDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(SkillLevel obj) {
		return skillLevelDao.save(obj);
	}
	@Override
	public SkillLevel queryObjById(Serializable id) {
		return skillLevelDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		skillLevelDao.deleteById(id);
	}
	@Override
	public List<SkillLevel> queryAllSkillLevels() {
		return skillLevelDao.findAll();
	}
}
