package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IMom_orderdetailStatusDao;
import com.digitzones.procurement.model.Mom_recorddetailStatus;

@Repository
public class Mom_orderdetailStatusDaoImpl extends CommonDaoImpl<Mom_recorddetailStatus> implements IMom_orderdetailStatusDao {
	public Mom_orderdetailStatusDaoImpl() {
		super(Mom_recorddetailStatus.class);
	}
}
