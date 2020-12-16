package com.digitzones.vo;
/**
 * 员工和技能映射实体的值对象
 * @author zdq
 * 2018年7月12日
 */
public class EmployeeSkillMappingVO {
	private Long id;
	private Long employeeId;
	private String employeeName;
	private Long skillId;
	private String skillName;
	private String employeeCode;
	private String skillCode;
	private String updateDate;
	private Long skillLevelId;
	private String skillLevelCode;
	private String skillLevelName;
	
	public Long getSkillLevelId() {
		return skillLevelId;
	}
	public void setSkillLevelId(Long skillLevelId) {
		this.skillLevelId = skillLevelId;
	}
	public String getSkillLevelCode() {
		return skillLevelCode;
	}
	public void setSkillLevelCode(String skillLevelCode) {
		this.skillLevelCode = skillLevelCode;
	}
	public String getSkillLevelName() {
		return skillLevelName;
	}
	public void setSkillLevelName(String skillLevelName) {
		this.skillLevelName = skillLevelName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getSkillId() {
		return skillId;
	}
	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getSkillCode() {
		return skillCode;
	}
	public void setSkillCode(String skillCode) {
		this.skillCode = skillCode;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
}
