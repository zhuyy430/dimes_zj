package com.digitzones.devmgr.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**
 * 转单实体类
 * @author zhuyy430
 *
 */
@Entity
public class TransferRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**转单时间*/
	private Date  transferDate;
	/**转单员工代码*/
	private  String transferCode;
	/**转单员工名称*/
	private  String transferName;
	/**转单原因*/
	private String reason;
	/**新接单员工代码*/
	private  String acceptCode;
	/**新接单员工名称*/
	private  String acceptName;
	/**转单状态*/
	private  Boolean status=false;
	/**维修单*/
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEVICEREPAIR_ID",foreignKey=@ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))	
	private  DeviceRepair deviceRepair;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public String getTransferCode() {
		return transferCode;
	}
	public void setTransferCode(String transferCode) {
		this.transferCode = transferCode;
	}
	public String getTransferName() {
		return transferName;
	}
	public void setTransferName(String transferName) {
		this.transferName = transferName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAcceptCode() {
		return acceptCode;
	}
	public void setAcceptCode(String acceptCode) {
		this.acceptCode = acceptCode;
	}
	public String getAcceptName() {
		return acceptName;
	}
	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	public DeviceRepair getDeviceRepair() {
		return deviceRepair;
	}
	public void setDeviceRepair(DeviceRepair deviceRepair) {
		this.deviceRepair = deviceRepair;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
