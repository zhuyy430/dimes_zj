package com.digitzones.devmgr.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备项目
 * @author zdq
 * 2018年6月3日
 */
public class DeviceProjectVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**项目编号*/
	private String code;
	/**项目名称*/
	private String name;
	/**标准*/
	private String standard;
	/**方法*/
	private String method;
	/**频次*/
	private String frequency;
	/**备注*/
	private String remark;
	/**说明*/
	private String note;
	/**设备类别*/
	private String  deviceTypeName;
	private Long  deviceTypeId;
	private String  deviceTypeCode;
	/**保养项目类型：
	 * SPOTINSPECTION:点检
	 * LUBRICATION：润滑
	 * MAINTAIN:保养
	 * BREAKDOWNREASON：故障原因
	 * MAINTENANCEITEM：维修项目
	 * */
	private String type;
	private Date createTime;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
