package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dao.IMaintenanceStaffDao;
import com.digitzones.devmgr.dao.IMaintenanceStaffStatusRecordDao;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.MaintenanceStaffStatusRecord;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class MaintenanceStaffServiceImpl implements IMaintenanceStaffService {
	@Autowired
	IMaintenanceStaffDao maintenanceStaffDao;
	@Autowired
	IMaintenanceStaffStatusRecordDao maintenanceStaffStatusRecordDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceStaffDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenanceStaff obj) {
		maintenanceStaffDao.update(obj);
	}

	@Override
	public MaintenanceStaff queryByProperty(String name, String value) {
		return maintenanceStaffDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenanceStaff obj) {
		return maintenanceStaffDao.save(obj);
	}

	@Override
	public MaintenanceStaff queryObjById(Serializable id) {
		return maintenanceStaffDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceStaffDao.deleteById(id);
	}

	@Override
	public boolean updateStatus(Long id, String status,String userName) {
		MaintenanceStaff m = maintenanceStaffDao.findById(id);
		MaintenanceStaffStatusRecord mRecord=new MaintenanceStaffStatusRecord();
		mRecord.setChangeBeforeStatus(m.getWorkStatus());
		mRecord.setChangeAfterStatus(status);
		mRecord.setChangeDate(new Date());
		mRecord.setCode(m.getCode());
		mRecord.setName(m.getName());
		mRecord.setOperator(userName);
		
		m.setWorkStatus(status);
		m.setChangeDate(new Date());
		maintenanceStaffDao.update(m);
		maintenanceStaffStatusRecordDao.save(mRecord);
		
		return true;
	}
	@Override
	public boolean updateStatus(MaintenanceStaff m, String status,String userName) {
		MaintenanceStaffStatusRecord mRecord=new MaintenanceStaffStatusRecord();
		mRecord.setChangeBeforeStatus(m.getWorkStatus());
		mRecord.setChangeAfterStatus(status);
		mRecord.setChangeDate(new Date());
		mRecord.setCode(m.getCode());
		mRecord.setName(m.getName());
		mRecord.setOperator(userName);
		
		m.setWorkStatus(status);
		/*if(!m.getOnDutyStatus().equals(status)){
			m.setOnDutyStatus(status);
		}*/
		m.setChangeDate(new Date());
		maintenanceStaffDao.update(m);
		maintenanceStaffStatusRecordDao.save(mRecord);
		
		return true;
	}

	@Override
	public List<MaintenanceStaff> queryListByProductionUnitIdAndStatus(Long productionUnitId,String status) {
		List<MaintenanceStaff> m = maintenanceStaffDao.findListByProductionUnitIdAndStatus(productionUnitId, status);
		return m;
	}
	@Override
	public List<MaintenanceStaff> queryListByStatus(String status) {
		List<MaintenanceStaff> m = maintenanceStaffDao.findListByStatus(status);
		return m;
	}

	@Override
	public List<MaintenanceStaff> queryAllMaintenanceStaff() {
		// TODO Auto-generated method stub
		return maintenanceStaffDao.findAll();
	}

	@Override
	public boolean updateWorkStatus(MaintenanceStaff m, String status, String userName) {
		MaintenanceStaffStatusRecord mRecord=new MaintenanceStaffStatusRecord();
		mRecord.setChangeBeforeStatus(m.getWorkStatus());
		mRecord.setChangeAfterStatus(status);
		mRecord.setChangeDate(new Date());
		mRecord.setCode(m.getCode());
		mRecord.setName(m.getName());
		mRecord.setOperator(userName);
		
		m.setWorkStatus(status);
		m.setChangeDate(new Date());
		maintenanceStaffDao.update(m);
		maintenanceStaffStatusRecordDao.save(mRecord);
		
		return true;
	}

	@Override
	public Long queryCountOnPosition() {
		return maintenanceStaffDao.findCount("from MaintenanceStaff ms where ms.workStatus in (?0,?1,?2)", 
				new Object[] {Constant.MaintenanceStaffStatus.ONDUTY,Constant.MaintenanceStaffStatus.MAINTAIN,Constant.MaintenanceStaffStatus.MAINTENANCE});
	}
}
