package com.digitzones.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Device;
import org.springframework.web.multipart.MultipartFile;

/**
 * 设备service
 * @author zdq
 * 2018年6月11日
 */
public interface IDeviceService extends ICommonService<Device> {
	/**
	 * 根据设备站点查找设备
	 * @param deviceSiteId
	 * @return
	 */
	public Device queryDeviceByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据生产单元id查找设备
	 * @param productionUnitId
	 * @return
	 */
	public List<Device> queryDevicesByProductionUnitId(Long productionUnitId,String module);
	/**
	 * 添加设备
	 * @param device
	 * @param file 设备图片文件对象
	 * @return
	 */
	public Serializable addDevice(Device device,File file);
	/**
	 * 更新设备
	 * @param device
	 * @param photo 设备图片
	 */
	public void updateDevice(Device device,File photo);
	/**
	 * 查询设备下设备站点的数量
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
	/**
	 * 批量删除设备
	 * @param ids
	 * @return
	 */
	public boolean deleteDevices(String[] ids);
	/**
	 * 停用设备
	 * @param ids
	 */
	public void disableDevices(String[] ids);
	/**
	 * 查询所有设备信息
	 * @return
	 */
	public List<Device> queryAllDevices(String module);
	/**
	 * 导入设备
	 * @param list
	 */
	public void imports(List<Device> list)throws RuntimeException;
	/**
	 * 复制
	 */
	public void addCopyDevice(Device device,Long copyId);
	/**
	 * 根据生产单元id和模块值查询设备信息，
	 * @param id
	 * @param module 值：dimes,deviceManage
	 * @return
	 */
	public List<Device> queryDevicesByProductionUnitIdAndModule(Long id, String module);
	/**
	 * 设备管理端的设备删除
	 * @param mgrDeviceIds
	 */
	public void deleteMgrDevices(String[] mgrDeviceIds);

	/**
	 * 导入工件工序 参数信息
	 * @param file
	 */
    void importWorkpieceProcessParameters(MultipartFile file);
}
