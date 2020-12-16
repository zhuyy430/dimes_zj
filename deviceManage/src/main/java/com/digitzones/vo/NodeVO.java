package com.digitzones.vo;

import java.util.List;

/**
 * 树形结构的节点
 * @author zdq
 * 2018年6月19日
 */
public class NodeVO {
	private Long id;
	private String text;
	private String code;
	private List<NodeVO> children;
	public List<NodeVO> getChildren() {
		return children;
	}
	public String getCode() {
		return code;
	}
	public Long getId() {
		return id;
	}
	public void setChildren(List<NodeVO> children) {
		this.children = children;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
