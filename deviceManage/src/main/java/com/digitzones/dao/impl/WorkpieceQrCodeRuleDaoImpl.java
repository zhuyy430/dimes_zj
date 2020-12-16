package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkpieceQrCodeRuleDao;
import com.digitzones.model.WorkpieceQrCodeRule;
@Repository
public class WorkpieceQrCodeRuleDaoImpl extends CommonDaoImpl<WorkpieceQrCodeRule> implements IWorkpieceQrCodeRuleDao {

	public WorkpieceQrCodeRuleDaoImpl() {
		super(WorkpieceQrCodeRule.class);
	}
}
