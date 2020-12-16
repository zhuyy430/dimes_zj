package com.digitzones.mc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCCommonSuggestionDao;
import com.digitzones.mc.model.MCCommonSuggestion;
import com.digitzones.mc.service.IMCCommonSuggestionService;
import com.digitzones.model.Pager;
@Service
public class MCCommonSuggestionServiceImpl implements IMCCommonSuggestionService {
	@Autowired
	private IMCCommonSuggestionDao MCWorkFlowSuggestionDao;

	@Override
	public List<MCCommonSuggestion> queryAllSuggestionByTypeCode(String code) {
		return MCWorkFlowSuggestionDao.queryAllSuggestByTypeCode(code);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return MCWorkFlowSuggestionDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MCCommonSuggestion obj) {
		MCWorkFlowSuggestionDao.update(obj);
	}

	@Override
	public MCCommonSuggestion queryByProperty(String name, String value) {
		return MCWorkFlowSuggestionDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MCCommonSuggestion obj) {
		return MCWorkFlowSuggestionDao.save(obj);
	}

	@Override
	public MCCommonSuggestion queryObjById(Serializable id) {
		return MCWorkFlowSuggestionDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		MCWorkFlowSuggestionDao.deleteById(id);
	}

}
