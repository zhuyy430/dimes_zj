package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IVendorDao;
import com.digitzones.procurement.model.Vendor;
import com.digitzones.procurement.service.IVendorService;
@Service
public class VendorServiceImpl implements IVendorService {
	@Autowired
	private IVendorDao vendorDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return vendorDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(Vendor obj) {
		vendorDao.update(obj);
	}
	@Override
	public Vendor queryByProperty(String name, String value) {
		return vendorDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(Vendor obj) {
		return vendorDao.save(obj);
	}
	@Override
	public Vendor queryObjById(Serializable id) {
		return vendorDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		vendorDao.deleteById(id);
	}
	@Override
	public List<Vendor> queryAllVendor() {
		return vendorDao.findAll();
	}
}
