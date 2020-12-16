package com.digitzones.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
/**
 * 发货计划实体
 */
@Entity
public class DeliveryPlan implements Serializable {
    /**发货单号*/
    @Id
    private String formNo;
    /**发货日期*/
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverDate;
    /**审核人编码*/
    private String auditorCode;
    /**审核人名称*/
    private String auditorName;
    /**审核日期*/
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditDate;
    /**审核状态*/
    private String auditStatus="未审核";
    /***状态：计划、完成、终止*/
    private String status="计划";
    /**制单人编码*/
    private String makerCode;
    /**制单人名称*/
    private String makerName;
    public String getMakerCode() {
        return makerCode;
    }
    public void setMakerCode(String makerCode) {
        this.makerCode = makerCode;
    }
    public String getMakerName() {
        return makerName;
    }
    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getFormNo() {
        return formNo;
    }
    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
    public Date getDeliverDate() {
        return deliverDate;
    }
    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
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
}
