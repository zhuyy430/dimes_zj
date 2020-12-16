package com.digitzones.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 设备类别实体
 * @author zdq
 */
@Entity
@Table(name="DEVICEPARAMETER")
public class DeviceParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**参数代码*/
	private String code;
	/**参数名称*/
	private String name;
	/**参数描述*/
	private String desc;
	/**参数值类型*/
	private String valueType;
	/**上线*/
	private Float upLine;
	/**下线*/
	private Float lowLine;
	/**标准值*/
	private Float standardValue;
	/**备注*/
	private String note;
	/**所属设备*/
	private Device device;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="_DESC")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public Float getUpLine() {
		return upLine;
	}
	public void setUpLine(Float upLine) {
		this.upLine = upLine;
	}
	public Float getLowLine() {
		return lowLine;
	}
	public void setLowLine(Float lowLine) {
		this.lowLine = lowLine;
	}
	public Float getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Float standardValue) {
		this.standardValue = standardValue;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@ManyToOne
	@JoinColumn(name="DEVICE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT,name="none"))
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
}
