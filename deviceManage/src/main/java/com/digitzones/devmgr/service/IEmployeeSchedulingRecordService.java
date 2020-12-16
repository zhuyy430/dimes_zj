package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.dto.EmployeeSchedulingDto;
import com.digitzones.devmgr.model.EmployeeSchedulingRecord;
import com.digitzones.service.ICommonService;
/**
 * 人员排班业务逻辑
 * @author zdq
 * 2019年3月10日
 */
public interface IEmployeeSchedulingRecordService extends ICommonService<EmployeeSchedulingRecord> {
	/**
	 * 添加人员排班
	 * @param employeeSchedulingDto
	 */
	public void addEmployeeSchedulings(EmployeeSchedulingDto employeeSchedulingDto);
	/**
	 * @param code
	 */
	public List<EmployeeSchedulingRecord> queryEmployeeSchedulingRecordsByTodayAndCode(String code);
}
