package com.digitzones.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 关联文档类别实体
 * @author zdq
 * 2019年1月2日
 */
@Entity
public class RelatedDocumentType implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**模块编码：Constant.RelatedDoc已定义，用于区分该文档所属模块*/
	private String moduleCode;
	/**模块名称*/
	private String moduleName;
	/**关联文档类型编码*/
	private String code;
	/**文档类别名称*/
	private String name;
	/**是否停用*/
	private boolean disabled = false;
	/**创建人id*/
	private Long createUserId;
	/**创建用户名称*/
	private String createUsername;
	/**创建时间*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	/**最后 修改人id*/
	private Long modifyUserId;
	/**最后修改人名称*/
	private String modifyUsername;
	/**修改时间*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;
	/**说明*/
	private String note;
	/**是否允许删除*/
	private boolean allowedDelete = true;
	
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAllowedDelete() {
		return allowedDelete;
	}
	public void setAllowedDelete(boolean allowedDelete) {
		this.allowedDelete = allowedDelete;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
