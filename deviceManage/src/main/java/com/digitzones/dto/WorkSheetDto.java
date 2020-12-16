package com.digitzones.dto;

/**
 * @author xuxf
 * @date 2020-7-18
 * @time 17:23
 */
public class WorkSheetDto {
    /**开始日期*/
    private String from;
    /**结束日期*/
    private String to;
    /**单号*/
    private String no;
    /**生产批号*/
    private String batchNo;
    /**工件编码*/
    private String inventoryCode;
    /**状态*/
    private String status;


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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
