package com.digitzones.procurement.model;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * ERP生产订单
 */
@Entity
public class Mom_recorddetailStatus {
	/*生产订单主表ID */
	@Id
	private String modId;
	/*生产订单状态 */
	private Boolean status;
	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
