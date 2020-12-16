package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceStaffStatusRecordDao;
import com.digitzones.devmgr.model.MaintenanceStaffStatusRecord;
@Repository
public class MaintenanceStaffStatusRecordDaoImpl extends CommonDaoImpl<MaintenanceStaffStatusRecord> implements IMaintenanceStaffStatusRecordDao {
	public MaintenanceStaffStatusRecordDaoImpl() {
		super(MaintenanceStaffStatusRecord.class);
	}
}
