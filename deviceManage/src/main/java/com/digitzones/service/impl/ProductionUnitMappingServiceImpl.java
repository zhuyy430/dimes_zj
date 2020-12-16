package com.digitzones.service.impl;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitzones.dao.IProductionUnitMappingDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnitMapping;
import com.digitzones.service.IProductionUnitMappingService;
@Service
public class ProductionUnitMappingServiceImpl implements IProductionUnitMappingService {
	@Autowired
	private IProductionUnitMappingDao productionUnitMappingDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return productionUnitMappingDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(ProductionUnitMapping obj) {
		productionUnitMappingDao.update(obj);
	}

	@Override
	public ProductionUnitMapping queryByProperty(String name, String value) {
		return productionUnitMappingDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(ProductionUnitMapping obj) {
		return productionUnitMappingDao.save(obj);
	}
	@Override
	public ProductionUnitMapping queryObjById(Serializable id) {
		return productionUnitMappingDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		productionUnitMappingDao.deleteById(id);
	}
}
