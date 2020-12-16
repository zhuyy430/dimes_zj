package com.digitzones.mc.service;

import java.util.List;

import com.digitzones.mc.model.MCDeviceSite;

/**
 * mc端设备站点service
 * @author Administrator
 *
 */
public interface IMCDeviceSiteService {
	/**
	 * 查询所有可用的设备站点(用于mc端本地站点设置)
	 * @return
	 */
	public List<MCDeviceSite> queryAvailableDeviceSites(String clientIp);
	/**
	 * 查询所有可用的设备站点(用于mc端本地站点设置)
	 * @return
	 */
	public List<MCDeviceSite> queryAvailableDeviceSitesByCondition(String clientIp,String deviceName,String deviceCode,String productionUnitCode);

	/**
	 * 获取关联的设备信息
	 * @return
	 */
	public List<MCDeviceSite> getAllMCDeviceSite(String clientIp);
	/**
	 * 添加关联设备
	 * @param mcDeviceSites
	 */
	public void addMCDeviceSites(List<MCDeviceSite> mcDeviceSites);
	/**
	 * 删除关联设备
	 * @param mcdsids
	 */
	public void deleteMCDeviceSite(List<Long> ids);
	/**
	 * 根据设备站点编码查找MC端设备站点
	 * @param deviceSiteCode
	 * @return
	 */
	public MCDeviceSite queryByDeviceSiteCode(String deviceSiteCode);
}
