package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.DeviceSparepartRecord;
import com.digitzones.service.ICommonService;
/**
 * 备品备件service
 * @author zdq
 * 2018年12月5日
 */
public interface IDeviceSparepartRecordService extends ICommonService<DeviceSparepartRecord>{
	
	public List<DeviceSparepartRecord> queryDeviceSparepartRecordByDeviceId(Long deviceId);
}
