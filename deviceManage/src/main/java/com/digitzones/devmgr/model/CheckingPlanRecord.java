package com.digitzones.devmgr.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import com.digitzones.constants.Constant;
/**
 * 点检记录实体
 * @author zdq
 * 2018年12月18日
 */
@Entity
public class CheckingPlanRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**点检单号
	 * 格式：日期+序列号
	 * */
	private String no;
	/**
	 * 计划点检日期
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkingDate;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
	/**点检状态*/
	private String status=Constant.Status.CHECKINGPLAN_PLAN;
	/**设备编码*/
	private String deviceCode;
	/**设备名称*/
	private String deviceName;
	/**规格型号*/
	private String unitType;
	/**
	 * 员工编码*/
	private String employeeCode;
	/**
	 * 点检人名称：员工名称
	 * */
	private String employeeName;
	/**
	 * 以逗号间隔的点检图片路径
	 */
	@Column(name="picPath",length=500)
	private String picPath;
	/**实际点检日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkedDate;
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
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
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Date getCheckedDate() {
		return checkedDate;
	}
	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCheckingDate() {
		return checkingDate;
	}
	public void setCheckingDate(Date checkingDate) {
		this.checkingDate = checkingDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
