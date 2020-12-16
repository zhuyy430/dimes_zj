package com.digitzones.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 加工参数记录	 
 * @author zdq
 * 2018年6月20日
 */
@Entity
@Table(name="PROCESSPARAMETERRECORD")
public class ProcessParameterRecord {
	private Long id;
	/**参数代码*/
	private String parameterCode;
	/**参数名称*/
	private String parameterName;
	/**单位*/
	private String unit;
	/**备注*/
	private String note;
	/**上线*/
	private Float upLine = 0f;
	/**下线*/
	private Float lowLine = 0f;
	/**标准值*/
	private Float standardValue = 0f;
	/**时间*/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private Date currentDate;
	/**参数值*/
	private String parameterValue;
	/**状态 NG,OK*/
	private String status;
	/**状态故障代码*/
	private String statusCode;
	/**删除标识位*/
	private Boolean deleted = false;
	/**加工记录*/
	private ProcessRecord processRecord;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getParameterCode() {
		return parameterCode;
	}
	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Float getUpLine() {
		return upLine==null?0f:upLine;
	}
	public void setUpLine(Float upLine) {
		this.upLine = upLine;
	}
	public Float getLowLine() {
		return lowLine==null?0f:lowLine;
	}
	public void setLowLine(Float lowLine) {
		this.lowLine = lowLine;
	}
	public Float getStandardValue() {
		return standardValue==null?0f:standardValue;
	}
	public void setStandardValue(Float standardValue) {
		this.standardValue = standardValue;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToOne
	@JoinColumn(name="PROCESSRECORD_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProcessRecord getProcessRecord() {
		return processRecord;
	}
	public void setProcessRecord(ProcessRecord processRecord) {
		this.processRecord = processRecord;
	}
}
