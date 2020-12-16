package com.digitzones.mc.service;

import java.util.List;

import com.digitzones.mc.model.MCCommonSuggestion;
import com.digitzones.service.ICommonService;

/**
 * mc端设备站点service
 * @author Administrator
 *
 */
public interface IMCCommonSuggestionService extends ICommonService<MCCommonSuggestion>{
	
	/**
	 * 获取理意见信息
	 * @param WorkFlowSuggestion
	 * @return
	 */
	public List<MCCommonSuggestion> queryAllSuggestionByTypeCode (String code);

}
