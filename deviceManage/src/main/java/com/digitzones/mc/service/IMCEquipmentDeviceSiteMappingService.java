package com.digitzones.mc.service;

import java.util.List;

import com.digitzones.model.EquipmentDeviceSiteMapping;

public interface IMCEquipmentDeviceSiteMappingService {
	/**
	 * 根据设备站点代码查找装备关联
	 */
	public List<EquipmentDeviceSiteMapping> findEquipmentDeviceSiteMappinglist(String deviceSiteCode,String deviceSiteName);
	
	

	/**
	 * 根据设备站点代码查找设备站点id
	 */
	public Long findDeviceSite(String deviceSiteCode);
}
