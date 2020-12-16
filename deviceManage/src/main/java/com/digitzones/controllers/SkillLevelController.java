package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.SkillLevel;
import com.digitzones.service.ISkillLevelService;
/**
 * 技能等级控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/skillLevel")
public class SkillLevelController {
	private ISkillLevelService skillLevelService;
	@Autowired
	public void setSkillLevelService(ISkillLevelService skillLevelService) {
		this.skillLevelService = skillLevelService;
	}
	/**
	 * 查找所有技能等级
	 * @return
	 */
	@RequestMapping("/queryAllSkillLevels.do")
	@ResponseBody
	public List<SkillLevel> queryAllSkillLevels(){
		return skillLevelService.queryAllSkillLevels();
	}
	
	/**
	 * 查询技能等级信息
	 * @return
	 */
	@RequestMapping("/querySkillLevels.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySkillLevels(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<SkillLevel> pager = skillLevelService.queryObjs("from SkillLevel c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addSkillLevel.do")
	@ResponseBody
	public ModelMap addSkillLevel(SkillLevel skillLevel) {
		ModelMap modelMap = new ModelMap();
		SkillLevel c4code = skillLevelService.queryByProperty("code", skillLevel.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "技能等级编码已被使用");
		}else  {
			SkillLevel c4name = skillLevelService.queryByProperty("name", skillLevel.getName());
			if(c4name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "班次名称已被使用");
			}else {
				skillLevelService.addObj(skillLevel);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	
	/**
	 * 根据id查询技能等级信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySkillLevelById.do")
	@ResponseBody
	public SkillLevel querySkillLevelById(Long id) {
		SkillLevel c =  skillLevelService.queryObjById(id);
		return c;
	}
	/**
	 * 更新技能等级
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateSkillLevel.do")
	@ResponseBody
	public ModelMap updateSkillLevel(SkillLevel skillLevel) {
		ModelMap modelMap = new ModelMap();
		SkillLevel c4name = skillLevelService.queryByProperty("name", skillLevel.getName());
		if(c4name!=null && !c4name.getId().equals(skillLevel.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "技能等级名称已被使用");
		}else {
			skillLevelService.updateObj(skillLevel);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	
	/**
	 * 根据id删除技能等级
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSkillLevel.do")
	@ResponseBody
	public ModelMap deleteSkillLevel(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		skillLevelService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
} 
