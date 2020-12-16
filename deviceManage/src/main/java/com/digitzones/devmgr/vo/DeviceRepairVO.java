package com.digitzones.devmgr.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.digitzones.model.DispatchedLevel;

/**
 * 设备报修单VO
 * @author zhuyy430
 *
 */
public class DeviceRepairVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**单据编号*/
	private String serialNumber;
	/**设备id*/
	private Long deviceId;
	/**设备code*/
	private String deviceCode;
	/**设备name*/
	private String deviceName;
	/**设备规格型号*/
	private String deviceUnitType;
	/**设备类别id*/
	private Long deviceTypeId;
	/**设备类别code*/
	private String deviceTypeCode;
	/**设备类别name*/
	private String deviceTypeName;
	/**设备安装位置*/
	private String deviceInstallPosition;
	/**报修人员*/
	private String Informant;
	/**报修人员Code*/
	private String InformantCode;
	/**维修人员*/
	private String maintainName;
	/**维修人员Code*/
	private String maintainCode;
	/**确认人员*/
	private String confirmName;
	/**确认人员Code*/
	private String confirmCode;
	/**报修时间*/
	private String createDate;
	/**生产单元Id*/
	private Long productionUnitId;
	/**生产单元code*/
	private String productionUnitCode;
	/**生产单元name*/
	private String productionUnitName;
	/**故障描述*/
	private String ngDescription;
	/**故障类别Id*/
	private Long NGId;
	/**故障类别code*/
	private String NGCode;
	/**故障类别name*/
	private String NGName;
	/**同NGId*/
	private Long pressLightId;
	/**同NGName*/
	private String pressLight;
	/**维修状态*/
	private String status;
	/**图片名称*/
	private List<String> picName = new ArrayList<>();
	/**是否为带病运行  带病运行为true 正常为false */
	private Boolean failSafeOperation;
	/**是否关闭，带病运行状态默认为false，正常确认状态为true */
	private Boolean isClosed;
	/**派单等级*/
	private DispatchedLevel dispatchedLevel;
	
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public Long getPressLightId() {
		return pressLightId;
	}
	public void setPressLightId(Long pressLightId) {
		this.pressLightId = pressLightId;
	}
	public String getPressLight() {
		return pressLight;
	}
	public void setPressLight(String pressLight) {
		this.pressLight = pressLight;
	}
	public Boolean getFailSafeOperation() {
		return failSafeOperation;
	}
	public void setFailSafeOperation(Boolean failSafeOperation) {
		this.failSafeOperation = failSafeOperation;
	}
	public Boolean isClosed() {
		return isClosed;
	}
	public void setClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}
	public DispatchedLevel getDispatchedLevel() {
		return dispatchedLevel;
	}
	public void setDispatchedLevel(DispatchedLevel dispatchedLevel) {
		this.dispatchedLevel = dispatchedLevel;
	}
	public List<String> getPicName() {
		return picName;
	}
	public void setPicName(List<String> picName) {
		this.picName = picName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
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
	public String getDeviceUnitType() {
		return deviceUnitType;
	}
	public void setDeviceUnitType(String deviceUnitType) {
		this.deviceUnitType = deviceUnitType;
	}
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public String getDeviceInstallPosition() {
		return deviceInstallPosition;
	}
	public void setDeviceInstallPosition(String deviceInstallPosition) {
		this.deviceInstallPosition = deviceInstallPosition;
	}
	public String getInformant() {
		return Informant;
	}
	public void setInformant(String informant) {
		Informant = informant;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getProductionUnitId() {
		return productionUnitId;
	}
	public void setProductionUnitId(Long productionUnitId) {
		this.productionUnitId = productionUnitId;
	}
	public String getProductionUnitName() {
		return productionUnitName;
	}
	public void setProductionUnitName(String productionUnitName) {
		this.productionUnitName = productionUnitName;
	}
	public String getNgDescription() {
		return ngDescription;
	}
	public void setNgDescription(String ngDescription) {
		this.ngDescription = ngDescription;
	}
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	public String getProductionUnitCode() {
		return productionUnitCode;
	}
	public void setProductionUnitCode(String productionUnitCode) {
		this.productionUnitCode = productionUnitCode;
	}
	public Long getNGId() {
		return NGId;
	}
	public void setNGId(Long nGId) {
		NGId = nGId;
	}
	public String getNGCode() {
		return NGCode;
	}
	public void setNGCode(String nGCode) {
		NGCode = nGCode;
	}
	public String getNGName() {
		return NGName;
	}
	public void setNGName(String nGName) {
		NGName = nGName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInformantCode() {
		return InformantCode;
	}
	public void setInformantCode(String informantCode) {
		InformantCode = informantCode;
	}
	public String getMaintainName() {
		return maintainName;
	}
	public void setMaintainName(String maintainName) {
		this.maintainName = maintainName;
	}
	public String getMaintainCode() {
		return maintainCode;
	}
	public void setMaintainCode(String maintainCode) {
		this.maintainCode = maintainCode;
	}
}
