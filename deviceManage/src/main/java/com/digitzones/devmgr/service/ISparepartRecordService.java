package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.SparepartRecord;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface ISparepartRecordService extends ICommonService<SparepartRecord>{
	/**
	 * 根据报修单id查询备件
	 */
	public List<SparepartRecord> querySparepartRecordBydeviceRepairOrderId(Long deviceRepairOrderId);
	/**
	 * 根据报修单id查询备件
	 */
	public void addSparepartRecords(Long deviceRepairId,String sparepartIds);
}
