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
import com.digitzones.devmgr.dao.ICheckingPlanDao;
import com.digitzones.devmgr.dao.ICheckingPlanRecordDao;
import com.digitzones.devmgr.dao.IDeviceCheckingPlanMappingDao;
import com.digitzones.devmgr.model.CheckingPlan;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.DeviceCheckingPlanMapping;
import com.digitzones.devmgr.service.ICheckingPlanService;
import com.digitzones.model.Classes;
import com.digitzones.model.Device;
import com.digitzones.model.Pager;
@Service
public class CheckingPlanServiceImpl implements ICheckingPlanService {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	@Autowired
	private ICheckingPlanDao checkingPlanDao;
	@Autowired
	private IDeviceDao deviceDao;
	@Autowired
	private IDeviceCheckingPlanMappingDao deviceCheckingPlanMappingDao;
	@Autowired
	private ICheckingPlanRecordDao checkingPlanRecordDao;
	@Autowired
	private IClassesDao  classesDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return checkingPlanDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(CheckingPlan obj) {
		checkingPlanDao.update(obj);
	}

	@Override
	public CheckingPlan queryByProperty(String name, String value) {
		return checkingPlanDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(CheckingPlan obj) {
		return checkingPlanDao.save(obj);
	}

	@Override
	public CheckingPlan queryObjById(Serializable id) {
		return checkingPlanDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		checkingPlanDao.deleteById(id);
	}

	@Override
	public Serializable addCheckingPlan(CheckingPlan checkingPlan, String deviceCodes,String classesCode) {
		Serializable id = checkingPlanDao.save(checkingPlan);
		//添加设备与点检计划的映射
		if(deviceCodes!=null&&!"".equals(deviceCodes.trim())) {
			if(deviceCodes.contains(",")) {
				String[] deviceCodesArray = deviceCodes.split(",");
				for(int i = 0;i<deviceCodesArray.length;i++) {
					Device device = deviceDao.findSingleByProperty("code", deviceCodesArray[i]);
					addDeviceCheckingPlanMapping(checkingPlan, device);
					addCheckingPlanRecord(checkingPlan, device,classesCode);
				}
			}else {
				Device device = deviceDao.findSingleByProperty("code", deviceCodes);
				if(device==null) {
					throw new  RuntimeException("请选择设备!");
				}
				addDeviceCheckingPlanMapping(checkingPlan, device);
				addCheckingPlanRecord(checkingPlan, device,classesCode);
			}
		}
		return id;
	}
	/**
	 * 添加设备点检记录
	 */
	private void addCheckingPlanRecord(CheckingPlan checkingPlan,Device device,String classesCode) {
		Date from = checkingPlan.getFrom();
		Date to = checkingPlan.getTo();
		String cycleType = checkingPlan.getCycleType();

		if(to.getTime()>=from.getTime()) {
			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(from);
			Calendar toCal = Calendar.getInstance();
			toCal.add(Calendar.DATE, 1);
			toCal.setTime(to);
			switch(cycleType) {
			//按班次点检
			case Constant.CycleType.FOR_CLASS:{
				Classes c = classesDao.findSingleByProperty("code", classesCode);
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						CheckingPlanRecord record = new CheckingPlanRecord();
						record.setCheckingDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
						record.setNo(generateNo(fromCal.getTime()));
						record.setDeviceCode(device.getCode());
						record.setDeviceName(device.getName());
						record.setUnitType(device.getUnitType());
						if(c!=null) {
								CheckingPlanRecord r = new CheckingPlanRecord();
								BeanUtils.copyProperties(record, r);
								r.setClassCode(c.getCode());
								r.setClassName(c.getName());
								checkingPlanRecordDao.save(r);
						}
						fromCal.add(Calendar.DATE, 1);
					}else {
						break;
					}
				}
					CheckingPlanRecord record = new CheckingPlanRecord();
					record.setCheckingDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
					record.setNo(generateNo(fromCal.getTime()));
					record.setDeviceCode(device.getCode());
					record.setDeviceName(device.getName());
					record.setUnitType(device.getUnitType());
					CheckingPlanRecord r = new CheckingPlanRecord();
					BeanUtils.copyProperties(record, r);
					r.setClassCode(c.getCode());
					r.setClassName(c.getName());
					checkingPlanRecordDao.save(r);
				break;
			}
			//按天点检
			case Constant.CycleType.FOR_DAY:{
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						CheckingPlanRecord record = new CheckingPlanRecord();
						record.setCheckingDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
						record.setNo(generateNo(fromCal.getTime()));
						record.setDeviceCode(device.getCode());
						record.setDeviceName(device.getName());
						record.setUnitType(device.getUnitType());
						checkingPlanRecordDao.save(record);
						fromCal.add(Calendar.DATE, 1);
					}else {
						break;
					}
				}
				CheckingPlanRecord record = new CheckingPlanRecord();
				record.setCheckingDate(DateStringUtil.toDaysEnd(fromCal.getTime()));
				record.setNo(generateNo(fromCal.getTime()));
				record.setDeviceCode(device.getCode());
				record.setDeviceName(device.getName());
				record.setUnitType(device.getUnitType());
				checkingPlanRecordDao.save(record);
				break;
			}
			//按周进行点检
			case Constant.CycleType.FOR_WEEK:{
				//获取周值，如：周一，周二等
				String week = checkingPlan.getValue();
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
						CheckingPlanRecord record = new CheckingPlanRecord();
						record.setCheckingDate(DateStringUtil.toDaysEnd(c));
						record.setNo(generateNo(c));
						record.setDeviceCode(device.getCode());
						record.setDeviceName(device.getName());
						record.setUnitType(device.getUnitType());
						checkingPlanRecordDao.save(record);
					}
				}
				break;
			}
			//按月点检
			case Constant.CycleType.FOR_MONTH:{
				//每月的几号，如：1，2,3号等
				String dayOfMonth = checkingPlan.getValue();
				int day = 1;
				if(StringUtils.isEmpty(dayOfMonth)) {
					day = 1;
				}else {
					day = Integer.valueOf(dayOfMonth);
				}
				List<Date> cals = new ArrayList<>();
				while(true) {
					if(!DateUtils.isSameDay(fromCal, toCal)) {
						if(fromCal.get(Calendar.DAY_OF_MONTH)==day) {
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
						CheckingPlanRecord record = new CheckingPlanRecord();
						record.setCheckingDate(DateStringUtil.toDaysEnd(c));
						record.setNo(generateNo(c));
						record.setDeviceCode(device.getCode());
						record.setDeviceName(device.getName());
						record.setUnitType(device.getUnitType());
						checkingPlanRecordDao.save(record);
					}
				}
				break;
			}
			//隔多长时间点检一次
			case Constant.CycleType.FOR_DURATION:{
				//隔的天数
				String days = checkingPlan.getValue();
				int daysIntValue = 1;
				if(!StringUtils.isEmpty(days)) {
					daysIntValue = Integer.valueOf(days);
				}
				//起始日期
				Date startDate = checkingPlan.getStartDate();
				if(startDate == null) {
					startDate = checkingPlan.getFrom();
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
						CheckingPlanRecord record = new CheckingPlanRecord();
						record.setCheckingDate(DateStringUtil.toDaysEnd(c));
						record.setNo(generateNo(c));
						record.setDeviceCode(device.getCode());
						record.setDeviceName(device.getName());
						record.setUnitType(device.getUnitType());
						checkingPlanRecordDao.save(record);
					}
				}
				break;
			}
			default: throw new RuntimeException("请选择点检周期!");
			}
		}
	}
	/**
	 * 生成点检单号
	 * @param date
	 * @return
	 */
	private String generateNo(Date date) {
		String no = checkingPlanRecordDao.queryMaxNoByDate(date);
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
	
	private void addDeviceCheckingPlanMapping(CheckingPlan checkingPlan,Device device) {
		DeviceCheckingPlanMapping dcpm = new DeviceCheckingPlanMapping();
		dcpm.setCheckingPlan(checkingPlan);
		dcpm.setDevice(device);
		deviceCheckingPlanMappingDao.save(dcpm);
	}
	
}
