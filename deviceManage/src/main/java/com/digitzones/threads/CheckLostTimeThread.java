package com.digitzones.threads;

import com.digitzones.constants.Constant;
import com.digitzones.model.Classes;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.LostTimeRecord;
import com.digitzones.model.User;
import com.digitzones.service.IClassesService;
import com.digitzones.service.ILostTimeRecordService;
import com.digitzones.service.IProcessRecordService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查是否损时线程
 * @author zdq
 * 2018年9月5日
 */
public class CheckLostTimeThread extends Thread {
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private Float standardBeat;
	private User user;
	private Map<String,Object> args;
	private Date preProcessRecordDate ;
	private Long deviceSiteId;
	private Integer threshold;
	private IProcessRecordService processRecordService;
	private IClassesService classService;
	public void setProcessRecordService(IProcessRecordService processRecordService) {
		this.processRecordService = processRecordService;
	}

	public IClassesService getClassService() {
		return classService;
	}

	public void setClassService(IClassesService classService) {
		this.classService = classService;
	}

	public ILostTimeRecordService getLostTimeRecordService() {
		return lostTimeRecordService;
	}

	public void setLostTimeRecordService(ILostTimeRecordService lostTimeRecordService) {
		this.lostTimeRecordService = lostTimeRecordService;
	}

	private ILostTimeRecordService lostTimeRecordService;
	private Calendar calendar = Calendar.getInstance();
	public CheckLostTimeThread(Long deviceSiteId, Float threshold, Date preProcessRecordDate, User user, Map<String,Object> args, Float standardBeat) {
		this.deviceSiteId=deviceSiteId;
		double threshold1 = Math.ceil(threshold);
		long threshold2 = Math.round(threshold1);
		this.threshold= (int)threshold2;
		this.preProcessRecordDate=preProcessRecordDate;
		this.user=user;
		this.args=args;
		this.standardBeat=standardBeat;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(this.threshold * 1000);
			//查询当前时间和产生当前线程的执行记录的时间之间是否有新的执行记录
			int count = processRecordService.queryCountBetween(this.preProcessRecordDate, new Date(), deviceSiteId);
			if(count<=0) {
				//添加损时
				LostTimeRecord lostTimeRecord = new LostTimeRecord();
				calendar.setTime(this.preProcessRecordDate);
				lostTimeRecord.setBeginTime(new Date(calendar.getTime().getTime()+(long)(standardBeat*1000)));
				//查询当前班次
				Classes c = classService.queryCurrentClasses();
				if(c!=null) {
					lostTimeRecord.setClassesCode(c.getCode());
					lostTimeRecord.setClassesName(c.getName());
					
					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTime(c.getStartTime());
					startCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
					startCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
					startCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

					Calendar endCalendar = Calendar.getInstance();
					endCalendar.setTime(c.getEndTime());
					endCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
					endCalendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
					endCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
					//跨天
					if(startCalendar.after(endCalendar)) {
						calendar.add(Calendar.DATE, -1);
						lostTimeRecord.setForLostTimeRecordDate(yyyyMMdd.format(calendar.getTime()));
					}else {
						lostTimeRecord.setForLostTimeRecordDate(yyyyMMdd.format(calendar.getTime()));
					}
				}
				DeviceSite deviceSite = new DeviceSite();
				deviceSite.setId(this.deviceSiteId);
				lostTimeRecord.setLostTimeTime(new Date());
				lostTimeRecord.setDeviceSite(deviceSite);
				Serializable id = lostTimeRecordService.addLostTimeRecord(lostTimeRecord, user, args);
				//执行损时申请
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
