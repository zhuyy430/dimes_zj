package com.digitzones.service;

import java.util.List;

import com.digitzones.model.ProcessSkillLevel;
/**
 * 工序 下的技能等级service
 * @author zdq
 * 2018年7月12日
 */
public interface IProcessSkillLevelService extends ICommonService<ProcessSkillLevel> {
	/**
	 * 根据技能id查找工序下的技能等级
	 * @param skillId
	 * @return
	 */
	public List<ProcessSkillLevel> queryProcessSkillLevelsBySkillId(Long skillId);

	/**
	 * 查询技能等级编码，名称和数量
	 * @return
	 */
	public List<Object[]> queryCount4SkillLevel();
	/**
	 * 根据技能等级编码查找工序编码，工序名称及数量
	 * @param skillLevelCode
	 * @return
	 */
	public List<Object[]> queryCount4ProcessBySkillLevelCode(String skillLevelCode);
}
