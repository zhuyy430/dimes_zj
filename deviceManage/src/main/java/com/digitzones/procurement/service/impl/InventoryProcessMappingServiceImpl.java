package com.digitzones.procurement.service.impl;

import com.digitzones.model.DeviceSite;
import com.digitzones.model.Pager;
import com.digitzones.model.Processes;
import com.digitzones.procurement.dao.IInventoryProcessMappingDao;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.model.InventoryProcessMapping;
import com.digitzones.procurement.service.IInventoryProcessMappingService;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IProcessDeviceSiteMappingService;
import com.digitzones.service.IProcessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
@Service
public class InventoryProcessMappingServiceImpl implements IInventoryProcessMappingService {
	@Autowired
	private IInventoryProcessMappingDao inventoryProcessMappingDao;
	@Autowired
	public IProcessesService processesService;
	@Autowired
	public IProcessDeviceSiteMappingService processDeviceSiteMappingService;
	@Autowired
	public IDeviceSiteService deviceSiteService;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return inventoryProcessMappingDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(InventoryProcessMapping obj) {
		inventoryProcessMappingDao.update(obj);
	}
	@Override
	public InventoryProcessMapping queryByProperty(String name, String value) {
		return inventoryProcessMappingDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(InventoryProcessMapping obj) {
		return inventoryProcessMappingDao.save(obj);
	}
	@Override
	public InventoryProcessMapping queryObjById(Serializable id) {
		return inventoryProcessMappingDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		inventoryProcessMappingDao.deleteById(id);
	}
	@Override
	public void deleteByInventoryCodeAndProcessId(String InventoryCode, Long processId) {
		InventoryProcessMapping w=queryObjById(processId);
		Long processRoute=w.getProcessRoute();
		String hql="from InventoryProcessMapping wpm where wpm.inventory.id=?0 and wpm.processRoute>?1 ";
		List<InventoryProcessMapping> xgwpms=inventoryProcessMappingDao.findByHQL(hql, new Object[] {InventoryCode,processRoute});
		for(int i=0;i<xgwpms.size();i++) {
			InventoryProcessMapping  upwpm=new InventoryProcessMapping();
			upwpm=xgwpms.get(i);
			upwpm.setProcessRoute(xgwpms.get(i).getProcessRoute()-1);
			inventoryProcessMappingDao.update(upwpm);
		}
		inventoryProcessMappingDao.deleteByInventoryCodeAndProcessId(processId);
		
	}

	@Override
	public void deleteByInventoryCode(String InventoryCode) {
		inventoryProcessMappingDao.deleteByInventoryCode(InventoryCode);
		
	}

	@Override
	public InventoryProcessMapping queryByInventoryCodeAndProcessId(String InventoryCode, Long processId) {
		List<InventoryProcessMapping> list = inventoryProcessMappingDao.findByHQL("from InventoryProcessMapping wpm where wpm.inventory.id=?0 and wpm.process.id=?1", new Object[] {InventoryCode,processId});
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<InventoryProcessMapping> queryByInventoryCode(String InventoryCode) {
		return inventoryProcessMappingDao.findByHQL("from InventoryProcessMapping wpm where wpm.inventory.id=?0 order by wpm.processRoute desc", new Object[] {InventoryCode});
	}

	@Override
	public void addInventoryProcessMapping(String InventoryCode, String processesId,Boolean isCraftsRoute) {
		if(processesId.contains("[")) {
			processesId = processesId.replace("[", "");
		}
		if(processesId.contains("]")) {
			processesId = processesId.replace("]", "");
		}

		String[] idArray = processesId.split(",");
		
		if(isCraftsRoute){
			deleteByInventoryCode(InventoryCode);
		}
		List<InventoryProcessMapping> nwpm=queryByInventoryCode(InventoryCode);
		Long processRoute=Long.valueOf(0);
		if(nwpm!=null&&nwpm.size()>0) {
			 processRoute=nwpm.get(0).getProcessRoute();
		}
		for(int i=0;i<nwpm.size();i++){
			for(int j=0;j<idArray.length;j++){
				if(nwpm.get(i).getProcess().getId()==Long.valueOf(idArray[j])){
					throw new RuntimeException("同一个物料下的工序不允许重复，请重新选择");
				}
			}
		}





		for(int i = 0;i<idArray.length;i++) {
			Processes device = new Processes();
			InventoryProcessMapping cdm = new InventoryProcessMapping();
			//InventoryProcessMapping exist = queryByInventoryCodeAndProcessId(InventoryCode, Long.valueOf(idArray[i]));
			device=processesService.queryObjById(Long.valueOf(idArray[i]));
			if(device!=null){
				cdm.setProcess(device);
			}else {
				throw new RuntimeException("工序发生改变，请重新选择");
			}


			List<InventoryProcessMapping> ipm=queryByInventoryCode(InventoryCode);
			for(int j=0;j<ipm.size();j++){
				if(ipm.get(j).getProcess().getId()==(Long.valueOf(idArray[i]))){
					throw new RuntimeException("同一个物料下的工序不允许重复，请重新选择");
				}
			}



			List<DeviceSite> allDevice=deviceSiteService.queryDeviceSiteByInventoryCodeMappingDeviceSite(InventoryCode);

			List<DeviceSite> deviceSite=deviceSiteService.queryDeviceSiteByProcessId(Long.valueOf(idArray[i]));
			for(int j=0;j<allDevice.size();j++){
				if(allDevice.get(j).getCode().equals(deviceSite.get(0))){
					throw new RuntimeException("工序的设备站点不允许相同，请重新选择");
				}
			}


			//device.setId(Long.valueOf(idArray[i]));
			Inventory c = new Inventory();
			c.setCode(InventoryCode);
			processRoute=processRoute+1;
			
			
			cdm.setInventory(c);
			cdm.setProcessRoute(processRoute);
			
			addObj(cdm);	
		}
	}
	@Override
	public void updateShiftUpProcessRoute(String InventoryCode, Long processId) {
		InventoryProcessMapping upw=queryObjById(processId);
		Long processRoute=upw.getProcessRoute();
		if(processRoute<=1) {
			throw new RuntimeException("已经是第一道工序");
		}else {
			String hql="from InventoryProcessMapping wpm where wpm.inventory.id=?0 and wpm.processRoute=?1 ";
			InventoryProcessMapping downw=inventoryProcessMappingDao.findByHQL(hql, new Object[] {InventoryCode,processRoute-1}).get(0);
			upw.setProcessRoute(processRoute-1);
			downw.setProcessRoute(processRoute);
			inventoryProcessMappingDao.update(upw);
			inventoryProcessMappingDao.update(downw);
		}
	}
	@Override
	public void updateShiftDownProcessRoute(String InventoryCode, Long processId) {
		InventoryProcessMapping downw=queryObjById(processId);
		Long processRoute=downw.getProcessRoute();
		String hql="from InventoryProcessMapping wpm where wpm.inventory.id=?0 and wpm.processRoute=?1 ";
		List<InventoryProcessMapping> upwList=inventoryProcessMappingDao.findByHQL(hql, new Object[] {InventoryCode,processRoute+1});
		if(upwList!=null&&upwList.size()>0) {
			InventoryProcessMapping upw=upwList.get(0);
			upw.setProcessRoute(processRoute);
			downw.setProcessRoute(processRoute+1);
			inventoryProcessMappingDao.update(upw);
			inventoryProcessMappingDao.update(downw);
		}else {
			throw new RuntimeException("已经是最后一道工序");
		}
	}
	/**
	 * 根据工单单号查找工序编码和名称
	 * @param no
	 * @return
	 */
	@Override
	public List<Object[]> queryProcessCodeAndNameByNo(String no) {
		return inventoryProcessMappingDao.queryProcessCodeAndNameByNo(no);
	}

	@Override
	public List<InventoryProcessMapping> queryByProcessId(Long id) {
		 List<InventoryProcessMapping> list = inventoryProcessMappingDao.findByHQL("from InventoryProcessMapping wpm where wpm.process.id=?0", new Object[] {id});
		return list;
	}

	/**
	 * 根据工件编码和工序编码查找
	 *
	 * @param workpieceCode
	 * @param processCode
	 * @return
	 */
	@Override
	public InventoryProcessMapping queryByInventoryCodeAndProcessCode(String workpieceCode, String processCode) {
		List<InventoryProcessMapping> list = inventoryProcessMappingDao.findByHQL("from InventoryProcessMapping wpm where wpm.inventory.code=?0 and wpm.process.code=?1",new Object[]{workpieceCode,processCode});
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return  list.get(0);
	}
}
