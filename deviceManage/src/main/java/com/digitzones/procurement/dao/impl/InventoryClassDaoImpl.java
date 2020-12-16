package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IInventoryClassDao;
import com.digitzones.procurement.model.InventoryClass;
@Repository
public class InventoryClassDaoImpl extends CommonDaoImpl<InventoryClass> implements IInventoryClassDao {

	public InventoryClassDaoImpl() {
		super(InventoryClass.class);
	}

}
