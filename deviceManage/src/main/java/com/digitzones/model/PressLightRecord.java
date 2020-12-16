package com.digitzones.model;
import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 按灯记录管理
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="PRESSLIGHTRECORD")
public class PressLightRecord {
	private Long id;
	/**按灯日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date pressLightTime;
	/**按灯类别名称*/
	private String pressLightTypeName;
	/**按灯类别代码*/
	private String pressLightTypeCode;
	/**按灯类别ID*/
	private Long pressLightTypeId;
	/**是否停机*/
	private Boolean halt;
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
	private Date recoverTime;
	/**熄灯时间*/
	private Date lightOutTime;
	/**确认时间*/
	private Date confirmTime;
	/**按灯的设备*/
	private DeviceSite deviceSite;
	/**删除标识*/
	private Boolean deleted = false;
	/**是否恢复*/
	private Boolean recovered;
	
	public Boolean getRecovered() {
		return recovered;
	}
	public void setRecovered(Boolean recovered) {
		this.recovered = recovered;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public Long getPressLightTypeId() {
		return pressLightTypeId;
	}
	public void setPressLightTypeId(Long pressLightTypeId) {
		this.pressLightTypeId = pressLightTypeId;
	}
	@ManyToOne
	@JoinColumn(name="DEVICESITE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Temporal(TemporalType.TIMESTAMP)
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
	public Boolean getHalt() {
		return halt;
	}
	public void setHalt(Boolean halt) {
		this.halt = halt;
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecoverTime() {
		return recoverTime;
	}
	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLightOutTime() {
		return lightOutTime;
	}
	public void setLightOutTime(Date lightOutTime) {
		this.lightOutTime = lightOutTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getPressLightCode() {
		return pressLightCode;
	}
	public void setPressLightCode(String pressLightCode) {
		this.pressLightCode = pressLightCode;
	}
	
}
