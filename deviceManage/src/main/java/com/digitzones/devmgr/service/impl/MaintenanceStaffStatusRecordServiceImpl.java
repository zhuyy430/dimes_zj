package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IMaintenanceStaffStatusRecordDao;
import com.digitzones.devmgr.model.MaintenanceStaffStatusRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffStatusRecordService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class MaintenanceStaffStatusRecordServiceImpl implements IMaintenanceStaffStatusRecordService {

	@Autowired
	IMaintenanceStaffStatusRecordDao maintenanceStaffStatusRecordDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceStaffStatusRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(MaintenanceStaffStatusRecord obj) {
		maintenanceStaffStatusRecordDao.update(obj);
		
	}
	@Override
	public MaintenanceStaffStatusRecord queryByProperty(String name, String value) {
		return maintenanceStaffStatusRecordDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(MaintenanceStaffStatusRecord obj) {
		return maintenanceStaffStatusRecordDao.save(obj);
	}

	@Override
	public MaintenanceStaffStatusRecord queryObjById(Serializable id) {
		return maintenanceStaffStatusRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceStaffStatusRecordDao.deleteById(id);
	}
}
