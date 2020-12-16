package com.digitzones.dto;
/**
 * 发货单查询条件类
 */
public class DeliveryPlanDetailRetrievalDto {
    /**发货日期开始日期*/
    private String from;
    /**发货日期结束日期*/
    private String to;
    /**销售订单单号*/
    private String formNo;
    /**物料编码*/
    private String inventoryCode;
    /**批号*/
    private String batchNumber;
    /**客户编码*/
    private String customerCode;
    /**状态*/
    private String status;
    public String getCustomerCode() {
        return customerCode;
    }
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getFormNo() {
        return formNo;
    }
    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
    public String getInventoryCode() {
        return inventoryCode;
    }
    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }
    public String getBatchNumber() {
        return batchNumber;
    }
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
