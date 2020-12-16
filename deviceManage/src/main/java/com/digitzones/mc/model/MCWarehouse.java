package com.digitzones.mc.model;

import javax.persistence.*;

/**
 * MC端配置的NG仓库
 * @author Administrator
 */
@Entity
@Table(name="MC_Warehouse")
public class MCWarehouse {
	private long id;
	private String clientIp;  	//ip地址
	private String cWhCode;	//仓库代码
	private String cWhName;	//仓库名称
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

	public String getcWhCode() {
		return cWhCode;
	}

	public void setcWhCode(String cWhCode) {
		this.cWhCode = cWhCode;
	}

	public String getcWhName() {
		return cWhName;
	}

	public void setcWhName(String cWhName) {
		this.cWhName = cWhName;
	}
}
