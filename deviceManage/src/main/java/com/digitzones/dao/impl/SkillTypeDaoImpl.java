package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ISkillTypeDao;
import com.digitzones.model.SkillType;
@Repository
public class SkillTypeDaoImpl extends CommonDaoImpl<SkillType> implements ISkillTypeDao {
	public SkillTypeDaoImpl() {
		super(SkillType.class);
	}
}
