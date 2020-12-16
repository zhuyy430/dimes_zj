package com.digitzones.mc.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.digitzones.constants.Constant;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.model.MCWorkSheet;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.mc.service.IMCWorkSheetService;
import com.digitzones.model.*;
import com.digitzones.procurement.model.InventoryEquipmentTypeMapping;
import com.digitzones.procurement.service.IInventoryEquipmentTypeMappingService;
import com.digitzones.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/mcWorkSheet")
public class MCWorkSheetController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	private static final String FORMNO_PREFIX = "ZJ-";
	@Autowired
	private IWorkSheetDetailService workSheetDetailService;
	@Autowired
	private IMCWorkSheetService mcWorkSheetService;
	@Autowired
	private IWorkSheetService workSheetService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProductionUnitService productionUnitService;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IInventoryEquipmentTypeMappingService inventoryEquipmentTypeMappingService;
	@Autowired
	private IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService;

	/**
	 * 判断该工单的站点是否全部关联装备
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryIshaveNotMapping.do")
	@ResponseBody
	public ModelMap queryIshaveNotMapping(Long id){
		ModelMap modelMap = new ModelMap();
		WorkSheetDetail d = workSheetDetailService.queryObjById(id);
		DeviceSite deviceSite=deviceSiteService.queryByProperty("code",d.getDeviceSiteCode());


		List<InventoryEquipmentTypeMapping> list= inventoryEquipmentTypeMappingService.queryListByInventoryCode(d.getWorkSheet().getWorkPieceCode());
		if(list!=null&&list.size()>0){
			for(InventoryEquipmentTypeMapping iem:list){
				EquipmentType equipmentType=iem.getEquipmentType();
				List<EquipmentDeviceSiteMapping> edms=equipmentDeviceSiteMappingService.queryListByDeviceSiteCodeAndEquipmentTypeId(d.getDeviceSiteCode(),equipmentType.getId(),d.getWorkSheet().getNo());
				if(edms!=null&&edms.size()>0){

				}else{
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", "生产该工单需关联的装备信息尚未绑定完成，是否继续开工!");
					return modelMap;
				}
			}

		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}


	/**
	 * 开工
	 * @return
	 */
	@RequestMapping("/startWork.do")
	@ResponseBody
	public ModelMap startWork(Long id) {
		ModelMap modelMap = new ModelMap();
		WorkSheetDetail d = workSheetDetailService.queryObjById(id);
		List<WorkSheetDetail> detailList = workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(d.getWorkSheet().getId());
		for(WorkSheetDetail detail:detailList){
			detail.setStatus(Constant.WorkSheet.Status.PROCESSING);
			workSheetDetailService.updateObj(detail);
			WorkSheet sheet = detail.getWorkSheet();
			if(!sheet.getStatus().equals(Constant.WorkSheet.Status.PROCESSING)){
				sheet.setStatus(Constant.WorkSheet.Status.PROCESSING);
				workSheetService.updateObj(sheet);
			}
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功!");
		return modelMap; 
	}
	/**
	 * 根据站点代码查找站点下的工单信息
	 * @return
	 */
	@RequestMapping("/queryWorkSheetByDeviceSiteCode.do")
	@ResponseBody
	public List<MCWorkSheet> queryWorkSheetByDeviceSiteCode(String deviceSiteCode) {
		List<MCWorkSheet> list = mcWorkSheetService.queryNotCompleteWorkSheetByDeviceCode(deviceSiteCode);
		List<MCWorkSheet> list0 = new ArrayList<>();
		List<MCWorkSheet> list1 = new ArrayList<>();
		List<MCWorkSheet> list2 = new ArrayList<>();
		if(list!=null && list.size()>0) {
			for(int i = 0;i<list.size();i++) {
				MCWorkSheet sheet = list.get(i);
				String no = sheet.getNo();
				String[] nolist = no.split("-");
				if(nolist[1].length()==1){
					sheet.setNo(nolist[0]+"-0"+nolist[1]);
				}
				if(Constant.WorkSheet.Status.PLAN.equals(sheet.getStatus())) {
					/*list.remove(i);
					list.add(0, sheet);*/
					list0.add(sheet);
				}else if(Constant.WorkSheet.Status.PROCESSING.equals(sheet.getStatus())){
					list1.add(sheet);
				}else{
					list2.add(sheet);
				}
			}
		}
		list0.sort((a, b) -> b.getNo().compareTo(a.getNo()));
		list1.sort((a, b) -> b.getNo().compareTo(a.getNo()));
		list2.sort((a, b) -> b.getNo().compareTo(a.getNo()));
		List<MCWorkSheet> reList = new ArrayList<>();
		reList.addAll(list1);
		reList.addAll(list0);
		reList.addAll(list2);
		return reList;
	}
	/**
	 * 根据站点代码查找站点下是否有开工的工单
	 * @return
	 */
	@RequestMapping("/judgequeryWorkSheetByDeviceSiteCode.do")
	@ResponseBody
	public ModelMap judgequeryWorkSheetByDeviceSiteCode(String deviceSiteCode) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryByProperty("code",deviceSiteCode);
		if(ds==null){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "站点不存在!");
			return modelMap;
		}
		ProductionUnit productionUnit = ds.getDevice().getProductionUnit();
		modelMap.addAttribute("allowMultiWorkSheetRunning",productionUnit.getAllowMultiWorkSheetRunning());
		//允许多个工单同时开工
		if (productionUnit.getAllowMultiWorkSheetRunning()){
			modelMap.addAttribute("existence",false);
		}else{
			List<MCWorkSheet> list=mcWorkSheetService.queryProcessingWorkSheetsByDeviceCode(deviceSiteCode);
			if(list!=null && list.size()>0) {
				modelMap.addAttribute("existence",true);
			}
		}
		return modelMap;
	}
	/**
	 * 查询所有正在开工的工单信息，如果设备站点代码为null，则查询当前机器所有站点上的正在开工的工单信息
	 * @param deviceSiteCode
	 * @return
	 */
	@RequestMapping("/queryProcessingWorkSheets.do")
	@ResponseBody
	public List<MCWorkSheet> queryProcessingWorkSheets(String deviceSiteCode,HttpServletRequest request){
		if(deviceSiteCode!=null) {
			return mcWorkSheetService.queryProcessingWorkSheetsByDeviceCode(deviceSiteCode);
		}else {
			return mcWorkSheetService.queryProcessingWorkSheets(request.getRemoteAddr());
		}
	}

	/**
	 * 停工
	 * @param deviceSiteCode
	 * @return
	 */
	@RequestMapping("/stopWork.do")
	@ResponseBody
	public ModelMap stopWork(String deviceSiteCode,HttpServletRequest request){
			ModelMap modelMap = new ModelMap();
			
			List<MCWorkSheet> list = mcWorkSheetService.queryProcessingWorkSheetsByDeviceCode(deviceSiteCode);
			MCWorkSheet workSheet = list.get(0);
			
			WorkSheetDetail d = workSheetDetailService.queryObjById(workSheet.getId());
			List<WorkSheetDetail> detailList = workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(d.getWorkSheet().getId());
			
			for(WorkSheetDetail detail:detailList){
				detail.setStatus(Constant.WorkSheet.Status.STOP);
				workSheetDetailService.updateObj(detail);
				WorkSheet sheet = detail.getWorkSheet();
				if(!sheet.getStatus().equals(Constant.WorkSheet.Status.STOP)){
					sheet.setStatus(Constant.WorkSheet.Status.STOP);
					workSheetService.updateObj(sheet);
				}
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "操作成功!");
			return modelMap; 
	}
	/**
	 * 完工
	 * @param deviceSiteCode
	 * @return
	 */
	@RequestMapping("/completedWork.do")
	@ResponseBody
	public ModelMap completedWork(String deviceSiteCode,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		
		List<MCWorkSheet> list = mcWorkSheetService.queryProcessingWorkSheetsByDeviceCode(deviceSiteCode);
		MCWorkSheet workSheet = list.get(0);
		WorkSheetDetail d = workSheetDetailService.queryObjById(workSheet.getId());
		List<WorkSheetDetail> detailList = workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(d.getWorkSheet().getId());
		
		for(WorkSheetDetail detail:detailList){
			detail.setCompleteTime(new Date());
			detail.setStatus(Constant.WorkSheet.Status.COMPLETE);
			workSheetDetailService.updateObj(detail);
			WorkSheet sheet = detail.getWorkSheet();
			if(!sheet.getStatus().equals(Constant.WorkSheet.Status.COMPLETE)){
				sheet.setCompleteTime(new Date());
				sheet.setStatus(Constant.WorkSheet.Status.COMPLETE);
				workSheetService.updateObj(sheet);
			}
			//解除站点装备关联
			MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
			User user = userService.queryByProperty("username", mcUser.getUsername());

			List<EquipmentDeviceSiteMapping> edms=equipmentDeviceSiteMappingService.queryListByDeviceSiteCodeAndEquipmentTypeId(deviceSiteCode,null,workSheet.getNo());
			for(EquipmentDeviceSiteMapping edm:edms){
				EquipmentDeviceSiteMapping equipmentDeviceSiteMapping = equipmentDeviceSiteMappingService.queryObjById(edm.getId());
				if(user!=null) {
					equipmentDeviceSiteMapping.setUnbindUserId(user.getId());
					equipmentDeviceSiteMapping.setUnbindUsername(user.getUsername());
				}
				equipmentDeviceSiteMapping.setUnbind(true);
				equipmentDeviceSiteMapping.setUnbindDate(new Date());
				equipmentDeviceSiteMappingService.updateObj(equipmentDeviceSiteMapping);
			}

		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功!");
		return modelMap; 
	}
	
	/**
	 * 报工(保存)
	 * @return
	 */
	@RequestMapping("/reportWork.do")
	@ResponseBody
	public ModelMap reportWork(String objs) {
		ModelMap modelMap = new ModelMap();
		JSONArray jsonArray = JSONArray.parseArray(objs);
		Map<Long,Integer> map = new HashMap<>();
		for(int i = 0;i<jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			map.put(jsonObject.getLong("id"), jsonObject.getInteger("reportCount"));
		}
		mcWorkSheetService.updateWorkSheetDetail(map);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作完成!");
		return modelMap;
	}
	/**
	 * 通过工单no查询工单
	 */
	@RequestMapping("/queryWorkSheetByNo.do")
	@ResponseBody
	public WorkSheet queryWorkSheetByNo(String no) {	
		return mcWorkSheetService.queryWorkSheetIdByNo(no);
	}
	/**
	 * 通过工单no查询生产单元
	 */
	@RequestMapping("/queryProductionUnitByNo.do")
	@ResponseBody
	public ProductionUnit queryProductionUnitByNo(String no) {	
		WorkSheet ws = mcWorkSheetService.queryWorkSheetIdByNo(no);
		return productionUnitService.queryByProperty("code", ws.getProductionUnitCode());
	}
	
	/**
	 * 更换工单的生产单元
	 * @return
	 */
	@RequestMapping("/chengeProductionUnitWithWorkSheet.do")
	@ResponseBody
	public ModelMap addWorkSheet(Long workSheetDetailId ,Long productionUnitId,HttpServletRequest request) {
		WorkSheetDetail workSheetDetail = workSheetDetailService.queryObjById(workSheetDetailId);
		WorkSheet workSheetOld = workSheetService.queryObjById(workSheetDetail.getWorkSheet().getId());
		WorkSheet workSheet = new WorkSheet();
		ProductionUnit p = productionUnitService.queryObjById(productionUnitId);
		//BeanUtils.copyProperties(workSheetOld, workSheet);
		capyWorkSheet(workSheetOld, workSheet);
		workSheet.setProductionUnitId(productionUnitId);
		workSheet.setProductionUnitCode(p.getCode());
		workSheet.setProductionUnitName(p.getName());
		ModelMap modelMap = new ModelMap();
		/*Date now = new Date();
		String formNo = workSheetService.queryMaxNoByMakeDocumentDate(now);
		if(!StringUtils.isEmpty(formNo)&&formNo.equals(workSheet.getNo())){
			formNo = StringUtil.increment(formNo);
			workSheet.setNo(formNo);
			modelMap.addAttribute("changeNoMsg","该单号已存在，修改单号为"+formNo);
		}else{
			formNo = FORMNO_PREFIX + format.format(now) + "000";
			workSheet.setNo(formNo);
		}*/
		List<WorkSheetDetail> workSheetDetails=new ArrayList<>();
		workSheetDetailService.buildWorkSheetDetailListInMemoryByWorkpieceId(workSheet.getProductCount(),workSheet.getWorkPieceCode(),workSheet.getProductionUnitCode(),workSheetDetails);
		
		if(CollectionUtils.isEmpty(workSheetDetails)){
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","缺少工序流转信息，保存失败!");
			return modelMap;
		}
		//String ip = request.getRemoteAddr();
		//MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		//workSheet.setDocumentMaker(mcUser.getEmployeeName());
		//workSheet.setMakeDocumentDate(new Date());
		workSheetService.addWorkSheet(workSheet,workSheetDetails,true);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","添加成功!");
		return modelMap;
	}
	/**
	 * 更换工单的生产单元
	 * @return
	 */
	@RequestMapping("/chengeProductionUnit.do")
	@ResponseBody
	public ModelMap chengeProductionUnit(Long workSheetId ,Long productionUnitId,HttpServletRequest request) {
		WorkSheet workSheetOld = workSheetService.queryObjById(workSheetId);
		WorkSheet workSheet = new WorkSheet();
		ProductionUnit p = productionUnitService.queryObjById(productionUnitId);
		//BeanUtils.copyProperties(workSheetOld, workSheet);
		capyWorkSheet(workSheetOld, workSheet);
		workSheet.setProductionUnitId(productionUnitId);
		workSheet.setProductionUnitCode(p.getCode());
		workSheet.setProductionUnitName(p.getName());
		ModelMap modelMap = new ModelMap();
		/*Date now = new Date();
		String formNo = workSheetService.queryMaxNoByMakeDocumentDate(now);
		if(!StringUtils.isEmpty(formNo)&&formNo.equals(workSheet.getNo())){
			formNo = StringUtil.increment(formNo);
			workSheet.setNo(formNo);
			modelMap.addAttribute("changeNoMsg","该单号已存在，修改单号为"+formNo);
		}else{
			formNo = FORMNO_PREFIX + format.format(now) + "000";
			workSheet.setNo(formNo);
		}*/
		List<WorkSheetDetail> workSheetDetails=new ArrayList<>();
		workSheetDetailService.buildWorkSheetDetailListInMemoryByWorkpieceId(workSheet.getProductCount(),workSheet.getWorkPieceCode(),workSheet.getProductionUnitCode(),workSheetDetails);

		if(CollectionUtils.isEmpty(workSheetDetails)){
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","缺少工序流转信息，保存失败!");
			return modelMap;
		}
		/*String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		workSheet.setDocumentMaker(mcUser.getEmployeeName());
		workSheet.setMakeDocumentDate(new Date());*/
		workSheetService.addWorkSheet(workSheet,workSheetDetails,true);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","添加成功!");
		return modelMap;
	}


	/**
	 * 更换工单的生产单元
	 * @return
	 */
	@RequestMapping("/chengeProductionUnitForPC.do")
	@ResponseBody
	public ModelMap chengeProductionUnitForPC(Long workSheetId ,Long productionUnitId,HttpServletRequest request) {
		WorkSheet workSheetOld = workSheetService.queryObjById(workSheetId);
		WorkSheet workSheet = new WorkSheet();
		ProductionUnit p = productionUnitService.queryObjById(productionUnitId);
		//BeanUtils.copyProperties(workSheetOld, workSheet);
		capyWorkSheet(workSheetOld, workSheet);
		workSheet.setProductionUnitId(productionUnitId);
		workSheet.setProductionUnitCode(p.getCode());
		workSheet.setProductionUnitName(p.getName());
		ModelMap modelMap = new ModelMap();
		/*Date now = new Date();
		String formNo = workSheetService.queryMaxNoByMakeDocumentDate(now);
		if(!StringUtils.isEmpty(formNo)&&formNo.equals(workSheet.getNo())){
			formNo = StringUtil.increment(formNo);
			workSheet.setNo(formNo);
			modelMap.addAttribute("changeNoMsg","该单号已存在，修改单号为"+formNo);
		}else{
			formNo = FORMNO_PREFIX + format.format(now) + "000";
			workSheet.setNo(formNo);
		}*/
		List<WorkSheetDetail> workSheetDetails=new ArrayList<>();
		workSheetDetailService.buildWorkSheetDetailListInMemoryByWorkpieceId(workSheet.getProductCount(),workSheet.getWorkPieceCode(),workSheet.getProductionUnitCode(),workSheetDetails);

		if(CollectionUtils.isEmpty(workSheetDetails)){
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","缺少工序流转信息，保存失败!");
			return modelMap;
		}
		/*String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
		workSheet.setDocumentMaker(mcUser.getEmployeeName());
		workSheet.setMakeDocumentDate(new Date());*/
		workSheetService.addWorkSheet(workSheet,workSheetDetails,true);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("msg","添加成功!");
		return modelMap;
	}
	private void capyWorkSheet(WorkSheet old,WorkSheet n){
		n.setId(old.getId());
		n.setBatchNumber(old.getBatchNumber());
		n.setCompleteTime(old.getCompleteTime());
		n.setDefine33(old.getDefine33());
		n.setDeleted(old.getDeleted());
		n.setDepartmentCode(old.getDepartmentCode());
		n.setDepartmentName(old.getDepartmentName());
		n.setDocumentMaker(old.getDocumentMaker());
		n.setFromErp(old.getFromErp());
		n.setGraphNumber(old.getGraphNumber());
		n.setLotNo(old.getLotNo());
		n.setMakeDocumentDate(old.getMakeDocumentDate());
		n.setManufactureDate(old.getManufactureDate());
		n.setMoallocateInvcode(old.getMoallocateInvcode());
		n.setMoallocateQty(old.getMoallocateQty());
		n.setMocode(old.getMocode());
		n.setMoDId(old.getMoDId());
		n.setMoId(old.getMoId());
		n.setNo(old.getNo());
		n.setNote(old.getNote());
		n.setProductCount(old.getProductCount());
		n.setRepairWorkSheetNo(old.getRepairWorkSheetNo());
		n.setStatus(old.getStatus());
		n.setStoveNumber(old.getStoveNumber());
		n.setUnitCode(old.getUnitCode());
		n.setUnitName(old.getUnitName());
		n.setUnitType(old.getUnitType());
		n.setWorkPieceCode(old.getWorkPieceCode());
		n.setWorkPieceName(old.getWorkPieceName());
		n.setWorkSheetType(old.getWorkSheetType());
	}

	/**
	 * 完工
	 */
	@RequestMapping("/over.do")
	@ResponseBody
	public ModelMap over(String no,String deviceSiteCode, HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		WorkSheet workSheet = workSheetService.queryByProperty("no",no);
		if(Constant.WorkSheet.Status.STOP.equals(workSheet.getStatus())||Constant.WorkSheet.Status.COMPLETE.equals(workSheet.getStatus())) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工单已处于停工或完工状态!");
			return modelMap;
		}

		//解除站点装备关联
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());
		User user = userService.queryByProperty("username", mcUser.getUsername());
		/*List<EquipmentDeviceSiteMapping> edms=equipmentDeviceSiteMappingService.queryListByDeviceSiteCodeAndEquipmentTypeId(deviceSiteCode,null,workSheet.getNo());
		for(EquipmentDeviceSiteMapping edm:edms){
			EquipmentDeviceSiteMapping equipmentDeviceSiteMapping = equipmentDeviceSiteMappingService.queryObjById(edm.getId());
			if(user!=null) {
				equipmentDeviceSiteMapping.setUnbindUserId(user.getId());
				equipmentDeviceSiteMapping.setUnbindUsername(user.getUsername());
			}
			equipmentDeviceSiteMapping.setUnbind(true);
			equipmentDeviceSiteMapping.setUnbindDate(new Date());
			equipmentDeviceSiteMappingService.updateObj(equipmentDeviceSiteMapping);
		}*/

		workSheetService.updateOver(workSheet.getId(),user);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已完工!");
		return modelMap;
	}

	/**
	 * 停工
	 * @param no
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws NumberFormatException
	 */
	@RequestMapping("/stop.do")
	@ResponseBody
	public ModelMap end(String no){
		ModelMap modelMap = new ModelMap();
		WorkSheet workSheet = workSheetService.queryByProperty("no",no);
		if(Constant.WorkSheet.Status.STOP.equals(workSheet.getStatus())||Constant.WorkSheet.Status.COMPLETE.equals(workSheet.getStatus())) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该工单已处于停工或完工状态!");
			return modelMap;
		}
		workSheetService.stop(workSheet.getId());
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已停工!");
		return modelMap;
	}
	/**
	 * 开工
	 * @param no
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws NumberFormatException
	 */
	@RequestMapping("/start.do")
	@ResponseBody
	public ModelMap start(String no){
		WorkSheet workSheet = workSheetService.queryByProperty("no",no);
		ProductionUnit unit = productionUnitService.queryByProperty("code",workSheet.getProductionUnitCode()) ;
		ModelMap modelMap = new ModelMap();
		if(!unit.getAllowMultiWorkSheetRunning()) {
			//查找该工单下是否存在正在开工的设备站点，如果存在，则不允许开工，否则，可以开工
			List<WorkSheetDetail> workSheetDetailList = workSheetDetailService.queryWorkSheetDetailsByWorkSheetId(workSheet.getId());
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
		workSheetService.start(workSheet.getId());
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "已开工!");
		return modelMap;
	}
}

