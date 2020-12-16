package com.digitzones.dao.impl;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.IProductionUnitDao;
import com.digitzones.model.ProductionUnit;
@SuppressWarnings("deprecation")
@Repository
public class ProductionUnitDaoImpl extends CommonDaoImpl<ProductionUnit> implements IProductionUnitDao {
	public ProductionUnitDaoImpl() {
		super(ProductionUnit.class);
	}
	@Override
	public Integer queryGoalOutputByProductionUnitId(Long productionUnitId) {
		String sql = "select goalOutput from PRODUCTIONUNIT where ID=?0";
		Integer goalOutput = (Integer) this.getSession().createNativeQuery(sql).setParameter(0,productionUnitId).uniqueResult();
		return goalOutput==null?0:goalOutput;
	}

	@Override
	public double queryOeeByProductionUnitId(Long productionUnitId) {
		
		String sql = "select goalOee from productionunit p where p.id=?0";
		Object goalOee =  getSession().createNativeQuery(sql)
					.setParameter(0, productionUnitId)
					.uniqueResult();
		return goalOee==null?0:(double)goalOee;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionUnit> queryAllLeafProductionUnits() {
		String sql = "select * from productionunit where id not in (select distinct p.id from PRODUCTIONUNIT  p inner join PRODUCTIONUNIT p0 on p.id=p0.PARENT_ID ) and parent_id is not null";
		SQLQuery<ProductionUnit> query = getSession().createNativeQuery(sql).addEntity(ProductionUnit.class);
		return query.list();
	}
	@Override
	public double queryGoalLostTimeByProductionUnitId(Long productionUnitId) {
		String sql = "select goalLostTime from productionUnit where id = ?0";
		Double result = (Double) getSession().createNativeQuery(sql).setParameter(0, productionUnitId).uniqueResult();
		return result == null?0:result;
	}
	@Override
	public double queryNgGoalById(Long id) {
		String sql = "select goalNG from productionUnit where id = ?0";
		Double result = (Double) getSession().createNativeQuery(sql).setParameter(0, id).uniqueResult();
		return result == null?0:result;
	}
	@Override
	public int queryDevicesCount(Long id) {
		String sql = "select count(id) from Device d where d.productionUnit_id=?0 and d.isDimesUse=1";
		return (int) getSession().createNativeQuery(sql).setParameter(0, id).uniqueResult();
	}
	@Override
	public int queryEmployeesCount(Long id) {
		String sql = "select count(id) from Employee d where d.productionUnit_id=?0";
		return (int) getSession().createNativeQuery(sql).setParameter(0, id).uniqueResult();
	}
	@Override
	public ProductionUnit queryParentProductionUnit() {
		return (ProductionUnit) getSession().createNativeQuery("select * from ProductionUnit pu where pu.parent_id is null ").addEntity(ProductionUnit.class).uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionUnit> queryProductionUnitsByParentId(Long id) {
		return getSession().createNativeQuery("select * from ProductionUnit pu where pu.parent_id=?0").addEntity(ProductionUnit.class)
				.setParameter(0,id).list();
	}
}
