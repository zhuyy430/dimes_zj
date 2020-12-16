package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IMeasuringToolTypeDao;
import com.digitzones.model.EquipmentType;
@Repository
public class MeasuringToolTypeDaoImpl extends CommonDaoImpl<EquipmentType> implements IMeasuringToolTypeDao {
	public MeasuringToolTypeDaoImpl() {
		super(EquipmentType.class);
	}
}
