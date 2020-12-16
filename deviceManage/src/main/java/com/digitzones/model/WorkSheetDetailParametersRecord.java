package com.digitzones.model;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 工单详情参数记录	 
 * @author zdq
 * 2018年6月20日
 */
@Entity
@Table(name="WORKSHEETDETAILPARAMETERSRECORD")
public class WorkSheetDetailParametersRecord implements Cloneable,Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**参数代码*/
	private String parameterCode;
	/**参数名称*/
	private String parameterName;
	/**单位*/
	private String unit;
	/**备注*/
	private String note;
	/**上线*/
	private Float upLine = 0f;
	/**下线*/
	private Float lowLine = 0f;
	/**标准值*/
	private Float standardValue = 0f;
	/**时间*/
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date currentDate;
	/**参数值*/
	private String parameterValue ;
	/**状态*/
	private String status;
	/**状态故障代码*/
	private String statusCode;
	/**删除标识位*/
	private Boolean deleted = false;
	/**工单详情*/
	private WorkSheetDetail workSheetDetail;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getParameterCode() {
		return parameterCode;
	}
	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Float getUpLine() {
		return upLine==null?0:upLine;
	}
	public void setUpLine(Float upLine) {
		this.upLine = upLine;
	}
	public Float getLowLine() {
		return lowLine==null?0:lowLine;
	}
	public void setLowLine(Float lowLine) {
		this.lowLine = lowLine;
	}
	public Float getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Float standardValue) {
		this.standardValue = standardValue;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToOne
	@JoinColumn(name="WORKSHEETDETAIL_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public WorkSheetDetail getWorkSheetDetail() {
		return workSheetDetail;
	}
	public void setWorkSheetDetail(WorkSheetDetail workSheetDetail) {
		this.workSheetDetail = workSheetDetail;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
