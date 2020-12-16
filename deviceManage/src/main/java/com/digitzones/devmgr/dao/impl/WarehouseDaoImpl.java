package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IWarehouseDao;
import com.digitzones.devmgr.model.Warehouse;
@Repository
public class WarehouseDaoImpl extends CommonDaoImpl<Warehouse> implements IWarehouseDao {
	public WarehouseDaoImpl() {
		super(Warehouse.class);
	}
}
