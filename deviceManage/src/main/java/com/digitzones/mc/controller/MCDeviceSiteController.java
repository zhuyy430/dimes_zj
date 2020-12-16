package com.digitzones.mc.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.mc.model.MCDeviceSite;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCDeviceSiteService;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.Classes;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.WorkSheetDetail;
import com.digitzones.service.IClassesService;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.ILostTimeRecordService;
import com.digitzones.service.IProcessRecordService;
import com.digitzones.service.IWorkSheetDetailService;

@Controller
@RequestMapping("/mcdeviceSite")
public class MCDeviceSiteController {
	@Autowired
	private IMCDeviceSiteService mcDeviceSiteService;
	@Autowired
	private IWorkSheetDetailService workSheetDetailService;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private ILostTimeRecordService lostTimeRecordService;
	@Autowired
	private IProcessRecordService processRecordService;
	@Autowired
	private IMCUserService mcUserService;
	/**
	 * 获取关联设备站点信息
	 * @return
	 */
	@RequestMapping("/getAllMCDeviceSite.do")
	@ResponseBody
	public List<MCDeviceSite> getAllMCDeviceSite(HttpServletRequest request) {
		String clientIp = request.getRemoteAddr();
		return mcDeviceSiteService.getAllMCDeviceSite(clientIp);
	}
	/**
	 * 获取未关联的设备站点
	 * 
	 * @return
	 */
	@RequestMapping("/getAllDeviceSite.do")
	@ResponseBody
	public List<MCDeviceSite> getAllDeviceSite(HttpServletRequest request,String deviceName,String deviceCode,String productionUnitCode) {
		return mcDeviceSiteService.queryAvailableDeviceSitesByCondition(request.getRemoteAddr(), deviceName, deviceCode, productionUnitCode);
	}

	@RequestMapping("/addMCDeviceSites.do")
	@ResponseBody
	public ModelMap addDeviceSites(String objs,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		String clientIp = request.getRemoteAddr();
		List<MCDeviceSite> list = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(objs);
		for(int i = 0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			MCDeviceSite mc = new MCDeviceSite();
			mc.setClientIp(clientIp);
			mc.setDeviceCode(obj.getString("deviceCode"));
			mc.setDeviceName(obj.getString("deviceName"));
			mc.setDeviceSiteCode(obj.getString("deviceSiteCode"));
			mc.setDeviceSiteName(obj.getString("deviceSiteName"));
			mc.setBottleneck(obj.getBoolean("bottleneck"));

			list.add(mc);
		}

		mcDeviceSiteService.addMCDeviceSites(list);
		return modelMap;
	}

	/**
	 * 删除设备站点
	 * @return
	 */
	@RequestMapping("/deleteMCDeviceSites.do")
	@ResponseBody
	public ModelMap deleteMCDeviceSites(@RequestParam("mcdsids")String mcdsids){
		ModelMap modelMap = new ModelMap();
		List<Long> list = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(mcdsids);
		for(int i = 0;i<jsonArray.size();i++) {
			Long id = jsonArray.getLong(i);
			list.add(id);
		}
		mcDeviceSiteService.deleteMCDeviceSite(list);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg","删除成功!");
		modelMap.addAttribute("title","删除提示 !");
		return modelMap; 
	}
	/***
	 * 根据该设备站点上正在加工的工单信息
	 * @return
	 */
	@RequestMapping("/queryProcessingWorkSheetDetailByDeviceSiteCode.do")
	@ResponseBody
	public WorkSheetDetail queryProcessingWorkSheetDetailByDeviceSiteCode(String deviceSiteCode) {
		WorkSheetDetail detail = workSheetDetailService.queryProcessingWorkSheetDetailByDeviceSiteCode(deviceSiteCode);
		if(detail == null) {
			detail = new WorkSheetDetail();
			DeviceSite ds = deviceSiteService.queryByProperty("code",deviceSiteCode);
			if(ds!=null) {
				if(ds.getDevice()!=null) {
					detail.setDeviceCode(ds.getDevice().getCode());
					detail.setDeviceName(ds.getDevice().getName());
				}
				detail.setDeviceSiteCode(ds.getCode());
				detail.setDeviceSiteName(ds.getName());
				detail.setDeviceSiteId(ds.getId());
			}
		}
		return detail;
	}

