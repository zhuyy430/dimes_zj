package com.digitzones.devmgr.service;

import java.io.Serializable;

import com.digitzones.devmgr.model.MaintenancePlan;
import com.digitzones.model.Employee;
import com.digitzones.service.ICommonService;

public interface IMaintenancePlanService extends ICommonService<MaintenancePlan> {
	/**
	 * 添加设备保养计划
	 * @param maintenancePlan 设备保养计划对象
	 * @param deviceCodes 设备编码，多个以逗号间隔
	 * @return Serializable 设备保养计划id
	 */
	public Serializable addMaintenancePlan(MaintenancePlan maintenancePlan,String deviceCodes,Employee employee);
}
