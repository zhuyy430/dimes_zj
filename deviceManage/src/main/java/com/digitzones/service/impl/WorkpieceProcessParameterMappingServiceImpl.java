package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IWorkpieceProcessParameterMappingDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkpieceProcessParameterMapping;
import com.digitzones.service.IWorkpieceProcessParameterMappingService;
import org.springframework.util.CollectionUtils;

@Service
public class WorkpieceProcessParameterMappingServiceImpl implements IWorkpieceProcessParameterMappingService {
	private IWorkpieceProcessParameterMappingDao workpieceProcessParameterMappingDao;
	@Autowired
	public void setWorkpieceProcessParameterMappingDao(
			IWorkpieceProcessParameterMappingDao workpieceProcessParameterMappingDao) {
		this.workpieceProcessParameterMappingDao = workpieceProcessParameterMappingDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workpieceProcessParameterMappingDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(WorkpieceProcessParameterMapping obj) {
		workpieceProcessParameterMappingDao.update(obj);
	}
	@Override
	public WorkpieceProcessParameterMapping queryByProperty(String name, String value) {
		return workpieceProcessParameterMappingDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(WorkpieceProcessParameterMapping obj) {
		return workpieceProcessParameterMappingDao.save(obj);
	}

	@Override
	public WorkpieceProcessParameterMapping queryObjById(Serializable id) {
		return workpieceProcessParameterMappingDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		workpieceProcessParameterMappingDao.deleteById(id);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Pager<WorkpieceProcessParameterMapping> queryWorkpieceProcessParameterMappingByWorkpieceCode(
			String workpieceCode, Integer rows, Integer page) {
	/*	//查询'工件工序站点'表中是否存在该工件信息
		Long count = workpieceProcessParameterMappingDao.queryCountByWorkpieceId(workpieceId);
		//不存在
		if(count<=0) {
			//查询所有工序
			List<WorkpieceProcessMapping> wpmList = workpieceProcessMappingDao.queryByWorkpieceId(workpieceId);
			if(wpmList != null && wpmList.size()>0) {
				//将工序信息和站点信息添加到'工件工序站点'表中
				for(WorkpieceProcessMapping wpm : wpmList) {
					Processes p = wpm.getProcess();
					//根据工序查询站点
					List<ProcessParameterMapping> pdsmList = processParameterMappingDao.findByHQL("from ProcessParameterMapping pdsm where pdsm.processes.id=?0", new Object[] {p.getId()});
					for(ProcessParameterMapping pdsm : pdsmList) {
						Parameter ds = pdsm.getParameter();
						//将关联添加到‘工件工序站点’
						WorkpieceProcessParameterMapping  wpdsm = new WorkpieceProcessParameterMapping();
						wpdsm.setParameter(ds);
						wpdsm.setWorkpieceProcess(wpm);
						
						workpieceProcessParameterMappingDao.save(wpdsm);
					}
				}
			}
		}*/
		return this.queryObjs("from WorkpieceProcessParameterMapping wpdsm where wpdsm.workpieceProcess.inventory.code=?0", page, rows, new Object[] {workpieceCode});
	}
	@Override
	public List<WorkpieceProcessParameterMapping> queryAllWorkpieceProcessParameterMappingByWorkpieceCodeAndProcessCode(String workpieceCode ,String processCode) {
		return workpieceProcessParameterMappingDao.findByHQL("from WorkpieceProcessParameterMapping wpdsm where wpdsm.workpieceProcess.inventory.code=?0 and"
				+ " wpdsm.workpieceProcess.process.code=?1", new Object[] {workpieceCode,processCode});
	}
	@Override
	public List<WorkpieceProcessParameterMapping> queryAllWorkpieceProcessParameterMappingByWorkpieceCode(String workpieceCode) {
		return workpieceProcessParameterMappingDao.findByHQL("from WorkpieceProcessParameterMapping wpdsm where wpdsm.workpieceProcess.inventory.code=?0", new Object[] {workpieceCode});
	}

	@Override
	public List<WorkpieceProcessParameterMapping> queryByWorkpieceCodeAndProcessCode(String workpieceCode,
			String processCode) {
		String hql = "from WorkpieceProcessParameterMapping wppm where wppm.workpieceProcess.inventory.code=?0 and wppm.workpieceProcess.process.code=?1";
		return workpieceProcessParameterMappingDao.findByHQL(hql,new Object[] {workpieceCode,processCode});
	}

	/**
	 * 根据工单编号和工序代码查找工件工序参数信息
	 *
	 * @param no
	 * @param processCode
	 * @return
	 */
	@Override
	public List<WorkpieceProcessParameterMapping> queryByNoAndProcessCode(String no, String processCode) {
		return workpieceProcessParameterMappingDao.queryByNoAndProcessCode(no,processCode);
	}

	/**
	 * 根据工件代码，工序代码和参数代码查找
	 *
	 * @param workpieceCode
	 * @param processCode
	 * @param parameterCode
	 * @return
	 */
	@Override
	public WorkpieceProcessParameterMapping queryByWorkpieceCodeAndProcessCodeAndParameterCode(String workpieceCode, String processCode, String parameterCode) {
		String hql = "from WorkpieceProcessParameterMapping wppm where wppm.workpieceProcess.inventory.code=?0 and " +
				" wppm.workpieceProcess.process.code=?1 and wppm.parameter.code=?2";
		List<WorkpieceProcessParameterMapping> list = workpieceProcessParameterMappingDao.findByHQL(hql,new Object[]{workpieceCode,processCode,parameterCode});
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}
}
