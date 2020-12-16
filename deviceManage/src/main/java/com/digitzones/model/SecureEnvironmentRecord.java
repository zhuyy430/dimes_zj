package com.digitzones.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * 安环记录
 * @author zdq
 * 2018年6月5日
 */
@Entity
@Table(name="SECUREENVIRONMENTRECORD")
public class SecureEnvironmentRecord {
	private Long id;
	/**当前日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date currentDate;
	/**安环类别id*/
	private Long typeId;
	/**安环类别名称*/
	private String typeName;
	/**描述*/
	private String description;
	/**安环等级*/
	private SecureEnvironmentGrade grade;
	@ManyToOne
	@JoinColumn(name="GRADE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	public SecureEnvironmentGrade getGrade() {
		return grade;
	}
	public void setGrade(SecureEnvironmentGrade grade) {
		this.grade = grade;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
