package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.dao.IWorkpieceQrCodeRuleDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkpieceQrCodeRule;
import com.digitzones.service.IWorkpieceQrCodeRuleService;
@Service
public class WorkpieceQrCodeRuleServiceImpl implements IWorkpieceQrCodeRuleService {
	@Autowired
	private IWorkpieceQrCodeRuleDao workpieceQrCodeRuleDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workpieceQrCodeRuleDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(WorkpieceQrCodeRule obj) {
		List<WorkpieceQrCodeRule> list = queryByPrinterIp(obj.getPrinterIp(), true);
		if(!CollectionUtils.isEmpty(list)) {
			for(WorkpieceQrCodeRule rule : list) {
				rule.setEnabled(false);
				workpieceQrCodeRuleDao.update(rule);
			}
		}
		obj.setEnabled(true);
		obj.setId(null);
		obj.setCreateDate(new Date());
		obj.setSended(true);
		workpieceQrCodeRuleDao.save(obj);
	}
	@Override
	public WorkpieceQrCodeRule queryByProperty(String name, String value) {
		return workpieceQrCodeRuleDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(WorkpieceQrCodeRule obj) {
		obj.setEnabled(true);
		return workpieceQrCodeRuleDao.save(obj);
	}
	@Override
	public WorkpieceQrCodeRule queryObjById(Serializable id) {
		return workpieceQrCodeRuleDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		workpieceQrCodeRuleDao.deleteById(id);
	}

	@Override
	public List<WorkpieceQrCodeRule> queryByPrinterIp(String printerIp,boolean status) {
		List<WorkpieceQrCodeRule> list = workpieceQrCodeRuleDao.findByHQL("from WorkpieceQrCodeRule w where w.enabled=?0 and w.printerIp=?1",new Object[] {status,printerIp});
		return list;
	}
	@Override
	public void updateWorkpieceQRCodeRule(WorkpieceQrCodeRule workpieceQrCodeRule) {
		workpieceQrCodeRuleDao.update(workpieceQrCodeRule);
	}
}
