package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceMaintenancePlanMappingDao;
import com.digitzones.devmgr.model.DeviceMaintenancePlanMapping;
@Repository
public class DeviceMaintenancePlanMappingDaoImpl extends CommonDaoImpl<DeviceMaintenancePlanMapping>
		implements IDeviceMaintenancePlanMappingDao {
	public DeviceMaintenancePlanMappingDaoImpl() {
		super(DeviceMaintenancePlanMapping.class);
	}
}
