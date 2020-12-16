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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * 业务流程中任务节点
 * @author zdq
 * 2019年1月8日
 */
@Entity
public class WorkflowTask implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**任务名称*/
	private String name;
	/**创建时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	/**任务拥有者*/
	private String ownner;
	/**任务指派人：任务指派给了谁*/
	private String assignee;
	/**任务指派的角色,多个角色，逗号间隔*/
	private String assignRole;
	/**任务描述*/
	private String description;
	/**任务状态:
	 * 0:发起
	 * 1：完成
	 * 2：驳回*/
	private int status;
	/**业务关键字*/
	private String businessKey;
	/**所属流程*/
	@ManyToOne
	@JoinColumn(name="WORKFLOWRECORD_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private WorkflowRecord workflowRecord;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getOwnner() {
		return ownner;
	}
	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getAssignRole() {
		return assignRole;
	}
	public void setAssignRole(String assignRole) {
		this.assignRole = assignRole;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public WorkflowRecord getWorkflowRecord() {
		return workflowRecord;
	}
	public void setWorkflowRecord(WorkflowRecord workflowRecord) {
		this.workflowRecord = workflowRecord;
	}
}
