package com.digitzones.app.controller;

import com.digitzones.model.RelatedDocument;
import com.digitzones.service.IRelatedDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/AppRelatedDocument")
public class AppRelatedDocumentController {
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	
	@RequestMapping("/queryDocByDeviceId.do")
	@ResponseBody
	public ModelMap queryDocByDeviceId(Long deviceId) {
		ModelMap modelMap = new ModelMap();
		List<RelatedDocument> docs=relatedDocumentService.queryDocsByConditionsByDocTypeModuleCodeAndRelatedId(deviceId.toString(), "device");
		modelMap.addAttribute("docs", docs);
		return modelMap;
	}
}
