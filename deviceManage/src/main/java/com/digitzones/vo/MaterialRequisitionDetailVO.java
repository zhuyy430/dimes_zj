package com.digitzones.vo;

/**
 * 领料详情
 */
public class MaterialRequisitionDetailVO {
    private String id;
    /**箱条码*/
    private String barCode;
    /**领料单号*/
    private String formNo;
    /**物料编码*/
    private String inventoryCode;
    /**物料名称*/
    private String inventoryName;
    /**规格型号*/
    private String specificationType;
    /**箱数*/
    private Double amountOfBoxes;
    /**领用数量*/
    private Double amount;
    /**领料日期*/
    private String pickingDate;
    /**箱号*/
    private int boxNum;
    /**批号*/
    private String batchNumber;
    /**材料编号*/
    private String furnaceNumber;
    /**货位编码*/
    private String positionCode;
    /**货位名称*/
    private String positionName;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public void setSpecificationType(String specificationType) {
        this.specificationType = specificationType;
    }

    public Double getAmountOfBoxes() {
        return amountOfBoxes;
    }

    public void setAmountOfBoxes(Double amountOfBoxes) {
        this.amountOfBoxes = amountOfBoxes;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPickingDate() {
        return pickingDate;
    }

    public void setPickingDate(String pickingDate) {
        this.pickingDate = pickingDate;
    }

    public int getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(int boxNum) {
        this.boxNum = boxNum;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getFurnaceNumber() {
        return furnaceNumber;
    }

    public void setFurnaceNumber(String furnaceNumber) {
        this.furnaceNumber = furnaceNumber;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
