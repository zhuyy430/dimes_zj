package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IDeviceSiteParameterMappingDao;
import com.digitzones.model.DeviceSiteParameterMapping;
@Repository
public class DeviceSiteParameterMappingDaoImpl extends CommonDaoImpl<DeviceSiteParameterMapping> implements IDeviceSiteParameterMappingDao{
	public DeviceSiteParameterMappingDaoImpl() {
		super(DeviceSiteParameterMapping.class);
	}
}
