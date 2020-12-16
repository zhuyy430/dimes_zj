package com.digitzones.mc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * MC端配置的登录用户信息
 * @author Administrator
 */
@Entity
@Table(name="MC_USER")
public class MCUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private String username;
	private String clientIp;
	private String employeeName;
	private String employeeCode;
	private String employeeICNo;
	//当前班次名称
	private String className;
	//当前班次代码
	private String classCode;
	/**是否登录状态*/
	private boolean sign_in = false;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public boolean isSign_in() {
		return sign_in;
	}
	public void setSign_in(boolean sign_in) {
		this.sign_in = sign_in;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeICNo() {
		return employeeICNo;
	}
	public void setEmployeeICNo(String employeeICNo) {
		this.employeeICNo = employeeICNo;
	}
}
