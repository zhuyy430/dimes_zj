package com.digitzones.dao;

import com.digitzones.model.SecureEnvironmentType;
/**
 * 安环类型dao
 * @author zdq
 * 2018年7月19日
 */
public interface ISecureEnvironmentTypeDao extends ICommonDao<SecureEnvironmentType> {
	/**
	 * 根据时间和类型id查询质量投诉数量
	 * @param year
	 * @param month
	 * @param day
	 * @param typeId
	 * @return
	 */
	public Integer queryCountByDayAndTypeId(int year,int month,int day,Long typeId);
}
