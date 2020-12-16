package com.digitzones.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProcessSkillLevelDao;
import com.digitzones.model.ProcessSkillLevel;
@Repository
public class ProcessSkillLevelDaoImpl extends CommonDaoImpl<ProcessSkillLevel> implements IProcessSkillLevelDao {
	public ProcessSkillLevelDaoImpl() {
		super(ProcessSkillLevel.class);
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<Object[]> queryCount4SkillLevel() {
		String sql = "select psl.code,psl.name,COUNT(psl.id) from PROCESSSKILLLEVEL psl inner join EMPLOYEE_SKILL es on psl.id=es.SKILLLEVEL_ID " + 
				"group by psl.code,psl.name";
		return getSession().createNativeQuery(sql).list();
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<Object[]> queryCount4ProcessBySkillLevelCode(String skillLevelCode) {
		String sql = "select p.code,p.name,COUNT(p.id) from SKILL s inner join PROCESSES p on s.PROCESS_ID=p.id " + 
				"			inner join PROCESSSKILLLEVEL psl on s.id=psl.SKILL_ID " + 
				"			inner join EMPLOYEE_SKILL es on psl.id=es.SKILLLEVEL_ID " + 
				"			where psl.code=?0 group by p.name,p.code";
		return getSession().createNativeQuery(sql).setParameter(0, skillLevelCode).list();
	}
}
