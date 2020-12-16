package com.digitzones.vo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.digitzones.model.DeviceSite;
import com.digitzones.model.Equipment;
/**
 * 站点关联装备
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="EQUIPMENT_DEVICESITE")
public class EquipmentDeviceSiteMappingVO {
	private Long id;
	/**站点*/
	private DeviceSite deviceSite;
	/**装备或量具*/
	private Equipment equipment;
	/**装备或量具名称*/
	private String equipmentName;
	/**装备或量具序号*/
	private String equipmentCode;
	private Long equipmentId;
	/**工单号*/
	private String workSheetCode;
	/**关联日期*/
	private String mappingDate;
	/**使用频次*/
	private Float usageRate = 1f;
	/**解除绑定日期*/
	private String unbindDate;
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
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public Long getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getMappingDate() {
		return mappingDate;
	}
	public void setMappingDate(String mappingDate) {
		this.mappingDate = mappingDate;
	}
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
	public Equipment getEquipment() {
		return equipment;
	}
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	public String getUnbindDate() {
		return unbindDate;
	}
	public void setUnbindDate(String unbindDate) {
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
}
