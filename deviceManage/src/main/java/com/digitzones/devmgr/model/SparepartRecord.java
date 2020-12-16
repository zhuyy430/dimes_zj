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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 备品记录
 * @author zhuyy430
 *
 */
@Entity
@Table(name="SPAREPARTRECORD")
public class SparepartRecord implements Serializable {
	private static final long serialVersionUID = -4711833140793307948L;
	private Long id;
	/**备件*/
	private Sparepart sparepart;
	/**数量*/
	private Long quantity;
	/**维修单*/
	private DeviceRepair deviceRepair;
	/**备注*/
	private String note;
	/**创建日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="SPAREPART_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Sparepart getSparepart() {
		return sparepart;
	}
	public void setSparepart(Sparepart sparepart) {
		this.sparepart = sparepart;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	@ManyToOne
	@JoinColumn(name="DEVICEREPAIR_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceRepair getDeviceRepair() {
		return deviceRepair;
	}
	public void setDeviceRepair(DeviceRepair deviceRepair) {
		this.deviceRepair = deviceRepair;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
