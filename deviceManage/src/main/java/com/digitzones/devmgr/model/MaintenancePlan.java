package com.digitzones.devmgr.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
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
/**
 * 设备保养计划实体
 * @author zdq
 * 2018年12月18日
 */
@Entity
public class MaintenancePlan implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**保养计划开始时间*/
	@Column(name="_FROM")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date from;
	/**保养计划结束时间*/
	@Column(name="_TO")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date to;
	/**保养周期类型：每班，每天，每周，每月，每隔多长时间,值参照Constant.CyclePlan*/
	private String cycleType;
	/**保养周期中的具体值，如：1号，周一，每隔15天等*/
	private String value;
	/**如果保养周期为每隔一段时间保养一次，则需要填写第一次保养的日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	/**预计用时*/
	private Double expectTime;
	
	public Double getExpectTime() {
		return expectTime;
	}
	public void setExpectTime(Double expectTime) {
		this.expectTime = expectTime;
	}
	/**保养类型*/
	@ManyToOne
	@JoinColumn(name="TYPE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	private MaintenanceType maintenanceType;
	public MaintenanceType getMaintenanceType() {
		return maintenanceType;
	}
	public void setMaintenanceType(MaintenanceType maintenanceType) {
		this.maintenanceType = maintenanceType;
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
