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

import com.digitzones.model.Classes;
import com.digitzones.model.Device;

/**
 * 设备运行记录
 * @author zhuyy430
 *
 */
@Entity
@Table(name="DEVICEOPERATIONALRECORD")
public class DeviceOperationalRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**设备*/
	private Device device;
	/**班次*/
	private Classes classes;
	/**累计开机时间*/
	private double sumTime;
	/**运行时间*/
	private double runTime;
	/**故障停机时间*/
	private double ngTime;
	/**填报人*/
	private String Informant;
	/**日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date date;
	/**填报时间*/
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
	@JoinColumn(name="DEVICE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	@ManyToOne
	@JoinColumn(name="CLASSES_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	public double getSumTime() {
		return sumTime;
	}
	public void setSumTime(double sumTime) {
		this.sumTime = sumTime;
	}
	public double getRunTime() {
		return runTime;
	}
	public void setRunTime(double runTime) {
		this.runTime = runTime;
	}
	public double getNgTime() {
		return ngTime;
	}
	public void setNgTime(double ngTime) {
		this.ngTime = ngTime;
	}
	public String getInformant() {
		return Informant;
	}
	public void setInformant(String informant) {
		Informant = informant;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
