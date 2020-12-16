package com.digitzones.devmgr.controller;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.devmgr.service.IMaintenanceTypeService;
import com.digitzones.model.Pager;
@Controller
@RequestMapping("/maintenanceType")
public class MaintenanceTypeController{
	@Autowired
	private IMaintenanceTypeService maintenanceTypeService;
	/**
	 * 查询保养类别信息
	 * @return
	 */
	@RequestMapping("/queryMaintenanceTypes.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryMaintenanceTypes(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceType> pager = maintenanceTypeService.queryObjs("from MaintenanceType c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查询所有保养类别信息
	 * @return
	 */
	@RequestMapping("/queryAllMaintenanceType.do")
	@ResponseBody
	public List<MaintenanceType> queryAllMaintenanceType() {
		return maintenanceTypeService.queryAllMaintenanceType();
	}
	/**
	 * 添加保养类别信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addMaintenanceType.do")
	@ResponseBody
	public ModelMap addMaintenanceType(MaintenanceType maintenanceType,Principal principal) {
		ModelMap modelMap = new ModelMap();
		MaintenanceType code =  maintenanceTypeService.queryByProperty("code", maintenanceType.getCode());
		if(code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "保养类别编码已被使用");
			return modelMap;
		}
		MaintenanceType c4name = maintenanceTypeService.queryByProperty("name", maintenanceType.getName());
		if(c4name!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "保养类别名称已被使用");
		}else {
			maintenanceTypeService.addObj(maintenanceType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询保养类别信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryMaintenanceTypeById.do")
	@ResponseBody
	public MaintenanceType queryMaintenanceTypeById(Long id) {
		MaintenanceType c =  maintenanceTypeService.queryObjById(id);
		return c;
	}
	/**
	 * 添加保养类别信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateMaintenanceType.do")
	@ResponseBody
	public ModelMap updateMaintenanceType(MaintenanceType maintenanceType,Principal principal) {
		ModelMap modelMap = new ModelMap();
		MaintenanceType code = maintenanceTypeService.queryByProperty("code", maintenanceType.getCode());
		if(code!=null && !code.getId().equals(maintenanceType.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "保养类别编码已被使用");
			return modelMap;
		}
		MaintenanceType c4name = maintenanceTypeService.queryByProperty("name", maintenanceType.getName());
		if(c4name!=null && !c4name.getId().equals(maintenanceType.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "保养类别名称已被使用");
		}else {
			maintenanceTypeService.updateObj(maintenanceType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}

	/**
	 * 根据id删除保养类别
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteMaintenanceType.do")
	@ResponseBody
	public ModelMap deleteMaintenanceType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		MaintenanceType maintenanceType = maintenanceTypeService.queryObjById(Long.valueOf(id));
		if(maintenanceType!=null && !maintenanceType.getAllowedDelete()) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该类别为系统预设类别，不允许删除!");
			return modelMap;
		}
	
		if(maintenanceTypeService.isInUse(Long.valueOf(id))) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该类别正在使用中，无法删除!");
		}else {
			maintenanceTypeService.deleteObj(Long.valueOf(id));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		}
		return modelMap;
	}
	/**
	 * 停用该保养类别
	 * @param id
	 * @return
	 *//*
	@RequestMapping("/disabledMaintenanceType.do")
	@ResponseBody
	public ModelMap disabledMaintenanceType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		MaintenanceType d = maintenanceTypeService.queryObjById(Long.valueOf(id));
		if(!d.isDisabled() && maintenanceTypeService.isInUse(Long.valueOf(id))) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该类别正在使用中，无法停用!");
			return modelMap;
		}
		d.setDisabled(!d.isDisabled());
		maintenanceTypeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}*/
} 
