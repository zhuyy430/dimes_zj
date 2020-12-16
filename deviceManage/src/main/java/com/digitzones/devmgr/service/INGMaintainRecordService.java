package com.digitzones.devmgr.service;

import com.digitzones.devmgr.model.NGMaintainRecord;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 设备关联项目接口
 */
public interface INGMaintainRecordService extends ICommonService<NGMaintainRecord>{
	/**
	 * 通过报修单id查询故障原因
	 * @param deviceRepairOrderId
	 * @return
	 */
	public List<NGMaintainRecord> queryNGMaintainRecordByDeviceRepairOrderId(Long deviceRepairOrderId);

	/**
	 * 通过报修单id查询故障原因
	 * @param deviceRepairOrderId
	 * @return
	 */
	public NGMaintainRecord queryNGMaintainRecordByDeviceRepairOrderIdAnddeviceProjectId(Long deviceRepairOrderId,Long deviceProjectId);
	/**
	 * 根据DeviceProject的id查询是否有对应的故障原因
	 */
	public List<NGMaintainRecord> queryNGMaintainRecordByDeviceProjectId(Long deviceProjectId);
}
