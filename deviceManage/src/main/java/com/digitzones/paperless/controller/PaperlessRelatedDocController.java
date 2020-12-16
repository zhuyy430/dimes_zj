package com.digitzones.paperless.controller;

import com.digitzones.model.Device;
import com.digitzones.model.RelatedDocument;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IRelatedDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/paperlessRelatedDoc")
public class PaperlessRelatedDocController {
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private IDeviceService deviceService;
	
	@RequestMapping("/queryDocsByRelatedIdAndType.do")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public List<RelatedDocument> queryDeviceDocsByRelatedIdAndType(String deviceCode,String moduleCode) {
		Device device=deviceService.queryByProperty("code", deviceCode);
		if(device==null){
			return null;
		}else{
			List<RelatedDocument> list = relatedDocumentService.queryDocsByConditionsByDocTypeModuleCodeAndRelatedId(device.getId().toString(), moduleCode);
			return list;
		}
	}
}
