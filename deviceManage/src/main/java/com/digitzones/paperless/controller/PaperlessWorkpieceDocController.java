package com.digitzones.paperless.controller;

import com.digitzones.constants.Constant;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.RelatedDocumentType;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IRelatedDocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 产品文档控制器
 * @author zdq
 * 2019年3月7日
 */
@RestController
@RequestMapping("paperlessWorkpieceDoc")
public class PaperlessWorkpieceDocController {
	@Autowired
	private IRelatedDocumentTypeService relatedDocumentTypeService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	/**
	 * 查找工件模块下的所有文档类型
	 * @return
	 */
	@RequestMapping("/queryAllWorkpieceDocTypes.do")
	public List<RelatedDocumentType> queryAllWorkpieceDocTypes(){
		return relatedDocumentTypeService.queryRelatedDocumentTypeByModuleCode(Constant.RelatedDoc.WORKPIECE);
	}
	/**
	 * 根据条件查找工件的文档
	 * @param param
	 * @return
	 */
	@RequestMapping("/queryAllWorkpieceDocs.do")
	public List<RelatedDocument> queryAllWorkpieceDocs(@RequestParam Map<String,String> param){
		List<RelatedDocument> list = relatedDocumentService.queryWorkpieceDocsByConditions(param);
		return list;
	}

	/**
	 * 根据生产单元查询当前加工中的工件的文档
	 */
	@RequestMapping("/queryDimesDo.do")
	public List<RelatedDocument> queryDimesDo(HttpServletRequest request){
		HttpSession session = request.getSession();
		ProductionUnit production=(ProductionUnit)session.getAttribute("productionLine");
		List<RelatedDocument> list = relatedDocumentService.queryDimesDo(production.getId());
		return list;
	}
}
