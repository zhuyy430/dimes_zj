package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IWarehouseGoodsMappingDao;
import com.digitzones.devmgr.model.WarehouseGoodsMapping;
import com.digitzones.devmgr.service.IWarehouseGoodsMappingService;
import com.digitzones.model.Pager;
@Service
public class WarehouseGoodsMappingServiceImpl implements IWarehouseGoodsMappingService {
	@Autowired
	private IWarehouseGoodsMappingDao warehouseGoodsMappingDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return warehouseGoodsMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(WarehouseGoodsMapping obj) {
		warehouseGoodsMappingDao.update(obj);
	}

	@Override
	public WarehouseGoodsMapping queryByProperty(String name, String value) {
		return warehouseGoodsMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(WarehouseGoodsMapping obj) {
		return warehouseGoodsMappingDao.save(obj);
	}

	@Override
	public WarehouseGoodsMapping queryObjById(Serializable id) {
		return warehouseGoodsMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		warehouseGoodsMappingDao.deleteById(id);
	}
}
