package com.digitzones.controllers;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.config.SysConfig;
import com.digitzones.constants.Constant;
import com.digitzones.model.Module;
import com.digitzones.model.Pager;
import com.digitzones.model.Power;
import com.digitzones.model.Role;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.User;
import com.digitzones.service.IModuleService;
import com.digitzones.service.IPowerService;
import com.digitzones.service.IRoleMCPowerService;
import com.digitzones.service.IRoleService;
import com.digitzones.service.IUserRoleService;
import com.digitzones.service.IUserService;
import com.digitzones.vo.ModuleVO2;
@Controller
@RequestMapping("/role")
public class RoleController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static Set<String> reLoginUser=new HashSet<>();
	private IRoleService roleService;
	private IUserRoleService userRoleService;
	private IPowerService powerService;
	@Autowired
	private IModuleService moduleService;
	@Autowired
	private IUserService userService;
	@Autowired
	 private SysConfig sysConfig;
	@Autowired
	private IRoleMCPowerService roleMCPowerService;
	@Autowired
	public void setPowerService(IPowerService powerService) {
		this.powerService = powerService;
	}
	@Autowired
	public void setUserRoleService(IUserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
	@Autowired
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	/**
	 * 查询角色信息
	 * @return
	 */
	@RequestMapping("/queryRoles.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryRoles(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from Role r where r.roleName!=?0 order by r.createDate";
		Pager<Role> pager = roleService.queryObjs(hql, page, rows, new Object[] {Constant.ADMIN});
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 添加 角色信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addRole.do")
	@ResponseBody
	public ModelMap addRole(Role role,Principal principal) {
		ModelMap modelMap = new ModelMap();
		Role u = roleService.queryByProperty("roleName",role.getRoleName());
		if(u!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "角色名称已被使用");
		}else {
			User loginUser = userService.queryUserByUsername(principal.getName());
			role.setCreateDate(new Date());
			if(loginUser!=null) {
				role.setCreateUserId(loginUser.getId());
				role.setCreateUsername(loginUser.getUsername());
			}
			roleService.addObj(role);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询角色信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryRoleById.do")
	@ResponseBody
	public Role queryRoleById(Long id) {
		Role role =  roleService.queryObjById(id);
		return role;
	}
	/**
	 * 更新角色信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateRole.do")
	@ResponseBody
	public ModelMap updateRole(Role role,Principal principal) {
		ModelMap modelMap = new ModelMap();
		Role r = roleService.queryObjById(role.getId());
		if(!r.getAllowDelete()) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "操作失败,该角色为预设角色，不允许修改!");
			return modelMap;
		}
		Role u = roleService.queryByProperty("roleName", role.getRoleName());
		if(u!=null && !u.getId().equals(role.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "角色名称已被使用");
		}else {
			User user = userService.queryUserByUsername(principal.getName());
			Role role0 = roleService.queryObjById(role.getId());
			if(user!=null) {
				role0.setModifyUserId(user.getId());
				role0.setModifyUsername(user.getUsername());
			}
			role0.setModifyDate(new Date());
			role0.setRoleName(role.getRoleName());
			role0.setNote(role.getNote());
			roleService.updateObj(role0);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteRole.do")
	@ResponseBody
	public ModelMap deleteRole(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Long roleId = Long.valueOf(id);
		Role r = roleService.queryObjById(roleId);
		if(!r.getAllowDelete()) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "删除失败,该角色为预设角色，不允许删除!");
			return modelMap;
		}
		//根据角色查询用户
		Long userCount = userRoleService.queryCountByRoleId(roleId);
		if(userCount>0) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "删除失败,角色已被使用!");
			return modelMap;
		}
		roleService.deleteObj(roleId);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该角色
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledRole.do")
	@ResponseBody
	public ModelMap disabledRole(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		Long roleId = Long.valueOf(id);
		ModelMap modelMap = new ModelMap();
		Role r = roleService.queryObjById(roleId);
		if(!r.getAllowDelete()) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "操作失败,该角色为预设角色，不允许停用!");
			return modelMap;
		}
		//根据角色查询用户
		Long userCount = userRoleService.queryCountByRoleId(roleId);
		if(userCount>0) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "操作失败,角色已被使用!");
			return modelMap;
		}
		Role d = roleService.queryObjById(roleId);
		if(d.getDisable()!=null) {
			d.setDisable(!d.getDisable());
		}else {
			d.setDisable(true);
		}
		roleService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * @param userId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryMenus.do")
	@ResponseBody
	public List<ModuleVO2> queryMenus(Long roleId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Principal principal){
		String username = principal.getName();
		User u = userService.queryUserByUsername(username);
		//查询所有模块
		List<Module> allModules = moduleService.queryAllTopModuleByUserId(u.getId());
		List<ModuleVO2> vos = new ArrayList<>();
		if(allModules!=null && allModules.size()>0) {
			for(Module m:allModules) {
				ModuleVO2 vo = module2Vo2(m);
				vos.add(vo);
				//递归查询下一级模块
				queryNextLevelModule(moduleService,vo);
			}
		}
		//根据角色id查找模块
		List<Module> roleModules = moduleService.queryAllModuleByRoleId(roleId);
		compareModule(roleModules,vos);
		return vos;
	}
	private void compareModule(List<Module> roleModules,List<ModuleVO2> vos) {
		if(roleModules==null || 
		   roleModules.size()<=0
		   || vos==null
		   || vos.size()<=0) {
			return ;
		}
		for(ModuleVO2 moduleVo : vos) {
			for(Module m : roleModules) {
				if(moduleVo.getId().equals(m.getId())) {
					moduleVo.setChecked(true);
					moduleVo.setSelected(true);
					compareModule(roleModules,moduleVo.getChildren());
				}
			}
		}
	}
	/**
	 * 递归查找子模块
	 * @param moduleService
	 * @param vo
	 */
	private  void queryNextLevelModule(IModuleService moduleService,ModuleVO2 vo) {
		if(!vo.getLeaf()) {
			List<Module> children = moduleService.querySubModuleByParentId(vo.getId());
			if(children!=null&&children.size()>0) {
				List<ModuleVO2> vos = new ArrayList<>();
				for(Module m : children) {
					ModuleVO2 v = module2Vo2(m);
					vos.add(v);
					queryNextLevelModule(moduleService,v);
				}
				vo.setChildren(vos);
			}
		}
	}
	private ModuleVO2 module2Vo2(Module m) {
		if(m==null) {
			return null;
		}
		ModuleVO2 vo = new ModuleVO2();
		vo.setId(m.getId());
		vo.setText(m.getName());
		vo.setUrl(m.getUrl());
		vo.setLeaf(m.getLeaf());
		vo.setNote(m.getNote());
		vo.setIconCls(m.getIcon());
		return vo;
	}
	
	/**
	 * @param userId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryPowers.do")
	@ResponseBody
	public JSONArray queryRoles(Long roleId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Principal principal){
		User u = userService.queryUserByUsername(principal.getName());
		//查询所有权限
		List<Power> allPowers = powerService.queryAllPowersByUserId(u.getId());
		//根据角色id查找权限
		List<Power> rolePowers = powerService.queryPowersByRoleId(roleId);
		JSONArray vos = new JSONArray();
		if(allPowers!=null) {
			for(int i = 0;i<allPowers.size();i++) {
				Power role = allPowers.get(i);
				JSONObject vo = model2JSONObject(role);
				vo.put("checked",false);
				if(rolePowers!=null) {
					for(int j = 0;j<rolePowers.size();j++) {
						Power r = rolePowers.get(j);
						if(role.getId().equals(r.getId())) {
							vo.put("checked",true);
							break;
						}
					}
				}
				vos.add(vo);
			}
		}
		return vos;
	}
	/**
	 * @param roleId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryPowersByGroup.do")
	@ResponseBody
	public JSONArray queryPowersByGroup(String group ,Long roleId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		//查询所有权限
		List<Power> allPowers = powerService.queryPowersByGroup(group);
		//根据角色id查找权限
		List<Power> rolePowers = powerService.queryPowersByRoleIdAndGroup(roleId, group);
		JSONArray vos = new JSONArray();
		if(allPowers!=null) {
			for(int i = 0;i<allPowers.size();i++) {
				Power role = allPowers.get(i);
				JSONObject vo = model2JSONObject(role);
				if(rolePowers!=null) {
					for(int j = 0;j<rolePowers.size();j++) {
						Power r = rolePowers.get(j);
						if(role.getId().equals(r.getId())) {
							vo.put("checked",true);
							break;
						}
					}
				}
				vos.add(vo);
			}
		}
		return vos;
	}
	private JSONObject model2JSONObject(Power r) {
		if(r == null) {
			return null;
		}
		JSONObject vo = new JSONObject();
		vo.put("id",r.getId());
		vo.put("powerCode",r.getPowerCode());
		vo.put("powerName",r.getPowerName());
		vo.put("note",r.getNote());
		vo.put("group",r.getGroup());
		return vo;
	}
	private JSONObject user2JSONObject(User user) {
		if(user == null) {
			return null;
		}
		JSONObject vo = new JSONObject();
		vo.put("id",user.getId());
		vo.put("username",user.getUsername());
		vo.put("realName",user.getRealName());
		vo.put("tel",user.getTel());
		vo.put("createDate",user.getCreateDate()!=null?format.format(user.getCreateDate()):"");
		return vo;
	}
	/**
	 * 为角色分配权限
	 * @param roleId
	 * @param powerIds
	 * @return
	 */
	@RequestMapping("/signPowers.do")
	@ResponseBody
	public ModelMap signPowers(Long roleId,String powerIds) {
		ModelMap modelMap = new ModelMap();
		if(powerIds!=null) {
			if(powerIds.contains("[")) {
				powerIds = powerIds.replace("[", "");
			}
			if(powerIds.contains("]")) {
				powerIds = powerIds.replace("]", "");
			}
			powerIds = powerIds.replace("\"", "");
			String[] idArray = powerIds.split(",");

			roleService.addPowersForRole(roleId, idArray);
			List<User> uList=userRoleService.queryUsersByRoleId(roleId);
			for(User user:uList) {
				reLoginUser.add(user.getUsername());
			}
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("message", "操作完成!");
			modelMap.addAttribute("title", "操作提示!");
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("message", "操作失败!");
			modelMap.addAttribute("title", "操作提示!");
		}
		return modelMap;
	}
	/**
	 * 为角色分配MC端权限
	 * @param roleId
	 * @param powerIds
	 * @return
	 */
	@RequestMapping("/signMCPowers.do")
	@ResponseBody
	public ModelMap signMCPowers(Long roleId,String powerNames) {
		ModelMap modelMap = new ModelMap();
		if(powerNames!=null) {
			if(powerNames.contains("[")) {
				powerNames = powerNames.replace("[", "");
			}
			if(powerNames.contains("]")) {
				powerNames = powerNames.replace("]", "");
			}
			powerNames = powerNames.replace("\"", "");
			String[] nameArray = powerNames.split(",");
			
			roleService.addMCPowersForRole(roleId, nameArray);
			/*List<User> uList=userRoleService.queryUsersByRoleId(roleId);
			for(User user:uList) {
				reLoginUser.add(user.getUsername());
			}*/
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("message", "操作完成!");
			modelMap.addAttribute("title", "操作提示!");
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("message", "操作失败!");
			modelMap.addAttribute("title", "操作提示!");
		}
		return modelMap;
	}
	/**
	 * 为角色分配菜单
	 * @param roleId
	 * @param powerIds
	 * @return
	 */
	@RequestMapping("/signMenus.do")
	@ResponseBody
	public ModelMap signMenus(Long roleId,String menuIds) {
 		ModelMap modelMap = new ModelMap();
		if(menuIds!=null) {
			if(menuIds.contains("[")) {
				menuIds = menuIds.replace("[", "");
			}
			if(menuIds.contains("]")) {
				menuIds = menuIds.replace("]", "");
			}
			String[] idArray = menuIds.split(",");
			roleService.addMenusForRole(roleId, idArray);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","操作完成!");
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * @param userId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryUsers.do")
	@ResponseBody
	public JSONArray queryUsers(Long roleId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Principal principal){
		//查询所有用户
		List<User> allUsers = userService.queryAllUsers();
		//根据角色id查找用户
		List<User> roleUsers = userService.queryUsersByRoleId(roleId);
		JSONArray vos = new JSONArray();
		if(allUsers!=null) {
			for(int i = 0;i<allUsers.size();i++) {
				User user = allUsers.get(i);
				JSONObject vo = user2JSONObject(user);
				vo.put("checked",false);
				if(roleUsers!=null) {
					for(int j = 0;j<roleUsers.size();j++) {
						User roleUser = roleUsers.get(j);
						if(roleUser.getId().equals(user.getId())) {
							vo.put("checked",true);
							break;
						}
					}
				}
				vos.add(vo);
			}
		}
		return vos;
	}
	/**
	 * 为角色分配用户
	 * @param roleId
	 * @param powerIds
	 * @return
	 */
	@RequestMapping("/signUsers.do")
	@ResponseBody
	public ModelMap signUsers(Long roleId,String userIds) {
		ModelMap modelMap = new ModelMap();
		if(userIds!=null) {
			if(userIds.contains("[")) {
				userIds = userIds.replace("[", "");
			}
			if(userIds.contains("]")) {
				userIds = userIds.replace("]", "");
			}
			String[] idArray = userIds.split(",");
			roleService.addUsersForRole(roleId, idArray);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","操作完成!");
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 查询MC端权限
	 * @return
	 */
	@RequestMapping("/queryMCPowers.do")
	@ResponseBody
	public ModelMap queryMCPowers(Long roleId) {
		ModelMap modelMap = new ModelMap();
		List<RoleMCPower> roleMCPowerList = roleMCPowerService.queryByRoleId(roleId);
		modelMap.addAttribute("mcPowers", sysConfig.getMcPowers().split(","));
		modelMap.addAttribute("roleMCPowers", roleMCPowerList);
		return modelMap;
	}
} 
