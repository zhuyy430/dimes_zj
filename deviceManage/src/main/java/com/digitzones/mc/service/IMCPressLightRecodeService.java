package com.digitzones.mc.service;

import java.util.List;

import com.digitzones.model.PressLightRecord;

public interface IMCPressLightRecodeService {
	/**
	 * @param typeId
	 * @return
	 */
	public List<PressLightRecord> queryPressLightRecordByBasicCode(String typeCode,String deviceSiteCode);

}
