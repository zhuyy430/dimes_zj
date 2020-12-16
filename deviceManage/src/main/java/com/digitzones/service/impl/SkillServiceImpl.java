package com.digitzones.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IProcessSkillLevelDao;
import com.digitzones.dao.ISkillDao;
import com.digitzones.dao.ISkillLevelDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessSkillLevel;
import com.digitzones.model.Skill;
import com.digitzones.model.SkillLevel;
import com.digitzones.service.ISkillService;
@Service
public class SkillServiceImpl implements ISkillService {
	private ISkillDao skillDao;
	private ISkillLevelDao skillLevelDao;
	private IProcessSkillLevelDao processSkillLevelDao;
	@Autowired
	public void setProcessSkillLevelDao(IProcessSkillLevelDao processSkillLevelDao) {
		this.processSkillLevelDao = processSkillLevelDao;
	}
	@Autowired
	public void setSkillLevelDao(ISkillLevelDao skillLevelDao) {
		this.skillLevelDao = skillLevelDao;
	}
	@Autowired
	public void setSkillDao(ISkillDao skillDao) {
		this.skillDao = skillDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return skillDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(Skill obj) {
		skillDao.update(obj);
	}
	@Override
	public Skill queryByProperty(String name, String value) {
		return skillDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(Skill obj) {
		Serializable id = skillDao.save(obj);
		//查找技能等级
		List<SkillLevel> skillLevels = skillLevelDao.findAll();
		for(SkillLevel level : skillLevels) {
			ProcessSkillLevel psl = new ProcessSkillLevel();
			psl.setCode(level.getCode());
			psl.setName(level.getName());
			psl.setSkill(obj);
			processSkillLevelDao.save(psl);
		}
		return id;
	}
	@Override
	public Skill queryObjById(Serializable id) {
		return skillDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		List<ProcessSkillLevel> skillLevels = processSkillLevelDao.findByHQL("from ProcessSkillLevel psl where psl.skill.id=?0", new Object[] {id});
		for(ProcessSkillLevel psl : skillLevels) {
			processSkillLevelDao.delete(psl);
		}
		skillDao.deleteById(id);
	}
	@Override
	public List<Skill> queryOtherSkillsByEmployeeId(Long employeeId) {
		String hql = "select d from Skill d where d.disabled!=?1 and d.id not in (select cdm.skillLevel.skill.id from EmployeeSkillMapping cdm"
				+ "  where  cdm.employee.id=?0)";
		return skillDao.findByHQL(hql, new Object[] {employeeId,true});
	}
	@Override
	public List<Skill> queryOtherSkillsByEmployeeIdAndCondition(Long employeeId, String q) {
		String hql = "select d from Skill d where d.disabled!=?2 and d.id not in (select cdm.skillLevel.skill.id from EmployeeSkillMapping cdm"
				+ "  where  cdm.employee.id=?0 and (d.code like ?1 or d.name like ?1 or d.skillType.name like ?1))";
		return skillDao.findByHQL(hql, new Object[] {employeeId,"%" + q + "%",true});
	}
}
