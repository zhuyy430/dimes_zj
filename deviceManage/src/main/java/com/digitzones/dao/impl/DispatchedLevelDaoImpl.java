package com.digitzones.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IDispatchedLevelDao;
import com.digitzones.model.DispatchedLevel;
@Repository
public class DispatchedLevelDaoImpl extends CommonDaoImpl<DispatchedLevel> implements IDispatchedLevelDao {
	public DispatchedLevelDaoImpl() {
		super(DispatchedLevel.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<DispatchedLevel> queryDispatchedLevelWithStatus(String status) {
		// TODO Auto-generated method stub
		String sql = "select * from DispatchedLevel d where  d.orderType=?0 order by d.timing";
		return getSession().createSQLQuery(sql).setParameter(0, status)
				.addEntity(DispatchedLevel.class).list();
	}
}
