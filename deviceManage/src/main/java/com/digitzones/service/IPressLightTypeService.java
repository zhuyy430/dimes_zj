package com.digitzones.service;
import java.io.Serializable;
import java.util.List;
import com.digitzones.model.PressLightType;
/**
 * 按灯类型servic
 * @author zdq
 * @author zdq  
 * 2018年6月13日
 */
public interface IPressLightTypeService extends ICommonService<PressLightType> {
	/**
	 * 根据父类型id查询子类型数量
	 * @param pid
	 * @return
	 */
	public Long queryCountOfSubPressLightType(Serializable pid);
	/**
	 * 查找顶层类型
	 * @return
	 */
	public List<PressLightType> queryTopPressLightType(String type);
	/**
	 * 查询第一级按灯类型，即level=1
	 * @return
	 */
	public List<PressLightType> queryFirstLevelType(String type);
	/**
	 * 根据父类 别id查询所有子类别
	 * @param pid
	 * @return
	 */
	public List<PressLightType> queryAllPressLightTypesByParentId(Long pid,String type);
	/**
	 * 查询所有按灯类别
	 * @return
	 */
	public List<PressLightType> queryAllPressLightTypes(String type);
	/**
	 * 根据id查找是否存在按灯信息
	 * @param id 按灯类别id
	 * @return
	 */
	public boolean isExistPressLights(Long id);
}
