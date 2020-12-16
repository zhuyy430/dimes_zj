package com.digitzones.procurement.dto;
import java.io.Serializable;

/**
 * 入库申请单详情
 */
public class WarehousingApplicationFormDetailDto implements Serializable {
	private static final long serialVersionUID = 1L;
    /**申请单编码*/
	private String id;
    /**采购单号*/
	private String purchasingNo;
	/**物料类别编码*/
	private String inventoryTypeCode;
	/**物料类别名称*/
	private String inventoryTypeName;
	/**物料编码*/
	private String inventoryCode;
	/**物料名称*/
	private String inventoryName;
    /**规格型号*/
	private String specificationType;
    /**单位*/
	private String unit;
    /**数量*/
	private Double amount;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String furnaceNumber;
    /**炉号*/
    private String stoveNumber;
	/**每箱数*/
	private Double amountOfPerBox;
	/**箱数*/
	private Double amountOfBoxes;
	/**仓库编码*/
	private String warehouseCode;
	/**仓库名称*/
	private String warehouseName;
	/**入库数量*/
	private Double amountOfInWarehouse;
	/**检验状态:默认未检验,通过检验，未通过检验*/
	private String checkStatus="未检验";
    /**厂商*/
    private String manufacturer;
    /**货位编码*/
    private String locationCode;
    /**货位名称*/
    private String locationName;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getCheckStatus() {
        return checkStatus;
    }
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPurchasingNo() {
        return purchasingNo;
    }
    public void setPurchasingNo(String purchasingNo) {
        this.purchasingNo = purchasingNo;
    }
    public String getInventoryTypeCode() {
        return inventoryTypeCode;
    }
    public void setInventoryTypeCode(String inventoryTypeCode) {
        this.inventoryTypeCode = inventoryTypeCode;
    }
    public String getInventoryTypeName() {
        return inventoryTypeName;
    }
    public void setInventoryTypeName(String inventoryTypeName) {
        this.inventoryTypeName = inventoryTypeName;
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
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
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
    public Double getAmountOfPerBox() {
        return amountOfPerBox;
    }
    public void setAmountOfPerBox(Double amountOfPerBox) {
        this.amountOfPerBox = amountOfPerBox;
    }
    public Double getAmountOfBoxes() {
        return amountOfBoxes;
    }
    public void setAmountOfBoxes(Double amountOfBoxes) {
        this.amountOfBoxes = amountOfBoxes;
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
    public Double getAmountOfInWarehouse() {
        return amountOfInWarehouse;
    }
    public void setAmountOfInWarehouse(Double amountOfInWarehouse) {
        this.amountOfInWarehouse = amountOfInWarehouse;
    }
    public String getStoveNumber() {
        return stoveNumber;
    }
    public void setStoveNumber(String stoveNumber) {
        this.stoveNumber = stoveNumber;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
