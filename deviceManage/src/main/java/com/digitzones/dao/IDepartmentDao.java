package com.digitzones.dao;

import com.digitzones.model.Department;
/**
 * 部门数据访问接口
 * @author zdq
 * 2018年6月7日
 */
public interface IDepartmentDao extends ICommonDao<Department> {
	/**
	 * 根据id查找父部门对象
	 * @param id 部门id
	 * @return 父部门对象
	 */
	Department queryParentById(Long id);

}
