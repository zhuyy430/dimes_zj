package com.digitzones.devmgr.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.model.Warehouse;
import com.digitzones.devmgr.model.WarehouseGoodsMapping;
import com.digitzones.devmgr.service.ISparepartService;
import com.digitzones.devmgr.service.IWarehouseGoodsMappingService;
import com.digitzones.devmgr.service.IWarehouseService;
import com.digitzones.model.Pager;
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private ISparepartService sparepartService;
	@Autowired
	private IWarehouseGoodsMappingService warehouseGoodsMappingService;
	/**
	 * 根据备件id查找库存信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryWarehousesBySparepartId.do")
	public ModelMap queryWarehousesBySparepartId(Long relatedId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		if(relatedId==null) {
			return modelMap;
		}
		Sparepart sparepart = sparepartService.queryObjById(relatedId);
		/*Pager<Warehouse> pager = warehouseService.queryObjs("from Warehouse w where w.code in"
				+ " (select warehouseCode from WarehouseGoodsMapping wgm where wgm.goodCode=?0 and wgm.goodType=?1)", page, rows, new Object[] {sparepart.getCode(),Constant.GoodType.SPAREPARTTYPE});*/
		Pager<WarehouseGoodsMapping> pager = warehouseGoodsMappingService.queryObjs("from WarehouseGoodsMapping wgm where wgm.goodCode=?0 and wgm.goodType=?1", page, rows, new Object[] {sparepart.getCode(),Constant.GoodType.SPAREPARTTYPE});
		modelMap.addAttribute("rows",pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	
	@RequestMapping("/queryWarehouses.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryWarehouses(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<WarehouseGoodsMapping> pager = warehouseGoodsMappingService.queryObjs("from WarehouseGoodsMapping ", page, rows, new Object[] {});
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	@RequestMapping("/queryAllWarehouses.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryAllWarehouses(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<Warehouse> pager = warehouseService.queryObjs("from Warehouse ", page, rows, new Object[] {});
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
}
