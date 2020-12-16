package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IEmployeeSchedulingRecordDao;
import com.digitzones.devmgr.model.EmployeeSchedulingRecord;
@Repository
public class EmployeeSchedulingRecordDaoImpl extends CommonDaoImpl<EmployeeSchedulingRecord>
		implements IEmployeeSchedulingRecordDao {
	public EmployeeSchedulingRecordDaoImpl() {
		super(EmployeeSchedulingRecord.class);
	}
}
