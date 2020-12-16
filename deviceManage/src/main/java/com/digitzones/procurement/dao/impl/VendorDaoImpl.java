package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IVendorDao;
import com.digitzones.procurement.model.Vendor;
@Repository
public class VendorDaoImpl extends CommonDaoImpl<Vendor> implements IVendorDao {

	public VendorDaoImpl() {
		super(Vendor.class);
	}

}
