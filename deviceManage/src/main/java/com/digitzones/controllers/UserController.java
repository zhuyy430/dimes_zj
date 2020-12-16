package com.digitzones.controllers;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.digitzones.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.constants.Constant;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.Position;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.Role;
import com.digitzones.model.User;
import com.digitzones.service.IRoleService;
import com.digitzones.service.IUserService;
import com.digitzones.util.PasswordEncoder;
import com.digitzones.vo.EmployeeVO;
import com.digitzones.vo.RoleVO;
import com.digitzones.vo.UserVO;
@Controller
@RequestMapping("/user")
public class UserController {
	private IUserService userService;
	private IRoleService roleService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	/**
	 * 查询用户信息
	 * @return
	 */
	@RequestMapping("/queryUsers.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryUsers(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		List<UserVO> dataList = new ArrayList<>();
		/**---------------------比对ERP中的员工，如果在ERP中不存在该员工，则将该用户员工字段置为空----------------*/
		List<Employee> employees = employeeService.queryAllEmployees();
		List<String> employeeCodesInUser = userService.queryAllEmployeeCodes();
		if(!CollectionUtils.isEmpty(employeeCodesInUser) && !CollectionUtils.isEmpty(employees)){
			line:for(String employeeCode : employeeCodesInUser){
					if (employeeCode==null){
						continue ;
					}
					boolean isExist = false;
					for(Employee emp : employees){
						if(employeeCode.equals(emp.getCode())){
							isExist = true;
							continue line;
						}
					}

					if(!isExist){
						userService.updateUsersEmployeeNull(employeeCode);
					}
				}
		}
		/**---------------------比对ERP中的员工，如果在ERP中不存在该员工，则将该用户员工字段置为空----------------*/
		Pager<User> pager = userService.queryObjs("from User order by createDate desc", page, rows, new Object[] {});
		List<User> data = pager.getData();
		for(User d:data){
			UserVO userVo = USERmodel2vo(d);
			dataList.add(userVo);
		}
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", dataList);
		return modelMap;
	}
	
	/**
	 * 添加 用户信息
	 * @return
	 */
	@RequestMapping("/addUser.do")
	@ResponseBody
	public ModelMap addUser(User user,Principal principal) {
		ModelMap modelMap = new ModelMap();
		
		int count = userService.queryCount();
		if(count>=Constant.USER_COUNT) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", Constant.USER_EXCEED_MSG);
			
			return modelMap;
		}
		
