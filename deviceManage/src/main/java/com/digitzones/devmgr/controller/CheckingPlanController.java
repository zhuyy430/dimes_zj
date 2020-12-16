package com.digitzones.devmgr.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.dto.CheckingPlanDto;
import com.digitzones.devmgr.model.CheckingPlan;
import com.digitzones.devmgr.service.ICheckingPlanService;
/**
 * 点检计划控制器
 * @author zdq
 * 2018年12月20日
 */
@RestController
@RequestMapping("/checkingPlan")
public class CheckingPlanController {
	@Autowired
	private ICheckingPlanService checkingPlanService;
	/**
	 * 生成点检计划
	 * @return
	 */
	@RequestMapping("/generateCheckingPlan.do")
	public ModelMap generateCheckingPlan(CheckingPlanDto checkingPlanDto,String classesCode) {
		ModelMap modelMap = new ModelMap();
		CheckingPlan  checkingPlan = new CheckingPlan();
		BeanUtils.copyProperties(checkingPlanDto, checkingPlan);
		try {
			checkingPlanService.addCheckingPlan(checkingPlan, checkingPlanDto.getDeviceCodes(),classesCode);
		}catch(RuntimeException re) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", re.getMessage());
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "点检计划已生成!");
		return modelMap;
	}
}
