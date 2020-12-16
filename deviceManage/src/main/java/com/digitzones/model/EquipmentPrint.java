package com.digitzones.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 装备打印记录实体
 * @author xuxf
 * @date 2020-5-6
 * @time 11:24
 */
@Entity
public class EquipmentPrint {
    /**装备编码*/
    @Id
    private String code;
    /**装备批号*/
    private String batchNo;
    /**打印状态 已打印: true，未打印: false*/
    private Boolean status=false;
    /**打印时间*/
    private Date printDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }
}
