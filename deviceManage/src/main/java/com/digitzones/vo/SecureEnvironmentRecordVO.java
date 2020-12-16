package com.digitzones.vo;

/**
 * 安环记录
 * @author zdq
 * 2018年6月5日
 */
public class SecureEnvironmentRecordVO {
	private Long id;
	/**当前日期*/
	private String currentDate;
	/**安环类别id*/
	private Long typeId;
	/**安环类别名称*/
	private String typeName;
	/**等级ID*/
	private Long gradeId;
	/**等级名称*/
	private String gradeName;
	/**描述*/
	private String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Long getGradeId() {
		return gradeId;
	}
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
