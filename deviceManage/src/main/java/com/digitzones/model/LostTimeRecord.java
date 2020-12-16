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
 * 损时记录管理
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="LOSTTIMERECORD")
public class LostTimeRecord {
	private Long id;
	/**损时日期*/
	private Date lostTimeTime = new  Date();
	/**损时类别名称 : 同按灯类别*/
	private String lostTimeTypeName;
	/**损时类别代码 : 同按灯类别代码*/
	private String lostTimeTypeCode;
	/**是否停机*/
	private Boolean halt;
	/**记录人员id*/
	private Long recordUserId;
	/**记录人员名称*/
	private String recordUserName;
	/**确认人员id*/
	private Long confirmUserId;
	/**确认人员名称*/
	private String confirmUserName;
	/**故障原因*/
	private String reason;
	/**按灯代码*/
	private String pressLightCode;
	/**恢复方法*/
	private String recoverMethod;
	/**确认时间*/
	private Date confirmTime;
	/**损时开始时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginTime;
	/**损时结束时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	/**合计,结束时间-开始时间，单位为分钟*/
	private Double sumOfLostTime;
	/**损时日期：所在班次的损时日期*/
	private String forLostTimeRecordDate;
	/**详细描述*/
	private String description;
	/**损时的站点*/
	private DeviceSite deviceSite;
	/**班次代码*/
	private String classesCode;
	/**班次名称*/
	private  String classesName;
	/**删除标识*/
	private Boolean deleted = false;
	/**计划 停机*/
	private Boolean planHalt = false;
	
	public String getForLostTimeRecordDate() {
		return forLostTimeRecordDate;
	}
	public void setForLostTimeRecordDate(String forLostTimeRecordDate) {
		this.forLostTimeRecordDate = forLostTimeRecordDate;
	}
	public Boolean getPlanHalt() {
		return planHalt;
	}
	public void setPlanHalt(Boolean planHalt) {
		this.planHalt = planHalt;
	}
	public LostTimeRecord() {}
	public LostTimeRecord(String lostTimeTypeName,Double sumOfLostTime) {
		this.lostTimeTypeName = lostTimeTypeName;
		this.sumOfLostTime = sumOfLostTime;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
	public Date getLostTimeTime() {
		return lostTimeTime;
	}
	public void setLostTimeTime(Date lostTimeTime) {
		this.lostTimeTime = lostTimeTime;
	}
	public String getLostTimeTypeName() {
		return lostTimeTypeName;
	}
	public void setLostTimeTypeName(String lostTimeTypeName) {
		this.lostTimeTypeName = lostTimeTypeName;
	}
	public String getLostTimeTypeCode() {
		return lostTimeTypeCode;
	}
	public void setLostTimeTypeCode(String lostTimeTypeCode) {
		this.lostTimeTypeCode = lostTimeTypeCode;
	}
	public Boolean getHalt() {
		return halt;
	}
	public void setHalt(Boolean halt) {
		this.halt = halt;
	}
	public Long getRecordUserId() {
		return recordUserId;
	}
	public void setRecordUserId(Long recordUserId) {
		this.recordUserId = recordUserId;
	}
	public String getRecordUserName() {
		return recordUserName;
	}
	public void setRecordUserName(String recordUserName) {
		this.recordUserName = recordUserName;
	}
	public Long getConfirmUserId() {
		return confirmUserId;
	}
	public void setConfirmUserId(Long confirmUserId) {
		this.confirmUserId = confirmUserId;
	}
	public String getConfirmUserName() {
		return confirmUserName;
	}
	public void setConfirmUserName(String confirmUserName) {
		this.confirmUserName = confirmUserName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRecoverMethod() {
		return recoverMethod;
	}
	public void setRecoverMethod(String recoverMethod) {
		this.recoverMethod = recoverMethod;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Double getSumOfLostTime() {
		return sumOfLostTime;
	}
	public void setSumOfLostTime(Double sumOfLostTime) {
		this.sumOfLostTime = sumOfLostTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne
	@JoinColumn(name="DEVICESITE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
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
	public String getPressLightCode() {
		return pressLightCode;
	}
	public void setPressLightCode(String pressLightCode) {
		this.pressLightCode = pressLightCode;
	}
	
}
