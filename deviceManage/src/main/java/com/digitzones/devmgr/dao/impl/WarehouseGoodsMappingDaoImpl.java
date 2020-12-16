package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IWarehouseGoodsMappingDao;
import com.digitzones.devmgr.model.WarehouseGoodsMapping;
@Repository
public class WarehouseGoodsMappingDaoImpl extends CommonDaoImpl<WarehouseGoodsMapping> implements IWarehouseGoodsMappingDao {
	public WarehouseGoodsMappingDaoImpl() {
		super(WarehouseGoodsMapping.class);
	}
}
