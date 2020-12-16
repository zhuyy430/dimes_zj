package com.digitzones.devmgr.vo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.digitzones.constants.Constant;
import com.digitzones.model.Device;
/**
 * 保养计划记录vo
 * @author zdq
 * 2018年12月28日
 */
public class MaintenancePlanRecordVO {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**
	 * 计划保养日期
	 */
	private String maintenanceDate;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
	/**保养状态*/
	private String status=Constant.Status.CHECKINGPLAN_PLAN;
	/**设备信息 */
	private Device device;
	/**规格型号*/
	private String unitType;
	/**单号*/
	private String no;
	/**预计用时*/
	private Double expectTime;
	/**
	 * 员工编码*/
	private String employeeCode;
	/**
	 * 保养人名称：员工名称
	 * */
	private String employeeName;
	/**
	 * 以逗号间隔的保养图片路径
	 */
	private String picPath;
	/**实际保养日期*/
	private String maintenancedDate;
	/**保养类型*/
	private String maintenanceType;
	/**保养类型编码*/
	private String maintenanceTypeCode;
	private String confirmCode;
	private String confirmName;
	private String confirmDate;
	/**保养完成日期*/
	private String completeDate;
	
	public Double getExpectTime() {
		return expectTime;
	}
	public void setExpectTime(Double expectTime) {
		this.expectTime = expectTime;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public String getMaintenanceTypeCode() {
		return maintenanceTypeCode;
	}
	public void setMaintenanceTypeCode(String maintenanceTypeCode) {
		this.maintenanceTypeCode = maintenanceTypeCode;
	}
	public String getMaintenanceType() {
		return maintenanceType;
	}
	public void setMaintenanceType(String maintenanceType) {
		this.maintenanceType = maintenanceType;
	}
	public String getMaintenanceDate() {
		return maintenanceDate;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public void setMaintenanceDate(String maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public String getMaintenancedDate() {
		return maintenancedDate;
	}
	public void setMaintenancedDate(String maintenancedDate) {
		this.maintenancedDate = maintenancedDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
}
