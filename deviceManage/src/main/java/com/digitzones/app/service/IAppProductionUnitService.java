package com.digitzones.app.service;

import java.util.List;

import com.digitzones.model.ProductionUnit;
import com.digitzones.service.ICommonService;

public interface IAppProductionUnitService extends ICommonService<ProductionUnit>{
	
	/**
	 * 查询所有生产单元
	 * @return
	 */
	public List<ProductionUnit> queryAllProductionUnit();
	/**
	 * 根据父单元Id查询子单元
	 * @return
	 */
	public List<ProductionUnit> queryAProductionUnitByParentId(Long Id);

}
