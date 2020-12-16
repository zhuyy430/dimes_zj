package com.digitzones.app.service.impl;

import com.digitzones.app.dao.IAppProductionUnitDao;
import com.digitzones.app.service.IAppProductionUnitService;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
@Service
public class AppProductionUnitServiceImpl implements IAppProductionUnitService {

	@Autowired
	IAppProductionUnitDao productionUnitDao; 
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return null;
	}
	@Override
	public void updateObj(ProductionUnit obj) {

	}
	@Override
	public ProductionUnit queryByProperty(String name, String value) {
		return null;
	}

	@Override
	public Serializable addObj(ProductionUnit obj) {
		return null;
	}

	@Override
	public ProductionUnit queryObjById(Serializable id) {
		return null;
	}

	@Override
	public void deleteObj(Serializable id) {

	}

	/*@Override
	public void deleteObj(Long id) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public List<ProductionUnit> queryAllProductionUnit() {
		return  productionUnitDao.findByHQL("from ProductionUnit pu where pu.parent is null order by pu.code", new Object[] {});
	}

	@Override
	public List<ProductionUnit> queryAProductionUnitByParentId(Long Id) {
		return  productionUnitDao.queryAProductionUnitByParentId(Id);
	}
}
