package com.digitzones.service;

import java.util.List;

import com.digitzones.model.ProjectType;
/**
 * 设备类型service
 * @author zdq
 * 2018年6月11日
 */
public interface IProjectTypeService extends ICommonService<ProjectType> {
	/**
	 * 查询所有设备类型
	 * @return
	 */
	List<ProjectType> queryAllProjectType();
	/**
	 * 查询顶级设备类型
	 * @param type 类型
	 * @return
	 */
	List<ProjectType> queryTopProjectTypes(String rootType);
	/**
	 * 根据type查询类型信息
	 * @param type 类型
	 * @return
	 */
	List<ProjectType> queryProjectTypesByType(String type);
	/**
	 * 根据父单元Id查询子单元
	 * @return
	 */
	List<ProjectType> queryProjectTypeByParentId(Long Id);
	/**
	 * 根据父单元Id查询子单元
	 * @return
	 */
	ProjectType queryProjectTypeByCodeAndType(String code,String type);

}
