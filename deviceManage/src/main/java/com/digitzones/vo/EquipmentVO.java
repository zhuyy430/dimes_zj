package com.digitzones.vo;
import com.digitzones.model.EquipmentType;
/**
 * 装备值对象
 * @author zdq
 * 2018年7月17日
 */
public class EquipmentVO {
	private Long id;
	private String code;
	/**计量累积*/
	private Float cumulation;
	/**计量差异*/
	private Float measurementDifference;
	/**质保期*/
	private Float warrantyPeriod;
	/**装备类型*/
	private EquipmentType equipmentType;
	/**装备图片*/
	private String picName;
	/**记录是量具还是装备*/
	private String baseCode;
	/**二维码图片路径*/
	private String qrPath;
	/**备注*/
	private String note;
	/**状态*/
	private String status;
	/**入库日期*/
	private String inWarehouseDate;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**是否停用*/
	protected Boolean disabled = false;
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
	public Float getCumulation() {
		return cumulation;
	}
	public void setCumulation(Float cumulation) {
		this.cumulation = cumulation;
	}
	public Float getMeasurementDifference() {
		return measurementDifference;
	}
	public void setMeasurementDifference(Float measurementDifference) {
		this.measurementDifference = measurementDifference;
	}
	public Float getWarrantyPeriod() {
		return warrantyPeriod;
	}
	public void setWarrantyPeriod(Float warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getInWarehouseDate() {
		return inWarehouseDate;
	}

	public void setInWarehouseDate(String inWarehouseDate) {
		this.inWarehouseDate = inWarehouseDate;
	}
}
