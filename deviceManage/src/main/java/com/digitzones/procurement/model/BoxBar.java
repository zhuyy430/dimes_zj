package com.digitzones.procurement.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
/**
 * 箱号条码
 */
@Entity
public class BoxBar {
    /**条码*/
    @Id
    private Long barCode;
    /**单据编号*/
    private String formNo;
    /**物料编码*/
    private String inventoryCode;
    /**物料名称*/
    private String inventoryName;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate = new Date();
    /**每箱数*/
    private Double amountOfPerBox;
    /**箱数*/
    private Double amountOfBoxes;
    /**规格型号*/
    private String specificationType;
    /**单位编码*/
    private String unitCode;
    /**钢厂*/
    private String manufacturer;
    /**单位名称*/
    private String unitName;
    /**批号*/
    private String batchNumber;
    /**材料编号*/
    private String furnaceNumber;
    /**炉号*/
    private String stoveNumber;
    /**数量*/
    private Double amount;
    /**箱号*/
    private int boxNum;
    /**采购单号,工单单号或其他单号*/
    private String purchasingNo;
    /**来源*/
    private String source;
    /**类型：采购入库单，报工单,发货单,临时条码*/
    private String boxBarType = "采购入库单";
    /** 入库单号*/
    private String WarehouseNo;
    /** 入库时间*/
    private Date WarehouseDate;
    /** 入库人代码*/
    private String employeeCode;
    /** 入库人名称*/
    private String employeeName;
    /**入库仓库编码*/
    private String warehouseCode;
    /**入库仓库名称*/
    private String warehouseName;
    /**剩余数量*/
    private Double SurplusNum;
    /**可包装数量*/
    private Double packingNum;
    /**货位编码*/
    private String positonCode;
    /**货位名称*/
    private String positonName;
    /**外键*/
    private String fkey;
    /**是否领料*/
    private Boolean haveRequisition=false;
    /**领料时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date requisitionDate;


    public Boolean getHaveRequisition() {
        return haveRequisition;
    }

    public void setHaveRequisition(Boolean haveRequisition) {
        this.haveRequisition = haveRequisition;
    }

    public Date getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(Date requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStoveNumber() {
        return stoveNumber;
    }
    public void setStoveNumber(String stoveNumber) {
        this.stoveNumber = stoveNumber;
    }
    public String getPositonCode() {
        return positonCode;
    }
    public void setPositonCode(String positonCode) {
        this.positonCode = positonCode;
    }
    public String getPositonName() {
        return positonName;
    }

    public void setPositonName(String positonName) {
        this.positonName = positonName;
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
    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    /***外键所属类名:JobBookingDetail*/
    private String className;
    /**外键所对应的表明*/
    private String tableName;
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
    /**是否已入库*/
    private Boolean isEnterWarehouse=false;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Double getSurplusNum() {
        return SurplusNum;
    }
    public void setSurplusNum(Double surplusNum) {
        SurplusNum = surplusNum;
    }
    public String getEmployeeCode() {
        return employeeCode;
    }
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public Date getWarehouseDate() {
        return WarehouseDate;
    }
    public void setWarehouseDate(Date warehouseDate) {
        WarehouseDate = warehouseDate;
    }
    public String getWarehouseNo() {
        return WarehouseNo;
    }
    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }
    public Boolean getEnterWarehouse() {
        return isEnterWarehouse;
    }
    public void setEnterWarehouse(Boolean enterWarehouse) {
        isEnterWarehouse = enterWarehouse;
    }
    public String getBoxBarType() {
        return boxBarType;
    }
    public void setBoxBarType(String boxBarType) {
        this.boxBarType = boxBarType;
    }
    public Long getBarCode() {
        return barCode;
    }
    public void setBarCode(Long barCode) {
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
    public String getPurchasingNo() {
        return purchasingNo;
    }
    public void setPurchasingNo(String purchasingNo) {
        this.purchasingNo = purchasingNo;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public Double getPackingNum() {
        return packingNum;
    }

    public void setPackingNum(Double packingNum) {
        this.packingNum = packingNum;
    }
}
