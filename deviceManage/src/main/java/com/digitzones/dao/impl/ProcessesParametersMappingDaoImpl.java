package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IProcessesParametersMappingDao;
import com.digitzones.model.ProcessesParametersMapping;
@Repository
public class ProcessesParametersMappingDaoImpl extends CommonDaoImpl<ProcessesParametersMapping> implements IProcessesParametersMappingDao {

	public ProcessesParametersMappingDaoImpl() {
		super(ProcessesParametersMapping.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteByProcessesIdAndParameterId(Long processesId, Long parameterId) {
		this.getSession().createSQLQuery("delete from PROCESSES_PARAMETERS  where PROCESS_ID=?0 and PARAMETER_ID=?1")
		.setParameter(0, processesId)
		.setParameter(1, parameterId)
		.executeUpdate();
	}
}
