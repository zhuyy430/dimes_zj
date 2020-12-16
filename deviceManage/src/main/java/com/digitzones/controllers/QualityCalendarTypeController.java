package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.QualityCalendarType;
import com.digitzones.service.IQualityCalendarTypeService;
/**
 * 质量日历类型控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/qualityCalendarType")
public class QualityCalendarTypeController {
	private IQualityCalendarTypeService qualityCalendarTypeService;
	@Autowired
	public void setQualityCalendarTypeService(IQualityCalendarTypeService qualityCalendarTypeService) {
		this.qualityCalendarTypeService = qualityCalendarTypeService;
	}
	/**
	 * 查找所有质量类型
	 * @return
	 */
	@RequestMapping("/queryAllQualityCalendarTypes.do")
	@ResponseBody
	public List<QualityCalendarType> queryAllQualityCalendarTypes(){
		return qualityCalendarTypeService.queryAllQualityCalendarTypes();
	}
	
	/**
	 * 查询质量类型信息
	 * @return
	 */
	@RequestMapping("/queryQualityCalendarTypes.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryQualityCalendarTypes(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<QualityCalendarType> pager = qualityCalendarTypeService.queryObjs("from QualityCalendarType c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addQualityCalendarType.do")
	@ResponseBody
	public ModelMap addQualityCalendarType(QualityCalendarType qualityCalendarType) {
		ModelMap modelMap = new ModelMap();
		QualityCalendarType c4code = qualityCalendarTypeService.queryByProperty("code", qualityCalendarType.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "质量类型编码已被使用");
		}else  {
			QualityCalendarType c4name = qualityCalendarTypeService.queryByProperty("name", qualityCalendarType.getName());
			if(c4name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "质量类型名称已被使用");
			}else {
				qualityCalendarTypeService.addObj(qualityCalendarType);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	
	/**
	 * 根据id查询质量类型信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryQualityCalendarTypeById.do")
	@ResponseBody
	public QualityCalendarType queryQualityCalendarTypeById(Long id) {
		QualityCalendarType c =  qualityCalendarTypeService.queryObjById(id);
		return c;
	}
	/**
	 * 更新质量类型
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateQualityCalendarType.do")
	@ResponseBody
	public ModelMap updateQualityCalendarType(QualityCalendarType qualityCalendarType) {
		ModelMap modelMap = new ModelMap();
		QualityCalendarType c4name = qualityCalendarTypeService.queryByProperty("name", qualityCalendarType.getName());
		if(c4name!=null && !c4name.getId().equals(qualityCalendarType.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "质量类型名称已被使用");
		}else {
			qualityCalendarTypeService.updateObj(qualityCalendarType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	
	/**
	 * 根据id删除质量类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteQualityCalendarType.do")
	@ResponseBody
	public ModelMap deleteQualityCalendarType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		qualityCalendarTypeService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
} 
