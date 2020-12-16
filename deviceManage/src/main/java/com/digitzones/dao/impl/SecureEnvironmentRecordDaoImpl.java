package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ISecureEnvironmentRecordDao;
import com.digitzones.model.SecureEnvironmentRecord;
@Repository
public class SecureEnvironmentRecordDaoImpl extends CommonDaoImpl<SecureEnvironmentRecord> implements ISecureEnvironmentRecordDao {
	public SecureEnvironmentRecordDaoImpl() {
		super(SecureEnvironmentRecord.class);
	}
}
