package com.digitzones.dto;
import com.digitzones.model.JobBookingForm;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 报工单详情
 */
public class JobBookingFormDetailDto implements Serializable {
	private static final long serialVersionUID = 1L;
    /**申请单编码*/
	private String id;
	/**箱条码*/
	private String barCode;

    /**设备站点编码*/
	private String deviceSiteCode;
	/**设备站点名称*/
	private String deviceSiteName;
	/**工序编码*/
	private String processCode;
	/**工序名称*/
	private String processName;
    /**工单单号*/
	private String no;
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
    /**数量*/
	private Double amount;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String furnaceNumber;
	/**每箱数*/
	private Double amountOfPerBox;
	/**箱数*/
	private Double amountOfBoxes;
	/**箱号*/
	private Integer boxNum;
	/**报工数*/
	private Double amountOfJobBooking;
	/**仓库编码*/
	private String warehouseCode;
	/**仓库名称*/
	private String warehouseName;
	/**入库数量*/
	private Double amountOfInWarehouse;
    public Integer getBoxNum() {
        return boxNum;
    }
    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }
    public String getDeviceSiteCode() {
        return deviceSiteCode;
    }
    public void setDeviceSiteCode(String deviceSiteCode) {
        this.deviceSiteCode = deviceSiteCode;
    }
    public String getDeviceSiteName() {
        return deviceSiteName;
    }
    public void setDeviceSiteName(String deviceSiteName) {
        this.deviceSiteName = deviceSiteName;
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

    public String getProcessCode() {
        return processCode;
    }
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }
    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    public String getBarCode() {
        return barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
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
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public Double getAmountOfJobBooking() {
        return amountOfJobBooking;
    }
    public void setAmountOfJobBooking(Double amountOfJobBooking) {
        this.amountOfJobBooking = amountOfJobBooking;
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
