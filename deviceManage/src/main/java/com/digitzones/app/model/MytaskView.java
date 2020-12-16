package com.digitzones.app.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="MYTASKVIEW")
public class MytaskView {
	private Long id;
	/**任务类型*/
	private String type;
	/**表*/
	private String tname;
	/**recordId*/
	private Long recordId;
	/**创建日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cdate;
	/**单号*/
	private String no;
	/**责任人姓名*/
	private String principalName;
	/**发起人名字*/
	private String informantName;
	/**发起人代码*/
	private String informantCode;
	/**派单人名字*/
	private String assignName;
	/**派单人代码*/
	private String assignCode;
	/**接收人名字*/
	private String name;
	/**接收人代码*/
	private String code;
	/**状态*/
	private String status;
	/**图片路径*/
	private String picPath;
	/**接收时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date receiptDate;
	/**完成时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completeDate;
	
	@Id
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInformantName() {
		return informantName;
	}
	public void setInformantName(String informantName) {
		this.informantName = informantName;
	}
	public String getInformantCode() {
		return informantCode;
	}
	public void setInformantCode(String informantCode) {
		this.informantCode = informantCode;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public String getAssignName() {
		return assignName;
	}
	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}
	public String getAssignCode() {
		return assignCode;
	}
	public void setAssignCode(String assignCode) {
		this.assignCode = assignCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPrincipalName() {
		return principalName;
	}
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
