package com.digitzones.devmgr.model;

import java.io.Serializable;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
/**
 * 设备和保养计划的中间实体
 * @author zdq
 * 2018年12月18日
 */
import javax.persistence.ManyToOne;
import com.digitzones.model.Device;
@Entity
public class DeviceMaintenancePlanMapping implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**设备*/
	@ManyToOne
	@JoinColumn(name="DEVICE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private Device device;
	/**设备保养计划*/
	@ManyToOne
	@JoinColumn(name="MAINTENANCEPLAN_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private MaintenancePlan maintenancePlan;
	public MaintenancePlan getMaintenancePlan() {
		return maintenancePlan;
	}
	public void setMaintenancePlan(MaintenancePlan maintenancePlan) {
		this.maintenancePlan = maintenancePlan;
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
}
