package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.EmployeeSkillMapping;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessSkillLevel;
import com.digitzones.model.Processes;
import com.digitzones.model.Skill;
import com.digitzones.model.SkillType;
import com.digitzones.service.IEmployeeSkillMappingService;
import com.digitzones.service.IProcessesService;
import com.digitzones.service.ISkillService;
import com.digitzones.service.ISkillTypeService;
import com.digitzones.vo.EmployeeSkillMappingVO;
import com.digitzones.vo.SkillVO;
@Controller
@RequestMapping("/skill")
public class SkillController {
	private ISkillService skillService;
	private ISkillTypeService skillTypeService;
	private IEmployeeSkillMappingService employeeSkillMappingService;
	private IProcessesService processesService;
	@Autowired
	public void setProcessesService(IProcessesService processesService) {
		this.processesService = processesService;
	}
	@Autowired
	public void setEmployeeSkillMappingService(IEmployeeSkillMappingService employeeSkillMappingService) {
		this.employeeSkillMappingService = employeeSkillMappingService;
	}
	@Autowired
	public void setSkillService(ISkillService skillService) {
		this.skillService = skillService;
	}
	@Autowired
	public void setSkillTypeService(ISkillTypeService skillTypeService) {
		this.skillTypeService = skillTypeService;
	}
	/**
	 * 查询技能信息
	 * @return
	 */
	@RequestMapping("/querySkillsByProcessId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySkillsByProcessId(Long processId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<Skill> pager = skillService.queryObjs("from Skill c where c.process.id=?0", page, rows, new Object[] {processId});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 添加技能信息
	 * @return
	 */
	@RequestMapping("/addSkill.do")
	@ResponseBody
	public ModelMap addSkill(Skill skill) {
		ModelMap modelMap = new ModelMap();

		if(skill.getProcess()==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择工序!");
			return modelMap;
		}

		Long processId = skill.getProcess().getId();
		//根据id查找工序
		Processes p = processesService.queryObjById(processId);
		if(p==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择工序!");
			return modelMap;
		}

		SkillType skillType = skill.getSkillType();
		if(skillType!=null) {
			SkillType ct = skillTypeService.queryByProperty("name", skillType.getName());
			if(ct==null) {
				Long id = (Long) skillTypeService.addObj(skillType);
				ct = skillTypeService.queryObjById(id);
			}
			skill.setSkillType(ct);
		}

		Skill c4code = skillService.queryByProperty("code", skill.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "技能编码已被使用");
		}else  {
			skillService.addObj(skill);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询技能信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySkillById.do")
	@ResponseBody
	public SkillVO querySkillById(Long id) {
		Skill c =  skillService.queryObjById(id);
		return model2vo(c);
	}

	private SkillVO model2vo(Skill skill) {
		if(skill==null) {
			return null;
		}
		SkillVO vo = new SkillVO();
		vo.setId(skill.getId());
		vo.setCode(skill.getCode());
		vo.setName(skill.getName());
		vo.setNote(skill.getNote());
		if(skill.getSkillType()!=null) {
			vo.setSkillTypeId(skill.getSkillType().getId());
			vo.setSkillTypeName(skill.getSkillType().getName());
			vo.setSkillTypeCode(skill.getSkillType().getCode());
		}
		return vo;
	}
	/**
	 * 更新技能信息
	 * @return
	 */
	@RequestMapping("/updateSkill.do")
	@ResponseBody
	public ModelMap updateSkill(Skill skill) {
		ModelMap modelMap = new ModelMap();
		//查看技能类型是否 存在，如果存在，则直接添加到技能中；如果不存在，则先保存技能类型
		SkillType skillType = skill.getSkillType();
		if(skillType!=null) {
			SkillType ct = skillTypeService.queryByProperty("name", skillType.getName());
			if(ct==null) {
				Long id = (Long) skillTypeService.addObj(skillType);
				ct = skillTypeService.queryObjById(id);
			}
			skill.setSkillType(ct);
		}

		Skill c4name = skillService.queryByProperty("name", skill.getName());
		if(c4name!=null && !c4name.getId().equals(skill.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "技能名称已被使用");
		}else {
			skillService.updateObj(skill);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}

	/**
	 * 根据id删除技能
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSkill.do")
	@ResponseBody
	public ModelMap deleteSkill(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		skillService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该技能
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledSkill.do")
	@ResponseBody
	public ModelMap disabledSkill(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Skill d = skillService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		skillService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 根据员工id查找员工技能
	 * @param employeeId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/querySkillsByEmployeeId.do")
	@ResponseBody
	public ModelMap querySkillsByEmployeeId(Long employeeId,Integer rows,Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from EmployeeSkillMapping esm where esm.employee.id=?0";
		@SuppressWarnings("unchecked")
		Pager<EmployeeSkillMapping> objPager = employeeSkillMappingService.queryObjs(hql, page, rows, new Object[] {employeeId});
		modelMap.addAttribute("rows", objPager.getData());
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}
	/**
	 * 添加非当前员工的技能
	 * @param employeeId
	 * @return
	 */
	@RequestMapping("/queryOtherSkills.do")
	@ResponseBody
	public List<Skill> queryOtherDevices(Long employeeId,String q) {
		if(q==null||"".equals(q.trim())) {
			return skillService.queryOtherSkillsByEmployeeId(employeeId);
		}else {
			return skillService.queryOtherSkillsByEmployeeIdAndCondition(employeeId, q);
		}
	}
	/**
	 * 为员工添加技能
	 * @param esm
	 * @return
	 */
	@RequestMapping("/addSkill4Employee.do")
	@ResponseBody
	public ModelMap addSkill4Employee(EmployeeSkillMapping esm) {
		ModelMap modelMap = new ModelMap();
		employeeSkillMappingService.addObj(esm);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功！");
		return modelMap;
	}
	/**
	 * 为员工添加技能
	 * @param esm
	 * @return
	 */
	@RequestMapping("/updateSkill4Employee.do")
	@ResponseBody
	public ModelMap updateSkill4Employee(EmployeeSkillMapping esm) {
		ModelMap modelMap = new ModelMap();

		EmployeeSkillMapping mapping = employeeSkillMappingService.queryObjById(esm.getId());

		mapping.setSkillLevel(esm.getSkillLevel());

		employeeSkillMappingService.updateObj(mapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功！");
		return modelMap;
	}
	/**
	 * 根据id查找员工和技能的映射
	 * @param employeeSkillMappingId
	 * @return
	 */
	@RequestMapping("/queryEmployeeSkillMappingById.do")
	@ResponseBody
	public EmployeeSkillMappingVO queryEmployeeSkillMappingById(Long employeeSkillMappingId) {
		return model2vo(employeeSkillMappingService.queryObjById(employeeSkillMappingId));
	}

	private EmployeeSkillMappingVO model2vo(EmployeeSkillMapping m) {
		if(m==null) {
			return null;
		}
		EmployeeSkillMappingVO vo = new EmployeeSkillMappingVO();
		vo.setId(m.getId());
		if(m.getEmployee()!=null) {
			vo.setEmployeeCode(m.getEmployee().getCode());
			vo.setEmployeeCode(m.getEmployee().getName());
		}
		ProcessSkillLevel skillLevel = m.getSkillLevel();
		if(skillLevel!=null) {
			Skill  skill = skillLevel.getSkill();
			if(skill!=null) {
				vo.setSkillCode(skill.getCode());
				vo.setSkillId(skill.getId());
				vo.setSkillName(skill.getName());
			}

			vo.setSkillLevelCode(skillLevel.getCode());
			vo.setSkillLevelId(skillLevel.getId());
			vo.setSkillLevelName(skillLevel.getName());
		}

		return vo;
	}
	/**
	 * 根据id删除技能
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEmployeeSkillMapping.do")
	@ResponseBody
	public ModelMap deleteEmployeeSkillMapping(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		employeeSkillMappingService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 查询所有人员技能类别
	 * @return
	 */
	@RequestMapping("/queryAllSkillTypes.do")
	@ResponseBody
	public List<SkillType> queryAllSkillTypes(){
		return skillTypeService.queryAllSkillTypes();
	}
} 
