package com.digitzones.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.dao.IEmployeeDao;
import com.digitzones.dao.IEmployeeSkillMappingDao;
import com.digitzones.model.Employee;
import com.digitzones.model.EmployeeSkillMapping;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
@Service
public class EmployeeServiceImpl implements IEmployeeService {
	private IEmployeeDao employeeDao;
	private IUserService userService;
	@Autowired
	private IEmployeeSkillMappingDao employeeSkillMapping;
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setEmployeeDao(IEmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return employeeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Employee obj) {
		employeeDao.update(obj);
	}

	@Override
	public Employee queryByProperty(String name, String value) {
		return employeeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Employee obj) {
		return employeeDao.save(obj);
	}

	@Override
	public Employee queryObjById(Serializable id) {
		return employeeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		employeeDao.deleteById(id);
	}

	@Override
	public List<Employee> queryAllEmployees() {
		return employeeDao.findAll();
	}

	@Override
	public boolean deleteEmployees(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			List<User> users = userService.queryUsersByEmployeeId(id);
			if(users!=null&&users.size()>0) {
				return false;
			}
			List<EmployeeSkillMapping> list = employeeSkillMapping.findByHQL("from EmployeeSkillMapping esm where esm.employee.id=?0", new Object[] {id});
			if(!CollectionUtils.isEmpty(list)) {
				for(EmployeeSkillMapping esm : list) {
					employeeSkillMapping.delete(esm);
				}
			}
			employeeDao.deleteById(id);
		}
		return true; 
	}

	@Override
	public List<Employee> queryEmployees4UserImport() {
		return employeeDao.queryEmployees4UserImport();
	}

	@Override
	public List<Employee> queryAllEmployeesByProductionUnitId(Long productionUnitId) {
		String hql = "select emp from Employee emp where emp.productionUnit.id=?0 and emp.disabled=?1";
		return employeeDao.findByHQL(hql, new Object[] {productionUnitId,false});
	}

	@Override
	public Serializable addEmployee(Employee employee, File pic) {
		return employeeDao.addEmployee(employee, pic);
	}

	@Override
	public void updateEmployee(Employee employee, File pic) {
		employeeDao.updateEmployee(employee, pic);
	}
	@Override
	public List<Employee> queryEmployeeByProductionUnitId(Long productionUnitId){
		return employeeDao.queryEmployeeByProductionUnitId(productionUnitId);
	}
	@Override
	public List<Employee> queryEmployeeNotInByProductionUnitId(Long productionUnitId){
		return employeeDao.queryEmployeeNotInByProductionUnitId(productionUnitId);
	}

	@Override
	public Employee queryEmployeeByCode(String code) {
		return employeeDao.findSingleByProperty("code", code);
	}

	@Override
	public List<Employee> queryAllEmployeeAndNotMaintenanceStaff() {
		String hql="select e from Employee e where e.code not in (select m.code from MaintenanceStaff m)";
		return employeeDao.findByHQL(hql, new Object[]{});
	}

	@Override
	public List<Employee> queryAllEmployeeAndMaintenanceStaff() {
		String hql="select e from Employee e where e.code in (select m.code from MaintenanceStaff m)";
		return employeeDao.findByHQL(hql, new Object[]{});
	}
}
