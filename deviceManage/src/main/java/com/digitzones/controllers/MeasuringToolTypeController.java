package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 装备类型管理控制器
 * @author zdq
 * 2018年6月7日
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.EquipmentType;
import com.digitzones.model.Pager;
import com.digitzones.service.IMeasuringToolTypeService;
import com.digitzones.vo.EquipmentTypeVO;
@Controller
@RequestMapping("/measuringToolType")
public class MeasuringToolTypeController {
	private IMeasuringToolTypeService measuringToolTypeService;
	@Autowired
	public void setMeasuringToolTypeService(IMeasuringToolTypeService measuringToolTypeService) {
		this.measuringToolTypeService = measuringToolTypeService;
	}
	/**
	 * 查询生产单元
	 * @return
	 */
	@RequestMapping("/queryTopEquipmentTypes.do")
	@ResponseBody
	public List<EquipmentType> queryTopEquipmentTypes(){
		return measuringToolTypeService.queryTopEquipmentType();
	}
	/**
	 * 分页查询装备类型
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentTypesByParentId.do")
	@ResponseBody
	public ModelMap queryEquipmentTypesByParentId(@RequestParam(value="pid",required=false)Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = measuringToolTypeService.queryObjs("from EquipmentType pt inner join pt.parent p  where p.id=?0", page, rows, new Object[] {pid});
		Pager<EquipmentTypeVO> pagerEquipmentTypeVO = new Pager<>();
		model2VO(pager, pagerEquipmentTypeVO);
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pagerEquipmentTypeVO.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}

	private void model2VO(Pager<Object[]> pagerEquipmentType,Pager<EquipmentTypeVO> measuringToolTypeVO) {
		List<Object[]> measuringToolTypes = pagerEquipmentType.getData();
		List<EquipmentTypeVO> measuringToolTypeVOs = measuringToolTypeVO.getData();
		for(Object[] obj:measuringToolTypes) {
			EquipmentTypeVO vo = new EquipmentTypeVO();
			EquipmentType son = (EquipmentType) obj[0];
			EquipmentType parent = (EquipmentType) obj[1];

			vo.setId(son.getId());
			vo.setName(son.getName());
			parent.setChildren(null);
			vo.setParent(parent);
			vo.setCode(son.getCode());
			vo.setDisabled(son.getDisabled());
			vo.setNote(son.getNote());
			vo.setBaseCode(son.getBaseCode());
			vo.setManufacturer(son.getManufacturer());
			vo.setMeasurementType(son.getMeasurementType());
			vo.setOutFactoryDate(son.getOutFactoryDate());
			vo.setMeasurementObjective(son.getMeasurementObjective());
			vo.setTrader(son.getTrader());
			vo.setUnitType(son.getUnitType());
			measuringToolTypeVOs.add(vo);

		}
	}
	/**
	 * 添加装备类型
	 * @return
	 */
	@RequestMapping("/addEquipmentType.do")
	@ResponseBody
	public ModelMap addEquipmentType(EquipmentType measuringToolType) {
		ModelMap modelMap = new ModelMap();
		EquipmentType measuringToolType4Code = measuringToolTypeService.queryByProperty("code", measuringToolType.getCode());
		if(measuringToolType4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "装备类别编码已被使用");
		}else {
			measuringToolTypeService.addObj(measuringToolType);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询装备类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryEquipmentTypeById.do")
	@ResponseBody
	public EquipmentType queryEquipmentTypeById(Long id) {
		EquipmentType measuringToolType = measuringToolTypeService.queryObjById(id);
		return measuringToolType;
	}
	/**
	 * 更新装备类型
	 * @return
	 */
	@RequestMapping("/updateEquipmentType.do")
	@ResponseBody
	public ModelMap updateEquipmentType(EquipmentType measuringToolType) {
		ModelMap modelMap = new ModelMap();
		measuringToolTypeService.updateObj(measuringToolType);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 根据id删除装备类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEquipmentType.do")
	@ResponseBody
	public ModelMap deleteEquipmentType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Long count = measuringToolTypeService.queryCountOfSubEquipmentType(Long.valueOf(id));
		Long measuringToolCount = measuringToolTypeService.queryCountOfEquipment(Long.valueOf(id));
		if(count>0 || measuringToolCount>0) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "该装备类型下存在子类型或装备，不允许删除!");
			modelMap.addAttribute("message", "该装备类型下存在子类型或装备，不允许删除!");
		}else {
			measuringToolTypeService.deleteObj(Long.valueOf(id));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		}
		return modelMap;
	}
	/**
	 * 停用该装备类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledEquipmentType.do")
	@ResponseBody
	public ModelMap disabledEquipmentType(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		EquipmentType d = measuringToolTypeService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		measuringToolTypeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
} 
