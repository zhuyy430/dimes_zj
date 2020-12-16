package com.digitzones.app.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.app.dao.IAppProductionUnitDao;
import com.digitzones.app.model.TaskPic;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.model.ProductionUnit;
@Repository
public class AppProductionUnitDaoImpl extends CommonDaoImpl<ProductionUnit> implements IAppProductionUnitDao {

	public AppProductionUnitDaoImpl() {
		super(ProductionUnit.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<ProductionUnit> queryAProductionUnitByParentId(Long Id) {
		String sql = "select p.id,p.code,p.disabled,p.name,p.note,p.goalLostTime,p.goalNg,p.goalOee,p.goalOutput,p.PARENT_ID "
					+" from PRODUCTIONUNIT p where p.PARENT_ID = ?0 and p.disabled =?1";
		List a = getSession().createSQLQuery(sql).setParameter(0, Id).setParameter(1, false)
				.addEntity(ProductionUnit.class).list();
		return a;
	}

}
