package com.digitzones.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IPowerDao;
import com.digitzones.model.Power;
@Repository
public class PowerDaoImpl extends CommonDaoImpl<Power> implements IPowerDao {
	public PowerDaoImpl() {
		super(Power.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Map<String,Object>> queryAllGroup() {
		String sql = "select distinct _group,note from power where _group is not null";
		return getSession().createNativeQuery(sql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
	}
}
