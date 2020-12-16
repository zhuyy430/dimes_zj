package com.digitzones.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
/**
 * 生产领料单
 */
@Entity
public class MaterialRequisition {
    /**领料单号*/
    @Id
    private String formNo;
    /**领料日期*/
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pickingDate;
    /**领料人编码*/
    private String pickerCode;
    /**领料人名称*/
    private String pickerName;
    /**领料仓库编码*/
    private String warehouseCode;
    /**领料仓库名称*/
    private String warehouseName;
    /**生成ERP领料单状态：未生成、已生成*/
    private String status="未生成";
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    /**领料工单*/
    @ManyToOne
    @JoinColumn(name="WORKSHEETNO",referencedColumnName = "no",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WorkSheet workSheet;
    public String getFormNo() {
        return formNo;
    }
    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
    public Date getPickingDate() {
        return pickingDate;
    }
    public void setPickingDate(Date pickingDate) {
        this.pickingDate = pickingDate;
    }
    public String getPickerCode() {
        return pickerCode;
    }
    public void setPickerCode(String pickerCode) {
        this.pickerCode = pickerCode;
    }
    public String getPickerName() {
        return pickerName;
    }
    public void setPickerName(String pickerName) {
        this.pickerName = pickerName;
    }
    public WorkSheet getWorkSheet() {
        return workSheet;
    }
    public void setWorkSheet(WorkSheet workSheet) {
        this.workSheet = workSheet;
    }
}
