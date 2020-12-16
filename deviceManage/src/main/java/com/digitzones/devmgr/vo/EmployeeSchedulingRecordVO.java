package com.digitzones.devmgr.vo;

public class EmployeeSchedulingRecordVO {
	private Long id;
	/**员工编码*/
	private String employeeCode;
	/**员工名称*/
	private String employeeName;
	/**排班日期*/
	private String schedulingDate;
	/**班次代码*/
	private String classCode;
	/**班次名称*/
	private String className;
	
	public String getSchedulingDate() {
		return schedulingDate;
	}
	public void setSchedulingDate(String schedulingDate) {
		this.schedulingDate = schedulingDate;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}

