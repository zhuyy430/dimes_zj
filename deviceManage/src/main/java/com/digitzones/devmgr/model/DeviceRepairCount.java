package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 报修单状态统计
 * @author zhuyy430
 *
 */
@Entity
@Table(name="DEVICEREPAIRCOUNT")
public class DeviceRepairCount implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**新增报警数*/
	private Long newAlarms;
	/**维修完成数*/
	private Long repairCompleted;
	/**未完成报警*/
	private Long outstandingAlarms;
	/**已报警Id*/
	private String alarmeds;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNewAlarms() {
		return newAlarms;
	}
	public void setNewAlarms(Long newAlarms) {
		this.newAlarms = newAlarms;
	}
	public Long getRepairCompleted() {
		return repairCompleted;
	}
	public void setRepairCompleted(Long repairCompleted) {
		this.repairCompleted = repairCompleted;
	}
	public Long getOutstandingAlarms() {
		return outstandingAlarms;
	}
	public void setOutstandingAlarms(Long outstandingAlarms) {
		this.outstandingAlarms = outstandingAlarms;
	}
	public String getAlarmeds() {
		return alarmeds;
	}
	public void setAlarmeds(String alarmeds) {
		this.alarmeds = alarmeds;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
