package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
/**
 * 供应商
 */
@Entity
@Subselect("select * from View_Vendor")
public class Vendor {
	/*供应商编码 （key）*/
	@Id
	@GeneratedValue
	private String cVenCode ;
	/*供应商名称 */
	private String cVenName 	;
	/*供应商简称 */
	private String cVenAbbName ;
	/*供应商分类编码 */
	private String cVCCode ;
	/*联系人*/
	private String cVenPerson;
	/*电话*/
	private String cVenPhone;
	public String getcVenCode() {
		return cVenCode;
	}
	public void setcVenCode(String cVenCode) {
		this.cVenCode = cVenCode;
	}
	public String getcVenName() {
		return cVenName;
	}
	public void setcVenName(String cVenName) {
		this.cVenName = cVenName;
	}
	public String getcVenAbbName() {
		return cVenAbbName;
	}
	public void setcVenAbbName(String cVenAbbName) {
		this.cVenAbbName = cVenAbbName;
	}
	public String getcVCCode() {
		return cVCCode;
	}
	public void setcVCCode(String cVCCode) {
		this.cVCCode = cVCCode;
	}
	public String getcVenPerson() {
		return cVenPerson;
	}
	public void setcVenPerson(String cVenPerson) {
		this.cVenPerson = cVenPerson;
	}
	public String getcVenPhone() {
		return cVenPhone;
	}
	public void setcVenPhone(String cVenPhone) {
		this.cVenPhone = cVenPhone;
	}
}
