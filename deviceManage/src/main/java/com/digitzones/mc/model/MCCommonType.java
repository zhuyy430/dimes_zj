package com.digitzones.mc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * MC端常用意见处理
 * @author Administrator
 */
@Entity
@Table(name="MC_COMMONTYPE")
public class MCCommonType {
	private long id;
	//意见代码
	private String code;
	//意见名称
	private String name;
	//意见父类
	private long parentsId;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public long getParentsId() {
		return parentsId;
	}
	public void setParentsId(long parentsId) {
		this.parentsId = parentsId;
	}
	
}
