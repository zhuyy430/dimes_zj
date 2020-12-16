package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.ICheckingPlanRecordItemDao;
import com.digitzones.devmgr.model.CheckingPlanRecordItem;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemService;
import com.digitzones.model.Pager;
@Service
public class CheckingPlanRecordItemServiceImpl implements ICheckingPlanRecordItemService {
	@Autowired
	private ICheckingPlanRecordItemDao checkingPlanRecordDao;
	@Override
	public Pager<?> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return checkingPlanRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(CheckingPlanRecordItem obj) {
		checkingPlanRecordDao.update(obj);
	}

	@Override
	public CheckingPlanRecordItem queryByProperty(String name, String value) {
		return checkingPlanRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(CheckingPlanRecordItem obj) {
		return checkingPlanRecordDao.save(obj);
	}

	@Override
	public CheckingPlanRecordItem queryObjById(Serializable id) {
		return checkingPlanRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		checkingPlanRecordDao.deleteById(id);
	}
	@Override
	public List<CheckingPlanRecordItem> queryByCheckingPlanRecordId(Long checkingPlanRecordId) {
		return checkingPlanRecordDao.findByHQL("from CheckingPlanRecordItem item where item.checkingPlanRecord.id=?0", new Object[] {checkingPlanRecordId});
	}
	@Override
	public int queryResultCountByCheckingPlanRecordId(Long checkingPlanRecordId) {
		List<CheckingPlanRecordItem> list= checkingPlanRecordDao.findByHQL("from CheckingPlanRecordItem item where item.checkingPlanRecord.id=?0 and (item.result is not null or item.result!='')", new Object[] {checkingPlanRecordId});	
		return list.size();
	}
}
