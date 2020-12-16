package com.digitzones.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 检验记录
 */
@Entity
public class InspectionRecord implements Serializable {
	private static final long serialVersionUID = 1L;
    /**检验单号*/
	@Id
	private String formNo;
	/**检验时间*/
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date inspectionDate;
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
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String furnaceNumber;
	/**生产单元编码*/
	private String productionUnitCode;
	/**生产单元名称*/
	private String productionUnitName;
    /**检验结果：NG或OK*/
	private String inspectionResult ;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
	/**检验员编码*/
	private String inspectorCode;
	/**检验员名称*/
	private String inspectorName;
	/**检验类型:首检、巡检、班检、末检*/
	private String inspectionType;

    public String getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getProductionUnitCode() {
        return productionUnitCode;
    }

    public void setProductionUnitCode(String productionUnitCode) {
        this.productionUnitCode = productionUnitCode;
    }

    public String getProductionUnitName() {
        return productionUnitName;
    }

    public void setProductionUnitName(String productionUnitName) {
        this.productionUnitName = productionUnitName;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
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

    public String getInspectorCode() {
        return inspectorCode;
    }

    public void setInspectorCode(String inspectorCode) {
        this.inspectorCode = inspectorCode;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }
}
