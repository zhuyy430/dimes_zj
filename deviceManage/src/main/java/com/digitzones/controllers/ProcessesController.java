package com.digitzones.controllers;
import com.digitzones.model.*;
import com.digitzones.procurement.dto.ProcessesDto;
import com.digitzones.procurement.model.InventoryProcessMapping;
import com.digitzones.procurement.service.IInventoryProcessMappingService;
import com.digitzones.service.*;
import com.digitzones.vo.Node;
import com.digitzones.vo.ProcessesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * 工序管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/processes")
public class ProcessesController {
	private IProcessesService processesService;
	private IProcessTypeService processTypeService;
	private IProcessDeviceSiteMappingService processDeviceSiteMappingService;
	private IProcessesParametersMappingService processesParametersMappingService;
	private IEmployeeProcessMappingService employeeProcessMappingService;
	@Autowired
	private ICraftsRouteProcessMappingService craftsRouteProcessMappingService;
	
	@Autowired
	private IInventoryProcessMappingService inventoryProcessMappingService;
	@Autowired
	public void setEmployeeProcessMappingService(IEmployeeProcessMappingService employeeProcessMappingService) {
		this.employeeProcessMappingService = employeeProcessMappingService;
	}
	@Autowired
	public void setProcessesParametersMappingService(IProcessesParametersMappingService processesParametersMappingService) {
		this.processesParametersMappingService = processesParametersMappingService;
	}
	@Autowired
	public void setProcessDeviceSiteMappingService(IProcessDeviceSiteMappingService processDeviceSiteMappingService) {
		this.processDeviceSiteMappingService = processDeviceSiteMappingService;
	}
	@Autowired
	public void setProcessTypeService(IProcessTypeService processTypeService) {
		this.processTypeService = processTypeService;
	}
	@Autowired
	public void setProcessesService(IProcessesService processesService) {
		this.processesService = processesService;
	}
	/**
	 * 更新工序
	 * @param processes
	 * @return
	 */
	@RequestMapping("/updateProcesses.do")
	@ResponseBody
	public ModelMap updateProcesses(Processes processes) {
		ModelMap modelMap = new ModelMap();
		ProcessType processType = processes.getProcessType();
		if(processType!=null) {
			ProcessType pt = processTypeService.queryByProperty("name", processes.getName());
			if(pt==null) {
				Long id = (Long) processTypeService.addObj(processType);
				pt = processTypeService.queryObjById(id);
				pt.setId(id);
			}
			processes.setProcessType(pt);
		}
		processesService.updateObj(processes);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 根据id删除工序，如果该工序存在子工序，则不允许删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteProcesses.do")
	@ResponseBody
	public ModelMap deleteProcesses(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		List<InventoryProcessMapping> ipmlist= inventoryProcessMappingService.queryByProcessId(Long.valueOf(id));
		if(ipmlist.size()>0){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工序与物料关联，不允许删除!");
			return modelMap;
		}
		List<CraftsRouteProcessMapping> cpmlist=craftsRouteProcessMappingService.queryByProcessId(Long.valueOf(id));
		if(cpmlist.size()>0){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300 );
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工序与工艺路线关联，不允许删除!");
			return modelMap;
		}
		processesService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("msg", "该工序下存在子工序，不允许删除!");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该工序
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledProcesses.do")
	@ResponseBody
	public ModelMap disabledProcesses(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Processes d = processesService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		processesService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 根据id查询工序
	 * @return
	 */
	@RequestMapping("/queryProcessesById.do")
	@ResponseBody
	public ProcessesVO queryProcessesById(Long id) {
		Processes processes = processesService.queryObjById(id);
		return model2Vo(processes);
	}

	/**
	 * 查找所有工序分页
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProcessess.do")
	@ResponseBody
	public ModelMap queryProcessess(Integer rows,Integer page){
		String hql = "from Processes p";
		Pager<Processes> pager = processesService.queryObjs(hql, page, rows, new Object[] {});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return  modelMap;
	}
	/**
	 * 查找同一个类型下工序
	 * @return
	 */
	@RequestMapping("/queryProcessesHaveOneType.do")
	@ResponseBody
	public List<Processes> queryProcessesHaveOneType(String code){
		List<Processes> pList = new ArrayList<>();
		if(null!=code&&!"".equals(code)){
			Processes p = processesService.queryByProperty("code", code);
			pList= processesService.queryProcessesByTypeId(p.getProcessType().getId());
		}
		return  pList;
	}


	/**
	 * 根据工件id查询非当前工件下的工序信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryOtherProcesses.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryOtherProcesses(String InventoryCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "select ds from Processes ds ";
/*		String hql = "select ds from Processes ds where ds.id not in ("
				+ "select pdm.process.id from InventoryProcessMapping pdm where pdm.inventory.cInvCode=?0)";
*/		Pager<Processes> pager = processesService.queryObjs(hql, page, rows, new Object[] {});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 查询非当前工艺路线下的工序信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryCraftsOtherProcesses.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryCraftsOtherProcesses(String craftsId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
//		String hql = "select ds from Processes ds ";
		String hql = "select ds from Processes ds where ds.id not in ("
				+ "select pdm.process.id from CraftsRouteProcessMapping pdm where pdm.craftsRoute.id=?0)";
		Pager<Processes> pager = processesService.queryObjs(hql, page, rows, new Object[] {craftsId});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据工件id查找工序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProcessessByInventoryCode.do")
	@ResponseBody
	public ModelMap queryProcessessByInventoryCode(String InventoryCode,Integer rows,Integer page){
		String hql = "select wpm from InventoryProcessMapping wpm inner join wpm.process p where wpm.inventory.id=?0  order by wpm.processRoute asc";
		Pager<InventoryProcessMapping> pager = inventoryProcessMappingService.queryObjs(hql, page, rows, new Object[] {InventoryCode});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return  modelMap;
	}

	/**
	 * 根据工件id查找工序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProcessessByCraftsId.do")
	@ResponseBody
	public ModelMap queryProcessessByWorkpieceId(String craftsId,Integer rows,Integer page){
		String hql1 = "select p from CraftsRouteProcessMapping wpm inner join wpm.process p where wpm.craftsRoute.id=?0  order by wpm.processRoute asc";
		Pager<Processes> pager1 = processesService.queryObjs(hql1, page, rows, new Object[] {craftsId});
		String hql2 = "select wpm.id from CraftsRouteProcessMapping wpm inner join wpm.process p where wpm.craftsRoute.id=?0  order by wpm.processRoute asc";
		Pager<Long> pager2 = processesService.queryObjs(hql2, page, rows, new Object[] {craftsId});
		ModelMap modelMap = new ModelMap();
		List<Processes> data1 = pager1.getData();
		List<Long> data2 = pager2.getData();
		modelMap.addAttribute("total", pager1.getTotalCount());
		modelMap.addAttribute("rows", model2Dto(data1,data2));
		return  modelMap;
	}

	private ProcessesVO model2Vo(Processes p) {
		ProcessesVO vo = new ProcessesVO();
		if(p == null) {
			return null;
		}

		vo.setId(p.getId());
		vo.setName(p.getName());
		vo.setCode(p.getCode());
		vo.setProcessType(p.getProcessType());
		if(p.getProcessType()!=null) {
			vo.setProcessTypeId(p.getProcessType().getId());
			vo.setProcessTypeName(p.getProcessType().getName());
		}
		vo.setNote(p.getNote());
		return vo;
	}
	
	private List<ProcessesDto> model2Dto(List<Processes> listProcess,List<Long> listId) {
		List<ProcessesDto> list = new ArrayList<>();
		for(int i=0;i<listProcess.size();i++){
			ProcessesDto vo = new ProcessesDto();
			Processes p = listProcess.get(i);
			
			vo.setPid(listId.get(i));
			vo.setId(p.getId());
			vo.setName(p.getName());
			vo.setCode(p.getCode());
			vo.setNote(p.getNote());
			list.add(vo);
		}
		return list;
	}
	/**
	 * 添加工序
	 * @param processes
	 * @return
	 */
	@RequestMapping("/addProcesses.do")
	@ResponseBody
	public ModelMap addProcesses(Processes processes) {
		ProcessType processType = processes.getProcessType();
		List<ProcessType> list = processTypeService.queryTopProcessTypes();
		ProcessType rootType = null;
		if(!CollectionUtils.isEmpty(list)) {
			rootType = list.get(0);
		}
		if(processType!=null && !"".equals(processType.getName().trim())) {
			ProcessType pt = processTypeService.queryByProperty("name", processType.getName());
			if(pt==null) {
				if(rootType!=null) {
					processType.setParent(rootType);
				}
				processType.setCode(processType.getName());
				Long id = (Long) processTypeService.addObj(processType);
				pt = processTypeService.queryObjById(id);
			}
			processes.setProcessType(pt);
		}else {
			if(rootType!=null) {
				processes.setProcessType(rootType);
			}else {
				processes.setProcessType(null);
			}
		}
		ModelMap modelMap = new ModelMap();
		//检测工序编码是否重复
		Processes dept4Code = processesService.queryByProperty("code", processes.getCode());
		if(dept4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "工序编码已被使用");
		}else {
			processesService.addObj(processes);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 对外提供的接口：用于接收其他应用录入的数据
	 * @param processData
	 * @return
	 */
	@RequestMapping("/saveProcesses.do")
	public ModelMap saveProcesses(String processData) {
		ModelMap modelMap = new ModelMap();
		return modelMap;
	}

	/**
	 * 为工序添加设备站点
	 * @return
	 */
	@RequestMapping("/addDeviceSite4Processes.do")
	@ResponseBody
	public ModelMap addDeviceSite4Processes(Long processesId,String deviceSiteIds) {
		ModelMap modelMap = new ModelMap();
		if(deviceSiteIds!=null) {
			if(deviceSiteIds.contains("[")) {
				deviceSiteIds = deviceSiteIds.replace("[", "");
			}
			if(deviceSiteIds.contains("]")) {
				deviceSiteIds = deviceSiteIds.replace("]", "");
			}

			String[] idArray = deviceSiteIds.split(",");
			for(int i = 0;i<idArray.length;i++) {
				DeviceSite device = new DeviceSite();
				device.setId(Long.valueOf(idArray[i]));
				Processes c = new Processes();
				c.setId(processesId);

				ProcessDeviceSiteMapping pdsm = new ProcessDeviceSiteMapping();
				pdsm.setDeviceSite(device);
				pdsm.setProcesses(c);

				processDeviceSiteMappingService.addObj(pdsm);
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 删除工序和设备站点的关联
	 * @return
	 */
	@RequestMapping("/deleteDeviceSiteFromProcesses.do")
	@ResponseBody
	public ModelMap deleteDeviceSiteFromProcesses(Long processesId,String deviceSiteId) {
		if(deviceSiteId.contains("'")) {
			deviceSiteId = deviceSiteId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		processDeviceSiteMappingService.deleteByProcessesIdAndDeviceSiteId(processesId, Long.valueOf(deviceSiteId));
		modelMap.addAttribute("message", "删除成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	/**
	 * 为工序添加参数
	 * @param processesId
	 * @return
	 */
	@RequestMapping("/addParameters4Processes.do")
	@ResponseBody
	public ModelMap addParameters4Processes(Long processesId,String parameterIds) {
		ModelMap modelMap = new ModelMap();
		if(parameterIds!=null) {
			if(parameterIds.contains("[")) {
				parameterIds = parameterIds.replace("[", "");
			}
			if(parameterIds.contains("]")) {
				parameterIds = parameterIds.replace("]", "");
			}

			String[] idArray = parameterIds.split(",");
			for(int i = 0;i<idArray.length;i++) {
				Parameters device = new Parameters();
				device.setId(Long.valueOf(idArray[i]));
				Processes c = new Processes();
				c.setId(processesId);

				ProcessesParametersMapping pdsm = new ProcessesParametersMapping();
				pdsm.setParameters(device);
				pdsm.setProcesses(c);

				processesParametersMappingService.addObj(pdsm);
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 删除工序和参数的关联
	 * @param processesId
	 * @param parametersId
	 * @return
	 */
	@RequestMapping("/deleteParameterFromProcesses.do")
	@ResponseBody
	public ModelMap deleteParameterFromProcesses(Long processesId,String parametersId) {
		if(parametersId.contains("'")) {
			parametersId = parametersId.replace("'", "");
		}
		String[] parametersIds = parametersId.split(",");
		for(String id:parametersIds){
			processesParametersMappingService.deleteByProcessesIdAndParameterId(processesId, Long.valueOf(id));
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("message", "删除成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	/**
	 * 根据工件id和设备站点id查找工序
	 * @param workpieceId
	 * @param deviceSiteId
	 * @return
	 */
	@RequestMapping("/queryProcessByWorkpieceIdAndDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryProcessByWorkpieceIdAndDeviceSiteId(String workpieceId,Long deviceSiteId,Integer rows,Integer page){
		String hql = "select p from InventoryProcessMapping wpm inner join wpm.process p where wpm.inventory.id=?0  order by wpm.processRoute asc";
		@SuppressWarnings("unchecked")
		Pager<Processes> pager = processesService.queryObjs(hql, page, rows, new Object[] {workpieceId});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 根据员工id查找员工技能
	 * @param employeeId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryProcessByEmployeeId.do")
	@ResponseBody
	public ModelMap queryProcessByEmployeeId(Long employeeId,Integer rows,Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from EmployeeProcessMapping esm where esm.employee.id=?0";
		@SuppressWarnings("unchecked")
		Pager<EmployeeProcessMapping> objPager = employeeProcessMappingService.queryObjs(hql, page, rows, new Object[] {employeeId});
		modelMap.addAttribute("rows", objPager.getData());
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查找工序树，结构为工序类型-->工序
	 * @return
	 */
	@RequestMapping("/queryProcessTree.do")
	@ResponseBody
	public List<Node> queryProcessTree() {
		//查找生产单元树形结构
		List<ProcessType> pus = processTypeService.queryTopProcessTypes();
		List<Node> nodes = new ArrayList<>();
		objList2NodeList(nodes, pus);
		return nodes;
	}
	/**
	 * 构建树形结构，结构为：工序类型-->工序
	 * @param nodes
	 * @param pus
	 */
	private void objList2NodeList(List<Node> nodes,Collection<ProcessType> pus) {
		for(ProcessType pu : pus) {
			Node node = new Node();
			node.setId(pu.getId());
			node.setCode(pu.getCode());
			node.setName(pu.getName());
			nodes.add(node);
			List<Node> nodeList = new ArrayList<Node>();
			node.setChildren(nodeList);
			if(pu.getChildren()!=null &&pu.getChildren().size()>0) {
				objList2NodeList(nodeList, pu.getChildren());
			}
			//根据工序类别id查询工序
			List<Processes> processes = processesService.queryProcessesByTypeId(pu.getId());
			for(Processes d : processes) {
				Node no = new Node();
				no.setId(d.getId());
				no.setName(d.getName());
				no.setCode(d.getCode());
				nodeList.add(no);
			}
		}
	}
	/**
	 * 查找所有工序类别
	 * @return
	 */
	@RequestMapping("/queryAllProcessTypes.do")
	@ResponseBody
	public List<ProcessType> queryAllProcessTypes(){
		return processTypeService.queryAllProcessTypes();
	}
	/**
	 * 查询所有工序
	 * @return
	 */
	@RequestMapping("/queryAllProcesses.do")
	@ResponseBody
	public List<Processes> queryAllProcesses(){
		return processesService.queryAllProcesses();
	}
}
