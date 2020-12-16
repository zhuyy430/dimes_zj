package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ICheckingPlanDao;
import com.digitzones.devmgr.model.CheckingPlan;
@Repository
public class CheckingPlanDaoImpl extends CommonDaoImpl<CheckingPlan> implements ICheckingPlanDao {
	public CheckingPlanDaoImpl() {
		super(CheckingPlan.class);
	}
}
