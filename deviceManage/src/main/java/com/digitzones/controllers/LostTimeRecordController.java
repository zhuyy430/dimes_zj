package com.digitzones.controllers;
import com.digitzones.constants.Constant;
import com.digitzones.model.*;
import com.digitzones.service.*;
import com.digitzones.util.DateStringUtil;
import com.digitzones.vo.LostTimeRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Controller
@RequestMapping("/lostTimeRecord")
public class LostTimeRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private ILostTimeRecordService lostTimeRecordService;
	private IClassesService classesService;
	private IProductionUnitService productionUnitService;
	private DateStringUtil util = new DateStringUtil();
	@Autowired
	private IProcessRecordService processRecordService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPressLightTypeService pressLightTypeService;
	@Autowired
	public void setProductionUnitService(IProductionUnitService productionUnitService) {
		this.productionUnitService = productionUnitService;
	}
	@Autowired
	public void setClassesService(IClassesService classesService) {
		this.classesService = classesService;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd"); 
	/**
	 * 根据设备站点id查找损时记录
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryLostTimeRecordByDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordByDeviceSiteId(Long deviceSiteId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from LostTimeRecord ltr where ltr.deviceSite.id=?0 and ltr.deleted=?1 order by ltr.beginTime desc";
		Pager<LostTimeRecord> pager = lostTimeRecordService.queryObjs(hql, page, rows, new Object[] {deviceSiteId,false});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据设备站点id查找损时记录（搜索）
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryLostTimeRecordByDeviceSiteIdandSearch.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordByDeviceSiteIdandSearch(Long deviceSiteId,@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {

		String hql = "from LostTimeRecord ltr where ltr.deviceSite.id=?0 and ltr.deleted=?1";

		String searchText=params.get("searchText");
		String searchChange=params.get("searchChange");
		String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		List<Object> list=new ArrayList<Object>();
		list.add(deviceSiteId);
		list.add(false);
		int i=list.size()-1;
		try {
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
				i++;
				hql+=" and ltr.beginTime>=?"+i;	
				list.add(format.parse(beginDateStr));
			}
			if(endDateStr!=null && !"".equals(endDateStr)) {
				i++;
				hql+=" and ltr.beginTime<=?"+i;
				list.add(format.parse(endDateStr));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(searchText!=null && !"".equals(searchText)&&searchChange!=null && !"".equals(searchChange)) {
			hql+=" and ltr."+searchChange+" like '%'+?"+(i+1)+"+'%'";
			list.add(searchText);
		}
		hql+=" order by ltr.beginTime desc";
		Object[] obj =  list.toArray(new Object[1]);  

		Pager<LostTimeRecord> pager = lostTimeRecordService.queryObjs(hql, page, rows, obj);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据过滤id查找损时记录
	 * 0:查询所有
	 * 1：未分类
	 * 2：待确认
	 * 3：已确认
	 * @param filterId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryLostTimeRecordByFilterId.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordByFilterId(Long filterId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		StringBuilder hql = new StringBuilder("from LostTimeRecord ltr where  ltr.deleted=?0");
		switch(String.valueOf(filterId)) {
		//查询所有
		case "0":{
			break;
		}
		//未分类
		case "1":{
			hql.append(" and ltr.lostTimeTypeCode is null or ltr.lostTimeTypeCode =''");
			break;
		}
		//待确认
		case "2":{
			hql.append(" and ltr.lostTimeTypeCode is not null and ltr.confirmTime is null");
			break;
		}
		//已确认
		case "3":{
			hql.append(" and ltr.confirmTime is not null");
			break;
		}
		}
		Pager<LostTimeRecord> pager = lostTimeRecordService.queryObjs(hql.toString(), page, rows, new Object[] {false});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 添加损时记录
	 * @return
	 */
	@RequestMapping("/addLostTimeRecord.do")
	@ResponseBody
	public ModelMap addLostTimeRecord(LostTimeRecord lostTimeRecord,Principal principal) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryObjById(lostTimeRecord.getDeviceSite().getId());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}

		if(lostTimeRecord.getBeginTime()==null) {
			lostTimeRecord.setBeginTime(new Date());
		}

		if(lostTimeRecord.getEndTime()!=null) {
			long endTimeMilliSec = lostTimeRecord.getEndTime().getTime();
			long beginTimeMilliSec = lostTimeRecord.getBeginTime().getTime();
			long result = endTimeMilliSec - beginTimeMilliSec;
			double sumTime = result/1000;
			lostTimeRecord.setSumOfLostTime(sumTime/3600);
		}
		Date now = new Date();
		lostTimeRecord.setLostTimeTime(now);
		//查找当前班次
