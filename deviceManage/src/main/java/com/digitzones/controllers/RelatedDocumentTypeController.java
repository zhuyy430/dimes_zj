package com.digitzones.controllers;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocumentType;
import com.digitzones.model.User;
/**
 * 关联文档类别管理控制器
 * @author zdq
 * 2018年6月7日
 */
import com.digitzones.service.IRelatedDocumentTypeService;
import com.digitzones.service.IUserService;
@Controller
@RequestMapping("/relatedDocumentType")
public class RelatedDocumentTypeController{
	@Autowired
	private IRelatedDocumentTypeService relatedDocumentTypeService;
	@Autowired
	private IUserService userService;
	/**
	 * 查询关联文档类别信息
	 * @return
	 */
	@RequestMapping("/queryRelatedDocumentType.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryRelatedDocumentType(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<RelatedDocumentType> pager = relatedDocumentTypeService.queryObjs("from RelatedDocumentType c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查询所有关联文档类别信息
	 * @return
	 */
	@RequestMapping("/queryRelatedDocumentTypeByModuleCode.do")
	@ResponseBody
	public List<RelatedDocumentType> queryRelatedDocumentTypeByModuleCode(String moduleCode) {
		return relatedDocumentTypeService.queryRelatedDocumentTypeByModuleCode(moduleCode);
	}
	/**
	 * 添加关联文档类别信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addRelatedDocumentType.do")
	@ResponseBody
	public ModelMap addRelatedDocumentType(RelatedDocumentType relatedDocumentType,Principal principal) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", principal.getName());
		relatedDocumentType.setCreateDate(new Date());
		relatedDocumentType.setCreateUserId(user.getId());
		relatedDocumentType.setCreateUsername(user.getUsername());
		relatedDocumentTypeService.addObj(relatedDocumentType);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		//}
		return modelMap;
	}
	/**
	 * 根据id查询关联文档类别信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryRelatedDocumentTypeById.do")
	@ResponseBody
	public RelatedDocumentType queryRelatedDocumentTypeById(Long id) {
		RelatedDocumentType c =  relatedDocumentTypeService.queryObjById(id);
		return c;
	}
	/**
	 * 添加关联文档类别信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateRelatedDocumentType.do")
	@ResponseBody
	public ModelMap updateRelatedDocumentType(RelatedDocumentType relatedDocumentType,Principal principal) {
		ModelMap modelMap = new ModelMap();
		RelatedDocumentType type = relatedDocumentTypeService.queryObjById(relatedDocumentType.getId());
		User user = userService.queryByProperty("username", principal.getName());
		type.setModifyDate(new Date());
		type.setModifyUserId(user.getId());
		type.setModifyUsername(user.getUsername());
		type.setNote(relatedDocumentType.getNote());
		type.setName(relatedDocumentType.getName());
		relatedDocumentTypeService.updateObj(type);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 根据id删除关联文档类别
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteRelatedDocumentType.do")
	@ResponseBody
	public ModelMap deleteRelatedDocumentType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		RelatedDocumentType type = relatedDocumentTypeService.queryObjById(Long.valueOf(id));
		if(!type.isAllowedDelete()) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "系统创建，不允许删除!");
			return modelMap;
		}
		if(relatedDocumentTypeService.isInUse(Long.valueOf(id))) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该文档类别正在使用中，无法删除!");
		}else {
			relatedDocumentTypeService.deleteObj(Long.valueOf(id));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		}
		return modelMap;
	}
	/**
	 * 停用该关联文档类别
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledRelatedDocumentType.do")
	@ResponseBody
	public ModelMap disabledRelatedDocumentType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		RelatedDocumentType d = relatedDocumentTypeService.queryObjById(Long.valueOf(id));
		if(!d.isDisabled() && relatedDocumentTypeService.isInUse(Long.valueOf(id))) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该文档类别正在使用中，无法停用!");
			return modelMap;
		}
		d.setDisabled(!d.isDisabled());
		relatedDocumentTypeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 通过文档类别code查找文档类别
	 */
	@RequestMapping("/queryRelatedDocumentTypeByCode.do")
	@ResponseBody
	public RelatedDocumentType queryRelatedDocumentTypeByCode(String code){
		return relatedDocumentTypeService.queryByProperty("code", code);
	}
} 
