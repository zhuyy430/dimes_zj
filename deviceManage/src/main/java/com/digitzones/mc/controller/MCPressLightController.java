package com.digitzones.mc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.mc.service.IMCPressLightService;
import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightType;
import com.digitzones.service.IPressLightService;
import com.digitzones.service.IPressLightTypeService;

@Controller
@RequestMapping("/mcPressLight")
public class MCPressLightController {
	@Autowired
	private IPressLightService pressLightService;
	@Autowired
	private IMCPressLightService mcPressLightService;
	@Autowired
	private IPressLightTypeService pressLightTypeService;
	/**
	 * 根据xxx查找损时记录
	 */
	@RequestMapping("/getPressLightByCode.do")
	@ResponseBody
	public PressLight getPressLightByCode(@RequestParam(value="code")String code){
		return pressLightService.queryByProperty("code", code);
	}
	/**
	 * 根据父类code查找所有损时原因
	 */
	@RequestMapping("/mcGetAllTypeByParentCode.do")
	@ResponseBody
	public List<PressLight> mcGetAllTypeByParentCode(@RequestParam(value="pcode")String pcode){
		List<PressLight> plList = pressLightService.queryPressLightByBasicCode(pcode);
		return plList;
	}
	/**
	 * 根据损时原因查找basic类别信息
	 */
	@RequestMapping("/mcGetParentTypeBytypeId.do")
	@ResponseBody
	public PressLightType mcGetParentTypeBytypeId(@RequestParam(value="typeid")Long typeid){
		PressLightType pressLightType = pressLightTypeService.queryObjById(typeid);
		if(pressLightType.getBasicCode()==null){
			return pressLightType;
		}else{
			PressLightType plType = pressLightTypeService.queryByProperty("code",pressLightType.getBasicCode());
			return plType;
		}
	}
	/**
	 * 根据损时原因的id查找level=1的类别信息(递归)
	 */
	@RequestMapping("/getParentTypeBytypeId.do")
	@ResponseBody
	public PressLightType getParentTypeBytypeId(@RequestParam(value="typeid")Long typeid){
		PressLightType plType = new PressLightType();
		getParentType(typeid,plType);
		return plType;
	}

	/**
	 * 查询所有损时原因
	 */
	@RequestMapping("/getAllPressLight.do")
	@ResponseBody
	public List<PressLight> getAllPressLight(){
		List<PressLight> pressLightList = mcPressLightService.queryAllPressLight();
		for(PressLight pl : pressLightList){
			if(pl.getPressLightType().getLevel().intValue()!=1){
				PressLightType plType = pressLightTypeService.queryByProperty("code",pl.getPressLightType().getBasicCode());
				if(!plType.getDisabled()){
					pl.setPressLightType(plType);
				}
			}
		}
		return pressLightList;
	}
	
	private void getParentType(Long typeid,PressLightType plType){
		PressLightType pressLightType = pressLightTypeService.queryObjById(typeid);
		if(pressLightType.getLevel().intValue()!=1){
			getParentType(pressLightType.getParent().getId(),plType);
		}else{
			plType.setChildren(pressLightType.getChildren());
			plType.setCode(pressLightType.getCode());
			plType.setDisabled(pressLightType.getDisabled());
			plType.setId(pressLightType.getId());
			plType.setLevel(pressLightType.getLevel());
			plType.setName(pressLightType.getName());
			plType.setNote(pressLightType.getNote());
			plType.setParent(pressLightType.getParent());
			plType.setText(pressLightType.getText());
		}
	}
}
