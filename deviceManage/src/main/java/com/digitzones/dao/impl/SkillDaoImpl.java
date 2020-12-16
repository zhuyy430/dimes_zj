package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ISkillDao;
import com.digitzones.model.Skill;
@Repository
public class SkillDaoImpl extends CommonDaoImpl<Skill> implements ISkillDao {
	public SkillDaoImpl() {
		super(Skill.class);
	}
}
