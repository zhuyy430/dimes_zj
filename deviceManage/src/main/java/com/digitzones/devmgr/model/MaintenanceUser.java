package com.digitzones.devmgr.model;
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
 * 设备保养人员
 * @author zdq
 * 2019年1月5日
 */
@Entity
public class MaintenanceUser implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**员工代码*/
	private String code;
	/**员工姓名*/
	private String name;
	/**接单类型：人工派单，协助等*/
	private String orderType;
	/**派单时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dispatchDate;
	/**派单人代码*/
	private String dispatchUsercode;
	/**派单人名称*/
	private String dispatchUsername;
	/**接单时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date receiptDate;
	/**完成时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completeDate;
	/**保养用时：完成时间-接单时间 ，单位：分*/
	private Double maintenanceTime;
	/**占用工时*/
	private Double occupyTime;
	public Double getMaintenanceTime() {
		return maintenanceTime;
	}
	public void setMaintenanceTime(Double maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
		if(this.occupyTime==null || this.occupyTime==0) {
			this.occupyTime = this.maintenanceTime;
		}
	}
	public Double getOccupyTime() {
		return occupyTime;
	}
	public void setOccupyTime(Double occupyTime) {
		this.occupyTime = occupyTime;
	}
	/**保养计划记录*/
	@ManyToOne
	@JoinColumn(name="MAINTENANCEPLANRECORD_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private MaintenancePlanRecord maintenancePlanRecord;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Date getDispatchDate() {
		return dispatchDate;
	}
	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
	public String getDispatchUsercode() {
		return dispatchUsercode;
	}
	public void setDispatchUsercode(String dispatchUsercode) {
		this.dispatchUsercode = dispatchUsercode;
	}
	public String getDispatchUsername() {
		return dispatchUsername;
	}
	public void setDispatchUsername(String dispatchUsername) {
		this.dispatchUsername = dispatchUsername;
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
	public MaintenancePlanRecord getMaintenancePlanRecord() {
		return maintenancePlanRecord;
	}
	public void setMaintenancePlanRecord(MaintenancePlanRecord maintenancePlanRecord) {
		this.maintenancePlanRecord = maintenancePlanRecord;
	}
}
