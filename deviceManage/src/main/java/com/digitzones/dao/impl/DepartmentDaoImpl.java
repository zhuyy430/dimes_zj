package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IDepartmentDao;
import com.digitzones.model.Department;
@Repository
public class DepartmentDaoImpl extends CommonDaoImpl<Department> implements IDepartmentDao {
	public DepartmentDaoImpl() {
		super(Department.class);
	}

	@Override
	public Department queryParentById(Long id) {
		return (Department) getSession().createNativeQuery("select parent.* from Department son inner join Department parent "
				+ " on son.parent_id = parent.id where son.id=?0")
							.setParameter(0, id)
							.addEntity(Department.class)
							.uniqueResult();
	}
}
