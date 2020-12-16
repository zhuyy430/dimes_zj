package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ISkillLevelDao;
import com.digitzones.model.SkillLevel;
@Repository
public class SkillLevelDaoImpl extends CommonDaoImpl<SkillLevel> implements ISkillLevelDao {

	public SkillLevelDaoImpl() {
		super(SkillLevel.class);
	}
}
