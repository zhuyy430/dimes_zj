package com.digitzones.controllers;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 人员技能控制器
 * @author zdq
 * 2018年7月12日
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Employee;
import com.digitzones.model.Processes;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IEmployeeSkillMappingService;
import com.digitzones.service.IProcessSkillLevelService;
import com.digitzones.service.IProcessesService;
@Controller
@RequestMapping("/employeeSkill")
public class EmployeeSkillController {
	private IProcessSkillLevelService processSkillLevelService;
	private IEmployeeService employeeService;
	private IEmployeeSkillMappingService employeeSkillMappingService;
	private IProcessesService processService;
	@Autowired
	public void setProcessService(IProcessesService processService) {
		this.processService = processService;
	}
	@Autowired
	public void setEmployeeSkillMappingService(IEmployeeSkillMappingService employeeSkillMappingService) {
		this.employeeSkillMappingService = employeeSkillMappingService;
	}
	@Autowired
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Autowired
	public void setProcessSkillLevelService(IProcessSkillLevelService processSkillLevelService) {
		this.processSkillLevelService = processSkillLevelService;
	}
	/**
	 * 人员技能：工厂级
	 * @return
	 */
	@RequestMapping("/queryEmployeeSkill.do")
	@ResponseBody
	public ModelMap queryEmployeeSkill() {
		ModelMap modelMap = new ModelMap();
		
		List<Map<String,Object>> inner = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> outer = new ArrayList<Map<String,Object>>();
		List<String> names = new ArrayList<>();
		//查询所有工序
		List<Processes> pList = processService.queryAllProcesses();
		
		//查询所有的技能等级，技能编码及名称
		List<Object[]> skillLevelList = processSkillLevelService.queryCount4SkillLevel();
		
		for(int i = 0;i< skillLevelList.size();i++) {
			Object[] skillLevel = skillLevelList.get(i);
			//根据等级编码查询工序数
			List<Object[]> processList = processSkillLevelService.queryCount4ProcessBySkillLevelCode(skillLevel[0]+"");
			for(Object[] oa : processList) {
				Map<String,Object> map = new HashMap<>();
				map.put("name", oa[1]);
				map.put("value", oa[2]);
				outer.add(map);
			}
			Map<String,Object> map = new HashMap<>();
			map.put("name", skillLevel[1]);
			map.put("value", skillLevel[2]);
			if(i == 0) {
				map.put("selected",true);
			}
			inner.add(map);
			
			names.add(skillLevel[1]+"");
		}
		
		for(Processes p : pList) {
			names.add(p.getName());
		}
		modelMap.addAttribute("inner", inner);
		modelMap.addAttribute("outer", outer);
		modelMap.addAttribute("names", names);
		return modelMap;
	}
	/**
	 * 人员技能：工厂级
	 * 每个员工每个技能等级下的技能数
	 * @return
	 */
	@RequestMapping("/queryEmployeeSkill4emp.do")
	@ResponseBody
	public ModelMap queryEmployeeSkill4emp() {
		ModelMap modelMap = new ModelMap();
		List<String> skillLevels = new ArrayList<>();
		List<String> empList = new ArrayList<>();
		List<List<Object>> data = new ArrayList<>();
		
		//查询所有技能等级
		//查询所有的技能等级，技能编码及名称，每个等级下的工序数
		List<Object[]> skillLevelList = processSkillLevelService.queryCount4SkillLevel();
		//查询所有员工
		List<Employee> emps = employeeService.queryAllEmployees();
		int i = 0;
		//查询每个等级，每个员工下的技能数
		for(Object[] oa : skillLevelList) {
			skillLevels.add(oa[1]+"");
			int j =0;
			for(Employee e : emps) {
				//根据技能id和员工id查找技能数
				Integer count = employeeSkillMappingService.queryCountBySkillLevelIdAndEmployeeCode(e.getCode(), oa[0]+"");
				List<Object> list = new ArrayList<>();
				list.add(j);
				list.add(i);
				list.add(count);
				j++;
				
				data.add(list);
			}
			i++;
		}
		
		for(Employee e : emps) {
			//empList.add(e.getName()+"-"+e.getCode());
			empList.add(e.getName());
		}
		
		modelMap.addAttribute("skillLevels", skillLevels);
		modelMap.addAttribute("emps", empList);
		modelMap.addAttribute("data", data);
		
		return modelMap;
	}
	/**
	 * 人员技能：工厂级
	 * 每个员工每个技能等级下的技能数
	 * @return
	 */
	@RequestMapping("/queryEmployeeSkillForemp.do")
	@ResponseBody
	public ModelMap queryEmployeeSkillForemp() {
		ModelMap modelMap = new ModelMap();
		List<String> skillLevels = new ArrayList<>();
		List<String> empList = new ArrayList<>();
		List<List<Object>> data = new ArrayList<>();
		
		//查询所有技能等级
		//查询所有的技能等级，技能编码及名称，每个等级下的工序数
		List<Object[]> skillLevelList = processSkillLevelService.queryCount4SkillLevel();
		//查询所有员工
		List<Employee> emps = employeeService.queryAllEmployees();
		int i = 0;
		//查询每个等级，每个员工下的技能数
		for(Object[] oa : skillLevelList) {
			skillLevels.add(oa[1]+"");
			int j =0;
			
					//根据技能id和员工id查找技能数
			List<?> count = employeeSkillMappingService.queryCountBySkillLevelCode(oa[0]+"");
			for(Employee e : emps) {
				boolean tf = true;
				List<Object> list = new ArrayList<>();
				for(Object ret:count){
					List<Object> list2 = new ArrayList<>();
					Object[] arr = (Object[])ret;
					String name= arr[0].toString();
					String sum= arr[1].toString();
					list2.add(j);
					list2.add(i);
					if(e.getName().equals(name)){
						tf = false;
						list2.add(Integer.parseInt(sum));
						j++;
						data.add(list2);
					}
				}
				if(tf){
					 list.add(j);
					 list.add(i);
					 list.add(0);
					 j++;
					 data.add(list);
				}
				if(!empList.contains(e.getName())){
					empList.add(e.getName());
				}
			}
			i++;
		}
		
		modelMap.addAttribute("skillLevels", skillLevels);
		modelMap.addAttribute("emps", empList);
		modelMap.addAttribute("data", data);
		
		return modelMap;
	}
	@RequestMapping("/queryEmployeeSkill4empByProductionUnitId.do")
	@ResponseBody
	public ModelMap queryEmployeeSkill4empByProductionUnitId(Long productionUnitId) {
		if(productionUnitId==null) {
			productionUnitId = 0l;
		}
		
		ModelMap modelMap = new ModelMap();
		List<String> skillLevels = new ArrayList<>();
		List<String> empList = new ArrayList<>();
		List<List<Object>> data = new ArrayList<>();
		
		//查询所有技能等级
		//查询所有的技能等级，技能编码及名称，每个等级下的工序数
		List<Object[]> skillLevelList = processSkillLevelService.queryCount4SkillLevel();
		//查询所有员工
		List<Employee> emps = employeeService.queryAllEmployeesByProductionUnitId(productionUnitId);
		int i = 0;
		//查询每个等级，每个员工下的技能数
		for(Object[] oa : skillLevelList) {
			skillLevels.add(oa[1]+"");
			int j =0;
			//0:员工id  1：技能数
			List<Object[]> empAndSkillCountList = employeeSkillMappingService.queryCountBySkillLevelIdAndEmployeeCodeAndProductionUnitId(oa[0]+"",productionUnitId);
			for(Employee e : emps) {
				boolean exist = false;
				for(Object[] obj : empAndSkillCountList) {
					String empCode = (String) obj[0];
					Integer count = (Integer) obj[1];
					if(e.getCode().equals(empCode)){
						List<Object> list = new ArrayList<>();
						list.add(j);
						list.add(i);
						list.add(count);
						j++;
						data.add(list);
						exist = true;
						break;
					}
				}
				if(!exist) {
					List<Object> list = new ArrayList<>();
					list.add(j);
					list.add(i);
					list.add(0);
					j++;
					data.add(list);
				}
			}
			/*for(Employee e : emps) {
				//根据技能id、员工id和产线id查找技能数
				Integer count = employeeSkillMappingService.queryCountBySkillLevelIdAndEmployeeIdAndProductionUnitId(e.getId(), oa[0]+"",productionUnitId);
				List<Object> list = new ArrayList<>();
				list.add(j);
				list.add(i);
				list.add(count);
				j++;
				
				data.add(list);
			}*/
			i++;
		}
		for(Employee e : emps) {
			//empList.add(e.getName()+"-"+e.getCode());
			empList.add(e.getName());
		}
		
		modelMap.addAttribute("skillLevels", skillLevels);
		modelMap.addAttribute("emps", empList);
		modelMap.addAttribute("data", data);
		
		return modelMap;
	}
} 
