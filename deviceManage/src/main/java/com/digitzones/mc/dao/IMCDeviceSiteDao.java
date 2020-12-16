package com.digitzones.mc.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.mc.model.MCDeviceSite;

/**
 * 
 * @author Administrator
 *
 */
public interface IMCDeviceSiteDao extends ICommonDao<MCDeviceSite>{
	
	/**
	 * 根据IP查找对应的设备信息
	 * @param IP
	 * @return
	 */
	List<MCDeviceSite> selectAllMCDeviceSiteByIP(String IP);
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
	
}
