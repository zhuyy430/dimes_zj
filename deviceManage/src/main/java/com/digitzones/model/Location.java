package com.digitzones.model;
import org.hibernate.annotations.Subselect;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
/**
 * 货位
 */
@Entity
@Subselect("select * from view_Position")
public class Location {
    /**货位编码*/
    @Id
    private String cPosCode ;
    /**货位名称*/
    private String cPosName ;
    /**仓库编码*/
    private String cWhCode ;
    public String getcPosCode() {
        return cPosCode;
    }
    public void setcPosCode(String cPosCode) {
        this.cPosCode = cPosCode;
    }
    public String getcPosName() {
        return cPosName;
    }
    public void setcPosName(String cPosName) {
        this.cPosName = cPosName;
    }
    public String getcWhCode() {
        return cWhCode;
    }
    public void setcWhCode(String cWhCode) {
        this.cWhCode = cWhCode;
    }
}
