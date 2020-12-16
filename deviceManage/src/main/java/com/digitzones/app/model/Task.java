package com.digitzones.app.model;

import com.digitzones.model.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * app任务
 * @author zdq
 * 2018年9月30日
 */
@Entity
@Table(name="APP_TASK")
public class Task {
	private Long id;
	/**状态*/
	private String status;
	/**内容描述*/
	private String description ;
	/**管理事项*/
	private String manageType;
	/**责任负责人id(员工ID)*/
	private Long userId;
	/**责任负责人Code*/
	private String code;
	/**责任负责人姓名*/
	private String userName;
	/**图片名称*/
	private List<String> picName = new ArrayList<>();
	/**图片信息*/
	private List<Blob> pic;
	/**处理说明*/
	private String treatDescription ;
	/**处理人*/
	private Long treatUserid;
	/**处理人*/
	private String treatUserName;
	/**创建人*/
	private Long createUserid;
	/**创建人Code*/
	private String createUserCode;
	/**创建人姓名*/
	private String createUserName;
	/**抄送人*/
	private List<Employee> ccpList;
	/**创建时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getManageType() {
		return manageType;
	}
	public void setManageType(String manageType) {
		this.manageType = manageType;
	}
	@JsonIgnore
	@Transient
	public List<Blob> getPic() {
		return pic;
	}
	public void setPic(List<Blob> pic) {
		this.pic = pic;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getTreatDescription() {
		return treatDescription;
	}
	public void setTreatDescription(String treatDescription) {
		this.treatDescription = treatDescription;
	}
	@Transient
	public List<String> getPicName() {
		return picName;
	}
	public void setPicName(List<String> picName) {
		this.picName = picName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getTreatUserid() {
		return treatUserid;
	}
	public void setTreatUserid(Long treatUserid) {
		this.treatUserid = treatUserid;
	}
	public Long getCreateUserid() {
		return createUserid;
	}
	public void setCreateUserid(Long createUserid) {
		this.createUserid = createUserid;
	}
	@Transient
	public List<Employee> getCcpList() {
		return ccpList;
	}
	public void setCcpList(List<Employee> ccpList) {
		this.ccpList = ccpList;
	}
	public String getTreatUserName() {
		return treatUserName;
	}
	public void setTreatUserName(String treatUserName) {
		this.treatUserName = treatUserName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreateUserCode() {
		return createUserCode;
	}
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}
}
