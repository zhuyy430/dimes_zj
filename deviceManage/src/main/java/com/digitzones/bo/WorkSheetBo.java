package com.digitzones.bo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxf
 * @date 2020-7-21
 * @time 8:51
 */
public class WorkSheetBo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**批号*/
    private String batchNumber;
    /**工单编号*/
    private String no;
    /**生产单元名称*/
    private String productionUnitName;
    /**工件代码*/
    private String workPieceCode;
    /**工件名称*/
    private String workPieceName;
    /**规格型号*/
    private String unitType;
    /**材料编号*/
    private String stoveNumber;
    /**生产总数量*/
    private Integer productCount;
    @Transient
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date firstMaterialDate;
    /**领料数量*/
    private Integer materialCount;
    /**领料箱数*/
    private Integer materialBoxNum;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastJobbookingDate;
    /**报工数量*/
    private Integer jobbookingCount;
    /**报工箱数*/
    private Integer jobbookingBoxNum;
    /**入库数量*/
    private Integer jobbookingInwarehouseCount;
    /**不合格数量*/
    private Integer unqualifiedCounts;
    /**料耗*/
    private Double cInvDefine14;

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getUnqualifiedCounts() {
        return unqualifiedCounts;
    }

    public void setUnqualifiedCounts(Integer unqualifiedCounts) {
        this.unqualifiedCounts = unqualifiedCounts;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProductionUnitName() {
        return productionUnitName;
    }

    public void setProductionUnitName(String productionUnitName) {
        this.productionUnitName = productionUnitName;
    }

    public String getWorkPieceCode() {
        return workPieceCode;
    }

    public void setWorkPieceCode(String workPieceCode) {
        this.workPieceCode = workPieceCode;
    }

    public String getWorkPieceName() {
        return workPieceName;
    }

    public void setWorkPieceName(String workPieceName) {
        this.workPieceName = workPieceName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getStoveNumber() {
        return stoveNumber;
    }

    public void setStoveNumber(String stoveNumber) {
        this.stoveNumber = stoveNumber;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public Date getFirstMaterialDate() {
        return firstMaterialDate;
    }

    public void setFirstMaterialDate(Date firstMaterialDate) {
        this.firstMaterialDate = firstMaterialDate;
    }

    public Integer getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
    }

    public Integer getMaterialBoxNum() {
        return materialBoxNum;
    }

    public void setMaterialBoxNum(Integer materialBoxNum) {
        this.materialBoxNum = materialBoxNum;
    }

    public Date getLastJobbookingDate() {
        return lastJobbookingDate;
    }

    public void setLastJobbookingDate(Date lastJobbookingDate) {
        this.lastJobbookingDate = lastJobbookingDate;
    }

    public Integer getJobbookingCount() {
        return jobbookingCount;
    }

    public void setJobbookingCount(Integer jobbookingCount) {
        this.jobbookingCount = jobbookingCount;
    }

    public Integer getJobbookingBoxNum() {
        return jobbookingBoxNum;
    }

    public void setJobbookingBoxNum(Integer jobbookingBoxNum) {
        this.jobbookingBoxNum = jobbookingBoxNum;
    }

    public Integer getJobbookingInwarehouseCount() {
        return jobbookingInwarehouseCount;
    }

    public void setJobbookingInwarehouseCount(Integer jobbookingInwarehouseCount) {
        this.jobbookingInwarehouseCount = jobbookingInwarehouseCount;
    }

    public Double getcInvDefine14() {
        return cInvDefine14;
    }

    public void setcInvDefine14(Double cInvDefine14) {
        this.cInvDefine14 = cInvDefine14;
    }
}
