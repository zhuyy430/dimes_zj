package com.digitzones.devmgr.model;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.digitzones.model.Device;
import com.digitzones.model.DispatchedLevel;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.ProjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 设备报修单
 * @author zhuyy430
 */
@Entity
@Table(name="DEVICEREPAIR")
public class DeviceRepair implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**单据编号*/
	private String serialNumber;
	/**设备*/
	private Device device;
	/**报修人员*/
	private String Informant;
	/**报修人员*/
	private String InformantCode;
	/**报修时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**确认时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date completeDate;
	/**提交确认时间，即维修完成时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date submitConfirmDate;
	/**生产单元*/
	private ProductionUnit productionUnit;
	/**故障描述*/
	private String ngDescription;
	/**设备类别*/
	private ProjectType deviceType;
	/**故障类别*/
	private DeviceProject ngreason;
	/**维修人员*/
	private String maintainName;
	/**维修人员*/
	private String maintainCode;
	/**确认人编码*/
	private String confirmCode;
	/**确认人名称*/
	private String confirmName;
	/**维修状态*/
	private String status;
	/**图片名称*/
	private List<String> picName = new ArrayList<>();
	/**图片信息*/
	private List<Blob> pic;
	/**推送状态*/
	private Boolean pushStatus = false;
	/**是否为带病运行  带病运行为true 正常为false */
	private Boolean failSafeOperation =false;
	/**是否关闭，带病运行状态默认为false，正常确认状态为true */
	private Boolean isClosed = true;

	public Boolean getClosed() {
		return isClosed;
	}

	public void setClosed(Boolean closed) {
		isClosed = closed;
	}

	/**派单等级*/
	private DispatchedLevel dispatchedLevel;
	/**原带病维修单*/
	private String originalFailSafeOperationNo;
	/**返修单号*/
	private String reworkNo;
	
	public String getOriginalFailSafeOperationNo() {
		return originalFailSafeOperationNo;
	}
	public void setOriginalFailSafeOperationNo(String originalFailSafeOperationNo) {
		this.originalFailSafeOperationNo = originalFailSafeOperationNo;
	}
	public String getReworkNo() {
		return reworkNo;
	}
	public void setReworkNo(String reworkNo) {
		this.reworkNo = reworkNo;
	}
	@Column(name="completeDate")
	public Date getSubmitConfirmDate() {
		return submitConfirmDate;
	}
	public void setSubmitConfirmDate(Date submitConfirmDate) {
		this.submitConfirmDate = submitConfirmDate;
	}

	public Boolean getFailSafeOperation() {
		return failSafeOperation;
	}
	public void setFailSafeOperation(Boolean failSafeOperation) {
		this.failSafeOperation = failSafeOperation;
	}
	@ManyToOne
	@JoinColumn(name="DISPATCHEDLEVEL_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DispatchedLevel getDispatchedLevel() {
		return dispatchedLevel;
	}
	public void setDispatchedLevel(DispatchedLevel dispatchedLevel) {
		this.dispatchedLevel = dispatchedLevel;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	@ManyToOne
	@JoinColumn(name="DEVICE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public String getInformant() {
		return Informant;
	}
	public void setInformant(String informant) {
		Informant = informant;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@ManyToOne
	@JoinColumn(name="PRODUCTIONUNIT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProductionUnit getProductionUnit() {
		return productionUnit;
	}
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}
	public String getNgDescription() {
		return ngDescription;
	}
	public void setNgDescription(String ngDescription) {
		this.ngDescription = ngDescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMaintainName() {
		return maintainName;
	}
	public void setMaintainName(String maintainName) {
		this.maintainName = maintainName;
	}
	@ManyToOne
	@JoinColumn(name="DEVICETYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProjectType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(ProjectType deviceType) {
		this.deviceType = deviceType;
	}
	@ManyToOne
	@JoinColumn(name="NGREASON_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceProject getNgreason() {
		return ngreason;
	}
	public void setNgreason(DeviceProject ngreason) {
		this.ngreason = ngreason;
	}
	
	@Transient
	public List<String> getPicName() {
		return picName;
	}
	public void setPicName(List<String> picName) {
		this.picName = picName;
	}
	@JsonIgnore
	@Transient
	public List<Blob> getPic() {
		return pic;
	}
	public void setPic(List<Blob> pic) {
		this.pic = pic;
	}
	@Column(name="confirmDate")
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public String getMaintainCode() {
		return maintainCode;
	}
	public void setMaintainCode(String maintainCode) {
		this.maintainCode = maintainCode;
	}
	public String getInformantCode() {
		return InformantCode;
	}
	public void setInformantCode(String informantCode) {
		InformantCode = informantCode;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	public Boolean getPushStatus() {
		return pushStatus;
	}
	public void setPushStatus(Boolean pushStatus) {
		this.pushStatus = pushStatus;
	}
}
