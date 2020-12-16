package com.digitzones.model;
import java.util.Set;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 按灯类别:损失类别
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="PRESSLIGHTTYPE")
public class PressLightType{
	private PressLightType parent;
	/**类别等级*/
	private Integer level = 0;
	/**
	 * 子类别
	 */
	private Set<PressLightType> children;
	/**基本类型代码:level为1的code值*/
	private String basicCode;
	/**树形结构 需要:和name值相同*/
	private String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@OneToMany(fetch=FetchType.EAGER,mappedBy="parent")
	@OrderBy(" ID DESC")
	public Set<PressLightType> getChildren() {
		return children;
	}
	public Integer getLevel() {
		return level;
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
	/**故障类别:故障原因，装备故障原因*/
	protected String typeName;
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
	@ManyToOne
	@JoinColumn(name="PARENT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	@JsonIgnore
	public PressLightType getParent() {
		return parent;
	}
	public void setChildren(Set<PressLightType> children) {
		this.children = children;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public void setParent(PressLightType parent) {
		this.parent = parent;
	}
	public String getBasicCode() {
		return basicCode;
	}
	public void setBasicCode(String basicCode) {
		this.basicCode = basicCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
