package com.digitzones.paperless.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.WorkSheet;
import com.digitzones.service.IWorkSheetDetailService;
import com.digitzones.service.IWorkSheetService;

@RestController
@RequestMapping("paperlessHomePage")
public class PaperlessHomePageController {
	@Autowired
	private IWorkSheetService workSHeetService;
	@Autowired
	private IWorkSheetDetailService workSheetDetailService;
	/**
	 * 根据生产单元编码查找正在加工的工单信息
	 * @param productionUnitCode
	 * @return
	 */
	@RequestMapping("/queryWorkingWorkSheetsByProductionUnitCode.do")
	public ModelMap queryWorkingWorkSheetsByProductionUnitCode(String productionUnitCode) {
		ModelMap modelMap = new ModelMap();
		List<Long> completeCountList = new ArrayList<>();
		List<WorkSheet> list = workSHeetService.queryWorkingWorkSheetsByProductionUnitCode(productionUnitCode);
		if(!CollectionUtils.isEmpty(list)) {
			for(WorkSheet workSheet : list) {
				Long sum = workSheetDetailService.querySumCompleteByWorkSheetId(workSheet.getId()); 
				completeCountList.add(sum);
			}
		}
		modelMap.addAttribute("workSheetsList", list);
		modelMap.addAttribute("completeCountList", completeCountList);
		return modelMap;
	}
}
