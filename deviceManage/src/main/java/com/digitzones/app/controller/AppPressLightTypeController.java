package com.digitzones.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/AppPressLightType")
public class AppPressLightTypeController {
	
	/*@Autowired
	IAppPressLightTypeService appPressLightTypeService;
	
	@RequestMapping("/queryAllPressLightType.do")
	@ResponseBody
	public List<PressLightType> queryAllPressLightType(@RequestBody JSONObject data){
		Long Id = data.getLong("id");
		List<PressLightType> PressLightTypeList=new ArrayList<>();
		if(null==Id||"".equals(Id)){
			PressLightTypeList = appPressLightTypeService.queryAllPressLightType();
		}else{
			PressLightTypeList = appPressLightTypeService.queryPressLightTypeByParentId(Id);
		}
		return PressLightTypeList;
	}*/
}
