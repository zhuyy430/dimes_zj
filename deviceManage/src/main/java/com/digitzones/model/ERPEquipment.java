package com.digitzones.model;

/**
 * ERP读取的装备
 * @author xuxf
 * @date 2020-5-6
 * @time 11:19
 */

import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Subselect("select * from view_Equipment")
public class ERPEquipment {
    @Id
    private Integer id;
    /**装备编码*/
    private String code ;
    /**装备名称*/
    private String name ;
    /**装备批号*/
    private String batchNo ;
    /**规格型号*/
    private String unitType;
    /**数量*/
    private Double qty;
    /**模具寿命*/
    private String useLife;
    /**入库时间*/
    private Date inWarehouseDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getUseLife() {
        return useLife;
    }

    public void setUseLife(String useLife) {
        this.useLife = useLife;
    }

    public Date getInWarehouseDate() {
        return inWarehouseDate;
    }

    public void setInWarehouseDate(Date inWarehouseDate) {
        this.inWarehouseDate = inWarehouseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
