package com.digitzones.mc.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCLostTimeService;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/mcLostTime")
public class MCLostTimeController {
	@Autowired
	private IMCLostTimeService MCLostTimeService;
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IPressLightTypeService pressLightTypeService;
	@Autowired
	private IPressLightService pressLightService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private ILostTimeRecordService lostTimeRecordService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 根据xxx查找损时记录
	 * @throws ParseException 
	 */
	@RequestMapping("/findLostTimeList.do")
	@ResponseBody
	public List<LostTimeRecord> findLostTimeList(@RequestParam(value="deviceSiteCode")String deviceSiteCode,
			String startTime,String endTime,String NGCode,String status) throws ParseException{
		
		Date now = new Date();
		if(!StringUtils.isEmpty(endTime)){
			now=format.parse(endTime+" 23:59:59");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		c.add(Calendar.DATE, -10);
		Date start = c.getTime();
		if(!StringUtils.isEmpty(startTime)){
			start=format.parse(startTime+" 00:00:00");
		}
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.add(Calendar.DATE, 10);
		Date end = c.getTime();
		if(!StringUtils.isEmpty(endTime)){
			end=format.parse(endTime+" 23:59:59");
		}
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
		String hql="from LostTimeRecord ltr where ltr.deleted=false and ltr.deviceSite.id=?0 and ltr.lostTimeTime between ?1 and ?2";
		List<Object> param = new ArrayList<>();
		param.add(deviceSite.getId());
		param.add(start);
		param.add(end);
		
		int i =3;
		if(!StringUtils.isEmpty(NGCode)) {
			hql += " and ltr.lostTimeTypeCode like ?" + i++;
			param.add("%" + NGCode + "%");
		}
		if(!StringUtils.isEmpty(status)) {
			if(status.equals("untreated")){
				hql += " and (ltr.pressLightCode is null or ltr.pressLightCode='')";
			}else if(status.equals("notSure")){
				hql += " and ((ltr.pressLightCode is not null and ltr.pressLightCode!='') and ltr.confirmUserId is null)";
			}else if(status.equals("historicalRecords")){
				hql += " and ltr.confirmUserId is not null";
			}
		}
		hql +=" order by ltr.lostTimeTime desc";
		List<LostTimeRecord> lostTimeRecord = MCLostTimeService.findLostTimelist(hql,param);
		for(LostTimeRecord ltr:lostTimeRecord){
			long beginTime = ltr.getBeginTime().getTime();
			long endDate ;
			if(null==ltr.getEndTime()){
				endDate = new Date().getTime();
			}else{
				endDate = ltr.getEndTime().getTime();
			}
			String sumTime = new DecimalFormat("#####.00").format((double)(endDate-beginTime)/60/1000);
			double sumLostTime = Double.parseDouble(sumTime);
			ltr.setSumOfLostTime(sumLostTime);
		}
		return lostTimeRecord;
	}
	
	/**
	 * 根据xxx查找损时总时长
	 */
	@RequestMapping("/getAllLostTime.do")
	@ResponseBody
	public Double findAllLostTimeAndCount(@RequestParam(value="untreated")String untreated,
			@RequestParam(value="notSure")String notSure,
			@RequestParam(value="historicalRecords")String historicalRecords,
			@RequestParam(value="deviceSiteCode")String deviceSiteCode){
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
		Double sum = MCLostTimeService.getAllLostTimelist( untreated,notSure,historicalRecords,deviceSite.getId());
		//count= MCLostTimeService.getCountLostTimelist( untreated,notSure,historicalRecords,deviceSite.getId());
		return sum;
	}
	/**
	 * 根据xxx查找损时记录数量
	 */
	@RequestMapping("/getCountLostTime.do")
	@ResponseBody
	public int getCountLostTime(@RequestParam(value="untreated")String untreated,
			@RequestParam(value="notSure")String notSure,
			@RequestParam(value="historicalRecords")String historicalRecords,
			@RequestParam(value="deviceSiteCode")String deviceSiteCode){
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
		int sum = MCLostTimeService.getCountLostTimelist( untreated,notSure,historicalRecords,deviceSite.getId());
		//count= MCLostTimeService.getCountLostTimelist( untreated,notSure,historicalRecords,deviceSite.getId());
		return sum;
	}

	/**
	 * 新增损时记录
	 * @param lostTimeRecord
	 * @return
	 */
	@RequestMapping("/mcAddLostTimeList.do")
	@ResponseBody
	public ModelMap addLostTimeList(LostTimeRecord lostTimeRecord,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryObjById(lostTimeRecord.getDeviceSite().getId());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}

		/*if(lostTimeRecord.getBeginTime()==null) {
			lostTimeRecord.setBeginTime(new Date());
		}

		if(lostTimeRecord.getEndTime()!=null) {
			long endTimeMilliSec = lostTimeRecord.getEndTime().getTime();
			long beginTimeMilliSec = lostTimeRecord.getBeginTime().getTime();
			long result = endTimeMilliSec - beginTimeMilliSec;
			lostTimeRecord.setSumOfLostTime(result/1000*1.0);
		}*/
		lostTimeRecord.setLostTimeTime(new Date());
		lostTimeRecord.setSumOfLostTime(lostTimeRecord.getSumOfLostTime()/60);
		//查找当前班次
		DeviceSite deviceSite=deviceSiteService.queryObjById(lostTimeRecord.getDeviceSite().getId());
		Classes c = classesService.queryCurrentClassesByClassesTypeName(deviceSite.getDevice().getProductionUnit().getClassTypeName());
		if(c!=null) {
			lostTimeRecord.setClassesCode(c.getCode());
			lostTimeRecord.setClassesName(c.getName());
		}
		lostTimeRecord.setForLostTimeRecordDate(yyyyMMdd.format(new Date()));
		if(null!=lostTimeRecord.getPressLightCode()&&!lostTimeRecord.getPressLightCode().equals("")){
			PressLight pl = pressLightService.queryByProperty("code", lostTimeRecord.getPressLightCode());
			lostTimeRecord.setDescription(pl.getDescription());
		}
		String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		User user = userService.queryUserByUsername(mcUser.getUsername());
		Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
		user.setUsername(employee.getName());

		//try {
			Map<String,Object> args = new HashMap<>();
			args.put("businessKey", workFlowKeyConfig.getLostTimeWorkflowKey());
			Serializable id = lostTimeRecordService.addLostTimeRecord(lostTimeRecord,user,args);
			//执行损时申请
			/*TaskService taskService = processEngine.getTaskService();
			Map<String,Object> variables = new HashMap<>();
			variables.put(Constant.Workflow.LOSTTIME_CONFIRM_ROLES, "损时确认人");
			variables.put(Constant.Workflow.LOSTTIME_CONFIRM_PERSON, "");
			variables.put(Constant.Workflow.LOSTTIME_CONFIRM_PERSONS, "");
			Task task = taskService.createTaskQuery().processInstanceBusinessKey(id+":" + LostTimeRecord.class.getName()).taskAssignee(user.getUsername()).singleResult();
			taskService.complete(task.getId(),variables);
		}catch(ActivitiObjectNotFoundException e) {
			e.printStackTrace();
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "启动流程失败:启动流程的key不存在！");
			return modelMap;
		}catch(Exception e){
			e.printStackTrace();
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "数据出现错误，添加失败！");
			return modelMap;
		}*/
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}

