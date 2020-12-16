package com.digitzones.model;
import org.hibernate.annotations.Subselect;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 销售订单
 */
@Entity
@Subselect("select * from view_SalesSlip")
public class SalesSlip implements Serializable {
	private static final long serialVersionUID = 1L;
	/**销售订单号*/
    @Column(name="cSOCode")
	private String formNo;
    /**币种*/
    @Column(name="cexch_name")
    private String currency;
    @Column(name="iSOsID")
    private String sosId;
	/**客户编码*/
    @Column(name="cCusCode")
	private String customerCode;
	/**客户名称*/
    @Column(name="cCusName")
	private String customerName;
	/**规格型号*/
    @Column(name="cInvStd")
	private String specificationType;
	/**存货编码*/
    @Column(name="cInvCode")
	private String inventoryCode;
	/**存货名称*/
    @Column(name="cInvName")
	private String inventoryName;
    /**唯一标识*/
    @Id
    @Column(name="AutoID")
    private Integer autoId;
    /**行号*/
    @Column(name="irowno")
    private Integer rowNo;
    /**数量*/
    @Column(name="iQuantity")
    private Double quantity;
    /**累计数量*/
    @Column(name="iFHQuantity")
    private Double fhQuantity;
    /**生产批号*/
    @Column(name="cDefine22")
    private String batchNumber;
    /**预发货日期*/
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="dPreDate")
    private Date preDate;
    /**备注*/
    @Column(name="cMemo")
    private String note;
    /**汇率*/
    @Column(name="iexchRate")
    private Double exchangeRate=0d;
    /**税率*/
    @Column(name="iTaxRate")
    private Double taxRate;
    /**单价*/
    @Column(name="iUnitPrice")
    private Double unitPrice;
    /**业务员*/
    @Column(name="cPersonCode")
    private String personCode;
    /**税后单价*/
    @Column(name="iTaxUnitPrice")
    private Double taxUnitPrice;
    @Column(name="iDiscount")
    private Double discount;
    @Column(name="iNatUnitPrice")
    private Double natUnitPrice;
    @Column(name="iNatDiscount")
    private Double natDiscount;
    public String getSosId() {
        return sosId;
    }
    public void setSosId(String sosId) {
        this.sosId = sosId;
    }
    public Double getNatDiscount() {
        return natDiscount;
    }
    public void setNatDiscount(Double natDiscount) {
        this.natDiscount = natDiscount;
    }
    public Double getNatUnitPrice() {
        return natUnitPrice;
    }
    public void setNatUnitPrice(Double natUnitPrice) {
        this.natUnitPrice = natUnitPrice;
    }
    public Double getDiscount() {
        return discount;
    }
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    public String getBatchNumber() {
        return batchNumber;
    }
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    public Double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public Double getTaxUnitPrice() {
        return taxUnitPrice;
    }
    public void setTaxUnitPrice(Double taxUnitPrice) {
        this.taxUnitPrice = taxUnitPrice;
    }

    public Double getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
    public Double getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public void setSpecificationType(String specificationType) {
        this.specificationType = specificationType;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getFhQuantity() {
        return fhQuantity;
    }

    public void setFhQuantity(Double fhQuantity) {
        this.fhQuantity = fhQuantity;
    }

    public Date getPreDate() {
        return preDate;
    }

    public void setPreDate(Date preDate) {
        this.preDate = preDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
