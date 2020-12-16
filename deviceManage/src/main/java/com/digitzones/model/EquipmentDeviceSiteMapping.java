package com.digitzones.model;

import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * 站点关联装备
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="EQUIPMENT_DEVICESITE")
public class EquipmentDeviceSiteMapping {
	private Long id;
	/**站点*/
	private DeviceSite deviceSite;
	/**装备或量具*/
	private Equipment equipment;
	/**工单号*/
	private String workSheetCode;
	/**关联日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date mappingDate = new Date();
	/**使用频次*/
	private Float usageRate = 1f;
	/**解除绑定日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date unbindDate;
	/**是否解除了绑定*/
	 private Boolean unbind = false;
	 /**绑定人id*/
	 private Long bindUserId;
	 /**绑定人姓名*/
	 private String bindUsername;
	 /**解除绑定用户id*/
	 private Long unbindUserId;
	 /**解除绑定用户名称*/
	 private String unbindUsername;
	 /**辅助人id*/
	 private String helperId;
	 /**辅助人姓名*/
	 private String helperName;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEVICESITE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="EQUIPMENT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Equipment getEquipment() {
		return equipment;
	}
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUnbindDate() {
		return unbindDate;
	}
	public void setUnbindDate(Date unbindDate) {
		this.unbindDate = unbindDate;
	}
	public Boolean getUnbind() {
		return unbind;
	}
	public void setUnbind(Boolean unbind) {
		this.unbind = unbind;
	}
	public Long getBindUserId() {
		return bindUserId;
	}
	public void setBindUserId(Long bindUserId) {
		this.bindUserId = bindUserId;
	}
	public String getBindUsername() {
		return bindUsername;
	}
	public void setBindUsername(String bindUsername) {
		this.bindUsername = bindUsername;
	}
	public Long getUnbindUserId() {
		return unbindUserId;
	}
	public void setUnbindUserId(Long unbindUserId) {
		this.unbindUserId = unbindUserId;
	}
	public String getUnbindUsername() {
		return unbindUsername;
	}
	public void setUnbindUsername(String unbindUsername) {
		this.unbindUsername = unbindUsername;
	}
	public String getHelperId() {
		return helperId;
	}
	public void setHelperId(String helperId) {
		this.helperId = helperId;
	}
	public String getHelperName() {
		return helperName;
	}
	public void setHelperName(String helperName) {
		this.helperName = helperName;
	}
	public Float getUsageRate() {
		return usageRate;
	}
	public void setUsageRate(Float usageRate) {
		this.usageRate = usageRate;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkSheetCode() {
		return workSheetCode;
	}
	public void setWorkSheetCode(String workSheetCode) {
		this.workSheetCode = workSheetCode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMappingDate() {
		return mappingDate;
	}
	public void setMappingDate(Date mappingDate) {
		this.mappingDate = mappingDate;
	}
}
