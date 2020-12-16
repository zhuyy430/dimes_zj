package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.digitzones.model.ProjectType;
/**
 * 保养备品备件
 * @author zdq
 * 2019年1月3日
 */
@Entity
public class MaintenanceSparepart implements Serializable {
	private static final long serialVersionUID = -4711833140793307948L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**备件代码*/
	private String code;
	/**备件名称*/
	private String name;
	/**规格型号*/
	private String unitType;
	/**制造商，生产厂家*/
	private String manufacturer;
	/**图号*/
	private String graphNumber;
	/**计量单位*/
	private String measurementUnit;
	/**批号*/
	private String batchNumber;
	/**助记码*/
	private String mnemonicCode;
	/**备注*/
	private String note;
	/**数量*/
	@Column(name="_COUNT")
	private int count;
	/**耗用日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date useDate;
	/**所属类别*/
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private ProjectType projectType;
	/**所属设备保养记录*/
	@ManyToOne
	@JoinColumn(name="MAINTENANCEPLANRECORD_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private MaintenancePlanRecord maintenancePlanRecord;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getUseDate() {
		return useDate;
	}
	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}
	public MaintenancePlanRecord getMaintenancePlanRecord() {
		return maintenancePlanRecord;
	}
	public void setMaintenancePlanRecord(MaintenancePlanRecord maintenancePlanRecord) {
		this.maintenancePlanRecord = maintenancePlanRecord;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getMnemonicCode() {
		return mnemonicCode;
	}
	public void setMnemonicCode(String mnemonicCode) {
		this.mnemonicCode = mnemonicCode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}
