package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IVendorClassDao;
import com.digitzones.procurement.model.VendorClass;
@Repository
public class VendorClassDaoImpl extends CommonDaoImpl<VendorClass> implements IVendorClassDao {
    public VendorClassDaoImpl() {
        super(VendorClass.class);
    }
}
