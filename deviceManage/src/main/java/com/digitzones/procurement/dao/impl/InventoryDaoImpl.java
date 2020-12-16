package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IInventoryDao;
import com.digitzones.procurement.model.Inventory;
@Repository
public class InventoryDaoImpl extends CommonDaoImpl<Inventory> implements IInventoryDao {

	public InventoryDaoImpl() {
		super(Inventory.class);
	}

}
