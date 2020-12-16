package com.digitzones.dao;
import java.io.Serializable;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import com.digitzones.model.Pager;
/**
 * 公共功能提取接口
 * @author zdq
 * 2018年6月5日
 */
public interface ICommonDao<T> {
	/**
	 * 添加对象
	 * @param o
	 * @return
	 */
	public Serializable save(T t);
	
	public void flush();
	/**
	 * 根据id删除
	 * @param id
	 */
	public void deleteById(final Serializable id);
	/**
	 * 根据实体对象删除
	 * @param t
	 */
	public void delete(T t);
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public T findById(Serializable id);
	/**
	 * 更新实体对象
	 * @param t
	 */
	public void update(T t);
	/**
	 * 离线查询
	 * @param criteria
	 * @return
	 */
	public List<T> findByCriteria(DetachedCriteria criteria);
	/**
	 * 查询所有对象
	 * @return
	 */
	public List<T> findAll();
	/**
	 * 根据hql查询
	 * @param hql hql语句
	 * @param values 填充hql语句的对象
	 * @return
	 */
	public List<T> findByHQL(String hql,Object ...values);
	/**
	 * 根据属性查找单个对象
	 * @param property 属性名称
	 * @param value 属性值
	 * @return T
	 */
	public T findSingleByProperty(String name,Object value);
	/**
	 * 根据属性查找对象列表
	 * @param property
	 * @param value
	 * @return
	 */
	public List<T> findListByProperty(String property,Object value);
	/**
	 * 根据sql语句查询
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<T> findBySql(String sql,Object... values);
	/**
	 * 分页查询
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @param values
	 * @return
	 */
    public Pager<T> findByPage(String hql,int pageNo,int pageSize,Object... values);
    /**
     * 查询数量
     * @param hql
     * @param values
     * @return
     */
    public Long findCount(String hql,Object... values);
}
