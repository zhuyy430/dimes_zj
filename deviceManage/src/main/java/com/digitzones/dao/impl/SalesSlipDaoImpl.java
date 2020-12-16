package com.digitzones.dao.impl;

import com.digitzones.dao.ISalesSlipDao;
import com.digitzones.model.SalesSlip;
import org.springframework.stereotype.Repository;
@Repository
public class SalesSlipDaoImpl extends CommonDaoImpl<SalesSlip> implements ISalesSlipDao {
    public SalesSlipDaoImpl() {
        super(SalesSlip.class);
    }
}
