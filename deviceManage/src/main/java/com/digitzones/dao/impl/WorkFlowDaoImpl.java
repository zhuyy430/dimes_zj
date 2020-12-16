package com.digitzones.dao.impl;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkFlowDao;
@Repository
public class WorkFlowDaoImpl implements IWorkFlowDao {
	private HibernateTemplate hibernateTemplate;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
	
	public Session getSession() {
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Object[]> queryMyTasks(List<String> roleNames) {
		String sql = "select task.ID_" + 
				"      ,task.REV_" + 
				"      ,task.EXECUTION_ID_" + 
				"      ,task.PROC_INST_ID_" + 
				"      ,task.PROC_DEF_ID_" + 
				"      ,task.NAME_" + 
				"      ,task.PARENT_TASK_ID_" + 
				"      ,task.DESCRIPTION_" + 
				"      ,task.TASK_DEF_KEY_" + 
				"      ,task.OWNER_" + 
				"      ,task.ASSIGNEE_" + 
				"      ,task.DELEGATION_" + 
				"      ,task.PRIORITY_" + 
				"      ,task.CREATE_TIME_" + 
				"      ,task.DUE_DATE_" + 
				"      ,task.CATEGORY_" + 
				"      ,task.SUSPENSION_STATE_" + 
				"      ,task.TENANT_ID_" + 
				"      ,task.FORM_KEY_" + 
				"      ,task.CLAIM_TIME_ "
				+ " from ACT_RU_TASK task inner join ACT_RU_IDENTITYLINK link on task.ID_=link.TASK_ID_ "
				+ " where link.TYPE_='candidate' and link.GROUP_ID_ in (:groups)";
		return getSession().createSQLQuery(sql).setParameter("groups", roleNames).list();
	}
}
