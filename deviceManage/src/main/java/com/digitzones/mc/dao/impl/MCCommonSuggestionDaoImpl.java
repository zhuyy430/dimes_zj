package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCCommonSuggestionDao;
import com.digitzones.mc.model.MCCommonSuggestion;
@Repository
public class MCCommonSuggestionDaoImpl extends CommonDaoImpl<MCCommonSuggestion> implements IMCCommonSuggestionDao{
	public MCCommonSuggestionDaoImpl() {
		super(MCCommonSuggestion.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCCommonSuggestion> queryAllSuggestByTypeCode(String code) {
		String sql = "select sug.id,sug.text,sug.parentsId from MC_COMMONSUGGESTION sug inner join "
				+ "MC_COMMONTYPE typ on sug.parentsId=typ.id where typ.code=?0";
		return getSession().createSQLQuery(sql).addEntity(MCCommonSuggestion.class).setParameter(0, code).list();
	}
}
