package com.digitzones.model;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 设备站点
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="DEVICESITE")
public class DeviceSite {
	/**条码读头地址*/
	private String barCodeAddress;
	/**站点所在设备*/
	private Device device;
	/**设备站点状态
	 * 状态取值:0:正常运行
	 * 1：待机
	 * 2：停机
	 * */
	private String status;
	/**目标oee*/
	private Float goalOee;
	/**是否在参数状态中显示*/
	private Boolean show = false;
	/**对象唯一标识*/
	protected Long id;
	/**编号*/
	protected String code;
	/**名称*/
	protected String name;
	/**备注*/
	protected String note;
	/**是否停用*/
	protected Boolean disabled = false;
	/**是否为瓶颈站点*/
	private Boolean bottleneck = false;
	public Boolean getBottleneck() {
		return bottleneck;
	}
	public void setBottleneck(Boolean bottleneck) {
		this.bottleneck = bottleneck;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public Float getGoalOee() {
		return goalOee;
	}
	public void setGoalOee(Float goalOee) {
		this.goalOee = goalOee;
	}
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
	public String getBarCodeAddress() {
		return barCodeAddress;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEVICE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Device getDevice() {
		return device;
	}
	public String getStatus() {
		return status;
	}
	public void setBarCodeAddress(String barCodeAddress) {
		this.barCodeAddress = barCodeAddress;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
