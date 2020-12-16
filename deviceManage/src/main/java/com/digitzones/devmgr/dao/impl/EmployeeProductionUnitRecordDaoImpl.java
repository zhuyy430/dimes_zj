package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IEmployeeProductionUnitRecordDao;
import com.digitzones.devmgr.model.EmployeeProductionUnitRecord;
@Repository
public class EmployeeProductionUnitRecordDaoImpl extends CommonDaoImpl<EmployeeProductionUnitRecord>
		implements IEmployeeProductionUnitRecordDao {
	public EmployeeProductionUnitRecordDaoImpl() {
		super(EmployeeProductionUnitRecord.class);
	}
}
