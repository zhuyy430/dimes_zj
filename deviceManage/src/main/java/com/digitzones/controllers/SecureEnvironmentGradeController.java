package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.SecureEnvironmentGrade;
import com.digitzones.service.ISecureEnvironmentGradeService;
/**
 * 安环等级控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/secureEnvironmentGrade")
public class SecureEnvironmentGradeController {
	private ISecureEnvironmentGradeService secureEnvironmentGradeService;
	@Autowired
	public void setSecureEnvironmentGradeService(ISecureEnvironmentGradeService secureEnvironmentGradeService) {
		this.secureEnvironmentGradeService = secureEnvironmentGradeService;
	}
	/**
	 * 查找所有安环等级
	 * @return
	 */
	@RequestMapping("/queryAllSecureEnvironmentGrades.do")
	@ResponseBody
	public List<SecureEnvironmentGrade> queryAllSecureEnvironmentGrades(){
		return secureEnvironmentGradeService.queryAllSecureEnvironmentGrades();
	}
	
	/**
	 * 查询安环等级信息
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentGrades.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySecureEnvironmentGrades(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<SecureEnvironmentGrade> pager = secureEnvironmentGradeService.queryObjs("from SecureEnvironmentGrade c ", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addSecureEnvironmentGrade.do")
	@ResponseBody
	public ModelMap addSecureEnvironmentGrade(SecureEnvironmentGrade secureEnvironmentGrade) {
		ModelMap modelMap = new ModelMap();
		SecureEnvironmentGrade c4code = secureEnvironmentGradeService.queryByProperty("code", secureEnvironmentGrade.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "安环等级编码已被使用");
		}else  {
			SecureEnvironmentGrade c4name = secureEnvironmentGradeService.queryByProperty("name", secureEnvironmentGrade.getName());
			if(c4name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "安环等级名称已被使用");
			}else {
				secureEnvironmentGradeService.addObj(secureEnvironmentGrade);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	
	/**
	 * 根据id查询安环等级信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentGradeById.do")
	@ResponseBody
	public SecureEnvironmentGrade querySecureEnvironmentGradeById(Long id) {
		SecureEnvironmentGrade c =  secureEnvironmentGradeService.queryObjById(id);
		return c;
	}
	/**
	 * 更新安环等级
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateSecureEnvironmentGrade.do")
	@ResponseBody
	public ModelMap updateSecureEnvironmentGrade(SecureEnvironmentGrade secureEnvironmentGrade) {
		ModelMap modelMap = new ModelMap();
		SecureEnvironmentGrade c4name = secureEnvironmentGradeService.queryByProperty("name", secureEnvironmentGrade.getName());
		if(c4name!=null && !c4name.getId().equals(secureEnvironmentGrade.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "安环等级名称已被使用");
		}else {
			secureEnvironmentGradeService.updateObj(secureEnvironmentGrade);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	
	/**
	 * 根据id删除安环等级
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSecureEnvironmentGrade.do")
	@ResponseBody
	public ModelMap deleteSecureEnvironmentGrade(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		secureEnvironmentGradeService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
} 
