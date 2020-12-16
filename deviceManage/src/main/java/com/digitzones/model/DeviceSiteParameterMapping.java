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

/**
 * 参数和设备站点关联
 * @author zdq
 * 2018年6月26日
 */
@Entity
@Table(name="DEVICESITE_PARAMETER")
public class DeviceSiteParameterMapping {
	private Long id;
	private DeviceSite deviceSite;
	private Parameters parameter;
	/**单位*/
	private String unit;
	/**备注*/
	private String note;
	/**上线*/
	private Float upLine;
	/**下线*/
	private Float lowLine;
	/**标准值*/
	private Float standardValue;
	/**实际值*/
	private Float trueValue;
	/**更新日期*/
	private Date updateDate;
	public Float getTrueValue() {
		return trueValue;
	}
	public void setTrueValue(Float trueValue) {
		this.trueValue = trueValue;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
	@ManyToOne
	@JoinColumn(name="DEVICESITE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PARAMETER_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Parameters getParameter() {
		return parameter;
	}
	public void setParameter(Parameters parameter) {
		this.parameter = parameter;
	}
}
