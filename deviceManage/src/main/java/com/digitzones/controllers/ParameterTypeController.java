package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 参数类型管理控制器
 * @author zdq
 * 2018年6月7日
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.ParameterType;
import com.digitzones.service.IParameterTypeService;
import com.digitzones.vo.ParameterTypeVO;
@Controller
@RequestMapping("/parameterType")
public class ParameterTypeController {
	private IParameterTypeService parameterTypeService;
	@Autowired
	public void setParameterTypeService(IParameterTypeService parameterTypeService) {
		this.parameterTypeService = parameterTypeService;
	}
	/**
	 * 查询生产单元
	 * @return
	 */
	@RequestMapping("/queryTopParameterTypes.do")
	@ResponseBody
	public List<ParameterType> queryTopParameterTypes(){
		return parameterTypeService.queryTopParameterType();
	}
	/**
	 * 分页查询参数类型
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryParameterTypesByParentId.do")
	@ResponseBody
	public ModelMap queryParameterTypesByParentId(@RequestParam(value="pid",required=false)Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = parameterTypeService.queryObjs("from ParameterType pt inner join pt.parent p  where p.id=?0", page, rows, new Object[] {pid});
		Pager<ParameterTypeVO> pagerParameterTypeVO = new Pager<>();
		model2VO(pager, pagerParameterTypeVO);
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pagerParameterTypeVO.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}

	private void model2VO(Pager<Object[]> pagerParameterType,Pager<ParameterTypeVO> parameterTypeVO) {
		List<Object[]> parameterTypes = pagerParameterType.getData();
		List<ParameterTypeVO> parameterTypeVOs = parameterTypeVO.getData();
		for(Object[] obj:parameterTypes) {
			ParameterTypeVO vo = new ParameterTypeVO();
			ParameterType son = (ParameterType) obj[0];
			ParameterType parent = (ParameterType) obj[1];

			vo.setId(son.getId());
			vo.setName(son.getName());
			parent.setChildren(null);
			vo.setParent(parent);
			vo.setCode(son.getCode());
			vo.setDisabled(son.getDisabled());
			vo.setNote(son.getNote());
			vo.setBaseCode(son.getBaseCode());
			parameterTypeVOs.add(vo);
			
		}
	}
	/**
	 * 添加参数类型
	 * @param department
	 * @return
	 */
	@RequestMapping("/addParameterType.do")
	@ResponseBody
	public ModelMap addParameterType(ParameterType parameterType) {
		ModelMap modelMap = new ModelMap();
		ParameterType parameterType4Code = parameterTypeService.queryByProperty("code", parameterType.getCode());
		if(parameterType4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "参数类别编码已被使用");
		}else {
			ParameterType	parameterType4Name = parameterTypeService.queryByProperty("name",parameterType.getName());
			if(parameterType4Name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "参数类别名称已被使用");
			}else {
				parameterTypeService.addObj(parameterType);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	/**
	 * 根据id查询参数类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryParameterTypeById.do")
	@ResponseBody
	public ParameterType queryParameterTypeById(Long id) {
		ParameterType parameterType = parameterTypeService.queryObjById(id);
		return parameterType;
	}
	/**
	 * 更新参数类型
	 * @param department
	 * @return
	 */
	@RequestMapping("/updateParameterType.do")
	@ResponseBody
	public ModelMap updateParameterType(ParameterType parameterType) {
		ModelMap modelMap = new ModelMap();
		ParameterType pu = parameterTypeService.queryByProperty("name", parameterType.getName());
		if(pu!=null && !parameterType.getId().equals(pu.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "参数名称已被使用");
		}else {
			parameterTypeService.updateObj(parameterType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除参数类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteParameterType.do")
	@ResponseBody
	public ModelMap deleteParameterType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Long count = parameterTypeService.queryCountOfSubParameterType(Long.valueOf(id));
		Long parameterCount = parameterTypeService.queryCountOfParameter(Long.valueOf(id));
		if(count>0 || parameterCount>0) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "该参数类型下存在子类型或参数，不允许删除!");
			modelMap.addAttribute("message", "该参数类型下存在子类型或参数，不允许删除!");
		}else {
			parameterTypeService.deleteObj(Long.valueOf(id));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		}
		return modelMap;
	}
	/**
	 * 停用该参数类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledParameterType.do")
	@ResponseBody
	public ModelMap disabledParameterType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		ParameterType d = parameterTypeService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());
		
		parameterTypeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
} 
