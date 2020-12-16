package com.digitzones.procurement.vo;
import java.io.Serializable;
/**
 * 钢厂
 */
public class SteelMillVO implements Serializable {
    /**别名*/
    private String cAlias;
    private String cId;
    private String cValue;
    public String getcAlias() {
        return cAlias;
    }
    public void setcAlias(String cAlias) {
        this.cAlias = cAlias;
    }
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
}
