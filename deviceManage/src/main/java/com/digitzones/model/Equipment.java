package com.digitzones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

/**
 * 装备
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="EQUIPMENT")
public class Equipment {
	/**计量累积*/
	private Float cumulation=0f;
	/**计量差异*/
	private Float measurementDifference=0f;
	/**质保期*/
	private Float warrantyPeriod;
	/**装备类型*/
	private EquipmentType equipmentType;
	/**装备图片*/
	private Blob pic;
	/**图片名称*/
	private String picName;
	/**记录是量具还是装备*/
	private String baseCode;
	/**对象唯一标识*/
	protected Long id;
	/**编号*/
	protected String code;
	/**备注*/
	protected String note;
	/**状态：在库、已关联、报废**/
	private String status="在库";

	/**出厂日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date outFactoryDate;

	public Float getWarrantyPeriod() {
		return warrantyPeriod;
	}
	public void setWarrantyPeriod(Float warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@ManyToOne
	@JoinColumn(name="EQUIPMENTTYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
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
	@JsonIgnore
	public Blob getPic() {
		return pic;
	}
	public void setPic(Blob pic) {
		this.pic = pic;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}

	public Date getOutFactoryDate() {
		return outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}
}
