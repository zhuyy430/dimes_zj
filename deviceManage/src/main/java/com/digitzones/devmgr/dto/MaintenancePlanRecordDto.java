package com.digitzones.devmgr.dto;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.model.Device;
public class MaintenancePlanRecordDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * 计划保养日期
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date maintenanceDate;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
	/**保养状态*/
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
	 * 保养人名称：员工名称
	 * */
	private String employeeName;
	/**确认人编码*/
	private String confirmCode;
	/**确认人名称*/
	private String confirmName;
	private Device device;
	/**
	 * 以逗号间隔的保养图片路径
	 */
	private String picPath;
	/**实际保养日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date maintenancedDate;
	/**保养类型*/
	private MaintenanceType maintenanceType;
	
	public MaintenanceType getMaintenanceType() {
		return maintenanceType;
	}
	public void setMaintenanceType(MaintenanceType maintenanceType) {
		this.maintenanceType = maintenanceType;
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
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public Date getMaintenanceDate() {
		return maintenanceDate;
	}
	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public Date getMaintenancedDate() {
		return maintenancedDate;
	}
	public void setMaintenancedDate(Date maintenancedDate) {
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
