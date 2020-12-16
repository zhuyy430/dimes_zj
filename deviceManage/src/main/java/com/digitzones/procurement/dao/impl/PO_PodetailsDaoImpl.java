package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IPO_PodetailsDao;
import com.digitzones.procurement.model.PO_Podetails;
@Repository
public class PO_PodetailsDaoImpl extends CommonDaoImpl<PO_Podetails> implements IPO_PodetailsDao {

	public PO_PodetailsDaoImpl() {
		super(PO_Podetails.class);
	}

}
