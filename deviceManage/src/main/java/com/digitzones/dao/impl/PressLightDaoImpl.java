package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IPressLightDao;
import com.digitzones.model.PressLight;
@Repository
public class PressLightDaoImpl extends CommonDaoImpl<PressLight> implements IPressLightDao {

	public PressLightDaoImpl() {
		super(PressLight.class);
	}
}
