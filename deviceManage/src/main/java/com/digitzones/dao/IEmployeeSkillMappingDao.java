package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.EmployeeSkillMapping;
/**
 * 员工和技能关联dao
 * @author zdq
 * 2018年6月11日
 */
public interface IEmployeeSkillMappingDao extends ICommonDao<EmployeeSkillMapping> {
	/**
	 * 根据员工id和技能等级id查找技能数
	 * @param employeeCode
	 * @param skillLevelCode
	 * @return
	 */
	public Integer queryCountBySkillLevelIdAndEmployeeCode(String employeeCode,String skillLevelCode);
	/**
	 * 技能等级id查找技能数
	 * @param skillLevelCode
	 * @return
	 */
	public List<?> queryCountBySkillLevelCode(String skillLevelCode);
	/**
	 * 根据员工id、技能id和产线id查找技能数
	 * @param employeeCode
	 * @param skillLevelCode
	 * @param productionUnitId
	 * @return
	 */
	public Integer queryCountBySkillLevelIdAndEmployeeCodeAndProductionUnitId(String employeeCode,String skillLevelCode,Long productionUnitId);
	/**
	 * 根据技能id和产线id查找员工信息及技能数
	 * @param skillLevelCode
	 * @param productionUnitId
	 * @return 0:员工id 1：技能数
	 */
	public List<Object[]> queryCountBySkillLevelIdAndEmployeeCodeAndProductionUnitId(String skillLevelCode,Long productionUnitId);
}
