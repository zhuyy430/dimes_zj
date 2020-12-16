package com.digitzones.model;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

/**
 * 原材料实体，可以做为半成品或成品的原材料
 * 可以是仓库中领的料，也可以是上一次报工的料
 */
@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class RawMaterial implements Serializable {
    @Id
    @GeneratedValue(generator = "id_generator")
    private String id;
    /**报工单详情*/
    @ManyToOne
    @JoinColumn(name="JOBBOOKINGFORMDETAIL_ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private JobBookingFormDetail jobBookingFormDetail;
    /**报工单详情id*/
    @Transient
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
    /**领用单Id*/
    private String MaterialRequisitionDetailId;
    /**数量*/
    private Double amount;
    /**消耗数量*/
    private Double amountOfUsed=0d;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public JobBookingFormDetail getJobBookingFormDetail() {
        return jobBookingFormDetail;
    }
    public void setJobBookingFormDetail(JobBookingFormDetail jobBookingFormDetail) {
        this.jobBookingFormDetail = jobBookingFormDetail;
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
	public String getMaterialRequisitionDetailId() {
		return MaterialRequisitionDetailId;
	}
	public void setMaterialRequisitionDetailId(String materialRequisitionDetailId) {
		MaterialRequisitionDetailId = materialRequisitionDetailId;
	}
}
