package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProcessTypeDao;
import com.digitzones.model.ProcessType;
@Repository
public class ProcessTypeDaoImpl extends CommonDaoImpl<ProcessType> implements IProcessTypeDao {

	public ProcessTypeDaoImpl() {
		super(ProcessType.class);
	}
}
