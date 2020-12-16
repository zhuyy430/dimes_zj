package com.digitzones.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.digitzones.app.service.IAppProductionUnitService;
import com.digitzones.model.ProductionUnit;

@Controller
@RequestMapping("/AppProductionUnit")
public class AppProductionUnitController {
	
	@Autowired
	IAppProductionUnitService appProductionUnitService;
	/**
	 * 获取所有用户
	 * @return
	 */
	@RequestMapping("/queryAllProductionUnit.do")
	@ResponseBody
	public List<ProductionUnit> queryAllProductionUnit(@RequestBody JSONObject data){
		Long Id = data.getLong("id");
		List<ProductionUnit> ProductionUnitList = new ArrayList<>();
		if(null==Id||"".equals(Id)){
			ProductionUnitList = appProductionUnitService.queryAllProductionUnit();
		}else{
			ProductionUnitList = appProductionUnitService.queryAProductionUnitByParentId(Id);
		}
		return ProductionUnitList;
	}
}
