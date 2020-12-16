package com.digitzones.procurement.dto;

/**
 * 临时条码查询条件类
 */
public class TemporaryBarcodeDetailRetrievalDto {
    /**录入日期开始日期*/
    private String from;
    /**录入日期结束日期*/
    private String to;
    /**单据类型*/
    private String billType;
    /**物料编码*/
    private String inventoryCode;
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
    public String getBillType() {
        return billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    public String getInventoryCode() {
        return inventoryCode;
    }
    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }
}
