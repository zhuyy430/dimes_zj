package com.digitzones.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * 工件二维码规则实体
 * @author Administrator
 */
public class WorkpieceQrCodeRuleVO implements Serializable {
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
	/**发送到服务器端的字符串*/
	private String tm1;
	/**发送到服务器端的字符串*/
	private String tm2;
	/**是否可用*/
	private boolean enabled = true;
	/**创建日期*/
	private Date createDate;
	/**发送到打印机服务器日期*/
	private Date sendDate;
	/**打印机登录用户*/
	private String remoteUser;
	/**共享目录*/
	private String sharedDir = "/tm2/";
	/**打印机登录密码*/
	private String remotePass;
	public String getRemoteUser() {
		return remoteUser;
	}
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}
	public String getSharedDir() {
		return sharedDir;
	}
	public void setSharedDir(String sharedDir) {
		this.sharedDir = sharedDir;
	}
	public String getRemotePass() {
		return remotePass;
	}
	public void setRemotePass(String remotePass) {
		this.remotePass = remotePass;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	/**工件id*/
	private Long workpieceId;
	/**工件代码*/
	private String workpieceCode;
	/**工件名称*/
	private String workpieceName;
	/**客户图号*/
	private String customerGraphNumber;
	/**版本号*/
	private String version;
	public Long getWorkpieceId() {
		return workpieceId;
	}
	public void setWorkpieceId(Long workpieceId) {
		this.workpieceId = workpieceId;
	}
	public String getWorkpieceName() {
		return workpieceName;
	}
	public void setWorkpieceName(String workpieceName) {
		this.workpieceName = workpieceName;
	}
	public String getCustomerGraphNumber() {
		return customerGraphNumber;
	}
	public void setCustomerGraphNumber(String customerGraphNumber) {
		this.customerGraphNumber = customerGraphNumber;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getWorkpieceCode() {
		return workpieceCode;
	}
	public void setWorkpieceCode(String workpieceCode) {
		this.workpieceCode = workpieceCode;
	}
}
