package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 维修人员状态切换记录
 * @author zhuyy430
 *
 */
@Entity
@Table(name="MAINTENANCESTAFFATATUSRECORD")
public class MaintenanceStaffStatusRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**员工编号*/
	private String code;
	/**员工名称*/
	private String name;
	/**切换前状态*/
	private String changeBeforeStatus;
	/**切换后状态*/
	private String changeAfterStatus;
	/**切换日期*/
	private Date changeDate;
	/**操作人员*/
	private String operator;
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
	public String getChangeBeforeStatus() {
		return changeBeforeStatus;
	}
	public void setChangeBeforeStatus(String changeBeforeStatus) {
		this.changeBeforeStatus = changeBeforeStatus;
	}
	public String getChangeAfterStatus() {
		return changeAfterStatus;
	}
	public void setChangeAfterStatus(String changeAfterStatus) {
		this.changeAfterStatus = changeAfterStatus;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
