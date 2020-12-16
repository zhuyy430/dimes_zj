package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ICheckingPlanRecordItemDao;
import com.digitzones.devmgr.model.CheckingPlanRecordItem;
@Repository
public class CheckingPlanRecordItemDaoImpl extends CommonDaoImpl<CheckingPlanRecordItem> implements ICheckingPlanRecordItemDao {
	public CheckingPlanRecordItemDaoImpl() {
		super(CheckingPlanRecordItem.class);
	}
}
