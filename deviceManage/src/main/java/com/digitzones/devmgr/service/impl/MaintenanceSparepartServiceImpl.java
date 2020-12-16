package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IMaintenanceSparepartDao;
import com.digitzones.devmgr.dao.ISparepartDao;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceSparepart;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.IDeviceSparepartMappingService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceSparepartService;
import com.digitzones.model.Pager;
@Service
public class MaintenanceSparepartServiceImpl implements IMaintenanceSparepartService {
	@Autowired
	private IMaintenanceSparepartDao maintenanceSparepartDao;
	@Autowired
	private ISparepartDao sparepartDao;
	@Autowired
	private IDeviceSparepartMappingService deviceSparepartMappingService;
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	@Override
	public Pager<MaintenanceSparepart> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceSparepartDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenanceSparepart obj) {
		maintenanceSparepartDao.update(obj);
	}

	@Override
	public MaintenanceSparepart queryByProperty(String name, String value) {
		return maintenanceSparepartDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenanceSparepart obj) {
		return maintenanceSparepartDao.save(obj);
	}

	@Override
	public MaintenanceSparepart queryObjById(Serializable id) {
		return maintenanceSparepartDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceSparepartDao.deleteById(id);
	}

	@Override
	public void addMaintenanceSpareparts(Long maintenancePlanRecordId, String sparepartIds,String count) {
		MaintenancePlanRecord record = new MaintenancePlanRecord();
		record.setId(maintenancePlanRecordId);
		String[] sparepartIdArray = sparepartIds.split(",");
		for(int i = 0;i<sparepartIdArray.length;i++) {
			Long sparepartId = Long.valueOf(sparepartIdArray[i]);
			Sparepart part = sparepartDao.findById(sparepartId);
			MaintenanceSparepart sparepart = new MaintenanceSparepart();

			BeanUtils.copyProperties(part, sparepart);
			if(null!=count&&!"".equals(count)){
				sparepart.setCount(Integer.parseInt(count));
			}
			sparepart.setMaintenancePlanRecord(record);
			sparepart.setUseDate(new Date());
			maintenanceSparepartDao.save(sparepart);
			MaintenancePlanRecord mplan = maintenancePlanRecordService.queryObjById(maintenancePlanRecordId);
			DeviceSparepartMapping dsMapping = deviceSparepartMappingService.queryBySparePartIDAndDeviceId(sparepartId, mplan.getDevice().getId());
			if(dsMapping!=null){
				dsMapping.setLastUseDate(new Date());
				deviceSparepartMappingService.updateObj(dsMapping);
			}
		}
	}

	@Override
	public List<MaintenanceSparepart> queryMaintenanceSparepartByRecordId(Long id) {
		// TODO Auto-generated method stub
		return maintenanceSparepartDao.findByHQL("from MaintenanceSparepart ms where ms.maintenancePlanRecord.id=?0", new Object[]{id});
	}
}
