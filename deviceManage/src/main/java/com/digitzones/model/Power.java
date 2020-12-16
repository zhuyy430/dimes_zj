package com.digitzones.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 权限
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="POWER")
public class Power {
	/**对象标识*/
	private Long id;
	/**权限码*/
	private String powerCode;
	/**权限名称*/
	private String powerName;
	/**创建日期*/
	private Date createDate;
	/**备注*/
	private String note;
	/**是否停用*/
	private Boolean disable=false;
	/**创建用户id*/
	private Long createUserId;
	/**创建用户名称*/
	private String createUsername;
	/**修改用户ID*/
	private Long modifyUserId;
	/**修改用户名称*/
	private String modifyUsername;
	/**修改日期*/
	private Date modifyDate;
	/**所属模块*/
	private String group;
	@Column(name="_group")
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPowerCode() {
		return powerCode;
	}
	public void setPowerCode(String powerCode) {
		this.powerCode = powerCode;
	}
	public String getPowerName() {
		return powerName;
	}
	public void setPowerName(String powerName) {
		this.powerName = powerName;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUsername() {
		return createUsername;
	}
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}
	public Long getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyUsername() {
		return modifyUsername;
	}
	public void setModifyUsername(String modifyUsername) {
		this.modifyUsername = modifyUsername;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
