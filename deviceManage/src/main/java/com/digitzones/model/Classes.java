package com.digitzones.model;

import java.util.Date;

import javax.persistence.Column;
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

/**
 * 班次
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="CLASSES")
public class Classes {
	/**开始时间*/
	@DateTimeFormat(pattern="HH:mm")
	private Date startTime;
	/**结束时间*/
	@DateTimeFormat(pattern="HH:mm")
	private Date endTime;
	/**是否跨天*/
	private Boolean crossDay;
	/**班次类别代码*/
	private String classTypeCode;
	/**班次类别名称*/
	private String classTypeName;
	/**班次类别*/
	private ClassType classType;
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
	public Classes() {
		startTime = new Date();
		endTime = new Date();
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CLASSTYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ClassType getClassType() {
		return classType;
	}
	public void setClassType(ClassType classType) {
		this.classType = classType;
	}
	
	public String getClassTypeCode() {
		return classTypeCode;
	}
	public String getClassTypeName() {
		return classTypeName;
	}
	public void setClassTypeCode(String classTypeCode) {
		this.classTypeCode = classTypeCode;
	}
	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="beginTime")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Boolean getCrossDay() {
		return crossDay;
	}
	public void setCrossDay(Boolean crossDay) {
		this.crossDay = crossDay;
	}
}
