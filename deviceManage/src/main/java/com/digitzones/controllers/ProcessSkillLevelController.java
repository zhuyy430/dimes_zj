package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.ProcessSkillLevel;
import com.digitzones.service.IProcessSkillLevelService;
@Controller
@RequestMapping("/processSkillLevel")
public class ProcessSkillLevelController {
	private IProcessSkillLevelService processSkillLevelService;
	@Autowired
	public void setProcessSkillLevelService(IProcessSkillLevelService processSkillLevelService) {
		this.processSkillLevelService = processSkillLevelService;
	}
	/**
	 * 查询工序下的技能等级信息
	 * @return
	 */
	@RequestMapping("/queryProcessSkillLevelsBySkillId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryProcessSkillLevelsBySkillId(Long skillId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<ProcessSkillLevel> pager = processSkillLevelService.queryObjs("from ProcessSkillLevel c where c.skill.id=?0 ", page, rows, new Object[] {skillId});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查询技能下的技能等级信息
	 * @return
	 */
	@RequestMapping("/queryProcessSkillLevelsBySkillIdNoPage.do")
	@ResponseBody
	public List<ProcessSkillLevel> queryProcessSkillLevelsBySkillId(Long skillId) {
		List<ProcessSkillLevel> list = processSkillLevelService.queryProcessSkillLevelsBySkillId(skillId);
		return list;
	}
	/**
	 * 添加工序下的技能等级信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addProcessSkillLevel.do")
	@ResponseBody
	public ModelMap addProcessSkillLevel(ProcessSkillLevel processSkillLevel) {
		ModelMap modelMap = new ModelMap();

		List<ProcessSkillLevel> skillLevelList = processSkillLevelService.queryProcessSkillLevelsBySkillId(processSkillLevel.getSkill().getId());
		for(ProcessSkillLevel sl:skillLevelList){
			if(sl.getCode().equals(processSkillLevel.getCode())){
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "编码已被使用");
				return modelMap;
			}else if(sl.getName().equals(processSkillLevel.getName())){
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "名称已被使用");
				return modelMap;
			}
		}
		processSkillLevelService.addObj(processSkillLevel);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
		//ProcessSkillLevel c4code = processSkillLevelService.queryByProperty("code", processSkillLevel.getCode());
		/*if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "编码已被使用");
		}else  {
			ProcessSkillLevel c4name = processSkillLevelService.queryByProperty("name", processSkillLevel.getName());
			if(c4name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "名称已被使用");
			}else {
				processSkillLevelService.addObj(processSkillLevel);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}*/
	}
	/**
	 * 根据id查询工序下的技能等级信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryProcessSkillLevelById.do")
	@ResponseBody
	public ProcessSkillLevel queryProcessSkillLevelById(Long id) {
		return processSkillLevelService.queryObjById(id);
	}

	/**
	 * 更新工序下的技能等级信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateProcessSkillLevel.do")
	@ResponseBody
	public ModelMap updateProcessSkillLevel(ProcessSkillLevel processSkillLevel) {
		ModelMap modelMap = new ModelMap();
		ProcessSkillLevel psl = processSkillLevelService.queryObjById(processSkillLevel.getId());
		psl.setName(processSkillLevel.getName());
		psl.setNote(processSkillLevel.getNote());
		processSkillLevelService.updateObj(psl);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}

	/**
	 * 根据id删除班次
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteProcessSkillLevel.do")
	@ResponseBody
	public ModelMap deleteProcessSkillLevel(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		processSkillLevelService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	
} 
