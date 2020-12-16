package com.digitzones.service;

import java.io.Serializable;

import com.digitzones.model.Pager;
/**
 * 通用分页查询业务接口
 * @author zdq
 * 2018年6月7日
 */
public interface ICommonService<T> {
	/**
	 * 分页查询对象
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @param values
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values);
	/**
	 * 更新对象
	 * @param obj
	 */
	public void updateObj(T obj);
	/**
	 * 根据属性查询对象
	 * @param name 属性名称
	 * @param value 属性值
	 * @return
	 */
	public T queryByProperty(String name,String value);
	/**
	 * 添加对象
	 * @param obj
	 * @return
	 */
	public Serializable addObj(T obj);
	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public T queryObjById(Serializable id);
	/**
	 * 根据id删除对象
	 * @param id
	 */
	public void deleteObj(Serializable id);
}
