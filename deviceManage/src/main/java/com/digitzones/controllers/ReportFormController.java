package com.digitzones.controllers;
import com.digitzones.model.*;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.PO_Pomain;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IPO_PomainService;
import com.digitzones.procurement.service.IWarehousingApplicationFormService;
import com.digitzones.service.*;
import com.digitzones.util.DateStringUtil;
import com.digitzones.vo.TraceNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 报表中心(中京)控制器
 */
@RestController
@RequestMapping("reportForm")
public class ReportFormController {
    private static final int DISTANCE = 200;
    private DecimalFormat format = new DecimalFormat("#0.00");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateStringUtil util = new DateStringUtil();
    private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private IProductionUnitService productionUnitService;
    @Autowired
    private IClassesService classService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IDeviceSiteService deviceSiteService;
    @Autowired
    private INGRecordService ngRecordService;
    @Autowired
    private ILostTimeRecordService lostTimeRecordService;
    @Autowired
    private IWorkSheetDetailService workSheetDetailService;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IWorkSheetService workSheetService;
    @Autowired
    private IWarehousingApplicationFormService warehousingApplicationFormService;
    @Autowired
    private IPO_PomainService pomainService;
    @Autowired
    private IPackageCodeService packageCodeService;
    /**
     * 查找不合格品记录汇总(月统计)
     * @param from 生产日期起始
     * @param to 生产日期结束
     * @param productionUnitIds 逗号间隔的生产单元id
     * @return
     */
    @RequestMapping("/queryNgRecordSummaryData.do")
    public ModelMap queryNgRecordSummaryData(String from,String to,String productionUnitIds) throws ParseException {
        ModelMap modelMap = new ModelMap();
        List<Long> productionIdList = new ArrayList<>();
        if(!StringUtils.isEmpty(productionUnitIds)){
           String[] pids =  productionUnitIds.split(",");
           if(pids!=null && pids.length>0){
               for(String pid : pids){
                   productionIdList.add(Long.valueOf(pid));
               }
           }
        }
        Calendar calendar = Calendar.getInstance();
        Date fromDate = null;
        Date toDate = null;
        try {
            if(!StringUtils.isEmpty(from)){
                fromDate = yyyyMMddHHmmss.parse(from+"-01 00:00:00");
            }
            if(!StringUtils.isEmpty(to)){
                toDate = yyyyMMddHHmmss.parse(to+"-01 00:00:00");
                calendar.setTime(toDate);
                calendar.add(Calendar.MONTH,1);
                toDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //根据时间和生产单元查找不合格品总数
        List<String[]> ngRecordList = ngRecordService.queryNgRecordGroupByCategory(fromDate,toDate,productionIdList);
        if(CollectionUtils.isEmpty(ngRecordList)){
            return modelMap;
        }
        List<Object> reportList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        List<String> sumList = new ArrayList<>();
        sumList.add("合计");
        reportList.add(monthList);
        monthList.add("");
       /* List<String> categoryNameList = new ArrayList<>();
        //存储类别名称
        for(String[] ngRecord : ngRecordList){
            categoryNameList.add(ngRecord[0]);
        }*/
       //存放不合格类别和不合格品数的Map
       Map<String,List<String>> map = new HashMap<>();
        for(Object[] ngRecord : ngRecordList){
            List<String> list = new ArrayList<>();
            list.add((String)ngRecord[0]);
            map.put((String)ngRecord[0],list);
        }
        //生成from-->to之间的月份
        List<String> months = util.generateMonthsBetween(from, to);
        if(!CollectionUtils.isEmpty(months)){
            for(String month : months){
                double sum = 0;
                monthList.add(month+" ");
                Date monthDate = yyyyMMddHHmmss.parse(month+"-01 00:00:00");
                List<String[]> ngRecord4MonthList = ngRecordService.queryNgRecord4MonthGroupByCategory(monthDate,productionIdList);
                if(!CollectionUtils.isEmpty(ngRecord4MonthList)){
                    for(int i = 0;i<ngRecord4MonthList.size();i++){
                        Object[] value = ngRecord4MonthList.get(i);
                        String category = (String)value[0];
                        String ngCount = String.valueOf(value[1]);
                        List<String> list = map.get(category);
                        list.add(ngCount);

                        sum += Double.valueOf(ngCount);
                    }
                }else{
                    for(Map.Entry<String,List<String>> entity : map.entrySet()){
                        entity.getValue().add("0");
                    }
                }
                sumList.add(sum+"");
            }
        }

        for(Map.Entry<String,List<String>> entity : map.entrySet()){
            reportList.add(entity.getValue());
        }
        reportList.add(sumList);
        modelMap.addAttribute("ngRecordList",ngRecordList);
        modelMap.addAttribute("reportList",reportList);
       // modelMap.addAttribute("categoryNameList",categoryNameList);
        return modelMap;
    }
    /**
     * 根据生产日期和生产单元查找日产量信息
     * @param productDate 生产日期
     * @param productionUnitId 生产单元编码或名称
     * @return
     */
    @RequestMapping("queryDailyOutputData.do")
    public ModelMap queryDailyOutputData(String productDate,Long productionUnitId){
        if(productionUnitId==null) {
            productionUnitId = 0l;
        }
        ModelMap modelMap = new ModelMap();
        ProductionUnit unit = productionUnitService.queryObjById(productionUnitId);
        if(unit==null) {
            return modelMap;
        }
        //通过产线id查找产量目标
        int goalOutput = unit.getGoalOutput();
        List<String> goalOutputList = new ArrayList<>();
        //查找所有班次
        List<Classes> classesList = classService.queryAllClassesByproductionUnitCode(unit.getCode());
        //班次名称
        List<String> classNameList = new ArrayList<>();
        Date now = new Date();
        Map<String, List<Double>> classOutputMap = new HashMap<>();
        //产生一个月的日期
        List<Date> days = util.generateOneMonthDay(util.date2Month(now));
        if(productDate!=null){
            days = util.generateOneMonthDay(productDate);
        }
        List<String> strDays = new ArrayList<>();
        for(Classes c : classesList) {
            classNameList.add(c.getName());
            List<Double> outputList = new ArrayList<>();
            for(Date d : days) {
                double  count =  jobBookingFormDetailService.queryBottleneckCountByClassesIdAndDay(c, d,productionUnitId);
                outputList.add(count);
            }
            classOutputMap.put(c.getName(), outputList);
        }
        //存放报表数据
        List<Object> reportList = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        dates.add("");
        for(Date d : days) {
            strDays.add(util.date2DayOfMonth(d));
            dates.add(util.date2DayOfMonth(d));
            goalOutputList.add(goalOutput + "");
        }
        reportList.add(dates);
       String[] avgList = new String[days.size()+1];
        String[] sumList = new String[days.size()+1];
        avgList[0] = "平均";
        sumList[0]="合计";
        int i = 1;
        for(Map.Entry<String,List<Double>> entity : classOutputMap.entrySet()){
            List<Object> list = new ArrayList<>();
            String key = entity.getKey();
            list.add(key);
            List<Double> value = entity.getValue();
            for(int j = 0;j<value.size();j++){
                list.add(value.get(j));
                String avg = avgList[j+1];
                if(avg == null){
                    avg = "0";
                }
                avgList[j+1]=(Double.parseDouble(avg)+value.get(j))/i + "";
                String sum = sumList[j+1];
                if(sum == null){
                    sum = "0";
                }
                sumList[j+1]=Double.parseDouble(sum)+value.get(j) + "";
            }
            i++;
            reportList.add(list);
        }
        reportList.add(avgList);
        reportList.add(sumList);
        modelMap.addAttribute("goalOutput", goalOutputList);
        modelMap.addAttribute("classNameList", classNameList);
        modelMap.addAttribute("outputMap", classOutputMap);
        modelMap.addAttribute("days", strDays);
        modelMap.addAttribute("reportList", reportList);
        return modelMap;
    }

    /**
     * 查询oee记录
     * @param productDate
     * @param productionUnitId
     * @return
     */
    @RequestMapping("/queryOeeRecordData.do")
    public ModelMap queryOeeRecordData(String productDate,Long productionUnitId) {
        if(productionUnitId==null) {
            return null;
        }

        if(StringUtils.isEmpty(productDate)){
            productDate = sdf.format(new Date());
        }
        /**-------------显示数据报表定义的数据结构------------------------*/
        List<Object> reportList = new ArrayList<>();
        List<String> reportDaysList = new ArrayList<>();
        List<Object> reportAvgList = new ArrayList<>();
        reportList.add(reportDaysList);
        reportDaysList.add("");
        reportAvgList.add("平均");
        /**-------------显示数据报表定义的数据结构------------------------*/
        ProductionUnit unit = productionUnitService.queryObjById(productionUnitId);
        //根据产线查找目标OEE
        double goalOee =unit.getGoalOee();
        List<String> goalOeeList = new ArrayList<>();
        ModelMap modelMap = new ModelMap();
        //产生一个月的时间
        DateStringUtil util = new DateStringUtil();
        String  dateStr = StringUtils.isEmpty(productDate)?util.date2Month(new Date()):productDate;
        List<Date> days = util.generateOneMonthDay(dateStr);
        List<List<String>> oeeList = new ArrayList<>();
        List<String> dayList = new ArrayList<>();
        for(Date day : days) {
            dayList.add(util.date2DayOfMonth(day));
            reportDaysList.add(util.date2DayOfMonth(day));
        }
        //根据生产单元id查询设备站点
        List<BigInteger> deviceSiteIds =  deviceSiteService.queryBottleneckDeviceSiteIdsByProductionUnitId(productionUnitId);
        Calendar calendar = Calendar.getInstance();
        //查询所有班次
        List<Classes> classesList = classService.queryAllClassesByproductionUnitCode(unit.getCode());
        for(int i = 0;i<classesList.size();i++) {
            Classes c = classesList.get(i);
            List<String> oees = new ArrayList<>();
            List<String> reportClassList = new ArrayList<>();
            reportClassList.add(c.getName());
            for(Date now : days) {
                goalOeeList.add(format.format(goalOee));
                calendar.setTime(now);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                if(!CollectionUtils.isEmpty(deviceSiteIds)) {
                    double oee = 0;
                    for(Object obj : deviceSiteIds) {
                        long dsid = Long.parseLong(String.valueOf(obj));
                        DeviceSite ds = deviceSiteService.queryObjById(dsid);
                        oee +=computeOee4CurrentClass(c,ds,calendar.getTime());
                    }
                    if(oee==0){
                        oees.add(0+"");
                        reportClassList.add(0+"");
                    }else {
                        oees.add(format.format(oee / deviceSiteIds.size() * 100));
                        reportClassList.add(format.format(oee / deviceSiteIds.size() * 100));
                    }
                }else {
                    oees.add(0+"");
                    reportClassList.add(0+"");
                }
            }
            oeeList.add(oees);
            reportList.add(reportClassList);
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
            reportAvgList.add(format.format(sum/oeeList.size()));
        }

        reportList.add(reportAvgList);

        modelMap.addAttribute("classes",classesList);
        modelMap.addAttribute("goalOeeList",goalOeeList);
        modelMap.addAttribute("avgs",avgs);
        modelMap.addAttribute("oeeList", oeeList);
        modelMap.addAttribute("days", dayList);
        modelMap.addAttribute("reportList", reportList);
        return modelMap;
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
     * @param deviceSite 设备站点
     * @return
     */
    public  double computeOee4CurrentClass(Classes currentClass ,DeviceSite deviceSite,Date date) {
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
     * 根据生产时间和生产单元查找总产量信息
     * @return
     * @throws ParseException 
     */
    @RequestMapping("queryOutputSumData.do")
    public ModelMap queryOutputSumData(String from,String to,String productionUnitIds,String cycle) throws ParseException{
    	if(productionUnitIds==null) {
    		productionUnitIds = 0l+"";
    	}
    	ModelMap modelMap = new ModelMap();
    	String[] pids = productionUnitIds.split(",");
    	
    	List<String> times=new ArrayList<>();
    	List<String> time=new ArrayList<>();
    	List<String> productionUnitNameList=new ArrayList<>();
    	Map<String, List<Long>> UnitSumMap = new HashMap<>();
    	if(cycle.equals("year")){
    		time = util.generateYearsBetween(from, to);
    	}else if(cycle.equals("month")){
    		time=util.generateMonthsBetween(from, to);
    	}else if(cycle.equals("week")){
    		time=util.generateWeeksBetween(from, to);
    	}else if(cycle.equals("day")){
    		time=util.generateDaysBetween(from, to);
    	}
    	times.add("");
    	times.addAll(time);
    	for(String pid:pids){
    		ProductionUnit p = productionUnitService.queryObjById(Long.parseLong(pid));
    		productionUnitNameList.add(p.getName());
    		List<Long> sumList = new ArrayList<>();
    		for(int i=0;i<time.size();i++){
    			Date fromDate = util.string2Date(time.get(i)+"-01");
    			Date toDate =null;
    			if(i==time.size()-1){
    				//toDate = util.string2Date(time.get(i)+"-31 23:59:59");
    				if(cycle.equals("month")){
	    				Calendar c = Calendar.getInstance() ;
	    				c.setTime(fromDate);
	    				int m = c.get(Calendar.MONTH) + 2;
	    				int y = c.get(Calendar.YEAR);
	    				if(m>12){
	    					m=1;
	    					y=y+1;
	    				}
	    				toDate = sdf.parse(y+"-"+m+"-01 23:59:59");
    				}else if(cycle.equals("day")){
    					toDate = sdf.parse(time.get(i)+" 23:59:59");
    				}
    			}else
    				toDate = util.string2Date(time.get(i+1)+"-01");
    			Long sum = workSheetDetailService.querySumByProductionUnitId(fromDate,toDate, pid);
    			if(sum==null)
    				sum=0l;
    			sumList.add(sum);
    		}
    		UnitSumMap.put(p.getName(), sumList);
    	}
    	//存放报表数据
    	List<Object> reportList = new ArrayList<>();
    	reportList.add(times);
    	String[] avgList = new String[time.size()+1];
    	String[] sumList = new String[time.size()+1];
    	avgList[0] = "平均";
    	sumList[0]="合计";
    	int i = 1;
    	for(Map.Entry<String,List<Long>> entity : UnitSumMap.entrySet()){
    		List<Object> list = new ArrayList<>();
    		String key = entity.getKey();
    		list.add(key);
    		List<Long> value = entity.getValue();
    		for(int j = 0;j<value.size();j++){
    			list.add(value.get(j));
    			String avg = avgList[j+1];
    			if(avg == null){
    				avg = "0";
    			}
    			avgList[j+1]=format.format((Double.parseDouble(avg)+value.get(j))/i) + "";
    			String sum = sumList[j+1];
    			if(sum == null){
    				sum = "0";
    			}
    			sumList[j+1]=Double.parseDouble(sum)+value.get(j) + "";
    		}
    		i++;
    		reportList.add(list);
    	}
    	reportList.add(avgList);
    	reportList.add(sumList);
    	modelMap.addAttribute("classNameList", productionUnitNameList);
    	modelMap.addAttribute("ngCountMap", UnitSumMap);
    	modelMap.addAttribute("thisMonth", time);
    	modelMap.addAttribute("reportList", reportList);
    	return modelMap;
    }
    /**
     * 根据报工条码反向追溯
     * @param packageCode 包装条码
     * @return List<TraceNode>
     */
    @RequestMapping("reverseTraceByPackageCode.do")
    public TraceNode reverseTraceByPackageCode(String packageCode){
        //条码信息为空
        if(StringUtils.isEmpty(packageCode)){
            return null;
        }

        PackageCode packageCode1 = packageCodeService.queryByProperty("code",packageCode);
        if(packageCode1==null){
            return null;
        }
        //反向追溯的根节点
        TraceNode rootNode = buildTraceNode(packageCode1,100,120);
        queryMaterials(packageCode,rootNode);
        return rootNode;
    }
    /**
     * 根据报工条码反向追溯
     * @param barCode 报工条码
     * @return List<TraceNode>
     */
    @RequestMapping("reverseTraceByBarCode.do")
    public TraceNode reverseTraceByBarCode(String barCode){
        //条码信息为空
        if(StringUtils.isEmpty(barCode)){
            return null;
        }
        //根据条码信息查找报工单箱号条码对象
        BoxBar boxBar = boxBarService.queryObjById(Long.valueOf(barCode));
        if(boxBar==null || !boxBar.getTableName().equals("JobBookingFormDetail")){
            return null;
        }
        //反向追溯的根节点
        TraceNode rootNode = buildTraceNode(boxBar,100,120);
        queryMaterials(barCode,rootNode);
        return rootNode;
    }
    /**
     * 查找领料信息
     * @param barCode
     * @param parentNode
     * @return
     */
    private int queryMaterials(String barCode,TraceNode parentNode){
        int height = parentNode.getY();
        List<TraceNode> childrenNodes = new ArrayList<>();
        parentNode.setChilds(childrenNodes);
        if(parentNode.getType().equals("JobBookingFormDetail")){
            //查找工单
           WorkSheet workSheet =  workSheetService.queryByBarCode(barCode);
           if(workSheet!=null){
                TraceNode workSheetNode = buildTraceNode(workSheet,parentNode.getX(),parentNode.getY()- 100);
                childrenNodes.add(workSheetNode);
           }
        }
        List<BoxBar> children = null;
        //--------------------------------------------------------------------
        if(parentNode.getType().equals("PackageCode")){
            children =  packageCodeService.queryBoxBarByPackageCode(barCode);
        }else{
            children = boxBarService.queryRawMaterialByBoxBar(barCode);
        }
        //--------------------------------------------------------------------
        if(!CollectionUtils.isEmpty(children)){
            int y = parentNode.getY();
            for(BoxBar boxBar : children){
                TraceNode traceNode = buildTraceNode(boxBar,parentNode.getX()+DISTANCE,y);
                childrenNodes.add(traceNode);
                int retHeight = queryMaterials(boxBar.getBarCode()+"",traceNode);
                y=retHeight+200;
            }
            height = y-200;
        }else{
            //查找入库申请单和采购订单
            WarehousingApplicationForm warehousingApplicationForm = warehousingApplicationFormService.queryByBarCode(barCode);
            if(warehousingApplicationForm!=null){
                TraceNode warehousingApplicationNode = buildTraceNode(warehousingApplicationForm,parentNode.getX()+DISTANCE,parentNode.getY());
                childrenNodes.add(warehousingApplicationNode);

                List<TraceNode> poMainList = new ArrayList<>();
                warehousingApplicationNode.setChilds(poMainList);
                //根据入库申请单查询采购订单
                List<PO_Pomain> list = pomainService.queryByWarehousingApplicationFormNoAndBarCode(warehousingApplicationForm.getFormNo(),barCode);
                if(!CollectionUtils.isEmpty(list)){
                    int y = warehousingApplicationNode.getY();
                    for(PO_Pomain po_pomain : list){
                        poMainList.add(buildTraceNode(po_pomain,warehousingApplicationNode.getX()+DISTANCE,y));
                        y+=100;
                    }
                }
            }
        }
        return height;
    }
    /**
     * 构建一个追溯节点对象
     * @param object
     * @param x
     * @param y
     * @return
     */
    private TraceNode buildTraceNode(Object object,int x,int y){
        if(object==null){
            throw new RuntimeException("节点对象不能为空!");
        }
        TraceNode traceNode = new TraceNode();
        if(object instanceof BoxBar){
            BoxBar boxBar = (BoxBar)object;
            traceNode.setId(boxBar.getBarCode()+"");
           traceNode.setText(boxBar.getBarCode()+"");
           traceNode.setExtra(boxBar.getInventoryName()==null?"":boxBar.getInventoryName());
           traceNode.setType(boxBar.getTableName());
           if(boxBar.getTableName().equals("WarehousingApplicationFormDetail")){
               traceNode.setImg("reportForm/imgs/原材料条码.png");
           }
           if(boxBar.getTableName().equals("JobBookingFormDetail")){
               traceNode.setImg("reportForm/imgs/报工条码.png");
           }
        }
        if(object instanceof WorkSheet){
            WorkSheet workSheet = (WorkSheet)object;
            traceNode.setId(workSheet.getNo());
            traceNode.setType("WorkSheet");
            traceNode.setText(workSheet.getNo());
            traceNode.setExtra("");
            traceNode.setImg("reportForm/imgs/工单数据.png");
        }
        if(object instanceof WarehousingApplicationForm){
            WarehousingApplicationForm warehousingApplicationForm = (WarehousingApplicationForm)object;
            traceNode.setId(warehousingApplicationForm.getFormNo());
            traceNode.setType("WarehousingApplicationForm");
            traceNode.setText(warehousingApplicationForm.getFormNo());
            traceNode.setExtra(warehousingApplicationForm.getVendorName()==null?"":warehousingApplicationForm.getVendorName());
            traceNode.setImg("reportForm/imgs/入库单.png");
        }
        if(object instanceof PO_Pomain){
            PO_Pomain po_pomain= (PO_Pomain)object;
            traceNode.setId(po_pomain.getcPOID());
            traceNode.setType("PO_Pomain");
            traceNode.setText(po_pomain.getcPOID());
            traceNode.setExtra(po_pomain.getcVenName()==null?"":po_pomain.getcVenName());
            traceNode.setImg("reportForm/imgs/采购订单.png");
        }

        if(object instanceof PackageCode){
            PackageCode packageCode = (PackageCode)object;
            traceNode.setId(packageCode.getCode());
            traceNode.setType("PackageCode");
            traceNode.setText(packageCode.getCode());
            traceNode.setExtra(packageCode.getBatchNumber()==null?"":packageCode.getBatchNumber());
            traceNode.setImg("reportForm/imgs/包装.png");
        }
        traceNode.setX(x);
        traceNode.setY(y);
        traceNode.setHeight(50);
        traceNode.setWidth(50);
        return traceNode;
    }

    /**
     * 产线级产量
     * @param productionUnitId
     * @return
     */
    @RequestMapping("/queryOutput4ProductionUnit.do")
    @ResponseBody
    public ModelMap queryOutput4ProductionUnit(Long productionUnitId) {
        if(productionUnitId==null) {
            productionUnitId = 0l;
        }
        ModelMap modelMap = new ModelMap();

        ProductionUnit unit = productionUnitService.queryObjById(productionUnitId);
        if(unit==null) {
            return modelMap;
        }
        //通过产线id查找产量目标
        int goalOutput = unit.getGoalOutput();
        List<String> goalOutputList = new ArrayList<>();
        //查找所有班次
        List<Classes> classesList = classService.queryAllClasses();
        //班次名称
        List<String> classNameList = new ArrayList<>();
        Date now = new Date();
        Map<String, List<String>> classOutputMap = new HashMap<>();
        //产生一个月的日期
        List<Date> days = util.generateOneMonthDay(util.date2Month(now));
        List<String> strDays = new ArrayList<>();
        for(Classes c : classesList) {
            classNameList.add(c.getName());
            List<String> outputList = new ArrayList<>();
            for(Date d : days) {
                double  count =  jobBookingFormDetailService.queryBottleneckCountByClassesIdAndDay(c, d,productionUnitId);
                outputList.add(count+"");
            }
            classOutputMap.put(c.getName(), outputList);
        }
        for(Date d : days) {
            strDays.add(util.date2DayOfMonth(d));
            goalOutputList.add(goalOutput + "");
        }

        modelMap.addAttribute("goalOutput", goalOutputList);
        modelMap.addAttribute("classNameList", classNameList);
        modelMap.addAttribute("outputMap", classOutputMap);
        modelMap.addAttribute("days", strDays);
        return modelMap;
    }
}
