package com.digitzones.controllers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.service.IDeviceProjectService;
import com.digitzones.model.Pager;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProjectTypeService;
import com.digitzones.vo.ProjectTypeVO;
@Controller
@RequestMapping("/projectType")
public class ProjectTypeController {
	@Autowired
	private IProjectTypeService projectTypeService;
	@Autowired
	private IDeviceProjectService deviceProjectService;
	/**
	 * @return
	 */
	@RequestMapping("/queryProjectTypes.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryProjectTypes(Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<Object[]> pager = projectTypeService.queryObjs("select c.parent,c from ProjectType c inner join "
				+ " c.parent where c.parent.id=?0 order by c.code", page, rows, new Object[] {pid});
		List<Object[]> data = pager.getData();
		List<ProjectTypeVO> ProjectTypeList = new ArrayList<>();
		for(Object[] o :data) {
			Object parent = o[0];
			ProjectType child = (ProjectType)o[1];
			if(parent!=null) {
				child.setParent((ProjectType)parent);
			}
			ProjectTypeList.add(model2VO(child));
		}
		modelMap.addAttribute("rows", ProjectTypeList);
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	private ProjectTypeVO model2VO(ProjectType ProjectType) {
		if(ProjectType == null) {
			return null;
		}
		ProjectTypeVO vo = new ProjectTypeVO();
		vo.setId(ProjectType.getId());
		vo.setCode(ProjectType.getCode());
		vo.setName(ProjectType.getName());
		vo.setDisabled(ProjectType.getDisabled());
		vo.setParent(ProjectType.getParent());
		vo.setNote(ProjectType.getNote());
		return vo;
	}
	/**
	 * @return
	 */
	@RequestMapping("/queryTopProjectTypes.do")
	@ResponseBody
	public List<ProjectType> queryTopProjectTypes(String rootType){
		List<ProjectType> projectTypeList = projectTypeService.queryTopProjectTypes(rootType);
		return projectTypeList;
	}
	/**
	 * @return
	 */
	@RequestMapping("/queryTopOneProjectTypes.do")
	@ResponseBody
	public List<ProjectType> queryTopOneProjectTypes(String rootType){
		List<ProjectType> projectTypeList = projectTypeService.queryTopProjectTypes(rootType);
		return projectTypeList.get(0).getChildren();
	}
	/**
	 * @return
	 */
	@RequestMapping("/queryProjectTypesByType.do")
	@ResponseBody
	public List<ProjectType> queryProjectTypesByType(String type){
		List<ProjectType> projectTypeList = projectTypeService.queryProjectTypesByType(type);
		return projectTypeList;
	}
	/**
	 * @return
	 */
	@RequestMapping("/queryAllProjectType.do")
	@ResponseBody
	public List<ProjectType> queryAllProjectType() {
		return projectTypeService.queryAllProjectType();
	}
	/**
	 * 添加设备类型
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addProjectType.do")
	@ResponseBody
	public ModelMap addProjectType(ProjectType ProjectType) {
		ModelMap modelMap = new ModelMap();
		ProjectType c4code = projectTypeService.queryByProperty("code", ProjectType.getCode());
		if(c4code!=null && c4code.getType().equals(ProjectType.getType())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "类型编码已被使用");
		}else  {
			ProjectType c4name = projectTypeService.queryByProperty("name", ProjectType.getName());
			if(c4name!=null && c4name.getType().equals(ProjectType.getType())) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "类型名称已被使用");
			}else {
				projectTypeService.addObj(ProjectType);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryProjectTypeById.do")
	@ResponseBody
	public ProjectType queryProjectTypeById(Long id) {
		ProjectType c =  projectTypeService.queryObjById(id);
		return c;
	}
	/**
	 * @param ProjectType
	 * @return
	 */
	@RequestMapping("/updateProjectType.do")
	@ResponseBody
	public ModelMap updateDevice(ProjectType ProjectType) {
		ModelMap modelMap = new ModelMap();

		ProjectType c4name = projectTypeService.queryByProperty("name", ProjectType.getName());
		if(c4name!=null && !c4name.getId().equals(ProjectType.getId()) && c4name.getType().equals(ProjectType.getType())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备类型名称已被使用");
		}else {
			projectTypeService.updateObj(ProjectType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	
	/**
	 * 根据id删除设备类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteProjectType.do")
	@ResponseBody
	public ModelMap deleteProjectType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		boolean exist = deviceProjectService.queryDeviceProjectByProjectTypeId(Long.parseLong(id));
		if(exist){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 333);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "操作失败:该类别下存在设备项目!");
			return modelMap;
		}
		projectTypeService.deleteObj(Long.valueOf(id));
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该设备类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledProjectType.do")
	@ResponseBody
	public ModelMap disabledProjectType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		ProjectType d = projectTypeService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		projectTypeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
} 
