package com.digitzones.service;

import java.util.List;

import com.digitzones.model.SkillLevel;
/**
 * 人员技能等级service
 * @author zdq
 * 2018年6月11日
 */
public interface ISkillLevelService extends ICommonService<SkillLevel> {
	/**
	 * 查找所有技能等级
	 * @return
	 */
	public List<SkillLevel> queryAllSkillLevels();

}
