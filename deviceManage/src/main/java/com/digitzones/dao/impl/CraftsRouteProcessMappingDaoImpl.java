package com.digitzones.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.ICraftsRouteProcessMappingDao;
import com.digitzones.model.CraftsRouteProcessMapping;
@Repository
public class CraftsRouteProcessMappingDaoImpl extends CommonDaoImpl<CraftsRouteProcessMapping> implements ICraftsRouteProcessMappingDao {
	public CraftsRouteProcessMappingDaoImpl() {
		super(CraftsRouteProcessMapping.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteByCraftsIdAndProcessId(Long processId) {
		getSession().createSQLQuery("delete from CRAFTSROUTE_PROCESS where id=?0 ")
		.setParameter(0, processId)
		.executeUpdate();
	}

	@Override
	public List<CraftsRouteProcessMapping> queryByCraftsId(String craftsId) {
		return this.findByHQL("from CraftsRouteProcessMapping wpm where wpm.craftsRoute.id=?0", new Object[] {craftsId});
	}
}