//		Classes c = classesService.queryClassesByTime(lostTimeRecord.getBeginTime());
		DeviceSite deviceSite = deviceSiteService.queryObjById(lostTimeRecord.getDeviceSite().getId());
		Classes c = classesService.queryClassesByTimeAndClassTypeName(lostTimeRecord.getBeginTime(),deviceSite.getDevice().getProductionUnit().getClassTypeName());
		if(c!=null) {
			lostTimeRecord.setClassesCode(c.getCode());
			lostTimeRecord.setClassesName(c.getName());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);//2020-04-17 10:00:00
			
			Calendar startCalendar = Calendar.getInstance();//2020-04-17 19:00:00
			startCalendar.setTime(c.getStartTime());
			startCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			startCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
			startCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
			
			Calendar endCalendar = Calendar.getInstance();//2020-04-17 7:00:00
			endCalendar.setTime(c.getEndTime());
			endCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			endCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
			endCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
			//跨天
			if(startCalendar.after(endCalendar)) {
				Calendar recordEndCalendar = Calendar.getInstance();//2020-04-17 23:00:00
				recordEndCalendar.setTime(lostTimeRecord.getEndTime());
				recordEndCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
				if(startCalendar.after(recordEndCalendar))
				calendar.add(Calendar.DATE, -1);
				lostTimeRecord.setForLostTimeRecordDate(yyyyMMdd.format(calendar.getTime()));
			}else {
				lostTimeRecord.setForLostTimeRecordDate(yyyyMMdd.format(calendar.getTime()));
			}
		}
		User user = userService.queryUserByUsername(principal.getName());
		Map<String,Object> args = new HashMap<>();
		Serializable id = lostTimeRecordService.addLostTimeRecord(lostTimeRecord,user,args);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询损时记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryLostTimeRecordById.do")
	@ResponseBody
	public LostTimeRecordVO queryLostTimeRecordById(Long id) {
		LostTimeRecord plr = lostTimeRecordService.queryObjById(id);
		return model2VO(plr);
	}


	private LostTimeRecordVO model2VO(LostTimeRecord lostTimeRecord) {
		LostTimeRecordVO vo = new LostTimeRecordVO();
		vo.setId(lostTimeRecord.getId());
		if(lostTimeRecord.getBeginTime()!=null) {
			vo.setBeginTime(format.format(lostTimeRecord.getBeginTime()));
		}

		if(lostTimeRecord.getEndTime()!=null) {
			vo.setEndTime(format.format(lostTimeRecord.getEndTime()));
		}
		vo.setDescription(lostTimeRecord.getDescription());
		vo.setLostTimeTypeName(lostTimeRecord.getLostTimeTypeName());
		vo.setReason(lostTimeRecord.getReason());
		vo.setLostTimeTypeCode(lostTimeRecord.getLostTimeTypeCode());
		vo.setPressLightCode(lostTimeRecord.getPressLightCode());

		long endTime;
		long beginTime = lostTimeRecord.getBeginTime().getTime();
		if(null==lostTimeRecord.getEndTime()){
			endTime = new Date().getTime();
		}else{
			endTime = lostTimeRecord.getEndTime().getTime();
		}

		String sumTime = new DecimalFormat("#####.00").format((double)(endTime-beginTime)/60/1000);
		double sumLostTime = Double.parseDouble(sumTime);
		vo.setSumOfLostTime(sumLostTime);
		return vo;
	}


	/**
	 * 编辑损时记录
	 * @return
	 */
	@RequestMapping("/updateLostTimeRecord.do")
	@ResponseBody
	public ModelMap updatePressLightRecord(LostTimeRecord lostTimeRecord) {
		ModelMap modelMap = new ModelMap();

		LostTimeRecord plr = lostTimeRecordService.queryObjById(lostTimeRecord.getId());
		if(lostTimeRecord.getEndTime()!=null) {
			long endTimeMilliSec = lostTimeRecord.getEndTime().getTime();
			long beginTimeMilliSec = lostTimeRecord.getBeginTime().getTime();
			long result = endTimeMilliSec - beginTimeMilliSec;
			plr.setSumOfLostTime(result/1000*1.0);
		}

		plr.setBeginTime(lostTimeRecord.getBeginTime());
		plr.setEndTime(lostTimeRecord.getEndTime());
		plr.setDescription(lostTimeRecord.getDescription());
		plr.setLostTimeTypeName(lostTimeRecord.getLostTimeTypeName());
		plr.setReason(lostTimeRecord.getReason());
		plr.setPlanHalt(lostTimeRecord.getPlanHalt());
		lostTimeRecordService.updateObj(plr);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功!");
		return modelMap;
	}
	/**
	 * 编辑损时记录
	 * @return
	 */
	@RequestMapping("/updateLostTimeRecordUpdate.do")
	@ResponseBody
	public ModelMap updateLostTimeRecordUpdate(LostTimeRecord lostTimeRecord) {
		ModelMap modelMap = new ModelMap();

		LostTimeRecord plr = lostTimeRecordService.queryObjById(lostTimeRecord.getId());
		if(lostTimeRecord.getEndTime()!=null) {
			long endTimeMilliSec = lostTimeRecord.getEndTime().getTime();
			long beginTimeMilliSec = lostTimeRecord.getBeginTime().getTime();
			long result = endTimeMilliSec - beginTimeMilliSec;
			plr.setSumOfLostTime(result/60/1000*1.0);
		}

		plr.setBeginTime(lostTimeRecord.getBeginTime());
		plr.setEndTime(lostTimeRecord.getEndTime());
		plr.setDescription(lostTimeRecord.getDescription());
		plr.setLostTimeTypeName(lostTimeRecord.getLostTimeTypeName());
		plr.setReason(lostTimeRecord.getReason());
		lostTimeRecordService.updateObj(plr);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功!");
		return modelMap;
	}
	/**
	 * 确认
	 * @return
	 */
	@RequestMapping("/confirmLostTimeRecord.do")
	@ResponseBody
	public ModelMap confirmLostTimeRecord(Long id,String suggestion,Principal principal) {
		/*HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Constant.User.LOGIN_USER);*/
		User user = userService.queryUserByUsername(principal.getName());
		LostTimeRecord plr = lostTimeRecordService.queryObjById(id);
		plr.setConfirmTime(new Date());
		Map<String,Object> args = new HashMap<>();
		args.put("suggestion", suggestion);
		args.put(Constant.Workflow.SUGGESTION, suggestion);
		lostTimeRecordService.confirm(plr,user,args);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("title", "提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 根据id删除损时记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteLostTimeRecord.do")
	@ResponseBody
	public ModelMap deleteLostTimeRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		LostTimeRecord pr = lostTimeRecordService.queryObjById(Long.valueOf(id));
		pr.setDeleted(true);
		try {
			lostTimeRecordService.deleteLostTimeRecord(pr);
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
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 查询当前年所有月份的损时统计
	 * 工厂级：故障时间
	 * @return
	 */
	@RequestMapping("/queryLostTimeRecordFor1Year.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordFor1Year() {
		ModelMap modelMap = new ModelMap();
		List<List<Object>> data = new ArrayList<>();
		Object[] months = {"月份","1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
		data.add(Arrays.asList(months));
 		List<Object> unCategoried = new ArrayList<>();
		unCategoried.add("未分类") ;
		data.add(unCategoried);
		double max = 0;

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		for(int i = 1;i<=12;i++) {
			if(i<=month) {
				List<Object[]> list = lostTimeRecordService.queryLostTimeRecordByYearAndMonth(year,i);
				data:for(int j = 0;j<data.size()-1;j++) {
					List<Object> typeList = data.get(j+1);
					for(int k = 0;k<list.size();k++) {
						Object[] ltr = list.get(k);
						if(ltr[0]==null || "".equals(ltr[0])) {
							ltr[0] = "未分类";
						}
						if(ltr[0].equals(typeList.get(0))) {
							double sumOflostTime = ((BigDecimal)ltr[1]).doubleValue();
							if(sumOflostTime>max) {
								max = sumOflostTime;
							}
							typeList.add(decimalFormat.format(sumOflostTime));
							continue data;
						}
					}
					typeList.add(0);
				}
			}else {
				for(int j = 0;j<data.size()-1;j++) {
					List<Object> typeList = data.get(j+1);
					typeList.add(0);
				}
			}
		}
		modelMap.addAttribute("max",max + max * 0.05);
		modelMap.addAttribute("data",data);
		return modelMap;
	}



	/**
	 * 根据月份和生产单元查询损时统计
	 * @return
	 */
	@RequestMapping("/queryLostTimeRecordByYearAndMonthAndProduction.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordByYearAndMonthAndProduction(String productionUnitIds,String productDate) {
		ModelMap modelMap = new ModelMap();
		List<List<Object>> data = new ArrayList<>();
		List<Object> months = new ArrayList<>();
		List<Date> thisMonth = util.generateOneMonthDay(productDate);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		Date date;
		Calendar c = Calendar.getInstance();
		try {
			date = sdf.parse(productDate);
			c.setTime(date);
		}catch (ParseException e) {
			System.err.println("日期格式不正确，需要yyyy-MM格式字符串,但得到的字符串为：" + productDate);
		}
		months.add("日期");
		for(Date today:thisMonth){
			months.add(util.date2DayOfMonth(today));
		}
		data.add(months);



		//查询所有大类别
		List<PressLightType> types = pressLightTypeService.queryFirstLevelType(Constant.PressLightTypeName.PRESSLIGHT);
		if(types==null || types.size()<=0) {
			List<Object> l = new ArrayList<>();
			l.add("");
			for(int i = 1;i<13;i++) {
				l.add(0);
			}
			data.add(l);
			modelMap.addAttribute("data",data);
			return modelMap;
		}

		for(int i = 0;i<types.size();i++) {
			PressLightType plt = types.get(i);
			List<Object> list = new ArrayList<>();
			list.add(plt.getName());

			data.add(list);
		}




		List<Object> unCategoried = new ArrayList<>();
		unCategoried.add("未分类") ;
		data.add(unCategoried);
		double max = 0;


		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day=c.get(Calendar.DAY_OF_MONTH);
		Calendar d = Calendar.getInstance();
		d.setTime(new Date());
		int nowYear = d.get(Calendar.YEAR);
		int nowMonth = d.get(Calendar.MONTH) + 1;
		int nowday=d.get(Calendar.DAY_OF_MONTH);
		for(int i = 1;i<months.size();i++) {
			if(nowYear==year&&month==nowMonth){
				if(i<=nowday) {
					List<Object[]> list = lostTimeRecordService.queryLostTimeRecordByProductionIdsAndYearAndMonth(year,month,i,productionUnitIds);
					data:for(int j = 0;j<data.size()-1;j++) {
						List<Object> typeList = data.get(j+1);
						for(int k = 0;k<list.size();k++) {
							Object[] ltr = list.get(k);
							if(ltr[0]==null || "".equals(ltr[0])) {
								ltr[0] = "未分类";
							}
							if(ltr[0].equals(typeList.get(0))) {
								double sumOflostTime = ((BigDecimal)ltr[1]).doubleValue();
								if(sumOflostTime>max) {
									max = sumOflostTime;
								}
								typeList.add(decimalFormat.format(sumOflostTime));
								continue data;
							}
						}
						typeList.add(0);
					}
				}else {
					for(int j = 0;j<data.size()-1;j++) {
						List<Object> typeList = data.get(j+1);
						typeList.add(0);
					}
				}
			}else{
				List<Object[]> list = lostTimeRecordService.queryLostTimeRecordByProductionIdsAndYearAndMonth(year,month,i,productionUnitIds);
				data:for(int j = 0;j<data.size()-1;j++) {
					List<Object> typeList = data.get(j+1);
					for(int k = 0;k<list.size();k++) {
						Object[] ltr = list.get(k);
						if(ltr[0]==null || "".equals(ltr[0])) {
							ltr[0] = "未分类";
						}
						if(ltr[0].equals(typeList.get(0))) {
							double sumOflostTime = ((BigDecimal)ltr[1]).doubleValue();
							if(sumOflostTime>max) {
								max = sumOflostTime;
							}
							typeList.add(decimalFormat.format(sumOflostTime));
							continue data;
						}
					}
					typeList.add(0);
				}
			}

		}
		modelMap.addAttribute("max",max + max * 0.05);
		modelMap.addAttribute("data",data);
		return modelMap;
	}



	/**
	 * 查询当前年所有月份的损时统计(优化)
	 * 工厂级：故障时间
	 * @return
	 */
	@RequestMapping("/queryAllMonthLostTimeRecordFor1Year.do")
	@ResponseBody
	public ModelMap queryAllMonthLostTimeRecordFor1Year() {
		ModelMap modelMap = new ModelMap();
		List<List<Object>> data = new ArrayList<>();
		Object[] months = {"月份","1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
		data.add(Arrays.asList(months));

		//查询所有大类别
			List<Object> l = new ArrayList<>();
			l.add("");
			for(int i = 1;i<13;i++) {
				l.add(0);
			}
		List<Object> unCategoried = new ArrayList<>();
		unCategoried.add("未分类") ;
		data.add(unCategoried);
		double max = 0;

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		List<Object[]> list = lostTimeRecordService.queryAllMonthLostTimeRecordFor1Year(year);
		for(int i = 1;i<=12;i++) {
			if(i<=month) {
				data:for(int j = 0;j<data.size()-1;j++) {
					List<Object> typeList = data.get(j+1);
					for(int k = 0;k<list.size();k++) {
						Object[] ltr = list.get(k);
						if(ltr[0]==null || "".equals(ltr[0])) {
							ltr[0] = "未分类";
						}
						if(ltr[0].equals(typeList.get(0))&&(int)ltr[2]==i) {
							double sumOflostTime = ((BigDecimal)ltr[1]).doubleValue();
							if(sumOflostTime>max) {
								max = sumOflostTime;
							}
							typeList.add(decimalFormat.format(sumOflostTime));
							continue data;
						}
					}
					typeList.add(0);
				}
			}else {
				for(int j = 0;j<data.size()-1;j++) {
					List<Object> typeList = data.get(j+1);
					typeList.add(0);
				}
			}
		}
		modelMap.addAttribute("max",max + max * 0.05);
		modelMap.addAttribute("data",data);
		return modelMap;
	}


	/**
	 * 工厂级：故障停机
	 * @return
	 */
	@RequestMapping("/queryLostTimeRecordOfHalt.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordOfHalt() {
		ModelMap modelMap = new ModelMap();
		//存放每个月的故障小时数
		List<String> hours = new ArrayList<>();
		//存放故障小时数和总运行数的占比
		List<String>  ratios = new ArrayList<>();
		//查询所有单元内的站点数目
		Long count = deviceSiteService.queryCountOfBottleneckDeviceSite();
		if(count==null || count == null) {
			modelMap.addAttribute("hours", 0);
			modelMap.addAttribute("ratios", 0);
			return modelMap;
		}
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		for(int i = 1;i<=12;i++) {
			if(i<=month) {
				Double hour = lostTimeRecordService.queryHoursOfLostTimeRecordByYearAndMonth(year, i);
				Double planHaltHour = lostTimeRecordService.queryHoursOfPlanHaltByYearAndMonth(year, i);
				hours.add(decimalFormat.format(hour));
				if(hour!=null) {
					switch(i) {
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:{
						ratios.add(decimalFormat.format(hour/(count*24*31-(planHaltHour==null?0:planHaltHour))*100));
						break;
					}
					case 4:
					case 6:
					case 9:
					case 11:{
						ratios.add(decimalFormat.format(hour/(count*24*30-(planHaltHour==null?0:planHaltHour))*100));
						break;
					}
					case 2:{
						GregorianCalendar gc = new GregorianCalendar();
						if(gc.isLeapYear(c.get(Calendar.YEAR))) {
							ratios.add(decimalFormat.format(hour/(count*24*29-(planHaltHour==null?0:planHaltHour))*100));
						}else {
							ratios.add(decimalFormat.format(hour/(count*24*28-(planHaltHour==null?0:planHaltHour))*100));
						}
						break;
					}
					}
				}else {
					ratios.add(decimalFormat.format(0.0));
				}
			}else {
				hours.add(decimalFormat.format(0.0));
				ratios.add(decimalFormat.format(0.0));
			}
		}
		modelMap.addAttribute("hours", hours);
		modelMap.addAttribute("ratios", ratios);
		return modelMap;
	}
	/**
	 * 工厂级：故障停机(优化)
	 * @return
	 */
	@RequestMapping("/queryLostTimeRecordOfHaltUp.do")
	@ResponseBody
	public ModelMap queryLostTimeRecordOfHaltUp() {
		ModelMap modelMap = new ModelMap();
		//存放每个月的故障小时数
		List<String> hours = new ArrayList<>();
		//存放故障小时数和总运行数的占比
		List<String>  ratios = new ArrayList<>();
		//查询所有单元内的站点数目
		Long count = deviceSiteService.queryCountOfDeviceSite();
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		List<?> obj = lostTimeRecordService.queryHoursOfLostTimeRecord(year);

		for(int i = 1;i<=12;i++) {
			if(i<=month) {
				for(Object ret:obj){
					Object[] arr = (Object[])ret;
					String mon= arr[0].toString();
					String times= arr[1].toString();
					Double hour= Double.parseDouble(times);
					Double planHaltHour = Double.parseDouble(times);
					if(hour!=null&&mon.equals(i+"")) {
						hours.add(decimalFormat.format(hour));
						switch(i) {
						case 1:
						case 3:
						case 5:
						case 7:
						case 8:
						case 10:
						case 12:{
							ratios.add(decimalFormat.format(hour/(count*24*31-(planHaltHour==null?0:planHaltHour))*100));
							break;
						}
						case 4:
						case 6:
						case 9:
						case 11:{
							ratios.add(decimalFormat.format(hour/(count*24*30-(planHaltHour==null?0:planHaltHour))*100));
							break;
						}
						case 2:{
							GregorianCalendar gc = new GregorianCalendar();
							if(gc.isLeapYear(c.get(Calendar.YEAR))) {
								ratios.add(decimalFormat.format(hour/(count*24*29-(planHaltHour==null?0:planHaltHour))*100));
							}else {
								ratios.add(decimalFormat.format(hour/(count*24*28-(planHaltHour==null?0:planHaltHour))*100));
							}
							break;
						}
						}
					}else {
						hours.add(decimalFormat.format(0.0));
						ratios.add(decimalFormat.format(0.0));
					}
				}
			}else {
				hours.add(decimalFormat.format(0.0));
				ratios.add(decimalFormat.format(0.0));
			}
		}
		modelMap.addAttribute("hours", hours);
		modelMap.addAttribute("ratios", ratios);
		return modelMap;
	}
	/**
	 * 损时原因分析
	 * @param productionUnitId
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/queryLostTimeReason4ProductionUnit.do")
	@ResponseBody
	public ModelMap queryLostTimeReason4ProductionUnit(Long productionUnitId) throws ParseException {
		ModelMap modelMap = new ModelMap();
		//产生一个月的时间
		DateStringUtil util = new DateStringUtil();
		List<Date> days =util.generateOneMonthDay(util.date2Month(new Date()));

		List<String> dayList = new ArrayList<>();
		for(Date day : days) {
			dayList.add(util.date2DayOfMonth(day));
		}
		//存放总损时数(每个班的损时List)
		List<List<String>> list = new ArrayList<>();
		Date date = new Date();
		//根据生产单元id查询设备站点
		//List<DeviceSite> deviceSites = deviceSiteService.queryDeviceSitesByProductionUnitId(productionUnitId);
		//查询所有班次
		List<Classes> classesList = classesService.queryAllClasses();
		//double totalHours = 0;
		for(int i = 0;i<classesList.size();i++) {
			//存放每个班的损时数
			List<String> lostTimeList = new ArrayList<>();
			Classes c = classesList.get(i);
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			//查找当前月的损时记录 0：日期  1：损时数
			List<Object[]> lostTimeInfoList = lostTimeRecordService.queryLostTime4PerMonth(c, date);
			for(Date now : days) {
				String nowDay = util.date2DayOfMonth(now);
				String dateDay = util.date2DayOfMonth(date);
				if(Integer.valueOf(nowDay)<=Integer.valueOf(dateDay)) {
					boolean exist = false;
					for(Object obj[] : lostTimeInfoList) {
						String dat = (String)obj[0];
						if(Integer.valueOf(nowDay)==Integer.valueOf(util.date2DayOfMonth(f.parse(dat)))) {
							exist = true;
							double lostTime = (Integer)obj[1];
							lostTimeList.add(decimalFormat.format(lostTime/60));
							break;
						}
					}

					if(!exist) {
						lostTimeList.add(0+"");
					}
				}else {
					lostTimeList.add(0+"");
				}
			}
			list.add(lostTimeList);
		}
		List<String> goalLostTimeList = new ArrayList<>();
		//计算损时目标
		double goalLostTime = productionUnitService.queryGoalLostTimeByProductionUnitId(productionUnitId)/60;
		for(@SuppressWarnings("unused") Date now : days) {
			goalLostTimeList.add(decimalFormat.format(goalLostTime));
		}
		modelMap.addAttribute("classes",classesList);
		modelMap.addAttribute("goalLostTimeList",goalLostTimeList);
		modelMap.addAttribute("lostTimeList",list);
		modelMap.addAttribute("days", dayList);
		return modelMap;
	}
	/**
	 * 损时原因分析
	 * @param productionUnitId
	 * @return
	 */
	@RequestMapping("/queryLostTimeReasonForProductionUnit.do")
	@ResponseBody
	public ModelMap queryLostTimeReasonForProductionUnit(Long productionUnitId) {
		ModelMap modelMap = new ModelMap();
		//产生一个月的时间
		DateStringUtil util = new DateStringUtil();
		List<Date> days =util.generateOneMonthDay(util.date2Month(new Date()));

		List<String> dayList = new ArrayList<>();
		for(Date day : days) {
			dayList.add(util.date2DayOfMonth(day));
		}
		//存放总损时数(每个班的损时List)
		List<List<String>> list = new ArrayList<>();
		//根据生产单元id查询设备站点
		List<BigInteger> deviceSites = deviceSiteService.queryBottleneckDeviceSiteIdsByProductionUnitId(productionUnitId);
		//查询所有班次
		List<Classes> classesList = classesService.queryAllClasses();
		for(int i = 0;i<classesList.size();i++) {
			//存放每个班的损时数
			List<String> lostTimeList = new ArrayList<>();
			Classes c = classesList.get(i);
			for(Date now : days) {
				Integer lostTime = lostTimeRecordService.queryLostTimeForPerDay(c, now,productionUnitId);
				lostTimeList.add(decimalFormat.format(lostTime*1.0/60/deviceSites.size()));
			}
			list.add(lostTimeList);
		}
		List<String> goalLostTimeList = new ArrayList<>();
		//查找损时目标
		double goalLostTime = productionUnitService.queryGoalLostTimeByProductionUnitId(productionUnitId);
		for(@SuppressWarnings("unused") Date now : days) {
			goalLostTimeList.add(decimalFormat.format(goalLostTime));
		}
		modelMap.addAttribute("classes",classesList);
		modelMap.addAttribute("goalLostTimeList",goalLostTimeList);
		modelMap.addAttribute("lostTimeList",list);
		modelMap.addAttribute("days", dayList);
		return modelMap;
	}
	/**
	 * 损时时间表
	 * @param productionUnitId
	 * @return
	 */
	@RequestMapping("/queryLostTimeReasonForProductionUnitAndMonth.do")
	@ResponseBody
	public ModelMap queryLostTimeReasonForProductionUnitAndMonth(Long productionUnitId,String productDate) {
		ModelMap modelMap = new ModelMap();
		//产生一个月的时间
		DateStringUtil util = new DateStringUtil();
		//List<Date> days =util.generateOneMonthDay(util.date2Month(new Date()));
		List<Date> days = util.generateOneMonthDay(productDate);

		List<String> dayList = new ArrayList<>();
		List<String> dates = new ArrayList<>();
		dates.add("");
		for(Date day : days) {
			dayList.add(util.date2DayOfMonth(day));
			dates.add(util.date2MonthOnly(day) +"-" + util.date2DayOfMonth(day));
		}
		ProductionUnit productionUnit = productionUnitService.queryObjById(productionUnitId);
		//存放总损时数(每个班的损时List)
		List<List<String>> list = new ArrayList<>();
		list.add(dates);
		//根据生产单元id查询设备站点
		List<BigInteger> deviceSites = deviceSiteService.queryBottleneckDeviceSiteIdsByProductionUnitId(productionUnitId);
		//查询所有班次
		List<Classes> classesList = classesService.queryAllClassesByproductionUnitCode(productionUnit.getCode());
		for(int i = 0;i<classesList.size();i++) {
			//存放每个班的损时数
			List<String> lostTimeList = new ArrayList<>();
			Classes c = classesList.get(i);
			lostTimeList.add(c.getName());
			for(Date now : days) {
				Integer lostTime = lostTimeRecordService.queryLostTimeForPerDay(c, now,productionUnitId);
				if(deviceSites!=null&&deviceSites.size()>0){
					lostTimeList.add(decimalFormat.format(lostTime*1.0/60/deviceSites.size()));
				}else{
					lostTimeList.add(decimalFormat.format(lostTime*1.0/60/1));
				}

			}
			list.add(lostTimeList);
		}

		List<String> sumList = new ArrayList<>();
		sumList.add("合计");
		for(int i=1;i<list.get(1).size();i++) {
			sumList.add(decimalFormat.format(Double.valueOf(list.get(1).get(i))+Double.valueOf(list.get(2).get(i))));
		}
		list.add(sumList);
		List<String> goalLostTimeList = new ArrayList<>();
		//查找损时目标
		double goalLostTime = productionUnitService.queryGoalLostTimeByProductionUnitId(productionUnitId);
		for(@SuppressWarnings("unused") Date now : days) {
			goalLostTimeList.add(decimalFormat.format(goalLostTime));
		}
		modelMap.addAttribute("classes",classesList);
		modelMap.addAttribute("goalLostTimeList",goalLostTimeList);
		modelMap.addAttribute("lostTimeList",list);
		modelMap.addAttribute("days", dayList);
		return modelMap;
	}

	/**
	 * 按分钟查询实时损时记录
	 * @return
	 */
	@RequestMapping("/queryLostTime4RealTime.do")
	@ResponseBody
	public ModelMap queryLostTime4RealTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		ModelMap modelMap = new ModelMap();
		//产生720分钟时间
		List<Date> minutesList = util.generate720Minutes(new Date());
		List<String> minutes = new ArrayList<>();
		List<Integer> minuteCountList = new ArrayList<>();
		Calendar nowCalendar = Calendar.getInstance();
		Calendar lostTimeCalendar = Calendar.getInstance();
		List<LostTimeRecord> list = lostTimeRecordService.queryLostTime4RealTime();
		for(Date date : minutesList) {
			double sumOfLostTime = 0;
			nowCalendar.setTime(date);
			int nowYear = nowCalendar.get(Calendar.YEAR);
			int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
			int nowDate = nowCalendar.get(Calendar.DATE);
			int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
			int nowMinute = nowCalendar.get(Calendar.MINUTE);
			for(LostTimeRecord record : list) {
				Date lostTimeTime = record.getLostTimeTime();
				if(lostTimeTime != null) {
					lostTimeCalendar.setTime(lostTimeTime);
				}
				int lostTimeYear = lostTimeCalendar.get(Calendar.YEAR);
				int lostTimeMonth = lostTimeCalendar.get(Calendar.MONTH) + 1;
				int lostTimeDate = lostTimeCalendar.get(Calendar.DATE);
				int lostTimeHour = lostTimeCalendar.get(Calendar.HOUR_OF_DAY);
				int lostTimeMinute = lostTimeCalendar.get(Calendar.MINUTE);
				if(nowYear == lostTimeYear &&
						nowMonth == lostTimeMonth &&
						nowDate == lostTimeDate && 
						nowHour == lostTimeHour &&
						nowMinute == lostTimeMinute) {
					Date beginTime = record.getBeginTime();
					Date endTime = record.getEndTime();
					Date now = new Date();
					double lostTime = 0;
					if(beginTime!=null) {
						if(endTime==null) {
							lostTime = (now.getTime() - beginTime.getTime())/1000;
						}else {
							lostTime = (endTime.getTime()-beginTime.getTime())/1000;
						}

						sumOfLostTime += lostTime/60;
					}
				}
			}
			minutes.add(simpleDateFormat.format(date));
			minuteCountList.add((int)sumOfLostTime);
		}

		modelMap.addAttribute("minutes", minutes);
		modelMap.addAttribute("lostTimeCounts", minuteCountList);
		return modelMap;
	}
	/**
	 * 设备运行时间表
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
		//查找当前班次
		Classes c = classesService.queryCurrentClasses();
		//查询当前生产单元下的所有设备站点
		List<DeviceSite> deviceSites = deviceSiteService.queryDeviceSitesByProductionUnitId(productionUnitId);
		//计算总时长
		int total = computeTotalRunningMinutes(c)*(deviceSites==null?0:deviceSites.size());
		titles.add("总时长");
		assistants.add(0);
		minutes.add(total);
		//短停机时长
		int shortHalt = processRecordService.queryShortHalt4ProductionUnit(c,productionUnitId);
		//计算正常运行时长
		int totalLostTime = (lostTimeRecordService.queryLostTime4ProductionUnit(c,productionUnitId)).intValue();
		titles.add("运行时长");
		minutes.add(total - totalLostTime-shortHalt/60);
		assistants.add(totalLostTime + shortHalt/60);

		titles.add("短停机");
		minutes.add(shortHalt/60);
		assistants.add(totalLostTime);
		//停机总时长
		titles.add("停机时长");
		minutes.add(totalLostTime);
		assistants.add(0); 
		//各个损时类别停机时长
		List<Object[]> objs = lostTimeRecordService.queryLostTimePerLosttimeType4ProductionUnit(c,productionUnitId);
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
		modelMap.addAttribute("deviceSiteCount",deviceSites.size());

		return modelMap;
	}
	/**
	 * 计算当前班次在线时长
	 */
	private int computeTotalRunningMinutes(Classes c) {
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
	 * 查找损时类别详情
	 * @param typeName 类别名称(父类别)
	 * @param month 月份
	 * @return
	 */
	@RequestMapping("/queryLostTimeDetail.do")
	@ResponseBody
	public List<Object[]> queryLostTimeDetail(String typeName,int month){
		return lostTimeRecordService.queryLostTimeDetail(typeName, month);
	}
} 
