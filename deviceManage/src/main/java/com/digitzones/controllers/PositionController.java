package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.Position;
/**
 * 部门管理控制器
 * @author zdq
 * 2018年6月7日
 */
import com.digitzones.service.IPositionService;
@Controller
@RequestMapping("/position")
public class PositionController {
	private IPositionService positionService;
	@Autowired
	public void setPositionService(IPositionService positionService) {
		this.positionService = positionService;
	}
	/**
	 * 分页查询职位
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPositionsByDepartmentId.do")
	@ResponseBody
	public ModelMap queryPositionsByDepartmentId(@RequestParam("id")Long deptId,@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Position> pager = positionService.queryObjs("select p from Position p inner join p.department d where d.id=?0 order by p.code", page, rows,new Object[] {deptId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}

	@RequestMapping("/queryPositions.do")
	@ResponseBody
	public List<Position> queryPositionsByDepartmentId(@RequestParam("deptid")Long deptId) {
		return positionService.queryAllByHql("select p from Position p inner join p.department d where d.id=?0 and p.disabled!=?1", new Object[] {deptId,true});
	}
	/**
	 * 添加部门
	 * @param department
	 * @return
	 */
	@RequestMapping("/addPosition.do")
	@ResponseBody
	public ModelMap addPosition(Position position) {
		ModelMap modelMap = new ModelMap();
		//检测职位编码和名称是否重复
		Position position4Code = positionService.queryByProperty("code", position.getCode());
		if(position4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "职位编码已被使用");
		}else {
			positionService.addPosition(position);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}

	/**
	 * 根据id查询职位
	 * @param department
	 * @return
	 */
	@RequestMapping("/queryPositionById.do")
	@ResponseBody
	public Position queryPositionById(Long id) {
		Position position = positionService.queryPositionById(id);
		return position;
	}

	/**
	 * 更新职位
	 * @param position
	 * @return
	 */
	@RequestMapping("/updatePosition.do")
	@ResponseBody
	public ModelMap updatePosition(Position position) {
		ModelMap modelMap = new ModelMap();
		Position d = positionService.queryByProperty("name", position.getName());
		if(d!=null && !position.getId().equals(d.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "职位名称已被使用");
		}else {
			positionService.updateObj(position);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除职位
	 * @param id
	 * @return
	 */
	@RequestMapping("/deletePosition.do")
	@ResponseBody
	public ModelMap deletePosition(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		if(positionService.isExistEmployees(Long.valueOf(id))) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "成功删除!");
			modelMap.addAttribute("message", "该岗位下存在员工，不允许删除!");
			return modelMap;
		}
		positionService.deletePosition(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该职位
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledPosition.do")
	@ResponseBody
	public ModelMap disabledPosition(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Position d = positionService.queryPositionById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		positionService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
} 
