package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 计量单位
 */
@Entity
@Subselect("select * from View_ComputationUnit")
public class ComputationUnit {
	/*对应存货编码*/
	private String cUnitRefInvCode;
	/*计量单位编码*/
	@Id
	@GeneratedValue
	private String cComunitCode;
	/*计量单位名称*/
	private String cComUnitName;
	/*计量单位组编码*/
	private String cGroupCode;
	/*对应条形码编码*/
	private String cBarCode;
	/*是否主计量单位 */
	private Boolean bMainUnit;
	public String getcUnitRefInvCode() {
		return cUnitRefInvCode;
	}
	public void setcUnitRefInvCode(String cUnitRefInvCode) {
		this.cUnitRefInvCode = cUnitRefInvCode;
	}
	public String getcComunitCode() {
		return cComunitCode;
	}
	public void setcComunitCode(String cComunitCode) {
		this.cComunitCode = cComunitCode;
	}
	public String getcComUnitName() {
		return cComUnitName;
	}
	public void setcComUnitName(String cComUnitName) {
		this.cComUnitName = cComUnitName;
	}
	public String getcGroupCode() {
		return cGroupCode;
	}
	public void setcGroupCode(String cGroupCode) {
		this.cGroupCode = cGroupCode;
	}
	public String getcBarCode() {
		return cBarCode;
	}
	public void setcBarCode(String cBarCode) {
		this.cBarCode = cBarCode;
	}
	public Boolean getbMainUnit() {
		return bMainUnit;
	}
	public void setbMainUnit(Boolean bMainUnit) {
		this.bMainUnit = bMainUnit;
	}
	
	
	
}
