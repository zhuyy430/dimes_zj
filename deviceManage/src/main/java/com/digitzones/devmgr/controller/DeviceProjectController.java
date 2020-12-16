package com.digitzones.devmgr.controller;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.NGMaintainRecord;
import com.digitzones.devmgr.service.IDeviceProjectService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.INGMaintainRecordService;
import com.digitzones.devmgr.vo.DeviceProjectVO;
import com.digitzones.model.Pager;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProjectTypeService;
import com.digitzones.util.BreakdownExcelUtil;
import com.digitzones.util.DeviceProjectExcelUtil;
import com.digitzones.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/deviceProject")
public class DeviceProjectController {
	@Autowired
	private IDeviceProjectService deviceProjectService;
	@Autowired
	private IProjectTypeService projectTypeService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private INGMaintainRecordService ngMaintainRecordService;
	/**
	 * 新增设备项目信息
	 * @return
	 */
	@RequestMapping("/addDeviceProject.do")
	@ResponseBody
	public ModelMap addDeviceProject(DeviceProject deviceProject){
		ModelMap modelMap = new ModelMap();
		DeviceProject dp = deviceProjectService.queryDeviceProjectByCodeAndType(deviceProject.getCode(),deviceProject.getType());
		if(dp==null){
			if(deviceProject.getProjectType().getId()==null) {
				deviceProject.setProjectType(null);
			}
			deviceProject.setCreateTime(new Date());
			deviceProjectService.addObj(deviceProject);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功！");
			return modelMap;
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "项目编码已存在！");
		}
		return modelMap;
	}
	/**
	 * 新增设备项目信息(故障原因)
	 * @return
	 */
	@RequestMapping("/addDeviceProjectWithBreakDown.do")
	@ResponseBody
	public ModelMap addDeviceProjectWithBreakDown(DeviceProject deviceProject){
		ModelMap modelMap = new ModelMap();
		if(deviceProject!=null&&deviceProject.getProjectType()!=null){
			deviceProject.setProjectType(projectTypeService.queryObjById(deviceProject.getProjectType().getId()));
			deviceProject.setCreateTime(new Date());
		}
		deviceProjectService.addObj(deviceProject);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功！");
		return modelMap;
	}
	/**
	 * 编辑设备项目信息
	 * @return
	 */
	@RequestMapping("/updateDeviceProject.do")
	@ResponseBody
	public ModelMap updateDeviceProject(DeviceProject deviceProject){
		ModelMap modelMap = new ModelMap();
		DeviceProject dp = deviceProjectService.queryObjById(deviceProject.getId());
		dp.setCode(deviceProject.getCode());
		dp.setName(deviceProject.getName());
		dp.setProjectType(deviceProject.getProjectType());
		dp.setFrequency(deviceProject.getFrequency());
		dp.setMethod(deviceProject.getMethod());
		dp.setNote(deviceProject.getNote());
		dp.setStandard(deviceProject.getStandard());
		dp.setRemark(deviceProject.getRemark());
		deviceProjectService.updateObj(dp);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功！");
		return modelMap;
	}
	/**
	 * 编辑设备项目信息(故障原因)
	 * @param deviceProject
	 * @return
	 */
	@RequestMapping("/updateDeviceProjectWithBreakDown.do")
	@ResponseBody
	public ModelMap updateDeviceProjectWithBreakDown(DeviceProject deviceProject){
		ModelMap modelMap = new ModelMap();
		DeviceProject dp = deviceProjectService.queryObjById(deviceProject.getId());
		ProjectType pt=projectTypeService.queryObjById(deviceProject.getProjectType().getId());
		dp.setCode(deviceProject.getCode());
		dp.setName(deviceProject.getName());
		dp.setProjectType(pt);
		dp.setMethod(deviceProject.getMethod());
		dp.setNote(deviceProject.getNote());
		dp.setRemark(deviceProject.getRemark());
		deviceProjectService.updateObj(dp);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功！");
		return modelMap;
	}
	/**
	 * 删除设备项目信息
	 * @return
	 */
	@RequestMapping("/deleteDeviceProject.do")
	@ResponseBody
	public ModelMap deleteDeviceProject(String id){
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		List<DeviceRepair> drList=deviceRepairOrderService.queryDeviceRepairNumByDeviceProjectId(Long.parseLong(id));
		List<NGMaintainRecord> ngList=ngMaintainRecordService.queryNGMaintainRecordByDeviceProjectId(Long.parseLong(id));
		if(drList!=null&&drList.size()>0) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 110);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "删除失败,该项已有被使用记录!");
			modelMap.addAttribute("message", "删除失败,该项已有被使用记录!!");
			return modelMap;
		}
		if(ngList!=null&&ngList.size()>0) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 110);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "删除失败,该项已有被使用记录!!");
			modelMap.addAttribute("message", "删除失败,该项已有被使用记录!!");
			return modelMap;
		}

		deviceProjectService.deleteObj(Long.parseLong(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("msg", "成功删除!");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 根据ID查询设备项目信息
	 * @return
	 */
	@RequestMapping("/queryDeviceProjectById.do")
	@ResponseBody
	public DeviceProjectVO queryDeviceProjectById(String id){
		DeviceProject deviceProject = deviceProjectService.queryObjById(Long.parseLong(id));
		return model2vo(deviceProject);
	}
	/**
	 * 根据ID查询设备项目信息
	 * @return
	 */
	@RequestMapping("/queryDeviceProject.do")
	@ResponseBody
	public DeviceProject queryDeviceProject(String id){
		return deviceProjectService.queryObjById(Long.parseLong(id));
	}
	/**
	 * 设备项目信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceProjectByType.do")
	@ResponseBody
	public ModelMap queryDeviceProjectByType(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String type,String condition,Long deviceId,String maintainType,String classesType){
		String hql ="from DeviceProject dp where dp.type=?0"
				+ " and dp.code not in( "
				+" select dpr.code from DeviceProjectRecord dpr where dpr.classesCode='' and dpr.device.id="+deviceId+" and dpr.type='"+type+"')";
		ModelMap modelMap = new ModelMap();
		if(deviceId!=null){
			switch(type) {
				//保养项目
				case Constant.DeviceProject.MAINTAIN:{
					if(StringUtils.isEmpty(maintainType)) {
						//hql = "from DeviceProject dp where dp.type=?0  and (dp.projectType.id=(select d.projectType.id from Device d where d.id = "+deviceId+" ) or dp.projectType.id is null)";
						hql = "from DeviceProject dp where dp.type=?0  ";
					}else{
						hql = "from DeviceProject dp where dp.type=?0  "
								+ "and dp.code not in( "
								+" select dpr.code from DeviceProjectRecord dpr where dpr.recordTypeCode='"+maintainType+"' and dpr.device.id="+deviceId+" and dpr.type='"+type+"')";
				}
					break;
				}
				case Constant.DeviceProject.SPOTINSPECTION:{
					if(!StringUtils.isEmpty(classesType)) {
						hql = "from DeviceProject dp where dp.type=?0  "
								+ "and dp.code not in( "
								+" select dpr.code from DeviceProjectRecord dpr where dpr.classesCode='"+classesType+"' and dpr.device.id="+deviceId+" and dpr.type='"+type+"')";
					}
					break;
				}
			}
		}
		List<Object> args = new ArrayList<>();
		args.add(type);
		if(!StringUtils.isEmpty(condition)) {
			hql += " and (dp.name like ?1 or dp.code like ?1)";
			args.add("%" + condition + "%");
		}
		Pager<DeviceProject> pager = deviceProjectService.queryObjs(hql, page, rows,args.toArray());
		List<DeviceProjectVO> list = new ArrayList<>();
		for(DeviceProject d:pager.getData()){
			list.add(model2vo(d));
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * 设备项目信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceProjectByTypeAndMaintainRecordId.do")
	@ResponseBody
	public ModelMap queryDeviceProjectByTypeAndMaintainRecordId(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String type,String condition,Long recordId,String maintainType){
		String hql ="from DeviceProject dp where dp.type=?0"
				+ " and dp.code not in( "
				+" select dpr.code from MaintenanceItem dpr where  dpr.maintenancePlanRecord.id="+recordId+")";
		ModelMap modelMap = new ModelMap();
		
		if(recordId!=null){
			switch(type) {
				//保养项目
				case Constant.DeviceProject.MAINTAIN:{
					if(StringUtils.isEmpty(maintainType)) {
						//hql = "from DeviceProject dp where dp.type=?0  and (dp.projectType.id=(select d.projectType.id from Device d where d.id = "+deviceId+" ) or dp.projectType.id is null)";
						hql = "from DeviceProject dp where dp.type=?0  ";
					}else {
						hql = "from DeviceProject dp where dp.type=?0  "
								+ "and dp.code not in( "
								+" select dpr.code from MaintenanceItem dpr where  dpr.maintenancePlanRecord.id="+recordId+" and dpr.recordTypeCode='"+maintainType+"')";
				}
					break;
				}
			}
		}
		
		List<Object> args = new ArrayList<>();
		args.add(type);
		if(!StringUtils.isEmpty(condition)) {
			hql += " and (dp.name like ?1 or dp.code like ?1)";
			args.add("%" + condition + "%");
		}
		Pager<DeviceProject> pager = deviceProjectService.queryObjs(hql, page, rows,args.toArray());
		List<DeviceProjectVO> list = new ArrayList<>();
		for(DeviceProject d:pager.getData()){
			list.add(model2vo(d));
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * 设备项目信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceProjectByType2.do")
	@ResponseBody
	public ModelMap queryDeviceProjectByType2(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String type){
		String hql ="from DeviceProject dp where dp.type=?0";
		ModelMap modelMap = new ModelMap();
		Pager<DeviceProject> pager = deviceProjectService.queryObjs(hql, page, rows, new Object[] {type});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 设备项目信息
	 * @return
	 */
	@RequestMapping("/queryAllDeviceProjectByType.do")
	@ResponseBody
	public List<DeviceProject> queryAllDeviceProjectByType(String type){
		List<DeviceProject> dpList = deviceProjectService.queryAllDeviceProjectByType(type);
		return dpList;
	}
	/**
	 * 分页查询类别中的项目
	 * @param projectTypeId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDevicesProjectByProjectTypeId.do")
	@ResponseBody
	public ModelMap queryDevicesProjectByProjectTypeId(@RequestParam(value="projectTypeId",required=false)Long projectTypeId,
			String type,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request) {

		ModelMap mm = new ModelMap();
		String hql = "select dp from DeviceProject dp inner join dp.projectType p where dp.type='" + type +"'";
		if(projectTypeId!=null && projectTypeId>0) {
			hql +=" and p.id=" + projectTypeId;
		}
		Pager<DeviceProject> pager = deviceProjectService.queryObjs(hql, page, rows, new Object[] {});
		List<DeviceProjectVO> vos = new ArrayList<>();
		if(pager.getData()!=null && pager.getData().size()>0) {
			for(DeviceProject d : pager.getData()) {
				vos.add(model2vo(d));
			}
		}
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("rows", vos);
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}

	/**
	 * 分页查询类别中的项目
	 * @return
	 */
	@RequestMapping("/queryDevicesProjectByType.do")
	@ResponseBody
	public List<DeviceProject> queryDevicesProjectByType(String type,HttpServletRequest request) {
		List<DeviceProject> list = deviceProjectService.queryAllDeviceProjectByType(type);
		return list;
	}

	/**
	 * 查询类别中的项目
	 * @return
	 */
	@RequestMapping("/queryDevicesProjectByProjectTypeIdNotPage.do")
	@ResponseBody
	public List<DeviceProjectVO> queryDevicesProjectByProjectTypeIdNotPage(@RequestParam(value="projectTypeId",required=false)Long projectTypeId,
			String type,HttpServletRequest request) {
		List<DeviceProject> list = deviceProjectService.queryDevicesProjectByProjectTypeIdNotPage(type,projectTypeId);
		List<DeviceProjectVO> vos = new ArrayList<>();
		if(list!=null && list.size()>0) {
			for(DeviceProject d : list) {
				vos.add(model2vo(d));
			}
		}
		return vos;
	}
	
	/**
	 * 分页查询类别中的项目
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDevicesProjectByDeviceRepairId.do")
	@ResponseBody
	public ModelMap queryDevicesProjectByDeviceRepairId(@RequestParam(value="projectTypeId",required=false)Long projectTypeId,@RequestParam(value="deviceRepairId",required=false)Long deviceRepairId,
			String type,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request) {
		
		ModelMap mm = new ModelMap();
		String hql = "select dp from DeviceProject dp inner join dp.projectType p where dp.type='" + type +"'";
		if(projectTypeId!=null && projectTypeId>0) {
			hql +=" and p.id=" + projectTypeId;
		}
		hql+=" and dp.id not in(select n.deviceProject.id from NGMaintainRecord n where n.deviceRepair.id=?0)";
		Pager<DeviceProject> pager = deviceProjectService.queryObjs(hql, page, rows, new Object[] {deviceRepairId});
		List<DeviceProjectVO> vos = new ArrayList<>();
		if(pager.getData()!=null && pager.getData().size()>0) {
			for(DeviceProject d : pager.getData()) {
				vos.add(model2vo(d));
			}
		}
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("rows", vos);
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}

	/**
	 * 根据装备id查询非当前的设备项目信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryOtherDeviceProjectByTypeAndDeviceId.do")
	@ResponseBody
	public ModelMap queryOtherDeviceProjectByTypeAndDeviceId(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,String type,Long deviceId){
		String hql = "select dp from DeviceProject dp where dp.id not in ("
				+"select dpm.deviceproject.id from DeviceAndProjectMapping dpm where dpm.device.id=?1 and deviceproject.type=?0)";
		Pager<DeviceProject> pager = deviceProjectService.queryObjs(hql, page, rows,new Object[]{type,deviceId});
		ModelMap modelMap = new ModelMap();
		List<DeviceProjectVO> list = new ArrayList<>();
		for(DeviceProject d:pager.getData()){
			list.add(model2vo(d));
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}

	private DeviceProjectVO model2vo(DeviceProject d) {
		if(d == null) {
			return null;
		}

		DeviceProjectVO vo = new DeviceProjectVO();
		vo.setId(d.getId());
		vo.setName(d.getName());
		vo.setNote(d.getNote());
		vo.setCode(d.getCode());
		vo.setStandard(d.getStandard());
		vo.setMethod(d.getMethod());
		vo.setFrequency(d.getFrequency());
		vo.setRemark(d.getRemark());
		if(d.getProjectType()!=null) {
			ProjectType dt = (ProjectType)d.getProjectType();
			vo.setDeviceTypeId(dt.getId());
			vo.setDeviceTypeCode(dt.getCode());
			vo.setDeviceTypeName(dt.getName());
		}
		return vo;
	}
	/**
	 * 根据项目类型id查询项目信息
	 */
	@RequestMapping("/queryAllDeviceProjectByProjectTypeId.do")
	@ResponseBody
	public List<DeviceProject> queryAllDeviceProjectByProjectTypeId(Long projectTypeId){
		List<DeviceProject> dpList = deviceProjectService.queryAllDeviceProjectByProjectTypeId(projectTypeId);
		return dpList;
	}
	/**
	 * 设备导入
	 * @param file
	 * @return
	 */
	@RequestMapping("/uploadTemplate.do")
	@ResponseBody
	public ModelMap uploadTemplate(MultipartFile file,String type) {
		ModelMap modelMap = new ModelMap();
		ExcelUtil deviceProjectExcelUtil = new DeviceProjectExcelUtil(projectTypeService,type);
		try {
			@SuppressWarnings("unchecked")
			List<DeviceProject> list = (List<DeviceProject>) deviceProjectExcelUtil.getData(file);
			deviceProjectService.imports(list);
			modelMap.addAttribute("success", true);
		}catch(RuntimeException re) {
			re.printStackTrace();
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", re.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	/**
	 * 导入故障原因
	 * @param file
	 * @return
	 */
	@RequestMapping("/uploadDeviceBreakDown.do")
	@ResponseBody
	public ModelMap uploadDeviceBreakDown(MultipartFile file) {
	    ModelMap modelMap = new ModelMap();
	    ExcelUtil breakdownExcelUtil = new BreakdownExcelUtil(projectTypeService,Constant.DeviceProject.BREAKDOWNREASON);
	    try {
	        @SuppressWarnings("unchecked")
	        List<DeviceProject> list = (List<DeviceProject>) breakdownExcelUtil.getData(file);
	        deviceProjectService.imports(list);
	        modelMap.addAttribute("success", true);
	    }catch(RuntimeException re) {
	        re.printStackTrace();
	        modelMap.addAttribute("success", false);
	        modelMap.addAttribute("msg", re.getMessage());
	        return modelMap;
	    }
	    return modelMap;
	}
}
