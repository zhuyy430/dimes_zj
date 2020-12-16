package com.digitzones.devmgr.vo;

import java.io.Serializable;

/**
 * 维修项目
 * @author zhuyy430
 *
 */
public class MaintainProjectVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**类型Id*/
	private Long typeId;
	/**类型代码*/
	private String typeCode;
	/**类型名称*/
	private String typeName;
	/**项目代码*/
	private String code;
	/**说明*/
	private String name;
	/**说明*/
	private String note;
	/**处理方法*/
	private String processingMethod;
	/**备注*/
	private String remark;
	/**维修单*/
	private Long deviceRepairId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getProcessingMethod() {
		return processingMethod;
	}
	public void setProcessingMethod(String processingMethod) {
		this.processingMethod = processingMethod;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getDeviceRepairId() {
		return deviceRepairId;
	}
	public void setDeviceRepairId(Long deviceRepairId) {
		this.deviceRepairId = deviceRepairId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
}