		User u = userService.queryByProperty("username", user.getUsername());
		if(u!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "用户名称已被使用");
		}else {
			/*HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
			User loginUser = userService.queryUserByUsername(principal.getName());
			user.setCreateDate(new Date());
			user.setModifyDate(new Date());
			if(loginUser!=null) {
				user.setCreateUserId(loginUser.getId());
				user.setCreateUsername(loginUser.getUsername());
				user.setModifyUserId(loginUser.getId());
				user.setModifyUsername(loginUser.getUsername());
			}
			if(user.getEmployee().getCode()==null) {
				user.setEmployee(null);
			}
			user.setPassword(new PasswordEncoder(user.getUsername()).encode(user.getPassword()));
			userService.addObj(user);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询用户信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryUserById.do")
	@ResponseBody
	public UserVO queryUserById(Long id) {
		User user =  userService.queryObjById(id);
		return USERmodel2vo(user);
	}

	/**
	 * 更新用户信息
	 * @return
	 */
	@RequestMapping("/updateUser.do")
	@ResponseBody
	public ModelMap updateUser(User user,Principal principal) {
		ModelMap modelMap = new ModelMap();
		User u = userService.queryByProperty("username", user.getUsername());
		if(u!=null && !u.getId().equals(user.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "用户名称已被使用");
		}else {
			User user0 = userService.queryObjById(user.getId());
			/*HttpSession session = request.getSession();
			User loginUser = (User)session.getAttribute(Constant.User.LOGIN_USER);*/
			User loginUser = userService.queryUserByUsername(principal.getName());
			if(loginUser!=null) {
				user0.setModifyUserId(loginUser.getId());
				user0.setModifyUsername(loginUser.getUsername());
			}
			user0.setModifyDate(new Date());
			user0.setUsername(user.getUsername());
			user0.setNote(user.getNote());
			if(user.getEmployee()!=null) {
				user0.setEmployee(user.getEmployee());
			}
			userService.updateObj(user0);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "更新成功!");
		}
		return modelMap;
	}

	/**
	 * 根据id删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteUser.do")
	@ResponseBody
	public ModelMap deleteUser(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		userService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该班次
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledUser.do")
	@ResponseBody
	public ModelMap disabledUser(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		User d = userService.queryObjById(Long.valueOf(id));
		d.setDisable(!d.getDisable());

		userService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public ModelMap login(@RequestBody User user,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		HttpSession session = request.getSession();
		User u = userService.login(user.getUsername(), new PasswordEncoder(user.getUsername()).encode(user.getPassword()));
		if(u==null) {
			modelMap.addAttribute("success",false);
		}else {
			modelMap.addAttribute("success",true);
			session.setAttribute(Constant.User.LOGIN_USER, u);
		}
		return modelMap;
	}
	/**
	 * 查询非当前的所有用户
	 * @return
	 */
	@RequestMapping("/queryNotCurrentUsers.do")
	@ResponseBody
	public List<Employee> queryNotCurrentUsers(Principal principal){
		List<Employee> dataList = new ArrayList<>();
		String username = principal.getName();
		User currentUser = userService.queryByProperty("username", username);
		if(currentUser!=null) {
			List<User> data = userService.queryNotCurrentUsers(currentUser.getId());
			for(User d:data){
				if(d.getEmployee()!=null) {
					Employee employee = employeeService.queryObjById(d.getEmployee().getCode());
					dataList.add(employee);
				}
			}
		}else {
			List<User> data = userService.queryAllUsers();
			for(User d:data){
				if(d.getEmployee()!=null) {
					Employee employee = employeeService.queryObjById(d.getEmployee().getCode());
					dataList.add(employee);
				}
			}
		}
		return dataList;
	}
	/**
	 * 1、根据用户id查找角色
	 * 2、查找所有角色
	 * @param userId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryRoles.do")
	@ResponseBody
	public List<RoleVO> queryRoles(Long userId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		List<Role> allRoles = roleService.queryAllRoles();
		List<Role> userRoles = roleService.queryRolesByUserId(userId);
		List<RoleVO> vos = new ArrayList<>();
		if(allRoles!=null) {
			for(int i = 0;i<allRoles.size();i++) {
				Role role = allRoles.get(i);
				RoleVO vo = model2vo(role);
				if(userRoles!=null) {
					for(int j = 0;j<userRoles.size();j++) {
						Role r = userRoles.get(j);
						if(role.getId().equals(r.getId())) {
							vo.setChecked(true);
							break;
						}
					}
				}
				vos.add(vo);
			}
		}
		return vos;
	}

	private RoleVO model2vo(Role r) {
		if(r == null) {
			return null;
		}
		RoleVO vo = new RoleVO();
		vo.setId(r.getId());
		vo.setRoleName(r.getRoleName());
		vo.setNote(r.getNote());
		return vo;
	}
	/**
	 * 为用户分配角色
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@RequestMapping("/signRoles.do")
	@ResponseBody
	public ModelMap signRoles(Long userId,String roleIds) {
		ModelMap modelMap = new ModelMap();
		if(roleIds!=null) {
			if(roleIds.contains("[")) {
				roleIds = roleIds.replace("[", "");
			}
			if(roleIds.contains("]")) {
				roleIds = roleIds.replace("]", "");
			}

			String[] idArray = roleIds.split(",");

			userService.addRolesForUser(userId, idArray);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","操作完成!");
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 修改密码
	 * @param user
	 * @param principal
	 * @return
	 */
	@RequestMapping("/modifyPassword.do")
	@ResponseBody
	public ModelMap modifyPassword(User user,Principal principal,String newpassword) {
		String username = user.getUsername();
		PasswordEncoder encoder = new PasswordEncoder(principal.getName());
		String password = encoder.encode(user.getPassword());
		ModelMap modelMap = new ModelMap();
		User u = userService.queryByProperty("username", username);
		if(!u.getPassword().equals(password)) {
			modelMap.addAttribute("statusCode",300);
			modelMap.addAttribute("message","原始密码不正确!");
		}else {
			u.setPassword(encoder.encode(newpassword));
			userService.updateObj(u);
			modelMap.addAttribute("statusCode",200);
			modelMap.addAttribute("message","操作成功!");
		}
		return modelMap;
	}
	/**
	 * @param e
	 * @return
	 */
	private EmployeeVO EMPmodel2vo(Employee e) {
		if(e == null) {
			return null;
		}
		EmployeeVO vo = new EmployeeVO();
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		return vo;
	}
	/**
	 * 
	 * @param u
	 * @return
	 */
	private UserVO USERmodel2vo(User u) {
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

	/**
	 * 查找当前登录用户信息
	 * @param principal
	 * @return
	 */
	@RequestMapping("/queryLoginUser.do")
	@ResponseBody
	public User queryLoginUser(Principal principal){
		return userService.queryByProperty("username",principal.getName());
	}
} 
