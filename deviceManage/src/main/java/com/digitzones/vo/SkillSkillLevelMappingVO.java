package com.digitzones.vo;
/**
 * 技能和技能等级映射的视图对象类
 * @author Administrator
 *
 */
public class SkillSkillLevelMappingVO {
	private Long id;
	private Long skillId;
	private String skillCode;
	private String skillName;
	private Long skillLevelId;
	private String skillLevelCode;
	private String skillLevelName;
	private String skillLevelNote;
	public String getSkillLevelNote() {
		return skillLevelNote;
	}
	public void setSkillLevelNote(String skillLevelNote) {
		this.skillLevelNote = skillLevelNote;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSkillId() {
		return skillId;
	}
	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}
	public String getSkillCode() {
		return skillCode;
	}
	public void setSkillCode(String skillCode) {
		this.skillCode = skillCode;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
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
	
}
