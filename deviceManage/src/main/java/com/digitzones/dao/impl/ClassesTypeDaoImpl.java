package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IClassTypeDao;
import com.digitzones.model.ClassType;
@Repository
public class ClassesTypeDaoImpl extends CommonDaoImpl<ClassType> implements IClassTypeDao{
	public ClassesTypeDaoImpl() {
		super(ClassType.class);
	}
}
