package com.digitzones.service.impl;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitzones.dao.IProductionUnitDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import com.digitzones.service.IProductionUnitService;
@Service
public class ProductionUnitServiceImpl implements IProductionUnitService {
	private IProductionUnitDao productionUnitDao;
	@Autowired
	public void setProductionUnitDao(IProductionUnitDao productionUnitDao) {
		this.productionUnitDao = productionUnitDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return this.productionUnitDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(ProductionUnit obj) {
		productionUnitDao.update(obj);
	}
	@Override
	public ProductionUnit queryByProperty(String name, String value) {
		return productionUnitDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(ProductionUnit obj) {
		return productionUnitDao.save(obj);
	}
	@Override
	public ProductionUnit queryObjById(Serializable id) {
		return productionUnitDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		productionUnitDao.deleteById(id);
	}
	@Override
	public List<ProductionUnit> queryTopProductionUnits() {
		return  productionUnitDao.findByHQL("select pu from ProductionUnit pu where pu.parent is null order by (case code when 'UNCATEGORIZED' then '-1' else code end) desc", new Object[] {});
	}
	@Override
	public Long queryCountOfSubProductionUnit(Serializable pid) {
		return productionUnitDao.findCount("from ProductionUnit pu inner join pu.parent p where p.id=?0 ", new Object[] {pid});
	}
	@Override
	public List<ProductionUnit> queryAllProductionUnits() {
		return this.productionUnitDao.findByHQL("from ProductionUnit pu where pu.code!='UNCATEGORIZED' and pu.parent is not null and pu not in "
				+ "(select p.parent from ProductionUnit p where p.parent is not null)", new Object[] {});
	}
	@Override
	public List<ProductionUnit> queryAllProductionUnitsByMaintenanceStaffId(String code) {
		return this.productionUnitDao.findByHQL("from ProductionUnit pu where pu.code!='UNCATEGORIZED' and pu.parent is not null and pu not in "
				+ "(select p.parent from ProductionUnit p where p.parent is not null) and pu.id not in"
				+ " (select t.productionUnit.id from EmployeeProductionUnitRecord t where t.employeeCode=?0)", new Object[] {code});
	}
	@Override
	public Integer queryGoalOutputByProductionUnitId(Long productionUnitId) {
		return productionUnitDao.queryGoalOutputByProductionUnitId(productionUnitId);
	}
	@Override
	public double queryOeeByProductionUnitId(Long productionUnitId) {
		return productionUnitDao.queryOeeByProductionUnitId(productionUnitId);
	}
	@Override
	public List<ProductionUnit> queryAllLeafProductionUnits() {
		return productionUnitDao.queryAllLeafProductionUnits();
	}
	@Override
	public boolean queryExistSubProductionUnit(Long id) {
		String hql = " from ProductionUnit pu where pu.parent.id=?0";
		Long count = productionUnitDao.findCount(hql, new Object[] {id});
		if(count!=null && count>0) {
			return true;
		}
		return false;
	}
	@Override
	public double queryGoalLostTimeByProductionUnitId(Long productionUnitId) {
		return productionUnitDao.queryGoalLostTimeByProductionUnitId(productionUnitId);
	}
	@Override
	public double queryNgGoalById(Long id) {
		return productionUnitDao.queryNgGoalById(id);
	}
	@Override
	public boolean isExistDevices(Long id) {
		return productionUnitDao.queryDevicesCount(id)>0?true:false;
	}
	@Override
	public boolean isExistEmployees(Long id) {
		return productionUnitDao.queryEmployeesCount(id)>0?true:false;
	}
	@Override
	public ProductionUnit queryParentProductionUnit() {
		return  productionUnitDao.queryParentProductionUnit();
	}
	@Override
	public List<ProductionUnit> queryByParentId(Long parentId) {
		return productionUnitDao.findByHQL("from ProductionUnit pu where pu.parent.id=?0 order by code",new Object[] {parentId});
	}
}
