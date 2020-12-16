package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceCheckingPlanMappingDao;
import com.digitzones.devmgr.model.DeviceCheckingPlanMapping;
@Repository
public class DeviceCheckingPlanMappingDaoImpl extends CommonDaoImpl<DeviceCheckingPlanMapping>
		implements IDeviceCheckingPlanMappingDao {
	public DeviceCheckingPlanMappingDaoImpl() {
		super(DeviceCheckingPlanMapping.class);
	}
}
