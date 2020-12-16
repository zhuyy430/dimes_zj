package com.digitzones.mc.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.Classes;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.User;
import com.digitzones.service.IClassesService;
import com.digitzones.service.ILostTimeRecordService;
import com.digitzones.service.IUserService;
import com.digitzones.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/mcuser")
public class MCUserController {
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ILostTimeRecordService lostTimeRecordService;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	/**
	 * 获取权限用户信息
	 * @return
	 */
	@RequestMapping("/getAllMCUser.do")
	@ResponseBody
	public List<MCUser> getAllMCUser(HttpServletRequest request) {
		return mcUserService.getAllMCUser(request.getRemoteAddr());
	}
	/**
	 * 获取未关联的用户信息
	 * @return
	 */
	@RequestMapping("/getAllUser.do")
	@ResponseBody
	public List<MCUser> getAllEmployee(HttpServletRequest request) {
		return mcUserService.queryAvailableUsers(request.getRemoteAddr());
	}
	/**
	 * 添加权限用户
	 * @return
	 */
	@RequestMapping("/addMCUsers.do")
	@ResponseBody
	public ModelMap addMCUser(String objs,HttpServletRequest request) {
		String clientIp =  request.getRemoteAddr();
		ModelMap modelMap = new ModelMap();
		JSONArray jsonArray = JSONArray.parseArray(objs);
		List<MCUser> mcUsers = new ArrayList<>();
		for(int i = 0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			MCUser user = new MCUser();
			user.setClientIp(clientIp);
			user.setSign_in(false);
			user.setEmployeeCode(obj.getString("employeeCode"));
			user.setEmployeeICNo(obj.getString("employeeICNo"));
			user.setEmployeeName(obj.getString("employeeName"));
			user.setUsername(obj.getString("username"));
			mcUsers.add(user);
		}
		mcUserService.addMCUsers(mcUsers);
		return modelMap;
	}
	/**
	 * 删除权限用户
	 * @return
	 */
	@RequestMapping("/deleteMCUsers.do")
	@ResponseBody
	public ModelMap deleteMCUsers(String ids){
		ModelMap modelMap = new ModelMap();
		JSONArray jsonArray = JSONArray.parseArray(ids);
		List<Long> mcUsers = new ArrayList<>();
		for(int i = 0;i<jsonArray.size();i++) {
			mcUsers.add(jsonArray.getLong(i));
		}
		mcUserService.deleteMCUser(mcUsers);
		return modelMap;
	}
	/**
	 * 人员登入
	 * @return
	 */
	@RequestMapping("/mcuserLogin.do")
	@ResponseBody
	public ModelMap mcuserLogin(User user,String classCode,String className ,HttpServletRequest request){
		String ip = request.getRemoteAddr();
		ModelMap modelMap = new ModelMap();
		MCUser u = mcUserService.queryLoginUserByClientIp(ip);
		if(u!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "已有用户登录到此设备，请先登出 ！");
			return modelMap;
		}
		user.setPassword(new PasswordEncoder(user.getUsername()).encode(user.getPassword()));

