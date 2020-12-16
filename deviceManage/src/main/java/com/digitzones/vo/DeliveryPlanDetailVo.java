package com.digitzones.vo;
import com.digitzones.model.DeliveryPlan;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 发货计划详情
 */
public class DeliveryPlanDetailVo implements Serializable {
	private static final long serialVersionUID = 1L;
    /**发货单详情编码*/
	private String id;
	private DeliveryPlan deliveryPlan;
	/**计划发货日期*/
	private String deliverDateOfPlan;
	/**销售订单编号*/
	private String formNo;
	/**销售订单详情id*/
	private Integer autoId;
	/**客户编码*/
	private String customerCode;
	/**客户名称*/
	private String customerName;
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
	/**计划数量*/
	private Double amountOfPlan;
	/**实发数量*/
	private Double amountOfSended;
	/**仓库编码*/
	private String warehouseCode;
	/**仓库名称*/
	private String warehouseName;
	/**出货批号*/
	private String batchNumberOfSended;
    /**质检报告(单号)*/
	private String inspectionReport;
	/**备注*/
	private String note;
	/**状态:计划、部分发货、全部已发*/
	private String status="计划";
    /**单重*/
    private Double weight;
    public Integer getAutoId() {
        return autoId;
    }
    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDeliverDateOfPlan() {
        return deliverDateOfPlan;
    }

    public void setDeliverDateOfPlan(String deliverDateOfPlan) {
        this.deliverDateOfPlan = deliverDateOfPlan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DeliveryPlan getDeliveryPlan() {
        return deliveryPlan;
    }

    public void setDeliveryPlan(DeliveryPlan deliveryPlan) {
        this.deliveryPlan = deliveryPlan;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Double getAmountOfPlan() {
        return amountOfPlan;
    }

    public void setAmountOfPlan(Double amountOfPlan) {
        this.amountOfPlan = amountOfPlan;
    }

    public Double getAmountOfSended() {
        return amountOfSended;
    }

    public void setAmountOfSended(Double amountOfSended) {
        this.amountOfSended = amountOfSended;
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

    public String getBatchNumberOfSended() {
        return batchNumberOfSended;
    }

    public void setBatchNumberOfSended(String batchNumberOfSended) {
        this.batchNumberOfSended = batchNumberOfSended;
    }

    public String getInspectionReport() {
        return inspectionReport;
    }

    public void setInspectionReport(String inspectionReport) {
        this.inspectionReport = inspectionReport;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
