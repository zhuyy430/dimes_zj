package com.digitzones.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 站点关联设备历史记录实体
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="EQUIPMENT_DEVICESITE_LOG")
public class EquipmentDeviceSiteMappingLog {
	private Long id;
	/**站点代码*/
	private String deviceSiteCode;
	/**站点名称*/
	private String deviceSiteName;
	/**装备代码*/
	private String quipmentCode;
	/**装备名称*/
	private String quipmentName;
	/**工单号*/
	private String workSheetCode;
	/**关联日期*/
	private Date mappingDate;
	/**解除关联日期*/
	private Date removeMappingDate;
	/**设备序号，具体设备的 编号*/
	private String deviceNo;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	public String getQuipmentCode() {
		return quipmentCode;
	}
	public void setQuipmentCode(String quipmentCode) {
		this.quipmentCode = quipmentCode;
	}
	public String getQuipmentName() {
		return quipmentName;
	}
	public void setQuipmentName(String quipmentName) {
		this.quipmentName = quipmentName;
	}
	public String getWorkSheetCode() {
		return workSheetCode;
	}
	public void setWorkSheetCode(String workSheetCode) {
		this.workSheetCode = workSheetCode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMappingDate() {
		return mappingDate;
	}
	public void setMappingDate(Date mappingDate) {
		this.mappingDate = mappingDate;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRemoveMappingDate() {
		return removeMappingDate;
	}
	public void setRemoveMappingDate(Date removeMappingDate) {
		this.removeMappingDate = removeMappingDate;
	}
	
}
