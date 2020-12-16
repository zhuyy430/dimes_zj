package com.digitzones.vo;

import com.digitzones.model.CommonModel;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.ProjectType;

/**
 * 设备VO
 * @author zdq
 * 2018年6月3日
 */
public class DeviceVO extends CommonModel {
	private static final long serialVersionUID = 1L;
	/**规格型号*/
	private String unitType;
	/**设备状态*/
	private String status;
	/**制造商*/
	private String manufacturer;
	/**经销商*/
	private String trader;
	/**安装日期*/
	private String installDate;
	/**资产编号*/
	private String assetNumber;
	/**设备重量*/
	private String weight;
	/**外形尺寸*/
	private String shapeSize;
	/**单台功率*/
	private String power;
	/**实际功率*/
	private String actualPower;
	/**出厂日期*/
	private String outFactoryDate;
	/**到厂日期*/
	private String inFactoryDate;
	/**验收日期*/
	private String checkDate;
	/**出厂编号*/
	private String outFactoryCode;
	/**安装位置*/
	private String installPosition;
	/**目标OEE*/
	private Float goalOee;
	/**参数取值
	 * 固定值
	 * 变动值
	 * */
	private String parameterValueType;
	/**设备类别*/
	private ProjectType projectType;
	/**设备类别id*/
	private Long projectTypeId;
	/**设备等级id*/
	private Long deviceLevelId;
	/**设备等级名称*/
	private String deviceLevelName;
	/**设备等级编码*/
	private String deviceLevelCode;
	/**设备等级权重*/
	private int deviceLevelWeight;
	/**生产单元*/
	private ProductionUnit productionUnit;
	/**二维码图片路径*/
	private String qrPath;
	/**图片名称*/
	private String photoName;
	public Long getDeviceLevelId() {
		return deviceLevelId;
	}
	public void setDeviceLevelId(Long deviceLevelId) {
		this.deviceLevelId = deviceLevelId;
	}
	public String getDeviceLevelName() {
		return deviceLevelName;
	}
	public void setDeviceLevelName(String deviceLevelName) {
		this.deviceLevelName = deviceLevelName;
	}
	public String getDeviceLevelCode() {
		return deviceLevelCode;
	}
	public void setDeviceLevelCode(String deviceLevelCode) {
		this.deviceLevelCode = deviceLevelCode;
	}
	public int getDeviceLevelWeight() {
		return deviceLevelWeight;
	}
	public void setDeviceLevelWeight(int deviceLevelWeight) {
		this.deviceLevelWeight = deviceLevelWeight;
	}
	public ProjectType getProjectType() {
		return projectType;
	}
	public Float getGoalOee() {
		return goalOee;
	}
	public String getInstallDate() {
		return installDate;
	}
	public String getInstallPosition() {
		return installPosition;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public String getOutFactoryCode() {
		return outFactoryCode;
	}
	public String getOutFactoryDate() {
		return outFactoryDate;
	}
	public String getParameterValueType() {
		return parameterValueType;
	}
	public String getPhotoName() {
		return photoName;
	}
	public ProductionUnit getProductionUnit() {
		return productionUnit;
	}
	public String getQrPath() {
		return qrPath;
	}
	public String getStatus() {
		return status;
	}
	public String getTrader() {
		return trader;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
	public void setGoalOee(Float goalOee) {
		this.goalOee = goalOee;
	}
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public void setOutFactoryCode(String outFactoryCode) {
		this.outFactoryCode = outFactoryCode;
	}
	public void setOutFactoryDate(String outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}
	public void setParameterValueType(String parameterValueType) {
		this.parameterValueType = parameterValueType;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}
	public void setQrPath(String qrPath) {
		this.qrPath = qrPath;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setTrader(String trader) {
		this.trader = trader;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}public String getInFactoryDate() {
		return inFactoryDate;
	}
	public void setInFactoryDate(String inFactoryDate) {
		this.inFactoryDate = inFactoryDate;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getAssetNumber() {
		return assetNumber;
	}
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getShapeSize() {
		return shapeSize;
	}
	public void setShapeSize(String shapeSize) {
		this.shapeSize = shapeSize;
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
	public Long getProjectTypeId() {
		return projectTypeId;
	}
	public void setProjectTypeId(Long id) {
		this.projectTypeId = id;
	}
	
	
}
