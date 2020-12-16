package com.digitzones.service;

import java.util.List;
import java.util.Map;

import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.Pager;
/**
 * 装备和设备站点关联service
 * @author zdq
 * 2018年6月11日
 */
public interface IMeasuringToolDeviceSiteMappingService extends ICommonService<EquipmentDeviceSiteMapping> {
	/**
	 * 根据序列号查询装备和设备的关联
	 * @param no
	 * @return
	 */
	public EquipmentDeviceSiteMapping queryByNo(String no);


	/**
	 * 根据序列号查询装备和设备的关联
	 * @return
	 */
	public EquipmentDeviceSiteMapping queryByEquipmentCode(String code);


	/**
	 * 获取站点关联的量具
	 * @param deviceSiteCode
	 */
	public List<EquipmentDeviceSiteMapping> queryByDeviceSiteCode(String deviceSiteCode,String searchText);
	/**
	 * 替换关联量具
	 * @param deviceSiteCode
	 */
	public List<EquipmentDeviceSiteMapping> repalce(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping);
	/**
	 * 量具关联记录汇总表
	 */
	public Pager<List<Object[]>> queryMeasuringCountReport(Map<String,String> params,int rows,int page);
}
