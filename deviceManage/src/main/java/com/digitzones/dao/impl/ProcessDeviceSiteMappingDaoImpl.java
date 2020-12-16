package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProcessDeviceSiteMappingDao;
import com.digitzones.model.ProcessDeviceSiteMapping;
@Repository
public class ProcessDeviceSiteMappingDaoImpl extends CommonDaoImpl<ProcessDeviceSiteMapping> implements IProcessDeviceSiteMappingDao {

	public ProcessDeviceSiteMappingDaoImpl() {
		super(ProcessDeviceSiteMapping.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteByProcessesIdAndDeviceSiteId(Long processesId, Long deviceSiteId) {
		this.getSession().createSQLQuery("delete from PROCESS_DEVICESITE  where PROCESS_ID=?0 and DEVICESITE_ID=?1")
		.setParameter(0, processesId)
		.setParameter(1, deviceSiteId)
		.executeUpdate();
	}
}
