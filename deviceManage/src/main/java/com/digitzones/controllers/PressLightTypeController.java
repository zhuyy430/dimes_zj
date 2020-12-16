package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 按灯类型管理控制器
 * @author zdq
 * 2018年6月7日
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.PressLightType;
import com.digitzones.service.IPressLightTypeService;
import com.digitzones.vo.PressLightTypeVO;
@Controller
@RequestMapping("/pressLightType")
public class PressLightTypeController {
	private IPressLightTypeService pressLightTypeService;
	@Autowired
	public void setPressLightTypeService(IPressLightTypeService pressLightTypeService) {
		this.pressLightTypeService = pressLightTypeService;
	}
	/**
	 * 查询顶层按灯类型
	 * @return
	 */
	@RequestMapping("/queryTopPressLightTypes.do")
	@ResponseBody
	public List<PressLightType> queryTopPressLightTypes(String type){
		return pressLightTypeService.queryTopPressLightType(type);
	}
	/**
	 * 查找第一级按灯类型，即level=1
	 * @return
	 */
	@RequestMapping("/queryFirstLevelType.do")
	@ResponseBody
	public List<PressLightType> queryFirstLevelType(String type){
		return pressLightTypeService.queryFirstLevelType(type);
	}
	/**
	 * 分页查询按灯类型
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPressLightTypesByParentId.do")
	@ResponseBody
	public ModelMap queryPressLightTypesByParentId(@RequestParam(value="pid",required=false)Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page
	,String type) {
		Pager<Object[]> pager = null;
		if(pid==null) {
			pager = pressLightTypeService.queryObjs("from PressLightType pt inner join pt.parent p where p.typeName=?0", page, rows, new Object[] {type});
		}else {
			pager = pressLightTypeService.queryObjs("from PressLightType pt inner join pt.parent p  where p.id=?0 and p.typeName=?1", page, rows, new Object[] {pid,type});
		}
		Pager<PressLightTypeVO> pagerParameterTypeVO = new Pager<>();
		model2VO(pager, pagerParameterTypeVO);
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pagerParameterTypeVO.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	/**
	 *根据父类别id查询子类别
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryAllPressLightTypesByParentId.do")
	@ResponseBody
	public List<PressLightType> queryAllPressLightTypesByParentId(@RequestParam(value="pid",required=false)Long pid,String type) {
		return pressLightTypeService.queryAllPressLightTypesByParentId(pid,type);
	}

	private void model2VO(Pager<Object[]> pagerParameterType,Pager<PressLightTypeVO> parameterTypeVO) {
		List<Object[]> parameterTypes = pagerParameterType.getData();
		List<PressLightTypeVO> parameterTypeVOs = parameterTypeVO.getData();
		for(Object[] obj:parameterTypes) {
			PressLightTypeVO vo = new PressLightTypeVO();
			PressLightType son = (PressLightType) obj[0];
			PressLightType parent = (PressLightType) obj[1];

			vo.setId(son.getId());
			vo.setName(son.getName());
			parent.setChildren(null);
			vo.setParent(parent);
			vo.setCode(son.getCode());
			vo.setDisabled(son.getDisabled());
			vo.setNote(son.getNote());
			parameterTypeVOs.add(vo);
		}
	}
	/**
	 * 添加按灯类别
	 * @param department
	 * @return
	 */
	@RequestMapping("/addPressLightType.do")
	@ResponseBody
	public ModelMap addPressLightType(PressLightType PressLightType) {
		ModelMap modelMap = new ModelMap();
		PressLightType parameterType4Code = pressLightTypeService.queryByProperty("code", PressLightType.getCode());
		if(parameterType4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "按灯类别编码已被使用");
		}else {
			PressLightType	parameterType4Name = pressLightTypeService.queryByProperty("name",PressLightType.getName());
			if(parameterType4Name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "按灯类别名称已被使用");
			}else {
				PressLightType plt = pressLightTypeService.queryObjById(PressLightType.getParent().getId());
				PressLightType.setLevel(plt.getLevel()+1);
				PressLightType.setText(PressLightType.getName());
				pressLightTypeService.addObj(PressLightType);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	/**
	 * 根据id查询按灯类别
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryPressLightTypeById.do")
	@ResponseBody
	public PressLightType queryPressLightTypeById(Long id) {
		PressLightType PressLightType = pressLightTypeService.queryObjById(id);
		return PressLightType;
	}
	/**
	 * 更新按灯类别
	 * @param PressLightType
	 * @return
	 */
	@RequestMapping("/updatePressLightType.do")
	@ResponseBody
	public ModelMap updatePressLightType(PressLightType pressLightType) {
		ModelMap modelMap = new ModelMap();
		PressLightType pu = pressLightTypeService.queryByProperty("name", pressLightType.getName());
		if(pu!=null && !pressLightType.getId().equals(pu.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "按灯类别名称已被使用");
		}else {
			PressLightType p = pressLightTypeService.queryObjById(pressLightType.getId());
			p.setName(pressLightType.getName());
			p.setNote(pressLightType.getNote());
			p.setText(pressLightType.getName());
			pressLightTypeService.updateObj(p);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除按灯类别
	 * @param id
	 * @return
	 */
	@RequestMapping("/deletePressLightType.do")
	@ResponseBody
	public ModelMap deletePressLightType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			Long count = pressLightTypeService.queryCountOfSubPressLightType(Long.valueOf(id));
			if(count>0) {
				throw new RuntimeException("存在子类别，不允许删除！");
			}

			if(pressLightTypeService.isExistPressLights(Long.valueOf(id))) {
				throw new RuntimeException("该类别正在使用中，不允许删除!");
			}
		}catch (RuntimeException e) {
			e.printStackTrace();
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", e.getMessage());
			return modelMap;
		}
		pressLightTypeService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该按灯类别
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledPressLightType.do")
	@ResponseBody
	public ModelMap disabledPressLightType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		PressLightType d = pressLightTypeService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		pressLightTypeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 查询所有按灯类型
	 * @return
	 */
	@RequestMapping("/queryAllPressLightType.do")
	@ResponseBody
	public List<PressLightType> queryAllPressLightType(String type){
		List<PressLightType> list = pressLightTypeService.queryAllPressLightTypes(type);

		return list;
	}
} 