	/**
	 * 修改损时记录
	 * @param lostTimeRecord
	 * @return
	 */
	@RequestMapping("/mcUpdateLostTimeList.do")
	@ResponseBody
	public ModelMap updateLostTimeList(LostTimeRecord lostTimeRecord){
		ModelMap modelMap = new ModelMap();
		LostTimeRecord lostTime = lostTimeRecordService.queryObjById(lostTimeRecord.getId());
		if(null!=lostTimeRecord.getLostTimeTypeCode()&&lostTimeRecord.getLostTimeTypeCode()!=""&&null!=lostTimeRecord.getReason()&&lostTimeRecord.getReason()!=""){
			PressLightType type = pressLightTypeService.queryByProperty("code", lostTimeRecord.getLostTimeTypeCode());
			PressLight reasonType = pressLightService.queryByProperty("code", lostTimeRecord.getPressLightCode());
			lostTime.setLostTimeTypeName(type.getName());
			lostTime.setLostTimeTypeCode(type.getCode());
			lostTime.setReason(reasonType.getReason());
			lostTime.setDescription(reasonType.getDescription());
			lostTime.setPressLightCode(reasonType.getCode());
		}
		if(lostTime.getConfirmUserId()==null){
			/*if(lostTimeRecord.getEndTime()!=null) {
				long endTimeMilliSec = lostTimeRecord.getEndTime().getTime();
				long beginTimeMilliSec = lostTimeRecord.getBeginTime().getTime();
				long result = endTimeMilliSec - beginTimeMilliSec;
				lostTime.setSumOfLostTime(result/60/1000*1.0);
			}*/
			lostTime.setSumOfLostTime(lostTimeRecord.getSumOfLostTime());
			lostTime.setBeginTime(lostTimeRecord.getBeginTime());
			lostTime.setEndTime(lostTimeRecord.getEndTime());
			lostTimeRecordService.updateObj(lostTime);

			modelMap.addAttribute("success", true);
			modelMap.addAttribute("message", "修改成功！");
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("message", "该记录已确认无法修改！");
		return modelMap;
	}
	/**
	 * 多条数据修改损时记录
	 * @return
	 */
	@RequestMapping("/mcupdateLostTimeByIds.do")
	@ResponseBody
	public ModelMap updateLostTimeByIds(String ids,String reasonCode){
		ModelMap modelMap = new ModelMap();
		String[] data = ids.split(",");
		for(String id:data){
			LostTimeRecord lostTime = lostTimeRecordService.queryObjById(Long.parseLong(id));
			PressLight reasonType = pressLightService.queryByProperty("code", reasonCode);
			lostTime.setReason(reasonType.getReason());
			lostTime.setDescription(reasonType.getDescription());
			lostTime.setPressLightCode(reasonType.getCode());
//			PressLightType type = pressLightTypeService.queryByProperty("code", reasonType.getPressLightType());
			PressLightType type = reasonType.getPressLightType();
			if(type.getLevel()==1){
				lostTime.setLostTimeTypeName(type.getName());
				lostTime.setLostTimeTypeCode(type.getCode());
			}else{
				PressLightType BasicType = pressLightTypeService.queryByProperty("code", type.getBasicCode());
				lostTime.setLostTimeTypeName(BasicType.getName());
				lostTime.setLostTimeTypeCode(BasicType.getCode());
			}
			lostTimeRecordService.updateObj(lostTime);
		}
			
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("message", "修改成功！");
		return modelMap;
	}


	/**
	 * 确认损时记录
	 */
	@RequestMapping("/mcConfirmLostTimeList.do")
	@ResponseBody
	public ModelMap mcConfirmLostTimeList(LostTimeRecord lostTimeRecord,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		String suggestion = lostTimeRecord.getRecoverMethod();
		LostTimeRecord lostTime = lostTimeRecordService.queryObjById(lostTimeRecord.getId());
		PressLight pl = pressLightService.queryByProperty("code", lostTimeRecord.getPressLightCode());
		lostTime.setPressLightCode(pl.getCode());
		lostTime.setReason(pl.getReason());
		lostTime.setDescription(pl.getDescription());
		if(pl.getPressLightType().getLevel().intValue()!=1){
			PressLightType plType = pressLightTypeService.queryByProperty("code", pl.getPressLightType().getBasicCode());
			lostTime.setLostTimeTypeCode(plType.getCode());
			lostTime.setLostTimeTypeName(plType.getName());
		}else{
			lostTime.setLostTimeTypeCode(pl.getPressLightType().getCode());
			lostTime.setLostTimeTypeName(pl.getPressLightType().getName());
		}
		
		String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		User user = userService.queryObjById(lostTimeRecord.getConfirmUserId());
		if(user.getEmployee()!=null){
			user.setUsername(user.getEmployee().getName());
		}


		if(lostTime.getConfirmUserId()==null){
			lostTime.setConfirmTime(new Date());
			Map<String,Object> args = new HashMap<>();
			args.put("suggestion", suggestion);
			try{
				lostTimeRecordService.confirm(lostTime, user, args);
			}catch(Exception e){
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("message", "确认失败！");
				return modelMap;
			}

			modelMap.addAttribute("success", true);
			modelMap.addAttribute("message", "确认成功！");
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("message", "该记录已经确认！");
		return modelMap;
	}
	/**
	 * 多条数据确认损时记录
	 */
	@RequestMapping("/mcConfirmLostTimeByIds.do")
	@ResponseBody
	public ModelMap mcConfirmLostTimeByIds(String ids,String reasonCode,String confirmUserId,String suggestion,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		String[] data = ids.split(",");
		for(String id:data){
			LostTimeRecord lostTime = lostTimeRecordService.queryObjById(Long.parseLong(id));
			PressLight pl = pressLightService.queryByProperty("code", reasonCode);
			lostTime.setPressLightCode(pl.getCode());
			lostTime.setReason(pl.getReason());
			lostTime.setDescription(pl.getDescription());
			PressLightType type = pl.getPressLightType();
			if(type.getLevel()==1){
				lostTime.setLostTimeTypeCode(type.getCode());
				lostTime.setLostTimeTypeName(type.getName());
			}else{
				PressLightType plType = pressLightTypeService.queryByProperty("code",type.getBasicCode());
				lostTime.setLostTimeTypeCode(plType.getCode());
				lostTime.setLostTimeTypeName(plType.getName());
			}
			String ip = request.getRemoteAddr();
			User user = userService.queryObjById(Long.parseLong(confirmUserId));
			MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
			/*Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
			user.setUsername(employee.getName());*/
			if(user.getEmployee()!=null){
				user.setUsername(user.getEmployee().getName());
			}
			
			lostTime.setConfirmTime(new Date());
			Map<String,Object> args = new HashMap<>();
			args.put("suggestion", suggestion);
			try{
				lostTimeRecordService.confirm(lostTime, user, args);
			}catch(Exception e){
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("message", "确认失败！");
				return modelMap;
			}
			
		}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("message", "确认成功！");
			return modelMap;
	}

	/**
	 * 根据id删除损时记录(将deleted设置为TRUE)
	 */
	@RequestMapping("/mcdeleteLostTimeRecord.do")
	@ResponseBody
	public ModelMap mcdeleteLostTimeRecord(String rids) {

		/*if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}*/
		ModelMap modelMap = new ModelMap();
		long count=0;
		try {
			String[] ss=rids.split(",");
			LostTimeRecord pr=new LostTimeRecord();
			for(String s:ss) {
				pr = lostTimeRecordService.queryObjById(Long.valueOf(s));
				pr.setDeleted(true);
				lostTimeRecordService.deleteLostTimeRecord(pr);
				count++;
			}

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
		modelMap.addAttribute("message", "成功删除"+count+"条记录");
		return modelMap;
	}
	/**
	 * 拆分保存
	 * @param data  被拆分的数据
	 * @param deviceSiteCode 站点code
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/mcSplitSaveLostTime.do")
	@ResponseBody
	public ModelMap mcSplitSaveLostTime(@RequestParam(value="data")String data,@RequestParam(value="deviceSite")String deviceSiteCode,HttpServletRequest request) {
		JSONArray jsonArrary = JSONArray.parseArray(data);
		for(Object json : jsonArrary ){
			JSONObject myJson = new JSONObject((Map<String, Object>) json);
			Long id = myJson.getLong("id");
			Double sum = myJson.getDouble("sumOfLostTime")*60;
			Date beginT = myJson.getDate("beginTime");
			Date endT = myJson.getDate("endTime");
			
			if(null!=id&&!id.equals("")&&sum>0){
				LostTimeRecord ltr = lostTimeRecordService.queryObjById(id);
				ltr.setSumOfLostTime(sum);
				ltr.setBeginTime(beginT);
				ltr.setEndTime(endT);
				lostTimeRecordService.updateObj(ltr);
			}else if(null!=id&&!id.equals("")&&sum<=0){
				lostTimeRecordService.deleteObj(id);
			}else{
				String ip = request.getRemoteAddr();
				MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
				User user = userService.queryUserByUsername(mcUser.getUsername());
				Employee employee = employeeService.queryByProperty("code", mcUser.getEmployeeCode());
				user.setUsername(employee.getName());
				
				Map<String,Object> args = new HashMap<>();
				LostTimeRecord spLostTimeRecord = new LostTimeRecord ();
				args.put("businessKey", workFlowKeyConfig.getLostTimeWorkflowKey());
				 
				Date beginTiem = myJson.getDate("beginTime");
				Double sumOfLostTime = myJson.getDouble("sumOfLostTime")*60;
				Date endTime = myJson.getDate("endTime");
				//String reason = myJson.getString("reason");
				String pressLightCode = myJson.getString("pressLightCode");
				String lostTimeTypeName;
				String lostTimeTypeCode;
				if(null!=pressLightCode&&!"".equals(pressLightCode)){
					PressLight pl = pressLightService.queryByProperty("code", pressLightCode);
					spLostTimeRecord.setReason(pl.getReason());
					spLostTimeRecord.setDescription(pl.getDescription());
					spLostTimeRecord.setPressLightCode(pl.getCode());
					
					PressLightType pressLightType = pl.getPressLightType();
					
					if(pressLightType.getBasicCode()==null){
						lostTimeTypeName = pressLightType.getName(); 
						lostTimeTypeCode = pressLightType.getCode();
						spLostTimeRecord.setLostTimeTypeName(lostTimeTypeName);
						spLostTimeRecord.setLostTimeTypeCode(lostTimeTypeCode);
					}else{
						PressLightType plType = pressLightTypeService.queryByProperty("code",pressLightType.getBasicCode());
						lostTimeTypeName = plType.getName(); 
						lostTimeTypeCode = plType.getCode();
						spLostTimeRecord.setLostTimeTypeName(lostTimeTypeName);
						spLostTimeRecord.setLostTimeTypeCode(lostTimeTypeCode);
					}
				}
				DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
				spLostTimeRecord.setBeginTime(beginTiem);
				spLostTimeRecord.setEndTime(endTime);
				spLostTimeRecord.setDeviceSite(deviceSite);
				spLostTimeRecord.setSumOfLostTime(sumOfLostTime/60);
				//lostTimeRecordService.addLostTimeRecord(spLostTimeRecord, user, args);
				
				args.put("businessKey", workFlowKeyConfig.getLostTimeWorkflowKey());
				Serializable recordId = lostTimeRecordService.addLostTimeRecord(spLostTimeRecord,user,args);
				//执行损时申请
				/*TaskService taskService = processEngine.getTaskService();
				Map<String,Object> variables = new HashMap<>();
				variables.put(Constant.Workflow.LOSTTIME_CONFIRM_ROLES, "损时确认人");
				variables.put(Constant.Workflow.LOSTTIME_CONFIRM_PERSON, "");
				variables.put(Constant.Workflow.LOSTTIME_CONFIRM_PERSONS, "");
				Task task = taskService.createTaskQuery().processInstanceBusinessKey(recordId+":" + LostTimeRecord.class.getName()).taskAssignee(user.getUsername()).singleResult();
				taskService.complete(task.getId(),variables);*/
			}
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成保存功");
		return modelMap;
	}
}
