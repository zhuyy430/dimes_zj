package com.digitzones.procurement.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
/**
 * 临时条码实体
 */
@Entity
public class TemporaryBarcode implements Serializable {
	private static final long serialVersionUID = 1L;
    /**临时条码编码*/
	@Id
	private String formNo;
    /**制单人编码*/
	private String inputPersonCode;
	/**制单人名称*/
	private String inputPersonName;
	/**制单时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inputDate;
	/**单据日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date billDate;
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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**单据类型：同TemporaryBarcodeDetail*/
    private String billType;
    public Date getBillDate() {
        return billDate;
    }
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
    public String getFormNo() {
        return formNo;
    }
    public void setFormNo(String formNo) {
        this.formNo = formNo;
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

    public String getInputPersonCode() {
        return inputPersonCode;
    }

    public void setInputPersonCode(String inputPersonCode) {
        this.inputPersonCode = inputPersonCode;
    }

    public String getInputPersonName() {
        return inputPersonName;
    }

    public void setInputPersonName(String inputPersonName) {
        this.inputPersonName = inputPersonName;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }
}
