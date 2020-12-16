package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IMom_orderdetailDao;
import com.digitzones.procurement.model.Mom_recorddetail;
import com.digitzones.procurement.service.IMom_orderdetailService;
import org.springframework.util.CollectionUtils;

@Service
public class Mom_orderdetailServiceImpl implements IMom_orderdetailService {

	@Autowired
	private IMom_orderdetailDao mom_orderdetailDao;
	
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		// TODO Auto-generated method stub
		return mom_orderdetailDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Mom_recorddetail obj) {
		// TODO Auto-generated method stub
		mom_orderdetailDao.update(obj);
	}

	@Override
	public Mom_recorddetail queryByProperty(String name, String value) {
		// TODO Auto-generated method stub
		return mom_orderdetailDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Mom_recorddetail obj) {
		// TODO Auto-generated method stub
		return mom_orderdetailDao.save(obj);
	}

	@Override
	public Mom_recorddetail queryObjById(Serializable id) {
		// TODO Auto-generated method stub
		return mom_orderdetailDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		// TODO Auto-generated method stub
		mom_orderdetailDao.deleteById(id);
	}

	@Override
	public Mom_recorddetail queryMom_recorddetailByCodeAndSeq(String code, String seq) {
		String hql = "from Mom_recorddetail m where m.mocode=?0 and m.sortSeq=?1 ";
		List<Mom_recorddetail> list = this.mom_orderdetailDao.findByHQL(hql, new Object[]{code, seq});
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
