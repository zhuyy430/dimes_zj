package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.ProcessSkillLevel;
/**
 * 工序下的技能等级
 * @author zdq
 * 2018年7月12日
 */
public interface IProcessSkillLevelDao extends ICommonDao<ProcessSkillLevel> {
	/**
	 * 查询技能等级编码，名称和数量
	 * @return List<Object[]> 0:code,1:name,2:数量
	 */
	public List<Object[]> queryCount4SkillLevel();
	/**
	 * 根据技能等级编码查找工序编码，工序名称及数量
	 * @param skillLevelCode
	 * @return
	 */
	public List<Object[]> queryCount4ProcessBySkillLevelCode(String skillLevelCode);
	
	
}
