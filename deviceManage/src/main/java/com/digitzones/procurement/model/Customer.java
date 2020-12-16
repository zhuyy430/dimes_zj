package com.digitzones.procurement.model;

import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 客户
 */
@Entity
@Subselect("select * from View_Customer")
public class Customer implements Serializable {
	/*客户编码（key）*/
	@Id
	@GeneratedValue
	private String ccusCode;
	/*客户名称 */
	private String ccusName;
	/*客户简称*/
	private String ccusAbbName;
	/*客户分类编码 */
	private String cccCode;
	/*所属行业*/
	private String ctrade;
	/*地址*/
	private String ccusAddress;

	public String getCcusCode() {
		return ccusCode;
	}

	public void setCcusCode(String ccusCode) {
		this.ccusCode = ccusCode;
	}

	public String getCcusName() {
		return ccusName;
	}

	public void setCcusName(String ccusName) {
		this.ccusName = ccusName;
	}

	public String getCcusAbbName() {
		return ccusAbbName;
	}

	public void setCcusAbbName(String ccusAbbName) {
		this.ccusAbbName = ccusAbbName;
	}

	public String getCccCode() {
		return cccCode;
	}

	public void setCccCode(String cccCode) {
		this.cccCode = cccCode;
	}
	public String getCtrade() {
		return ctrade;
	}

	public void setCtrade(String ctrade) {
		this.ctrade = ctrade;
	}

	public String getCcusAddress() {
		return ccusAddress;
	}

	public void setCcusAddress(String ccusAddress) {
		this.ccusAddress = ccusAddress;
	}
}
