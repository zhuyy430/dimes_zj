package com.digitzones.app.controller;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.app.service.IAppProductionUnitService;
import com.digitzones.app.service.IAppUserService;
import com.digitzones.controllers.RoleController;
import com.digitzones.dto.UserDto;
import com.digitzones.model.*;
import com.digitzones.service.*;
import com.digitzones.util.JwtTokenUnit;
import com.digitzones.util.PasswordEncoder;
import com.digitzones.vo.EmployeeVO;
import com.digitzones.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
@Controller
@RequestMapping("/AppUser")
public class AppUserController {
	@Autowired
	IAppUserService appUserService;
	@Autowired
	IUserService userService;
	@Autowired
	IAppProductionUnitService appProductionUnitService;
	@Autowired
	IEmployeeService employeeService; 
	@Autowired
	private IAppClientMapService appClientMapService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IPowerService powerService;
	/**
	 * 获取所有用户
	 * @return
	 */
	@RequestMapping("/queryAllEmployee.do")
	@ResponseBody
	public List<UserVO> queryAllEmployee(HttpServletRequest request){
		List<User> users = appUserService.queryAllUser();
		List<User> userList = new ArrayList<>();
		List<UserVO> userVoList = new ArrayList<>();
		for(User user:users){
			if(null==user.getEmployee()){
				userList.add(user);
			}
		}
		users.removeAll(userList);
		for(User user:users){
			UserVO userVo = USERmodel2vo(user,request);
			userVoList.add(userVo);
		}
		return userVoList;
	}
	
	
	/**
	 * 根据部门分类查询所有人员
	 */
	@RequestMapping("/queryAllEmployeeByDepartment.do")
	@ResponseBody
	public ModelMap queryAllEmployeeByDepartment() {
		ModelMap modelMap=new ModelMap();
		//查找所有有员工的部门
		List<Department> dList=departmentService.queryDepartmentsByHaveEmployee();	
		List<Employee> eList=employeeService.queryAllEmployees();
		List<Map<String,Object>> dmlist = new ArrayList<>();
		
		for(Department d:dList) {
			Map<String,Object> map=new HashMap<>();
			map.put("value",d.getCode());
			map.put("label",d.getName());
			
			List<Map<String,Object>> emlist = new ArrayList<>();
			for(Employee e:eList) {
				if(e.getcDept_Num()!=null&&e.getcDept_Num().equals(d.getCode())) {
					Map<String,Object> map1=new HashMap<>();
					map1.put("value",e.getCode());
					map1.put("label",e.getName());
					emlist.add(map1);
				}
			}
			map.put("children",emlist);
			dmlist.add(map);
		}
		modelMap.addAttribute("list",dmlist);
		
		return modelMap;
	}
	
	
	
