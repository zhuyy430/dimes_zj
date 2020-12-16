package com.digitzones.model;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 用户
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="T_USER")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	/**对象标识*/
	private Long id;
	/**用户名称*/
	private String username;
	/**密码*/
	private String password;
	/**真实姓名*/
	private String realName;
	/**邮箱*/
	private String email;
	/**联系方式*/
	private String tel;
	/**创建日期*/
	private Date createDate;
	/**备注*/
	private String note;
	/**是否停用*/
	private Boolean disable = false;
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
	/**员工*/
	private Employee employee;
	/**用户头像名称*/
	private String picName;
	/**用户头像*/
	private Blob pic;
	/**是否允许删除*/
	private Boolean allowDelete=true;
	public Boolean getAllowDelete() {
		return allowDelete;
	}
	public void setAllowDelete(Boolean allowDelete) {
		this.allowDelete = allowDelete;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="EMPLOYEE_CODE",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	@JsonIgnore
	public Blob getPic() {
		return pic;
	}
	public void setPic(Blob pic) {
		this.pic = pic;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
