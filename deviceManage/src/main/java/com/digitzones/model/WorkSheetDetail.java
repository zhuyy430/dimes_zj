package com.digitzones.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 工单 详情
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="WORKSHEETDETAIL")
public class WorkSheetDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**工序id*/
	private Long processId;
	/**工序代码*/
	private String processCode;
	/**工序名称*/
	private String processName;
	/**设备代码*/
	private String deviceCode;
	/**设备名称*/
	private String deviceName;
	/**站点id*/
	private Long deviceSiteId;
	/**站点代码*/
	private String deviceSiteCode;
	/**站点名称*/
	private String deviceSiteName;
	/**完工数量*/
	private Integer completeCount = 0;
	/**合格数量*/
	private Integer qualifiedCount = 0;
	/**不合格数量*/
	private Integer unqualifiedCount = 0;
	/**返修数量*/
	private Integer repairCount = 0;
	/**报废数量*/
	private Integer scrapCount = 0;
	/**参数取值来源*/
	private String parameterSource;
	/**首件报告*/
	private String firstReport;
	/**状态 :0 表示‘计划’、 1 表示‘加工中’、 2 表示‘停工’、 3 表示‘已完工’*/
	private String status="0";
	/**备注*/
	private String note;
	/**所属工单*/
	private WorkSheet workSheet;
	/**完成时间*/
	private Date completeTime;
	/**报工数*/
	private Integer reportCount = 0;
	/**生产数量 修改为计划数量*/
	private Integer productionCount=0;
	/**让步接收数量*/
	private Integer compromiseCount = 0;
	/**删除标识*/
	private Boolean deleted = false;

	/**加工顺序*/
	private Long processRoute;

	public Long getProcessRoute() {
		return processRoute;
	}

	public void setProcessRoute(Long processRoute) {
		this.processRoute = processRoute;
	}

	public Integer getCompromiseCount() {
		return compromiseCount;
	}
	public void setCompromiseCount(Integer compromiseCount) {
		this.compromiseCount = compromiseCount;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	/**工单详情参数记录*/
	private Set<WorkSheetDetailParametersRecord> parameterRecords;
	@OneToMany(mappedBy="workSheetDetail")
	@OrderBy("ID DESC")
	@JsonIgnore
	public Set<WorkSheetDetailParametersRecord> getParameterRecords() {
		return parameterRecords;
	}
	public void setParameterRecords(Set<WorkSheetDetailParametersRecord> parameterRecords) {
		this.parameterRecords = parameterRecords;
	}
	public Long getProcessId() {
		return processId;
	}
	public Integer getProductionCount() {
		return productionCount;
	}
	public void setProductionCount(Integer productionCount) {
		this.productionCount = productionCount;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDeviceSiteId() {
		return deviceSiteId;
	}
	public void setDeviceSiteId(Long deviceSiteId) {
		this.deviceSiteId = deviceSiteId;
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
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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
	public Integer getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(Integer completeCount) {
		this.completeCount = completeCount;
	}
	public Integer getQualifiedCount() {
		return qualifiedCount;
	}
	public void setQualifiedCount(Integer qualifiedCount) {
		this.qualifiedCount = qualifiedCount;
	}
	public Integer getUnqualifiedCount() {
		return unqualifiedCount;
	}
	public void setUnqualifiedCount(Integer unqualifiedCount) {
		this.unqualifiedCount = unqualifiedCount;
	}
	public Integer getRepairCount() {
		return repairCount;
	}
	public void setRepairCount(Integer repairCount) {
		this.repairCount = repairCount;
	}
	public Integer getScrapCount() {
		return scrapCount;
	}
	public void setScrapCount(Integer scrapCount) {
		this.scrapCount = scrapCount;
	}
	public String getParameterSource() {
		return parameterSource;
	}
	public void setParameterSource(String parameterSource) {
		this.parameterSource = parameterSource;
	}
	public String getFirstReport() {
		return firstReport;
	}
	public void setFirstReport(String firstReport) {
		this.firstReport = firstReport;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="WORKSHEET_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public WorkSheet getWorkSheet() {
		return workSheet;
	}
	public void setWorkSheet(WorkSheet workSheet) {
		this.workSheet = workSheet;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public Integer getReportCount() {
		return reportCount;
	}
	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}
}
