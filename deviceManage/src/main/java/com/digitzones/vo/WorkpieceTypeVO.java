package com.digitzones.vo;
import java.util.Set;
import com.digitzones.procurement.model.InventoryClass;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 工序类别值对象实体
 * @author zdq
 * 2018年6月7日
 *
 * InventoryClass的值对象
 */
@SuppressWarnings("rawtypes")
public class WorkpieceTypeVO {
	private Long id;
	private String name;
	private Integer status;
	private Set children;
	private InventoryClass parent;
	private String code;
	private Boolean disabled;
	private String note;
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
	
	public InventoryClass getParent() {
		return parent;
	}
	public void setParent(InventoryClass parent) {
		this.parent = parent;
	}
	@JsonIgnore
	public Set getChildren() {
		return children;
	}
	public void setChildren(Set children) {
		this.children = children;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
