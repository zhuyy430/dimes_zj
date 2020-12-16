package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.SecureEnvironmentType;
import com.digitzones.service.ISecureEnvironmentTypeService;
/**
 * 安环类型控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/secureEnvironmentType")
public class SecureEnvironmentTypeController {
	private ISecureEnvironmentTypeService secureEnvironmentTypeService;
	@Autowired
	public void setSecureEnvironmentTypeService(ISecureEnvironmentTypeService secureEnvironmentTypeService) {
		this.secureEnvironmentTypeService = secureEnvironmentTypeService;
	}
	/**
	 * 查找所有安环类型
	 * @return
	 */
	@RequestMapping("/queryAllSecureEnvironmentTypes.do")
	@ResponseBody
	public List<SecureEnvironmentType> queryAllSecureEnvironmentTypes(){
		return secureEnvironmentTypeService.queryAllSecureEnvironmentTypes();
	}
	
	/**
	 * 查询安环类型信息
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentTypes.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySecureEnvironmentTypes(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<SecureEnvironmentType> pager = secureEnvironmentTypeService.queryObjs("from SecureEnvironmentType c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addSecureEnvironmentType.do")
	@ResponseBody
	public ModelMap addSecureEnvironmentType(SecureEnvironmentType secureEnvironmentType) {
		ModelMap modelMap = new ModelMap();
		SecureEnvironmentType c4code = secureEnvironmentTypeService.queryByProperty("code", secureEnvironmentType.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "安环类型编码已被使用");
		}else  {
			SecureEnvironmentType c4name = secureEnvironmentTypeService.queryByProperty("name", secureEnvironmentType.getName());
			if(c4name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "安环类型名称已被使用");
			}else {
				secureEnvironmentTypeService.addObj(secureEnvironmentType);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	
	/**
	 * 根据id查询安环类型信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentTypeById.do")
	@ResponseBody
	public SecureEnvironmentType querySecureEnvironmentTypeById(Long id) {
		SecureEnvironmentType c =  secureEnvironmentTypeService.queryObjById(id);
		return c;
	}
	/**
	 * 更新安环类型
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateSecureEnvironmentType.do")
	@ResponseBody
	public ModelMap updateSecureEnvironmentType(SecureEnvironmentType secureEnvironmentType) {
		ModelMap modelMap = new ModelMap();
		SecureEnvironmentType c4name = secureEnvironmentTypeService.queryByProperty("name", secureEnvironmentType.getName());
		if(c4name!=null && !c4name.getId().equals(secureEnvironmentType.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "安环类型名称已被使用");
		}else {
			secureEnvironmentTypeService.updateObj(secureEnvironmentType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	
	/**
	 * 根据id删除安环类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSecureEnvironmentType.do")
	@ResponseBody
	public ModelMap deleteSecureEnvironmentType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		secureEnvironmentTypeService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
} 
