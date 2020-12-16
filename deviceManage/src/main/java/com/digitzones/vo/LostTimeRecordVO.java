package com.digitzones.vo;

import com.digitzones.model.DeviceSite;

/**
 * 损时
 * @author zdq
 * 2018年6月21日
 */
public class LostTimeRecordVO {
	private Long id;
	/**损时日期*/
	private String lostTimeTime;
	/**损时类别名称 : 同按灯类别*/
	private String lostTimeTypeName;
	/**损时类别代码 : 同按灯类别代码*/
	private String lostTimeTypeCode;
	/**是否停机*/
	private Boolean halt;
	/**记录人员id*/
	private Long recordUserId;
	/**记录人员名称*/
	private String recordUserName;
	/**确认人员id*/
	private Long confirmUserId;
	/**确认人员名称*/
	private String confirmUserName;
	/**故障原因*/
	private String reason;
	/**按灯代码*/
	private String pressLightCode;
	/**恢复方法*/
	private String recoverMethod;
	/**确认时间*/
	private String confirmTime;
	/**损时开始时间*/
	private String beginTime;
	/**损时结束时间*/
	private String endTime;
	/**合计,结束时间-开始时间，单位为分钟*/
	private Double sumOfLostTime;
	/**详细描述*/
	private String description;
	/**损时的站点*/
	private DeviceSite deviceSite;
	/**班次代码*/
	private String classesCode;
	/**班次名称*/
	private  String classesName;
	/**删除标识*/
	private Boolean deleted = false;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLostTimeTime() {
		return lostTimeTime;
	}
	public void setLostTimeTime(String lostTimeTime) {
		this.lostTimeTime = lostTimeTime;
	}
	public String getLostTimeTypeName() {
		return lostTimeTypeName;
	}
	public void setLostTimeTypeName(String lostTimeTypeName) {
		this.lostTimeTypeName = lostTimeTypeName;
	}
	public String getLostTimeTypeCode() {
		return lostTimeTypeCode;
	}
	public void setLostTimeTypeCode(String lostTimeTypeCode) {
		this.lostTimeTypeCode = lostTimeTypeCode;
	}
	public Boolean getHalt() {
		return halt;
	}
	public void setHalt(Boolean halt) {
		this.halt = halt;
	}
	public Long getRecordUserId() {
		return recordUserId;
	}
	public void setRecordUserId(Long recordUserId) {
		this.recordUserId = recordUserId;
	}
	public String getRecordUserName() {
		return recordUserName;
	}
	public void setRecordUserName(String recordUserName) {
		this.recordUserName = recordUserName;
	}
	public Long getConfirmUserId() {
		return confirmUserId;
	}
	public void setConfirmUserId(Long confirmUserId) {
		this.confirmUserId = confirmUserId;
	}
	public String getConfirmUserName() {
		return confirmUserName;
	}
	public void setConfirmUserName(String confirmUserName) {
		this.confirmUserName = confirmUserName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRecoverMethod() {
		return recoverMethod;
	}
	public void setRecoverMethod(String recoverMethod) {
		this.recoverMethod = recoverMethod;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getSumOfLostTime() {
		return sumOfLostTime;
	}
	public void setSumOfLostTime(Double sumOfLostTime) {
		this.sumOfLostTime = sumOfLostTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
	public String getClassesCode() {
		return classesCode;
	}
	public void setClassesCode(String classesCode) {
		this.classesCode = classesCode;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getPressLightCode() {
		return pressLightCode;
	}
	public void setPressLightCode(String pressLightCode) {
		this.pressLightCode = pressLightCode;
	}
	
}
