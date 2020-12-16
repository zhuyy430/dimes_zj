package com.digitzones.model;

import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * 部门实体
 * @author zdq
 * 2018年6月2日
 */
@Entity
@Subselect("select * from View_Department")
public class Department {
	/**部门编码*/
	@Id
	@Column(name="cDepCode")
	protected String code;
	/**名称*/
	@Column(name="cDepName")
	protected String name;
	/*编码级次*/
	private Integer iDepGrade;
	/*负责人编码*/
	private String cDepPerson;

	/*部门类型*/
	private String cDepType;
	/*部门序号*/
	private String iDepOrder;
	@Transient
	/*下属物料类别*/
	private List<Department> children;

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
	public Integer getiDepGrade() {
		return iDepGrade;
	}
	public void setiDepGrade(Integer iDepGrade) {
		this.iDepGrade = iDepGrade;
	}
	public String getcDepPerson() {
		return cDepPerson;
	}
	public void setcDepPerson(String cDepPerson) {
		this.cDepPerson = cDepPerson;
	}

	public String getcDepType() {
		return cDepType;
	}
	public void setcDepType(String cDepType) {
		this.cDepType = cDepType;
	}
	public String getiDepOrder() {
		return iDepOrder;
	}
	public void setiDepOrder(String iDepOrder) {
		this.iDepOrder = iDepOrder;
	}

	public List<Department> getChildren() {
		return children;
	}

	public void setChildren(List<Department> children) {
		this.children = children;
	}
}
