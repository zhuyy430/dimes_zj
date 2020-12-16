package com.digitzones.mc.service;

import java.util.List;

import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightType;

public interface IMCPressLightService {
	/**
	 * 根据父类code查找所有损时原因
	 * @param pcode
	 * @return
	 */
	public List<PressLightType> queryPressLightRecordByBasicCode(String pcode);
	/**
	 * 查找所有损时原因
	 * @param pcode
	 * @return
	 */
	public List<PressLight> queryAllPressLight();

}
