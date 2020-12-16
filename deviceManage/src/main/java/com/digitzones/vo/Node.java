package com.digitzones.vo;

import java.util.List;

/**
 * 树形结构的节点
 * @author zdq
 * 2018年6月19日
 */
public class Node {
	private Long id;
	private String name;
	private String code;
	private String unitType;
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	private List<Node> children;
	public List<Node> getChildren() {
		return children;
	}
	public String getCode() {
		return code;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
}
