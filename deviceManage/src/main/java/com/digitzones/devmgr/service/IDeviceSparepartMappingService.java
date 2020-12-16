package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.model.Device;
import com.digitzones.service.ICommonService;
/**
 * 设备和备品备件关联实体service
 * @author zdq
 * 2019年2月27日
 */
public interface IDeviceSparepartMappingService extends ICommonService<DeviceSparepartMapping> {
	/**
	 * 为备品备件添加所属设备
	 * @param sparepart 备品备件对象
	 * @param deviceIds 设备id数组
	 */
	public void addDevices4Sparepart(Sparepart sparepart,String[] deviceIds);
	/**
	 * 为设备添加备品备件
	 * @param device 设备对象
	 * @param sparepartIds 备品备件id数组
	 */
	public void addSpareparts4Device(Device device,String[] sparepartIds);
	/**
	 * 更加设备id查询所有该设备与备件的关联项
	 */
	public List<DeviceSparepartMapping> queryDeviceSparepartMappingByDeviceId(Long deviceId);
	/**
	 * 根据备件ID和设备ID查询
	 * @param sparepartId
	 * @param deviceId
	 */
	public DeviceSparepartMapping queryBySparePartIDAndDeviceId(Long sparepartId ,Long deviceId);
}
