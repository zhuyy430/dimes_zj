package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ISecureEnvironmentTypeDao;
import com.digitzones.model.SecureEnvironmentType;
@Repository
public class SecureEnvironmentTypeDaoImpl extends CommonDaoImpl<SecureEnvironmentType> implements ISecureEnvironmentTypeDao {

	public SecureEnvironmentTypeDaoImpl() {
		super(SecureEnvironmentType.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer queryCountByDayAndTypeId(int year, int month, int day, Long typeId) {
		String sql = "select count(id) from SECUREENVIRONMENTRECORD q where year(currentDate)=?0 and month(currentDate)=?1"
				+ " and day(currentDate)=?2 and typeId=?3";
		Integer count = (Integer) this.getSession().createSQLQuery(sql)
						 .setParameter(0, year)
						 .setParameter(1, month)
						 .setParameter(2, day)
						 .setParameter(3,typeId)
						 .uniqueResult();
		if(count!=null) {
			return count;
		}
		return 0;
	}
}
