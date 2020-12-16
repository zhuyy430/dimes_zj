package com.digitzones.app.vo;

import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.model.Device;

import java.io.Serializable;

public class DeviceSparepartMappingVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**设备*/
	private Device device;
	/**备品备件*/
	private Sparepart sparepart;
	/**最近领用日期*/
	private String lastUseDate;
	
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
	public String getLastUseDate() {
		return lastUseDate;
	}
	public void setLastUseDate(String lastUseDate) {
		this.lastUseDate = lastUseDate;
	}
}
