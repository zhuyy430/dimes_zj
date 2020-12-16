package com.digitzones.mc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.MaintainProject;
import com.digitzones.devmgr.service.IMaintainProjectService;
import com.digitzones.devmgr.vo.MaintainProjectVO;
import com.digitzones.model.Pager;
import com.digitzones.model.ProjectType;
@RestController
@RequestMapping("/mcMaintainProject")
public class MCMaintainProjectController {
	@Autowired
	private IMaintainProjectService maintainProjectService;
	
	/**
	 * 分页查询设备报修
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMaintainProject.do")
	@ResponseBody
	public ModelMap queryMaintainProject(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceRepairOrderId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<MaintainProject> pager = maintainProjectService.queryObjs("select m from MaintainProject m inner join m.deviceRepair d "
				+ "where d.id=?0", page, rows, new Object[] {deviceRepairOrderId});
		List<MaintainProject> data = pager.getData();
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addMaintainProject.do")
	@ResponseBody
	public ModelMap addMaintainProject(MaintainProject maintainProject){
		ModelMap modelMap = new ModelMap();
		maintainProject.setCreateDate(new Date());
		maintainProjectService.addObj(maintainProject);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryMaintainProjectById.do")
	@ResponseBody
	public MaintainProjectVO queryMaintainProjectById(Long id){
		MaintainProject maintainProject = maintainProjectService.queryObjById(id);
		return model2VO(maintainProject);
	}
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/updateMaintainProject.do")
	@ResponseBody
	public ModelMap updateMaintainProject(MaintainProject maintainProject){
		ModelMap modelMap = new ModelMap();
		MaintainProject m = maintainProjectService.queryObjById(maintainProject.getId());
		m.setNote(maintainProject.getNote());
		m.setRemark(maintainProject.getRemark());
		m.setProcessingMethod(maintainProject.getProcessingMethod());
		m.setCode(maintainProject.getCode());
		m.setName(maintainProject.getName());
		ProjectType ptype = new ProjectType();
		ptype.setId(maintainProject.getType().getId());
		m.setType(ptype);
		maintainProjectService.updateObj(m);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/deleteMaintainProjectById.do")
	@ResponseBody
	public ModelMap deleteMaintainProjectById(String id){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		maintainProjectService.deleteObj(Long.parseLong(id));
		
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "删除成功!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	private MaintainProjectVO model2VO(MaintainProject maintainProject) {
		if(maintainProject == null) {
			return null;
		}
		MaintainProjectVO vo = new MaintainProjectVO();
		vo.setId(maintainProject.getId());
		vo.setCode(maintainProject.getCode());
		vo.setName(maintainProject.getName());
		vo.setNote(maintainProject.getNote());
		vo.setProcessingMethod(maintainProject.getProcessingMethod());
		vo.setRemark(maintainProject.getRemark());
		if(maintainProject.getType()!=null){
			vo.setTypeCode(maintainProject.getType().getCode());
			vo.setTypeId(maintainProject.getType().getId());
			vo.setTypeName(maintainProject.getType().getName());
		}
		return vo;
	}
}
