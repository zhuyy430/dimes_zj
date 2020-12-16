package com.digitzones.procurement.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 入库申请单
 */
@Entity
public class WarehousingApplicationForm implements Serializable {
	private static final long serialVersionUID = 1L;
    /**申请单编码*/
	@Id
	private String formNo;
    /**部门编号*/
    private String cDepCode;
    /**业务员编号*/
    private String cPersonCode;
    /**供应商编码*/
	private String vendorCode;
	/**供应商名称*/
	private String vendorName;
    /**审核状态*/
	private String auditStatus="未审核";
	/**审核人编码*/
	private String auditorCode;
	/**审核人名称*/
	private String auditorName;
	/**审核时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
	private Date auditDate;
	/**入库仓库编码*/
	private String warehouseCode;
	/**入库仓库名称*/
	private String warehouseName;
    /**收料日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
	private Date receivingDate;
    /**入库申请单的生产日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date formDate;
    /**申请人员编码*/
    private String applierCode;
    /**申请人员名称*/
    private String applierName;
    /**入库状态：0：未入库 1：已入库
     * 是否生成ERP到货单*/
    private String warehousingStatus="0";

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

    public String getWarehousingStatus() {
        return warehousingStatus;
    }
    public void setWarehousingStatus(String warehousingStatus) {
        this.warehousingStatus = warehousingStatus;
    }
    public String getFormNo() {
        return formNo;
    }
    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
    public String getVendorCode() {
        return vendorCode;
    }
    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
    public Date getFormDate() {
        return formDate;
    }
    public void setFormDate(Date formDate) {
        this.formDate = formDate;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public String getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getAuditorCode() {
        return auditorCode;
    }
    public void setAuditorCode(String auditorCode) {
        this.auditorCode = auditorCode;
    }
    public String getAuditorName() {
        return auditorName;
    }
    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
    public Date getAuditDate() {
        return auditDate;
    }
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }
    public String getWarehouseCode() {
        return warehouseCode;
    }
    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
    public String getWarehouseName() {
        return warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public Date getReceivingDate() {
        return receivingDate;
    }
    public void setReceivingDate(Date receivingDate) {
        this.receivingDate = receivingDate;
    }
    public String getApplierCode() {
        return applierCode;
    }
    public void setApplierCode(String applierCode) {
        this.applierCode = applierCode;
    }
    public String getApplierName() {
        return applierName;
    }
    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }
}
