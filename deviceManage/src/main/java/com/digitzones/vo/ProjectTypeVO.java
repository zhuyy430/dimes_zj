package com.digitzones.vo;

import java.util.List;

import com.digitzones.model.ProjectType;

public class ProjectTypeVO {

	/**对象唯一标识*/
	protected Long id;
	/**编号*/
	protected String code;
	/**名称*/
	protected String name;
	/**方法*/
	private String method;
	/**备注*/
	private String remark;
	/**说明*/
	protected String note;
	/**是否停用*/
	protected Boolean disabled = false;
	private List<ProjectType> children;
	/**类别：
	 * DEVICE_TYPE：设备类型
	 * SPAREPART_TYPE:备件类型
	 * */
	private String type;
	private ProjectType parent;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<ProjectType> getChildren() {
		return children;
	}
	public void setChildren(List<ProjectType> children) {
		this.children = children;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ProjectType getParent() {
		return parent;
	}
	public void setParent(ProjectType parent) {
		this.parent = parent;
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
}
