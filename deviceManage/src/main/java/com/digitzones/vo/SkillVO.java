package com.digitzones.vo;

import com.digitzones.model.Skill;

/**
 * 技能VO
 * @author zdq
 * 2018年6月3日
 */
public class SkillVO extends Skill {
	private static final long serialVersionUID = 1L;
	private Long skillTypeId;
	private String skillTypeName;
	private String skillTypeCode;
	public Long getSkillTypeId() {
		return skillTypeId;
	}
	public void setSkillTypeId(Long skillTypeId) {
		this.skillTypeId = skillTypeId;
	}
	public String getSkillTypeName() {
		return skillTypeName;
	}
	public void setSkillTypeName(String skillTypeName) {
		this.skillTypeName = skillTypeName;
	}
	public String getSkillTypeCode() {
		return skillTypeCode;
	}
	public void setSkillTypeCode(String skillTypeCode) {
		this.skillTypeCode = skillTypeCode;
	}
	
}
