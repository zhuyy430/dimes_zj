package com.digitzones.dao;

import com.digitzones.model.ProcessDeviceSiteMapping;

/**
 * 工序和设备站点关联dao
 * @author zdq
 * 2018年6月14日
 */
public interface IProcessDeviceSiteMappingDao extends ICommonDao<ProcessDeviceSiteMapping> {
	/**
	 * 根据工序id和设备站点id删除关联
	 * @param processesId 工序id
	 * @param deviceSiteId 设备站点id
	 */
	public void deleteByProcessesIdAndDeviceSiteId(Long processesId,Long deviceSiteId);
}
