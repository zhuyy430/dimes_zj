package com.digitzones.devmgr.dto;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 员工排班计划
 * @author zdq
 * 2018年12月18日
 */
public class EmployeeSchedulingDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	/**点检计划开始时间*/
	private Date from;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	/**点检计划结束时间*/
	private Date to;
	/**以逗号间隔的员工编码*/
	private String employeeCodes;
	/**以逗号间隔的员工名称*/
	private String employeeNames;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public String getEmployeeCodes() {
		return employeeCodes;
	}
	public void setEmployeeCodes(String employeeCodes) {
		this.employeeCodes = employeeCodes;
	}
	public String getEmployeeNames() {
		return employeeNames;
	}
	public void setEmployeeNames(String employeeNames) {
		this.employeeNames = employeeNames;
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
