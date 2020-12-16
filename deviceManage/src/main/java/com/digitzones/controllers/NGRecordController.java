package com.digitzones.controllers;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.constants.Constant;
import com.digitzones.model.Classes;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Employee;
import com.digitzones.model.NGReason;
import com.digitzones.model.NGRecord;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.User;
import com.digitzones.model.WorkSheet;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IClassesService;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IJobBookingFormService;
import com.digitzones.service.INGReasonService;
import com.digitzones.service.INGRecordService;
import com.digitzones.service.IProcessRecordService;
import com.digitzones.service.IProductionUnitService;
import com.digitzones.service.IUserService;
import com.digitzones.service.IWorkSheetService;
import com.digitzones.util.DateStringUtil;
import com.digitzones.util.JwtTokenUnit;
import com.digitzones.util.StringUtil;
import com.digitzones.vo.NGRecordVO;
import com.digitzones.xml.JobBookingSlipUtil;
import com.digitzones.xml.model.Result;
/**
 * 不合格品记录控制器
 * @author zdq
 * 2018年6月28日
 */
@Controller
@RequestMapping("/ngRecord")
public class NGRecordController {
	private DateStringUtil util = new DateStringUtil();
	private DecimalFormat format = new DecimalFormat("#0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	@Autowired
	private INGRecordService ngRecordService;
	private IDeviceSiteService deviceSiteService;
	private IClassesService classService;
	private IProcessRecordService processRecordService;
	private IProductionUnitService productionUnitService;
	@Autowired
	private IUserService userService;
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private INGReasonService ngReasonService;
	@Autowired
	private IWorkSheetService workSheetService;
	@Autowired
    private JobBookingSlipUtil jobBookingSlipUtil;
	@Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IJobBookingFormService jobBookingFormService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	public void setWorkFlowKeyConfig(WorkFlowKeyConfig workFlowKeyConfig) {
		this.workFlowKeyConfig = workFlowKeyConfig;
	}

	@Autowired
	public void setProductionUnitService(IProductionUnitService productionUnitService) {
		this.productionUnitService = productionUnitService;
	}

	@Autowired
	public void setProcessRecordService(IProcessRecordService processRecordService) {
		this.processRecordService = processRecordService;
	}

	@Autowired
	public void setClassService(IClassesService classService) {
		this.classService = classService;
	}

	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	/**
	 * 根据设备站点id查找不合格品记录
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGRecordByDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryNGRecordByDeviceSiteId(Long deviceSiteId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from NGRecord record where record.deviceSiteId=?0 and record.deleted=?1 order by record.occurDate desc";
		Pager<NGRecord> pager = ngRecordService.queryObjs(hql, page, rows, new Object[] {deviceSiteId,false});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	
	/**
	 * 根据设备站点id查找不合格品记录（搜索）
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGRecordByDeviceSiteIdandSearch.do")
	@ResponseBody
	public ModelMap queryNGRecordByDeviceSiteIdandSearch(Long deviceSiteId,@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		
		String hql = "from NGRecord record where record.deviceSiteId=?0 and record.deleted=?1";
		
		
		String searchText=params.get("searchText");
	 	String searchChange=params.get("searchChange");
	 	String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		String inStatus = params.get("inStatus");
		List<Object> list=new ArrayList<Object>();
		list.add(deviceSiteId);
		list.add(false);
		int i=list.size()-1;
		try {
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
					i++;
					hql+=" and record.occurDate>=?"+i;	
					list.add(sdf.parse(beginDateStr));
			}
			if(endDateStr!=null && !"".equals(endDateStr)) {
				i++;
				hql+=" and record.occurDate<=?"+i;
				list.add(sdf.parse(endDateStr));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(searchText!=null && !"".equals(searchText)&&searchChange!=null && !"".equals(searchChange)) {
			if(searchChange.equals("processingMethod")) {
				if(searchText.equals("报废")) {
					searchText="scrap";
				}
				if(searchText.equals("返修")) {
					searchText="repair";
				}
				if(searchText.equals("让步接收")) {
					searchText="compromise";
				}
			}
			hql+=" and record."+searchChange+" like '%'+?"+(i+1)+"+'%'";
			list.add(searchText);
		}
		if(inStatus!=null && !"".equals(inStatus)) {
			i++;
			if("true".equals(inStatus)){
				hql+=" and record.inWarehouse=?"+i;
				list.add(true);
			}else{
				hql+=" and (record.inWarehouse is null or record.inWarehouse=?"+i+")";
				list.add(false);
			}

		}
		hql+=" order by record.occurDate desc";
		Object[] obj =  list.toArray(new Object[1]);  
		
		
		
		Pager<NGRecord> pager = ngRecordService.queryObjs(hql, page, rows, obj);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}



	/**
	 * 根据设备站点id查找不合格品记录
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGRecordByFilterId.do")
	@ResponseBody
	public ModelMap queryNGRecordByFilterId(Long filterId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		StringBuilder hql = new StringBuilder("from NGRecord record where  record.deleted=?0 ");
		switch(String.valueOf(filterId)) {
		//查询所有
		case "0":{
			break;
		}
		//待审核
		case "1":{
			hql.append(" and record.auditDate is null");
			break;
		}
		//待复核
		case "2":{
			hql.append(" and record.auditDate is not null and record.reviewDate is null");
			break;
		}
		//待确认
		case "3":{
			hql.append("and record.reviewDate is not null and record.confirmDate is null");
			break;
		}
		//已完成
		case "4":{
			hql.append(" and record.confirmDate is not null");
			break;
		}
		}
		Pager<NGRecord> pager = ngRecordService.queryObjs(hql.toString(), page, rows, new Object[] {false});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
  	/**
	 * 添加不合格品记录
	 * @return
	 */
	@RequestMapping("/addNGRecord.do")
	@ResponseBody
	public ModelMap addNGRecord(NGRecord ngRecord,Principal principal) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryObjById(ngRecord.getDeviceSiteId());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}else {
			ngRecord.setDeviceSiteCode(ds.getCode());
			ngRecord.setDeviceSiteName(ds.getName());
		}
		
