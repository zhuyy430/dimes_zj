package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.dao.ICraftsRouteProcessMappingDao;
import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.model.CraftsRouteProcessMapping;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Pager;
import com.digitzones.model.Processes;
import com.digitzones.procurement.model.CraftsRoute;
import com.digitzones.service.ICraftsRouteProcessMappingService;
import com.digitzones.service.IProcessesService;
@Service
public class CraftsRouteProcessMappingServiceImpl implements ICraftsRouteProcessMappingService {
	@Autowired
	private ICraftsRouteProcessMappingDao craftsRouteProcessMappingDao;
	@Autowired
	public IProcessesService processesService;
	@Autowired
	public IDeviceSiteDao deviceSiteDao;


	@Override
	public void deleteByCraftsIdAndProcessId(String craftsId, Long processId) {
		CraftsRouteProcessMapping w=queryObjById(processId);
		Long processRoute=w.getProcessRoute();
		String hql="from CraftsRouteProcessMapping wpm where wpm.craftsRoute.id=?0 and wpm.processRoute>?1 ";
		List<CraftsRouteProcessMapping> xgwpms=craftsRouteProcessMappingDao.findByHQL(hql, new Object[] {craftsId,processRoute});
		for(int i=0;i<xgwpms.size();i++) {
			CraftsRouteProcessMapping  upwpm=new CraftsRouteProcessMapping();
			upwpm=xgwpms.get(i);
			upwpm.setProcessRoute(xgwpms.get(i).getProcessRoute()-1);
			craftsRouteProcessMappingDao.update(upwpm);
		}
		craftsRouteProcessMappingDao.deleteByCraftsIdAndProcessId(processId);
		
	}

	@Override
	public CraftsRouteProcessMapping queryByCraftsIdAndProcessId(String craftsId, Long processId) {
		return craftsRouteProcessMappingDao.findByHQL("from CraftsRouteProcessMapping wpm where wpm.craftsRoute.id=?0 and wpm.process.id=?1", new Object[] {craftsId,processId}).get(0);
	}

	@Override
	public List<CraftsRouteProcessMapping> queryByCraftsId(String craftsId) {
		return craftsRouteProcessMappingDao.findByHQL("from CraftsRouteProcessMapping wpm where wpm.craftsRoute.id=?0 order by wpm.processRoute desc", new Object[] {craftsId});
	}

	@Override
	public void addCraftsProcessMapping(String CraftsId,String processesId) {
		if(processesId.contains("[")) {
			processesId = processesId.replace("[", "");
		}
		if(processesId.contains("]")) {
			processesId = processesId.replace("]", "");
		}

		String[] idArray = processesId.split(",");
		
		List<CraftsRouteProcessMapping> nwpm=queryByCraftsId(CraftsId);
		Long processRoute=Long.valueOf(0);
		if(nwpm!=null&&nwpm.size()>0) {
			 processRoute=nwpm.get(0).getProcessRoute();
		}
		for(int i = 0;i<idArray.length;i++) {
			//添加工序的设备站点
			String hql = "select distinct d from CraftsRouteProcessMapping c inner join ProcessDeviceSiteMapping p "
					+ "on c.process.id=p.processes.id inner join DeviceSite d on p.deviceSite.id=d.id where c.craftsRoute.id=?0 and d.code in("
					+ "select ds.code from ProcessDeviceSiteMapping pdm inner join  pdm.deviceSite ds  inner join pdm.processes p where p.id=?1)";
			List<DeviceSite> list = deviceSiteDao.findByHQL(hql,new Object[] {CraftsId,Long.valueOf(idArray[i])});
			if(!CollectionUtils.isEmpty(list)){
				throw new RuntimeException("工序存在重复设备站点，请重新选择");
			}
			Processes device = new Processes();
			CraftsRouteProcessMapping cdm = new CraftsRouteProcessMapping();
			device=processesService.queryObjById(Long.valueOf(idArray[i]));
			if(device!=null){
				cdm.setProcess(device);
			}else {
				throw new RuntimeException("工序发生改变，请重新选择");
			}
			//device.setId(Long.valueOf(idArray[i]));
			CraftsRoute c = new CraftsRoute();
			c.setId(CraftsId);
			processRoute=processRoute+1;
			
			
			cdm.setCraftsRoute(c);
			cdm.setProcessRoute(processRoute);
			
			addObj(cdm);	
		}
	}

	@Override
	public void updateShiftUpProcessRoute(String CraftsId,Long processId,String c_pMappingId) {
		CraftsRouteProcessMapping upw=queryObjById(Long.parseLong(c_pMappingId));
		Long processRoute=upw.getProcessRoute();
		if(processRoute<=1) {
			throw new RuntimeException("已经是第一道工序");
		}else {
			String hql="from CraftsRouteProcessMapping wpm where wpm.craftsRoute.id=?0 and wpm.processRoute=?1 ";
			CraftsRouteProcessMapping downw=craftsRouteProcessMappingDao.findByHQL(hql, new Object[] {CraftsId,processRoute-1}).get(0);
			upw.setProcessRoute(processRoute-1);
			downw.setProcessRoute(processRoute);
			craftsRouteProcessMappingDao.update(upw);
			craftsRouteProcessMappingDao.update(downw);
		}
		
		
	}

	@Override
	public void updateShiftDownProcessRoute(String CraftsId,Long processId,String c_pMappingId) {
		CraftsRouteProcessMapping downw=queryObjById(Long.parseLong(c_pMappingId));
		Long processRoute=downw.getProcessRoute();
		
	
		String hql="from CraftsRouteProcessMapping wpm where wpm.craftsRoute.id=?0 and wpm.processRoute=?1 ";
		List<CraftsRouteProcessMapping> upwList=craftsRouteProcessMappingDao.findByHQL(hql, new Object[] {CraftsId,processRoute+1});
		if(upwList!=null&&upwList.size()>0) {
			CraftsRouteProcessMapping upw=upwList.get(0);
			upw.setProcessRoute(processRoute);
			downw.setProcessRoute(processRoute+1);
			craftsRouteProcessMappingDao.update(upw);
			craftsRouteProcessMappingDao.update(downw);
		}else {
			throw new RuntimeException("已经是最后一道工序");
		}
		
	}

	@Override
	public List<CraftsRouteProcessMapping> queryByProcessId(Long ProcessId) {
		 return craftsRouteProcessMappingDao.findByHQL("from CraftsRouteProcessMapping wpm where wpm.process.id=?0 order by wpm.processRoute desc", new Object[] {ProcessId});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return craftsRouteProcessMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(CraftsRouteProcessMapping obj) {
		craftsRouteProcessMappingDao.update(obj);
	}

	@Override
	public CraftsRouteProcessMapping queryByProperty(String name, String value) {
		return craftsRouteProcessMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(CraftsRouteProcessMapping obj) {
		return craftsRouteProcessMappingDao.save(obj);
	}

	@Override
	public CraftsRouteProcessMapping queryObjById(Serializable id) {
		return craftsRouteProcessMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		craftsRouteProcessMappingDao.deleteById(id);
	}
		
	
}
