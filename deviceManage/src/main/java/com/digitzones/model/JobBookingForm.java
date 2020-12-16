package com.digitzones.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
/**
 * 报工单
 */
@Entity
public class JobBookingForm implements Serializable {
	private static final long serialVersionUID = 1L;
    /**报工单编码*/
	@Id
	private String formNo;
	/**生产单元编码*/
	private String productionUnitCode;
	/**生产单元名称*/
	private String productionUnitName;
    /**审核状态*/
	private String auditStatus="未审核";
	/**审核人编码*/
	private String auditorCode;
	/**审核人名称*/
	private String auditorName;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;

	/**审核时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
	private Date auditDate;
    /**报工日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
	private Date jobBookingDate;
    /**报工单的产生日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date formDate;
    /**报工人员编码*/
    private String jobBookerCode;
    /**报工人员名称*/
    private String jobBookerName;
    /**单位*/
    private String unit;
    /**工单单号*/
    private String workSheetNo;
    /**物料代码*/
    private String inventoryCode;
    /**物料名称*/
    private String inventoryName;
    /**规格型号*/
    private String unitType;
    /**工序代码*/
    private String processesCode;
    /**工序名称*/
    private String processesName;
    /**站点代码*/
    private String deviceSiteCode;
    /**站点名称*/
    private String deviceSiteName;
    /**批号*/
    private String batchNumber;
    /**材料编号*/
    private String stoveNumber;
    /**箱数*/
    private Double amountOfBoxes = 0d;
    /**报工数*/
    private Double amountOfJobBooking = 0d;
    public String getWorkSheetNo() {
        return workSheetNo;
    }
    public void setWorkSheetNo(String workSheetNo) {
        this.workSheetNo = workSheetNo;
    }
    public Double getAmountOfBoxes() {
        return amountOfBoxes;
    }
    public void setAmountOfBoxes(Double amountOfBoxes) {
        this.amountOfBoxes = amountOfBoxes;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Double getAmountOfJobBooking() {
        return amountOfJobBooking;
    }
    public void setAmountOfJobBooking(Double amountOfJobBooking) {
        this.amountOfJobBooking = amountOfJobBooking;
    }
    public String getFormNo() {
        return formNo;
    }
    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }
    public Date getFormDate() {
        return formDate;
    }
    public void setFormDate(Date formDate) {
        this.formDate = formDate;
    }
    public String getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getAuditorCode() {
        return auditorCode;
    }
    public void setAuditorCode(String auditorCode) {
        this.auditorCode = auditorCode;
    }
    public String getAuditorName() {
        return auditorName;
    }
    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
    public Date getAuditDate() {
        return auditDate;
    }
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
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
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
    public Date getJobBookingDate() {
        return jobBookingDate;
    }
    public void setJobBookingDate(Date jobBookingDate) {
        this.jobBookingDate = jobBookingDate;
    }
    public String getJobBookerCode() {
        return jobBookerCode;
    }
    public void setJobBookerCode(String jobBookerCode) {
        this.jobBookerCode = jobBookerCode;
    }
    public String getJobBookerName() {
        return jobBookerName;
    }
    public void setJobBookerName(String jobBookerName) {
        this.jobBookerName = jobBookerName;
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
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getProcessesCode() {
		return processesCode;
	}
	public void setProcessesCode(String processesCode) {
		this.processesCode = processesCode;
	}
	public String getProcessesName() {
		return processesName;
	}
	public void setProcessesName(String processesName) {
		this.processesName = processesName;
	}
	public String getDeviceSiteCode() {
		return deviceSiteCode;
	}
	public void setDeviceSiteCode(String deviceSiteCode) {
		this.deviceSiteCode = deviceSiteCode;
	}
	public String getDeviceSiteName() {
		return deviceSiteName;
	}
	public void setDeviceSiteName(String deviceSiteName) {
		this.deviceSiteName = deviceSiteName;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getStoveNumber() {
		return stoveNumber;
	}
	public void setStoveNumber(String stoveNumber) {
		this.stoveNumber = stoveNumber;
	}
}
