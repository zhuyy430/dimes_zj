package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IWarehouseDao;
import com.digitzones.devmgr.model.Warehouse;
import com.digitzones.devmgr.service.IWarehouseService;
import com.digitzones.model.Pager;
@Service
public class WarehouseServiceImpl implements IWarehouseService {
	@Autowired
	private IWarehouseDao warehouseDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return warehouseDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Warehouse obj) {
		warehouseDao.update(obj);
	}

	@Override
	public Warehouse queryByProperty(String name, String value) {
		return warehouseDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Warehouse obj) {
		return warehouseDao.save(obj);
	}

	@Override
	public Warehouse queryObjById(Serializable id) {
		return warehouseDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		warehouseDao.deleteById(id);
	}
}
