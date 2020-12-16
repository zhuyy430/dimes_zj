package com.digitzones.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
/**
 * 设备点检计划任务，每天23:59:59秒执行一次，检查是否存在未点检的记录
 * 如果存在，则更新为'未完成'
 * @author zdq
 * 2018年12月22日
 */
@Component("CheckingPlanTask")
public class CheckingPlanTask {
	@Autowired
	private ICheckingPlanRecordService checkingPlanRecordService;
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	public void checkingPlanStatus() {
		//更新点检计划信息
		checkingPlanRecordService.updateStatus2Uncomplete();
		//更新保养计划信息
		maintenancePlanRecordService.updateStatus2Uncomplete();
	}
}
