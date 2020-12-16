package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceSparepartRecordDao;
import com.digitzones.devmgr.model.DeviceSparepartRecord;
@Repository
public class DeviceSparepartRecordDaoImpl extends CommonDaoImpl<DeviceSparepartRecord> implements IDeviceSparepartRecordDao {
	public DeviceSparepartRecordDaoImpl() {
		super(DeviceSparepartRecord.class);
	}
}
