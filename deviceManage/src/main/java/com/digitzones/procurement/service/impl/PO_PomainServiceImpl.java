package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IPO_PomainDao;
import com.digitzones.procurement.model.PO_Pomain;
import com.digitzones.procurement.service.IPO_PomainService;
@Service
public class PO_PomainServiceImpl implements IPO_PomainService {
	@Autowired
	private IPO_PomainDao po_PomainDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return po_PomainDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(PO_Pomain obj) {
		po_PomainDao.update(obj);
	}
	@Override
	public PO_Pomain queryByProperty(String name, String value) {
		return po_PomainDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(PO_Pomain obj) {
		return po_PomainDao.save(obj);
	}
	@Override
	public PO_Pomain queryObjById(Serializable id) {
		return po_PomainDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		po_PomainDao.deleteById(id);
	}
	/**
	 * 根据入库申请单单号查找采购订单
	 * @param formNo
	 * @return
	 */
	@Override
	public List<PO_Pomain> queryByWarehousingApplicationFormNoAndBarCode(String formNo,String barCode) {
		return po_PomainDao.queryByWarehousingApplicationFormNoAndBarCode(formNo,barCode);
	}
}
