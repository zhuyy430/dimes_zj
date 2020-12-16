package com.digitzones.controllers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.digitzones.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Classes;
import com.digitzones.model.Device;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.DeviceSiteParameterMapping;
import com.digitzones.model.ProductionUnit;
import com.digitzones.util.DateStringUtil;
@Controller
@RequestMapping("/oee")
public class OeeController {
	private DecimalFormat format = new DecimalFormat("#.00");
	private IProductionUnitService productionUnitService;
	private IDeviceSiteService deviceSiteService;
	private IClassesService classesService;
	private ILostTimeRecordService lostTimeRecordService;
	private IProcessRecordService processRecordService;
	private INGRecordService ngRecordService;
	private IDeviceSiteParameterMappingService deviceSiteParameterMappingService;
	@Autowired
	private IJobBookingFormDetailService jobBookingFormDetailService;
	@Autowired
	public void setDeviceSiteParameterMappingService(IDeviceSiteParameterMappingService deviceSiteParameterMappingService) {
		this.deviceSiteParameterMappingService = deviceSiteParameterMappingService;
	}
	@Autowired
	public void setNgRecordService(INGRecordService ngRecordService) {
		this.ngRecordService = ngRecordService;
	}
	@Autowired
	public void setProcessRecordService(IProcessRecordService processRecordService) {
		this.processRecordService = processRecordService;
	}
	@Autowired
	public void setLostTimeRecordService(ILostTimeRecordService lostTimeRecordService) {
		this.lostTimeRecordService = lostTimeRecordService;
	}
	@Autowired
	public void setClassesService(IClassesService classesService) {
		this.classesService = classesService;
	}
	@Autowired
	public void setProductionUnitService(IProductionUnitService productionUnitService) {
		this.productionUnitService = productionUnitService;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}
	/**
	 * 工厂级oee查询
	 * @return
	 */
	@RequestMapping("/queryOee4FactoryUp.do")
	@ResponseBody
	public ModelMap queryOee4Factory(){
		ModelMap modelMap = new ModelMap();
		//查找所有生产单元
		List<ProductionUnit> productionUnits = productionUnitService.queryAllProductionUnits();
		modelMap.put("productionUnits", productionUnits);
		modelMap.put("values", queryOee4PerProductionUnit(productionUnits));
		//产生一个月的时间
		DateStringUtil util = new DateStringUtil();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date preDate = c.getTime();
		List<Date> days =util.generateOneMonthDay(util.date2Month(preDate));
		c.set(Calendar.DATE, days.size());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		modelMap.put("preMonthOee", format.format(oneMonthOee(c,days)*100));
		Calendar currCal = Calendar.getInstance();
		currCal.setTime(new Date());
		Date currDate = currCal.getTime();
		days = util.generateOneMonthDay(util.date2Month(currDate));
		modelMap.put("currentMonthOee",format.format(oneMonthOee(currCal,days)*100));
		modelMap.put("currentDayOee",format.format(currentDayOee()*100));
		return modelMap;
	}
	/**
	 * 当天OEE值
	 * @return
	 */
	private Double currentDayOee() {
		double sumOee = 0;
		Date now = new Date();
		//查找瓶颈站点id
		List<BigInteger> deviceSiteIds = deviceSiteService.queryBottleneckDeviceSiteIds();
		//查询当前班次
		Classes c = classesService.queryCurrentClasses();
		//查询当前班次下损时信息：0：设备站点id  1：损时数
		List<Object[]> lostTimeList = lostTimeRecordService.queryLostTime(c, now);
		//查询当前班次下计划停机信息：0：设备站点id  1：计划停机数
		List<Object[]> planHaltList = lostTimeRecordService.queryPlanHaltTime(c, now);
		//查询当前班，当前设备的生产数量，总标准节拍(加工数量*标准节拍),总短停机时间(即时节拍-标准节拍的和)
		List<Object[]> countAndSumOfStandardBeatAndShortHaltList = processRecordService.queryCountAndSumOfStandardBeatAndSumOfShortHalt4RealTime(c, now);
		//查询当前班的ng信息
		List<Object[]> ngRecordList = ngRecordService.queryNgCount4ClassToday(c, now);
		//获取当前班次在线时长,当前时间-班次开始时间，单位：秒
		long onlineTime = cumputeClassOnlineTime(c,now);
		//根据设备站点查询加工信息
		for(BigInteger deviceSiteId : deviceSiteIds) {
			//损时时间(包括计划停机时间)，单位：分钟
			double lostTime  = 0;
			//计划停机时间，单位：分钟
			double planHaltTime = 0;
			for(Object[] obj : lostTimeList) {
				BigInteger id = (BigInteger)obj[0];
				if(deviceSiteId.equals(id)) {
					lostTime += ((Integer)obj[1]).doubleValue();
				}
			}
			for(Object[] obj : planHaltList) {
				BigInteger id = (BigInteger)obj[0];
				if(deviceSiteId.equals(id)) {
					planHaltTime += ((Integer)obj[1]).doubleValue();
				}
			}
			//时间开动率
			double timeRate = 1;
			if(onlineTime!=0) {
				timeRate=(onlineTime-lostTime*60)/(onlineTime-planHaltTime*60);
			}
			//总标准节拍
			double sumOfStandardBeat = 0;
			//总短停机时间
			double sumOfShortHalt = 0;
			//总生产数
			double count = 0;
			for(Object[] obj : countAndSumOfStandardBeatAndShortHaltList) {
				BigInteger id = (BigInteger)obj[0];
				if(deviceSiteId.equals(id)) {
					if(obj[1]!=null) {
						count += (int)obj[1];
					}
					if(obj[2]!=null) {
						sumOfStandardBeat += Double.parseDouble(String.valueOf(obj[1]));
					}
					if(obj[3]!=null) {
						sumOfShortHalt += Double.parseDouble(String.valueOf(obj[2]));
					}
				}
			}
			//性能开动率
			double performanceRate = 1;
			if((sumOfStandardBeat+sumOfShortHalt)!=0) {
				performanceRate = sumOfStandardBeat/(sumOfStandardBeat+sumOfShortHalt);
			}
			int ngCount = 0;
			//合格品率
			double qualifiedRate = 1;
			for(Object[] obj : ngRecordList) {
				BigInteger id = (BigInteger)obj[0];
				if(deviceSiteId.equals(id)) {
					ngCount += (int)obj[1];
				}
			}
			if(count!=0) {
				qualifiedRate= (count - ngCount) / count;
			}
			sumOee += (timeRate * performanceRate * qualifiedRate);
		}
		return sumOee/deviceSiteIds.size();
	}
	/**
	  * 查询每个生产单元的OEE值
	  * @param productionUnits
	  * @return
	  */
	private List<String> queryOee4PerProductionUnit(List<ProductionUnit> productionUnits){
		List<String> values = new ArrayList<>();
		for(ProductionUnit pu : productionUnits) {
			double sumOee = 0;
			//根据生产单元查询设备站点
			List<BigInteger> deviceSites = deviceSiteService.queryBottleneckDeviceSiteIdsByProductionUnitId(pu.getId());
			//查询当前班次
			Classes c = classesService.queryCurrentClasses();
			//根据设备站点查询加工信息
			for(int i = 0;i<deviceSites.size();i++) {
				Long dsid = Long.valueOf(String.valueOf(deviceSites.get(i)));
				DeviceSite ds = new DeviceSite();
				ds.setId(dsid.longValue());
				sumOee += computeOee4CurrentClass(c, ds,new Date());
			}
			if(CollectionUtils.isEmpty(deviceSites)) {
				values.add(format.format(0));
			}else {
				values.add(format.format(sumOee/deviceSites.size()*100));
			}
		}
		return values;
	}
	/**
	 * 查询产线当天的OEE
	 * @param productionUnitId
	 * @return
	 */
	@RequestMapping("/queryCurrentDayOee4ProductionUnit.do")
	@ResponseBody
	public String queryCurrentDayOee4ProductionUnit(Long productionUnitId) {
		double sumOee = 0;
		//根据生产单元查询设备站点
		List<BigInteger> deviceSites = deviceSiteService.queryBottleneckDeviceSiteIdsByProductionUnitId(productionUnitId);
		//查询当前班次
		Classes c = classesService.queryCurrentClasses();
		//根据设备站点查询加工信息
		for(int i = 0;i<deviceSites.size();i++) {
			Long dsid = Long.valueOf(String.valueOf(deviceSites.get(i)));
			DeviceSite ds = new DeviceSite();
			ds.setId(dsid.longValue());
			sumOee += computeOee4CurrentClass(c, ds,new Date());
		}
		return format.format(deviceSites.size()>0?(sumOee/deviceSites.size()*100):sumOee);
	}
	/**
	 * 查询当前月的OEE值
	 * @return
	 */
	private Double oneMonthOee(Calendar c,List<Date> days) {
		Long deviceSiteCount = deviceSiteService.queryCountOfBottleneckDeviceSite();
		//计算上个月总工时，单位：秒
		long total = 24*3600*days.size()*deviceSiteCount;
		//查询上个月损时数,包括计划停机,单位：分钟
		double totalLostTime =  lostTimeRecordService.queryLostTimeFromBeginOfMonthUntilTheDate(c.getTime(), null);
		//查询上个月计划停机数,单位：分钟
		double totalPlanHalt = lostTimeRecordService.queryLostTimeFromBeginOfMonthUntilTheDate(c.getTime(), true);
		//时间开动率
		double timeRate=(total-totalLostTime*60)/(total-totalPlanHalt*60);
		//查询上月生产数量，总标准节拍(加工数量*标准节拍),总短停机时间(即时节拍-标准节拍的和)
		Object[] countAndSumOfStandardBeatAndShortHalt = processRecordService.queryCountAndSumOfStandardBeatAndSumOfShortHaltFromBeginOfMonthUntilTheDate(c.getTime());
		//总标准节拍
		double sumOfStandardBeat = 0;
		//总短停机时间
		double sumOfShortHalt = 0;
		//总生产数
		double count = 0;
		if(countAndSumOfStandardBeatAndShortHalt!=null && countAndSumOfStandardBeatAndShortHalt.length>0) {
			if(countAndSumOfStandardBeatAndShortHalt[0]!=null) {
				count = (int)countAndSumOfStandardBeatAndShortHalt[0];
			}
			if(countAndSumOfStandardBeatAndShortHalt[1]!=null) {
				sumOfStandardBeat = Double.valueOf(String.valueOf(countAndSumOfStandardBeatAndShortHalt[1]));
			}
			if(countAndSumOfStandardBeatAndShortHalt[2]!=null) {
				sumOfShortHalt = Double.valueOf(String.valueOf(countAndSumOfStandardBeatAndShortHalt[2]));
			}
		}
		//性能开动率
		double performanceRate = 1;
		if(sumOfStandardBeat!=0) {
			performanceRate = sumOfStandardBeat/(sumOfStandardBeat+sumOfShortHalt);
		}
		//查询不合格品数
		int ngCount = ngRecordService.queryNgCountFromBeginOfMonthUntilTheDate(c.getTime());
		//合格品数量
		int notNgCount = (int) (count - ngCount);
		double qualifiedRate = 1;
		if(count != 0) {
			//合格品率
			qualifiedRate = notNgCount / count;
		}
		return timeRate * performanceRate * qualifiedRate;
	}
	/**
	 * 查询当前设备站点的oee数据
	 * @param deviceSiteCode
	 * @return
	 */
	@RequestMapping("/queryOee4CurrentDeviceSite.do")
	@ResponseBody
	public ModelMap queryOee4CurrentDeviceSite(String deviceSiteCode) {
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
		Date date = new Date();
		ModelMap modelMap = new ModelMap();
		Classes currentClass = classesService.queryCurrentClasses();
		if(currentClass==null) {
			return modelMap;
		}
		//获取当前班次在线时长,当前时间-班次开始时间，单位：秒
		long onlineTime = cumputeClassOnlineTime(currentClass,date);
		//查询损时时间(包括计划停机时间)，单位：分钟
		double lostTime = lostTimeRecordService.queryLostTime(currentClass,deviceSite.getId(),date);
		//查询计划停机时间，单位：分钟
		double planHaltTime = lostTimeRecordService.queryPlanHaltTime(currentClass, deviceSite.getId(),date);
		//时间开动率
		double timeRate = 0;
		if(onlineTime!=0) {
			timeRate=(onlineTime-lostTime*60)/(onlineTime-planHaltTime*60);
		}
		//查询当前班，当前设备的生产数量，总标准节拍(加工数量*标准节拍),总短停机时间(即时节拍-标准节拍的和)
		Object[] countAndSumOfStandardBeat = jobBookingFormDetailService.queryCountAndSumOfStandardBeat4CurrentClass(currentClass, deviceSite.getCode(),date);
		//总标准节拍
		double sumOfStandardBeat = 0;
		//总生产数
		double count = 0;
		if(countAndSumOfStandardBeat!=null && countAndSumOfStandardBeat.length>0) {
			if(countAndSumOfStandardBeat[0]!=null) {
				count = (double)countAndSumOfStandardBeat[0];
			}
			if(countAndSumOfStandardBeat[1]!=null) {
				sumOfStandardBeat = Double.parseDouble(String.valueOf(countAndSumOfStandardBeat[1]));
			}
		}
		//性能开动率
		double performanceRate = 0;
		if(onlineTime-lostTime*60!=0) {
			performanceRate = (double)countAndSumOfStandardBeat[2]/(onlineTime-lostTime*60);
		}

		if(sumOfStandardBeat==0){
			performanceRate=1;
		}
		//查询不合格品数
		int ngCount = ngRecordService.queryNgCount4ClassFromClassBegin2now(currentClass, deviceSite.getId());
		//合格品数量
		int notNgCount = (int) (count - ngCount);
		//合格品率
		double qualifiedRate = 0;
		if(count!=0) {
			qualifiedRate= notNgCount / count;
		}
		double oee = timeRate * performanceRate * qualifiedRate;
		modelMap.addAttribute("qualityRate", format.format(qualifiedRate*100)+ "%");
		modelMap.addAttribute("performanceRate", format.format(performanceRate*100)+ "%");
		modelMap.addAttribute("motionRate", format.format(timeRate*100)+ "%");
		modelMap.addAttribute("oee", format.format(oee*100) + "%");
		return modelMap;
	}
	/**
	 * 计算当前班次的在线时间，单位：秒
	 * @param c
	 * @return
	 */
	private long cumputeClassOnlineTime(Classes c,Date date) {
		//当前时间-班次开始时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//是否具有时分秒->非查询当天时使用
		if(calendar.get(Calendar.HOUR) == 0 && calendar.get(Calendar.MINUTE)==0 && calendar.get(Calendar.SECOND)==0) {
			//跨天
			if(c.getStartTime().getTime()>c.getEndTime().getTime()) {
				Calendar start = Calendar.getInstance();
				start.setTime(c.getStartTime());
				start.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				start.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
				start.set(Calendar.DATE, calendar.get(Calendar.DATE));

				Calendar end = Calendar.getInstance();
				end.setTime(c.getEndTime());
				end.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				end.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
				end.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);

				return (end.getTime().getTime() - start.getTime().getTime())/1000;
			}else {
				return (c.getEndTime().getTime()-c.getStartTime().getTime())/1000;
			}
		}

