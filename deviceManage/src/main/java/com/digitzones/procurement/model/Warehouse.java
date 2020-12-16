package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 仓库
 */
@Entity
@Subselect("select * from View_Warehouse")
public class Warehouse {
	/*仓库编码 （key）*/
	@Id
	@GeneratedValue
	private String cWhCode ;
	/*仓库名称*/
	private String cWhName 	;
	/*所属部门*/
	private String cDepCode ;
	/*仓库地址 */
	private String cWhAddress ;
	public String getcWhCode() {
		return cWhCode;
	}
	public void setcWhCode(String cWhCode) {
		this.cWhCode = cWhCode;
	}
	public String getcWhName() {
		return cWhName;
	}
	public void setcWhName(String cWhName) {
		this.cWhName = cWhName;
	}
	public String getcDepCode() {
		return cDepCode;
	}
	public void setcDepCode(String cDepCode) {
		this.cDepCode = cDepCode;
	}
	public String getcWhAddress() {
		return cWhAddress;
	}
	public void setcWhAddress(String cWhAddress) {
		this.cWhAddress = cWhAddress;
	}
}
