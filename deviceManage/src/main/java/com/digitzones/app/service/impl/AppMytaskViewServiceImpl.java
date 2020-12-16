package com.digitzones.app.service.impl;

import com.digitzones.app.dao.IAppMytaskViewDao;
import com.digitzones.app.model.MytaskView;
import com.digitzones.app.service.IAppMytaskViewService;
import com.digitzones.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
@Service
public class AppMytaskViewServiceImpl implements IAppMytaskViewService{
	@Autowired
	IAppMytaskViewDao appMytaskViewDao;
	
	@Override
	public List<MytaskView> queryMySendOutTaskByUsercode(String code) {
		return appMytaskViewDao.queryMySendOutTaskByUsercode(code);
	}

	@Override
	public List<MytaskView> queryMyReceiveTaskByUsercode(String code) {
		return appMytaskViewDao.queryMyReceiveTaskByUsercode(code);
	}

	@Override
	public List<MytaskView> queryMyCompleteTaskByUsercode(String code) {
		return appMytaskViewDao.queryMyCompleteTaskByUsercode(code);
	}

	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return appMytaskViewDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MytaskView obj) {
		appMytaskViewDao.update(obj);
	}

	@Override
	public MytaskView queryByProperty(String name, String value) {
		return appMytaskViewDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MytaskView obj) {
		// TODO Auto-generated method stub
		return appMytaskViewDao.save(obj);
	}

	@Override
	public MytaskView queryObjById(Serializable id) {
		return appMytaskViewDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		appMytaskViewDao.deleteById(id);
	}
}
