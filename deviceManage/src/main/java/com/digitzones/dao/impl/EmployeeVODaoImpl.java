package com.digitzones.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IEmployeeVODao;
import com.digitzones.vo.EmployeeVO;
@SuppressWarnings({"deprecation","unchecked"})
@Repository
public class EmployeeVODaoImpl extends CommonDaoImpl<EmployeeVO> implements IEmployeeVODao {
	public EmployeeVODaoImpl() {
		super(EmployeeVO.class);
	}
	
	@Override
	public List<EmployeeVO> queryEmployeesByDepartmentId(final Long departmentId) {
		return getHibernateTemplate().execute(new HibernateCallback<List<EmployeeVO>>() {
			@Override
			public List<EmployeeVO> doInHibernate(Session session) throws HibernateException {
				List<EmployeeVO> vos = new ArrayList<>();
				String queryString = "SELECT E.ID ID,E.CODE CODE,E.NAME NAME,E.DISABLED DISABLED,E.NOTE NOTE " +
										",P.CODE POSITIONCODE,P.ID POSITIONID,P.NAME POSITIONNAME, "
										+ " D.ID DEPARTMENTID,D.CODE DEPARTMENTCODE,D.NAME DEPARTMENTNAME "
										+ " FROM EMPLOYEE E LEFT JOIN POSITION P ON E.POSITION_ID=P.ID "
										+ " LEFT JOIN DEPARTMENT D ON P.DEPARTMENT_ID=D.ID WHERE D.ID= ?";
				SQLQuery<EmployeeVO> sqlQuery = session.createSQLQuery(queryString);
				sqlQuery.setLong(0,departmentId);
				sqlQuery.addEntity(EmployeeVO.class);
				vos = sqlQuery.list();
				return vos;
			}
		});
	}

}
