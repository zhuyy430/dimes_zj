package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 供应商类别
 */
@Entity
@Subselect("select * from VIEW_VENDORCLASS")
public class VendorClass implements Serializable {
    /**供应商类别编码*/
    @Id
    @GeneratedValue
    private String cvcCode;
    /**供应商类别名称*/
    private String cvcName;
    public String getCvcCode() {
        return cvcCode;
    }
    public void setCvcCode(String cvcCode) {
        this.cvcCode = cvcCode;
    }
    public String getCvcName() {
        return cvcName;
    }
    public void setCvcName(String cvcName) {
        this.cvcName = cvcName;
    }
}
