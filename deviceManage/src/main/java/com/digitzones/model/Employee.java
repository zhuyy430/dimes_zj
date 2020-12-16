package com.digitzones.model;

import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
/**
 * 员工实体
 * @author zdq
 * 2018年6月2日
 */
@Entity
@Subselect("select * from View_Person")
public class Employee implements Serializable{
	private static final long serialVersionUID = 1L;
	/**部门编号*/
	private String cDept_Num;
	/**部门名称*/
	private String cDepName;
	@Column(name="cPsnEmail")
	private String email;
	/**编号*/
	@Id
	@Column(name = "cPsn_Num")
	private String code;
	/**名称*/
	@Column(name = "cPsn_Name")
	private  String name;
	/**联系方式*/
	@Column(name = "cPsnMobilePhone")
	private String tel;
	public String getcDept_Num() {
		return cDept_Num;
	}
	public void setcDept_Num(String cDept_Num) {
		this.cDept_Num = cDept_Num;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getcDepName() {
		return cDepName;
	}
	public void setcDepName(String cDepName) {
		this.cDepName = cDepName;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
