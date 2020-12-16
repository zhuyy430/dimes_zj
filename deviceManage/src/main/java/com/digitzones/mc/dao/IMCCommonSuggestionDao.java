package com.digitzones.mc.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.mc.model.MCCommonSuggestion;

/**
 * 
 * @author Administrator
 *
 */
public interface IMCCommonSuggestionDao extends ICommonDao<MCCommonSuggestion>{
	/**
	 * 根据类型代码获取常用处理意见
	 * @param code
	 * @return
	 */
	public List<MCCommonSuggestion> queryAllSuggestByTypeCode(String code);
}
