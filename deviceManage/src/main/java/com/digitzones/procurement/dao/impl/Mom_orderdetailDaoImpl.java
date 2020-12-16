package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IMom_orderdetailDao;
import com.digitzones.procurement.model.Mom_recorddetail;

@Repository
public class Mom_orderdetailDaoImpl extends CommonDaoImpl<Mom_recorddetail> implements IMom_orderdetailDao {
	public Mom_orderdetailDaoImpl() {
		super(Mom_recorddetail.class);
	}
}
