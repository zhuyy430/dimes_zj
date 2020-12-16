package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.INGProcessMethodDao;
import com.digitzones.model.NGProcessMethod;
import com.digitzones.model.Pager;
import com.digitzones.service.INGProcessMethodService;
@Service
public class NGProcessMethodServiceImpl implements INGProcessMethodService {
	private INGProcessMethodDao ngProcessMethodDao;
	@Autowired
	public void setNgProcessMethodDao(INGProcessMethodDao ngProcessMethodDao) {
		this.ngProcessMethodDao = ngProcessMethodDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return ngProcessMethodDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(NGProcessMethod obj) {
		ngProcessMethodDao.update(obj);
	}

	@Override
	public NGProcessMethod queryByProperty(String name, String value) {
		return ngProcessMethodDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(NGProcessMethod obj) {
		return ngProcessMethodDao.save(obj);
	}

	@Override
	public NGProcessMethod queryObjById(Serializable id) {
		return ngProcessMethodDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		ngProcessMethodDao.deleteById(id);
	}

	@Override
	public List<NGProcessMethod> queryNGProcessMethodsByNGRecordId(Long ngRecordId) {
		return this.ngProcessMethodDao.findByHQL("from NGProcessMethod m where m.ngRecord.id=?0", new Object[] {ngRecordId});
	}
}
