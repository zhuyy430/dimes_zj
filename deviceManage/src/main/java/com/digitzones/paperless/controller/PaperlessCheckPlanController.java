package com.digitzones.paperless.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.CheckingPlanRecordItem;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemService;
import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.model.Device;
import com.digitzones.model.Employee;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.User;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IRelatedDocumentService;
/**
 * 点检记录
 * @author zdq
 * 2019年3月8日
 */
@RestController
@RequestMapping("paperlessCheckPlan")
public class PaperlessCheckPlanController {
	@Autowired
	private ICheckingPlanRecordService checkingPlanRecordService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private ICheckingPlanRecordItemService checkingPlanRecordItemService;
	@Autowired
	private IDeviceProjectRecordService deviceProjectRecordService;
	/**
	 * 根据设备代码查找点检记录
	 * @param deviceCode
	 * @return List<CheckingPlanRecord>
	 */
	@RequestMapping("/queryCheckingPlanRecordByDeviceCode.do")
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCode(String deviceCode,Long productionUnitId){
		Calendar c = Calendar.getInstance();
		if(StringUtils.isEmpty(deviceCode)) {
			return checkingPlanRecordService.queryCheckingPlanRecordByProductionUnitIdAndDate(productionUnitId==null?null:productionUnitId.toString(),  c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH));
		}
		List<CheckingPlanRecord> recordList = checkingPlanRecordService.queryCheckingPlanRecordByDeviceCodeAndDate(deviceCode, c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH));
		return recordList;//checkingPlanRecordService.queryCheckingPlanRecordByDeviceCode(deviceCode);
	}
	/**
	 * 根据生产单元id查找所有设备信息
	 * @return
	 */
	@RequestMapping("/loadDevices4ProductionUnit.do")
	public List<Device> loadDevices4ProductionUnit(HttpSession session,String module){
		ProductionUnit productionUnit = (ProductionUnit) session.getAttribute("productionLine");
		if(productionUnit!=null) {
			return deviceService.queryDevicesByProductionUnitId(productionUnit.getId(),module);
		}else {
			return deviceService.queryAllDevices(module);
		}
	}
	/**
	 * 根据生产单元id查找所有设备信息
	 * @return
	 */
	@RequestMapping("/loadDevices4ProductionUnitId.do")
    public List<Device> loadDevices4ProductionUnitId(Long productionUnitId,String module){
        if(productionUnitId!=null) {
            return deviceService.queryDevicesByProductionUnitId(productionUnitId,module);
        }else {
            return deviceService.queryAllDevices(module);
        }
    }
	/**
	 * 根据点检记录id查询点检记录信息
	 * @param id 
	 * @return
	 */
	@GetMapping("/queryCheckingPlanRecordById.do")
	public ModelMap queryCheckingPlanRecordById(Long id,HttpSession session) {
		ModelMap modelMap = new ModelMap();
		CheckingPlanRecord record = checkingPlanRecordService.queryObjById(id);
		if(record!=null) {
			modelMap.addAttribute("record", record);
			//根据设备编码查询文档信息
			String deviceCode = record.getDeviceCode();
			Device device = deviceService.queryByProperty("code", deviceCode);
			if(device!=null) {
				Long deviceId = device.getId();
				//查找关联文档
				List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, deviceId);
				modelMap.addAttribute("docs", docs);
				
				//根据设备编码查找点检记录项信息
				List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(id);
				if(CollectionUtils.isEmpty(list)) {
					//查找标准点检项目
					List<DeviceProjectRecord> projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceId(deviceId);
					modelMap.addAttribute("projectRecords", projectRecords);
				}else {
					modelMap.addAttribute("projectRecords", list);
				}
			}
		}
		
		if(record.getEmployeeCode()==null) {
			Employee employee = (Employee) session.getAttribute("employee");
			if(employee!=null) {
					modelMap.addAttribute("checkUsername", employee.getName());
					modelMap.addAttribute("checkUsercode", employee.getCode());
			}
		}else {
			modelMap.addAttribute("checkUsercode", record.getEmployeeCode());
			modelMap.addAttribute("checkUsername", record.getEmployeeName());
		}
		return modelMap;
	}
	/**
	 * 设备点检
	 * @param record
	 * @param itemIds
	 * @param results
	 * @return
	 */
	@RequestMapping("/deviceCheck.do")
	public ModelMap deviceCheck(CheckingPlanRecord record,String itemIds,String results,String notes,String checkValue) {
		ModelMap modelMap = new ModelMap();
		CheckingPlanRecord checkingPlanRecord = checkingPlanRecordService.queryObjById(record.getId());
		checkingPlanRecord.setCheckedDate(record.getCheckedDate());
		checkingPlanRecord.setEmployeeCode(record.getEmployeeCode());
		checkingPlanRecord.setEmployeeName(record.getEmployeeName());
		checkingPlanRecord.setPicPath(record.getPicPath());
		checkingPlanRecordService.updateCheckingPlanRecord(checkingPlanRecord,itemIds,results,notes,checkValue);
		return modelMap;
	}
}
