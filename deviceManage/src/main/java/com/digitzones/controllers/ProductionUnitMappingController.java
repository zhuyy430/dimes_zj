package com.digitzones.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.ProductionUnitMapping;
import com.digitzones.service.IProductionUnitMappingService;
import com.digitzones.vo.ProductionUnitMappingVO;

/**
 * 生产单元(产线)和客户端IP地址映射控制器
 * @author zdq
 * 2018年9月7日
 */
@RequestMapping("/productionUnitMapping")
@Controller
public class ProductionUnitMappingController {
	@Autowired
	private IProductionUnitMappingService productionUnitMappingService;
	/**
	 * 查询映射对象信息
	 * @return
	 */
	@RequestMapping("/queryProductionUnitMappings.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryProductionUnitMappings(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<ProductionUnitMapping> pager = productionUnitMappingService.queryObjs("from ProductionUnitMapping c ", page, rows, new Object[] {});

		List<ProductionUnitMappingVO> vos = new ArrayList<>();
		for(ProductionUnitMapping c : pager.getData()) {
			vos.add(model2VO(c));
		}
		modelMap.addAttribute("rows", vos);
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 添加映射对象信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addProductionUnitMapping.do")
	@ResponseBody
	public ModelMap addProductionUnitMapping(ProductionUnitMapping p) {
		ModelMap modelMap = new ModelMap();
		ProductionUnitMapping mapping = productionUnitMappingService.queryByProperty("clientIp", p.getClientIp());
		//如果该IP已存在映射，则更新
		if(mapping!=null) {
			mapping.setProductionUnit(p.getProductionUnit());
			productionUnitMappingService.updateObj(mapping);
		}else {
			productionUnitMappingService.addObj(p);
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功!");
		return modelMap;
	}

	/**
	 * 根据id查询映射对象信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryProductionUnitMappingById.do")
	@ResponseBody
	public ProductionUnitMappingVO queryProductionUnitMappingById(Long id) {
		ProductionUnitMapping c =  productionUnitMappingService.queryObjById(id);
		return model2VO(c);
	}

	private ProductionUnitMappingVO model2VO(ProductionUnitMapping c) {
		ProductionUnitMappingVO vo = new ProductionUnitMappingVO();
		vo.setId(c.getId());
		vo.setClientIp(c.getClientIp());
		if(c.getProductionUnit()!=null) {
			vo.setProductionUnitId(c.getProductionUnit().getId());
			vo.setProductionUnitCode(c.getProductionUnit().getCode());
			vo.setProductionUnitName(c.getProductionUnit().getName());
		}
		return vo;
	}

	/**
	 * 添加映射对象信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateProductionUnitMapping.do")
	@ResponseBody
	public ModelMap updateProductionUnitMapping(ProductionUnitMapping classes) {
		ModelMap modelMap = new ModelMap();
		productionUnitMappingService.updateObj(classes);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}

	/**
	 * 根据id删除映射对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteProductionUnitMapping.do")
	@ResponseBody
	public ModelMap deleteProductionUnitMapping(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		productionUnitMappingService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 查询当前的产线
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryProductionUnit.do")
	@ResponseBody
	public ProductionUnit queryProductionUnit(HttpServletRequest request) {
		String  clientIp = request.getRemoteAddr();
		ProductionUnitMapping mapping = productionUnitMappingService.queryByProperty("clientIp", clientIp);
		if(mapping == null) {
			return null;
		}
		return mapping.getProductionUnit();
	}
}
