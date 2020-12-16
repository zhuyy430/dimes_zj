package com.digitzones.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.constants.Constant;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProjectTypeService;

@Controller
@RequestMapping("/AppProjectType")
public class AppProjectTypeController {
	@Autowired
	IProjectTypeService projectTypeService;
	
	/**
	 * 获取顶级设备类别
	 */
	@RequestMapping("/queryAllDeviceProjectType.do")
	@ResponseBody
	public List<ProjectType> queryAllDeviceProjectType(){
		
		return projectTypeService.queryTopProjectTypes(Constant.ProjectType.ROOTDEVICETYPE);
	}
	/**
	 *根据父类id查询子类
	 */
	@RequestMapping("/queryProjectTypeByParentId.do")
	@ResponseBody
	public List<ProjectType> queryProjectTypeByParentId(Long Id){
		
		return projectTypeService.queryProjectTypeByParentId(Id);
	}
}
