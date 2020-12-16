package com.digitzones.service;

import com.digitzones.model.Department;

import java.io.Serializable;
import java.util.List;

/**
 * 部门管理业务接口
 * @author zdq
 * 2018年6月7日
 */
public interface IDepartmentService extends ICommonService<Department>{
	/**
	 * 查找子部门 
	 * @param pid 父部门id
	 * @return
	 */
	public List<Department> querySubDepartment(Serializable pid);
	/**
	 * 添加部门
	 * @param department
	 * @return
	 */
	public Serializable addDepartment(Department department);
	/**
	 * 根据id查找部门
	 * @param id
	 * @return
	 */
	public Department queryDepartmentById(Serializable id);
	/**
	 * 根据属性名称查找部门信息
	 * @param name
	 * @param value
	 * @return
	 */
	public Department queryDepartmentByProperty(String name,Object value);
	/**
	 * 查询所有部门信息
	 * @return
	 */
	public List<Department> queryAllDepartments();
	/**
	 * 根据id删除部门
	 * @param id
	 */
	public void deleteDepartment(Serializable id);
	/**
	 * 查找顶层部门，实际上就是公司信息
	 * @return
	 */
	public List<Department> queryTopDepartment();
	/**
	 * 根据父部门id查询子部门数量
	 * @param pid
	 * @return
	 */
	public Long queryCountOfSubDepartment(Serializable pid);
	/**
	 * 根据id查找父部门
	 * @param id 部门id
	 * @return 父部门对象
	 */
	public Department queryParentById(Long id);
	/**
	 * 查询本部门外的其他 部门
	 * @param id 本部门id
	 * @return 其他部门列表
	 */
	public List<Department> queryOtherDepartments(Long id);
	/**
	 * 查询所有有员工的部门
	 */
	public List<Department> queryDepartmentsByHaveEmployee();
	/**
	 * 查找所有维修人员的部门
	 */
	public List<Department> queryDepartmentsByHaveMaintenanceStaff();
	/**
	 * 查询所有非维修人员的部门
	 */
	public List<Department> queryDepartmentsByNotHaveMaintenanceStaff();

	/**
	 * 查找顶层部门，实际上就是公司信息（中京）
	 * @return
	 */
	public List<Department> queryTopDepartmentByzj();


	/**
	 * 根据编号和层级查找部门子类别（中京）
	 * @param code
	 * @param level
	 * @return
	 */
	public List<Department> queryChildrenDepartment(String code, int level);

}
