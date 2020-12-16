package com.digitzones.service;

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.ParameterType;
/**
 * 参数类型service
 * @author zdq
 * 2018年6月12日
 */
public interface IParameterTypeService extends ICommonService<ParameterType> {
	/**
	 * 查找顶层参数类型
	 * @return
	 */
	public List<ParameterType> queryTopParameterType();
	/**
	 * 根据父类型id查询子类型数量
	 * @param pid
	 * @return
	 */
	public Long queryCountOfSubParameterType(Serializable pid);
	/**
	 * 根据类型id查询参数数量
	 * @param typeId
	 * @return
	 */
	public Long queryCountOfParameter(Serializable typeId);
}
