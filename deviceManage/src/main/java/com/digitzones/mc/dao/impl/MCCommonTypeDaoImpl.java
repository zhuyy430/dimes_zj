package com.digitzones.mc.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCCommonTypeDao;
import com.digitzones.mc.model.MCCommonType;
@Repository
public class MCCommonTypeDaoImpl extends CommonDaoImpl<MCCommonType> implements IMCCommonTypeDao{
	public MCCommonTypeDaoImpl() {
		super(MCCommonType.class);
	}
}
