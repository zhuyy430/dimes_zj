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

import com.digitzones.model.Device;
/**
 * 设备，备品备件关联实体
 * @author zdq
 * 2019年2月27日
 */
@Entity
public class DeviceSparepartMapping implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**设备*/
	@ManyToOne(targetEntity=Device.class)
	@JoinColumn(name="DEVICE_CODE",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT)
	,referencedColumnName="code")
	private Device device;
	/**备品备件*/
	@ManyToOne(targetEntity=Sparepart.class)
	@JoinColumn(name="SPAREPART_CODE",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT),
	referencedColumnName="code")
	private Sparepart sparepart;
	/**最近领用日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUseDate=new Date();
	public Date getLastUseDate() {
		return lastUseDate;
	}
	public void setLastUseDate(Date lastUseDate) {
		this.lastUseDate = lastUseDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public Sparepart getSparepart() {
		return sparepart;
	}
	public void setSparepart(Sparepart sparepart) {
		this.sparepart = sparepart;
	}
}
