package com.digitzones.service.impl;

import com.digitzones.dao.IDepartmentDao;
import com.digitzones.model.Department;
import com.digitzones.model.Pager;
import com.digitzones.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
@Service
public class DepartmentServiceImpl  implements IDepartmentService {
	private IDepartmentDao departmentDao;
	@Autowired
	public void setDepartmentDao(IDepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	@Override
	public List<Department> queryTopDepartment() {
		return departmentDao.findByHQL("from Department d where d.parent is null and d.disabled=?0 order by code", new Object[] {false});
	}

	@Override
	public List<Department> querySubDepartment(Serializable pid) {
		return departmentDao.findByHQL("from Department d where d.id = ?0 order by code", new Object[] {pid});
	}

	@Override
	public Serializable addDepartment(Department department) {
		return departmentDao.save(department);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return departmentDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public Department queryDepartmentById(Serializable id) {
		return departmentDao.findById(id);
	}

	@Override
	public Department queryDepartmentByProperty(String name, Object value) {
		return departmentDao.findSingleByProperty(name, value);
	}

	@Override
	public List<Department> queryAllDepartments() {
		return departmentDao.findAll();
	}

	@Override
	public void updateObj(Department obj) {
		this.departmentDao.update(obj);
	}

	@Override
	public void deleteDepartment(Serializable id) {
		departmentDao.deleteById(id);
	}

	@Override
	public Long queryCountOfSubDepartment(Serializable pid) {
		return departmentDao.findCount("from Department d inner join d.parent p where p.id=?0 ", new Object[] {pid});
	}

	@Override
	public Department queryByProperty(String name, String value) {
		return departmentDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Department obj) {
		return departmentDao.save(obj);
	}

	@Override
	public Department queryObjById(Serializable id) {
		return departmentDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		departmentDao.deleteById(id);
	}

	@Override
	public Department queryParentById(Long id) {
		return departmentDao.queryParentById(id);
	}

	@Override
	public List<Department> queryOtherDepartments(Long id) {
		return departmentDao.findByHQL("from Department d where d.id!=?0", new Object[] {id});
	}

	@Override
	public List<Department> queryDepartmentsByHaveEmployee() {
		String hql="from Department dl where dl.code in (select e.cDept_Num from Employee e  group by e.cDept_Num)";
		return departmentDao.findByHQL(hql, new Object[] {});
	}

	@Override
	public List<Department> queryDepartmentsByHaveMaintenanceStaff() {
		String hql="from Department dl where dl.code in (select e.cDept_Num from Employee e  inner join MaintenanceStaff ms on e.code=ms.code group by e.cDept_Num)";
		return departmentDao.findByHQL(hql, new Object[] {});
	}

	@Override
	public List<Department> queryDepartmentsByNotHaveMaintenanceStaff() {
		String hql="from Department dl where dl.code in (select e.cDept_Num from Employee e  where e.code not in (select ms.code from MaintenanceStaff ms) group by e.cDept_Num)";
		return departmentDao.findByHQL(hql, new Object[] {});
	}

	@Override
	public List<Department> queryTopDepartmentByzj() {
		List<Department> list = departmentDao.findByHQL("from Department d where d.iDepGrade=1", new Object[] {});
		return list;
	}

	@Override
	public List<Department> queryChildrenDepartment(String code, int level) {
		return departmentDao.findByHQL("from Department d where d.iDepGrade=?0 and d.code like ?1", new Object[] {level,code+"%"});
	}
}
