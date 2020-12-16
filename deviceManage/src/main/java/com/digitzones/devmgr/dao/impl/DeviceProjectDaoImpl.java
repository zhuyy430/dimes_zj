package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceProjectDao;
import com.digitzones.devmgr.model.DeviceProject;
@Repository
public class DeviceProjectDaoImpl extends CommonDaoImpl<DeviceProject> implements IDeviceProjectDao {
	public DeviceProjectDaoImpl() {
		super(DeviceProject.class);
	}
}
