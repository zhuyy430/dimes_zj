package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.service.ICommonService;
/**
 * 项目数据访问接口
 * @author Administrator
 *
 */
public interface IDeviceProjectService extends ICommonService<DeviceProject>{
	/**
	 * 查询设备项目的信息
	 * @return
	 */
	List<DeviceProject> queryAllDeviceProjectByType(String type);
	/**
	 * 根据ID和类型，查询设备项目的信息
	 * @return
	 */
	DeviceProject queryDeviceProjectByCodeAndType(String code,String type);
	/**
	 * 查询projectTypeId是否存在设备项目
	 * @param projectTypeId
	 * @return
	 */
	boolean queryDeviceProjectByProjectTypeId(Long projectTypeId);
	/**
	 * 根据项目类型id查询项目信息
	 */
	public List<DeviceProject> queryAllDeviceProjectByProjectTypeId(Long projectTypeId);
	/**
	 * 信息导入
	 */
	public void imports(List<DeviceProject> dataList);
	List<DeviceProject> queryDevicesProjectByProjectTypeIdNotPage(String type, Long projectTypeId);
}
