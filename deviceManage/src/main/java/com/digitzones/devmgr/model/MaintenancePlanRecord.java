package com.digitzones.devmgr.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import com.digitzones.constants.Constant;
/**
 * 保养记录实体
 * @author zdq
 * 2018年12月18日
 */
import com.digitzones.model.Device;
@Entity
public class MaintenancePlanRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**保养单号
	 * 格式：日期+序列号
	 * */
	private String no;
	/**
	 * 计划保养日期
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date maintenanceDate;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
	/**保养状态*/
	private String status=Constant.Status.MAINTENANCEPLAN_PLAN;
	/**规格型号*/
	private String unitType;
	/**确认人编码*/
	private String confirmCode;
	/**确认人名称*/
	private String confirmName;
	/**预计用时*/
	private Double expectTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  confirmDate;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEVICE_ID",foreignKey=@ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	private Device device;
	/**
	 * 以逗号间隔的保养图片路径
	 */
	@Column(name="picPath",length=500)
	private String picPath;
	/**实际保养日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date maintenancedDate;
	/**保养类型*/
	@ManyToOne
	@JoinColumn(name="TYPE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	private MaintenanceType maintenanceType;
	
	public Double getExpectTime() {
		return expectTime;
	}
	public void setExpectTime(Double expectTime) {
		this.expectTime = expectTime;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
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
}
