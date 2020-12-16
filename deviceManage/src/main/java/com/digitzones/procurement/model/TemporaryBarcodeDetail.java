package com.digitzones.procurement.model;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
/**
 * 临时条码详情
 */
@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class TemporaryBarcodeDetail implements Serializable {
	private static final long serialVersionUID = 1L;
    /**临时条码编码*/
	@Id
    @GeneratedValue(generator = "id_generator")
	private String id;
	/**临时条码*/
	@ManyToOne
    @JoinColumn(name="TEMPORARYBARCODE_CODE")
	private TemporaryBarcode temporaryBarcode;
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
	/**备注*/
	private String note;
    /**单据类型：0:新增、1:报工条码、2:材料条码*/
	private String billType;
    /**来源条码*/
	private String sourceBarcode;
    /**厂商*/
    private String manufacturer;
    /**货位编码*/
    private String positonCode;
    public String getSourceBarcode() {
        return sourceBarcode;
    }
    public void setSourceBarcode(String sourceBarcode) {
        this.sourceBarcode = sourceBarcode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public TemporaryBarcode getTemporaryBarcode() {
        return temporaryBarcode;
    }
    public void setTemporaryBarcode(TemporaryBarcode temporaryBarcode) {
        this.temporaryBarcode = temporaryBarcode;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getBillType() {
        return billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
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
}
