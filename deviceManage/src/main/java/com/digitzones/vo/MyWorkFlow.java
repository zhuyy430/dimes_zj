package com.digitzones.vo;
/**
 * 后台首页我的流程VO
 * @author Administrator
 */
public class MyWorkFlow {
	/**单据类型*/
	private String billType;
	/**时间*/
	private String date;
	/**单据ID*/
	private Long id;
	/**制单人*/
	private String documentMaker;
	/**代码*/
	private String code;
	/**数量*/
	private int count=1;
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDocumentMaker() {
		return documentMaker;
	}
	public void setDocumentMaker(String documentMaker) {
		this.documentMaker = documentMaker;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
