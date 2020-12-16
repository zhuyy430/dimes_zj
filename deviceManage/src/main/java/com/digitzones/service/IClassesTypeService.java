package com.digitzones.service;

import java.util.List;

import com.digitzones.model.ClassType;
/**
 * 班次业务逻辑接口
 * @author zdq
 * 2018年6月11日
 */
public interface IClassesTypeService extends ICommonService<ClassType> {
	/**
	 * 查找所有班次类别
	 * @return
	 */
	public List<ClassType> queryAllClassTypes();
}
