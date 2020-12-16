package com.digitzones.app.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.model.ProductionUnit;

public interface IAppProductionUnitDao extends ICommonDao<ProductionUnit>{
	
	public List<ProductionUnit> queryAProductionUnitByParentId(Long Id);

}
