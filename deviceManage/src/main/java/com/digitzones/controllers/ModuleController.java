package com.digitzones.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.digitzones.model.Module;
import com.digitzones.service.IModuleService;
import com.digitzones.vo.ModuleVO;

/**
 * 功能模块控制器
 * @author zdq
 * 2018年6月6日
 */
@Controller
@RequestMapping("/module")
public class ModuleController {
	private IModuleService moduleService;
	@Autowired
	public void setModuleService(IModuleService moduleService) {
		this.moduleService = moduleService;
	}
	/**
	 * 查找顶层模块 信息
	 * @return
	 */
	@RequestMapping("/queryTopModule.do")
	@ResponseBody
	public List<ModuleVO> queryTopModule(Principal principal){
		List<Module> modules = moduleService.queryTopModule(principal.getName());
		return model2Vo(modules);
	}
	@RequestMapping("/querySubModule.do")
	@ResponseBody
	public List<ModuleVO> querySubModule(@RequestParam("pid")Long pid,Principal principal){
		List<Module> list = moduleService.querySubModule(pid,principal==null?"":principal.getName() );
		return model2Vo(list);
	}
	/**
	 * model转换为vo
	 * @param modules
	 * @return
	 */
	private List<ModuleVO> model2Vo(List<Module> modules){
		if(modules==null) {
			return null;
		}
		
		List<ModuleVO> voList = new ArrayList<>();
		for(int i = 0;i<modules.size();i++) {
			ModuleVO vo = new ModuleVO();
			Module module = modules.get(i);
			vo.setIconCls(module.getIcon());
			vo.setId(module.getId());
			vo.setText(module.getName());
			vo.setUrl(module.getUrl());
			vo.setPid(module.getParent()==null?null:module.getParent().getId());
			vo.setLeaf(module.getLeaf());
			voList.add(vo);
		}
		return voList;
	}
} 
