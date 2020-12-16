package com.digitzones.procurement.vo;

public class BoxBarVO {
    private Long barCode;
    /**入库申请单详情id*/
    private String formDetailId;
    /**入库申请单编号*/
    private String formNo;
    /**物料编码*/
    private String inventoryCode;
    /**物料名称*/
    private String inventoryName;
    /**默认仓库*/
    private String defaultWarehouseCode;
    /**每箱数*/
    private Double amountOfPerBox;
    /**箱数*/
    private Double amountOfBoxes;
    /**数量*/
    private Double amount;
    /**箱号*/
    private int boxNum;
    /**采购单号*/
    private String purchasingNo;
    /**来源*/
    private String source;
    /**类型：采购入库单*/
    private String boxBarType = "采购入库单";
    /**二维码路径*/
    private String qrPath;
    /**入库日期*/
    private String warehousingDate;
    /**报工日期*/
    private String jobBookingDate;
    /**生产单元*/
    private String productionLine;


    /**规格型号*/
    private String specificationType;
    /**批号*/
    private String batchNumber;
    /**材料编号*/
    private String furnaceNumber;
    /**炉号*/
    private String stoveNumber;
    /**剩余数量*/
    private Double SurplusNum;
    /** 入库单号*/
    private String WarehouseNo;
    /** 入库人名称*/
    private String employeeName;
    /**入库仓库编码*/
    private String warehouseCode;
    /**入库仓库名称*/
    private String warehouseName;
    /**厂商*/
    private String manufacturer;
    /**班次编码*/
    private String classCode;
    /**班次名称*/
    private String className;

    /**货位编码*/
    private String positonCode;
    /**货位名称*/
    private String positonName;

    /**是否领料*/
    private Boolean haveRequisition=false;
    /**领料时间*/
    private String requisitionDate;


    public String getDefaultWarehouseCode() {
        return defaultWarehouseCode;
    }

    public void setDefaultWarehouseCode(String defaultWarehouseCode) {
        this.defaultWarehouseCode = defaultWarehouseCode;
    }

    public Boolean getHaveRequisition() {
        return haveRequisition;
    }

    public void setHaveRequisition(Boolean haveRequisition) {
        this.haveRequisition = haveRequisition;
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getWarehouseNo() {
        return WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public String getJobBookingDate() {
        return jobBookingDate;
    }

    public void setJobBookingDate(String jobBookingDate) {
        this.jobBookingDate = jobBookingDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getSurplusNum() {
        return SurplusNum;
    }

    public void setSurplusNum(Double surplusNum) {
        SurplusNum = surplusNum;
    }

    public String getWarehousingDate() {
        return warehousingDate;
    }

    public void setWarehousingDate(String warehousingDate) {
        this.warehousingDate = warehousingDate;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public void setSpecificationType(String specificationType) {
        this.specificationType = specificationType;
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

    public Long getBarCode() {
        return barCode;
    }

    public void setBarCode(Long barCode) {
        this.barCode = barCode;
    }

    public String getFormDetailId() {
        return formDetailId;
    }

    public void setFormDetailId(String formDetailId) {
        this.formDetailId = formDetailId;
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

    public String getBoxBarType() {
        return boxBarType;
    }

    public void setBoxBarType(String boxBarType) {
        this.boxBarType = boxBarType;
    }

    public String getQrPath() {
        return qrPath;
    }

    public void setQrPath(String qrPath) {
        this.qrPath = qrPath;
    }

    public String getStoveNumber() {
        return stoveNumber;
    }

    public void setStoveNumber(String stoveNumber) {
        this.stoveNumber = stoveNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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
}
