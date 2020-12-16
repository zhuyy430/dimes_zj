package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenancePlanDao;
import com.digitzones.devmgr.model.MaintenancePlan;
@Repository
public class MaintenancePlanDaoImpl extends CommonDaoImpl<MaintenancePlan> implements IMaintenancePlanDao {
	public MaintenancePlanDaoImpl() {
		super(MaintenancePlan.class);
	}
	
}
