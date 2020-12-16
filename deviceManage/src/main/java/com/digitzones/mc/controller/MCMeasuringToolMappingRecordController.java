package com.digitzones.mc.controller;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.service.*;
import com.digitzones.vo.EquipmentDeviceSiteMappingVO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 设备站点和量具关联记录
 * @author zdq
 * 2018年6月28日
 */
@Controller
@RequestMapping("/mcMeasuringToolMappingRecord")
public class MCMeasuringToolMappingRecordController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private IMeasuringToolDeviceSiteMappingService  measuringToolDeviceSiteMappingService;
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEquipmentService equipmentService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IMeasuringToolService measuringToolService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	@Autowired
	public void setMeasuringToolDeviceSiteMappingService(IMeasuringToolDeviceSiteMappingService measuringToolDeviceSiteMappingService) {
		this.measuringToolDeviceSiteMappingService = measuringToolDeviceSiteMappingService;
	}


	/**
	 * 添加量具关联记录
	 * @return
	 */
	@RequestMapping("/mcAddMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap addMeasuringToolMappingRecord(EquipmentDeviceSiteMapping measuringToolDeviceSiteMapping,String deviceSiteCode,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = new DeviceSite();
		if(null!=deviceSiteCode&&!"".equals(deviceSiteCode)){
			ds = deviceSiteService.queryByProperty("code", deviceSiteCode);
			if(ds==null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "请选择设备站点");
				return modelMap;
			}
		}else{
			ds = deviceSiteService.queryObjById(measuringToolDeviceSiteMapping.getDeviceSite().getId());
			if(ds==null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "请选择设备站点");
				return modelMap;
			}
		}

		//判断该量具是否已经被关联到设备上

		EquipmentDeviceSiteMapping edm = measuringToolDeviceSiteMappingService.queryByEquipmentCode(measuringToolDeviceSiteMapping.getEquipment().getCode());
		if(edm!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该量具已被关联到 " + edm.getDeviceSite().getCode() + "上");
			return modelMap;
		}


		//辅助人设置
		Employee emp = employeeService.queryEmployeeByCode(measuringToolDeviceSiteMapping.getHelperId());
		if(null!=emp) {
			measuringToolDeviceSiteMapping.setHelperId(emp.getCode());
			measuringToolDeviceSiteMapping.setHelperName(emp.getName());
		}
		
		if(measuringToolDeviceSiteMapping.getMappingDate()==null) {
			measuringToolDeviceSiteMapping.setMappingDate(new Date());
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
			measuringToolDeviceSiteMapping.setBindUserId(user.getId());
			measuringToolDeviceSiteMapping.setBindUsername(user.getUsername());
		}
		measuringToolDeviceSiteMappingService.addObj(measuringToolDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询量具关联记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/mcQueryMeasuringToolMappingRecordById.do")
	@ResponseBody
	public EquipmentDeviceSiteMappingVO queryMeasuringToolMappingRecordById(Long id) {
		EquipmentDeviceSiteMapping plr = measuringToolDeviceSiteMappingService.queryObjById(id);
		
		return model2VO(plr);
	}
	/**
	 * 将领域模型转换为值对象模型
	 * @param plr
	 * @return
	 */
	private EquipmentDeviceSiteMappingVO model2VO(EquipmentDeviceSiteMapping plr) {
		if(plr == null) {
			return null;
		}
		EquipmentDeviceSiteMappingVO vo = new EquipmentDeviceSiteMappingVO();
		vo.setId(plr.getId());
		vo.setBindUsername(plr.getBindUsername());
		vo.setBindUserId(plr.getBindUserId());
		vo.setDeviceSite(plr.getDeviceSite());
		vo.setEquipment(plr.getEquipment());
		if(plr.getEquipment()!=null) {
			vo.setEquipmentId(plr.getEquipment().getId());
		}
		vo.setHelperId(plr.getHelperId());
		vo.setHelperName(plr.getHelperName());
		vo.setWorkSheetCode(plr.getWorkSheetCode());
		if(plr.getMappingDate()!=null) {
			vo.setMappingDate(sdf.format(plr.getMappingDate()));
		}
		vo.setUsageRate(plr.getUsageRate());
		return vo;
	}

	/**
	 * MC编辑量具关联记录
	 * @return
	 */
	@RequestMapping("/mcUpdateMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap mcUpdateMeasuringToolMappingRecord(EquipmentDeviceSiteMapping measuringToolDeviceSiteMapping) {
		ModelMap modelMap = new ModelMap();
		Long id = measuringToolDeviceSiteMapping.getId();
		String dsCode = measuringToolDeviceSiteMapping.getDeviceSite().getCode();
		Long equId = measuringToolDeviceSiteMapping.getEquipment().getId();
		String no = measuringToolDeviceSiteMapping.getEquipment().getCode();
		Float usageRate = measuringToolDeviceSiteMapping.getUsageRate();
		String helpId = measuringToolDeviceSiteMapping.getHelperId();
				
		//User user = new User();
		EquipmentDeviceSiteMapping measuringTool = measuringToolDeviceSiteMappingService.queryObjById(id);
		//辅助人设置
		Employee emp = employeeService.queryEmployeeByCode(helpId);
		if(null!=emp) {
			measuringTool.setHelperId(emp.getCode());
			measuringTool.setHelperName(emp.getName());
		}
		
		if(equId.intValue()==measuringTool.getEquipment().getId().intValue()){
			Equipment equipment = equipmentService.queryObjById(equId);
			measuringTool.setEquipment(equipment);
		}else{
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "量具未关联到该站点!");
			return modelMap;
		}
		/*if(!measuringTool.getEquipment().getCode().equals(no)){
			EquipmentDeviceSiteMapping edm = measuringToolDeviceSiteMappingService.queryByNo(no);
			if(edm!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "该量具已被关联到 " + edm.getDeviceSite().getCode() + "上");
				return modelMap;
			}
		}*/
		measuringTool.setUsageRate(usageRate);
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", dsCode);
		measuringTool.setDeviceSite(deviceSite);
		
		measuringToolDeviceSiteMappingService.updateObj(measuringTool);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 解除量具关联
	 * @param id
	 * @return
	 */
	@RequestMapping("/mcUnbindMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap unbindMeasuringToolMappingRecord(String id,HttpServletRequest request) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		//获取当前登录用户
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		
		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping measuringToolDeviceSiteMapping = measuringToolDeviceSiteMappingService.queryObjById(Long.valueOf(id));
		if(mcUser!=null) {
			measuringToolDeviceSiteMapping.setUnbindUserId(mcUser.getId());
			measuringToolDeviceSiteMapping.setUnbindUsername(mcUser.getUsername());
		}
		measuringToolDeviceSiteMapping.setUnbind(true);
		measuringToolDeviceSiteMapping.setUnbindDate(new Date());
		measuringToolDeviceSiteMappingService.updateObj(measuringToolDeviceSiteMapping);
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
	@RequestMapping("/mcReplaceMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap replaceMeasuringToolMappingRecord(EquipmentDeviceSiteMapping newMeasuringToolDeviceSiteMapping,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		
		//辅助人设置
		Employee emp = employeeService.queryEmployeeByCode(newMeasuringToolDeviceSiteMapping.getHelperId());
		if(null!=emp) {
			newMeasuringToolDeviceSiteMapping.setHelperId(emp.getCode());
			newMeasuringToolDeviceSiteMapping.setHelperName(emp.getName());
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
			newMeasuringToolDeviceSiteMapping.setBindUserId(user.getId());
			newMeasuringToolDeviceSiteMapping.setBindUsername(user.getUsername());
		}
		DeviceSite ds =deviceSiteService.queryByProperty("code", newMeasuringToolDeviceSiteMapping.getDeviceSite().getCode());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}
		newMeasuringToolDeviceSiteMapping.setDeviceSite(ds);
		//判断该量具是否已经被关联到设备上
		EquipmentDeviceSiteMapping edm = measuringToolDeviceSiteMappingService.queryByEquipmentCode(newMeasuringToolDeviceSiteMapping.getEquipment().getCode());
		if(edm!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该量具已被关联到 " + edm.getDeviceSite().getCode() + "上");
			return modelMap;
		}

		Equipment equ = equipmentService.queryObjById(newMeasuringToolDeviceSiteMapping.getEquipment().getId());
		if(equ==null){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择量具站点");
			return modelMap;
		}
		newMeasuringToolDeviceSiteMapping.setEquipment(equ);
		if(newMeasuringToolDeviceSiteMapping.getMappingDate()==null) {
			newMeasuringToolDeviceSiteMapping.setMappingDate(new Date());
		}
		measuringToolDeviceSiteMappingService.deleteObj(newMeasuringToolDeviceSiteMapping.getId());
		newMeasuringToolDeviceSiteMapping.setId(null);
		measuringToolDeviceSiteMappingService.addObj(newMeasuringToolDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "替换成功!");
		return modelMap;
	}
	/**
	 * 根据设备站点代码查找关联的量具
	 * @return
	 */
	@RequestMapping("/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do")
	@ResponseBody
	public List<EquipmentDeviceSiteMapping> queryMeasuringToolMappingRecordByDeviceSiteCode(@RequestParam(value="deviceSiteCode") String deviceSiteCode,@RequestParam(value="searchText") String searchText,HttpServletRequest request) {
		List<EquipmentDeviceSiteMapping> equList = measuringToolDeviceSiteMappingService.queryByDeviceSiteCode(deviceSiteCode,searchText);
		for(EquipmentDeviceSiteMapping eq:equList){
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
		return equList;
	}
	/**
	 * 查询所有的装备
	 * @return
	 */
	@RequestMapping("/mcQueryAllMeasuringTools.do")
	@ResponseBody
	public Equipment mcQueryAllMeasuringTools(String q){
		List<Equipment> list = new ArrayList<>();
		if(q==null) {
			list = measuringToolService.queryAllMeasuringTools();
			if(!list.isEmpty()&&list.size()>0){
				return list.get(0);
			}
		}else {
			list = measuringToolService.queryMeasuringToolsByCodeOrNameOrUnity(q);
			if(!list.isEmpty()&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 查询所有的装备
	 * @return
	 */
	@RequestMapping("/mcQueryMeasuringToolsByCode.do")
	@ResponseBody
	public Equipment mcQueryMeasuringToolsByCode(String q){
		List<Equipment> list = measuringToolService.queryMeasuringToolsByCode(q);
		if(!list.isEmpty()&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
} 
