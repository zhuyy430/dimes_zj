package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
/**
 * 钢厂
 */
@Entity
@Subselect("select * from view_UserDefine")
public class SteelMill implements Serializable {
    @EmbeddedId
    private SteelMillPK steelMillPK;
    /**别名*/
    private String cAlias;
    public String getcAlias() {
        return cAlias;
    }
    public void setcAlias(String cAlias) {
        this.cAlias = cAlias;
    }
    public SteelMillPK getSteelMillPK() {
        return steelMillPK;
    }
    public void setSteelMillPK(SteelMillPK steelMillPK) {
        this.steelMillPK = steelMillPK;
    }
}
