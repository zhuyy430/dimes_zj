package com.digitzones.procurement.service.impl;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IMom_orderdetailStatusDao;
import com.digitzones.procurement.model.Mom_recorddetail;
import com.digitzones.procurement.model.Mom_recorddetailStatus;
import com.digitzones.procurement.service.IMom_orderdetailStatusService;
@Service
public class Mom_orderdetailStatusServiceImpl implements IMom_orderdetailStatusService {

	@Autowired
	private IMom_orderdetailStatusDao mom_orderdetaiStatuslDao;

	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		// TODO Auto-generated method stub
		return mom_orderdetaiStatuslDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Mom_recorddetailStatus obj) {
		// TODO Auto-generated method stub
		mom_orderdetaiStatuslDao.update(obj);
	}

	@Override
	public Mom_recorddetailStatus queryByProperty(String name, String value) {
		// TODO Auto-generated method stub
		return mom_orderdetaiStatuslDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Mom_recorddetailStatus obj) {
		// TODO Auto-generated method stub
		return mom_orderdetaiStatuslDao.save(obj);
	}

	@Override
	public Mom_recorddetailStatus queryObjById(Serializable id) {
		// TODO Auto-generated method stub
		return mom_orderdetaiStatuslDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		// TODO Auto-generated method stub
		mom_orderdetaiStatuslDao.deleteById(id);
	}
	
}
