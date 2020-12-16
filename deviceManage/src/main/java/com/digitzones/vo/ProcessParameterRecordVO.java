package com.digitzones.vo;

/**
 * 加工参数记录	 
 * @author zdq
 * 2018年6月20日
 */
public class ProcessParameterRecordVO {
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
	private Float upLine;
	/**下线*/
	private Float lowLine;
	/**标准值*/
	private Float standardValue;
	/**时间*/
	private String currentDate;
	/**参数值*/
	private String parameterValue;
	/**状态*/
	private String status;
	/**状态故障代码*/
	private String statusCode;
	/**删除标识位*/
	private Boolean deleted = false;
	
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
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
		return upLine;
	}
	public void setUpLine(Float upLine) {
		this.upLine = upLine;
	}
	public Float getLowLine() {
		return lowLine;
	}
	public void setLowLine(Float lowLine) {
		this.lowLine = lowLine;
	}
	public Float getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Float standardValue) {
		this.standardValue = standardValue;
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
}
