package com.digitzones.devmgr.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备报修单图片
 * @author zhuyy430
 *
 */
@Entity
@Table(name="DEVICEREPAIRPIC")
public class DeviceRepairPic implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**报修单Id*/
	private Long orderId;
	/**图片信息*/
	private Blob pic;
	/**图片名称*/
	private String picName;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Blob getPic() {
		return pic;
	}
	public void setPic(Blob pic) {
		this.pic = pic;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
}
