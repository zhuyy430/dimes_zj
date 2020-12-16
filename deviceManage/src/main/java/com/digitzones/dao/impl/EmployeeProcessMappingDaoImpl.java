package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IEmployeeProcessMappingDao;
import com.digitzones.model.EmployeeProcessMapping;
@Repository
public class EmployeeProcessMappingDaoImpl extends CommonDaoImpl<EmployeeProcessMapping>
		implements IEmployeeProcessMappingDao {
	public EmployeeProcessMappingDaoImpl() {
		super(EmployeeProcessMapping.class);
	}
}
