package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceLevelDao;
import com.digitzones.devmgr.model.DeviceLevel;
@Repository
public class DeviceLevelDaoImpl extends CommonDaoImpl<DeviceLevel> implements IDeviceLevelDao {

	public DeviceLevelDaoImpl() {
		super(DeviceLevel.class);
	}

}
