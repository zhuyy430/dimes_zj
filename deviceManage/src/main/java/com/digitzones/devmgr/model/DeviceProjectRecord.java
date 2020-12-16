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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.digitzones.model.Device;
/**
 * 设备各个项目的标准值
 * 设备项目记录实体
 * @author Administrator
 */
@Entity
@Table(name="DEVICEPROJECTRECORD")
public class DeviceProjectRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	/**项目编号*/
	private String code;
	/**记录所属类别编码：如果为保养项目，则该值为保养类别编码*/
	private String recordTypeCode;
	/**记录所属类别名称*/
	private String recordTypeName;
	/**项目名称*/
	private String name;
	/**标准*/
	private String standard;
	/**方法*/
	private String method;
	/**频次*/
	private String frequency;
	/**说明*/
	private String note;
	/**上限值*/
	private String upperLimit;
	/**下限值*/
	private String lowerLimit;
	/**设备类别*/
	private Device device;
	/**项目类型：
	 * SPOTINSPECTION:点检
	 * LUBRICATION：润滑
	 * MAINTAIN:保养
	 * 
	 * 字段值参照Constant.DeviceProject中的常量
	 * */
	private String type;
	/**所属班次代码*/
	private String classesCode;
	/**所属班次名称*/
	private String classesName;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEVICE_ID",foreignKey=@ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRecordTypeCode() {
		return recordTypeCode;
	}
	public void setRecordTypeCode(String recordTypeCode) {
		this.recordTypeCode = recordTypeCode;
	}
	public String getRecordTypeName() {
		return recordTypeName;
	}
	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}
	public String getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}
	public String getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public String getClassesCode() {
		return classesCode;
	}
	public void setClassesCode(String classesCode) {
		this.classesCode = classesCode;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
}
