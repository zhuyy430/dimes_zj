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
@Entity
@Table(name="NGPROCESSMETHOD")
public class NGProcessMethod {
	private Long id;
	/**处理方法*/
	private String processMethod;
	/**数量*/
	private Integer ngCount = 0;
	/**修改人id*/
	private Long modifyUserId;
	/**修改人姓名*/
	private String modifyUsername;
	/**修改时间*/
	private Date modifyDate;
	/**关联的不合格记录*/
	private NGRecord ngRecord;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProcessMethod() {
		return processMethod;
	}
	public void setProcessMethod(String processMethod) {
		this.processMethod = processMethod;
	}
	public Integer getNgCount() {
		return ngCount;
	}
	public void setNgCount(Integer ngCount) {
		this.ngCount = ngCount;
	}
	public Long getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyUsername() {
		return modifyUsername;
	}
	public void setModifyUsername(String modifyUsername) {
		this.modifyUsername = modifyUsername;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	@ManyToOne
	@JoinColumn(name="NGRECORD_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public NGRecord getNgRecord() {
		return ngRecord;
	}
	public void setNgRecord(NGRecord ngRecord) {
		this.ngRecord = ngRecord;
	}
}
