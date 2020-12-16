package com.digitzones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 报工单详情
 */
@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class JobBookingFormDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	/**原材料*/
	@Transient
    @JsonIgnore
	private List<RawMaterial> rawMaterialList;
    /**报工单编码*/
	@Id
    @GeneratedValue(generator = "id_generator")
	private String id;
	/**箱条码*/
	private String barCode;
	/**箱号*/
	private Integer boxNum;
	/**班次编码*/
	private String classCode;
	/**班次名称*/
	private String className;
    /**所有的班次编码:领料时只能领取该值相同的料，报工时，需要将该字段加上当前的班次*/
    private String allClassCodes="";
    /**所属的报工时间*/
    private String forJobBookingDate;

	/**报工单*/
	@ManyToOne
    @JoinColumn(name="JOBBOOKING_CODE" ,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private JobBookingForm jobBookingForm;
    /**设备站点编码*/
	private String deviceSiteCode;
	/**设备站点名称*/
	private String deviceSiteName;
    /**设备编码*/
	private String deviceCode;
	/**设备名称*/
	private String deviceName;
	/**工序编码*/
	private String processCode;
	/**工序名称*/
	private String processName;
    /**工单单号*/
	private String no;
	/**物料编码*/
	private String inventoryCode;
	/**物料名称*/
	private String inventoryName;
    /**规格型号*/
	private String specificationType;
    /**单位编码*/
	private String unitCode;
    /**单位名称*/
	private String unitName;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String furnaceNumber;
	/**每箱数*/
	private Double amountOfPerBox;
	/**箱数*/
	private Double amountOfBoxes;
	/**报工数*/
	private Double amountOfJobBooking;
	/**仓库编码*/
	private String warehouseCode;
	/**仓库名称*/
	private String warehouseName;
	/**入库数量*/
	private Double amountOfInWarehouse;

    /**是否领料*/
    private Boolean haveRequisition=false;
    /**领料时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date requisitionDate;

    public Boolean getHaveRequisition() {
        return haveRequisition;
    }

    public void setHaveRequisition(Boolean haveRequisition) {
        this.haveRequisition = haveRequisition;
    }

    public Date getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(Date requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getAllClassCodes() {
        return allClassCodes==null?"":allClassCodes;
    }

    public void setAllClassCodes(String allClassCodes) {
        this.allClassCodes = allClassCodes;
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
    public String getDeviceCode() {
        return deviceCode;
    }
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getProcessCode() {
        return processCode;
    }
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }
    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    public String getBarCode() {
        return barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getSpecificationType() {
        return specificationType;
    }
    public void setSpecificationType(String specificationType) {
        this.specificationType = specificationType;
    }
    public String getBatchNumber() {
        return batchNumber;
    }
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    public String getFurnaceNumber() {
        return furnaceNumber;
    }
    public void setFurnaceNumber(String furnaceNumber) {
        this.furnaceNumber = furnaceNumber;
    }
    public Double getAmountOfPerBox() {
        return amountOfPerBox;
    }
    public void setAmountOfPerBox(Double amountOfPerBox) {
        this.amountOfPerBox = amountOfPerBox;
    }
    public Double getAmountOfBoxes() {
        return amountOfBoxes;
    }
    public void setAmountOfBoxes(Double amountOfBoxes) {
        this.amountOfBoxes = amountOfBoxes;
    }
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
    public Double getAmountOfInWarehouse() {
        return amountOfInWarehouse;
    }
    public void setAmountOfInWarehouse(Double amountOfInWarehouse) {
        this.amountOfInWarehouse = amountOfInWarehouse;
    }
    public JobBookingForm getJobBookingForm() {
        return jobBookingForm;
    }
    public void setJobBookingForm(JobBookingForm jobBookingForm) {
        this.jobBookingForm = jobBookingForm;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public Double getAmountOfJobBooking() {
        return amountOfJobBooking;
    }
    public void setAmountOfJobBooking(Double amountOfJobBooking) {
        this.amountOfJobBooking = amountOfJobBooking;
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public List<RawMaterial> getRawMaterialList() {
        return rawMaterialList;
    }

    public void setRawMaterialList(List<RawMaterial> rawMaterialList) {
        this.rawMaterialList = rawMaterialList;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getForJobBookingDate() {
        return forJobBookingDate;
    }

    public void setForJobBookingDate(String forJobBookingDate) {
        this.forJobBookingDate = forJobBookingDate;
    }
}
