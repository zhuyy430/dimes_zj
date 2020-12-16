package com.digitzones.devmgr.dto;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.digitzones.devmgr.model.MaintenanceType;
/**
 * 设备点检计划实体dto
 * @author zdq
 * 2018年12月18日
 */
public class MaintenancePlanDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	/**点检计划开始时间*/
	private Date from;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	/**点检计划结束时间*/
	private Date to;
	/**点检周期类型：每班，每天，每周，每月，每隔多长时间,值参照Constant.CyclePlan*/
	private String cycleType;
	/**点检周期中的具体值，如：1号，周一，每隔15天等*/
	private String value;
	/**如果点检周期为每隔多长时间点检一次，则需要填写第一次点检的日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	/**以逗号间隔的设备编码*/
	private String deviceCodes;
	/**预计用时*/
	private Double expectTime;
	public Double getExpectTime() {
		return expectTime;
	}
	public void setExpectTime(Double expectTime) {
		this.expectTime = expectTime;
	}
	/**
	 * 保养类型
	 */
	private MaintenanceType maintenanceType;
	public MaintenanceType getMaintenanceType() {
		return maintenanceType;
	}
	public void setMaintenanceType(MaintenanceType maintenanceType) {
		this.maintenanceType = maintenanceType;
	}
	public String getDeviceCodes() {
		return deviceCodes;
	}
	public void setDeviceCodes(String deviceCodes) {
		this.deviceCodes = deviceCodes;
	}
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
	public String getCycleType() {
		return cycleType;
	}
	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}