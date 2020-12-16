package com.digitzones.dao;

import java.util.Date;

import com.digitzones.model.PressLightRecord;
/**
 * 按灯(故障)记录dao
 * @author zdq
 * 2018年6月20日
 */
public interface IPressLightRecordDao extends ICommonDao<PressLightRecord> {
	/**
	 * 根据按灯日期查询按灯次数
	 * @param pressLightTime
	 * @return
	 */
	public Long queryCountByPressLightTime(Date pressLightTime);
}
