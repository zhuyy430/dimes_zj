package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IMaintenanceStaffRecordDao;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffRecordService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class MaintenanceStaffRecordServiceImpl implements IMaintenanceStaffRecordService {

	@Autowired
	IMaintenanceStaffRecordDao maintenanceStaffRecordDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceStaffRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenanceStaffRecord obj) {
		maintenanceStaffRecordDao.update(obj);
	}

	@Override
	public MaintenanceStaffRecord queryByProperty(String name, String value) {
		return maintenanceStaffRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenanceStaffRecord obj) {
		return maintenanceStaffRecordDao.save(obj);
	}

	@Override
	public MaintenanceStaffRecord queryObjById(Serializable id) {
		return maintenanceStaffRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceStaffRecordDao.deleteById(id);
	}

	@Override
	public List<MaintenanceStaffRecord> queryByOrderId(Long orderId) {
		List<MaintenanceStaffRecord> MaintenanceStaffRecords = maintenanceStaffRecordDao.findByOrderId(orderId);
		if(MaintenanceStaffRecords!=null&&MaintenanceStaffRecords.size()>0){
			return MaintenanceStaffRecords;
		}
		return null;
	}

	@Override
	public List<MaintenanceStaffRecord> queryByOrderIdandUsercode(Long orderId, String usercode) {
		String hql="from MaintenanceStaffRecord m where m.code=?1 and m.deviceRepair.id=?0";
		return maintenanceStaffRecordDao.findByHQL(hql, new Object[]{orderId,usercode});
	}

	@Override
	public List<MaintenanceStaffRecord> queryMaintenanceStaffRecordPersonLiableByorderId(Long orderId) {
		String hql="from MaintenanceStaffRecord m where m.receiveType!='ASSIST' and m.deviceRepair.id=?0";
		return maintenanceStaffRecordDao.findByHQL(hql, new Object[]{orderId});
	}

	@Override
	public List<MaintenanceStaffRecord> queryMaintenanceStaffRecordHelpByorderId(Long orderId) {
		String hql="from MaintenanceStaffRecord m where m.receiveType='ASSIST' and m.deviceRepair.id=?0";
		return maintenanceStaffRecordDao.findByHQL(hql, new Object[]{orderId});
	}

	@Override
	public List<MaintenanceStaffRecord> queryWithDeviceRepairStatus(String status) {
		return maintenanceStaffRecordDao.findByHQL("select record from MaintenanceStaffRecord record "
				+ " inner join fetch record.deviceRepair dr inner join fetch dr.device "
				+ " left join fetch dr.productionUnit p where dr.status=?0", new Object[] {status});
	}

	@Override
	public List<MaintenanceStaffRecord> queryWithDeviceRepairStatus(String status, String employeeCode) {
		return maintenanceStaffRecordDao.findByHQL("select record from MaintenanceStaffRecord record "
				+ " inner join fetch record.deviceRepair dr inner join fetch dr.device "
				+ " left join fetch dr.productionUnit p where dr.status=?0 and record.code=?1", new Object[] {status,employeeCode});
	}
}
