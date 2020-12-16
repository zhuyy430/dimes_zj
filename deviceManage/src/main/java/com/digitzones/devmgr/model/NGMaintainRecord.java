package com.digitzones.devmgr.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 故障原因记录
 * @author zhuyy430
 *
 */
@Entity
@Table(name="NGMAINTAINRECORD")
public class NGMaintainRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**故障原因*/
	private DeviceProject deviceProject;
	/**说明*/
	private String note;
	/**处理方法*/
	private String processingMethod;
	/**备注*/
	private String remark;
	/**是否可删除*/
	private boolean status;
	/**维修单*/
	private DeviceRepair deviceRepair;
	/**创建时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getProcessingMethod() {
		return processingMethod;
	}
	public void setProcessingMethod(String processingMethod) {
		this.processingMethod = processingMethod;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@ManyToOne
	@JoinColumn(name="DEVICEREPAIR_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceRepair getDeviceRepair() {
		return deviceRepair;
	}
	public void setDeviceRepair(DeviceRepair deviceRepair) {
		this.deviceRepair = deviceRepair;
	}
	@ManyToOne
	@JoinColumn(name="DEVICEPROJECT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceProject getDeviceProject() {
		return deviceProject;
	}
	public void setDeviceProject(DeviceProject deviceProject) {
		this.deviceProject = deviceProject;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
