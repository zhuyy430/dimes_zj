package com.digitzones.mc.dao;


import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightType;
/**
 * 
 * @author zdq
 * 2018年6月20日
 */
public interface IMCPressLightDao extends ICommonDao<PressLightType> {
	/**
	 * 根据父类code查询所有子类
	 * @param pcode
	 * @return
	 */
	List<PressLightType> queryAllTypeByParentCode(String pcode);

	/**
	 * 查询所有故障原因
	 * @return
	 */
	List<PressLight> queryAllPressLight();
}
