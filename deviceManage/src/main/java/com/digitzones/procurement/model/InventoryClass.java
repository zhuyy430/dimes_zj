package com.digitzones.procurement.model;

import org.hibernate.annotations.Subselect;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 物料类别
 */
@Entity
@Subselect("select * from View_InventoryClass")
public class InventoryClass {
	/*物料类别代码（key）*/
	private String cInvCCode;
	/*下属物料类别*/
	private List<InventoryClass> children;
	/*物料类别名称*/
	private String cInvCName	;
	/*编码级次 */
	private String iInvCGrade;
	/*是否末级*/
	private Boolean bInvCEnd;
	@Id
	@GeneratedValue
	public String getcInvCCode() {
		return cInvCCode;
	}
	public void setcInvCCode(String cInvCCode) {
		this.cInvCCode = cInvCCode;
	}
	public String getcInvCName() {
		return cInvCName;
	}
	public void setcInvCName(String cInvCName) {
		this.cInvCName = cInvCName;
	}
	public String getiInvCGrade() {
		return iInvCGrade;
	}
	public void setiInvCGrade(String iInvCGrade) {
		this.iInvCGrade = iInvCGrade;
	}
	public Boolean getbInvCEnd() {
		return bInvCEnd;
	}
	public void setbInvCEnd(Boolean bInvCEnd) {
		this.bInvCEnd = bInvCEnd;
	}
	@Transient
	public List<InventoryClass> getChildren() {
		return children;
	}
	public void setChildren(List<InventoryClass> children) {
		this.children = children;
	}
}
