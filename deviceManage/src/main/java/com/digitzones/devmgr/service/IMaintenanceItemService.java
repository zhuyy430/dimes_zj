package com.digitzones.devmgr.service;
import java.util.List;

import com.digitzones.devmgr.model.MaintenanceItem;
import com.digitzones.service.ICommonService;
public interface IMaintenanceItemService extends ICommonService<MaintenanceItem> {
	/**
	 * 添加保养项目记录
	 * @param maintenancePlanRecordId
	 * @param itemIds
	 */
	public void addMaintenanceItems(Long maintenancePlanRecordId, String itemIds);
	/**
	 * 添加保养项目记录
	 * @param maintenancePlanRecordId
	 * @param itemIds
	 * @param typeCode 保养等级
	 */
	public void addMaintenanceItemsByType(Long maintenancePlanRecordId, String itemIds,String typeCode);
	/**
	 * 确认保养项目
	 * @param item
	 */
	public void confirm(MaintenanceItem item);
	/**
	 * 根据保养项目计划记录id查询保养项目
	 */
	public List<MaintenanceItem> queryMaintenanceItemByMaintenancePlanRecordId(Long Id);

	/**
	 * 根据点检记录id查找点检过的记录项数量
	 */
	public int queryResultCountByMaintenanceItemId(Long maintenancePlanRecordId);
}
