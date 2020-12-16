package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IWorkpieceProcessDeviceSiteMappingDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkpieceProcessDeviceSiteMapping;
import com.digitzones.service.IWorkpieceProcessDeviceSiteMappingService;
@Service
public class WorkpieceProcessDeviceSiteMappingServiceImpl implements IWorkpieceProcessDeviceSiteMappingService {
	private IWorkpieceProcessDeviceSiteMappingDao workpieceProcessDeviceSiteMappingDao;
	@Autowired
	public void setWorkpieceProcessDeviceSiteMappingDao(
			IWorkpieceProcessDeviceSiteMappingDao workpieceProcessDeviceSiteMappingDao) {
		this.workpieceProcessDeviceSiteMappingDao = workpieceProcessDeviceSiteMappingDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workpieceProcessDeviceSiteMappingDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(WorkpieceProcessDeviceSiteMapping obj) {
		workpieceProcessDeviceSiteMappingDao.update(obj);
	}
	@Override
	public WorkpieceProcessDeviceSiteMapping queryByProperty(String name, String value) {
		return workpieceProcessDeviceSiteMappingDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(WorkpieceProcessDeviceSiteMapping obj) {
		return workpieceProcessDeviceSiteMappingDao.save(obj);
	}

	@Override
	public WorkpieceProcessDeviceSiteMapping queryObjById(Serializable id) {
		return workpieceProcessDeviceSiteMappingDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		workpieceProcessDeviceSiteMappingDao.deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager<WorkpieceProcessDeviceSiteMapping> queryOrAddWorkpieceProcessDeviceSiteMappingByWorkpieceId(
			Long workpieceId, Integer rows, Integer page) {
	/*	//查询'工件工序站点'表中是否存在该工件信息
		Long count = workpieceProcessDeviceSiteMappingDao.queryCountByWorkpieceId(workpieceId);
		//不存在
		if(count<=0) {
			//查询所有工序
			List<WorkpieceProcessMapping> wpmList = workpieceProcessMappingDao.queryByWorkpieceId(workpieceId);
			if(wpmList != null && wpmList.size()>0) {
				//将工序信息和站点信息添加到'工件工序站点'表中
				for(WorkpieceProcessMapping wpm : wpmList) {
					Processes p = wpm.getProcess();
					//根据工序查询站点
					List<ProcessDeviceSiteMapping> pdsmList = processDeviceSiteMappingDao.findByHQL("from ProcessDeviceSiteMapping pdsm where pdsm.processes.id=?0", new Object[] {p.getId()});
					for(ProcessDeviceSiteMapping pdsm : pdsmList) {
						DeviceSite ds = pdsm.getDeviceSite();
						//将关联添加到‘工件工序站点’
						WorkpieceProcessDeviceSiteMapping  wpdsm = new WorkpieceProcessDeviceSiteMapping();
						wpdsm.setDeviceSite(ds);
						wpdsm.setWorkpieceProcess(wpm);
						
						workpieceProcessDeviceSiteMappingDao.save(wpdsm);
					}
				}
			}
		}*/
		String hql = "from WorkpieceProcessDeviceSiteMapping wpdsm where wpdsm.workpieceProcess.workpiece.id=?0";
				//+ " and wpdsm.workpieceProcess.process.id in ()";tt
		return this.queryObjs(hql, page, rows, new Object[] {workpieceId});
	}
	@Override
	public WorkpieceProcessDeviceSiteMapping queryByWorkPieceIdAndProcessIdAndDeviceSiteId(Long workPieceId,
			Long processId, Long deviceSiteId) {
		List<WorkpieceProcessDeviceSiteMapping> list = workpieceProcessDeviceSiteMappingDao.findByHQL("from WorkpieceProcessDeviceSiteMapping wpdsm where wpdsm.workpieceProcess.workpiece.id=?0"
				+ "	and wpdsm.workpieceProcess.process.id=?1 and wpdsm.deviceSite.id=?2", new Object[] {workPieceId,processId,deviceSiteId});
		return (list!=null && list.size()>0)?list.get(0):null;
	}
	@Override
	public Float queryProcessingBeatByProductionUnitId(Long productionUnitId) {
		return workpieceProcessDeviceSiteMappingDao.queryProcessingBeatByProductionUnitId(productionUnitId);
	}
	@Override
	public Float queryProcessingBeatByClassesId(Long classesId) {
		return workpieceProcessDeviceSiteMappingDao.queryProcessingBeatByClassesId(classesId);
	}
	@Override
	public Float queryProcessingBeat(String workPieceCode, Long processId, Long deviceSiteId) {
		return workpieceProcessDeviceSiteMappingDao.queryProcessingBeat(workPieceCode, processId, deviceSiteId);
	}
}
