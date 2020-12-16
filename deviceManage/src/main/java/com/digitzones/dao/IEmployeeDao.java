package com.digitzones.dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Employee;

/**
 * 人员数据访问接口
 * @author zdq
 * 2018年6月10日
 */
public interface IEmployeeDao extends ICommonDao<Employee> {
	/**
	 * 查找所有非用户员工
	 * @return
	 */
	public List<Employee> queryEmployees4UserImport();
	/**
	 * 添加员工信息带图片
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
}
