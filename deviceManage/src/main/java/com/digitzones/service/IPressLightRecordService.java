package com.digitzones.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.model.PressLightRecord;
import com.digitzones.model.User;
/**
 * 按灯(故障)记录service
 * @author zdq
 * 2018年6月20日
 */
public interface IPressLightRecordService extends ICommonService<PressLightRecord> {
	/**
	 * 根据按灯日期查询按灯次数
	 * @param pressLightTime
	 * @return
	 */
	public Long queryCountByPressLightTime(Date pressLightTime);
	/**
	 * 根据时间查询按灯记录
	 * @param date
	 * @return
	 */
	public List<PressLightRecord> queryPressLightRecordsByTime(Date date);
	/**
	 * 添加按灯记录
	 * @param pressLightRecord 按灯记录
	 * @param user 按灯用户
	 * @param args 其他参数
	 * @return
	 */
	public Serializable addPressLightRecord(PressLightRecord pressLightRecord,User user,Map<String,Object> args);
	/**
	 * 恢复按灯
	 * @param pressLightRecord
	 * @param user
	 * @param args
	 */
	public void recoverPressLightRecord(PressLightRecord pressLightRecord,User user,Map<String,Object> args);
	/**
	 * 熄灯
	 * @param pressLightRecord
	 * @param user
	 * @param args
	 */
	public void lightoutPressLightRecord(PressLightRecord pressLightRecord,User user,Map<String,Object> args);
	/**
	 * 确认
	 * @param pressLightRecord
	 * @param user
	 * @param args
	 */
	public void confirmPressLightRecord(PressLightRecord pressLightRecord,User user,Map<String,Object> args);
	/**
	 * 删除按灯记录
	 * @param pressLightRecord
	 */
	public void deletePressLightRecord(PressLightRecord pressLightRecord);
}
