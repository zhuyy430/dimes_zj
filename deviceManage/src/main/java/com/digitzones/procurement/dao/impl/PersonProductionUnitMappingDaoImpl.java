package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IPersonProductionUnitMappingDao;
import com.digitzones.procurement.model.PersonProductionUnitMapping;
@Repository
public class PersonProductionUnitMappingDaoImpl extends CommonDaoImpl<PersonProductionUnitMapping> implements IPersonProductionUnitMappingDao {

	public PersonProductionUnitMappingDaoImpl() {
		super(PersonProductionUnitMapping.class);
	}

}
