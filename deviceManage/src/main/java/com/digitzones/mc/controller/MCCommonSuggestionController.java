package com.digitzones.mc.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.mc.model.MCCommonSuggestion;
import com.digitzones.mc.service.IMCCommonSuggestionService;

@Controller
@RequestMapping("/mcSuggestion")
public class MCCommonSuggestionController {
	@Autowired
	private IMCCommonSuggestionService mcCommonSuggestionService;
	/**
	 * 获取权限用户信息
	 * @return
	 */
	@RequestMapping("/getCommonSuggestion.do")
	@ResponseBody
	public List<MCCommonSuggestion> getCommonSuggestion(String code) {
		return mcCommonSuggestionService.queryAllSuggestionByTypeCode(code);
	}
}
