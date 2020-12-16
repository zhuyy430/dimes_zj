package com.digitzones.service;

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.EquipmentType;
/**
 * 装备类型service
 * @author zdq
 * 2018年6月11日
 */
public interface IMeasuringToolTypeService extends ICommonService<EquipmentType> {
	/**
	 * 查找顶层参数类型
	 * @return
	 */
	public List<EquipmentType> queryTopEquipmentType();
	/**
	 * 根据父类型id查询子类型数量
	 * @param pid
	 * @return
	 */
	public Long queryCountOfSubEquipmentType(Serializable pid);
	/**
	 * 根据类型id查询参数数量
	 * @param typeId
	 * @return
	 */
	public Long queryCountOfEquipment(Serializable typeId);
}
