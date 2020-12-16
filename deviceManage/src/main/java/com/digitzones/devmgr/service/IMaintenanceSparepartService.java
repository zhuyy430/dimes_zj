package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.MaintenanceSparepart;
import com.digitzones.service.ICommonService;
/**
 * 保养备件service
 * @author zdq
 * 2019年1月4日
 */
public interface IMaintenanceSparepartService extends ICommonService<MaintenanceSparepart> {
	/**
	 * 添加保养备件记录
	 * @param maintenancePlanRecordId
	 * @param sparepartIds
	 */
	public void addMaintenanceSpareparts(Long maintenancePlanRecordId, String sparepartIds,String count);
	/**
	 * 根据保养记录Id获取备件信息
	 * @param id
	 * @return
	 */
	public List<MaintenanceSparepart> queryMaintenanceSparepartByRecordId(Long id);

}
