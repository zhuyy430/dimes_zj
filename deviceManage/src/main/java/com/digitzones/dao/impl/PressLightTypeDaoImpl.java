package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IPressLightTypeDao;
import com.digitzones.model.PressLightType;
@Repository
public class PressLightTypeDaoImpl extends CommonDaoImpl<PressLightType> implements IPressLightTypeDao {

	public PressLightTypeDaoImpl() {
		super(PressLightType.class);
	}
}
