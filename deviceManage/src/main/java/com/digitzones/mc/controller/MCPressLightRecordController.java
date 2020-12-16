package com.digitzones.mc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.constants.Constant;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCPressLightRecodeService;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightRecord;
import com.digitzones.model.PressLightType;
import com.digitzones.model.Role;
import com.digitzones.model.User;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IPressLightRecordService;
import com.digitzones.service.IPressLightService;
import com.digitzones.service.IPressLightTypeService;
import com.digitzones.service.IRoleService;
import com.digitzones.service.IUserService;
import com.digitzones.vo.PressLightRecordVO;
@Controller
@RequestMapping("/mcPressLightRecord")
public class MCPressLightRecordController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IPressLightRecordService pressLightRecordService;
	private IDeviceSiteService deviceSiteService;
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private IPressLightService pressLightService;
	@Autowired
	private IPressLightTypeService pressLightTypeService;
	@Autowired
	private IMCPressLightRecodeService pressLightRecodeService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	public void setWorkFlowKeyConfig(WorkFlowKeyConfig workFlowKeyConfig) {
		this.workFlowKeyConfig = workFlowKeyConfig;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}
	/*@Autowired
	public void setPressLightRecordService(@Qualifier("pressLightRecordServiceProxy")IPressLightRecordService pressLightRecordService) {
		this.pressLightRecordService = pressLightRecordService;
	} */
	/**
	 * 根据设备站点id查找按灯记录
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPressLightRecordByDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryPressLightRecordByDeviceSiteId(Long deviceSiteId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from PressLightRecord plr where plr.deviceSite.id=?0 and plr.deleted=?1";
		Pager<PressLightRecord> pager = pressLightRecordService.queryObjs(hql, page, rows, new Object[] {deviceSiteId,false});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 添加按灯记录
	 * @param pressLightRecord
	 * @return
	 */
	@RequestMapping("/addPressLightRecord.do")
	@ResponseBody
	public ModelMap addPressLightRecord(PressLightRecord pressLightRecord,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryByProperty("code", pressLightRecord.getDeviceSite().getCode());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}else {
			pressLightRecord.setDeviceSite(ds);
		}

		if(pressLightRecord.getLightOutUserId()!=null) {
			Employee e = employeeService.queryObjById(pressLightRecord.getLightOutUserId());
			pressLightRecord.setLightOutUserName(e.getName());
		}

		MCUser mcUser  = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		if(mcUser == null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请登录!");
			return modelMap;
		}
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		User user = userService.queryUserByUsername(mcUser.getUsername());
		user.setUsername(employee.getName());
		modelMap.put("businessKey", workFlowKeyConfig.getPressLightWorkflowKey());
		pressLightRecord.setPressLightTime(new Date());
		pressLightRecord.setRecovered(false);;

		if(pressLightRecord.getPressLightCode()!=null &&!"".equals(pressLightRecord.getPressLightCode().trim())) {
			PressLight pressLight = pressLightService.queryByProperty("code", pressLightRecord.getPressLightCode());
			pressLightRecord.setReason(pressLight.getReason());
			pressLightRecord.setPressLightTypeCode(pressLight.getPressLightType().getCode());
			pressLightRecord.setPressLightTypeId(pressLight.getPressLightType().getId());
			pressLightRecord.setPressLightTypeName(pressLight.getPressLightType().getName());
		}
		pressLightRecordService.addPressLightRecord(pressLightRecord, user, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询按灯记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryPressLightRecordById.do")
	@ResponseBody
	public PressLightRecordVO queryPressLightRecordById(Long id) {
		PressLightRecord plr = pressLightRecordService.queryObjById(id);
		PressLightType plType = pressLightTypeService.queryObjById(plr.getPressLightTypeId());
		if(plType.getLevel()!=1){
			PressLightType parentType = pressLightTypeService.queryByProperty("code", plType.getBasicCode());
			plr.setPressLightTypeCode(parentType.getCode());
			plr.setPressLightTypeId(parentType.getId());
			plr.setPressLightTypeName(parentType.getName());
		}
		return model2vo(plr);
	}

	private PressLightRecordVO model2vo(PressLightRecord plr) {
		if(plr==null) {
			return null;
		}
		PressLightRecordVO vo = new PressLightRecordVO();
		vo.setId(plr.getId());
		if(plr.getConfirmTime()!=null)
			vo.setConfirmTime(sdf.format(plr.getConfirmTime()));
		vo.setConfirmUserId(plr.getConfirmUserId());
		vo.setConfirmUserName(plr.getConfirmUserName());
		vo.setDeleted((plr.getDeleted()!=null&&plr.getDeleted())?"是":"否");
		vo.setDeviceSite(plr.getDeviceSite());
		vo.setHalt((plr.getHalt()!=null&&plr.getHalt())?"是":"否");
		if(plr.getLightOutTime()!=null) {
			vo.setLightOutTime(sdf.format(plr.getLightOutTime()));
		}
		vo.setLightOutUserId(plr.getLightOutUserId());
		vo.setLightOutUserName(plr.getLightOutUserName());
		if(plr.getPressLightTime()!=null) {
			vo.setPressLightTime(plr.getPressLightTime());
		}
		vo.setPressLightCode(plr.getPressLightCode());
		vo.setPressLightTypeCode(plr.getPressLightTypeCode());
		vo.setPressLightTypeId(plr.getPressLightTypeId());
		vo.setPressLightTypeName(plr.getPressLightTypeName());
		vo.setPressLightUserName(plr.getPressLightUserName());
		vo.setPressLightUserId(plr.getPressLightUserId());
		vo.setReason(plr.getReason());
		vo.setRecoverMethod(plr.getRecoverMethod());
		vo.setRecovered((plr.getRecovered()!=null&&plr.getRecovered())?"Y":"N");
		vo.setRecoverUserId(plr.getRecoverUserId());
		if(plr.getRecoverTime()!=null) {
			vo.setRecoverTime(sdf.format(plr.getRecoverTime()));
		}
		vo.setRecoverUserName(plr.getRecoverUserName());
		return vo;
	}

	/**
	 * 编辑按灯记录
	 * @param pressLightRecord
	 * @return
	 */
	@RequestMapping("/updatePressLightRecord.do")
	@ResponseBody
	public ModelMap updatePressLightRecord(PressLightRecord pressLightRecord) {
		ModelMap modelMap = new ModelMap();

		PressLightRecord plr = pressLightRecordService.queryObjById(pressLightRecord.getId());
		plr.setHalt(pressLightRecord.getHalt());

		if(pressLightRecord.getPressLightCode()!=null &&!"".equals(pressLightRecord.getPressLightCode().trim())) {
			PressLight pressLight = pressLightService.queryByProperty("code", pressLightRecord.getPressLightCode());
			pressLightRecord.setReason(pressLight.getReason());
			if(pressLight.getPressLightType()!=null) {
				pressLightRecord.setPressLightTypeCode(pressLight.getPressLightType().getCode());
				pressLightRecord.setPressLightTypeId(pressLight.getPressLightType().getId());
				pressLightRecord.setPressLightTypeName(pressLight.getPressLightType().getName());
			}
		}
		plr.setPressLightCode(pressLightRecord.getPressLightCode());
		plr.setRecoverMethod(pressLightRecord.getRecoverMethod());
		if(pressLightRecord.getLightOutUserId()!=null) {
			Employee e = employeeService.queryObjById(pressLightRecord.getLightOutUserId());
			pressLightRecord.setLightOutUserName(e.getName());
		}

		pressLightRecordService.updateObj(plr);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 恢复
	 * @param pressLightRecord
	 * @return
	 */
	@RequestMapping("/recoverPressLightRecord.do")
	@ResponseBody
	public ModelMap recoverPressLightRecord(Long id,String suggestion,String userCode,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();

		/*MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		User user = userService.queryByProperty("username", mcUser.getUsername());*/
		User user = userService.queryUserByEmployeeCode(userCode);
		Employee employee = employeeService.queryByProperty("code", userCode);
		user.setUsername(employee.getName());

		PressLightRecord plr = pressLightRecordService.queryObjById(id);
		
		if(plr.getRecovered()) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该记录已恢复!");
			return modelMap;
		}
		
		plr.setRecovered(true);
		plr.setRecoverTime(new Date());
		plr.setHalt(false);
		Map<String,Object> args = new HashMap<>();
		args.put("suggestion", suggestion);
		pressLightRecordService.recoverPressLightRecord(plr, user, args);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("title", "提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 确认
	 * @param pressLightRecord
	 * @return
	 */
	@RequestMapping("/confirmPressLightRecord.do")
	@ResponseBody
	public ModelMap confirmPressLightRecord(Long id,String suggestion,String userCode,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		PressLightRecord plr = pressLightRecordService.queryObjById(id);
		if(plr.getConfirmTime()!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该记录已确认!");
			return modelMap;
		}
		/*MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		User user = userService.queryByProperty("username", mcUser.getUsername());*/
		User user = userService.queryUserByEmployeeCode(userCode);
		Employee employee = employeeService.queryByProperty("code", userCode);
		user.setUsername(employee.getName());
		
		Map<String,Object> args = new HashMap<>();
		args.put("suggestion", suggestion);
		pressLightRecordService.confirmPressLightRecord(plr, user, args);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("title", "提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 *熄灯
	 * @param pressLightRecord
	 * @return
	 */
	@RequestMapping("/lightOutPressLightRecord.do")
	@ResponseBody
	public ModelMap lightOutPressLightRecord(Long id,String suggestion,String userCode,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		PressLightRecord plr = pressLightRecordService.queryObjById(id);
		if(plr.getLightOutTime()!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该记录已熄灯!");
			return modelMap;
		}
		//MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		User user = userService.queryUserByEmployeeCode(userCode);
		Employee employee = employeeService.queryByProperty("code", userCode);
		user.setUsername(employee.getName());
		//User user = userService.queryByProperty("username", mcUser.getUsername());
		Map<String,Object> args = new HashMap<>();
		args.put("suggestion", suggestion);
		pressLightRecordService.lightoutPressLightRecord(plr, user, args);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("title", "提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 根据id删除按灯记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/mcDeletePressLightRecord.do")
	@ResponseBody
	public ModelMap mcDeletePressLightRecord(Long id) {
		ModelMap modelMap = new ModelMap();
		PressLightRecord pr = pressLightRecordService.queryObjById(id);
		try {
			pressLightRecordService.deletePressLightRecord(pr);
		}catch(RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", e.getMessage());
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 根据故障类id查找按灯记录
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryPressLightRecordByBasicCode.do")
	@ResponseBody
	public List<PressLightRecord> queryPressLightRecordByBasicCode(String typeCode,String deviceSiteCode,HttpServletRequest request) {
		List<PressLightRecord> list = pressLightRecodeService.queryPressLightRecordByBasicCode(typeCode,deviceSiteCode);
		return list;
	}
	/**
	 * 根据按灯代码查询按灯信息
	 * @param code
	 * @return
	 */
	@RequestMapping("/queryPressLightByCode.do")
	@ResponseBody
	public PressLight queryPressLightByCode(String code) {
		PressLight pressLight = pressLightService.queryByProperty("code", code);
		if(null==pressLight){
			return null;
		}
		if(pressLight.getPressLightType().getLevel().intValue()!=1){
			PressLightType plType = pressLightTypeService.queryByProperty("code",pressLight.getPressLightType().getBasicCode());
			if(!plType.getDisabled()){
				pressLight.setPressLightType(plType);
			}
		}
		return pressLight;
	}
	/**
	 * 判断员工是否具有某操作权限
	 * @param employeeCode 员工代码
	 * @param operType 操作类型：lightOut:审核  recover：复核  confirm：确认
	 * @return
	 */
	@RequestMapping("/hasThePermission.do")
	@ResponseBody
	public ModelMap hasThePermission(String employeeCode,String operType) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryUserByEmployeeCode(employeeCode);
		if(user==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "该员工不存在!");
			return modelMap;
		}
		List<Role> roles = roleService.queryRolesByUserId(user.getId());
		boolean hasThePermission = false;
		
		String roleName = "";
		switch(operType) {
		case "lightOut":{
			roleName = Constant.Role.LIGHTOUT_ROLE;
			break;
		}
		case "recover":{
			roleName = Constant.Role.LIGHTOUT_RECOVER_ROLE;
			break;
		}
		case "confirm":{
			roleName = Constant.Role.LIGHTOUT_CONFIRM_ROLE;
			break;
		}
		}
		
		for(Role role : roles) {
			if(roleName.equals(role.getRoleName())) {
				hasThePermission = true;
				break;
			}
		}
		//没有该权限
		if(!hasThePermission) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("message", "您没有该权限，请联系管理员!");
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}
} 
