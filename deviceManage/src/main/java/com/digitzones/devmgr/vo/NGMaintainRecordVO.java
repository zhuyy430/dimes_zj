package com.digitzones.devmgr.vo;

import java.io.Serializable;

import com.digitzones.vo.ProjectTypeVO;

/**
 * 故障原因记录
 * @author zhuyy430
 *
 */
public class NGMaintainRecordVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**故障原因id*/
	private ProjectTypeVO projectType;
	/**故障原因*/
	private String reason;
	/**故障原因code*/
	private String code;
	/**说明*/
	private String note;
	/**处理方法*/
	private String processingMethod;
	/**备注*/
	private String remark;
	/**维修单*/
	private Long deviceRepairId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProjectTypeVO getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectTypeVO projectType) {
		this.projectType = projectType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getProcessingMethod() {
		return processingMethod;
	}
	public void setProcessingMethod(String processingMethod) {
		this.processingMethod = processingMethod;
	}
	public Long getDeviceRepairId() {
		return deviceRepairId;
	}
	public void setDeviceRepairId(Long deviceRepairId) {
		this.deviceRepairId = deviceRepairId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
