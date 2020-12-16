package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 维修记录
 * @author zhuyy430
 *
 */
@Entity
@Table(name="MAINTENANCESTAFFRECORD")
public class MaintenanceStaffRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**员工编号*/
	private String code;
	/**员工名称*/
	private String name;
	/**派单人员编号*/
	private String assignCode;
	/**派单人员名称*/
	private String assignName;
	/**接单类型*/
	private String receiveType;
	/**派单时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date assignTime;
	/**接单时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date receiveTime;
	/**完成时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date completeTime;
	/**维修单*/
	private DeviceRepair deviceRepair;
	/**维修用时：完成时间-报修时间 ，单位：分*/
	private Double maintenanceTime;
	/**占用工时*/
	private Double occupyTime;
	public Double getMaintenanceTime() {
		return maintenanceTime;
	}
	public void setMaintenanceTime(Double maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
		if(occupyTime==null || occupyTime==0) {
			occupyTime = this.maintenanceTime;
		}
	}
	public Double getOccupyTime() {
		return occupyTime;
	}
	public void setOccupyTime(Double occupyTime) {
		this.occupyTime = occupyTime;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	public String getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	@ManyToOne
	@JoinColumn(name="DEVICEREPAIR_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceRepair getDeviceRepair() {
		return deviceRepair;
	}
	public void setDeviceRepair(DeviceRepair deviceRepair) {
		this.deviceRepair = deviceRepair;
	}
	public Date getAssignTime() {
		return assignTime;
	}
	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public String getAssignCode() {
		return assignCode;
	}
	public void setAssignCode(String assignCode) {
		this.assignCode = assignCode;
	}
	public String getAssignName() {
		return assignName;
	}
	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}
}
