package com.digitzones.service;

import java.util.List;

import com.digitzones.model.DeviceSiteParameterMapping;
/**
 * 站点和参数关联service
 * @author zdq
 * 2018年6月26日
 */
public interface IDeviceSiteParameterMappingService extends ICommonService<DeviceSiteParameterMapping> {
	/**
	 * 根据设备站点查询设备参数
	 * @param deviceSiteId
	 * @return
	 */
	public List<DeviceSiteParameterMapping> queryByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据设备站点id列表查询设备站点参数映射信息
	 * @param deviceSiteIdList 设备站点id列表
	 * @return
	 */
	public List<DeviceSiteParameterMapping> queryByDeviceSiteIds(List<Long> deviceSiteIdList);
	/**
	 * 为设备添加 参数
	 * @param deviceId
	 * @param deviceSiteParameter
	 */
	public void addDeviceSiteParameterMappings(Long deviceId,DeviceSiteParameterMapping deviceSiteParameter);
}
