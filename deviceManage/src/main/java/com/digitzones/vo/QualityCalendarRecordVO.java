package com.digitzones.vo;

/**
 * 质量日历记录VO
 * @author zdq
 * 2018年7月19日
 */
public class QualityCalendarRecordVO {
	private Long id;
	/**当前日期*/
	private String currentDate;
	/**质量类别id*/
	private Long typeId;
	/**质量类别名称*/
	private String typeName;
	/**等级ID*/
	private Long gradeId;
	/**等级名称*/
	private String gradeName;
	/**备注*/
	private String note;
	/**投诉客户*/
	private String customer;
	/**联系方式*/
	private String tel;
	/**联系人*/
	private String contacts;
	/**投诉内容*/
	private String content;
	public String getContacts() {
		return contacts;
	}
	public String getContent() {
		return content;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public String getCustomer() {
		return customer;
	}
	public Long getGradeId() {
		return gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public Long getId() {
		return id;
	}
	public String getNote() {
		return note;
	}
	public String getTel() {
		return tel;
	}
	public Long getTypeId() {
		return typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
