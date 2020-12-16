package com.digitzones.vo;

public class PressLightVO {
	/**唯一标识*/
	private Long id;
	/**代码*/
	private String code;
	/**原因*/
	private String reason;
	/**是否停机*/
	private Boolean halt;
	/**详细描述*/
	private String description;
	/**备注*/
	private String note;
	/**是否停用*/
	private String disabled;
	/**是否为计划停机*/
	private String planHalt;
	/**按灯类型代码*/
	private String pressLightTypeCode;
	/**按灯类型名称*/
	private String pressLightTypeName;
	private String qrPath;
	
	public String getPressLightTypeCode() {
		return pressLightTypeCode;
	}
	public void setPressLightTypeCode(String pressLightTypeCode) {
		this.pressLightTypeCode = pressLightTypeCode;
	}
	public String getPressLightTypeName() {
		return pressLightTypeName;
	}
	public void setPressLightTypeName(String pressLightTypeName) {
		this.pressLightTypeName = pressLightTypeName;
	}
	public String getQrPath() {
		return qrPath;
	}
	public void setQrPath(String qrPath) {
		this.qrPath = qrPath;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Boolean getHalt() {
		return halt;
	}
	public void setHalt(Boolean halt) {
		this.halt = halt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getPlanHalt() {
		return planHalt;
	}
	public void setPlanHalt(String planHalt) {
		this.planHalt = planHalt;
	}
}
