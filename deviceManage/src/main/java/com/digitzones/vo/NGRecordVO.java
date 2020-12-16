package com.digitzones.vo;
import java.util.Date;
/**
 * 不合格品记录
 * @author zdq
 * 2018年7月8日
 */
public class NGRecordVO {
	private Long id;
	/**录入时间*/
	private Date inputDate;
	/**产生不良产品时间*/
	private String occurDate;
	/**工单id*/
	private Long workSheetId;
	/**工单号*/
	private String no;
	/**站点id*/
	private Long deviceSiteId;
	/**站点代码*/
	private String deviceSiteCode;
	/**站点名称*/
	private String deviceSiteName;
	/**工件id,没有维护*/
	private String workpieceId;
	/**工件代码*/
	private String workpieceCode;
	/**工件名称*/
	private String workpieceName;
	/**工件规格型号*/
	private String unitType;
	/**客户图号*/
	private String customerGraphNumber;
	/**图号*/
	private String graphNumber;
	/**版本号*/
	private String version;
	/**数量*/
	private int ngCount;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String stoveNumber;
	/**ng分类id*/
	private Long ngTypeId; 
	/**ng分类名称*/
	private String ngTypeName;
	/**NG原因ID*/
	private Long ngReasonId;
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
	/**复核意见*/
	private String reviewSuggestion;
	/**复核时间*/
	private Date reviewDate;
	/**审核人ID*/
	private Long auditorId;
	/**审核人姓名*/
	private String auditorName;
	/**审核意见*/
	private String auditSuggestion;
	/**审核时间*/
	private Date auditDate;
	/**确认人ID*/
	private Long confirmUserId;
	/**确认人姓名*/
	private String confirmUsername;
	/**确认意见*/
	private String confirmSuggestion;
	/**确认时间*/
	private Date confirmDate;
	/**工序id*/
	private Long processId;
	/**工序代码*/
	private String processCode;
	/**工序名称*/
	private String processName;
	/**删除标识*/
	private Boolean deleted = false;
 	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public Long getWorkSheetId() {
		return workSheetId;
	}
	public void setWorkSheetId(Long workSheetId) {
		this.workSheetId = workSheetId;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
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
	public String getWorkpieceId() {
		return workpieceId;
	}
	public void setWorkpieceId(String workpieceId) {
		this.workpieceId = workpieceId;
	}
	public String getWorkpieceCode() {
		return workpieceCode;
	}
	public void setWorkpieceCode(String workpieceCode) {
		this.workpieceCode = workpieceCode;
	}
	public String getWorkpieceName() {
		return workpieceName;
	}
	public void setWorkpieceName(String workpieceName) {
		this.workpieceName = workpieceName;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getCustomerGraphNumber() {
		return customerGraphNumber;
	}
	public void setCustomerGraphNumber(String customerGraphNumber) {
		this.customerGraphNumber = customerGraphNumber;
	}
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getNgCount() {
		return ngCount;
	}
	public void setNgCount(int ngCount) {
		this.ngCount = ngCount;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getStoveNumber() {
		return stoveNumber;
	}
	public void setStoveNumber(String stoveNumber) {
		this.stoveNumber = stoveNumber;
	}
	public Long getNgTypeId() {
		return ngTypeId;
	}
	public void setNgTypeId(Long ngTypeId) {
		this.ngTypeId = ngTypeId;
	}
	public String getNgTypeName() {
		return ngTypeName;
	}
	public void setNgTypeName(String ngTypeName) {
		this.ngTypeName = ngTypeName;
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
	public String getReviewSuggestion() {
		return reviewSuggestion;
	}
	public void setReviewSuggestion(String reviewSuggestion) {
		this.reviewSuggestion = reviewSuggestion;
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
	public String getAuditSuggestion() {
		return auditSuggestion;
	}
	public void setAuditSuggestion(String auditSuggestion) {
		this.auditSuggestion = auditSuggestion;
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
	public String getConfirmSuggestion() {
		return confirmSuggestion;
	}
	public void setConfirmSuggestion(String confirmSuggestion) {
		this.confirmSuggestion = confirmSuggestion;
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
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Date getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
}
