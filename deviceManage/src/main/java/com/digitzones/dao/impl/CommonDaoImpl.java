package com.digitzones.dao.impl;
import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.ICommonDao;
import com.digitzones.model.Pager;
@SuppressWarnings("deprecation")
@Repository("commonDaoImpl")
@Transactional
public abstract class CommonDaoImpl<T> implements ICommonDao<T> {
	private Class<T> clazz; 
	
	public CommonDaoImpl(Class<T> clazz) {
		this.clazz = clazz;
	}
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public Session getSession() {
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return this.hibernateTemplate.getSessionFactory();
	}
	public void flush() {
		this.getSessionFactory().getCurrentSession().flush();
	}
	@Override
	public Serializable save(T t) {
		return this.hibernateTemplate.save(t);
	}
	@Override
	public void deleteById(Serializable id) {
		T t = findById(id);
		delete(t);
	}
	@Override
	public void delete(T t) {
		this.hibernateTemplate.delete(t);
	}
	@Override
	public T findById(Serializable id) {
		return this.hibernateTemplate.get(clazz, id);
	}
	@Override
	public void update(T t) {
		this.hibernateTemplate.update(t);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByCriteria(DetachedCriteria criteria) {
		return (List<T>) this.hibernateTemplate.findByCriteria(criteria);
	}
	@Override
	public List<T> findAll() {
		return this.hibernateTemplate.loadAll(clazz);
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<T> findByHQL(String hql, Object... values) {
		return (List<T>) this.hibernateTemplate.find(hql, values);
	}
	@Override
	public T findSingleByProperty(String property, Object value) {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		criteria.add(Restrictions.eq(property, value));
		List<T> list = findByCriteria(criteria);
		return (list!=null &&list.size()>0)?list.get(0):null;
	}
	@Override
	public List<T> findListByProperty(String property, Object value) {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		criteria.add(Restrictions.eq(property, value));
		List<T> list = findByCriteria(criteria);
		return list;
	}
	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public Pager<T> findByPage(String hql, int pageNo, int pageSize, Object... values) {
		// Count查询
		String countQueryString = " select count (*) " + removeSelect(removeOrderBy(hql));
		List countlist = getHibernateTemplate().find(countQueryString.replace("fetch", ""), values);
		long totalCount = countlist.size()<=0?0:(Long) countlist.get(0);

		if (totalCount < 1)
			return new Pager();
		// 实际查询返回分页对象
		int startIndex = Pager.getStartOfPage(pageNo, pageSize);
		Query query = createQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Pager(startIndex,  pageSize, (int)totalCount,list);
	}
	/**
	 * 去掉hql语句中的select部分
	 * @param hql
	 * @return
	 */
	private  String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}
	
	private String removeOrderBy(String hql) {
		if(hql.toLowerCase().contains("order")) {
			int endPos = hql.toLowerCase().indexOf("order");
			return hql.substring(0, endPos);
		}else {
			return hql;
		}
	}
	
	/**
	 * 创建查询语句
	 * @param hql
	 * @param values
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	private Query createQuery(String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}
	@Override
	public Long findCount(String hql, Object... values) {
		String h = "select count(*) " + hql;
		return (Long) this.hibernateTemplate.find(h, values).get(0);
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<T> findBySql(String sql, Object... values) {
		SQLQuery<T> sqlQuery = this.getSession().createSQLQuery(sql);
		sqlQuery.addEntity(clazz);
		for(int i = 0;i<values.length;i++) {
			sqlQuery.setParameter(i,values[i]);
		}
		return sqlQuery.list();
	}
}
