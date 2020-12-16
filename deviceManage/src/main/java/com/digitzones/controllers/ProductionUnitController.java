package com.digitzones.controllers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.digitzones.model.*;
import com.digitzones.service.IClassesTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IProductionUnitService;
import com.digitzones.vo.Node;
import com.digitzones.vo.NodeVO;
import com.digitzones.vo.ProductionUnitVO;
/**
 * 生产单元管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/productionUnit")
public class ProductionUnitController {
	private IProductionUnitService productionUnitService;
	private IDeviceService deviceService;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	private IClassesTypeService classesTypeService;
	@Autowired
	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}
	@Autowired
	public void setProductionUnitService(IProductionUnitService productionUnitService) {
		this.productionUnitService = productionUnitService;
	}
	/**
	 * 查询生产单元
	 * @return
	 */
	@RequestMapping("/queryTopProductionUnits.do")
	@ResponseBody
	public List<Node> queryTopProductionUnits(){
		List<Node> nodes = new ArrayList<Node>();
			//查找生产单元树形结构
			ProductionUnit pus = productionUnitService.queryParentProductionUnit();
			List<ProductionUnit> list = new ArrayList<>();
			list.add(pus);
		buildTree(nodes,list);
		return nodes;
	}
	/**
	 * 构建树状结构
	 * @param nodes
	 */
	private void buildTree(List<Node> nodes,List<ProductionUnit> topProductionUnits) {
			for(ProductionUnit pu : topProductionUnits) {
				Node node = new Node();
				node.setId(pu.getId());
				node.setName(pu.getName());
				node.setCode(pu.getCode());
				nodes.add(node);
				List<Node> nodeList = new ArrayList<Node>();
				node.setChildren(nodeList);
				List<ProductionUnit> list = productionUnitService.queryByParentId(pu.getId());
				if(!CollectionUtils.isEmpty(list)) {
					node.setState("closed");
					buildTree(nodeList, list);
				}
			}
	}
	/**
	 * 查找设备站点树，结构为生产单元-->设备-->设备站点
	 * @return
	 */
	@RequestMapping("/queryDeviceSiteTree.do")
	@ResponseBody
	public List<Node> queryDeviceSiteTree(String module) {
		//查找生产单元树形结构
		List<ProductionUnit> pus = productionUnitService.queryTopProductionUnits();
		List<Node> nodes = new ArrayList<>();
		objList2NodeList(nodes, pus,module);
		return nodes;
	}
	/**
	 * 查找设备站点树，结构为生产单元-->设备-->设备站点
	 * @return
	 */
	@RequestMapping("/queryProductionUnitSiteTree.do")
	@ResponseBody
	public List<NodeVO> queryProductionUnitSiteTree() {
		//查找生产单元树形结构
		List<ProductionUnit> pus = productionUnitService.queryTopProductionUnits();
		List<NodeVO> nodes = new ArrayList<>();
		objList2NodeVOList(nodes, pus);
		return nodes;
	}
	/**
	 * 查询顶级生产单元
	 * @return
	 */
	@RequestMapping("/queryProductionUnitDeviceTree.do")
	@ResponseBody
	public List<Node> queryProductionUnitDeviceTree(String module,Long parentId) {
		List<Node> nodes = new ArrayList<>();
		if(parentId==null) {
			//查找生产单元树形结构
			ProductionUnit pus = productionUnitService.queryParentProductionUnit();
			if(pus!=null) {
				Node node = new Node();
				node.setCode(pus.getCode());
				node.setName(pus.getName());
				node.setId(pus.getId());
				node.setChildren(queryByParentId(node.getId(),module));
				nodes.add(node);
			}
		}else {
			nodes = queryByParentId(parentId,module);
		}
		return nodes;
	}

	/**
	 * 根据父id查找子生产单元 ，如果没查询到，则查询该生产单元下的设备信息
	 * @param parentId
	 * @return
	 */
	private List<Node> queryByParentId(Long parentId,String module){
		List<Node> children = new ArrayList<>();
		List<ProductionUnit> childrenList = productionUnitService.queryByParentId(parentId);
		if(!CollectionUtils.isEmpty(childrenList)) {
			Iterator<ProductionUnit> it = childrenList.iterator();
			while(it.hasNext()) {
				ProductionUnit pu = it.next();
				Node childNode = new Node();
				childNode.setCode(pu.getCode());
				childNode.setName(pu.getName());
				childNode.setState("closed");
				childNode.setId(pu.getId());
				children.add(childNode);
			}
		}else {
			if(module!=null) {
				//查询设备
				List<Device> list = deviceService.queryDevicesByProductionUnitIdAndModule(parentId, module);
				if(!CollectionUtils.isEmpty(list)) {
					Iterator<Device> it = list.iterator();
					while(it.hasNext()) {
						Device device = it.next();
						Node node = new Node();
						node.setId(device.getId());
						node.setCode(device.getCode());
						node.setName(device.getName());

						children.add(node);
					}
				}
			}
		}
		return children;
	}
	/*	@RequestMapping("/queryProductionUnitDeviceTree.do")
	@ResponseBody
	public List<Node> queryProductionUnitDeviceTree(String module) {
		//查找生产单元树形结构
		List<ProductionUnit> pus = productionUnitService.queryTopProductionUnits();
		List<Node> nodes = new ArrayList<>();
		objList2DeviceList(nodes, pus,module);
		return nodes;
	}
	 */	/**
	 * 查找设备站点树，结构为生产单元-->设备
	 * @return
	 */
	@RequestMapping("/queryParentProductionUnit.do")
	@ResponseBody
	public ProductionUnit queryParentProductionUnit(String module) {
		return productionUnitService.queryParentProductionUnit();
	}
	/**
	 * 构建树形结构，结构为：生产单元-->设备-->设备站点
	 * @param nodes
	 * @param pus
	 */
	private void objList2NodeList(List<Node> nodes,Collection<ProductionUnit> pus,String module) {
		for(ProductionUnit pu : pus) {
			Node node = new Node();
			node.setId(pu.getId());
			node.setName(pu.getName());
			node.setCode(pu.getCode());
			nodes.add(node);
			List<Node> nodeList = new ArrayList<Node>();
			node.setChildren(nodeList);
			List<ProductionUnit> list = productionUnitService.queryByParentId(pu.getId());
			if(!CollectionUtils.isEmpty(list)) {
				objList2NodeList(nodeList, list,module);
				node.setState("closed");
			}
			//根据生产单元id查询设备
			List<Device> devices = deviceService.queryDevicesByProductionUnitIdAndModule(pu.getId(),module);
			for(Device d : devices) {
				Node no = new Node();
				no.setId(d.getId());
				no.setName(d.getCode() + "-" + d.getName());
				no.setCode(d.getCode());
				nodeList.add(no);
				List<Node> dsList = new ArrayList<>();
				no.setChildren(dsList);
				Set<DeviceSite> deviceSites = d.getDeviceSites();
				for(DeviceSite ds : deviceSites) {
					Node n = new Node();
					no.setState("closed");
					n.setId(ds.getId());
					n.setName(ds.getCode() + "-" + ds.getName());
					n.setCode(ds.getCode());
					dsList.add(n);
				}
			}
		}
	}
	/**
	 * 构建树形结构，结构为：生产单元-->设备
	 * @param nodes
	 * @param pus
	 * @param module  值为：dimes,deviceManage
	 */
	private void objList2DeviceList(List<Node> nodes,Collection<ProductionUnit> pus,String module) {
		for(ProductionUnit pu : pus) {
			Node node = new Node();
			node.setId(pu.getId());
			node.setName(pu.getName());
			node.setCode(pu.getCode());
			nodes.add(node);
			List<Node> nodeList = new ArrayList<Node>();
			node.setChildren(nodeList);
			List<ProductionUnit> list = productionUnitService.queryByParentId(pu.getId());
			if(!CollectionUtils.isEmpty(list)) {
				objList2DeviceList(nodeList, list,module);
			}
			//根据生产单元id查询设备
			List<Device> devices = deviceService.queryDevicesByProductionUnitIdAndModule(pu.getId(),module);
			for(Device d : devices) {
				Node no = new Node();
				no.setUnitType(d.getUnitType());
				no.setId(d.getId());
				no.setName(d.getName());
				no.setCode(d.getCode());
				nodeList.add(no);
			}
		}
	}
	/**
	 * 构建树形结构，结构为：生产单元-->设备-->设备站点
	 * @param nodes
	 * @param pus
	 */
	private void objList2NodeVOList(List<NodeVO> nodes,Collection<ProductionUnit> pus) {
		for(ProductionUnit pu : pus) {
			NodeVO node = new NodeVO();
			node.setId(pu.getId());
			node.setText(pu.getName());
			node.setCode(pu.getCode());
			nodes.add(node);
			List<NodeVO> nodeList = new ArrayList<NodeVO>();
			node.setChildren(nodeList);
			List<ProductionUnit> list = productionUnitService.queryByParentId(pu.getId());
			if(!CollectionUtils.isEmpty(list)) {
				objList2NodeVOList(nodeList, list);
			}
		}
	}
	/**
	 * 分页查询生产单元
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProductionUnitsByParentId.do")
	@ResponseBody
	public ModelMap queryProductionUnitsByParentId(@RequestParam(value="pid",required=false)Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = productionUnitService.queryObjs("from ProductionUnit pu inner join pu.parent p  where p.id=?0 order by pu.code", page, rows, new Object[] {pid});

		Pager<ProductionUnitVO> pagerProductionUnitVO = new Pager<>();
		model2VO(pager, pagerProductionUnitVO);
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pagerProductionUnitVO.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}

	private void model2VO(Pager<Object[]> pagerProductionUnit,Pager<ProductionUnitVO> ProductionUnitVO) {
		List<Object[]> productionUnits = pagerProductionUnit.getData();
		List<ProductionUnitVO> productionUnitVOs = ProductionUnitVO.getData();
		for(Object[] obj:productionUnits) {
			ProductionUnitVO vo = new ProductionUnitVO();
			ProductionUnit son = (ProductionUnit) obj[0];
			ProductionUnit parent = (ProductionUnit) obj[1];

			vo.setId(son.getId());
			vo.setName(son.getName());
			vo.setParent(parent);
			vo.setCode(son.getCode());
			vo.setDisabled(son.getDisabled());
			vo.setNote(son.getNote());
			vo.setGoalOutput(son.getGoalOutput());
			vo.setGoalLostTime(son.getGoalLostTime());
			vo.setGoalNg(son.getGoalNg());
			vo.setGoalOee(son.getGoalOee());
			vo.setThreshold(son.getThreshold());
			productionUnitVOs.add(vo);
		}
	}

	/**
	 * 添加生产单元
	 * @return
	 */
	@RequestMapping("/addProductionUnit.do")
	@ResponseBody
	public ModelMap addProductionUnit(ProductionUnit productionUnit) {
		ModelMap modelMap = new ModelMap();
		ProductionUnit productionUnit4Code = productionUnitService.queryByProperty("code", productionUnit.getCode());
		if(productionUnit4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "生产单元编码已被使用");
		}else {
			ProductionUnit	productionUnit4Name = productionUnitService.queryByProperty("name",productionUnit.getName());
			if(productionUnit4Name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "生产单元名称已被使用");
			}else {
				ClassType classType = classesTypeService.queryByProperty("name",productionUnit.getClassTypeName());
				productionUnit.setClassType(classType);
				if(productionUnit.getThreshold()==null){
					productionUnit.setThreshold(0f);
				}
				productionUnitService.addObj(productionUnit);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	/**
	 * 根据id查询生产单元
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryProductionUnitById.do")
	@ResponseBody
	public ProductionUnit queryProductionUnitById(Long id) {
		ProductionUnit productionUnit = productionUnitService.queryObjById(id);
		return productionUnit;
	}

	/**
	 * 查询所有生产单元
	 * @return
	 */
	@RequestMapping("/queryAllProductionUnits.do")
	@ResponseBody
	public List<ProductionUnit> queryAllProductionUnits(){
		return productionUnitService.queryAllProductionUnits();
	}
	/**
	 * 维修人员查询所有可添加的生产单元
	 * @return
	 */
	@RequestMapping("/queryAllProductionUnitsByMaintenanceStaffId.do")
	@ResponseBody
	public List<ProductionUnit> queryAllProductionUnitsByMaintenanceStaffId(Long id){
		MaintenanceStaff m = maintenanceStaffService.queryObjById(id);
		return productionUnitService.queryAllProductionUnitsByMaintenanceStaffId(m.getCode());
	}
	/**
	 * 查询所有叶子节点生产单元
	 * @return
	 */
	@RequestMapping("/queryAllLeafProductionUnits.do")
	@ResponseBody
	public List<ProductionUnit> queryAllLeafProductionUnits(){
		return productionUnitService.queryAllLeafProductionUnits();
	}
	/**
	 * 更新部门
	 * @return
	 */
	@RequestMapping("/updateProductionUnit.do")
	@ResponseBody
	public ModelMap updateProductionUnit(ProductionUnit productionUnit) {
		ModelMap modelMap = new ModelMap();
		ProductionUnit pu = productionUnitService.queryByProperty("name", productionUnit.getName());
		if(pu!=null && !productionUnit.getId().equals(pu.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "生产单元名称已被使用");
		}else {
			ClassType classType = classesTypeService.queryByProperty("name",productionUnit.getClassTypeName());
			productionUnit.setClassType(classType);
			productionUnitService.updateObj(productionUnit);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除部门，如果该部门存在子部门，则不允许删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteProductionUnit.do")
	@ResponseBody
	public ModelMap deleteProductionUnit(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}

		Long productionUnitId = Long.valueOf(id);

		ModelMap modelMap = new ModelMap();
		try {
			Long subPUCount = productionUnitService.queryCountOfSubProductionUnit(productionUnitId);
			if(subPUCount>0) {
				throw new RuntimeException("存在子生产单元，不允许删除!");
			}
			if(productionUnitService.isExistDevices(productionUnitId)) {
				throw new RuntimeException("该生产单元下存在设备，不允许删除!");
			}
			productionUnitService.deleteObj(productionUnitId);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		} catch (RuntimeException e) {
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("title", "操作提示");
		}
		return modelMap;
	}
	/**
	 * 停用该部门
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledProductionUnit.do")
	@ResponseBody
	public ModelMap disabledProductionUnit(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		ProductionUnit d = productionUnitService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		productionUnitService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 生产单元下是否存在设备:不允许 添加子生产单元
	 * @param id
	 * @return
	 */
	@RequestMapping("/existDevices.do")
	@ResponseBody
	public ModelMap existDevices(Long id,String module) {
		ModelMap modelMap = new ModelMap();
		List<Device> devices = deviceService.queryDevicesByProductionUnitId(id,module);
		if(devices!=null && devices.size()>0) {
			modelMap.addAttribute("statusCode",300);
			modelMap.addAttribute("message", "该生产单元下存在设备，不允许添加子生产单元!");
			modelMap.addAttribute("title", "操作提示!");
		}else {
			modelMap.addAttribute("statusCode",200);
		}

		return modelMap;
	}
	/**
	 * 生产单元下是否存在子生产单元：不允许添加设备
	 * @param id
	 * @return
	 */
	@RequestMapping("/existSubProductionUnit.do")
	@ResponseBody
	public ModelMap existSubProductionUnit(Long id) {
		ModelMap modelMap = new ModelMap();
		if(productionUnitService.queryExistSubProductionUnit(id)) {
			modelMap.addAttribute("statusCode",300);
			modelMap.addAttribute("message", "该生产单元下存在子生产单元，不允许添加设备!");
			modelMap.addAttribute("title", "操作提示!");
		}else {
			modelMap.addAttribute("statusCode",200);
		}

		return modelMap;
	}

} 
