package com.digitzones.mc.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.model.PressLightRecord;
/**
 * 按灯(故障)记录dao
 * @author zdq
 * 2018年6月20日
 */
public interface IMCPressLightRecordDao extends ICommonDao<PressLightRecord> {
	/**
	 * @param pressLightTime
	 * @return
	 */
	public List<PressLightRecord> queryPressLightRecordByBasicCode(String typeCode,String deviceSiteCode);
}
