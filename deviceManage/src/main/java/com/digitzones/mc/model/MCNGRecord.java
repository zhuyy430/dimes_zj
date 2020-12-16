package com.digitzones.mc.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 不合格品记录MC端显示用
 * @author zdq
 * 2018年7月8日
 */
@Entity
public class MCNGRecord {
	private Long id;
	/**录入时间*/
	private Date inputDate;
	/**产生不良产品时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date occurDate;
	/**站点id*/
	private Long deviceSiteId;
	/**站点代码*/
	private String deviceSiteCode;
	/**站点名称*/
	private String deviceSiteName;
	/**数量*/
	private int ngCount = 0;
	/**NG原因ID*/
	private Long ngReasonId;
	/**NG原因代码*/
	private String ngReasonCode;
	/**NG原因*/
	private String ngReason;
	/**处理方法*/
	private String processingMethod;
	/**录入人ID*/
	private Long inputUserId;
	/**录入人姓名*/
	private String inputUsername;
	/**复核人ID*/
	private Long reviewerId;
	/**复核人姓名*/
	private String reviewerName;
	/**复核时间*/
	private Date reviewDate;
	/**审核人ID*/
	private Long auditorId;
	/**审核人姓名*/
	private String auditorName;
	/**审核时间*/
	private Date auditDate;
	/**确认人ID*/
	private Long confirmUserId;
	/**确认人姓名*/
	private String confirmUsername;
	/**确认时间*/
	private Date confirmDate;
	/**工序id*/
	private Long processId;
	/**工序代码*/
	private String processCode;
	/**工序名称*/
	private String processName;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNgReasonCode() {
		return ngReasonCode;
	}
	public void setNgReasonCode(String ngReasonCode) {
		this.ngReasonCode = ngReasonCode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public Long getDeviceSiteId() {
		return deviceSiteId;
	}
	public void setDeviceSiteId(Long deviceSiteId) {
		this.deviceSiteId = deviceSiteId;
	}
	public String getDeviceSiteCode() {
		return deviceSiteCode;
	}
	public void setDeviceSiteCode(String deviceSiteCode) {
		this.deviceSiteCode = deviceSiteCode;
	}
	public String getDeviceSiteName() {
		return deviceSiteName;
	}
	public void setDeviceSiteName(String deviceSiteName) {
		this.deviceSiteName = deviceSiteName;
	}
	public int getNgCount() {
		return ngCount;
	}
	public void setNgCount(int ngCount) {
		this.ngCount = ngCount;
	}
	public Long getNgReasonId() {
		return ngReasonId;
	}
	public void setNgReasonId(Long ngReasonId) {
		this.ngReasonId = ngReasonId;
	}
	public String getNgReason() {
		return ngReason;
	}
	public void setNgReason(String ngReason) {
		this.ngReason = ngReason;
	}
	public String getProcessingMethod() {
		return processingMethod;
	}
	public void setProcessingMethod(String processingMethod) {
		this.processingMethod = processingMethod;
	}
	public Long getInputUserId() {
		return inputUserId;
	}
	public void setInputUserId(Long inputUserId) {
		this.inputUserId = inputUserId;
	}
	public String getInputUsername() {
		return inputUsername;
	}
	public void setInputUsername(String inputUsername) {
		this.inputUsername = inputUsername;
	}
	public Long getReviewerId() {
		return reviewerId;
	}
	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public Long getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public Long getConfirmUserId() {
		return confirmUserId;
	}
	public void setConfirmUserId(Long confirmUserId) {
		this.confirmUserId = confirmUserId;
	}
	public String getConfirmUsername() {
		return confirmUsername;
	}
	public void setConfirmUsername(String confirmUsername) {
		this.confirmUsername = confirmUsername;
	}
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}
}