		//当前时间，单位(秒)
		long currentTime = calendar.get(Calendar.HOUR_OF_DAY)*3600 + calendar.get(Calendar.MINUTE)*60 + calendar.get(Calendar.SECOND);
		Calendar cal = Calendar.getInstance();
		if(c!=null) {
			cal.setTime(c.getStartTime());
		}else {
			cal.setTime(new Date());
		}
		//当前班次的开始时间，单位(秒)
		long classesBeginTime = cal.get(Calendar.HOUR_OF_DAY)*3600 + cal.get(Calendar.MINUTE)*60 + cal.get(Calendar.SECOND);
		long onlineTime = 0;
		if(currentTime>classesBeginTime) {
			onlineTime = currentTime - classesBeginTime;
		}else {
			onlineTime = currentTime+24*3600 - classesBeginTime;
		}
		return onlineTime;
	}
	/**
	 * oee:Overall Equipment Effectiveness设备综合效率
	 * 公式：OEE=时间开动率*性能开动率*合格品率*100%
	 * 时间开动率=((当前时间-班次开始时间)-损时)/((当前时间-班次开始时间)-计划停机时间)
	 * 性能开动率=理论加工周期/实际加工周期=加工数量*标准节拍/(加工数量*标准节拍+短停机) 
	 * 短停机=(即时节拍1-标准节拍)+(即时节拍2-标准节拍)...
	 * 合格品率＝合格品数量/加工数量
	 * 查询当前班的当前设备即时OEE
	 * @param currentClass 当前班次
	 * @return
	 */
	private double computeOee4CurrentClass(Classes currentClass ,DeviceSite deviceSite,Date date) {
		if(currentClass==null) {
			return 0;
		}
		//获取当前班次在线时长,当前时间-班次开始时间，单位：秒
		long onlineTime = cumputeClassOnlineTime(currentClass,date);
		//查询损时时间(包括计划停机时间)，单位：分钟
		double lostTime = lostTimeRecordService.queryLostTime(currentClass,deviceSite.getId(),date);
		//查询计划停机时间，单位：分钟
		double planHaltTime = lostTimeRecordService.queryPlanHaltTime(currentClass, deviceSite.getId(),date);
		//时间开动率
		double timeRate = 0;
		if(onlineTime!=0) {
			timeRate=(onlineTime-lostTime*60)/(onlineTime-planHaltTime*60);
		}
		//查询当前班，当前设备的生产数量，总标准节拍(加工数量*标准节拍)
		Object[] countAndSumOfStandardBeat = jobBookingFormDetailService.queryCountAndSumOfStandardBeat4CurrentClass(currentClass, deviceSite.getCode(),date);
		//总标准节拍
		double sumOfStandardBeat = 0;
		//总生产数
		double count = 0;
		if(countAndSumOfStandardBeat!=null && countAndSumOfStandardBeat.length>0) {
			if(countAndSumOfStandardBeat[0]!=null) {
				count = (double)countAndSumOfStandardBeat[0];
			}
			if(countAndSumOfStandardBeat[1]!=null) {
				sumOfStandardBeat = Double.parseDouble(String.valueOf(countAndSumOfStandardBeat[1]));
			}
		}
		//性能开动率
		double performanceRate = 0;
		if(onlineTime-lostTime*60!=0) {
			performanceRate = (double)countAndSumOfStandardBeat[2]/(onlineTime-lostTime*60);
		}

		if(sumOfStandardBeat==0){
			performanceRate=1;
		}
		//查询不合格品数
		int ngCount = ngRecordService.queryNgCount4Class(currentClass, deviceSite.getId(),date);
		//合格品数量
		int notNgCount = (int) (count - ngCount);
		//合格品率
		double qualifiedRate = 0;
		if(count!=0) {
			qualifiedRate= notNgCount / count;
		}
		return timeRate * performanceRate * qualifiedRate;
	}
	/**
	 * 查询每个生产单元的OEE值
	 * @return
	 */
	@RequestMapping("/queryOee4ProductionUnit.do")
	@ResponseBody
	public ModelMap queryOee4ProductionUnit(Long productionUnitId) {
		if(productionUnitId==null) {
			return null;
		}
		ProductionUnit unit = productionUnitService.queryObjById(productionUnitId);
		//根据产线查找目标OEE
		double goalOee =unit.getGoalOee();
		List<String> goalOeeList = new ArrayList<>();
		ModelMap modelMap = new ModelMap();
		//产生一个月的时间
		DateStringUtil util = new DateStringUtil();
		List<Date> days =util.generateOneMonthDay(util.date2Month(new Date()));
		List<List<String>> oeeList = new ArrayList<>();
		List<String> dayList = new ArrayList<>();
		for(Date day : days) {
			dayList.add(util.date2DayOfMonth(day));
		}
		Date date = new Date();
		//根据生产单元id查询设备站点
		List<BigInteger> deviceSiteIds =  deviceSiteService.queryBottleneckDeviceSiteIdsByProductionUnitId(productionUnitId);
		Calendar calendar = Calendar.getInstance();
		String dateDay = util.date2DayOfMonth(date);
		//查询所有班次
		List<Classes> classesList = classesService.queryAllClasses();
		for(int i = 0;i<classesList.size();i++) {
			Classes c = classesList.get(i);
			List<String> oees = new ArrayList<>();
			for(Date now : days) {
				goalOeeList.add(format.format(goalOee));
				calendar.setTime(now);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				String nowDay = util.date2DayOfMonth(now);
				if(Integer.valueOf(nowDay)<=Integer.valueOf(dateDay) && !CollectionUtils.isEmpty(deviceSiteIds)) {
					double oee = 0;
					for(Object obj : deviceSiteIds) {
						long dsid = Long.parseLong(String.valueOf(obj));
						DeviceSite ds = new DeviceSite();
						ds.setId(dsid);
						oee +=computeOee4CurrentClass(c,ds,calendar.getTime());
					}
					oees.add(format.format(oee/deviceSiteIds.size()*100));
				}else {
					oees.add(0+"");
				}
			}
			oeeList.add(oees);
		}
		//求平均值
		List<String> avgs = new ArrayList<>();
		for(int j = 0;j<days.size();j++) {
			double sum = 0;
			for(int i = 0;i<oeeList.size();i++) {
				if(j<oeeList.get(i).size()) {
					try {
						sum += Double.parseDouble(oeeList.get(i).get(j));
					}catch (Exception e) {
						System.err.println(e.getMessage());
						sum += 0;
					}
				}
			}
			avgs.add(format.format(sum/oeeList.size()));
		}
		modelMap.addAttribute("classes",classesList);
		modelMap.addAttribute("goalOeeList",goalOeeList);
		modelMap.addAttribute("avgs",avgs);
		modelMap.addAttribute("oeeList", oeeList);
		modelMap.addAttribute("days", dayList);
		return modelMap;
	}
	  
	/**
	 * 查询运行率top
	 * @return
	 */
	@RequestMapping("/queryOperationRateTop.do")
	@ResponseBody
	public ModelMap queryOperationRateTop() {
		ModelMap modelMap = new ModelMap();
		String[] placingList = {
				"第一名",
				"第二名",
				"第三名",
				"第四名",
				"第五名",
				"第六名",
				"第七名",
				"第八名",
				"第九名",
				"第十名",
		};
		List<String> placing = new ArrayList<>();
		List<String> deviceSiteNames = new ArrayList<>();
		List<Object[]> values = new ArrayList<>();
		Date today = new Date();
		//根据生产单元查询设备站点
		List<DeviceSite> deviceSites = deviceSiteService.queryAllDeviceSites();
		//查询当前班次
		Classes c = classesService.queryCurrentClasses();
		//根据设备站点查询加工信息
		for(DeviceSite ds : deviceSites) {
			double oee = computeOee4CurrentClass(c, ds,today);
			Object[] o = new Object[2];
			o[0] = ds.getName()+"("+ds.getCode()+")";
			o[1] = oee;
			values.add(o);
		} 
		//排序
		Object[] t = null;
		for (int i = 0; i < values.size() - 1; i++) {
			for (int j = 0; j < values.size() - 1 - i; j++) {
				Object[] v1 = values.get(j);
				Object[] v2 = values.get(j+1);
				if ((Double)v1[1] <(Double)v2[1]) {
					t = v1;
					values.set(j, v2);
					values.set(j+1, t);
				}
			}
		}

		for(int i=0;i<values.size()&&i<5;i++) {
			placing.add(placingList[i]);
			deviceSiteNames.add(values.get(i)[0]+"");
		}

		modelMap.addAttribute("placing", placing);
		modelMap.addAttribute("deviceSiteNames", deviceSiteNames);
		return modelMap;
	}
	/**
	 * 查询在参数状态页面显示的设备站点
	 * @return
	 */
	@RequestMapping("/queryDeviceSite4ParameterStatusShow.do")
	@ResponseBody
	public ModelMap queryDeviceSite4ParameterStatusShow(HttpServletRequest request) {
		DecimalFormat format = new DecimalFormat("#.00");
		ModelMap modelMap = new ModelMap();
		List<DeviceSite> deviceSites = deviceSiteService.queryDeviceSitesByShow(true);
		List<String> oees = new ArrayList<>();
		List<List<DeviceSiteParameterMapping>> list = new ArrayList<>();
		//良品率
		List<String> rtys = new ArrayList<>();
		//查询当前班次
		Classes c = classesService.queryCurrentClasses();
		if(c==null) {
			modelMap.addAttribute("error", "不存在当前班次信息!");
			return modelMap;
		}
		Date now = new Date();
		//根据设备站点查询加工信息
		for(DeviceSite ds : deviceSites) {
			writeImg2Dir(ds.getDevice(),request);
			//查询设备站点关联的参数
			list.add(deviceSiteParameterMappingService.queryByDeviceSiteId(ds.getId()));

			//查询当前班，当前设备的生产数量，总标准节拍(加工数量*标准节拍),总短停机时间(即时节拍-标准节拍的和)
			Object[] countAndSumOfStandardBeatAndShortHalt = processRecordService.queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(c, ds.getId(),now);
			//总生产数
			double count = 0;
			if(countAndSumOfStandardBeatAndShortHalt!=null && countAndSumOfStandardBeatAndShortHalt.length>0) {
				if(countAndSumOfStandardBeatAndShortHalt[0]!=null)
					count = (int)countAndSumOfStandardBeatAndShortHalt[0];
			}
			//查询不合格品数
			int ngCount = ngRecordService.queryNgCount4Class(c, ds.getId(),now);
			//合格品数量
			int notNgCount = (int) (count - ngCount);
			if(count == 0) {
				rtys.add("100");
			}else {
				rtys.add(format.format(notNgCount*1.0/count*100));
			}
			double oee = computeOee4CurrentClass(c,ds, now);
			oees.add(format.format(oee*100));
		}
		modelMap.addAttribute("deviceSites", deviceSites);
		modelMap.addAttribute("parameters", list);
		modelMap.addAttribute("oees", oees);
		modelMap.addAttribute("rtys", rtys);
		return modelMap;
	}
	  	/**
	  * 将数据库中的图片 写入到服务器目录
	  * @param device
	  * @param request
	  */
	private void writeImg2Dir(Device device,HttpServletRequest request) {
		if(device!=null) {
			if(device.getPhoto()!=null) {
				String dir = request.getServletContext().getRealPath("/");
				String realName = device.getPhotoName();
				String fileName = realName;
				InputStream is;
				try {
					is = device.getPhoto().getBinaryStream();
					File out = new File(dir,fileName);
					FileCopyUtils.copy(is, new FileOutputStream(out));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
} 
