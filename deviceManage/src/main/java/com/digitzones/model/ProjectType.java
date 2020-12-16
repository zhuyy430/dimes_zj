package com.digitzones.model;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
/**
 * 设备类别,备件类别,故障原因类别，维修项目类别
 * 编码(code),参照Constant.ProjectType类中的定义
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="PROJECTTYPE")
public class ProjectType {
	private List<ProjectType> children;
	@OneToMany(fetch=FetchType.EAGER,mappedBy="parent")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(" code asc")
	public List<ProjectType> getChildren() {
		return children;
	}
	public void setChildren(List<ProjectType> children) {
		this.children = children;
	}
	private ProjectType parent;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	@JsonBackReference
	public ProjectType getParent() {
		return parent;
	}
	public void setParent(ProjectType parent) {
		this.parent = parent;
	}
	/**对象唯一标识*/
	protected Long id;
	/**编号*/
	protected String code;
	/**名称*/
	protected String name;
	/**备注*/
	protected String note;
	/**是否停用*/
	protected Boolean disabled = false;
	/**类别：
	 * DEVICE_TYPE：设备类型
	 * SPAREPART_TYPE:备件类型
	 * */
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