		if(ngRecord.getNgReasonId()!=null&&ngRecord.getNgReasonId()!=0) {
			NGReason ngReason = ngReasonService.queryObjById(ngRecord.getNgReasonId());
			ngRecord.setNgReason(ngReason.getNgReason());
			ngRecord.setNgReasonCode(ngReason.getNgCode());
		}
		
		//获取当前登录用户
		/*HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Constant.User.LOGIN_USER);*/
		
		User user = userService.queryUserByUsername(principal.getName());
		modelMap.put("businessKey", workFlowKeyConfig.getNgWorkflowKey());
		ngRecordService.addNGRecord(ngRecord, user);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询不合格记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryNGRecordById.do")
	@ResponseBody
	public NGRecordVO queryNGRecordById(Long id) {
		NGRecord record = ngRecordService.queryObjById(id);
		return model2vo(record);
	}

	private NGRecordVO model2vo(NGRecord record) {
		if(record == null) {
			return null;
		}
		NGRecordVO vo = new NGRecordVO();
		vo.setId(record.getId());
		vo.setProcessCode(record.getProcessCode());
		vo.setNo(record.getNo());
		vo.setProcessName(record.getProcessName());
		vo.setProcessId(record.getProcessId());
		vo.setWorkpieceCode(record.getWorkpieceCode());
		vo.setProcessingMethod(record.getProcessingMethod());
		vo.setWorkpieceId(record.getWorkpieceId());
		vo.setWorkSheetId(record.getWorkSheetId());
		vo.setWorkpieceName(record.getWorkpieceName());
		vo.setStoveNumber(record.getStoveNumber());
		vo.setCustomerGraphNumber(record.getCustomerGraphNumber());
		vo.setGraphNumber(record.getGraphNumber());
		vo.setVersion(record.getVersion());
		vo.setNgCount(record.getNgCount());
		vo.setNgReason(record.getNgReason());
		vo.setNgReasonId(record.getNgReasonId());
		vo.setNgTypeId(record.getNgTypeId());
		vo.setNgTypeName(record.getNgTypeName());
		vo.setUnitType(record.getUnitType());
		vo.setBatchNumber(record.getBatchNumber());
		if(record.getOccurDate()!=null) {
			vo.setOccurDate(sdf.format(record.getOccurDate()));
		}
		return vo;
	}
	/**
	 * 编辑不合格品记录
	 * @return
	 */
	@RequestMapping("/updateNGRecord.do")
	@ResponseBody
	public ModelMap updateNGRecord(NGRecord record) {
		ModelMap modelMap = new ModelMap();
		NGRecord ng = ngRecordService.queryObjById(record.getId());
		if(ng.getInWarehouse()!=null&&ng.getInWarehouse()){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "该NG记录已入库，不允许修改！");
			return modelMap;
		}
		ng.setWorkSheetId(record.getWorkSheetId());
		ng.setOccurDate(record.getOccurDate());
		ng.setNo(record.getNo());
		ng.setWorkpieceId(record.getWorkpieceId());
		ng.setWorkpieceCode(record.getWorkpieceCode());
		ng.setWorkpieceName(record.getWorkpieceName());
		ng.setCustomerGraphNumber(record.getCustomerGraphNumber());
		ng.setVersion(record.getVersion());
		ng.setBatchNumber(record.getBatchNumber());
		ng.setStoveNumber(record.getStoveNumber());
		ng.setGraphNumber(record.getGraphNumber());
		ng.setUnitType(record.getUnitType());
		ng.setNgCount(record.getNgCount());
		ng.setNgTypeId(record.getNgTypeId());
		ng.setNgTypeName(record.getNgTypeName());
		if(record.getNgReasonId()!=null&&record.getNgReasonId()!=0) {
			NGReason ngReason = ngReasonService.queryObjById(record.getNgReasonId());
			ng.setNgReason(ngReason.getNgReason());
			ng.setNgReasonCode(ngReason.getNgCode());
			ng.setNgReasonId(record.getNgReasonId());
		}
		ng.setProcessId(record.getProcessId());
		ng.setProcessCode(record.getProcessCode());
		ng.setProcessName(record.getProcessName());
		ng.setProcessingMethod(record.getProcessingMethod());
		ngRecordService.updateObj(ng);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 根据id删除不合格品记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteNGRecord.do")
	@ResponseBody
	public ModelMap deleteNGRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		NGRecord record = ngRecordService.queryObjById(Long.valueOf(id));
		if(record.getInWarehouse()!=null&&record.getInWarehouse()){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该NG记录已入库，不允许删除！");
			return modelMap;
		}
		record.setDeleted(true);
		try {
		ngRecordService.deleteNGRecord(record);
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
	 * 复核
	 * @param id
	 * @return
	 */
	@RequestMapping("/reviewNGRecord.do")
	@ResponseBody
	public ModelMap reviewNGRecord(Long id,String suggestion ,Principal principal) {
		ModelMap modelMap = new ModelMap();
		NGRecord record = ngRecordService.queryObjById(id);

		if(record.getReviewerId()!=null) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该记录已复核!");
			return modelMap;
		}

		/*HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User loginUser = userService.queryUserByUsername(principal.getName());

		if(loginUser!=null) {
			record.setReviewDate(new Date());
			record.setReviewerId(loginUser.getId());
			record.setReviewerName(loginUser.getUsername());
		}
		record.setReviewSuggestion(suggestion);
		modelMap.put(Constant.Workflow.SUGGESTION, suggestion);
		ngRecordService.reviewNGRecord(record, loginUser, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 审核
	 * @param id
	 * @return
	 */
	@RequestMapping("/auditNGRecord.do")
	@ResponseBody
	public ModelMap auditNGRecord(Long id,String suggestion,Principal principal) {
		ModelMap modelMap = new ModelMap();
		NGRecord record = ngRecordService.queryObjById(id);

		if(record.getAuditorId()!=null) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该记录已审核!");
			return modelMap;
		}

		/*HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User loginUser = userService.queryUserByUsername(principal.getName());
		if(loginUser!=null) {
			record.setAuditDate(new Date());
			record.setAuditorId(loginUser.getId());
			record.setAuditorName(loginUser.getUsername());
		}
		record.setAuditSuggestion(suggestion);
		modelMap.put(Constant.Workflow.SUGGESTION, suggestion);
		ngRecordService.auditNGRecord(record, loginUser, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 确认
	 * @param id
	 * @return
	 */
	@RequestMapping("/confirmNGRecord.do")
	@ResponseBody
	public ModelMap confirmNGRecord(Long id,String suggestion,Principal principal) {
		ModelMap modelMap = new ModelMap();
		NGRecord record = ngRecordService.queryObjById(id);

		if(record.getConfirmUserId()!=null) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该记录已确认!");
			return modelMap;
		}

		/*HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User loginUser = userService.queryUserByUsername(principal.getName());
		if(loginUser!=null) {
			record.setConfirmDate(new Date());
			record.setConfirmUserId(loginUser.getId());
			record.setConfirmUsername(loginUser.getUsername());
		}
		record.setConfirmSuggestion(suggestion);
		modelMap.put(Constant.Workflow.SUGGESTION, suggestion);
		ngRecordService.confirmNGRecord(record, loginUser, modelMap);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已确认!");
		return modelMap;
	}

	/**
	 * 查询不合格率：产线级
	 * @return
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	@RequestMapping("/queryNGRecord4ProductionUnit.do")
	@ResponseBody
	public ModelMap queryNGRecord4ProductionUnit(Long productionUnitId) throws NumberFormatException, ParseException {
		ModelMap modelMap = new ModelMap();
		ProductionUnit productionUnit = productionUnitService.queryObjById(productionUnitId);
		//查找当前生产单元的ngGoal值
		double ngGoal =productionUnitService.queryNgGoalById(productionUnitId);
		//查找所有班次
		List<Classes> classList = classService.queryAllClassesByproductionUnitCode(productionUnit.getCode());
		List<String> classNameList = new ArrayList<>();
		List<Date> thisMonth = util.generateOneMonthDay(sdf.format(new Date()));
		Map<String, List<String>> ppmMap = new HashMap<>();
		Map<String, List<Double>> ngCountMap = new HashMap<>();
		List<String> thisMonthList =new ArrayList<>();
		
		for(Classes c : classList) {
			classNameList.add(c.getName());
			List<String> ppmList = new ArrayList<>();
			List<Double> ngCountList = new ArrayList<>();
			for(Date today : thisMonth) {
				//查找当月不合格品数  0：日期  1：不合格品数
				Integer ngCount = processRecordService.queryWorkSheetNGCountPerClasses4ProductionUnit(today, c, productionUnitId);
				//查找生产数 0:日期 1：生产总数
				Integer sumCount = processRecordService.querySumCountPerClasses4ProductionUnit(today, c, productionUnitId);
				ngCountList.add((double)ngCount);
				double ppm = 0;
				if(sumCount!=0 || ngCount != 0) {
					//计算ppm
					ppm = ngCount*1.0/sumCount*1000000;
				}else {
					ppm = 0;
				}
				ppmList.add(format.format(ppm));
			}
			ngCountMap.put(c.getName(), ngCountList);
			ppmMap.put(c.getName(), ppmList);
		}
		
		List<String> goalNgList = new ArrayList<>();
		List<String> dates = new ArrayList<>();
        dates.add("");
		//产生本月天数
		for(Date today:thisMonth){
            dates.add(util.date2MonthOnly(today) +"-" + util.date2DayOfMonth(today));
			thisMonthList.add(util.date2DayOfMonth(today));
			goalNgList.add(ngGoal + "");
		}
		 //存放报表数据
        List<Object> reportList = new ArrayList<>();
        reportList.add(dates);
		String[] avgList = new String[thisMonth.size()+1];
        String[] sumList = new String[thisMonth.size()+1];
        avgList[0] = "平均";
        sumList[0]="合计";
        int i = 1;
        for(Map.Entry<String,List<Double>> entity : ngCountMap.entrySet()){
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
		
		
		/*classNameList.add("目标");
		classNameList.add("PPM");*/
		modelMap.addAttribute("thisMonth", thisMonthList);
		modelMap.addAttribute("classNameList", classNameList);
		modelMap.addAttribute("ngCountMap", ngCountMap);
		modelMap.addAttribute("ngGoalList", goalNgList);
		modelMap.addAttribute("reportList", reportList);
		modelMap.addAttribute("ppmMap", ppmMap);
		return modelMap;
	}
	
