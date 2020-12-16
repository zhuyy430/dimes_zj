package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.NGProcessMethod;
import com.digitzones.service.INGProcessMethodService;
/**
 * 不合格处理详情控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/ngProcessMethod")
public class NGProcessMethodController {
	private INGProcessMethodService ngProcessMethodService;
	@Autowired
	public void setNgProcessMethodService(INGProcessMethodService ngProcessMethodService) {
		this.ngProcessMethodService = ngProcessMethodService;
	}
	/**
	 * 根据不合格记录id查找不合格处理详情
	 * @param ngRecordId
	 * @return
	 */
	@RequestMapping("/queryNGProcessMethodsByNGRecordId.do")
	@ResponseBody
	public List<NGProcessMethod> queryNGProcessMethodsByNGRecordId(Long ngRecordId){
		return ngProcessMethodService.queryNGProcessMethodsByNGRecordId(ngRecordId);
	}
	/**
	 * 根据id查找不合格处理详情对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryNGProcessMethodById.do")
	@ResponseBody
	public NGProcessMethod queryNGProcessMethodById(Long id) {
		NGProcessMethod ng = ngProcessMethodService.queryObjById(id);
		if(ng.getProcessMethod().equals("scrap")){
			ng.setProcessMethod("报废");
		}else if(ng.getProcessMethod().equals("repair")){
			ng.setProcessMethod("返修");
		}else if(ng.getProcessMethod().equals("compromise")){
			ng.setProcessMethod("让步接收");
		}else{
			ng.setProcessMethod("");
		}
		return ng;
	}
	/**
	 * 更新不合格品处理详情
	 * @param method
	 * @return
	 */
	@RequestMapping("/updateNGProcessMethod.do")
	@ResponseBody
	public ModelMap updateNGProcessMethod(NGProcessMethod method) {
		ModelMap modelMap = new ModelMap();
		NGProcessMethod ngm = ngProcessMethodService.queryObjById(method.getId());
		ngm.setNgCount(method.getNgCount());
		
		ngProcessMethodService.updateObj(ngm);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
} 
