package com.digitzones.service;

import java.util.List;

import com.digitzones.model.QualityCalendarType;
/**
 * 质量日历类别service
 * @author zdq
 * 2018年7月19日
 */
public interface IQualityCalendarTypeService extends ICommonService<QualityCalendarType> {
	/**
	 * 查询所有质量日历类型
	 * @return
	 */
	public List<QualityCalendarType> queryAllQualityCalendarTypes();
}
