package com.digitzones.service;

import java.util.List;

import com.digitzones.model.Skill;
/**
 * 人员技能业务逻辑接口
 * @author zdq
 * 2018年6月11日
 */
public interface ISkillService extends ICommonService<Skill> {
	/**
	 * 查找不属于当前员工的技能信息
	 * @param employeeId
	 * @return
	 */
	public List<Skill> queryOtherSkillsByEmployeeId(Long employeeId);
	/**
	 * 根据条件查找不属于当前员工的技能信息
	 * @param employeeId
	 * @param q
	 * @return
	 */
	public List<Skill> queryOtherSkillsByEmployeeIdAndCondition(Long employeeId,String q);
}
