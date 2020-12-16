package com.digitzones.mc.dao;

import java.util.List;

import com.digitzones.model.DeviceSite;
import com.digitzones.model.EquipmentDeviceSiteMapping;

public interface IMCEquipmentDeviceSiteMappingDao {
	
	/**
	 * 根据设备站点代码查找装备关联
	 */
	public List<EquipmentDeviceSiteMapping> findEquipmentDeviceSiteMappinglist(String deviceSiteCode,String hql);
	
	
	
	/**
	 * 根据设备站点代码查找设备站点id
	 */
	public List<DeviceSite> findDeviceSite(String deviceSiteCode);
}