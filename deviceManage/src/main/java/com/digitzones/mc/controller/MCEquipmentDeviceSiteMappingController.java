package com.digitzones.mc.controller;

import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCEquipmentDeviceSiteMappingService;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/mcequipment")
public class MCEquipmentDeviceSiteMappingController {
	private IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService;
	private IDeviceSiteService deviceSiteService;
	private IEquipmentService equipmentService;
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	@Autowired
	public void setEquipmentDeviceSiteMappingService(IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService) {
		this.equipmentDeviceSiteMappingService = equipmentDeviceSiteMappingService;
	}
	
	@Autowired
	public void setEquipmentService(IEquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
	@Autowired
	private IMCEquipmentDeviceSiteMappingService IMCEService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 根据设备站点代码查找装备关联
	 */
	@RequestMapping("/findList.do")
	@ResponseBody
	public List<EquipmentDeviceSiteMapping> findEquipmentDeviceSiteMappinglist(@RequestParam(value="deviceSiteCode")String deviceSiteCode,
			@RequestParam(value="searchText")String equipmentName,HttpServletRequest request){
		List<EquipmentDeviceSiteMapping> equipment = IMCEService.findEquipmentDeviceSiteMappinglist(deviceSiteCode,equipmentName);
		for(EquipmentDeviceSiteMapping eq:equipment){
			Equipment e = eq.getEquipment();
			if(e.getPic()!=null) {
				String dir = request.getServletContext().getRealPath("/");
				InputStream is;
				try {
					is = e.getPic().getBinaryStream();
					File out = new File(dir,e.getPicName());
					FileCopyUtils.copy(is, new FileOutputStream(out));
					e.setPicName(e.getPicName());
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
			
		return equipment;

	}
	/**
	 * 根据设备站点代码查找设备站点id
	 */
	@RequestMapping("/findDeviceSiteId.do")
	@ResponseBody
	public ModelMap findDeviceSiteId(@RequestParam(value="deviceSiteCode")String deviceSiteCode){
		ModelMap modelMap = new ModelMap();
		Long ds = IMCEService.findDeviceSite(deviceSiteCode);
		if(ds!=null) {
			modelMap.addAttribute("id",ds);
			return modelMap;
		}
		return null;
	}
	/**
	 * MC添加装备关联记录
	 * @return
	 */
	@RequestMapping("/mcaddEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap mcaddEquipmentMappingRecord(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryObjById(equipmentDeviceSiteMapping.getDeviceSite().getId());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}
		equipmentDeviceSiteMapping.setDeviceSite(ds);
		Equipment eq=equipmentService.queryObjById(equipmentDeviceSiteMapping.getEquipment().getId());
		equipmentDeviceSiteMapping.setDeviceSite(ds);
		equipmentDeviceSiteMapping.setEquipment(eq);
		//判断该装备是否已经被关联到设备上
		EquipmentDeviceSiteMapping edm = equipmentDeviceSiteMappingService.queryByEquipmentCode(equipmentDeviceSiteMapping.getEquipment().getCode());
		if(edm!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该装备已被关联到 " + edm.getDeviceSite().getCode());
			return modelMap;
		}
		
		//辅助人设置
				//User hUser = userService.queryUserByEmployeeCode(equipmentDeviceSiteMapping.getHelperId());
		Employee emp = employeeService.queryEmployeeByCode(equipmentDeviceSiteMapping.getHelperId());
				if(null!=emp) {
					equipmentDeviceSiteMapping.setHelperId(emp.getCode());
					equipmentDeviceSiteMapping.setHelperName(emp.getName());
				}
		
		if(equipmentDeviceSiteMapping.getMappingDate()==null) {
			equipmentDeviceSiteMapping.setMappingDate(new Date());
		}

		//获取当前登录用户
		MCUser mcUser  = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		if(mcUser == null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请登录!");
			return modelMap;
		}
		
		User user = userService.queryUserByUsername(mcUser.getUsername());
		//获取当前登录用户
		if(user!=null) {
			equipmentDeviceSiteMapping.setBindUserId(user.getId());
			equipmentDeviceSiteMapping.setBindUsername(user.getUsername());
		}
		equipmentDeviceSiteMappingService.addObj(equipmentDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}


	/**
	 * MC工单界面添加装备关联记录
	 * @return
	 */
	@RequestMapping("/mcaddEquipmentsMappingRecordInWorkSheet.do")
	@ResponseBody
	public ModelMap mcaddEquipmentsMappingRecordInWorkSheet(String equipmentNos,String deviceSiteCode,String helperId,String usageRates,HttpServletRequest request,String workSheetNo){
		ModelMap modelMap=new ModelMap();
		try {
			equipmentDeviceSiteMappingService.addMCEquipmentsMappingRecordInWorkSheet(equipmentNos,deviceSiteCode,helperId,usageRates,request,workSheetNo);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","关联成功");
		}catch (RuntimeException e){
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg",e.getMessage());
			return modelMap;
		}
		return modelMap;
	}





	/**
	 * MC编辑装备关联记录
	 * @return
	 */
	@RequestMapping("/mcupdateEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap MCupdateEquipmentMappingRecord(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping) {
		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping plr = equipmentDeviceSiteMappingService.queryObjById(equipmentDeviceSiteMapping.getId());
		plr.setEquipment(equipmentDeviceSiteMapping.getEquipment());
		plr.setDeviceSite(equipmentDeviceSiteMapping.getDeviceSite());
		
		//辅助人设置
		Employee emp = employeeService.queryEmployeeByCode(equipmentDeviceSiteMapping.getHelperId());
		if(null!=emp) {
			equipmentDeviceSiteMapping.setHelperId(emp.getCode());
			equipmentDeviceSiteMapping.setHelperName(emp.getName());
		}
		plr.setHelperId(equipmentDeviceSiteMapping.getHelperId());
		plr.setHelperName(equipmentDeviceSiteMapping.getHelperName());
			
		plr.setUsageRate(equipmentDeviceSiteMapping.getUsageRate());
		equipmentDeviceSiteMappingService.updateObj(plr);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	
	/**
	 * 解除装备关联
	 * @param id
	 * @return
	 */
	@RequestMapping("/mcUnbindEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap unbindEquipmentMappingRecord(String id,HttpServletRequest request) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		//获取当前登录用户
		/*HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		User user = userService.queryByProperty("username", mcUser.getUsername());

		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping equipmentDeviceSiteMapping = equipmentDeviceSiteMappingService.queryObjById(Long.valueOf(id));
		if(user!=null) {
			equipmentDeviceSiteMapping.setUnbindUserId(user.getId());
			equipmentDeviceSiteMapping.setUnbindUsername(user.getUsername());
		}
		equipmentDeviceSiteMapping.setUnbind(true);
		equipmentDeviceSiteMapping.setUnbindDate(new Date());
		equipmentDeviceSiteMappingService.updateObj(equipmentDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}

	/**
	 * 替换量具关联
	 * @return
	 */
	@RequestMapping("/mcReplaceEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap mcReplaceEquipmentMappingRecord(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();

		//辅助人设置
		Employee emp = employeeService.queryEmployeeByCode(equipmentDeviceSiteMapping.getHelperId());
		if(null!=emp) {
			equipmentDeviceSiteMapping.setHelperId(emp.getCode());
			equipmentDeviceSiteMapping.setHelperName(emp.getName());
		}

		MCUser mcUser  = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		if(mcUser == null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请登录!");
			return modelMap;
		}

		User user = userService.queryUserByUsername(mcUser.getUsername());
		//获取当前登录用户
		if(user!=null) {
			equipmentDeviceSiteMapping.setBindUserId(user.getId());
			equipmentDeviceSiteMapping.setBindUsername(user.getUsername());
		}
		DeviceSite ds =deviceSiteService.queryByProperty("code", equipmentDeviceSiteMapping.getDeviceSite().getCode());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}
		equipmentDeviceSiteMapping.setDeviceSite(ds);
		//判断该量具是否已经被关联到设备上
		EquipmentDeviceSiteMapping edm = equipmentDeviceSiteMappingService.queryByEquipmentCode(equipmentDeviceSiteMapping.getEquipment().getCode());
		if(edm!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该量具已被关联到 " + edm.getDeviceSite().getCode() + "上");
			return modelMap;
		}

		Equipment equ = equipmentService.queryObjById(equipmentDeviceSiteMapping.getEquipment().getId());
		if(equ==null){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择量具站点");
			return modelMap;
		}
		equipmentDeviceSiteMapping.setEquipment(equ);
		if(equipmentDeviceSiteMapping.getMappingDate()==null) {
			equipmentDeviceSiteMapping.setMappingDate(new Date());
		}
		equipmentDeviceSiteMappingService.deleteObj(equipmentDeviceSiteMapping.getId());
		equipmentDeviceSiteMapping.setId(null);
		equipmentDeviceSiteMappingService.addObj(equipmentDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "替换成功!");
		return modelMap;
	}
	/**
	 * 根据id查询参数
	 * @param q
	 * @return
	 */
	@RequestMapping("/queryEquipmentByCode.do")
	@ResponseBody
	public List<Equipment> queryEquipmentByCode(String q) {
		List<Equipment> list = equipmentService.queryEquipmentsByCode(q);
		return list;
	}

	/**
	 * 根据装备序号查询该装备是否已经被关联
	 * @param equipmentNo
	 * @return
	 */
	@RequestMapping("/queryEquipmentIsMappingByCode.do")
	@ResponseBody
	public ModelMap queryEquipmentIsMappingByCode(String equipmentNo){
		ModelMap modelMap=new ModelMap();
		List<Equipment> list = equipmentService.queryEquipmentsByCode(equipmentNo);
		if(list!=null&&list.size()>0){
			EquipmentDeviceSiteMapping edm=equipmentDeviceSiteMappingService.queryByEquipmentCode(equipmentNo);
			if(edm!=null){
				modelMap.addAttribute("success",false);
				modelMap.addAttribute("msg","该装备已经被设备站点"+edm.getDeviceSite().getName()+"关联!");
				return modelMap;
			}else{
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("equipment",list.get(0));
			}
		}else{
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","没有此种装备");
		}
		return modelMap;
	}

}
