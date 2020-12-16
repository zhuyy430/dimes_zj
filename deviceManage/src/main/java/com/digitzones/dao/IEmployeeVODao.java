package com.digitzones.dao;

import java.util.List;

import com.digitzones.vo.EmployeeVO;
/**
 * 人员数据访问接口
 * @author zdq
 * 2018年6月7日
 */
public interface IEmployeeVODao extends ICommonDao<EmployeeVO>{
	/**
	 * 根据部门id查询员工信息
	 * @param departmentId
	 * @return
	 */
	public List<EmployeeVO> queryEmployeesByDepartmentId(Long departmentId);
}
