package com.digitzones.paperless.controller;

import com.alibaba.fastjson.JSONObject;
import com.digitzones.model.Classes;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.*;
import com.digitzones.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
@Controller
@RequestMapping("/paperlessUser")
public class PaperLessUserController {
	private DecimalFormat format = new DecimalFormat("#.00");
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IUserService userService;
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * 用户登录
	 * @return
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public ModelMap login(@RequestBody JSONObject data,HttpServletRequest request) {
		String username = data.getString("username");
		String password = data.getString("password");
		ModelMap modelMap = new ModelMap();
		HttpSession session = request.getSession();
		User u = userService.login(username, new PasswordEncoder(username).encode(password));
		if(null==u) {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","用户名或者密码错误！");
		}else {
			if(null!=u.getEmployee()){
				modelMap.addAttribute("success",true);
				session.setAttribute("employee",employeeService.queryObjById(u.getEmployee().getCode()));
			}else{
				modelMap.addAttribute("success",false);
				modelMap.addAttribute("msg","该账户没有分配员工,无法登入！");
			}
		}
		return modelMap;
	}
	
	/**
	 * 人员扫描登入
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/scanQrLogin.do")
	@ResponseBody
	public ModelMap scanQrLogin(String data,Long userId,String employeeCode,HttpServletRequest request){
		HttpSession session = request.getSession();
		ModelMap modelMap = new ModelMap();
		if(data!=null&&!data.equals("")){//扫描二维码登录
			Map<String, Object> map = (Map<String, Object>) session.getAttribute("msg");
			User zxinguser = userService.queryObjById(userId);
			if(map.get(data).equals("")){
				map.remove(data);
				map.put(data, zxinguser);
			}
			session.setAttribute("zxing",map);
			session.setAttribute("employee",zxinguser.getEmployee());
			modelMap.addAttribute("success",true);
			return modelMap;
		}
		Employee emp = employeeService.queryByProperty("ICNo", employeeCode);
		if(emp!=null){
			User user = userService.queryUserByEmployeeCode(emp.getCode());
			if(user!=null){
				modelMap.addAttribute("success",true);
				session.setAttribute("employee",emp);
				return modelMap;
			}
		}
		modelMap.addAttribute("success",false);
		modelMap.addAttribute("msg","未找到员工！");
		return modelMap;
	}
	/**
	 * 扫描二维码登录
	 * @return
	 */
	@RequestMapping("/loginZxing.do")
	@ResponseBody
	public ModelMap loginZxing(@RequestBody JSONObject data,HttpServletRequest request) {
		String zxing = data.getString("data");
		Long userId = data.getLong("userId");
		Map<String, Object> zxingMap = GenerateZxingCodeController.zxingMap;
		ModelMap modelMap = new ModelMap();
		if(zxing!=null&&!zxing.equals("")){//扫描二维码登录
			User zxinguser = userService.queryObjById(userId);
			zxingMap.put(zxing, zxinguser);
			modelMap.addAttribute("success",true);
			return modelMap;
		}
		modelMap.addAttribute("msg","二维码无效,请重新登录");
		modelMap.addAttribute("success",false);
		return modelMap;
	}
	/**
	 * 用户登录
	 * @return
	 */
	@RequestMapping("/logout.do")
	@ResponseBody
	public ModelMap logout(String data,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		HttpSession session = request.getSession();
		session.removeAttribute("employee");
		modelMap.addAttribute("success",true);
		return modelMap;
	}
} 
