package com.digitzones.vo;
import java.util.Date;

import com.digitzones.model.DeviceSite;
/**
 * 按灯记录管理
 * @author zdq
 * 2018年6月4日
 */
public class PressLightRecordVO {
	private Long id;
	/**按灯日期*/
	private Date pressLightTime;
	/**按灯类别名称*/
	private String pressLightTypeName;
	/**按灯类别代码*/
	private String pressLightTypeCode;
	/**按灯类别ID*/
	private Long pressLightTypeId;
	/**是否停机*/
	private String halt;
	/**按灯人员id*/
	private Long pressLightUserId;
	/**按灯人员名称*/
	private String pressLightUserName;
	/**熄灯人员id*/
	private Long lightOutUserId;
	/**熄灯人员名称*/
	private String lightOutUserName;
	/**确认人员id*/
	private Long confirmUserId;
	/**确认人员名称*/
	private String confirmUserName;
	/**恢复人员id*/
	private Long recoverUserId;
	/**恢复人员名称*/
	private String recoverUserName;
	/**故障原因,原因小类*/
	private String reason;
	/**按灯代码*/
	private String pressLightCode;
	/**恢复方法*/
	private String recoverMethod;
	/**恢复时间*/
	private String recoverTime;
	/**熄灯时间*/
	private String lightOutTime;
	/**确认时间*/
	private String confirmTime;
	/**按灯的设备*/
	private DeviceSite deviceSite;
	/**删除标识*/
	private String deleted;
	/**是否恢复*/
	private String recovered;
	
	public String getHalt() {
		return halt;
	}
	public void setHalt(String halt) {
		this.halt = halt;
	}
	public String getRecoverTime() {
		return recoverTime;
	}
	public void setRecoverTime(String recoverTime) {
		this.recoverTime = recoverTime;
	}
	public String getLightOutTime() {
		return lightOutTime;
	}
	public void setLightOutTime(String lightOutTime) {
		this.lightOutTime = lightOutTime;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getRecovered() {
		return recovered;
	}
	public void setRecovered(String recovered) {
		this.recovered = recovered;
	}
	public Long getPressLightTypeId() {
		return pressLightTypeId;
	}
	public void setPressLightTypeId(Long pressLightTypeId) {
		this.pressLightTypeId = pressLightTypeId;
	}
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getPressLightTime() {
		return pressLightTime;
	}
	public void setPressLightTime(Date pressLightTime) {
		this.pressLightTime = pressLightTime;
	}
	public String getPressLightTypeName() {
		return pressLightTypeName;
	}
	public void setPressLightTypeName(String pressLightTypeName) {
		this.pressLightTypeName = pressLightTypeName;
	}
	public String getPressLightTypeCode() {
		return pressLightTypeCode;
	}
	public void setPressLightTypeCode(String pressLightTypeCode) {
		this.pressLightTypeCode = pressLightTypeCode;
	}
	public Long getPressLightUserId() {
		return pressLightUserId;
	}
	public void setPressLightUserId(Long pressLightUserId) {
		this.pressLightUserId = pressLightUserId;
	}
	public String getPressLightUserName() {
		return pressLightUserName;
	}
	public void setPressLightUserName(String pressLightUserName) {
		this.pressLightUserName = pressLightUserName;
	}
	public Long getLightOutUserId() {
		return lightOutUserId;
	}
	public void setLightOutUserId(Long lightOutUserId) {
		this.lightOutUserId = lightOutUserId;
	}
	public String getLightOutUserName() {
		return lightOutUserName;
	}
	public void setLightOutUserName(String lightOutUserName) {
		this.lightOutUserName = lightOutUserName;
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
	public Long getRecoverUserId() {
		return recoverUserId;
	}
	public void setRecoverUserId(Long recoverUserId) {
		this.recoverUserId = recoverUserId;
	}
	public String getRecoverUserName() {
		return recoverUserName;
	}
	public void setRecoverUserName(String recoverUserName) {
		this.recoverUserName = recoverUserName;
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
	public String getPressLightCode() {
		return pressLightCode;
	}
	public void setPressLightCode(String pressLightCode) {
		this.pressLightCode = pressLightCode;
	}
	
}
