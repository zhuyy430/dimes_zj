package com.digitzones.vo;

import com.digitzones.model.ProcessType;

/**
 * 工序值对象实体
 * @author zdq
 * 2018年6月7日
 */
public class ProcessesVO {
	private Long id;
	private String name;
	private Long pid;
	private Integer status;
	private ProcessType processType;
	private String code;
	private Boolean disabled;
	private String note;
	private String processTypeName;
	private Long processTypeId;
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProcessType getProcessType() {
		return processType;
	}
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public String getProcessTypeName() {
		return processTypeName;
	}
	public void setProcessTypeName(String processTypeName) {
		this.processTypeName = processTypeName;
	}
	public Long getProcessTypeId() {
		return processTypeId;
	}
	public void setProcessTypeId(Long processTypeId) {
		this.processTypeId = processTypeId;
	}
	
}
