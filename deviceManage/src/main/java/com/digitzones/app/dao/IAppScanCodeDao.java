package com.digitzones.app.dao;

import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;

import com.digitzones.dao.ICommonDao;

public interface IAppScanCodeDao extends ICommonDao<T>{
	public List<?> findSearch(String sql);

}
