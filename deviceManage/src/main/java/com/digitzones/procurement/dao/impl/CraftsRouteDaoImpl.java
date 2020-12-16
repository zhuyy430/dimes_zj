package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.ICraftsRouteDao;
import com.digitzones.procurement.model.CraftsRoute;
@Repository
public class CraftsRouteDaoImpl extends CommonDaoImpl<CraftsRoute> implements ICraftsRouteDao {

	public CraftsRouteDaoImpl() {
		super(CraftsRoute.class);
	}

}
