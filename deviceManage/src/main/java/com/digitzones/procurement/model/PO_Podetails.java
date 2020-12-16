package com.digitzones.procurement.model;

import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 采购订单子表
 */
@Entity
@Subselect("select * from View_PO_Podetails")
public class PO_Podetails {
	/*采购订单子表标识（key）*/
	@Id
	@GeneratedValue
	private Integer id;
	/*采购订单id*/
	private String poId;
	/**采购订单号*/
	private String cpoId;
	/*物料类别*/
	private String cInvCName;
	/*存货编码 */
	private String cInvCode;
	/*存货名称*/
	private String cInvName;
	/*规格型号*/
	private String cInvStd;
	/**已申请数量*/
	private Double cDefine7;
	/*数量*/
	private BigDecimal iQuantity;
	/*计量单位名称*/
	private String cComUnitName;
	/*计划到货日期*/
	private String dArriveDate;
	/*标志 */
	private String iflag;
	/*到货数量 */
	private BigDecimal iarrqty;
	/**税率*/
	private Double iPerTaxRate;
	/*货位编码*/
	private String cPosition;
	/**含税单价*/
	private Double iTaxPrice;
	/**未税单价*/
	private Double iUnitPrice;
	/**未税总额*/
	private Double iMoney;
	/**税额*/
	private Double iTax;
	/**含税总金额*/
	private Double iSum;
	/**未税单价*/
	private Double iNatUnitPrice;
	/**未税总额*/
	private Double iNatMoney;
	/**税额*/
	private Double iNatTax;
	/**含税总金额*/
	private Double iNatSum;
	public Double getiTaxPrice() {
		return iTaxPrice;
	}
	public void setiTaxPrice(Double iTaxPrice) {
		this.iTaxPrice = iTaxPrice;
	}
	public Double getiUnitPrice() {
		return iUnitPrice;
	}
	public void setiUnitPrice(Double iUnitPrice) {
		this.iUnitPrice = iUnitPrice;
	}
	public Double getiMoney() {
		return iMoney;
	}
	public void setiMoney(Double iMoney) {
		this.iMoney = iMoney;
	}
	public Double getiTax() {
		return iTax;
	}
	public void setiTax(Double iTax) {
		this.iTax = iTax;
	}
	public Double getiSum() {
		return iSum;
	}

	public void setiSum(Double iSum) {
		this.iSum = iSum;
	}

	public Double getiNatUnitPrice() {
		return iNatUnitPrice;
	}

	public void setiNatUnitPrice(Double iNatUnitPrice) {
		this.iNatUnitPrice = iNatUnitPrice;
	}

	public Double getiNatMoney() {
		return iNatMoney;
	}

	public void setiNatMoney(Double iNatMoney) {
		this.iNatMoney = iNatMoney;
	}

	public Double getiNatTax() {
		return iNatTax;
	}

	public void setiNatTax(Double iNatTax) {
		this.iNatTax = iNatTax;
	}

	public Double getiNatSum() {
		return iNatSum;
	}

	public void setiNatSum(Double iNatSum) {
		this.iNatSum = iNatSum;
	}

	public Double getiPerTaxRate() {
		return iPerTaxRate;
	}

	public void setiPerTaxRate(Double iPerTaxRate) {
		this.iPerTaxRate = iPerTaxRate;
	}

	public String getCpoId() {
		return cpoId;
	}
	public void setCpoId(String cpoId) {
		this.cpoId = cpoId;
	}
	public Double getcDefine7() {
		return cDefine7;
	}
	public void setcDefine7(Double cDefine7) {
		this.cDefine7 = cDefine7;
	}
	public String getPoId() {
		return poId;
	}
	public void setPoId(String poId) {
		this.poId = poId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getcInvCName() {
		return cInvCName;
	}
	public void setcInvCName(String cInvCName) {
		this.cInvCName = cInvCName;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getcInvName() {
		return cInvName;
	}
	public void setcInvName(String cInvName) {
		this.cInvName = cInvName;
	}
	public String getcInvStd() {
		return cInvStd;
	}
	public void setcInvStd(String cInvStd) {
		this.cInvStd = cInvStd;
	}
	public BigDecimal getiQuantity() {
		return iQuantity;
	}
	public void setiQuantity(BigDecimal iQuantity) {
		this.iQuantity = iQuantity;
	}
	public String getcComUnitName() {
		return cComUnitName;
	}
	public void setcComUnitName(String cComUnitName) {
		this.cComUnitName = cComUnitName;
	}
	public String getdArriveDate() {
		return dArriveDate;
	}
	public void setdArriveDate(String dArriveDate) {
		this.dArriveDate = dArriveDate;
	}
	public String getIflag() {
		return iflag;
	}
	public void setIflag(String iflag) {
		this.iflag = iflag;
	}
	public BigDecimal getIarrqty() {
		return iarrqty;
	}
	public void setIarrqty(BigDecimal iarrqty) {
		this.iarrqty = iarrqty;
	}
	public String getcPosition() {
		return cPosition;
	}
	public void setcPosition(String cPosition) {
		this.cPosition = cPosition;
	}
}
