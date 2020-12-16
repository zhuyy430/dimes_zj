package com.digitzones.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.bo.WorkSheetBo;
import com.digitzones.constants.Constant;
import com.digitzones.dto.WorkSheetDetailDto;
import com.digitzones.dto.WorkSheetDto;
import com.digitzones.model.*;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.model.Mom_recorddetailStatus;
import com.digitzones.procurement.service.IInventoryProcessMappingService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.procurement.service.IMom_orderdetailStatusService;
import com.digitzones.service.*;
import com.digitzones.util.ExcelUtil;
import com.digitzones.util.QREncoder;
import com.digitzones.util.StringUtil;
import com.digitzones.vo.WorkSheetDetailVO;
import com.digitzones.vo.WorkSheetVO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工件管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/workSheet")
public class WorkSheetController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	private static final String FORMNO_PREFIX = "ZJ-";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd");
	private IWorkSheetService workSheetService;
	private IWorkSheetDetailService workSheetDetailService;
	private IDeviceSiteService deviceSiteService;
	private IProcessesService processService;
	@Autowired
	private IJobBookingFormDetailService jobBookingFormDetailService;
	@Autowired
	private IProductionUnitService productionUnitService;
	@Autowired
	private IInventoryProcessMappingService  inventoryProcessMappingService;
	@Autowired
	private IMom_orderdetailStatusService mom_orderdetailStatusService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IWorkpieceProcessDeviceSiteMappingService workpieceProcessDeviceSiteMappingService;
	@Autowired
	private IInventoryService inventoryService;
	@Autowired
	private IWorkpieceProcessParameterMappingService workpieceProcessParameterMappingService;

	private IWorkSheetDetailParametersRecordService parameterRecordService;
	@Autowired
	public void setParameterRecordService(IWorkSheetDetailParametersRecordService parameterRecordService) {
		this.parameterRecordService = parameterRecordService;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}
	@Autowired
	public void setProcessService(IProcessesService processService) {
		this.processService = processService;
	}
	@Autowired
	public void setWorkSheetDetailService(IWorkSheetDetailService workSheetDetailService) {
		this.workSheetDetailService = workSheetDetailService;
	}
	@Autowired
	public void setWorkSheetService(IWorkSheetService workSheetService) {
		this.workSheetService = workSheetService;
	}
	@Autowired
	private IMaterialRequisitionDetailService materialRequisitionDetailService ;
	/**
	 * 生成默认工单单号
	 * @return
	 */
	@RequestMapping("/generateNo.do")
	@ResponseBody
	public String generateNo(){
		Date now = new Date();
		String formNo = workSheetService.queryMaxNoByMakeDocumentDate(now);
		String requestNo = "";
		if(!StringUtils.isEmpty(formNo)){
			requestNo = StringUtil.increment(formNo);
		}else{
			requestNo = FORMNO_PREFIX + format.format(now) + "000";
		}
		return requestNo;
	}
	/**
	 * 根据生产单元id查询工单信息
	 * @return
	 */
	@RequestMapping("/queryWorkSheetsByProductionUnitId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryWorkSheetsByProductionUnitId(Long productionUnitId,WorkSheetDto workSheetDto,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		if(productionUnitId!=null){
			Pager<WorkSheet> pager = workSheetService.queryObjs("select d from WorkSheet d where d.productionUnitId=?0 and deleted=?1 order by d.manufactureDate desc,d.status asc", page, rows, new Object[] {productionUnitId,false});
			List<WorkSheet> workSheets = pager.getData();
			modelMap.addAttribute("rows",workSheets);
			modelMap.addAttribute("total", pager.getTotalCount());
		}else{
			String hql = "from WorkSheet w where 1=1 ";
			List<Object> data = new ArrayList<>();
			int i = 0;
			try {
				if(!StringUtils.isEmpty(workSheetDto.getFrom()) && !workSheetDto.getFrom().trim().equals("")){
					hql += " and w.manufactureDate>=?" + i++;
					data.add(sdfm.parse(workSheetDto.getFrom()+ " 00:00:00"));
				}
				if(!StringUtils.isEmpty(workSheetDto.getTo())&&!workSheetDto.getTo().trim().equals("")){
					hql += " and w.manufactureDate<=?" + i++;
					data.add(sdfm.parse(workSheetDto.getTo() + " 23:59:59"));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(!StringUtils.isEmpty(workSheetDto.getNo())){
				hql += " and w.no like ?" + i++;
				data.add("%"+workSheetDto.getNo()+"%");
			}
			if(!StringUtils.isEmpty(workSheetDto.getInventoryCode())){
				hql += " and w.workPieceCode like ?" + i++;
				data.add("%"+workSheetDto.getInventoryCode()+"%");
			}

			if(!StringUtils.isEmpty(workSheetDto.getBatchNo())){
				hql += " and w.batchNumber like ?" +  i++;
				data.add("%"+workSheetDto.getBatchNo()+"%");
			}

			if(!StringUtils.isEmpty(workSheetDto.getStatus())){
				hql += " and w.status like ?" +  i++;
				data.add("%"+workSheetDto.getStatus()+"%");
			}

			hql += " order by w.manufactureDate desc,w.no desc";
			Pager<WorkSheet> pager = jobBookingFormDetailService.queryObjs(hql,page,rows,data.toArray());
			modelMap.addAttribute("rows", pager.getData());
			modelMap.addAttribute("total",pager.getTotalCount());
		}

		return modelMap;
	}



	/**
	 * 根据工单id查询工单详情(now)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryWorkSheetDetailsByWorkpieceId.do")
	@ResponseBody
	public ModelMap queryWorkSheetDetailsByWorkpieceId(Integer count,String inventoryId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap=new ModelMap();
		return  modelMap;
	}

	/**
	 * 根据工单单号查找工序代码和名称
	 * @param no 工单单号
	 * @return
	 */
	@RequestMapping("/queryProcessCodeAndNameByNo.do")
	@ResponseBody
	public List<Object[]> queryProcessCodeAndNameByNo(String no){
		return inventoryProcessMappingService.queryProcessCodeAndNameByNo(no);
	}

	/**
	 * 根据工序id查询该工序内的其他设备信息
	 * @param processId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryOtherDeviceSitesByProcessId.do")
	@ResponseBody
	public ModelMap queryOtherDeviceSitesByProcessId(Long processId,Long productionUnitId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		HttpSession session = request.getSession();
		List<WorkSheetDetail> list = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		Pager<ProcessDeviceSiteMapping> pager = workSheetDetailService.queryOtherDeviceSitesByProcessId(processId,productionUnitId, page, rows,list);
		modelMap.addAttribute("rows",pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查询所有
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllWorkSheetDetails.do")
	@ResponseBody
	public ModelMap queryAllWorkSheetDetails(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		String hql = "select detail.workSheet.batchNumber,detail.processCode,detail.workSheet.workPieceCode,sum(detail.completeCount) from WorkSheetDetail detail where detail.deleted=?0 and detail.workSheet.deleted=?0 and detail.status=?1 group by detail.workSheet.batchNumber,detail.processCode,detail.workSheet.workPieceCode";
		Pager<WorkSheetDetail> pager = workSheetDetailService.queryObjs(hql, page, rows, new Object[] {false,Constant.WorkSheet.Status.PROCESSING});
		modelMap.addAttribute("rows",pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 为工单中的工序添加设备站点
	 * @param processesId
	 * @param deviceSiteIds
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addDeviceSite4Processes.do")
	@ResponseBody
	public ModelMap addDeviceSite4Processes(Long processesId,String deviceSiteIds,HttpServletRequest request) throws CloneNotSupportedException {
		HttpSession session = request.getSession();
		List<WorkSheetDetail> list = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		ModelMap modelMap = new ModelMap();
		if(deviceSiteIds!=null) {
			if(deviceSiteIds.contains("[")) {
				deviceSiteIds = deviceSiteIds.replace("[", "");
			}
			if(deviceSiteIds.contains("]")) {
				deviceSiteIds = deviceSiteIds.replace("]", "");
			}

			WorkSheetDetail d = null;
			WorkSheetDetail exist  = null;
			//删除工序中设备站点为空的项
			for(WorkSheetDetail detail : list) {
				if(detail.getProcessId().equals(processesId) ) {
					exist = detail;
					if(detail.getDeviceSiteId()==null) {
						d = detail;
						break;
					}
				}
			}
			if(d != null) {
				list.remove(d);
			}
			long detailId = 1;
			Processes c = processService.queryObjById(processesId);
			String[] idArray = deviceSiteIds.split(",");
			for(int i = 0;i<idArray.length;i++) {
				DeviceSite deviceSite = deviceSiteService.queryObjById(Long.valueOf(idArray[i]));
				WorkSheetDetail detail = new WorkSheetDetail();
				detail.setDeviceCode(deviceSite.getDevice().getCode());
				detail.setDeviceName(deviceSite.getDevice().getName());
				detail.setDeviceSiteId(deviceSite.getId());
				detail.setDeviceSiteCode(deviceSite.getCode());
				detail.setDeviceSiteName(deviceSite.getName());
				detail.setProcessId(c.getId());
				detail.setProcessCode(c.getCode());
				detail.setParameterSource(deviceSite.getDevice().getParameterValueType());
				detail.setProcessName(c.getName());
				detail.setId(detailId++);
				if(exist!=null && exist.getParameterRecords()!=null &&exist.getParameterRecords().size()>0) {
					Set<WorkSheetDetailParametersRecord> records = new HashSet<>();
					Long id = new Date().getTime();
					for(WorkSheetDetailParametersRecord record : exist.getParameterRecords()) {
						WorkSheetDetailParametersRecord r = (WorkSheetDetailParametersRecord) record.clone();
						r.setId(id++);
						records.add(r);
					}
					detail.setParameterRecords(records);
				}
				list.add(detail);

				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 更新工单
	 * @param workSheet
	 * @return
	 */
	@RequestMapping("/updateWorkSheet.do")
	@ResponseBody
	public ModelMap updateWorkSheet(String details,String workSheet) {
		ModelMap modelMap = new ModelMap();
		JSONArray jsonArray = JSONArray.parseArray(details);
		JSONObject jsonObject = JSONObject.parseObject(workSheet);
		WorkSheet ws = workSheetService.queryObjById(jsonObject.getLong("id"));
		ws.setBatchNumber(jsonObject.getString("batchNumber"));
		ws.setProductCount(jsonObject.getIntValue("productCount"));
		//ws.setStatus(jsonObject.getString("status"));
		ws.setNote(jsonObject.getString("note"));
		ws.setProductionUnitCode(jsonObject.getString("productionUnitCode"));
		ws.setProductionUnitName(jsonObject.getString("productionUnitName"));
		ws.setProductionUnitId(jsonObject.getLong("productionUnitId"));
		ws.setStoveNumber(jsonObject.getString("stoveNumber"));
		if(jsonObject.getString("manufactureDate")!=null &&!"".equals(jsonObject.getString("manufactureDate"))) {
			try {
				ws.setManufactureDate(sdf.parse(jsonObject.getString("manufactureDate")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//构建工单 详情数组
		List<WorkSheetDetail> detailsList = new ArrayList<>();
		for(int i = 0;i<jsonArray.size();i++) {
			JSONObject jo = jsonArray.getJSONObject(i);
			WorkSheetDetail detail = new WorkSheetDetail();
			detail.setId(jo.getLong("id"));
			detail.setCompleteCount(jo.getIntValue("completeCount"));
			detail.setStatus(jo.getString("status"));
			detail.setNote(jo.getString("note"));
			detail.setProductionCount(jo.getIntValue("productionCount"));
			detail.setScrapCount(jo.getIntValue("scrapCount"));
			detail.setRepairCount(jo.getIntValue("repairCount"));
			detail.setReportCount(jo.getIntValue("reportCount"));
			detail.setUnqualifiedCount(jo.getIntValue("unqualifiedCount"));
			detail.setQualifiedCount(jo.getIntValue("qualifiedCount"));

			detailsList.add(detail);
		}

		workSheetService.updateWorkSheetAndWorkSheetDetails(ws, detailsList);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","操作完成!");
		return modelMap;
	}
	/**
	 * 更新工单详情
	 * @return
	 */
	@RequestMapping("/updateWorkSheetDetail.do")
	@ResponseBody
	public ModelMap updateWorkSheetDetail(WorkSheetDetailDto workSheetDetailDto) {
		ModelMap modelMap = new ModelMap();
		WorkSheetDetail detail = workSheetDetailService.queryObjById(workSheetDetailDto.getId());
		detail.setRepairCount(workSheetDetailDto.getRepairCount());
		detail.setReportCount(workSheetDetailDto.getReportCount());
		detail.setQualifiedCount(workSheetDetailDto.getQualifiedCount());
		detail.setUnqualifiedCount(workSheetDetailDto.getUnqualifiedCount());
		detail.setScrapCount(workSheetDetailDto.getScrapCount());
		detail.setStatus(workSheetDetailDto.getStatus());
		detail.setNote(workSheetDetailDto.getNote());
		detail.setCompleteCount(workSheetDetailDto.getCompleteCount());
		detail.setProductionCount(workSheetDetailDto.getProductionCount());

		workSheetDetailService.updateObj(detail);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","操作完成!");
		return modelMap;
	}
	/**
	 * 更新工单详情
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateWorkSheetDetailInMemory.do")
	@ResponseBody
	public ModelMap updateWorkSheetDetailInMemory(WorkSheetDetailDto workSheetDetailDto,HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<WorkSheetDetail> list = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		ModelMap modelMap = new ModelMap();
		for(WorkSheetDetail detail:list) {
			if(detail.getId() == workSheetDetailDto.getId()) {
				detail.setRepairCount(workSheetDetailDto.getRepairCount());
				detail.setReportCount(workSheetDetailDto.getReportCount());
				detail.setQualifiedCount(workSheetDetailDto.getQualifiedCount());
				detail.setUnqualifiedCount(workSheetDetailDto.getUnqualifiedCount());
				detail.setScrapCount(workSheetDetailDto.getScrapCount());
				detail.setStatus(workSheetDetailDto.getStatus());
				detail.setNote(workSheetDetailDto.getNote());
				detail.setCompleteCount(workSheetDetailDto.getCompleteCount());
				detail.setProductionCount(workSheetDetailDto.getProductionCount());
			}
		}
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","操作完成!");
		return modelMap;
	}
	/**
	 * 根据工单详情查找工单参数记录
	 * @param workSheetDetailId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryParameterRecordsByWorkSheetDetailId.do")
	@ResponseBody
	public ModelMap queryParameterRecordsByWorkSheetDetailId(Long workSheetDetailId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from WorkSheetDetailParametersRecord wsdpr where wsdpr.workSheetDetail.id=?0";
		@SuppressWarnings("unchecked")
		Pager<WorkSheetDetailParametersRecord> pager = parameterRecordService.queryObjs(hql, page, rows, new Object[] {workSheetDetailId});
		List<WorkSheetDetailParametersRecord> result = pager.getData();
		result.sort((h1, h2) -> h1.getParameterCode().compareTo(h2.getParameterCode()));
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据工单id查询工单详情
	 * @param workSheetId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryWorkSheetDetailByWorkSheetId.do")
	@ResponseBody
	public ModelMap queryWorkSheetDetailByWorkSheetId(Long workSheetId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		String hql = "from WorkSheetDetail wsd where wsd.workSheet.id=?0";
		@SuppressWarnings("unchecked")
		Pager<WorkSheetDetail> pager = workSheetDetailService.queryObjs(hql, page, rows, new Object[] {workSheetId});
		List<WorkSheetDetailVO> vos = new ArrayList<>();
		for(WorkSheetDetail detail : pager.getData()) {
			vos.add(workSheetDetail2VO(detail));
		}
		modelMap.addAttribute("rows",vos);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 根据工单id查询工单详情
	 * @param workSheetId
	 * @return
	 */
	@RequestMapping("/queryWorkSheetDetailByWorkSheetId4Print.do")
	@ResponseBody
	public List<WorkSheetDetailVO> queryWorkSheetDetailByWorkSheetId4Print(Long workSheetId){
		List<WorkSheetDetail> list = workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(workSheetId);
		List<WorkSheetDetailVO> vos = new ArrayList<>();
		for(WorkSheetDetail detail : list) {
			vos.add(workSheetDetail2VO(detail));
		}
		return vos;
	}
	private WorkSheetDetailVO workSheetDetail2VO(WorkSheetDetail detail) {
		if(detail == null) {
			return null;
		}
		WorkSheetDetailVO vo = new WorkSheetDetailVO();
		vo.setId(detail.getId());
		vo.setCompleteCount(detail.getCompleteCount());
		vo.setFirstReport(detail.getFirstReport());
		vo.setReportCount(detail.getReportCount());
		vo.setCompromiseCount(detail.getCompromiseCount());
		vo.setRepairCount(detail.getRepairCount());
		vo.setQualifiedCount(detail.getQualifiedCount());
		vo.setUnqualifiedCount(detail.getUnqualifiedCount());
		vo.setScrapCount(detail.getScrapCount());
		vo.setStatus(detail.getStatus());
		vo.setNote(detail.getNote());
		vo.setCompleteCount(detail.getCompleteCount());
		vo.setProductionCount(detail.getProductionCount());
		if(detail.getDeleted()!=null) {
			vo.setDeleted(detail.getDeleted()?"Y":"N");
		}
		if(detail.getWorkSheet()!=null) {
			vo.setWorkSheetId(detail.getWorkSheet().getId());
			vo.setWorkSheetNo(detail.getWorkSheet().getNo());
		}
		vo.setDeviceCode(detail.getDeviceCode());
		vo.setDeviceName(detail.getDeviceName());
		vo.setDeviceSiteCode(detail.getDeviceSiteCode());
		vo.setDeviceSiteId(detail.getDeviceSiteId());
		vo.setDeviceSiteName(detail.getDeviceSiteName());
		vo.setProcessId(detail.getProcessId());
		vo.setProcessCode(detail.getProcessCode());
		vo.setProcessName(detail.getProcessName());
		vo.setParameterSource(detail.getParameterSource());
		vo.setProcessRoute(detail.getProcessRoute());
		return vo;
	}
	/**
	 * 根据id查询工单
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryWorkSheetById.do")
	@ResponseBody
	public WorkSheetVO queryWorkSheetById(Long id) {
		return model2VO(workSheetService.queryObjById(id));
	}
	/**
	 * 根据id查询工单
	 * @return
	 */
	@RequestMapping("/queryWorkSheetByNo.do")
	@ResponseBody
	public WorkSheetVO queryWorkSheetByNo(String No,HttpServletRequest request) {
		WorkSheet workSheet = workSheetService.queryByProperty("no",No);
		if(workSheet!=null){
			String qrCodeUrl = generateQrCodeUrl(request,workSheet);
			WorkSheetVO vo = model2VO(workSheet);
			vo.setQrCodeUrl(qrCodeUrl);
			/**报工总数*/
			Double sumOfAllJobBooking=0.d;
			/**入库总数*/
			Double sumOfAllInWarehouse=0.d;
			List<JobBookingFormDetail> list=jobBookingFormDetailService.queryByWorksheetNo(No);
			for(JobBookingFormDetail jbfd:list){
				if(jbfd.getAmountOfJobBooking()!=null){
					sumOfAllJobBooking=sumOfAllJobBooking+jbfd.getAmountOfJobBooking();
				}
				if(jbfd.getAmountOfInWarehouse()!=null){
					sumOfAllInWarehouse=sumOfAllInWarehouse+jbfd.getAmountOfInWarehouse();
				}
			}
			vo.setSumOfAllJobBooking(sumOfAllJobBooking);
			vo.setSumOfAllInWarehouse(sumOfAllInWarehouse);
			Inventory inventory=inventoryService.queryByProperty("code",workSheet.getWorkPieceCode());
			vo.setDefaultWarehouseCode(inventory.getcDefWareHouse());
			return vo;
		}else{
			return null;
		}

	}

	/**
	 * 生成工单二维码
	 * @param request
	 * @param workSheet
	 * @return
	 */
	private String generateQrCodeUrl(HttpServletRequest request, WorkSheet workSheet) {
		String baseUrl = request.getServletContext().getRealPath("/");
		return new QREncoder().generateQR(workSheet.getNo(),baseUrl,"qrCodes/workSheet");
	}

	private WorkSheetVO model2VO(WorkSheet workSheet) {
		if(workSheet == null) {
			return null;
		}
		WorkSheetVO vo = new WorkSheetVO();
		vo.setId(workSheet.getId());
		vo.setGraphNumber(workSheet.getGraphNumber());
		vo.setNo(workSheet.getNo());
		vo.setBatchNumber(workSheet.getBatchNumber());
		vo.setNote(workSheet.getNote());
		vo.setWorkSheetType(workSheet.getWorkSheetType());
		vo.setRepairWorkSheetNo(workSheet.getRepairWorkSheetNo());
		if(workSheet.getCompleteTime()!=null) {
			vo.setCompleteTime(sdf.format(workSheet.getCompleteTime()));
		}
		if(workSheet.getMakeDocumentDate()!=null) {
			vo.setMakeDocumentDate(sdf.format(workSheet.getMakeDocumentDate()));
		}
		if(workSheet.getManufactureDate()!=null) {
			vo.setManufactureDate(sdf.format(workSheet.getManufactureDate()));
		}
		vo.setDeleted(workSheet.getDeleted()?"Y":"N");
		vo.setWorkPieceName(workSheet.getWorkPieceName());
		vo.setWorkPieceCode(workSheet.getWorkPieceCode());
		vo.setStatus(workSheet.getStatus());
		vo.setStoveNumber(workSheet.getStoveNumber());
		vo.setProductCount(workSheet.getProductCount());
		vo.setProductionUnitCode(workSheet.getProductionUnitCode());
		vo.setProductionUnitName(workSheet.getProductionUnitName());
		vo.setProductionUnitId(workSheet.getProductionUnitId());
		vo.setUnitType(workSheet.getUnitType());
		vo.setDocumentMaker(workSheet.getDocumentMaker());
		vo.setDefine33(workSheet.getDefine33());
		vo.setMoallocateInvcode(workSheet.getMoallocateInvcode());
		vo.setMoallocateQty(workSheet.getMoallocateQty());
		vo.setLotNo(workSheet.getLotNo());
		vo.setMoDId(workSheet.getMoDId());
		vo.setDepartmentCode(workSheet.getDepartmentCode());
		vo.setDepartmentName(workSheet.getDepartmentName());
		vo.setFromErp(workSheet.getFromErp());
		vo.setLocationCodes(workSheet.getLocationCodes());
		return vo;
	}
	/**
	 * 添加工单
	 * @param workSheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addWorkSheet.do")
	@ResponseBody
	public ModelMap addWorkSheet(WorkSheet workSheet,HttpServletRequest request,Principal principal) {
		HttpSession session = request.getSession();
		ModelMap modelMap = new ModelMap();
		Date now = new Date();
		String formNo = workSheetService.queryMaxNoByMakeDocumentDate(now);
		if(!StringUtils.isEmpty(formNo)&&formNo.equals(workSheet.getNo())){
			formNo = StringUtil.increment(formNo);
			workSheet.setNo(formNo);
			modelMap.addAttribute("changeNoMsg","该单号已存在，修改单号为"+formNo);
		}
		List<WorkSheetDetail> workSheetDetails=new ArrayList<>();
		workSheetDetailService.buildWorkSheetDetailListInMemoryByWorkpieceId(workSheet.getProductCount(),workSheet.getWorkPieceCode(),workSheet.getProductionUnitCode(),workSheetDetails);

		if(CollectionUtils.isEmpty(workSheetDetails)){
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","缺少工序流转信息，保存失败!");
			return modelMap;
		}
		User user = userService.queryByProperty("username", principal.getName());
		Employee employee = user.getEmployee();
		workSheet.setDocumentMaker(employee==null?"":employee.getName());
		workSheet.setMakeDocumentDate(new Date());
		if(workSheet.getId()!=null){
			workSheetService.updateObj(workSheet);
			session.setAttribute("WorkSheetNo",workSheet.getNo());
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","变更成功!");
			return modelMap;
		}
		workSheetService.addWorkSheet(workSheet,workSheetDetails,false);
		session.setAttribute("WorkSheetNo",workSheet.getNo());
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","添加成功!");
		return modelMap;
	}
	/**
	 * 修改工单
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updataWorkSheet.do")
	@ResponseBody
	public ModelMap updataWorkSheet(WorkSheet workSheet,HttpServletRequest request,Principal principal) {
		HttpSession session = request.getSession();
		ModelMap modelMap = new ModelMap();
		WorkSheet w=workSheetService.queryObjById(workSheet.getId());
		System.out.println(!w.getStatus().equals(Constant.WorkSheet.Status.PLAN));
		/*if(!w.getStatus().equals(Constant.WorkSheet.Status.PLAN)){
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","该工单已开工，无法修改!");
			return modelMap;
		}*/
		User user = userService.queryByProperty("username", principal.getName());
		Employee employee = user.getEmployee();

		//workSheet.setDocumentMaker(employee==null?"":employee.getName());
		//workSheet.setMakeDocumentDate(new Date());


		w.setDocumentMaker(employee==null?"":employee.getName());
		w.setMakeDocumentDate(new Date());
		w.setProductCount(workSheet.getProductCount());
		w.setBatchNumber(workSheet.getBatchNumber());
		w.setStoveNumber(workSheet.getStoveNumber());
		w.setDefine33(workSheet.getDefine33());
		w.setMoallocateInvcode(workSheet.getMoallocateInvcode());
		w.setMoallocateQty(workSheet.getMoallocateQty());
		w.setLotNo(workSheet.getLotNo());
		w.setLocationCodes(workSheet.getLocationCodes());

		workSheetService.updateObj(w);
		List<WorkSheetDetail> list=workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(workSheet.getId());
		Set<Long> roteList=new HashSet<>();
		for (WorkSheetDetail detail:list){
			int index=1;
			for(WorkSheetDetail d:list){
				if(d.getProcessId().equals(detail.getProcessId())&&d.getProcessRoute().equals(detail.getProcessRoute())){
					index++;
				}
			}
			int avgCount = 0;
			int yuShu = 0;
			if(  workSheet.getProductCount()!=0) {
				avgCount = workSheet.getProductCount()/(index-1);
				yuShu = workSheet.getProductCount()%(index-1);
			}
			detail.setNote(workSheet.getNote());
			if(!roteList.contains(detail.getProcessRoute())) {
				detail.setProductionCount(avgCount+yuShu);
			}else {
				detail.setProductionCount(avgCount);
			}
			workSheetDetailService.updateObj(detail);
			roteList.add(detail.getProcessRoute());
		}
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","修改成功!");
		return modelMap;
	}
	/**
	 * 删除工单
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteWorkSheet.do")
	@ResponseBody
	public ModelMap deleteWorkSheet(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		WorkSheet workSheet = workSheetService.queryObjById(Long.valueOf(id));
		workSheet.setDeleted(true);
		if(workSheet.getFromErp()){
			mom_orderdetailStatusService.deleteObj(workSheet.getMoDId().toString());
		}
		workSheetService.deleteObj(workSheet.getId());
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 清空session
	 * @param session
	 */
	@RequestMapping("/clearSession.do")
	@ResponseBody
	public void clearSession(HttpSession session){
		session.removeAttribute("WorkSheetNo");
	}
	/**
	 * 开工
	 * @param id 工单id
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws NumberFormatException
	 */
	@RequestMapping("/start.do")
	@ResponseBody
	public ModelMap start(String id){
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		WorkSheet workSheet = workSheetService.queryObjById(Long.valueOf(id));
		ProductionUnit unit = productionUnitService.queryByProperty("code",workSheet.getProductionUnitCode()) ;
		ModelMap modelMap = new ModelMap();
		if(!unit.getAllowMultiWorkSheetRunning()) {
			//查找该工单下是否存在正在开工的设备站点，如果存在，则不允许开工，否则，可以开工
			List<WorkSheetDetail> workSheetDetailList = workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(Long.valueOf(id));
			if (!CollectionUtils.isEmpty(workSheetDetailList)) {
				for (WorkSheetDetail workSheetDetail : workSheetDetailList) {
					String deviceSiteCode = workSheetDetail.getDeviceSiteCode();
					List<WorkSheetDetail> list = workSheetDetailService.queryWorkSheetDetailByDeviceSiteCode(deviceSiteCode);
					for (WorkSheetDetail detail : list) {
						if (detail.getStatus().equals(Constant.WorkSheet.Status.PROCESSING)) {
							modelMap.addAttribute("statusCode", 300);
							modelMap.addAttribute("title", "操作提示");
							modelMap.addAttribute("message", "该工单下存在正在执行的设备站点，无法开工!");
							return modelMap;
						}
					}
				}
			} else {
				modelMap.addAttribute("statusCode", 300);
				modelMap.addAttribute("title", "操作提示");
				modelMap.addAttribute("message", "该工单下没有执行设备!");
				return modelMap;
			}
		}

		//已开工
		if(Constant.WorkSheet.Status.PROCESSING.equals(workSheet.getStatus())) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工单已处于开工状态!");
			return modelMap;
		}
		//已完工
		/*if(Constant.WorkSheet.Status.COMPLETE.equals(workSheet.getStatus())) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工单已完工!");
			return modelMap;
		}*/
		workSheetService.start(Long.valueOf(id));
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已开工!");
		return modelMap;
	}
	/**
	 * 停工
	 * @param id
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws NumberFormatException
	 */
	@RequestMapping("/stop.do")
	@ResponseBody
	public ModelMap end(String id){
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		WorkSheet workSheet = workSheetService.queryObjById(Long.valueOf(id));
		//已开工
		if(Constant.WorkSheet.Status.STOP.equals(workSheet.getStatus())||Constant.WorkSheet.Status.COMPLETE.equals(workSheet.getStatus())) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工单已处于停工或完工状态!");
			return modelMap;
		}
		workSheetService.stop(Long.valueOf(id));
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已停工!");
		return modelMap;
	}

	/**
	 * 完工
	 */
	@RequestMapping("/over.do")
	@ResponseBody
	public ModelMap over(String id,Principal principal){
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		WorkSheet workSheet = workSheetService.queryObjById(Long.valueOf(id));
		//已开工
		if(Constant.WorkSheet.Status.STOP.equals(workSheet.getStatus())||Constant.WorkSheet.Status.COMPLETE.equals(workSheet.getStatus())) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工单已处于停工或完工状态!");
			return modelMap;
		}
		User user = userService.queryUserByUsername(principal.getName());
		workSheetService.updateOver(Long.valueOf(id),user);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已完工!");
		return modelMap;
	}

	/**
	 * 根据工序id查找工单详情参数
	 * @param processId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryParametersByParameterRecordId.do")
	@ResponseBody
	public List<WorkSheetDetailParametersRecord> queryParametersByParameterRecordId(Long processId,Long deviceSiteId,HttpServletRequest request){
		HttpSession session = request.getSession();
		List<WorkSheetDetail> list = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		for(WorkSheetDetail detail :list) {
			if(detail.getProcessId().equals(processId) && detail.getDeviceSiteId()==deviceSiteId) {
				List<WorkSheetDetailParametersRecord> result = new ArrayList<>(detail.getParameterRecords());
				result.sort((h1, h2) -> h1.getParameterCode().compareTo(h2.getParameterCode()));
				return result;
			}
		}
		return new ArrayList<>();
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateParameterRecord.do")
	@ResponseBody
	public void updateParameterRecord(Long id,Float upLine,Float lowLine,Float standardValue,HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<WorkSheetDetail> list = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		for(WorkSheetDetail detail : list) {
			Set<WorkSheetDetailParametersRecord> recordSet = detail.getParameterRecords();
			if(recordSet!=null) {
				for(WorkSheetDetailParametersRecord pr : recordSet) {
					if(pr.getId().equals(id)) {
						pr.setLowLine(lowLine);
						pr.setUpLine(upLine);
						pr.setStandardValue(standardValue);
						detail.setFirstReport("已填写");
					}
				}
			}
		}
	}
	@RequestMapping("/updateWorkSheetDetailParameterRecord.do")
	@ResponseBody
	public void updateWorkSheetDetailParameterRecord(Long id,Float upLine,Float lowLine,Float standardValue) {
		WorkSheetDetailParametersRecord  record = parameterRecordService.queryObjById(id);
		record.setUpLine(upLine);
		record.setLowLine(lowLine);
		record.setStandardValue(standardValue);
		parameterRecordService.updateObj(record);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/deleteWorkSheetDetailInMemory.do")
	@ResponseBody
	public ModelMap deleteWorkSheetDetailInMemory(String id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<WorkSheetDetail> list = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		WorkSheetDetail wd = null;
		for(WorkSheetDetail detail:list) {
			if(detail.getId()==Long.valueOf(id)) {
				wd = detail;
				break;
			}
		}

		if(wd!=null) {
			list.remove(wd);
		}

		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "删除成功！");
		return modelMap;
	}
	@RequestMapping("/querySessionWorkSheetDetail.do")
	@ResponseBody
	public List<WorkSheetDetail> querySessionWorkSheetDetail(HttpServletRequest request){
		HttpSession session = request.getSession();
		List<WorkSheetDetail> workSheetDetails = (List<WorkSheetDetail>) session.getAttribute(Constant.WORKSHEETDETAIL);
		return workSheetDetails;
	}

	/**
	 * 根据id删除工单详情
	 * @return
	 */
	@RequestMapping("/deleteWorkSheetDetail.do")
	@ResponseBody
	public ModelMap deleteWorkSheetDetail(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();

		workSheetDetailService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "删除成功！");


		return modelMap;
	}
	/**
	 * 根据站点id查找工单
	 * @param deviceSiteId
	 * @return
	 */
	@RequestMapping("/queryWorkSheetByDeviceSiteId.do")
	@ResponseBody
	public List<WorkSheet> queryWorkSheetByDeviceSiteId(Long deviceSiteId,String q){
		if(q==null ||"".equals(q.trim())) {
			return workSheetService.queryWorkSheetsByDeviceSiteId(deviceSiteId);
		}else {
			return workSheetService.queryWorkSheetsByDeviceSiteIdAndConditions(deviceSiteId, q);
		}
	}
	/**
	 * 根据站点id查找工单
	 * @param deviceSiteId
	 * @return
	 */
	@RequestMapping("/queryProcessingWorkSheetByDeviceSiteId.do")
	@ResponseBody
	public WorkSheet queryProcessingWorkSheetByDeviceSiteId(Long deviceSiteId){
			return workSheetService.queryRunningWorkSheetByDeviceSiteId(deviceSiteId);
	}

	/**
	 * 根据工单id和设备站点id查找工单详情
	 * @param workSheetId
	 * @param deviceSiteId
	 * @return
	 */
	@RequestMapping("/queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId.do")
	@ResponseBody
	public List<WorkSheetDetail> queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId(Long workSheetId,Long deviceSiteId){
		return workSheetDetailService.queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId(workSheetId, deviceSiteId);
	}

	/**
	 * 将ids表示的订单详情存入session
	 * @param ids
	 * @param session
	 */
	@RequestMapping("/addWorkSheets2Session.do")
	@ResponseBody
	public void addWorkSheets2Session(String ids ,String classCode,String className,HttpSession session){
		ids = StringUtil.remove(ids,"[");
		ids = StringUtil.remove(ids,"]");
		ids = StringUtil.remove(ids,"\"");
		List<Long> idsList = new ArrayList<>();
		if(!StringUtils.isEmpty(ids)){
			String[] idsArray = ids.split(",");
			if(idsArray!=null&&idsArray.length>0){
				for(String id : idsArray){
					idsList.add(Long.parseLong(id));
				}
			}
		}
		//根据id查找工单详情
		List<WorkSheetDetail> detailsList = workSheetDetailService.queryByIds(idsList);
		//从session中取出报工单详情列表
		List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JobBookingFormDetailController.JOB_BOOKING_LIST_NAME);
		if(list == null){
			list = new ArrayList<>();
			session.setAttribute(JobBookingFormDetailController.JOB_BOOKING_LIST_NAME,list);
			for(WorkSheetDetail detail:detailsList){
				JobBookingFormDetail d = copyProperties(detail);
				d.setClassCode(classCode);
				d.setClassName(className);
				list.add(d);
			}
		}else{
			for(WorkSheetDetail detail:detailsList){
				boolean isExist = false;
				for(JobBookingFormDetail formDetail : list){
					if(String.valueOf(detail.getId()).equals(formDetail.getId())){
						isExist = true;
						break;
					}
				}
				if(!isExist){
					JobBookingFormDetail d = copyProperties(detail);
					d.setClassCode(classCode);
					d.setClassName(className);
					list.add(d);
				}
			}
		}
	}
	/**
	 * 属性拷贝
	 * @param detail
	 * @return
	 */
	private JobBookingFormDetail copyProperties(WorkSheetDetail detail){
		JobBookingFormDetail formDetail = new JobBookingFormDetail();
		formDetail.setId(detail.getId()+"");
		formDetail.setNo(detail.getWorkSheet().getNo());
		formDetail.setInventoryName(detail.getWorkSheet().getWorkPieceName());
		formDetail.setInventoryCode(detail.getWorkSheet().getWorkPieceCode());
		formDetail.setSpecificationType(detail.getWorkSheet().getUnitType());
		formDetail.setProcessCode(detail.getProcessCode());
		formDetail.setProcessName(detail.getProcessName());
		formDetail.setDeviceSiteCode(detail.getDeviceSiteCode());
		formDetail.setDeviceSiteName(detail.getDeviceSiteName());
		formDetail.setUnitCode(detail.getWorkSheet().getUnitCode());
		formDetail.setUnitName(detail.getWorkSheet().getUnitName());
		formDetail.setBatchNumber(detail.getWorkSheet().getBatchNumber());
		formDetail.setFurnaceNumber(detail.getWorkSheet().getStoveNumber());
		formDetail.setDeviceCode(detail.getDeviceCode());
		formDetail.setDeviceName(detail.getDeviceName());
		return formDetail;
	}
	/**
	 * 根据条件查找工单详情
	 * @param productionUnitCode
	 * @param no
	 * @param workpiece
	 * @param device
	 * @param process
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryWorkSheetDetailsByProductionUnitCode.do")
	@ResponseBody
	public ModelMap queryWorkSheetDetailsByProductionUnitCode(String productionUnitCode,String no, String workpiece, String device,String process,
															  @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from WorkSheetDetail d where 1=1 ";
		List<Object> data = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(productionUnitCode)){
			hql += " and d.workSheet.productionUnitCode=?" + i++;
			data.add(productionUnitCode);
		}
		if(!StringUtils.isEmpty(no) && !no.trim().equals("")){
			hql += " and d.workSheet.no like ?" + i++;
			data.add("%" + no.trim() + "%");
		}
		if(!StringUtils.isEmpty(workpiece) && !workpiece.trim().equals("")){
			hql += " and (d.workSheet.workPieceCode like ?" + i + " or d.workSheet.workPieceName like ?" + i++ +")";
			data.add("%" + workpiece.trim() + "%");
		}
		if(!StringUtils.isEmpty(device) && !device.trim().equals("")){
			hql += " and (d.deviceCode like ?" + i + " or d.deviceName like ?" + i++ +")";
			data.add("%" + device.trim() + "%");
		}
		if(!StringUtils.isEmpty(process) && !process.trim().equals("")){
			hql += " and (d.processCode like ?" + i + " or d.processName like ?" + i++ + ")";
			data.add("%" + process.trim() + "%");
		}
		Pager<WorkSheetDetail> pager = workSheetDetailService.queryObjs(hql,page,rows,data.toArray());
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 根据条件查找工单信息
	 * @param productionUnit
	 * @param workpiece
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryWorkSheets.do")
	@ResponseBody
	public ModelMap queryWorkSheets(String productionUnit, String workpiece, String from,String to,String batchNumber,
									@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from WorkSheet ws where 1=1 ";
		List<Object> data = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(productionUnit)){
			hql += " and (productionUnitCode like ?" + i + " or productionUnitName like ?" + i++ +")";
			data.add("%"+productionUnit+"%");
		}
		if(!StringUtils.isEmpty(workpiece) && !workpiece.trim().equals("")){
			hql += " and (workPieceCode like ?" + i + " or workPieceName like ?" + i++ +")";
			data.add("%" + workpiece.trim() + "%");
		}
		if(!StringUtils.isEmpty(batchNumber) && !batchNumber.trim().equals("")){
			hql += " and batchNumber like ?" + i++ ;
			data.add("%" + batchNumber.trim() + "%");
		}
		format.applyPattern("yyyy-MM-dd HH:mm:ss");
		try {
			if(!StringUtils.isEmpty(from) && !from.trim().equals("")){
				hql += " and manufactureDate>=?" + i++;
				data.add(format.parse(from+ " 00:00:00"));
			}
			if(!StringUtils.isEmpty(to)&&!to.trim().equals("")){
				hql += " and manufactureDate<=?" + i++;
				data.add(format.parse(to + " 23:59:59"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		format.applyPattern("yyyyMMdd");
		hql += " order by manufactureDate desc";
		Pager<WorkSheet> pager = workSheetService.queryObjs(hql,page,rows,data.toArray());
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 根据工单单号和工序代码查找工单详情参数信息
	 */
	@RequestMapping("/queryWorkSheetDetailParametersRecordByNoAndoProcessCode.do")
	@ResponseBody
	public void queryWorkSheetDetailParametersRecordByNoAndoProcessCode(String no,String processCode,HttpSession session){
		session.removeAttribute(InspectionRecordDetailController.LIST_NAME);
		//根据id查找工单详情参数信息
		List<WorkpieceProcessParameterMapping> detailsList = workpieceProcessParameterMappingService.queryByNoAndProcessCode(no,processCode);
		List<InspectionRecordDetail> list = new ArrayList<>();
		session.setAttribute(InspectionRecordDetailController.LIST_NAME,list);
		for(WorkpieceProcessParameterMapping detail:detailsList){
			InspectionRecordDetail inspectionRecordDetail = new InspectionRecordDetail();
			BeanUtils.copyProperties(detail,inspectionRecordDetail);
			inspectionRecordDetail.setParameterCode(detail.getParameter().getCode());
			inspectionRecordDetail.setParameterName((detail.getParameter().getName()));
			inspectionRecordDetail.setInspectionResult("OK");
			inspectionRecordDetail.setId(UUID.randomUUID().toString());
			list.add(inspectionRecordDetail);
		}
	}
	/**
	 * 根据工单单号和工序代码查找设备站点
	 */
	@RequestMapping("/queryDeviceSitesByNoAndProcessCode.do")
	@ResponseBody
	public List<Object[]> queryDeviceSitesByNoAndProcessCode(String no,String processCode,HttpSession session){
		return workSheetDetailService.queryDeviceSiteByNoAndProcessCode(no,processCode);
	}

	/**
	 * 锯料生产指令单打印
	 */
	@RequestMapping("/querySawingProductionPrintByWorkSheetId.do")
	@ResponseBody
	public ModelMap querySawingProductionPrintByWorkSheetId(Long workSheetId){
		ModelMap modelMap=new ModelMap();
		WorkSheet workSheet=workSheetService.queryObjById(workSheetId);
		Inventory inventory=inventoryService.queryByProperty("code",workSheet.getStoveNumber());
		modelMap.addAttribute("position",workSheet.getLocationCodes());
		if(inventory!=null){
			modelMap.addAttribute("cInvDefine14",inventory.getcInvDefine14()!=null?inventory.getcInvDefine14():"");
		}
		modelMap.addAttribute("time",sdfm.format(workSheet.getMakeDocumentDate()));
		modelMap.addAttribute("finishTime",sdfm.format(workSheet.getManufactureDate()));
		return modelMap;
	}


	/**
	 * 查询生产过程监控
	 */
	@RequestMapping("/queryProductionProcessMonitoring.do")
	@ResponseBody
	public ModelMap queryProductionProcessMonitoring(String productionUnit, String workpiece, String from,String to,String batchNumber,String no,String stoveNumber,
													 @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();

		String sql="select w.*,wp.cInvDefine14,material.firstMaterialDate,material.materialCount,material.materialBoxNum,jobbooking.lastJobbookingDate,jobbooking.jobbookingCount,jobbooking.jobbookingBoxNum,jobbooking.jobbookingInwarehouseCount,wdetail.unqualifiedCounts from WORKSHEET w left join (select MIN(mr.pickingDate) as firstMaterialDate,SUM(md.amount) as materialCount,SUM(md.amountOfBoxes)as materialBoxNum,mr.WORKSHEETNO as no from MaterialRequisitionDetail md " +
				"left join MaterialRequisition mr on mr.formNo=md.MATERIAL_REQUISITION_FORMNO group by mr.WORKSHEETNO) as material on w.no=material.no " +
				"left join (select MAX(jf.jobBookingDate)as lastJobbookingDate,SUM(jd.amountOfJobBooking)as jobbookingCount,SUM(jd.amountOfBoxes) as jobbookingBoxNum," +
				"SUM(jd.amountOfInWarehouse)as jobbookingInwarehouseCount,jd.no as no from JobBookingFormDetail jd left join JobBookingForm jf on jf.formNo=jd.JOBBOOKING_CODE group by jd.no)  as jobbooking on w.no=jobbooking.no "+
				"left join (select SUM(unqualifiedCount) as unqualifiedCounts,WORKSHEET_ID from WORKSHEETDETAIL group by WORKSHEET_ID) wdetail on wdetail.WORKSHEET_ID=w.id " +
				"left join WorkPiece wp on w.workpieceCode =wp.cInvCode where 1=1 ";

		List<Object> data = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(productionUnit)){
			sql += " and (w.productionUnitCode like ?" + i + " or w.productionUnitName like ?" + i++ +")";
			data.add("%"+productionUnit+"%");
		}
		if(!StringUtils.isEmpty(workpiece) && !workpiece.trim().equals("")){
			sql += " and (w.workPieceCode like ?" + i + " or w.workPieceName like ?" + i++ +")";
			data.add("%" + workpiece.trim() + "%");
		}
		if(!StringUtils.isEmpty(batchNumber) && !batchNumber.trim().equals("")){
			sql += " and w.batchNumber like ?" + i++ ;
			data.add("%" + batchNumber.trim() + "%");
		}

		if(!StringUtils.isEmpty(no) && !no.trim().equals("")){
			sql += " and w.no like ?" + i++ ;
			data.add("%" + no.trim() + "%");
		}

		if(!StringUtils.isEmpty(stoveNumber) && !stoveNumber.trim().equals("")){
			sql += " and w.stoveNumber like ?" + i++ ;
			data.add("%" + stoveNumber.trim() + "%");
		}

		format.applyPattern("yyyy-MM-dd HH:mm:ss");
		try {
			if(!StringUtils.isEmpty(from) && !from.trim().equals("")){
				sql += " and material.firstMaterialDate>=?" + i++;
				data.add(format.parse(from+ " 00:00:00"));
			}
			if(!StringUtils.isEmpty(to)&&!to.trim().equals("")){
				sql += " and material.firstMaterialDate<=?" + i++;
				data.add(format.parse(to + " 23:59:59"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		format.applyPattern("yyyyMMdd");
		sql += " order by w.batchNumber desc,material.firstMaterialDate desc";
		Pager<WorkSheetBo> pager = workSheetService.queryByMonitoring(sql,page,rows,data.toArray());
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 导出生产过程监控
	 */
	@RequestMapping("/exportProductionProcessMonitoring.do")
	@ResponseBody
	public void exportProductionProcessMonitoring(String productionUnit, String workpiece, String from,String to,String batchNumber,String no,String stoveNumber,
													 @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page,HttpServletResponse response) {

		String sql="select w.*,material.firstMaterialDate,material.materialCount,material.materialBoxNum,jobbooking.lastJobbookingDate,jobbooking.jobbookingCount,jobbooking.jobbookingBoxNum,jobbooking.jobbookingInwarehouseCount,wdetail.unqualifiedCounts from WORKSHEET w left join (select MIN(mr.pickingDate) as firstMaterialDate,SUM(md.amount) as materialCount,SUM(md.amountOfBoxes)as materialBoxNum,mr.WORKSHEETNO as no from MaterialRequisitionDetail md " +
				"left join MaterialRequisition mr on mr.formNo=md.MATERIAL_REQUISITION_FORMNO group by mr.WORKSHEETNO) as material on w.no=material.no " +
				"left join (select MAX(jf.jobBookingDate)as lastJobbookingDate,SUM(jd.amountOfJobBooking)as jobbookingCount,SUM(jd.amountOfBoxes) as jobbookingBoxNum," +
				"SUM(jd.amountOfInWarehouse)as jobbookingInwarehouseCount,jd.no as no from JobBookingFormDetail jd left join JobBookingForm jf on jf.formNo=jd.JOBBOOKING_CODE group by jd.no)  as jobbooking on w.no=jobbooking.no "+
				"left join (select SUM(unqualifiedCount) as unqualifiedCounts,WORKSHEET_ID from WORKSHEETDETAIL group by WORKSHEET_ID) wdetail on wdetail.WORKSHEET_ID=w.id where 1=1 ";

		List<Object> data = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(productionUnit)){
			sql += " and (w.productionUnitCode like ?" + i + " or w.productionUnitName like ?" + i++ +")";
			data.add("%"+productionUnit+"%");
		}
		if(!StringUtils.isEmpty(workpiece) && !workpiece.trim().equals("")){
			sql += " and (w.workPieceCode like ?" + i + " or w.workPieceName like ?" + i++ +")";
			data.add("%" + workpiece.trim() + "%");
		}
		if(!StringUtils.isEmpty(batchNumber) && !batchNumber.trim().equals("")){
			sql += " and w.batchNumber like ?" + i++ ;
			data.add("%" + batchNumber.trim() + "%");
		}

		if(!StringUtils.isEmpty(no) && !no.trim().equals("")){
			sql += " and w.no like ?" + i++ ;
			data.add("%" + no.trim() + "%");
		}

		if(!StringUtils.isEmpty(stoveNumber) && !stoveNumber.trim().equals("")){
			sql += " and w.stoveNumber like ?" + i++ ;
			data.add("%" + stoveNumber.trim() + "%");
		}

		format.applyPattern("yyyy-MM-dd HH:mm:ss");
		try {
			if(!StringUtils.isEmpty(from) && !from.trim().equals("")){
				sql += " and material.firstMaterialDate>=?" + i++;
				data.add(format.parse(from+ " 00:00:00"));
			}
			if(!StringUtils.isEmpty(to)&&!to.trim().equals("")){
				sql += " and material.firstMaterialDate<=?" + i++;
				data.add(format.parse(to + " 23:59:59"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		format.applyPattern("yyyyMMdd");
		sql += " order by w.batchNumber desc,material.firstMaterialDate desc";
		List<WorkSheetBo> list = workSheetService.queryByMonitoring(sql,data.toArray());


		//设置Excel中的title
		String[] titles = {"生产令/批号","单号","生产单元","工件代码","工件名称","规格型号","材料编号","计划生产数量","投产日期","领料数量","领料箱数","报工日期","报工数量","报工箱数","入库数量","不合格数量","差异数"};
		//存储所有数据
		List<String[]> dataList = new ArrayList<>();
		for(WorkSheetBo vo : list) {
			String firstMaterialDate="";
			if(vo.getFirstMaterialDate()!=null){
				firstMaterialDate=sdfm.format(vo.getFirstMaterialDate());
			}
			String lastJobbookingDate="";
			if(vo.getLastJobbookingDate()!=null){
				lastJobbookingDate=sdfm.format(vo.getLastJobbookingDate());
			}

			Integer diffrentNum=vo.getProductCount();

			if(vo.getJobbookingInwarehouseCount()!=null){
				diffrentNum=diffrentNum-vo.getJobbookingInwarehouseCount();
			}
			if(vo.getUnqualifiedCounts()!=null){
				diffrentNum=diffrentNum-vo.getUnqualifiedCounts();
			}

			Integer jobbookingCount=0;
			if(vo.getJobbookingCount()!=null){
				jobbookingCount=vo.getJobbookingCount();
			}

			Integer jobbookingBoxNum=0;
			if(vo.getJobbookingBoxNum()!=null){
				jobbookingBoxNum=vo.getJobbookingBoxNum();
			}

			Integer jobbookingInwarehouseCount=0;
			if(vo.getJobbookingInwarehouseCount()!=null){
				jobbookingInwarehouseCount=vo.getJobbookingInwarehouseCount();
			}

			String[] ExcelData = {
					vo.getBatchNumber(),
					vo.getNo(),
					vo.getProductionUnitName(),
					vo.getWorkPieceCode(),
					vo.getWorkPieceName(),
					vo.getUnitType(),
					vo.getStoveNumber(),
					vo.getProductCount()+"",
					firstMaterialDate,
					vo.getMaterialCount()+"",
					vo.getMaterialBoxNum()+"",
					lastJobbookingDate,
					jobbookingCount+"",
					jobbookingBoxNum+"",
					jobbookingInwarehouseCount+"",
					vo.getUnqualifiedCounts()+"",
					diffrentNum+""
			};

			dataList.add(ExcelData);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		HSSFWorkbook workBook = ExcelUtil.getHSSFWorkbook("生产过程监控", titles, dataList);
		try {
			this.setResponseHeader(response, "生产过程监控"+sdf.format(new Date())+".xls");
			OutputStream os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
