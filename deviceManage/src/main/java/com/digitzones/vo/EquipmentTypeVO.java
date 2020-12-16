package com.digitzones.vo;
import com.digitzones.model.EquipmentType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 装备类型值对象实体
 * @author zdq
 * 2018年6月7日
 */
public class EquipmentTypeVO {
	private Long id;
	private String name;
	private EquipmentType parent;
	private String code;
	private Boolean disabled;
	private String note;
	private String baseCode;
	/**规格型号*/
	private String unitType;
	/**状态*/
	private String status;
	/**制造商*/
	private String manufacturer;
	/**经销商*/
	private String trader;
	/**出厂日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date outFactoryDate;
	/**计量目标*/
	private Float measurementObjective = 0f;
	/**计量类型*/
	private String measurementType;
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public Date getOutFactoryDate() {
		return outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}

	public Float getMeasurementObjective() {
		return measurementObjective;
	}

	public void setMeasurementObjective(Float measurementObjective) {
		this.measurementObjective = measurementObjective;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public EquipmentType getParent() {
		return parent;
	}
	public void setParent(EquipmentType parent) {
		this.parent = parent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
}
