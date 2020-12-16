package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.service.ICommonService;
/**
 * 保养类别service
 * @author zdq
 * 2019年1月3日
 */
public interface IMaintenanceTypeService extends ICommonService<MaintenanceType> {
	/**
	 * 查找所有的保养类别
	 * @return
	 */
	public List<MaintenanceType> queryAllMaintenanceType();
	/**
	 * 保养类别是否正在使用
	 * @param typeId
	 * @return
	 */
	public boolean isInUse(Long typeId);

}
