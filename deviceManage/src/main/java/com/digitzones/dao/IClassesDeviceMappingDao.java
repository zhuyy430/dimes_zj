package com.digitzones.dao;

import com.digitzones.model.ClassesDeviceMapping;
/**
 * 班次和设备关联dao
 * @author zdq
 * 2018年6月13日
 */
public interface IClassesDeviceMappingDao extends ICommonDao<ClassesDeviceMapping> {
	/**
	 * 根据班次id和设备id删除关联记录
	 * @param classesId 班次id
	 * @param deviceId 设备id
	 */
	public void deleteByClassIdAndDeviceId(Long classesId,Long deviceId);
}
