package com.digitzones.devmgr.model;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 设备等级实体
 * @author Administrator
 */
@Entity
public class DeviceLevel implements Serializable {
	private static final long serialVersionUID = 1055807629628886472L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**设备等级代码*/
	private String code;
	/**设备等级名称*/
	private String name;
	/**权重：等级的优先级*/
	private int weight;
	/**父等级*/
	@ManyToOne
	@JoinColumn(name="PARENT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	@JsonIgnore
	private DeviceLevel parent;
	/**备注*/
	private String note;
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	/**等级*/
	@OneToMany(fetch=FetchType.EAGER,mappedBy="parent")
	@OrderBy("code asc")
	private Set<DeviceLevel> children;
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
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public DeviceLevel getParent() {
		return parent;
	}
	public void setParent(DeviceLevel parent) {
		this.parent = parent;
	}
	public Set<DeviceLevel> getChildren() {
		return children;
	}
	public void setChildren(Set<DeviceLevel> children) {
		this.children = children;
	}
	
}
