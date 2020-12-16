package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IParameterTypeDao;
import com.digitzones.model.ParameterType;
@Repository
public class ParameterTypeDaoImpl extends CommonDaoImpl<ParameterType> implements IParameterTypeDao {
	public ParameterTypeDaoImpl() {
		super(ParameterType.class);
	}
}
