package com.digitzones.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.RawMaterial;
import com.digitzones.service.IRawMaterialService;
/**
 * 原材料控制器：
 * 原材料：产品或半成品上一道工序的产物
 */
@RestController
@RequestMapping("rawMaterial")
public class RawMaterialController {
    @Autowired
    private IRawMaterialService rawMaterialService;
    
    /**
	 * 保存
	 * @return
	 */
	@RequestMapping("/saveRawMaterialList.do")
	@ResponseBody
	public ModelMap saveRawMaterialList(List<RawMaterial> dataList){
		ModelMap modelMap = new ModelMap();
		for(RawMaterial r:dataList){
			rawMaterialService.addObj(r);
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
}
