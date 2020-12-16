package com.digitzones.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IEmployeeDao;
import com.digitzones.model.Employee;
@Repository
public class EmployeeDaoImpl extends CommonDaoImpl<Employee> implements IEmployeeDao {

	public EmployeeDaoImpl() {
		super(Employee.class);
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Employee> queryEmployees4UserImport() {
		String sql = "select e.* from View_Person e where e.cPsn_Num not in (select u.employee_code from t_user u where u.EMPLOYEE_code is not null)";
		return getSession().createSQLQuery(sql).addEntity(Employee.class).list();
	}
	@Override
	public Serializable addEmployee(Employee employee, File pic) {
		if(pic!=null && pic.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(pic);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return this.save(employee);
	}
	@Override
	public void updateEmployee(Employee employee, File pic) {
		if(pic!=null && pic.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(pic);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		this.update(employee);
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Employee> queryEmployeeByProductionUnitId(Long productionUnitId) {
		String sql = "select e.id,e.ICNo,e.code,e.disabled,e.name,e.note,e.photo,e.POSITION_ID,e.PRODUCTIONUNIT_ID,e.pic "
				+ " from EMPLOYEE e  where e.disabled=?0 and e.PRODUCTIONUNIT_ID=?1";
		return getSession().createSQLQuery(sql).setParameter(0, false)
											   .setParameter(1,productionUnitId)
											   .addEntity(Employee.class)
											   .list();
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Employee> queryEmployeeNotInByProductionUnitId(Long productionUnitId) {
		String sql = "select e.id,e.ICNo,e.code,e.disabled,e.name,e.note,e.photo,e.POSITION_ID,e.PRODUCTIONUNIT_ID,e.pic "
				+ " from EMPLOYEE e  where e.disabled=?0 and (e.PRODUCTIONUNIT_ID!=?1 or e.PRODUCTIONUNIT_ID is null)";
		return getSession().createSQLQuery(sql).setParameter(0, false)
				.setParameter(1,productionUnitId)
				.addEntity(Employee.class)
				.list();
	}
}
