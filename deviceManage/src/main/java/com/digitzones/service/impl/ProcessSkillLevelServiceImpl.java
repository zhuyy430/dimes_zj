package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IProcessSkillLevelDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessSkillLevel;
import com.digitzones.service.IProcessSkillLevelService;
@Service
public class ProcessSkillLevelServiceImpl implements IProcessSkillLevelService {
	private IProcessSkillLevelDao processSkillLevelDao;
	@Autowired
	public void setProcessSkillLevelDao(IProcessSkillLevelDao processSkillLevelDao) {
		this.processSkillLevelDao = processSkillLevelDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return processSkillLevelDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ProcessSkillLevel obj) {
		processSkillLevelDao.update(obj);
	}

	@Override
	public ProcessSkillLevel queryByProperty(String name, String value) {
		return processSkillLevelDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ProcessSkillLevel obj) {
		return processSkillLevelDao.save(obj);
	}

	@Override
	public ProcessSkillLevel queryObjById(Serializable id) {
		return processSkillLevelDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		processSkillLevelDao.deleteById(id);
	}

	@Override
	public List<ProcessSkillLevel> queryProcessSkillLevelsBySkillId(Long skillId) {
		String hql = "from ProcessSkillLevel psl where psl.skill.id=?0";
		return processSkillLevelDao.findByHQL(hql, new Object[] {skillId});
	}

	@Override
	public List<Object[]> queryCount4SkillLevel() {
		return processSkillLevelDao.queryCount4SkillLevel();
	}

	@Override
	public List<Object[]> queryCount4ProcessBySkillLevelCode(String skillLevelCode) {
		return processSkillLevelDao.queryCount4ProcessBySkillLevelCode(skillLevelCode);
	}
}
