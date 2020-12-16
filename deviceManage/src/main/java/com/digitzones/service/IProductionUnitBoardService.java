package com.digitzones.service;

import com.digitzones.model.ProductionUnitBoard;
/**
 * 产线与图片的关联service
 * @author zdq
 * 2018年9月7日
 */
public interface IProductionUnitBoardService extends ICommonService<ProductionUnitBoard> {

	/**
	 * 根据生产单元Id获取信息
	 * @param productionUnitId
	 * @return
	 */
	public ProductionUnitBoard queryProductionUnitBoardByProductionUnitId(Long productionUnitId);
}
