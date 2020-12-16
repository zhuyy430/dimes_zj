package com.digitzones.dto;

/**
 * 报工单查询条件类
 */
public class JobBookingFormDetailRetrievalDto {
    /**报工日期开始日期*/
    private String from;
    /**报工日期结束日期*/
    private String to;
    /**工单单号*/
    private String no;
    /**生产单元编码**/
    private String productionUnitCode;
    /**物料编码*/
    private String inventoryCode;
    /**箱条码*/
    private String barCode;
    /**批号*/
    private String batchNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProductionUnitCode() {
        return productionUnitCode;
    }

    public void setProductionUnitCode(String productionUnitCode) {
        this.productionUnitCode = productionUnitCode;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
