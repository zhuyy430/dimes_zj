package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProductionUnitBoardDao;
import com.digitzones.model.ProductionUnitBoard;
@Repository
public class ProductionUnitBoardDaoImpl extends CommonDaoImpl<ProductionUnitBoard>
		implements IProductionUnitBoardDao {

	public ProductionUnitBoardDaoImpl() {
		super(ProductionUnitBoard.class);
	}

}
