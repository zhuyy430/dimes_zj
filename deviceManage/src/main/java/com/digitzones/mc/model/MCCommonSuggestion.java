package com.digitzones.mc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * MC端常用意见处理
 * @author Administrator
 */
@Entity
@Table(name="MC_COMMONSUGGESTION")
public class MCCommonSuggestion {
	private long id;
	//意见代码
	private String text;
	//意见名称
	private MCCommonType CommonType;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@ManyToOne
	@JoinColumn(name="parentsId")
	public MCCommonType getCommonType() {
		return CommonType;
	}
	public void setCommonType(MCCommonType commonType) {
		CommonType = commonType;
	}
}
