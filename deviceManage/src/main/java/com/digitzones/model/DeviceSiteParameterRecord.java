package com.digitzones.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 站点和 参数的记录实体
 * @author zdq
 * 2018年6月26日
 */
@Entity
@Table(name="DEVICESITEPARAMETERRECORD")
public class DeviceSiteParameterRecord {
	private Long  id;
	private Long deviceSiteId;
	private String deviceSiteName;
	private String deviceSiteCode;
	private Long parameterId;
	private String parameterName;
	private String parameterCode;
	/**数据录入日期*/
	private Date updateDate;
	/**参数值*/
	private Float value;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDeviceSiteId() {
		return deviceSiteId;
	}
	public void setDeviceSiteId(Long deviceSiteId) {
		this.deviceSiteId = deviceSiteId;
	}
	public String getDeviceSiteName() {
		return deviceSiteName;
	}
	public void setDeviceSiteName(String deviceSiteName) {
		this.deviceSiteName = deviceSiteName;
	}
	public String getDeviceSiteCode() {
		return deviceSiteCode;
	}
	public void setDeviceSiteCode(String deviceSiteCode) {
		this.deviceSiteCode = deviceSiteCode;
	}
	public Long getParameterId() {
		return parameterId;
	}
	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterCode() {
		return parameterCode;
	}
	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updateDate")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	
}
