package com.digitzones.vo;

import java.util.List;

/**
 * 功能模块的值对象：仅用于为角色分配菜单
 * @author zdq
 * 2018年6月6日
 */
public class ModuleVO2 {
	private Long id;
	private String text;
	private String url;
	private Long pid;
	private String iconCls;
	private String note;
	private Boolean leaf;
	/**是否被选中*/
	private boolean isChecked;
	/**是否被选中*/
	private boolean isSelected;
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	private List<ModuleVO2> children;
	public List<ModuleVO2> getChildren() {
		return children;
	}
	public void setChildren(List<ModuleVO2> children) {
		this.children = children;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public Long getId() {
		return id;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
