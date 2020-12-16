package com.digitzones.procurement.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
/**
 * 钢厂复核主键
 */
@Embeddable
public class SteelMillPK implements Serializable {
    /**自定义属性：类别id*/
    private String cId;
    /**值*/
    private String cValue;
    public String getcId() {
        return cId;
    }
    public void setcId(String cId) {
        this.cId = cId;
    }
    public String getcValue() {
        return cValue;
    }
    public void setcValue(String cValue) {
        this.cValue = cValue;
    }

    @Override
    public int hashCode() {
        return cId.hashCode() + cValue.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }
        if(obj instanceof SteelMillPK){
            SteelMillPK steelMillPK = (SteelMillPK) obj;
            if(cId.equals(steelMillPK.getcId()) && cValue.equals(steelMillPK.getcValue())){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
}
