package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 设备和备件绑定记录
 * @author zdq
 * 2018年12月6日
 */
@Entity
@Table(name="DEVICESPAREPARTRECORD")
public class DeviceSparepartRecord implements Serializable {
	private static final long serialVersionUID = -4676030604242827018L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**设备id*/
	private Long deviceId;
	/**备品备件id*/
	private Long sparepartId;
	/**使用数量*/
	private int useCount;
	/**设备编码*/
	private String deviceCode;
	/**设备名称*/
	private String deviceName;
	/**备品备件编码*/
	private String sparepartCode;
	/**备品备件名称*/
	private String sparepartName;
	/**规格型号*/
	private String unitType;
	/**计量单位*/
	private String measurementUnit;
	/**备注*/
	private String note;
	/**绑定日期*/
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date bindDate;
	/**解除绑定日期*/
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date unbindDate;
	/**绑定人id*/
	private Long bindUserId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public Long getSparepartId() {
		return sparepartId;
	}
	public void setSparepartId(Long sparepartId) {
		this.sparepartId = sparepartId;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
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
	public String getSparepartCode() {
		return sparepartCode;
	}
	public void setSparepartCode(String sparepartCode) {
		this.sparepartCode = sparepartCode;
	}
	public String getSparepartName() {
		return sparepartName;
	}
	public void setSparepartName(String sparepartName) {
		this.sparepartName = sparepartName;
	}
	public Date getBindDate() {
		return bindDate;
	}
	public void setBindDate(Date bindDate) {
		this.bindDate = bindDate;
	}
	public Date getUnbindDate() {
		return unbindDate;
	}
	public void setUnbindDate(Date unbindDate) {
		this.unbindDate = unbindDate;
	}
	public Long getBindUserId() {
		return bindUserId;
	}
	public void setBindUserId(Long bindUserId) {
		this.bindUserId = bindUserId;
	}
	public String getBindUsername() {
		return bindUsername;
	}
	public void setBindUsername(String bindUsername) {
		this.bindUsername = bindUsername;
	}
	public Long getUnbindUserId() {
		return unbindUserId;
	}
	public void setUnbindUserId(Long unbindUserId) {
		this.unbindUserId = unbindUserId;
	}
	public String getUnbindUsername() {
		return unbindUsername;
	}
	public void setUnbindUsername(String unbindUsername) {
		this.unbindUsername = unbindUsername;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	/**绑定人名称*/
	private String bindUsername;
	/**解除绑定人id*/
	private Long unbindUserId;
	/**解除绑定人名称*/
	private String unbindUsername;
}