	/**
	 * NG记录入库
     * @param id NG记录ID
     * @param warehouseCode 仓库编码
     * @return
     */
	@RequestMapping("/inWarehouseByNGRecord.do")
	@ResponseBody
	public ModelMap inWarehouseByNGRecord(Long id,String warehouseCode,Principal principal){
        ModelMap modelMap=new ModelMap();
        String no="";
        User user = userService.queryUserByUsername(principal.getName());
        NGRecord ngRecord = ngRecordService.queryObjById(id);
        if(ngRecord.getInWarehouse()!=null&&ngRecord.getInWarehouse()){
        	modelMap.addAttribute("msg", "NG记录已经入库！");
        	return modelMap;
        }
        
        String WarehouseNo=ngRecordService.queryMaxWarehouseNo();
        String requestNo = StringUtil.increment(WarehouseNo);
        if(WarehouseNo!=null&&!no.equals(requestNo)) {
            no=requestNo;
            modelMap.addAttribute("changeNo", "提交成功 ，单号已存在，修改单号为"+requestNo+"。");
        }else{
        	no = "BGRK"+ dateFormat.format(new Date()) + "000";
        }
		//仓库信息
		Warehouse warehouse = warehouseService.queryObjById(warehouseCode);
		//设置NG记录信息
        ngRecord.setInWarehouse(true);
        ngRecord.setWarehouseDate(new Date());
        ngRecord.setWarehouseNo(no);
		ngRecord.setInWarehouseCode(warehouseCode);
		ngRecord.setInWarehouseName(warehouse.getcWhName());
		ngRecord.setInWarehouseUserName(user.getEmployee().getName());
      //生成ERP入库单
        WorkSheet workSheet = workSheetService.queryByProperty("no",ngRecord.getNo());
        if(workSheet!=null &&workSheet.getFromErp()!=null&& workSheet.getFromErp()) {
            Map<String, Object> map = new HashMap<>();
            map.put("warehouseCode", warehouseCode);
            map.put("warehouseDate", new Date());
            map.put("warehouseNo", no);
            map.put("detailBatchNumber",ngRecord.getBatchNumber());
            Employee employee = user.getEmployee();
            if (employee != null) {
                map.put("makerName", employee.getName());
                map.put("makerCode", employee.getCode());
            }
            map.put("amount", ngRecord.getNgCount());
            Result result = new Result();
            try{
            	result =  jobBookingSlipUtil.generateJobBookingSlip(workSheet, map);
            }catch(Exception e){
            	modelMap.addAttribute("msg", "ERP连接异常");
				return modelMap;
            }
          if(result.getStatusCode()!=200){
			  modelMap.addAttribute("msg", result.getMessage());
			  return modelMap;
          }
          
        }
        ngRecordService.updateObj(ngRecord);
        modelMap.addAttribute("msg", "NG记录入库成功！");
        return modelMap;
    }
} 
