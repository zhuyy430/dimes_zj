package com.digitzones.mc.controller;

import com.digitzones.mc.model.MCWarehouse;
import com.digitzones.mc.service.IMCWarehouseService;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/mcWarehouse")
public class MCWarehouseController {
	@Autowired
	private IMCWarehouseService mcWarehouseService;
	@Autowired
	private IWarehouseService warehouseService;
	/**
	 * 获取关联设备站点信息
	 * @return
	 */
	@RequestMapping("/queryMCWarehouse.do")
	@ResponseBody
	public MCWarehouse queryMCWarehouse(HttpServletRequest request) {
		String clientIp = request.getRemoteAddr();
		return mcWarehouseService.queryMCWarehouse(clientIp);
	}
	/**
	 * 获取关联设备站点信息
	 * @return
	 */
	@RequestMapping("/addOrUpdateMCWarehouse.do")
	@ResponseBody
	public ModelMap addOrUpdateMCWarehouse(HttpServletRequest request,String cwhCode) {
		ModelMap modelMap = new ModelMap();
		String clientIp = request.getRemoteAddr();
		Warehouse warehouse = warehouseService.queryObjById(cwhCode);
		if (null == warehouse) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "仓库不存在，请确认！");
				return modelMap;
		}
		MCWarehouse mcWarehouse = mcWarehouseService.queryMCWarehouse(clientIp);
		if(null==mcWarehouse){
			mcWarehouse = new MCWarehouse();
			mcWarehouse.setClientIp(clientIp);
			mcWarehouse.setcWhCode(warehouse.getcWhCode());
			mcWarehouse.setcWhName(warehouse.getcWhName());
			mcWarehouseService.addMCWarehouse(mcWarehouse);
		}else {
			mcWarehouse.setClientIp(clientIp);
			mcWarehouse.setcWhCode(warehouse.getcWhCode());
			mcWarehouse.setcWhName(warehouse.getcWhName());
			mcWarehouseService.updateMCWarehouse(mcWarehouse);
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功");
		return modelMap;
	}
}
