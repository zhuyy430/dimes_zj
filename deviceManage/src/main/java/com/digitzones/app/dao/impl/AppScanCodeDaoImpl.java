package com.digitzones.app.dao.impl;

import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.stereotype.Repository;

import com.digitzones.app.dao.IAppScanCodeDao;
import com.digitzones.dao.impl.CommonDaoImpl;
@Repository
public class AppScanCodeDaoImpl extends CommonDaoImpl<T> implements IAppScanCodeDao {
	public AppScanCodeDaoImpl() {
		super(T.class);
	}
	@SuppressWarnings({ "deprecation"})
	@Override
	public List<?> findSearch(String sql) {
	List list = getSession().createSQLQuery(sql)
			.list();
	
	return list;
	}

}
