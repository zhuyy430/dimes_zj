package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProjectTypeDao;
import com.digitzones.model.ProjectType;
@Repository
public class ProjectTypeDaoImpl extends CommonDaoImpl<ProjectType> implements IProjectTypeDao {

	public ProjectTypeDaoImpl() {
		super(ProjectType.class);
	}
}