	/**
	 * 根据部门分类查询所有非维修人员
	 */
	@RequestMapping("/queryAllEmployeeAndNotMaintenanceStaffByDepartment.do")
	@ResponseBody
	public ModelMap queryAllEmployeeAndNotMaintenanceStaffByDepartment() {
		ModelMap modelMap=new ModelMap();
		//查找所有非员工的部门
		List<Department> dList=departmentService.queryDepartmentsByNotHaveMaintenanceStaff();	
		List<Employee> eList=employeeService.queryAllEmployeeAndNotMaintenanceStaff();
		List<Map<String,Object>> dmlist = new ArrayList<>();
		
		for(Department d:dList) {
			Map<String,Object> map=new HashMap<>();
			map.put("value",d.getCode());
			map.put("label",d.getName());
			
			List<Map<String,Object>> emlist = new ArrayList<>();
			for(Employee e:eList) {
				if(e.getcDept_Num()!=null&&e.getcDept_Num().equals(d.getCode())) {
					Map<String,Object> map1=new HashMap<>();
					map1.put("value",e.getCode());
					map1.put("label",e.getName());
					emlist.add(map1);
				}
			}
			map.put("children",emlist);
			dmlist.add(map);
		}
		modelMap.addAttribute("list",dmlist);
		
		return modelMap;
	}
	
	
	/**
	 * 根据部门分类查询所有维修人员
	 */
	@RequestMapping("/queryAllMaintenanceStaffByDepartment.do")
	@ResponseBody
	public ModelMap queryAllMaintenanceStaffByDepartment() {
		ModelMap modelMap=new ModelMap();
		//查找所有维修人员的部门
		List<Department> dList=departmentService.queryDepartmentsByHaveMaintenanceStaff();	
		List<Employee> eList=employeeService.queryAllEmployeeAndMaintenanceStaff();
		List<Map<String,Object>> dmlist = new ArrayList<>();
		
		for(Department d:dList) {
			Map<String,Object> map=new HashMap<>();
			map.put("value",d.getCode());
			map.put("label",d.getName());
			
			List<Map<String,Object>> emlist = new ArrayList<>();
			for(Employee e:eList) {
				if(e.getcDept_Num()!=null&&e.getcDept_Num().equals(d.getCode())) {
					Map<String,Object> map1=new HashMap<>();
					map1.put("value",e.getCode());
					map1.put("label",e.getName());
					emlist.add(map1);
				}
			}
			map.put("children",emlist);
			dmlist.add(map);
		}
		modelMap.addAttribute("list",dmlist);
		
		return modelMap;
	}
	
	
	/**
	 * 根据部门分类查询所有用户
	 */
	@RequestMapping("/queryAllUserByDepartment.do")
	@ResponseBody
	public ModelMap queryAllUserByDepartment() {
		ModelMap modelMap=new ModelMap();
		//查找所有有员工的部门
		List<Department> dList=departmentService.queryDepartmentsByHaveEmployee();	
		List<User> eList=userService.queryAllUsers();
		List<Map<String,Object>> dmlist = new ArrayList<>();
		
		for(Department d:dList) {
			Map<String,Object> map=new HashMap<>();
			map.put("value",d.getCode());
			map.put("label",d.getName());
			
			List<Map<String,Object>> emlist = new ArrayList<>();
			for(User e:eList) {
				if(e.getEmployee()!=null&&e.getEmployee().getcDept_Num()!=null&&e.getEmployee().getcDept_Num().equals(d.getCode())) {
					Map<String,Object> map1=new HashMap<>();
					map1.put("value",e.getId());
					map1.put("label",e.getEmployee().getName());
					emlist.add(map1);
				}
			}
			map.put("children",emlist);
			dmlist.add(map);
		}
		modelMap.addAttribute("list",dmlist);
		
		return modelMap;
	}
	
	
	
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public ModelMap login(@RequestBody UserDto user, HttpServletRequest request) {
		ModelMap modelMap=new ModelMap();
		/*Properties props = new Properties();
		try {
			InputStream  in=PropertyUtil.class.getClassLoader().getResourceAsStream("verson.properties");
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String appVerson=props.getProperty("appVerson");*/
		String token;
		//if(user.getVerson().equals(appVerson)||user.getVerson().equals("")) {
			User u = userService.login(user.getUsername(), new PasswordEncoder(user.getUsername()).encode(user.getPassword()));
			
			if(null!=u&&u.getEmployee()!=null){
				token= JwtTokenUnit.sign(u.getUsername(), u.getId());
			}else {
				if(null==u) {
					modelMap.addAttribute("status", "loginMsg");
					modelMap.addAttribute("msg", "账号或密码错误");
					modelMap.addAttribute("success", false);
					return modelMap;
				}else {
					modelMap.addAttribute("status", "NoEmployee");
					modelMap.addAttribute("msg", "登录失败，该用户没有绑定员工");
					modelMap.addAttribute("success", false);
					return modelMap;
				}
				
			}
			
			
			String clientId = user.getClientid();
			AppClientMap appClientMap = appClientMapService.queryByProperty("username", user.getUsername());
			if(appClientMap != null) {
				if(clientId!=null && !"".equals(clientId.trim())) {
					if(!clientId.equals(appClientMap.getCid())) {
						appClientMap.setCid(clientId);
	
						appClientMapService.updateObj(appClientMap);
					}
				}
			}else {
				if(!StringUtils.isEmpty(clientId)) {
					AppClientMap map = new AppClientMap();
					map.setUsername(user.getUsername());
					map.setCid(clientId);
	
					appClientMapService.addObj(map);
				}
			}
			
			RoleController.reLoginUser.remove(user.getUsername());
			Map<String,Object> rUser = new HashMap<String,Object>(); 
			rUser.put("username", u.getUsername());
			rUser.put("userId", u.getId());
			modelMap.addAttribute("powerCodes", queryPowerCodeWithUser(u.getId()));
			modelMap.addAttribute("user", rUser);
			Employee employee = u.getEmployee();
			modelMap.addAttribute("employee", employee);
			modelMap.addAttribute("token", token);
			modelMap.addAttribute("success", true);
		/*}else {
			modelMap.addAttribute("status", "update");
			modelMap.addAttribute("msg", "您所使用的版本过旧，请更新");
			modelMap.addAttribute("success", false);
		}*/
		return modelMap;
	}
	
	
	/**
	 * 快速登录
	 */
	@RequestMapping("/fLogin.do")
	@ResponseBody
	public ModelMap fLogin(@RequestBody JSONObject data,HttpServletRequest request) {
		ModelMap modelMap=new ModelMap();
		String verson = data.getString("verson");
			String token =request.getHeader("accessToken");
			String username=JwtTokenUnit.getUsername(token);
			User u=userService.queryUserByUsername(username);
			if(null!=u&&u.getEmployee()!=null){
				RoleController.reLoginUser.remove(username);
				Map<String,Object> rUser = new HashMap<String,Object>(); 
				rUser.put("username", u.getUsername());
				rUser.put("userId", u.getId());
				modelMap.addAttribute("powerCodes", queryPowerCodeWithUser(u.getId()));
				modelMap.addAttribute("user", rUser);
				Employee employee = u.getEmployee();
				modelMap.addAttribute("employee", employee);
				modelMap.addAttribute("success", true);
			}else {
				if(null==u) {
					modelMap.addAttribute("status", "loginMsg");
					modelMap.addAttribute("msg", "账号或密码错误");
					modelMap.addAttribute("success", false);
					return modelMap;
				}else {
					modelMap.addAttribute("status", "NoEmployee");
					modelMap.addAttribute("msg", "登录失败，该用户没有绑定员工");
					modelMap.addAttribute("success", false);
					return modelMap;
				}
			}
			
		/*}else {
			modelMap.addAttribute("status", "update");
			modelMap.addAttribute("msg", "您所使用的版本过旧，请更新");
			modelMap.addAttribute("success", false);
		}*/
		return modelMap;
	}
	
	
	/**
	 * 根据id查询用户信息
	 */
	@RequestMapping("/queryUserById.do")
	@ResponseBody
	public UserVO queryUserById(@RequestBody JSONObject data, HttpServletRequest request) {
		Long userId = data.getLong("userId");
		User user =  userService.queryObjById(userId);
		return USERmodel2vo(user,request);
	}
	/**
	 * 更新用户信息
	 */
	@RequestMapping("/UpdateUserById.do")
	@ResponseBody
	public ModelMap UpdateUserById(@RequestBody JSONObject data, HttpServletRequest request) {
		Long userId = data.getLong("userId");
		String newPassword = data.getString("newPassword");
		String oldPassword = data.getString("oldPassword");
		Long ProductionUnitId = data.getLong("ProductionUnitId");
		ModelMap modelMap = new ModelMap();
		User user =  userService.queryObjById(userId);
		Employee emp = user.getEmployee();
		if(null!=ProductionUnitId&&!"".equals(ProductionUnitId)){
			ProductionUnit pro = appProductionUnitService.queryObjById(ProductionUnitId);
			employeeService.updateObj(emp);
		}
		if(new PasswordEncoder(user.getUsername()).encode(oldPassword).equals(user.getPassword())){
			user.setModifyDate(new Date());
			user.setPassword(new PasswordEncoder(user.getUsername()).encode(newPassword));
			userService.updateObj(user);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("msg", "密码错误,请重新输入!");
		return modelMap;
	}
	
	
	
	
	/**
	 * 返回用户权限
	 */
	public List<String> queryPowerCodeWithUser(Long userId) {
		List<Role> rList= roleService.queryRolesByUserId(userId);
		List<String> pList=new ArrayList<>();
		for(Role r:rList) {
			List<Power> rpList=powerService.queryPowersByRoleId(r.getId());
			for(Power rp:rpList) {
				pList.add(rp.getPowerCode());
			}
		}
		return pList;
	}
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	private EmployeeVO EMPmodel2vo(Employee e,HttpServletRequest request) {
		if(e == null) {
			return null;
		}

		EmployeeVO vo = new EmployeeVO();
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		return vo;
	}
	/**
	 * @param u
	 * @return
	 */
	private UserVO USERmodel2vo(User u,HttpServletRequest request) {
		if(u == null) {
			return null;
		}

		UserVO vo = new UserVO();
		vo.setId(u.getId());
		vo.setUsername(u.getUsername());
		vo.setPassword(u.getPassword());
		vo.setRealName(u.getRealName());
		vo.setEmail(u.getEmail());
		vo.setTel(u.getTel());
		vo.setCreateDate(u.getCreateDate());
		vo.setNote(u.getNote());
		vo.setDisable(u.getDisable());
		vo.setCreateUserId(u.getCreateUserId());
		vo.setCreateUsername(u.getCreateUsername());
		vo.setModifyUserId(u.getModifyUserId());
		vo.setModifyUsername(u.getModifyUsername());
		vo.setModifyDate(u.getModifyDate());
		vo.setEmployee(u.getEmployee());
		vo.setPicName(u.getPicName());
		vo.setAllowDelete(u.getAllowDelete());
		return vo;
	}
	
}
