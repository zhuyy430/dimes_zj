package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IPO_PodetailsDao;
import com.digitzones.procurement.model.PO_Podetails;
import com.digitzones.procurement.service.IPO_PodetailsService;
@Service
public class PO_PodetailsServiceImpl implements IPO_PodetailsService {
	@Autowired
	private IPO_PodetailsDao po_PodetailsDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return po_PodetailsDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(PO_Podetails obj) {
		po_PodetailsDao.update(obj);
	}

	@Override
	public PO_Podetails queryByProperty(String name, String value) {
		return po_PodetailsDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(PO_Podetails obj) {
		return po_PodetailsDao.save(obj);
	}

	@Override
	public PO_Podetails queryObjById(Serializable id) {
		return po_PodetailsDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		po_PodetailsDao.deleteById(id);
	}

	@Override
	public List<PO_Podetails> queryPO_PodetailsByPOID(String POID) {
		return po_PodetailsDao.findByHQL("from PO_Podetails p where p.cpoId=?0", new Object [] {POID});
	}
	/**
	 * 查找ids表示的订单详情
	 * @param ids 多个订单详情id，以逗号间隔
	 * @return
	 */
	@Override
	public List<PO_Podetails> queryByIds(List<Integer> ids) {
		return po_PodetailsDao.findByHQL("from PO_Podetails po where po.id in ?0",new Object[]{ids});
	}
}
