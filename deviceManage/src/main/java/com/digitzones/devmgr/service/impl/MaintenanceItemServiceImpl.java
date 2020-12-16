package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IDeviceProjectDao;
import com.digitzones.devmgr.dao.IMaintenanceItemDao;
import com.digitzones.devmgr.dao.IMaintenancePlanRecordDao;
import com.digitzones.devmgr.dao.IMaintenanceTypeDao;
import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.devmgr.model.MaintenanceItem;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.devmgr.service.IMaintenanceItemService;
import com.digitzones.model.Pager;

@Service
public class MaintenanceItemServiceImpl implements IMaintenanceItemService {
	@Autowired
	private IMaintenanceItemDao maintenanceItemDao;
	@Autowired
	private IDeviceProjectDao deviceProjectDao;
	@Autowired
	private IMaintenancePlanRecordDao maintenancePlanRecordDao;
	@Autowired
	private IMaintenanceTypeDao maintenanceTypeDao;

	@Override
	public Pager<MaintenanceItem> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceItemDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenanceItem obj) {
		maintenanceItemDao.update(obj);
	}

	@Override
	public MaintenanceItem queryByProperty(String name, String value) {
		return maintenanceItemDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenanceItem obj) {
		return maintenanceItemDao.save(obj);
	}

	@Override
	public MaintenanceItem queryObjById(Serializable id) {
		return maintenanceItemDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceItemDao.deleteById(id);
	}

	@Override
	public void addMaintenanceItems(Long maintenancePlanRecordId, String itemIds) {
		MaintenancePlanRecord record = new MaintenancePlanRecord();
		record.setId(maintenancePlanRecordId);
		String[] itemIdArray = itemIds.split(",");
		for (int i = 0; i < itemIdArray.length; i++) {
			Long deviceProjectId = Long.valueOf(itemIdArray[i]);
			DeviceProject project = deviceProjectDao.findById(deviceProjectId);
			MaintenanceItem item = new MaintenanceItem();

			BeanUtils.copyProperties(project, item);
			item.setMaintenancePlanRecord(record);
			maintenanceItemDao.save(item);
		}
	}

	@Override
	public void confirm(MaintenanceItem item) {
		maintenanceItemDao.update(item);
		Long count = maintenanceItemDao.findCount(
				"from MaintenanceItem item where item.confirmDate is null and item.maintenancePlanRecord.id=?0",
				new Object[] { item.getMaintenancePlanRecord().getId() });
		// 所有保养项目都已确认，则保养记录设置为已确认
		if (count == null || count == 0) {
			MaintenancePlanRecord record = maintenancePlanRecordDao.findById(item.getMaintenancePlanRecord().getId());
			record.setConfirmDate(new Date());
			maintenancePlanRecordDao.update(record);
		}
	}

	@Override
	public List<MaintenanceItem> queryMaintenanceItemByMaintenancePlanRecordId(Long Id) {
		return maintenanceItemDao.findByHQL("from MaintenanceItem ms where ms.maintenancePlanRecord.id=?0",
				new Object[] { Id });
	}

	@Override
	public int queryResultCountByMaintenanceItemId(Long maintenancePlanRecordId) {
		List<MaintenanceItem> list = maintenanceItemDao.findByHQL(
				"from MaintenanceItem item where item.maintenancePlanRecord.id=?0 and (item.confirmDate is not null or confirmDate!='')",
				new Object[] { maintenancePlanRecordId });
		return list.size();
	}

	@Override
	public void addMaintenanceItemsByType(Long maintenancePlanRecordId, String itemIds, String typeCode) {
		MaintenancePlanRecord record = new MaintenancePlanRecord();
		record.setId(maintenancePlanRecordId);
		String[] itemIdArray = itemIds.split(",");
		MaintenanceType type = maintenanceTypeDao.findSingleByProperty("code", typeCode);
		for (int i = 0; i < itemIdArray.length; i++) {
			Long deviceProjectId = Long.valueOf(itemIdArray[i]);
			DeviceProject project = deviceProjectDao.findById(deviceProjectId);
			MaintenanceItem item = new MaintenanceItem();
			if (type != null) {
				item.setRecordTypeCode(type.getCode());
				item.setRecordTypeName(type.getName());
			}
			BeanUtils.copyProperties(project, item);
			item.setMaintenancePlanRecord(record);
			maintenanceItemDao.save(item);
		}

	}
}
