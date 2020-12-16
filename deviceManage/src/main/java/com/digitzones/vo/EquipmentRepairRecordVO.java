package com.digitzones.vo;

import com.digitzones.model.Equipment;
import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightType;

import java.io.Serializable;
import java.util.Date;

/**
 * 装备维修记录
 * @author zdq
 * 2018年6月3日
 */
public class EquipmentRepairRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**唯一标识*/
	private Long id;
	/**装备代码*/
	private String equipmentTypeCode;
	/**装备名称*/
	private String equipmentTypeName;
	/**规则型号*/
	private String unitType;
	/**装备序号*/
	private String equipmentCode;
	/**装备Id*/
	private Long equipmentId;
	/**计量累计*/
	private Float cumulation;
	/**故障类别Code*/
	private String pressLightTypeCode;
	/**故障类别名称*/
	private String pressLightTypeName;
	/**维修原因Id*/
	private Long pressLightId;
	/**维修原因*/
	private String pressLightReason;
	/**维修时间*/
	private String repairDate;
	/**维修人员名称*/
	private String repairEmployeeName;
	/**维修人员代码*/
	private String repairEmployeeCode;
	/**维修说明*/
	private String note;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipmentTypeCode() {
		return equipmentTypeCode;
	}

	public void setEquipmentTypeCode(String equipmentTypeCode) {
		this.equipmentTypeCode = equipmentTypeCode;
	}

	public String getEquipmentTypeName() {
		return equipmentTypeName;
	}

	public void setEquipmentTypeName(String equipmentTypeName) {
		this.equipmentTypeName = equipmentTypeName;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Float getCumulation() {
		return cumulation;
	}

	public void setCumulation(Float cumulation) {
		this.cumulation = cumulation;
	}

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

	public Long getPressLightId() {
		return pressLightId;
	}

	public void setPressLightId(Long pressLightId) {
		this.pressLightId = pressLightId;
	}

	public String getPressLightReason() {
		return pressLightReason;
	}

	public void setPressLightReason(String pressLightReason) {
		this.pressLightReason = pressLightReason;
	}

	public String getRepairEmployeeName() {
		return repairEmployeeName;
	}

	public void setRepairEmployeeName(String repairEmployeeName) {
		this.repairEmployeeName = repairEmployeeName;
	}

	public String getRepairEmployeeCode() {
		return repairEmployeeCode;
	}

	public void setRepairEmployeeCode(String repairEmployeeCode) {
		this.repairEmployeeCode = repairEmployeeCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRepairDate() {
		return repairDate;
	}

	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
	}
}
