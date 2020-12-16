package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.QualityGrade;
import com.digitzones.service.IQualityGradeService;
/**
 * 质量等级控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/qualityGrade")
public class QualityGradeController {
	private IQualityGradeService qualityGradeService;
	@Autowired
	public void setQualityGradeService(IQualityGradeService qualityGradeService) {
		this.qualityGradeService = qualityGradeService;
	}
	/**
	 * 查找所有质量等级
	 * @return
	 */
	@RequestMapping("/queryAllQualityGrades.do")
	@ResponseBody
	public List<QualityGrade> queryAllQualityGrades(){
		return qualityGradeService.queryAllQualityGrades();
	}
	
	/**
	 * 查询质量等级信息
	 * @return
	 */
	@RequestMapping("/queryQualityGrades.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryQualityGrades(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<QualityGrade> pager = qualityGradeService.queryObjs("from QualityGrade c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addQualityGrade.do")
	@ResponseBody
	public ModelMap addQualityGrade(QualityGrade qualityGrade) {
		ModelMap modelMap = new ModelMap();
		QualityGrade c4code = qualityGradeService.queryByProperty("code", qualityGrade.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "质量等级编码已被使用");
		}else  {
			QualityGrade c4name = qualityGradeService.queryByProperty("name", qualityGrade.getName());
			if(c4name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "质量等级名称已被使用");
			}else {
				qualityGradeService.addObj(qualityGrade);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	
	/**
	 * 根据id查询质量等级信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryQualityGradeById.do")
	@ResponseBody
	public QualityGrade queryQualityGradeById(Long id) {
		QualityGrade c =  qualityGradeService.queryObjById(id);
		return c;
	}
	/**
	 * 更新质量等级
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateQualityGrade.do")
	@ResponseBody
	public ModelMap updateQualityGrade(QualityGrade qualityGrade) {
		ModelMap modelMap = new ModelMap();
		QualityGrade c4name = qualityGradeService.queryByProperty("name", qualityGrade.getName());
		if(c4name!=null && !c4name.getId().equals(qualityGrade.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "质量等级名称已被使用");
		}else {
			qualityGradeService.updateObj(qualityGrade);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	
	/**
	 * 根据id删除质量等级
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteQualityGrade.do")
	@ResponseBody
	public ModelMap deleteQualityGrade(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		qualityGradeService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
} 
