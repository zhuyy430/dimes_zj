package com.digitzones.model;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * 反向追溯
 * @author zhuyy430
 *
 */
@Entity
public class TraceReverse {
	private Long id;
	/**设备站点代码*/
	private String deviceSiteCode;
	/**设备站点名称*/
	private String deviceSiteName;
	/**工序代码*/
	private String processCode;
	/**工序名称*/
	private String processName;
	/**参数代码*/
	private String parameterCode;
	/**参数名称*/
	private String parameterName;
	/**控制线UL*/
	private Float upLine;
	/**控制线LL*/
	private Float lowLine;
	/**标准值*/
	private String standardValue;
	/**参数值*/
	private String parameterValue;
	/**状态*/
	private String status;
	/**状态/故障代码*/
	private String statusCode;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String stoveNumber;
	/**采集时间*/
	private String collectionDate;
	public String getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Float getUpLine() {
		return upLine;
	}
	public void setUpLine(Float upLine) {
		this.upLine = upLine;
	}
	public Float getLowLine() {
		return lowLine;
	}
	public void setLowLine(Float lowLine) {
		this.lowLine = lowLine;
	}
	public String getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
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
}
