package com.digitzones.dto;

import java.io.Serializable;
/**
 * 原材料实体，可以做为半成品或成品的原材料
 * 可以是仓库中领的料，也可以是上一次报工的料
 */
public class RawMaterialDto implements Serializable {
    private String id;
    /**报工单详情id*/
    private String jobBookingFormDetailId;
    /**材料条码*/
    private String barCode;
    /**物料编码*/
    private String inventoryCode;
    /**物料名称*/
    private String inventoryName;
    /**规格型号*/
    private String specificationType;
    /**单位编码*/
    private String unitCode;
    /**单位名称*/
    private String unitName;
    /**批号*/
    private String batchNumber;
    /**材料编号*/
    private String furnaceNumber;
    /**数量*/
    private Double amount;
    /**消耗数量*/
    private Double amountOfUsed=0d;
    /**领用单Id*/
    private String materialRequisitionDetailId;

    public String getMaterialRequisitionDetailId() {
        return materialRequisitionDetailId;
    }

    public void setMaterialRequisitionDetailId(String materialRequisitionDetailId) {
        this.materialRequisitionDetailId = materialRequisitionDetailId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getUnitCode() {
        return unitCode;
    }
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
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
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Double getAmountOfUsed() {
        return amountOfUsed;
    }
    public void setAmountOfUsed(Double amountOfUsed) {
        this.amountOfUsed = amountOfUsed;
    }
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
    public String getJobBookingFormDetailId() {
        return jobBookingFormDetailId;
    }
    public void setJobBookingFormDetailId(String jobBookingFormDetailId) {
        this.jobBookingFormDetailId = jobBookingFormDetailId;
    }
}
