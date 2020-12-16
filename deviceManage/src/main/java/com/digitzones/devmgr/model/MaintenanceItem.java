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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 保养项目
 * @author zdq
 * 2019年1月4日
 */
@Entity
public class MaintenanceItem implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**项目编号*/
	private String code;
	/**项目名称*/
	private String name;
	/**记录所属类别编码：如果为保养项目，则该值为保养类别编码*/
	private String recordTypeCode;
	/**记录所属类别名称*/
	private String recordTypeName;
	/**标准*/
	private String standard;
	/**方法*/
	private String method;
	/**频次*/
	private String frequency;
	/**说明*/
	private String note;
	/**备注*/
	private String Remarks;
	/**保养日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date maintenanceDate;
	/**确认日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmDate;
	/**确认人编码，员工编码，如果用户没有关联员工，则为用户名*/
	private String confirmCode;
	/**确认人名称，员工名称，如果用户没有关联员工，则为用户名*/
	private String  confirmUser;
	/***
	 * 保养计划记录
	 */
	@ManyToOne
	@JoinColumn(name="MAINTENANCEPLANRECORD_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private MaintenancePlanRecord maintenancePlanRecord;
	public MaintenancePlanRecord getMaintenancePlanRecord() {
		return maintenancePlanRecord;
	}
	public void setMaintenancePlanRecord(MaintenancePlanRecord maintenancePlanRecord) {
		this.maintenancePlanRecord = maintenancePlanRecord;
	}
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
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getMaintenanceDate() {
		return maintenanceDate;
	}
	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getConfirmUser() {
		return confirmUser;
	}
	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}
	public String getRecordTypeCode() {
		return recordTypeCode;
	}
	public void setRecordTypeCode(String recordTypeCode) {
		this.recordTypeCode = recordTypeCode;
	}
	public String getRecordTypeName() {
		return recordTypeName;
	}
	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
}
