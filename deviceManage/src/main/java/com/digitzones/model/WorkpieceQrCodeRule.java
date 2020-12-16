package com.digitzones.model;

import com.digitzones.procurement.model.Inventory;

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
/**
 * 工件二维码规则实体
 * @author Administrator
 */
@Entity
public class WorkpieceQrCodeRule implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	/**厂商代码*/
	private String manufacturerCode;
	/**产生日期，手动输入*/
	private String generateDate;
	/**流水号 */
	private String serNum;
	/**打印机IP*/
	private String printerIp;
	/**是否可用*/
	private boolean enabled = true;
	/**创建日期*/
	private Date createDate;
	/**发送到打印机服务器日期*/
	private Date sendDate;
	/**发送到服务器端的字符串*/
	private String tm1;
	/**发送到服务器端的字符串*/
	private String tm2;
	/**关联的工件对象*/
	private Inventory workpiece;
	/**是否已发送到打印机*/
	private boolean sended = false;
	/**打印机登录用户*/
	private String remoteUser;
	/**共享目录*/
	private String sharedDir = "/tm2/";
	/**打印机登录密码*/
	private String remotePass;
	public String getSharedDir() {
		return sharedDir;
	}
	public void setSharedDir(String sharedDir) {
		this.sharedDir = sharedDir;
	}
	public String getRemoteUser() {
		return remoteUser;
	}
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}
	public String getRemotePass() {
		return remotePass;
	}
	public void setRemotePass(String remotePass) {
		this.remotePass = remotePass;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isSended() {
		return sended;
	}
	public void setSended(boolean sended) {
		this.sended = sended;
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getGenerateDate() {
		return generateDate;
	}
	public void setGenerateDate(String generateDate) {
		this.generateDate = generateDate;
	}
	public String getSerNum() {
		return serNum;
	}
	public void setSerNum(String serNum) {
		this.serNum = serNum;
	}
	public String getPrinterIp() {
		return printerIp;
	}
	public void setPrinterIp(String printerIp) {
		this.printerIp = printerIp;
	}
	public String getTm1() {
		return tm1;
	}
	public void setTm1(String tm1) {
		this.tm1 = tm1;
	}
	public String getTm2() {
		return tm2;
	}
	public void setTm2(String tm2) {
		this.tm2 = tm2;
	}
	@ManyToOne
	@JoinColumn(name="WORKPIECE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	public Inventory getWorkpiece() {
		return workpiece;
	}
	public void setWorkpiece(Inventory workpiece) {
		this.workpiece = workpiece;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
}
