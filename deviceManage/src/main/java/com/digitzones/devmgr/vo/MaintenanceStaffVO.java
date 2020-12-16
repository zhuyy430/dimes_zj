package com.digitzones.devmgr.vo;

import java.io.Serializable;
import java.util.Date;

import com.digitzones.model.Classes;

/**
 * 维修人员状态VO
 * @author zhuyy430
 *
 */
public class MaintenanceStaffVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**员工编号*/
	private String code;
	/**员工名称*/
	private String name;
	/**部门名称*/
	private String departmentName;
	/**岗位名称*/
	private String positionName;
	/**派单顺序*/
	private String queue;
	/**在岗状态*/
	private String onDutyStatus;
	/**工作状态*/
	private String workStatus;
	/**切换日期*/
	private Date changeDate;
	/**班次*/
	private Classes classes;
	/**联系方式*/
	private String tel;
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getOnDutyStatus() {
		return onDutyStatus;
	}
	public void setOnDutyStatus(String onDutyStatus) {
		this.onDutyStatus = onDutyStatus;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
}
