package com.digitzones.paperless.controller;

import com.digitzones.constants.Constant;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.RelatedDocument;
import com.digitzones.service.IProductionUnitService;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IRelatedDocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/paperlessProductionLine")
public class PaperLessProductionLineController {
	@Autowired
	private IRelatedDocumentTypeService relatedDocumentTypeService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private IProductionUnitService productionUnitService;
	
	@RequestMapping("/queryProductionLineDocs.do")
	@ResponseBody
	public List<RelatedDocument> queryProductionLineDocs(Long productionLineId){
		return  relatedDocumentService.queryDocsByConditionsByDocTypeModuleCodeAndRelatedId(productionLineId.toString(), Constant.RelatedDoc.PRODUCTIONLINE);
	}
	
	@RequestMapping("/updateConfirmProductionLine.do")
	@ResponseBody
	public ModelMap updateConfirmProductionLine(Long productionLineId,HttpServletRequest request) {
		ModelMap modelMap=new ModelMap();
		HttpSession session = request.getSession();
		ProductionUnit production=productionUnitService.queryObjById(productionLineId);
		session.setAttribute("productionLine",production);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("productionLine", production);
		return modelMap;
	}
}
