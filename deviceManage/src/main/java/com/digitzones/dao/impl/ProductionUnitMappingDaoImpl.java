package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProductionUnitMappingDao;
import com.digitzones.model.ProductionUnitMapping;
@Repository
public class ProductionUnitMappingDaoImpl extends CommonDaoImpl<ProductionUnitMapping>
		implements IProductionUnitMappingDao {

	public ProductionUnitMappingDaoImpl() {
		super(ProductionUnitMapping.class);
	}

}
