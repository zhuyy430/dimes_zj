package com.digitzones.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintainProject;
import com.digitzones.devmgr.service.IMaintainProjectService;
import com.digitzones.model.Pager;
import com.digitzones.model.ProjectType;


/**
 * 
 * 维修项目
 *
 */
@Controller
@RequestMapping("/AppMaintainProject")
public class AppMaintainProjectController {
	@Autowired
	private IMaintainProjectService maintainProjectService;
	
	/**
	 * 根据报修单id查询维修项目
	 * @return
	 */
	@RequestMapping("/queryMaintainProject.do")
	@ResponseBody
	public List<MaintainProject> queryMaintainProject(Long deviceRepairOrderId,HttpServletRequest request){
		return maintainProjectService.queryMaintainProject(deviceRepairOrderId);
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addMaintainProject.do")
	@ResponseBody
	public ModelMap addMaintainProject(MaintainProject maintainProject,Long deviceRepair_id,Long type_id){
		ModelMap modelMap = new ModelMap();
		ProjectType type=new ProjectType();
		type.setId(type_id);
		DeviceRepair deviceRepair =new DeviceRepair();
		deviceRepair.setId(deviceRepair_id);
		maintainProject.setType(type);
		maintainProject.setDeviceRepair(deviceRepair);
		maintainProject.setCreateDate(new Date());
		maintainProjectService.addObj(maintainProject);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
}
