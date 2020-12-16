package com.digitzones.service;

import java.util.List;

import com.digitzones.model.PressLight;
/**
 * 按灯servic
 * @author zdq
 * 2018年6月13日
 */
public interface IPressLightService extends ICommonService<PressLight> {
	/**
	 * 根据类别id查询按灯信息
	 * @param typeId 类别id
	 * @return List<PressLight>
	 */
	public List<PressLight> queryAllPressLightByTypeId(Long typeId);
	/**
	 * 根据basicCode查询按灯信息
	 * @param basicCode level 1的按灯类别代码 
	 * @return List<PressLight>
	 */
	public List<PressLight> queryPressLightByBasicCode(String basicCode);
	/**
	 * 查询所有按灯信息
	 * @return List<PressLight>
	 */
	public List<PressLight> queryAllPressLight();
	
}
