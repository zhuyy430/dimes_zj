package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.dao.IProcessTypeDao;
import com.digitzones.dao.IProcessesDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessType;
import com.digitzones.model.Processes;
import com.digitzones.service.IProcessesService;
@Service
public class ProcessesServiceImpl implements IProcessesService {
	private IProcessesDao processesDao;
	@Autowired
	private IProcessTypeDao processTypeDao;
	@Autowired
	public void setProcessesDao(IProcessesDao processesDao) {
		this.processesDao = processesDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return processesDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Processes obj) {
		processesDao.update(obj);
	}

	@Override
	public Processes queryByProperty(String name, String value) {
		return processesDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Processes obj) {
		return processesDao.save(obj);
	}

	@Override
	public Processes queryObjById(Serializable id) {
		return processesDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		Processes p = processesDao.findById(id);
		ProcessType pt = p.getProcessType();
		processesDao.deleteById(id);
		if(pt!=null) {
			List<ProcessType> list = processTypeDao.findByHQL("from Processes p where p.processType.id=?0",new Object[] {pt.getId()});
			if(CollectionUtils.isEmpty(list)) {
				processTypeDao.delete(pt);
			}
		}
	}
	@Override
	public List<Processes> queryProcessByWorkpieceCodeAndDeviceSiteId(String workpieceCode, Long deviceSiteId) {
		String hql = "select wpdsm.workpieceProcess.process from WorkpieceProcessDeviceSiteMapping wpdsm where wpdsm.workpieceProcess.inventory.code=?0 and wpdsm.deviceSite.id=?1";
		return processesDao.findByHQL(hql, new Object[] {workpieceCode,deviceSiteId});
	}

	@Override
	public List<Processes> queryAllProcesses() {
		return processesDao.findAll();
	}

	@Override
	public List<Processes> queryOtherProcessesByEmployeeId(Long employeeId) {
		String hql = "select d from Processes d where d.id not in (select cdm.process.id from EmployeeProcessMapping cdm"
				+ "  where  cdm.employee.id=?0)";
		return processesDao.findByHQL(hql, new Object[] {employeeId});
	}

	@Override
	public List<Processes> queryOtherProcessesByEmployeeIdAndCondition(Long employeeId, String q) {
		String hql = "select d from Processes d where d.id not in (select cdm.process.id from EmployeeProcessMapping cdm"
				+ "  where  cdm.employee.id=?0 and (d.code like ?1 or d.name like ?1 or d.processType.name like ?1))";
		return processesDao.findByHQL(hql, new Object[] {employeeId,"%" + q + "%"});
	}

	@Override
	public List<Processes> queryProcessesByTypeId(Long processTypeId) {
		return processesDao.findByHQL("from Processes p where p.processType.id=?0", new Object[] {processTypeId});
	}

	@Override
	public List<Processes> queryProcessesByCraftsId(String CraftsId) {
		return processesDao.findByHQL("select p from CraftsRouteProcessMapping wpm inner join wpm.process p where wpm.craftsRoute.id=?0  order by wpm.processRoute asc", new Object[] {CraftsId});
	}
}
