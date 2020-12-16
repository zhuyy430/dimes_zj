package com.digitzones.procurement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.Pager;
import com.digitzones.procurement.model.CraftsRoute;
import com.digitzones.procurement.service.ICraftsRouteService;
import com.digitzones.service.ICraftsRouteProcessMappingService;

/**
 * 工艺路线控制器
 * @author zhuyy430
 *
 */
@RestController
@RequestMapping("/craftsRoute")
public class CraftsRouteController {
	@Autowired
	private ICraftsRouteService craftsRouteService;
	@Autowired
	private ICraftsRouteProcessMappingService craftsRouteProcessMappingService;
	/**
	 * 工艺路线信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("queryCraftsRoute.do")
	public ModelMap queryCraftsRoute(@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from CraftsRoute i where 1=1 ";

		Pager<CraftsRoute> pager = craftsRouteService.queryObjs(hql,page,rows,new Object[]{});
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 新增工艺路线信息
	 * @param craftsRoute
	 * @return
	 */
	@RequestMapping("addCraftsRoute.do")
	public ModelMap addCraftsRoute(CraftsRoute craftsRoute) {
		ModelMap modelMap = new ModelMap();
		CraftsRoute crafts = craftsRouteService.queryByProperty("code", craftsRoute.getCode());
		if(crafts!=null){
			
		}
		
		craftsRouteService.addObj(craftsRoute);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 更新工艺路线信息
	 * @param craftsRoute
	 * @return
	 */
	@RequestMapping("updateCraftsRoute.do")
	public ModelMap updateCraftsRoute(CraftsRoute craftsRoute) {
		ModelMap modelMap = new ModelMap();
		CraftsRoute old = craftsRouteService.queryByProperty("id", craftsRoute.getId());
		old.setName(craftsRoute.getName());
		old.setVersion(craftsRoute.getVersion());
		old.setNote(craftsRoute.getNote());
		craftsRouteService.updateObj(old);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 查询工艺路线信息
	 * @param id
	 * @return
	 */
	@RequestMapping("queryCraftsRouteById.do")
	public CraftsRoute queryCraftsRouteById(String id) {
		return craftsRouteService.queryByProperty("id", id);
	}
	/**
	 * 删除工艺路线信息
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteCraftsRoute.do")
	public ModelMap deleteCraftsRoute(String id) {
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		craftsRouteService.deleteObjById(id);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "删除成功!");
		return modelMap;
	}

	/**
	 * 停用工艺路线信息
	 * @param id
	 * @return
	 */
	@RequestMapping("disabledCraftsRoute.do")
	public ModelMap disabledCraftsRoute(String id) {
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		CraftsRoute crafts = craftsRouteService.queryByProperty("id", id);
		crafts.setDisabled(!crafts.getDisabled());
		craftsRouteService.updateObj(crafts);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 为工艺路线添加工序
	 * @param workpieceId
	 * @param processesId
	 * @return
	 */
	@RequestMapping("/addProcesses4CraftsRoute.do")
	@ResponseBody
	public ModelMap addProcesses4Workpiece(String craftsId,String processesId) {
		ModelMap modelMap = new ModelMap();
		if(processesId!=null) {
			try {
				craftsRouteProcessMappingService.addCraftsProcessMapping(craftsId, processesId);
			} catch (RuntimeException e) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", e.getMessage());	
				return modelMap;
			}
			
			modelMap.addAttribute("msg", "操作完成！");
			modelMap.addAttribute("success",true);
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 删除工序和工件的关联
	 * @param cdm
	 * @return
	 */
	@RequestMapping("/deleteProcessesFromCraftsRoute.do")
	@ResponseBody
	public ModelMap deleteDeviceSiteFromProcesses(String craftsId,String processesId) {
		if(processesId.contains("'")) {
			processesId = processesId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		craftsRouteProcessMappingService.deleteByCraftsIdAndProcessId(craftsId,Long.valueOf(processesId));
		modelMap.addAttribute("message", "删除成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	
	/**
	 * 上移工件工序关联的工艺路线
	 * @param cdm
	 * @return
	 */
	@RequestMapping("/updateShiftUpProcessRoute.do")
	@ResponseBody
	public ModelMap updateShiftUpProcessRoute(String craftsId,String processesId,String c_pMappingId) {
		if(processesId.contains("'")) {
			processesId = processesId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			craftsRouteProcessMappingService.updateShiftUpProcessRoute(craftsId, Long.valueOf(processesId),c_pMappingId);
		} catch (RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("title", "操作提示");
			return modelMap;
		}
		
		modelMap.addAttribute("message", "移动成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	
	/**
	 * 下移工件工序关联的工艺路线
	 * @param cdm
	 * @return
	 */
	@RequestMapping("/updateShiftDownProcessRoute.do")
	@ResponseBody
	public ModelMap updateShiftDownProcessRoute(String craftsId,String processesId,String c_pMappingId) {
		if(processesId.contains("'")) {
			processesId = processesId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			craftsRouteProcessMappingService.updateShiftDownProcessRoute(craftsId, Long.valueOf(processesId),c_pMappingId);
		} catch (RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", e.getMessage());	
			modelMap.addAttribute("title", "操作提示");
			return modelMap;
		}
		modelMap.addAttribute("message", "移动成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
}
