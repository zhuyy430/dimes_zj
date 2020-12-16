package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.digitzones.model.ProjectType;

/**
 * 维修项目
 * @author zhuyy430
 *
 */
@Entity
@Table(name="MAINTAINPROJECT")
public class MaintainProject implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**故障原因*/
	private ProjectType type;
	/**项目代码*/
	private String code;
	/**说明*/
	private String name;
	/**说明*/
	private String note;
	/**处理方法*/
	private String processingMethod;
	/**备注*/
	private String remark;
	/**维修单*/
	private DeviceRepair deviceRepair;
	/**创建时间*/
	private Date createDate;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProjectType getType() {
		return type;
	}
	public void setType(ProjectType type) {
		this.type = type;
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
	public String getProcessingMethod() {
		return processingMethod;
	}
	public void setProcessingMethod(String processingMethod) {
		this.processingMethod = processingMethod;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
}
