package com.digitzones.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IProductionUnitBoardDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnitBoard;
import com.digitzones.service.IProductionUnitBoardService;
@Service
public class ProductionUnitBoardServiceImpl implements IProductionUnitBoardService {
	@Autowired
	private IProductionUnitBoardDao ProductionUnitBoardDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return ProductionUnitBoardDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ProductionUnitBoard obj) {
		ProductionUnitBoardDao.update(obj);
	}

	@Override
	public ProductionUnitBoard queryByProperty(String name, String value) {
		return ProductionUnitBoardDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ProductionUnitBoard obj) {
		return ProductionUnitBoardDao.save(obj);
	}

	@Override
	public ProductionUnitBoard queryObjById(Serializable id) {
		return ProductionUnitBoardDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		ProductionUnitBoardDao.deleteById(id);
	}

	@Override
	public ProductionUnitBoard queryProductionUnitBoardByProductionUnitId(Long productionUnitId) {
		List<ProductionUnitBoard> pList = ProductionUnitBoardDao.findByHQL("from ProductionUnitBoard p where p.productionUnit.id=?0", new Object[] {productionUnitId});
		if(pList!=null&&!pList.isEmpty()){
			return pList.get(0);
		}
		return null;
	}
}
