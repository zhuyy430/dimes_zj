package com.digitzones.devmgr.vo;

import java.io.Serializable;

/**
 * 备品记录
 * @author zhuyy430
 *
 */
public class SparepartRecordVO implements Serializable {
	private static final long serialVersionUID = -4711833140793307948L;
	private Long id;
	/**备件*/
	private Long sparepartId;
	/**备件code*/
	private String sparepartCode;
	/**备件name*/
	private String sparepartName;
	/**数量*/
	private Long quantity;
	/**备注*/
	private String note;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSparepartId() {
		return sparepartId;
	}
	public void setSparepartId(Long sparepartId) {
		this.sparepartId = sparepartId;
	}
	public String getSparepartCode() {
		return sparepartCode;
	}
	public void setSparepartCode(String sparepartCode) {
		this.sparepartCode = sparepartCode;
	}
	public String getSparepartName() {
		return sparepartName;
	}
	public void setSparepartName(String sparepartName) {
		this.sparepartName = sparepartName;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
