package com.digitzones.service;

import java.util.List;

import com.digitzones.model.SkillType;
/**
 * 人员技能类别service
 * @author zdq
 * 2018年6月11日
 */
public interface ISkillTypeService extends ICommonService<SkillType> {
	/**
	 * 查询所有技能类别
	 * @return
	 */
	public List<SkillType> queryAllSkillTypes();
}
