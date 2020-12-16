package com.digitzones.model;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.Set;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.digitzones.devmgr.model.DeviceLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 设备实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="DEVICE")
public class Device implements Serializable{
	private static final long serialVersionUID = 1L;
	/**单台功率*/
	private String power;
	/**实际功率*/
	private String actualPower;
	/**外形尺寸*/
	private String shapeSize;
	/**设备重量*/
	private String weight;
	/**资产编号*/
	private String assetNumber;
	/**规格型号*/
	private String unitType;
	/**设备状态*/
	private String status;
	/**制造商，生产厂家*/
	private String manufacturer;
	/**经销商*/
	private String trader;
	/**安装日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date installDate;
	/**到厂日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date inFactoryDate;
	/**验收日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date checkDate;
	/**出厂日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date outFactoryDate;
	/**出厂编号*/
	private String outFactoryCode;
	/**安装位置*/
	private String installPosition;
	/**参数取值
	 * 固定值
	 * 变动值
	 * */
	private String parameterValueType;
	/**设备类别*/
	private ProjectType projectType;
	/**设备等级*/
	private DeviceLevel deviceLevel;
	/**设备站点*/
	private Set<DeviceSite> deviceSites;
	/**生产单元*/
	private ProductionUnit productionUnit;
	/**设备图片*/
	private Blob photo;
	/**图片的名称*/
	private String photoName;
	/**对象唯一标识*/
	protected Long id;
	/**编号*/
	protected String code;
	/**名称*/
	protected String name;
	/**备注*/
	protected String note;
	/**是否停用*/
	protected Boolean disabled = false;
	/**该设备是否为dimes系统使用*/
	private Boolean isDimesUse;

	public Boolean getDimesUse() {
		return isDimesUse;
	}

	public void setDimesUse(Boolean dimesUse) {
		isDimesUse = dimesUse;
	}

	public Boolean getDeviceManageUse() {
		return isDeviceManageUse;
	}

	public void setDeviceManageUse(Boolean deviceManageUse) {
		isDeviceManageUse = deviceManageUse;
	}

	/**该设备是否为设备管理系统使用*/
	private Boolean isDeviceManageUse;
	public Boolean getIsDimesUse() {
		return isDimesUse;
	}
	public void setIsDimesUse(Boolean isDimesUse) {
		this.isDimesUse = isDimesUse;
	}
	public Boolean getIsDeviceManageUse() {
		return isDeviceManageUse;
	}
	public void setIsDeviceManageUse(Boolean isDeviceManageUse) {
		this.isDeviceManageUse = isDeviceManageUse;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	@JsonIgnore
	public Blob getPhoto() {
		return photo;
	}
	public void setPhoto(Blob photo) {
		this.photo = photo;
	}
	/**
	 * 设备和班次关联类
	 */
	private Set<ClassesDeviceMapping> classesDevice;
	@OneToMany(mappedBy="device",targetEntity=ClassesDeviceMapping.class)
	@JsonIgnore
	public Set<ClassesDeviceMapping> getClassesDevice() {
		return classesDevice;
	}
	public void setClassesDevice(Set<ClassesDeviceMapping> classesDevice) {
		this.classesDevice = classesDevice;
	}
	@OneToMany(mappedBy="device",fetch=FetchType.EAGER,targetEntity=DeviceSite.class)
	@OrderBy("ID DESC")
	@JsonIgnore
	public Set<DeviceSite> getDeviceSites() {
		return deviceSites;
	}
	public void setDeviceSites(Set<DeviceSite> deviceSites) {
		this.deviceSites = deviceSites;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getTrader() {
		return trader;
	}
	public void setTrader(String trader) {
		this.trader = trader;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInstallDate() {
		return installDate;
	}
	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOutFactoryDate() {
		return outFactoryDate;
	}
	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}
	public String getOutFactoryCode() {
		return outFactoryCode;
	}
	public void setOutFactoryCode(String outFactoryCode) {
		this.outFactoryCode = outFactoryCode;
	}
	public String getInstallPosition() {
		return installPosition;
	}
	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}
	public String getParameterValueType() {
		return parameterValueType;
	}
	public void setParameterValueType(String parameterValueType) {
		this.parameterValueType = parameterValueType;
	}
	@ManyToOne
	@JoinColumn(name="DEVICETYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
	@ManyToOne
	@JoinColumn(name="DEVICELEVEL_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	public DeviceLevel getDeviceLevel() {
		return deviceLevel;
	}
	public void setDeviceLevel(DeviceLevel deviceLevel) {
		this.deviceLevel = deviceLevel;
	}
	@ManyToOne
	@JoinColumn(name="PRODUCTIONUNIT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProductionUnit getProductionUnit() {
		return productionUnit;
	}
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getActualPower() {
		return actualPower;
	}
	public void setActualPower(String actualPower) {
		this.actualPower = actualPower;
	}
	public String getShapeSize() {
		return shapeSize;
	}
	public void setShapeSize(String shapeSize) {
		this.shapeSize = shapeSize;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getAssetNumber() {
		return assetNumber;
	}
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}
	public Date getInFactoryDate() {
		return inFactoryDate;
	}
	public void setInFactoryDate(Date inFactoryDate) {
		this.inFactoryDate = inFactoryDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
}
