package com.digitzones.dao;

import java.util.List;
import java.util.Map;

import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.Pager;
/**
 * 装备和设备站点关联dao
 * @author zdq
 * 2018年6月11日
 */
public interface IEquipmentDeviceSiteMappingDao extends ICommonDao<EquipmentDeviceSiteMapping> {
	/**
	 * 装备关联记录汇总
	 */
	public Pager<List<Object[]>> queryEquipmentCountReport(Map<String, String> params, int rows, int page);
}