		if(mcUserService.login(user,classCode,className,ip)) {
			modelMap.addAttribute("success", true);
			if(user.getEmployee()!=null){
				MaintenanceStaff m = maintenanceStaffService.queryByProperty("name", user.getEmployee().getName());
				m.setWorkStatus(Constant.MaintenanceStaffStatus.ONDUTY);
				maintenanceStaffService.updateObj(m);
			}
		}else{
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "您没有登录此设备的权限!");
		}
		return modelMap;
	}
	/**
	 * 人员登出
	 * @return
	 */
	@RequestMapping("/mcuserLogout.do")
	@ResponseBody
	public ModelMap mcuserLogout(User user,HttpServletRequest request){
		String ip = request.getRemoteAddr();
		ModelMap modelMap = new ModelMap();
		MCUser u = mcUserService.queryLoginUserByClientIp(ip);
		User logUser = userService.queryUserByUsername(user.getUsername());
		
		String password = new PasswordEncoder(user.getUsername()).encode(user.getPassword());
		
		if(password.equals(logUser.getPassword())) {
			mcUserService.logout(u);
			modelMap.addAttribute("success", true);
		}else{
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "密码不正确，请重新输入!");
		}
		return modelMap;
	}
	/**
	 * 人员扫描登入
	 * @return
	 */
	@RequestMapping("/scanQrLogin.do")
	@ResponseBody
	public ModelMap scanQrLogin(String employeeCode,String classCode,String className, HttpServletRequest request){
		String ip = request.getRemoteAddr();
		ModelMap modelMap = new ModelMap();
		MCUser user = mcUserService.queryLoginUserByClientIp(ip);
		if(user==null) {
			MCUser u = mcUserService.queryMCUserByEmployeeCodeAndClientIp(employeeCode, ip);
			if(u==null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "您没有登录此设备的权限!");
			}else {
				u.setSign_in(true);
				u.setClassCode(classCode);
				u.setClassName(className);
				mcUserService.updateMCUser(u);
				modelMap.addAttribute("success", true);
				if(u.getEmployeeName()!=null){
					MaintenanceStaff m = maintenanceStaffService.queryByProperty("code", u.getEmployeeCode());
					if(m!=null){
						maintenanceStaffService.updateStatus(m, Constant.MaintenanceStaffStatus.ONDUTY,
								Constant.ReceiveType.SYSTEMGASSIGN);
					}
				}
			}
		}else{
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "已有用户登录到此设备，请先登出 ！");
		}
		return modelMap;
	}
	/**
	 * 人员扫描登出
	 * @return
	 */
	@RequestMapping("/scanQrLogOut.do")
	@ResponseBody
	public ModelMap scanQrLogOut(String employeeCode,HttpServletRequest request){
		String ip = request.getRemoteAddr();
		ModelMap modelMap = new ModelMap();

			MCUser u = mcUserService.queryMCUserByEmployeeCodeAndClientIpAndsign_in(employeeCode, ip);
			if(u==null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "您没有登录此设备,无法签退!");
			}else {
				//查询为处理损时
				Map<String,Object> args = new HashMap<>();
				args.put("businessKey", workFlowKeyConfig.getLostTimeWorkflowKey());
				u.setClassCode("");
				u.setClassName("");
				mcUserService.logout(u);
				modelMap.addAttribute("success", true);
				if(u.getEmployeeName()!=null){
					MaintenanceStaff m = maintenanceStaffService.queryByProperty("code", u.getEmployeeCode());
					if(m!=null){
						maintenanceStaffService.updateStatus(m, Constant.MaintenanceStaffStatus.REST,
								Constant.ReceiveType.SYSTEMGASSIGN);
					}
				}
			}
		return modelMap;
	}
	/**
	 * 查询是否具有未处理损时记录
	 * @return
	 */
	@RequestMapping("/queryUnhandledLostTimeRecord.do")
	@ResponseBody
	public ModelMap queryUnhandledLostTimeRecord() {
		ModelMap modelMap = new ModelMap();
		//查询未处理损时
		Classes c = classesService.queryCurrentClasses();
		long lostTimeRecordCount = lostTimeRecordService.queryUnhandledLostTimeRecordCount4Classes(c);
		if(lostTimeRecordCount>0) {
			modelMap.addAttribute("hasUnhandledLostTimeRecords", true);
		}else {
			modelMap.addAttribute("hasUnhandledLostTimeRecords", false);
		}
		return modelMap;
	}
	/**
	 * 系统管理员登录
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/adminLogin.do")
	@ResponseBody
	public ModelMap adminLong(User user,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		if(!Constant.User.ADMIN.equals(user.getUsername())) {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","您不是系统管理员，没有此登录权限!");
			return modelMap;
		}
		User u = userService.queryUserByUsername(Constant.User.ADMIN);
		if(!u.getPassword().equals(new PasswordEncoder(Constant.User.ADMIN).encode(user.getPassword()))) {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","密码错误!");
			return modelMap;
		}
		modelMap.addAttribute("success",true);
		HttpSession session = request.getSession();
		session.setAttribute(Constant.User.ADMIN, u);
		return modelMap;
	}
	/**
	 * 根据IP获取登录用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryLoginMCUserByClientIp.do")
	@ResponseBody
	public ModelMap queryLoginMCUserByClientIp(HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		String clientIp = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(clientIp);
		List<RoleMCPower> list = mcUserService.queryMCPowers(mcUser.getUsername());
		modelMap.addAttribute("mcUser", mcUser);
		modelMap.addAttribute("mcPowers", list);
		return modelMap;
	}
	/**
	 * 判断用户名是否存在损时确认权限
	 * @return
	 */
	@RequestMapping("/mcQueryUserExistPowerWithChexkLostTime.do")
	@ResponseBody
	public User mcQueryUserExistPowerWithChexkLostTime(@RequestParam(value="empCode") String empCode,HttpServletRequest requestt) {
		User user = userService.queryUserByEmployeeCode(empCode);
		if(null==user){
			return null;
		}
		return user;
	}
	/**
	 * 根据员工代码或者用户名查询用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryUserByEmployeeCodeOrUserName.do")
	@ResponseBody
	public User queryUserByEmployeeCodeOrUserName(@RequestParam(value="empCode") String empCode,@RequestParam(value="userName") String userName,HttpServletRequest request) {
		if(empCode!=null&&empCode!=""){
			return userService.queryUserByEmployeeCode(empCode);
		}
		return userService.queryByProperty("username", userName);
	}
	/**
	 * 根据id查找MC端用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryMCUserById.do")
	@ResponseBody
	public MCUser queryMCUserById(Long id) {
		if(id!=null)
		return mcUserService.queryMCUserById(id);
		return null;
	}
}
