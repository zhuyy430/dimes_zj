package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ISecureEnvironmentGradeDao;
import com.digitzones.model.SecureEnvironmentGrade;
@Repository
public class SecureEnvironmentGradeDaoImpl extends CommonDaoImpl<SecureEnvironmentGrade> implements ISecureEnvironmentGradeDao {

	public SecureEnvironmentGradeDaoImpl() {
		super(SecureEnvironmentGrade.class);
	}
}
