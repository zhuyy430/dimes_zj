package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IEquipmentDeviceSiteMappingLogDao;
import com.digitzones.model.EquipmentDeviceSiteMappingLog;
@Repository
public class EquipmentDeviceSiteMappingLogDaoImpl extends CommonDaoImpl<EquipmentDeviceSiteMappingLog>
		implements IEquipmentDeviceSiteMappingLogDao {

	public EquipmentDeviceSiteMappingLogDaoImpl() {
		super(EquipmentDeviceSiteMappingLog.class);
	}
}
