package com.digitzones.dto;

import com.digitzones.model.MaterialRequisition;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
/**
 * 领用单详情
 */
public class MaterialRequisitionDetailDto {
    private String id;
    /**箱条码*/
    private String barCode;
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
    /**箱号*/
    private int boxNum;
    /**批号*/
    private String batchNumber;
    /**材料编号*/
    private String furnaceNumber;
    /**备注*/
    private String note;
    /**货位编码*/
    private String positionCode;
    /**货位名称*/
    private String positionName;
    /**班次名称*/
	private String className;
    /**所有的班次编码:领料时只能领取该值相同的料，报工时，需要将该字段加上当前的班次*/
    private String allClassCodes="";

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
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAllClassCodes() {
		return allClassCodes;
	}

	public void setAllClassCodes(String allClassCodes) {
		this.allClassCodes = allClassCodes;
	}
}
