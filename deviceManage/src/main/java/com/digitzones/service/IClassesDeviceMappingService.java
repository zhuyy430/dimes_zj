package com.digitzones.service;

import com.digitzones.model.ClassesDeviceMapping;
/**
 * 班次和设备关联折率i额
 * @author zdq
 * 2018年6月13日
 */
public interface IClassesDeviceMappingService extends ICommonService<ClassesDeviceMapping> {
	/**
	 * 根据班次id和设备id删除关联记录
	 * @param classesId
	 * @param deviceId
	 */
	public void deleteByClassesIdAndDeviceId(Long classesId,Long deviceId);
}
