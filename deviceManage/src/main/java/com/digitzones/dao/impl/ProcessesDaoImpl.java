package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProcessesDao;
import com.digitzones.model.Processes;
@Repository
public class ProcessesDaoImpl extends CommonDaoImpl<Processes> implements IProcessesDao {

	public ProcessesDaoImpl() {
		super(Processes.class);
	}
}
