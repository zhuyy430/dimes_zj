package com.digitzones.devmgr.service;

import java.io.Serializable;

import com.digitzones.devmgr.model.CheckingPlan;
import com.digitzones.service.ICommonService;
/**
 * 点检计划
 * @author zdq
 * 2018年12月20日
 */
public interface ICheckingPlanService extends ICommonService<CheckingPlan> {
	/**
	 * 添加设备点检计划
	 * @param checkingPlan 设备点检计划对象
	 * @param deviceCodes 设备编码，多个以逗号间隔
	 * @return Serializable 设备点检计划id
	 */
	public Serializable addCheckingPlan(CheckingPlan checkingPlan,String deviceCodes,String classesCode);
}
