package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.digitzones.util.DateStringUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.dao.IClassesDao;
import com.digitzones.dao.IDeviceDao;
import com.digitzones.devmgr.dao.IDeviceMaintenancePlanMappingDao;
import com.digitzones.devmgr.dao.IDeviceProjectRecordDao;
import com.digitzones.devmgr.dao.IMaintenanceItemDao;
import com.digitzones.devmgr.dao.IMaintenancePlanDao;
import com.digitzones.devmgr.dao.IMaintenanceTypeDao;
import com.digitzones.devmgr.model.DeviceMaintenancePlanMapping;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.model.MaintenanceItem;
import com.digitzones.devmgr.model.MaintenancePlan;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenancePlanService;
import com.digitzones.model.Classes;
import com.digitzones.model.Device;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
@Service
public class MaintenancePlanServiceImpl implements IMaintenancePlanService {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	@Autowired
	private IMaintenancePlanDao maintenancePlanDao;
	@Autowired
	private IDeviceMaintenancePlanMappingDao deviceMaintenancePlanMappingDao;
	@Autowired
	private IDeviceDao deviceDao;
	@Autowired
	private IClassesDao classesDao;
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	@Autowired
	private IMaintenanceItemDao maintenanceItemDao;
	@Autowired
	private IDeviceProjectRecordDao deviceProjectRecordDao;
	@Autowired
	private IMaintenanceTypeDao maintenanceTypeDao;
	@Override
	public Pager<MaintenancePlan> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenancePlanDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenancePlan obj) {
		maintenancePlanDao.update(obj);
	}

	@Override
	public MaintenancePlan queryByProperty(String name, String value) {
		return maintenancePlanDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenancePlan obj) {
		return maintenancePlanDao.save(obj);
	}

	@Override
	public MaintenancePlan queryObjById(Serializable id) {
		return maintenancePlanDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenancePlanDao.deleteById(id);
	}
	@Override
	public Serializable addMaintenancePlan(MaintenancePlan maintenancePlan, String deviceCodes,Employee employee) {
		Serializable id = maintenancePlanDao.save(maintenancePlan);
		//添加设备与保养计划的映射
		if(deviceCodes!=null&&!"".equals(deviceCodes.trim())) {
			if(deviceCodes.contains(",")) {
				String[] deviceCodesArray = deviceCodes.split(",");
				for(int i = 0;i<deviceCodesArray.length;i++) {
					Device device = deviceDao.findSingleByProperty("code", deviceCodesArray[i]);
					addDeviceMaintenancePlanMapping(maintenancePlan, device);
					addMaintenancePlanRecord(maintenancePlan, device,employee);
				}
			}else {
				Device device = deviceDao.findSingleByProperty("code", deviceCodes);
				if(device==null) {
					throw new  RuntimeException("请选择设备!");
				}
				addDeviceMaintenancePlanMapping(maintenancePlan, device);
				addMaintenancePlanRecord(maintenancePlan, device,employee);
			}
		}
		return id;
	}
	
	/**
	 * 为保养计划记录添加保养项
	 * @param deviceProjectRecordList
	 * @param maintenancePlanRecord
	 */
	private void addMaintenanceItem(List<DeviceProjectRecord> deviceProjectRecordList,MaintenancePlanRecord maintenancePlanRecord) {
		if(!CollectionUtils.isEmpty(deviceProjectRecordList)) {
			for(DeviceProjectRecord record : deviceProjectRecordList) {
				MaintenanceItem item = new MaintenanceItem();
				BeanUtils.copyProperties(record, item);
				item.setMaintenancePlanRecord(maintenancePlanRecord);
				item.setCode(record.getCode());
				maintenanceItemDao.save(item);
			}
		}
	}
	/**
	 * 添加设备保养记录
	 */
	private void addMaintenancePlanRecord(MaintenancePlan maintenancePlan,Device device,Employee employee) {
		//查找保养类别
		MaintenanceType maintenanceType = maintenanceTypeDao.findById(maintenancePlan.getMaintenanceType().getId());
		Date from = maintenancePlan.getFrom();
		Date to = maintenancePlan.getTo();
		String cycleType = maintenancePlan.getCycleType();
		//根据设备代码 查找保养
		List<DeviceProjectRecord> deviceProjectRecordList = deviceProjectRecordDao.findByHQL("from DeviceProjectRecord r where r.device.code=?0"
				+ " and r.recordTypeCode=?1 and  r.device.isDeviceManageUse=?2", new Object[] {device.getCode(),maintenanceType.getCode(),true});
		if(to.getTime()>=from.getTime()) {
			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(from);
			Calendar toCal = Calendar.getInstance();
			toCal.add(Calendar.DATE, 1);
			toCal.setTime(to);
			switch(cycleType) {
			//按班次保养
			case Constant.CycleType.FOR_CLASS:{
				List<Classes> classesList = classesDao.findAll();
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						MaintenancePlanRecord record = new MaintenancePlanRecord();
						record.setMaintenanceDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
						record.setNo(generateNo(fromCal.getTime()));
						record.setUnitType(device.getUnitType());
						record.setDevice(device);
						record.setExpectTime(maintenancePlan.getExpectTime());
						record.setMaintenanceType(maintenancePlan.getMaintenanceType());
						if(!CollectionUtils.isEmpty(classesList)) {
							for(Classes c : classesList) {
								MaintenancePlanRecord r = new MaintenancePlanRecord();
								BeanUtils.copyProperties(record, r);
								r.setClassCode(c.getCode());
								r.setClassName(c.getName());
								maintenancePlanRecordService.addMaintenancePlanRecord(r, employee);
								//为保养计划记录添加保养计划项
								addMaintenanceItem(deviceProjectRecordList,r);
							}
						}
						fromCal.add(Calendar.DATE, 1);
					}else {
						break;
					}
				}
					MaintenancePlanRecord record = new MaintenancePlanRecord();
				record.setMaintenanceDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
					record.setNo(generateNo(fromCal.getTime()));
					record.setUnitType(device.getUnitType());
					record.setDevice(device);
					record.setExpectTime(maintenancePlan.getExpectTime());
					record.setMaintenanceType(maintenancePlan.getMaintenanceType());
				for(Classes c : classesList) {
					MaintenancePlanRecord r = new MaintenancePlanRecord();
					BeanUtils.copyProperties(record, r);
					r.setClassCode(c.getCode());
					r.setClassName(c.getName());
					maintenancePlanRecordService.addMaintenancePlanRecord(r, employee);
					//为保养计划记录添加保养计划项
					addMaintenanceItem(deviceProjectRecordList,r);
				}
				break;
			}
			//按天保养
			case Constant.CycleType.FOR_DAY:{
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						MaintenancePlanRecord record = new MaintenancePlanRecord();
						record.setMaintenanceDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
						record.setNo(generateNo(fromCal.getTime()));
						record.setUnitType(device.getUnitType());
						record.setDevice(device);
						record.setExpectTime(maintenancePlan.getExpectTime());
						record.setMaintenanceType(maintenancePlan.getMaintenanceType());
						maintenancePlanRecordService.addMaintenancePlanRecord(record, employee);
						//为保养计划记录添加保养计划项
						addMaintenanceItem(deviceProjectRecordList,record);
						fromCal.add(Calendar.DATE, 1);
					}else {
						break;
					}
				}
				MaintenancePlanRecord record = new MaintenancePlanRecord();
				record.setMaintenanceDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
				record.setNo(generateNo(fromCal.getTime()));
				record.setUnitType(device.getUnitType());
				record.setDevice(device);
				record.setExpectTime(maintenancePlan.getExpectTime());
				record.setMaintenanceType(maintenancePlan.getMaintenanceType());
				maintenancePlanRecordService.addMaintenancePlanRecord(record, employee);
				//为保养计划记录添加保养计划项
				addMaintenanceItem(deviceProjectRecordList,record);
				break;
			}
			//按周进行保养
			case Constant.CycleType.FOR_WEEK:{
				//获取周值，如：周一，周二等
				String week = maintenancePlan.getValue();
				int weekIntValue = 1; //周日
				switch(week) {
				case "周一": weekIntValue = 2;break;
				case "周二": weekIntValue = 3;break;
				case "周三": weekIntValue = 4;break;
				case "周四": weekIntValue = 5;break;
				case "周五": weekIntValue = 6;break;
				case "周六": weekIntValue = 7;break;
				case "周日": weekIntValue = 1;break;
				default:weekIntValue = 1;
				}
				List<Date> cals = new ArrayList<>();
				//查找给定周的日期
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						if(fromCal.get(Calendar.DAY_OF_WEEK)==weekIntValue) {
							 
							cals.add(fromCal.getTime());
						}
						fromCal.add(Calendar.DATE, 1);
					}else {
						break;
					}
				}
				if(fromCal.get(Calendar.DAY_OF_WEEK)==weekIntValue) {
					cals.add(fromCal.getTime());
				}
				if(!CollectionUtils.isEmpty(cals)) {
					for(Date c : cals) {
						MaintenancePlanRecord record = new MaintenancePlanRecord();
						record.setMaintenanceDate(DateStringUtil.toDaysEnd(c));
						record.setNo(generateNo(c));
						record.setUnitType(device.getUnitType());
						record.setDevice(device);
						record.setExpectTime(maintenancePlan.getExpectTime());
						record.setMaintenanceType(maintenancePlan.getMaintenanceType());
						maintenancePlanRecordService.addMaintenancePlanRecord(record, employee);
						//为保养计划记录添加保养计划项
						addMaintenanceItem(deviceProjectRecordList,record);
					}
				}
				break;
			}
			//按月保养
			case Constant.CycleType.FOR_MONTH:{
				//每月的几号，如：1，2,3号等
				String dayOfMonth = maintenancePlan.getValue();
				int day = 1;
				if(StringUtils.isEmpty(dayOfMonth)) {
					day = 1;
				}else {
					day = Integer.valueOf(dayOfMonth);
				}
				List<Date> cals = new ArrayList<>();
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						System.out.println("月："+fromCal.get(Calendar.YEAR));
						if(fromCal.get(Calendar.DAY_OF_MONTH)==day) {
							cals.add(fromCal.getTime());
						}else if((day==31&&fromCal.get(Calendar.DAY_OF_MONTH)==30&&(fromCal.get(Calendar.MONTH)==3||fromCal.get(Calendar.MONTH)==5
								||fromCal.get(Calendar.MONTH)==8||fromCal.get(Calendar.MONTH)==10))){
							cals.add(fromCal.getTime());
						}else if(fromCal.get(Calendar.YEAR)%4==0&&day>28&&fromCal.get(Calendar.DAY_OF_MONTH)==29&&fromCal.get(Calendar.MONTH)==1){
							cals.add(fromCal.getTime());
						}else if(fromCal.get(Calendar.YEAR)%4!=0&&day>28&&fromCal.get(Calendar.DAY_OF_MONTH)==28&&fromCal.get(Calendar.MONTH)==1){
							cals.add(fromCal.getTime());
						}
						fromCal.add(Calendar.DATE, 1);
					}else {
						break;
					}
				}
				if(fromCal.get(Calendar.DAY_OF_MONTH)==day) {
					cals.add(fromCal.getTime());
				}
				if(!CollectionUtils.isEmpty(cals)) {
					for(Date c : cals) {
						MaintenancePlanRecord record = new MaintenancePlanRecord();
						record.setMaintenanceDate(DateStringUtil.toDaysEnd(c));
						record.setNo(generateNo(c));
						record.setUnitType(device.getUnitType());
						record.setDevice(device);
						record.setExpectTime(maintenancePlan.getExpectTime());
						record.setMaintenanceType(maintenancePlan.getMaintenanceType());
						maintenancePlanRecordService.addMaintenancePlanRecord(record, employee);
						//为保养计划记录添加保养计划项
						addMaintenanceItem(deviceProjectRecordList,record);
					}
				}
				break;
			}
			//隔多长时间保养一次
			case Constant.CycleType.FOR_DURATION:{
				//隔的天数
				String days = maintenancePlan.getValue();
				int daysIntValue = 1;
				if(!StringUtils.isEmpty(days)) {
					daysIntValue = Integer.valueOf(days);
				}
				//起始日期
				Date startDate = maintenancePlan.getStartDate();
				if(startDate == null) {
					startDate = maintenancePlan.getFrom();
				}
				List<Date> cals = new ArrayList<>();
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(startDate);
				while(startCal.getTimeInMillis()<toCal.getTimeInMillis()) {
					cals.add(startCal.getTime());
					startCal.add(Calendar.DAY_OF_MONTH, daysIntValue);
				}
				if(!CollectionUtils.isEmpty(cals)) {
					for(Date c : cals) {
						MaintenancePlanRecord record = new MaintenancePlanRecord();
						record.setMaintenanceDate(DateStringUtil.toDaysEnd(c));
						record.setNo(generateNo(c));
						record.setUnitType(device.getUnitType());
						record.setDevice(device);
						record.setExpectTime(maintenancePlan.getExpectTime());
						record.setMaintenanceType(maintenancePlan.getMaintenanceType());
						maintenancePlanRecordService.addMaintenancePlanRecord(record, employee);
						//为保养计划记录添加保养计划项
						addMaintenanceItem(deviceProjectRecordList,record);
					}
				}
				break;
			}
			default: throw new RuntimeException("请选择保养周期!");
			}
		}
	}
	/**
	 * 生成点检单号
	 * @param date
	 * @return
	 */
	private String generateNo(Date date) {
		String no = maintenancePlanRecordService.queryMaxNoByDate(date);
		if(StringUtils.isEmpty(no)) {
			no = format.format(date) + "001";
		}else {
			String num = no.substring(8);
			int serno = Integer.valueOf(num) + 1;
			if(serno>=100) {
				no = no.substring(0, 8) + serno;
			}else if(serno>=10) {
				no = no.substring(0,8) + "0" + serno;
			}else {
				no = no.substring(0,8) + "00" + serno;
			}
		}
		return no;
	}
	private void addDeviceMaintenancePlanMapping(MaintenancePlan maintenancePlan,Device device) {
		DeviceMaintenancePlanMapping dcpm = new DeviceMaintenancePlanMapping();
		dcpm.setMaintenancePlan(maintenancePlan);
		dcpm.setDevice(device);
		deviceMaintenancePlanMappingDao.save(dcpm);
	}
}
