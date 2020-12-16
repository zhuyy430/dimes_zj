package com.digitzones.paperless.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.digitzones.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.User;
import com.digitzones.util.ZXingCode;

@RestController
@RequestMapping("/generateZxingCode")
public class GenerateZxingCodeController {
	public static Map<String,Object> zxingMap = new HashMap<>();
	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 生成无纸化登录二维码
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/generateZxingCodes.do")
	public ModelMap generateZxingCodes(HttpSession session,HttpServletRequest request,String data){
		ModelMap modelMap = new ModelMap();
		String dir = request.getServletContext().getRealPath("/")+"console/zxingcode/";
		data = UUID.randomUUID().toString();
		//data = new Date().getTime()+"";
		File parentDir = new File(dir);  //存储路径文件夹
		if(!parentDir.exists()) {
			parentDir.mkdirs();
		}
		File parentDirurl = new File(dir+data+".png");  //存储路径
		File logoFile = null;//new File("D://QrCode/logo3.png"); //logo
		String note = "登陆二维码";    
		ZXingCode.drawLogoQRCode(logoFile, parentDirurl, data, null);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("zxingdata", data);
		modelMap.addAttribute("filePath", "console/zxingcode/"+data+".png");
		return modelMap;
	}
	
	/**
	 * 检查无纸化登录二维码
	 * @param session
	 * @return
	 */
	@RequestMapping("/checkZxingCodes.do")
	public ModelMap checkZxingCodes(HttpSession session,String zxing){
		ModelMap modelMap =new ModelMap();
		User user = (User) zxingMap.get(zxing);
		if(user!=null){
			if(user.getEmployee()!=null){
				session.setAttribute("employee",employeeService.queryObjById(user.getEmployee().getCode()));
				modelMap.addAttribute("success",true);
				zxingMap.remove(zxing);
				return modelMap;
			}
		}
		modelMap.addAttribute("success", false);
		return modelMap;
	}
}
