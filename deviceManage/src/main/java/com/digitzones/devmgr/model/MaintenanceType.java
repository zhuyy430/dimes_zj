package com.digitzones.devmgr.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 设备保养类别
 * @author zdq
 * 2019年1月2日
 */
@Entity
public class MaintenanceType implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	/**
	 * 类别编码
	 */
	private String code;
	/**类别名称*/
	private String name;
	/**备注*/
	private String note;
	/**是否允许删除，一级保养和二级保养是不允许删除的*/
	private Boolean allowedDelete = true;
	public Boolean getAllowedDelete() {
		return allowedDelete;
	}
	public void setAllowedDelete(Boolean allowedDelete) {
		this.allowedDelete = allowedDelete;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
}
