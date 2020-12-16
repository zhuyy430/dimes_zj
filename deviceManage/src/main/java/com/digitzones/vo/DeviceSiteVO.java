package com.digitzones.vo;
/**
 * 设备站点值对象
 * @author Administrator
 *
 */
public class DeviceSiteVO {
	private Long id;
	private String disabled;
	private String note;
	private String name;
	private String code;
	private String show;
	private String barCodeAddress;
	private String status;
	private Float goalOee;
	/**是否为瓶颈站点*/
	private Boolean bottleneck = false;
	public Boolean getBottleneck() {
		return bottleneck;
	}
	public void setBottleneck(Boolean bottleneck) {
		this.bottleneck = bottleneck;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public String getBarCodeAddress() {
		return barCodeAddress;
	}
	public void setBarCodeAddress(String barCodeAddress) {
		this.barCodeAddress = barCodeAddress;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Float getGoalOee() {
		return goalOee;
	}
	public void setGoalOee(Float goalOee) {
		this.goalOee = goalOee;
	}
}
