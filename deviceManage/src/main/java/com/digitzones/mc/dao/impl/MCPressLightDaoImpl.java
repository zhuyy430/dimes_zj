package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCPressLightDao;
import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightType;
@Repository
public class MCPressLightDaoImpl extends CommonDaoImpl<PressLightType> implements IMCPressLightDao {

	public MCPressLightDaoImpl() {
		super(PressLightType.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<PressLightType> queryAllTypeByParentCode(String pcode) {
		String sql="select pl.id,pl.code,pl.disabled,pl.name,pl.note,pl.level,pl.PARENT_ID,pl.text,pl.basicCode "
				+ " from PRESSLIGHTTYPE pl where pl.disabled=?0 and pl.basicCode=?1";
		return getSession().createSQLQuery(sql)
				.setParameter(0, false)
				.setParameter(1, pcode)
				.addEntity(PressLightType.class)
				.list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<PressLight> queryAllPressLight() {
		String sql="select pl.* from PRESSLIGHT pl ";
		return getSession().createSQLQuery(sql)
				.addEntity(PressLight.class)
				.list();
	}
	
}
