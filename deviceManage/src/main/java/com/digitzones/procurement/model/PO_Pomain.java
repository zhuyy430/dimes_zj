package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 采购订单
 */
@Entity
@Subselect("select * from View_PO_Pomain")
public class PO_Pomain {
	/*采购订单主表标识 （key）*/
	@Id
	@GeneratedValue
	private String POID;
	/**部门编号*/
	private String cDepCode;
	/**业务员编号*/
	private String cPersonCode;
	/*采购订单号 */
	private String cPOID;

	public String getcDepCode() {
		return cDepCode;
	}

	public void setcDepCode(String cDepCode) {
		this.cDepCode = cDepCode;
	}

	public String getcPersonCode() {
		return cPersonCode;
	}

	public void setcPersonCode(String cPersonCode) {
		this.cPersonCode = cPersonCode;
	}

	/*供应商编码*/
	private String  cVenCode;
	/*制单人 */
	private String  cMaker;
	/*审核人 */
	private String  cVerifier;
	/*业务类型 */
	private String  cBusType;
	/*单据日期 */
	private String  dPODate;
	/*状态 */
	private String  cState;
	/*备注 */
	private String  cMemo;
	/*联系人 */
	private String  cVenPerson;
	/*电话 */
	private String  cVenPhone;
	/*供应商名称 */
	private String  cVenName;
	public String getcPOID() {
		return cPOID;
	}
	public void setcPOID(String cPOID) {
		this.cPOID = cPOID;
	}
	public String getcVenCode() {
		return cVenCode;
	}
	public void setcVenCode(String cVenCode) {
		this.cVenCode = cVenCode;
	}
	public String getcMaker() {
		return cMaker;
	}
	public void setcMaker(String cMaker) {
		this.cMaker = cMaker;
	}
	public String getcVerifier() {
		return cVerifier;
	}
	public void setcVerifier(String cVerifier) {
		this.cVerifier = cVerifier;
	}

	public String getcBusType() {
		return cBusType;
	}

	public void setcBusType(String cBusType) {
		this.cBusType = cBusType;
	}

	public String getdPODate() {
		return dPODate;
	}

	public void setdPODate(String dPODate) {
		this.dPODate = dPODate;
	}

	public String getcState() {
		return cState;
	}

	public void setcState(String cState) {
		this.cState = cState;
	}

	public String getcMemo() {
		return cMemo;
	}

	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
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

	public String getcVenName() {
		return cVenName;
	}

	public void setcVenName(String cVenName) {
		this.cVenName = cVenName;
	}
	public String getPOID() {
		return POID;
	}
	public void setPOID(String pOID) {
		POID = pOID;
	}
	
}
