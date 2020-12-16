package com.digitzones.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IEmployeeSkillMappingDao;
import com.digitzones.model.EmployeeSkillMapping;
@Repository
public class EmployeeSkillMappingDaoImpl extends CommonDaoImpl<EmployeeSkillMapping> implements IEmployeeSkillMappingDao {
	public EmployeeSkillMappingDaoImpl() {
		super(EmployeeSkillMapping.class);
	}
	@Override
	public Integer queryCountBySkillLevelIdAndEmployeeCode(String employeeCode, String skillLevelCode) {
		String sql = "select COUNT(es.Id) from EMPLOYEE_SKILL es  inner join PROCESSSKILLLEVEL psl on es.SKILLLEVEL_ID =psl.ID where es.EMPLOYEE_CODE=?0 and psl.code=?1";
		@SuppressWarnings("deprecation")
		Integer count = (Integer) getSession().createSQLQuery(sql)
					.setParameter(0,employeeCode)
					.setParameter(1,skillLevelCode)
					.uniqueResult();
		return count==null?0:count;
	}
	@Override
	public List<?> queryCountBySkillLevelCode(String skillLevelCode) {
		String sql = "select e.name,COUNT(es.Id) as sum from EMPLOYEE_SKILL es  inner join PROCESSSKILLLEVEL psl on es.SKILLLEVEL_ID =psl.ID "
				+ " inner join EMPLOYEE as e on e.id=es.EMPLOYEE_ID "
				+ " where  psl.code=?0 group by e.name";
		@SuppressWarnings("deprecation")
		List<?> count = getSession().createSQLQuery(sql)
		.setParameter(0,skillLevelCode)
		.list();
		return count;
	}
	@Override
	public Integer queryCountBySkillLevelIdAndEmployeeCodeAndProductionUnitId(String employeeCode, String skillLevelCode,
			Long productionUnitId) {
		String sql = "select COUNT(es.Id) from EMPLOYEE_SKILL es  inner join PROCESSSKILLLEVEL psl on es.SKILLLEVEL_ID =psl.ID inner join EMPLOYEE e on es.EMPLOYEE_CODE=e.id " +
				 " where es.EMPLOYEE_CODE=?0 and psl.code=?1 and e.PRODUCTIONUNIT_ID=?2";
		Integer count = (Integer) getSession().createNativeQuery(sql)
					.setParameter(0,employeeCode)
					.setParameter(1,skillLevelCode)
					.setParameter(2,productionUnitId)
					.uniqueResult();
		return count==null?0:count;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> queryCountBySkillLevelIdAndEmployeeCodeAndProductionUnitId(String skillLevelCode,
			Long productionUnitId) {
		String sql = "select es.EMPLOYEE_ID emp_id, COUNT(es.Id) _count from EMPLOYEE_SKILL es  "
				+ "	inner join PROCESSSKILLLEVEL psl on es.SKILLLEVEL_ID =psl.ID "
				+ "	inner join EMPLOYEE e on es.EMPLOYEE_CODE=e.id " +
				 " where psl.code=?0 and e.PRODUCTIONUNIT_ID=?1 group by es.EMPLOYEE_ID";
		
		List<Object[]> list = getSession().createNativeQuery(sql)
					.setParameter(0, skillLevelCode)
					.setParameter(1, productionUnitId)
					.list();
		return list;
	}
}
