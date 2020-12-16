package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IParameterDao;
import com.digitzones.model.Parameters;
@Repository
public class ParameterDaoImpl extends CommonDaoImpl<Parameters> implements IParameterDao {
	public ParameterDaoImpl() {
		super(Parameters.class);
	}
}
