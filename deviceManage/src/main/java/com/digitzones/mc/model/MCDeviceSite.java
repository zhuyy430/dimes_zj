package com.digitzones.mc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * MC端配置的设备站点
 * @author Administrator
 */
@Entity
@Table(name="MC_DEVICESITE")
public class MCDeviceSite {
	private long id;
	private String clientIp;  	//ip地址
	private String deviceName;	//设备名称
	private String deviceCode;	//设备代码
	private String deviceSiteCode;//设备站点代码
	private String deviceSiteName;//设备站点名称
	private Boolean bottleneck = false;/**是否为瓶颈站点*/
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
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
	public Boolean getBottleneck() {
		return bottleneck;
	}
	public void setBottleneck(Boolean bottleneck) {
		this.bottleneck = bottleneck;
	}
}
