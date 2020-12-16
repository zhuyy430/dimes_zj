package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceSparepartMappingDao;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
@Repository
public class DeviceSparepartMappingDaoImpl extends CommonDaoImpl<DeviceSparepartMapping>
		implements IDeviceSparepartMappingDao {
	public DeviceSparepartMappingDaoImpl() {
		super(DeviceSparepartMapping.class);
	}
}
