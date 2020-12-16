package com.digitzones.service;

import java.util.Date;

import com.digitzones.model.QualityCalendarRecord;
/**
 * 质量日历记录service
 * @author zdq
 * 2018年7月19日
 */
public interface IQualityCalendarRecordService extends ICommonService<QualityCalendarRecord> {
	/**
	 * 根据时间和类型查询质量投诉数
	 * @param day
	 * @param typeId
	 * @return
	 */
	public Integer queryCountByDayAndTypeId(Date day,Long typeId);
}
