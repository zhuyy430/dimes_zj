package com.digitzones.paperless.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.devmgr.vo.MaintenancePlanRecordVO;
import com.digitzones.model.Employee;
/**
 * 点检记录
 * @author zdq
 * 2019年3月8日
 */
@RestController
@RequestMapping("paperlessMaintenancePlan")
public class PaperlessMaintenancePlanRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	/**
	 * 查询所有保养计划记录
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryAllMaintenancePlanRecordsForPaperless.do")
	public List<MaintenancePlanRecordVO> queryAllMaintenancePlanRecordsByDeviceSiteCode(String deviceCode,Long productionUnitId){
		List<MaintenancePlanRecord> ret = new ArrayList<>();
		if(!StringUtils.isEmpty(deviceCode)) {
			ret = maintenancePlanRecordService.queryAllMaintenancePlanRecordsByDeviceCodeAndUser(deviceCode);
		}else{
			ret = maintenancePlanRecordService.queryAllMaintenancePlanRecordsByProductionUnitIdAndUser(productionUnitId);
		}
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(ret)) {
			for(MaintenancePlanRecord record : ret) {
				list.add(model2vo(record));
			}
		}
		return list;
	}
	
	/**
	 * 根据保养记录id查询保养记录信息
	 * @param id 
	 * @return
	 */
	@GetMapping("/queryMaintenancePlanRecordById.do")
	public MaintenancePlanRecordVO queryMaintenancePlanRecordById(Long id,HttpServletRequest request) {
		MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(id);
		MaintenancePlanRecordVO vo = new MaintenancePlanRecordVO();
		BeanUtils.copyProperties(record, vo);
		
		List<MaintenanceUser> mu = maintenanceUserService.queryMaintenanceUserByRecordId(record.getId());
		if(!CollectionUtils.isEmpty(mu)){
			MaintenanceUser user = mu.get(0);
			vo.setEmployeeName(user.getName());
			vo.setEmployeeCode(user.getCode());
		}
		
		if(record.getMaintenanceDate()!=null) {
			vo.setMaintenanceDate(format.format(record.getMaintenanceDate()));
		}
		if(record.getMaintenancedDate()!=null) {
			vo.setMaintenancedDate(format.format(record.getMaintenancedDate()));
		}
		if(record.getMaintenanceType()!=null) {
			vo.setMaintenanceType(record.getMaintenanceType().getName());
			vo.setMaintenanceTypeCode(record.getMaintenanceType().getCode());
		}
		return vo;
	}
	
	/**
	 * 执行保养计划记录
	 * @param ids
	 * @return
	 */
	@RequestMapping("/updateMaintenancePlanRecord.do")
	public ModelMap updateMaintenancePlanRecord(MaintenancePlanRecord maintenancePlanRecord,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Employee employee=(Employee)request.getSession().getAttribute("employee");
		
		MaintenancePlanRecord record = maintenancePlanRecordService.queryObjById(maintenancePlanRecord.getId());
		record.setMaintenancedDate(maintenancePlanRecord.getMaintenancedDate());
		record.setConfirmName(maintenancePlanRecord.getConfirmName());
		record.setConfirmCode(maintenancePlanRecord.getConfirmCode());
		record.setStatus(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED);
		maintenancePlanRecordService.confirmMaintenance(record, employee.getName(), Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED);
		maintenancePlanRecordService.updateObj(record);
		return modelMap;
	}
	/**
	 * model转vo
	 * @param record
	 * @return
	 */
	public MaintenancePlanRecordVO model2vo(MaintenancePlanRecord record) {
		//format.applyPattern("yyyy-MM-dd");
		MaintenancePlanRecordVO vo = new MaintenancePlanRecordVO();
		BeanUtils.copyProperties(record, vo);
		MaintenanceUser u = maintenanceUserService.queryPersonInChargeByMaintenancePlanRecordId(record.getId());
		if(u!=null) {
			vo.setEmployeeCode(u.getCode());
			vo.setEmployeeName(u.getName());
		}
		if(record.getMaintenanceDate()!=null) {
			vo.setMaintenanceDate(format.format(record.getMaintenanceDate()));
		}
		if(record.getMaintenancedDate()!=null) {
			vo.setMaintenancedDate(format.format(record.getMaintenancedDate()));
		}
		if(record.getMaintenanceType()!=null) {
			vo.setMaintenanceType(record.getMaintenanceType().getName());
			vo.setMaintenanceTypeCode(record.getMaintenanceType().getCode());
		}
		return vo;
	}
}
