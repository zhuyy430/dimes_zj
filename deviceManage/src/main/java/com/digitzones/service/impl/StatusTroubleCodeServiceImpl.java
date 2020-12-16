package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IStatusTroubleCodeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.StatusTroubleCode;
import com.digitzones.service.IStatusTroubleCodeService;
@Service
public class StatusTroubleCodeServiceImpl implements IStatusTroubleCodeService {
	private IStatusTroubleCodeDao statusTroubleCodeDao;
	@Autowired
	public void setStatusTroubleCodeDao(IStatusTroubleCodeDao statusTroubleCodeDao) {
		this.statusTroubleCodeDao = statusTroubleCodeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return statusTroubleCodeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(StatusTroubleCode obj) {
		statusTroubleCodeDao.update(obj);
	}
	@Override
	public StatusTroubleCode queryByProperty(String name, String value) {
		return statusTroubleCodeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(StatusTroubleCode obj) {
		return statusTroubleCodeDao.save(obj);
	}
	@Override
	public StatusTroubleCode queryObjById(Serializable id) {
		return statusTroubleCodeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		statusTroubleCodeDao.deleteById(id);
	}
}
