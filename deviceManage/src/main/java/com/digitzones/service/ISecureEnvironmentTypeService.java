package com.digitzones.service;

import java.util.Date;
import java.util.List;

import com.digitzones.model.SecureEnvironmentType;
/**
 * 安环service
 * @author zdq
 * 2018年7月19日
 */
public interface ISecureEnvironmentTypeService extends ICommonService<SecureEnvironmentType> {
	/**
	 * 查询所有安环类型
	 * @return
	 */
	public List<SecureEnvironmentType> queryAllSecureEnvironmentTypes();
	
	/**
	 * 根据时间和类型查询安环记录数
	 * @param day
	 * @param typeId
	 * @return
	 */
	public Integer queryCountByDayAndTypeId(Date day,Long typeId);
}
