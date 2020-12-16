package com.digitzones.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IProcessesParametersMappingDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessesParametersMapping;
import com.digitzones.service.IProcessesParametersMappingService;
@Service
public class ProcessesParametersMappingServiceImpl implements IProcessesParametersMappingService {
	private IProcessesParametersMappingDao processesParametersMappingDao;
	@Autowired
	public void setProcessesParametersMappingDao(IProcessesParametersMappingDao processesParametersMappingDao) {
		this.processesParametersMappingDao = processesParametersMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return processesParametersMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ProcessesParametersMapping obj) {
		processesParametersMappingDao.update(obj);
	}

	@Override
	public ProcessesParametersMapping queryByProperty(String name, String value) {
		return processesParametersMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ProcessesParametersMapping obj) {
		return processesParametersMappingDao.save(obj);
	}

	@Override
	public ProcessesParametersMapping queryObjById(Serializable id) {
		return processesParametersMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		processesParametersMappingDao.deleteById(id);
	}

	@Override
	public void deleteByProcessesIdAndParameterId(Long processesId, Long parameterId) {
		processesParametersMappingDao.deleteByProcessesIdAndParameterId(processesId, parameterId);
	}

}
