package com.digitzones.dao;

import java.io.File;
import java.io.Serializable;

import com.digitzones.model.Device;
/**
 * 设备dao
 * @author zdq
 * 2018年6月11日
 */
public interface IDeviceDao extends ICommonDao<Device> {
	/**
	 * 添加设备
	 * @param device
	 * @param photo 设备图片
	 */
	public Serializable addDevice(Device device,File photo);
	/**
	 * 更新设备
	 * @param device
	 * @param photo 设备图片
	 */
	public void updateDevice(Device device,File photo);
	/**
	 * 根据设备ID查找设备站点数
	 * @param deviceId
	 * @return
	 */
	public Integer queryDeviceSiteCountByDeviceId(Long deviceId);
	/**
	 * 根据设备ID查找班次数
	 * @param deviceId
	 * @return
	 */
	public Integer queryClassesCountByDeviceId(Long deviceId);
}
