package com.digitzones.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Employee;
/**
 * 人员业务逻辑接口
 * @author zdq
 * 2018年6月10日
 */
public interface IEmployeeService extends ICommonService<Employee> {
	/**
	 * 查询所有员工
	 * @return
	 */
	public List<Employee> queryAllEmployees();
	/**
	 * 根据生产单元查询所有员工
	 * @return
	 */
	public List<Employee> queryAllEmployeesByProductionUnitId(Long productionUnitId);
	/**
	 * 删除员工
	 * @param ids 要删除的员工的id数组
	 * @return boolean true:成功，false：失败，关联用户
	 */
	public boolean deleteEmployees(String[] ids);
	/**
	 * 查询所有非用户的员工
	 * @return
	 */
	public List<Employee> queryEmployees4UserImport();
	/**
	 * 添加员工带图片
	 * @param employee
	 * @param pic
	 * @return
	 */
	public Serializable addEmployee(Employee employee, File pic);
	
	/**
	 * 更新员工信息
	 * @param employee
	 * @param pic
	 */
	public void updateEmployee(Employee employee, File pic);
	/**
	 * 根据生产单元ID获取员工信息
	 * @param productionUnitId
	 * @return
	 */
	public List<Employee> queryEmployeeByProductionUnitId(Long productionUnitId);
	/**
	 * 根据生产单元ID获取非该单元的员工信息
	 * @param productionUnitId
	 * @return
	 */
	public List<Employee> queryEmployeeNotInByProductionUnitId(Long productionUnitId);
	/**
	 * 根据员工代码查找员工对象
	 * @param code
	 * @return
	 */
	public Employee queryEmployeeByCode(String code);
	/**
	 * 查询所有非维修人员
	 */
	public List<Employee> queryAllEmployeeAndNotMaintenanceStaff();
	/**
	 * 查询所有维修人员
	 */
	public List<Employee> queryAllEmployeeAndMaintenanceStaff();
	
}