	/***
	 * 根据该设备站点获取班次信息
	 * @return
	 */
	@RequestMapping("/queryClassesByDeviceSiteCode.do")
	@ResponseBody
	public Classes queryClassesByDeviceSiteCode(String deviceSiteCode) {
		return classesService.queryClassesByDeviceSiteCode(deviceSiteCode);
		
	}
	/**
	 * 设备运行时间表
	 * @return
	 */
	@RequestMapping("/queryDeviceRunningTimes.do")
	@ResponseBody
	public ModelMap queryDeviceRunningTimes(String deviceSiteCode) {
		if(deviceSiteCode==null) {
			return null;
		}
		ModelMap modelMap = new ModelMap();
		//存放x轴的文本信息
		List<String> titles = new ArrayList<>();
		//存放分钟数
		List<Integer> minutes = new ArrayList<>();
		//存放辅助数
		List<Integer> assistants = new ArrayList<>();
		//查找当前班次的总时长
		Classes c = classesService.queryCurrentClasses();
		if(c==null) {
			return modelMap;
		}
/*		//短停机时长
		int shortHalt = processRecordService.queryShortHalt(c,deviceSiteCode);*/
		//计算总时长
		int total = computeTotalRunningMinutes(titles, minutes, assistants,c);
		//计算正常运行时长
		//查询计划停机时间，单位：分钟
		int totalLostTime = (lostTimeRecordService.queryLostTime4Classes(c,deviceSiteCode)).intValue();
		titles.add("运行时长");
		minutes.add(total - totalLostTime);
		assistants.add(totalLostTime );
/*
		minutes.add(total - totalLostTime-shortHalt/60);
		assistants.add(totalLostTime + shortHalt/60);
*/
		/*titles.add("短停机");
		minutes.add(shortHalt/60);
		assistants.add(totalLostTime);*/

		//停机总时长
		titles.add("停机时长");
		minutes.add(totalLostTime);
		assistants.add(0); 

		//各个损时类别停机时长
		List<Object[]> objs = lostTimeRecordService.queryLostTimePerLosttimeType(c,deviceSiteCode);
		int temp = totalLostTime;
		for(Object[] o : objs) {
			String typename = (o[0]==null||"".equals(o[0]))?"未处理":o[0].toString();
			int m =(o[1] == null?0:(int)o[1]);
			titles.add(typename);
			minutes.add(m);
			temp -=m;
			assistants.add(temp);
		}

		modelMap.addAttribute("titles",titles);
		modelMap.addAttribute("minutes",minutes);
		modelMap.addAttribute("assistants",assistants);
		return modelMap;
	}
	/**
	 * 生产单元设备运行时间表
	 * @return
	 */
	@RequestMapping("/queryProductionUnitRunningTimes.do")
	@ResponseBody
	public ModelMap queryProductionUnitRunningTimes(Long productionUnitId) {
		ModelMap modelMap = new ModelMap();
		//存放x轴的文本信息
		List<String> titles = new ArrayList<>();
		//存放分钟数
		List<Integer> minutes = new ArrayList<>();
		//存放辅助数
		List<Integer> assistants = new ArrayList<>();
		//查找当前班次的总时长
		Classes c = classesService.queryCurrentClasses();
		if(c==null) {
			return modelMap;
		}
		//查找生产单元下的所有设备站点
		List<DeviceSite> deviceSiteList = deviceSiteService.queryDeviceSitesByProductionUnitId(productionUnitId);
		if(CollectionUtils.isEmpty(deviceSiteList)) {
			return modelMap;
		}
		//短停机时长
		//int shortHalt = 0;
		//计算正常运行时长
		//查询计划停机时间，单位：分钟
		int totalLostTime =0;
		//计算总时长
		int total = computeTotalRunningMinutes(titles, minutes, assistants,c)*deviceSiteList.size();
		titles.add("总时长");
		minutes.add(total);
		assistants.add(0);
		for(DeviceSite deviceSite : deviceSiteList) {
			//shortHalt += processRecordService.queryShortHalt(c,deviceSite.getCode());
			//查询计划停机时间，单位：分钟
			totalLostTime += (lostTimeRecordService.queryLostTime4Classes(c,deviceSite.getCode())).intValue();
		}

		titles.add("运行时长");
		minutes.add(total - totalLostTime);
		assistants.add(totalLostTime);
/*
		minutes.add(total - totalLostTime-shortHalt/60);
		assistants.add(totalLostTime + shortHalt/60);
*/

		/*titles.add("短停机");
		minutes.add(shortHalt/60);
		assistants.add(totalLostTime);*/


		//停机总时长
		titles.add("停机时长");
		minutes.add(totalLostTime);
		assistants.add(0); 

		modelMap.addAttribute("titles",titles);
		modelMap.addAttribute("minutes",minutes);
		modelMap.addAttribute("assistants",assistants);
		return modelMap;
	}
	/**
	 * 计算总时长
	 * @param titles
	 * @param minutes
	 * @param assistants
	 */
	private int computeTotalRunningMinutes(List<String> titles,List<Integer> minutes,List<Integer> assistants,Classes c) {
		Date now = new Date();
		Date startTime = c.getStartTime();
		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(now);
		Calendar calendarStartTime = Calendar.getInstance();
		calendarStartTime.setTime(startTime);
		calendarNow.set(Calendar.HOUR_OF_DAY, calendarStartTime.get(Calendar.HOUR_OF_DAY));
		calendarNow.set(Calendar.MINUTE,calendarStartTime.get(Calendar.MINUTE));
		Date startDate = calendarNow.getTime();
		int total = 0;
		//跨天  计算总时长
		if(startDate.getTime()>now.getTime()) {
			total = (int)((now.getTime()+24*60*60*1000)-startDate.getTime())/1000/60;
		}else {
			total = (int)(now.getTime()-startDate.getTime())/1000/60;
		}
		return total;
	}
	/**
	 * 查询员工在岗状态
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryEmployeeStatus.do")
	@ResponseBody
	public ModelMap queryEmployeeStatus(HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		List<DeviceSite> deviceSiteList = deviceSiteService.queryAllDeviceSites();

		List<Object[]> mcDeviceSites = new ArrayList<>();
		//查询所有mc端登录的用户
		List<MCUser> userList = mcUserService.queryAllLoginUser();
		for(MCUser user: userList) {
			Object[] mcDevice_mcUser = new Object[2];
			List<MCDeviceSite> list = mcDeviceSiteService.getAllMCDeviceSite(user.getClientIp());
			mcDevice_mcUser[0] = user;
			mcDevice_mcUser[1] = list;
			mcDeviceSites.add(mcDevice_mcUser);
		}
		modelMap.addAttribute("deviceSites", deviceSiteList);
		modelMap.addAttribute("mcDeviceSites", mcDeviceSites);
		return modelMap;
	}
}
