package com.digitzones.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.DispatchedLevel;
import com.digitzones.model.Pager;
import com.digitzones.service.IDispatchedLevelService;

@Controller
@RequestMapping("/DispatchedLevel")
public class DispatchedLevelController {
	@Autowired
	private IDispatchedLevelService orderLevelService;

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	@RequestMapping("/queryDispatchedLevel.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryDispatchedLevel(@RequestParam(value = "rows", defaultValue = "20") Integer rows,
			@RequestParam(defaultValue = "1") Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from DispatchedLevel o";
		Pager<DispatchedLevel> pager = orderLevelService.queryObjs(hql, page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}

	/**
	 * 添加
	 * 
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addDispatchedLevel.do")
	@ResponseBody
	public ModelMap addDispatchedLevel(DispatchedLevel dispatchedLevel, Principal principal) {
		ModelMap modelMap = new ModelMap();
		orderLevelService.addObj(dispatchedLevel);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		// }
		return modelMap;
	}

	/**
	 * 更新
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateDispatchedLevel.do")
	@ResponseBody
	public ModelMap updateDispatchedLevel(DispatchedLevel dispatchedLevel) {
		ModelMap modelMap = new ModelMap();
		orderLevelService.updateObj(dispatchedLevel);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	
	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDispatchedLevel.do")
	@ResponseBody
	public ModelMap deleteDispatchedLevel(String id) {
		if (id != null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		orderLevelService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDispatchedLevelById.do")
	@ResponseBody
	public DispatchedLevel queryDispatchedLevelById(Long id) {
		 DispatchedLevel order = orderLevelService.queryObjById(id);
		return order;
	}
}
