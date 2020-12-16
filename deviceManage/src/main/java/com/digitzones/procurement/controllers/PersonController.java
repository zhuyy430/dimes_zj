package com.digitzones.procurement.controllers;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.procurement.service.IPersonProductionUnitMappingService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IProductionUnitService;
@Controller
@RequestMapping("/person")
public class PersonController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private IProductionUnitService productionUnitService;
	@Autowired
	private IEmployeeService personService;
	@Autowired
	private IPersonProductionUnitMappingService personProductionUnitMappingService;
	/**
	 * 根据生产单元获取非该单元的员工
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPersonNotInByProductionUnitId.do")
	@ResponseBody
	public ModelMap queryPersonNotInByProductionUnitId(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,@RequestParam(value="productionUnitId")Long productionUnitId,
			String code,String name,String dept,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		String hql = "from Employee e  where e.code not in(select pp.person.code from PersonProductionUnitMapping pp where pp.productionUnit.id=?0)";
		List<Object> paramsList = new ArrayList<>();
		paramsList.add(productionUnitId);
		
		int i = 1;
		if(!StringUtils.isEmpty(code)) {
			hql += " and e.code like ?"+(i++);
			paramsList.add("%" + code + "%");
		}
		if(!StringUtils.isEmpty(name)) {
			hql += " and e.name like ?"+(i++);
			paramsList.add("%" + name + "%");
		}
		if(!StringUtils.isEmpty(dept)) {
			hql += " and e.cDept_Num like ?"+(i++);
			paramsList.add("%" + dept + "%");
		}

		Pager<Employee> pager = personService.queryObjs(hql, page, rows, paramsList.toArray());
		List<Employee> emp = pager.getData();
		modelMap.addAttribute("rows", emp);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 修改员工的生产单元
	 * @return
	 */
	@RequestMapping("/updateEmployeeProductionUnitId.do")
	@ResponseBody
	public ModelMap updateEmployeeProductionUnitId(Long productionUnitId,String employeeCodes){
		ModelMap modelMap = new ModelMap();
		if(employeeCodes!=null) {
			if(employeeCodes.contains("[")) {
				employeeCodes = employeeCodes.replace("[", "");
			}
			if(employeeCodes.contains("]")) {
				employeeCodes = employeeCodes.replace("]", "");
			}
			String[] idArray = employeeCodes.split(",");
			for(int i = 0;i<idArray.length;i++) {
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 删除员工的生产单元
	 * @return
	 */
	@RequestMapping("/deleteEmployeeProductionUnitId.do")
	@ResponseBody
	public ModelMap deleteEmployeeProductionUnitId(String employeeId){
		ModelMap modelMap = new ModelMap();
		if(employeeId!=null && employeeId.contains("'")) {
			employeeId = employeeId.replace("'", "");
		}
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	/**
	 * 删除员工的生产单元
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEmployeeNotInMaintenanceStaff.do")
	@ResponseBody
	public ModelMap queryEmployeeNotInMaintenanceStaff(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		return modelMap;
	}
}
