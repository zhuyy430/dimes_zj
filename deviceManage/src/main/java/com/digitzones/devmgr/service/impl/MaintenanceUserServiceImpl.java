package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.devmgr.dao.IMaintenancePlanRecordDao;
import com.digitzones.devmgr.dao.IMaintenanceUserDao;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkflowRecord;
import com.digitzones.model.WorkflowTask;
import com.digitzones.service.IWorkflowRecordService;
import com.digitzones.service.IWorkflowTaskService;
@Service
public class MaintenanceUserServiceImpl implements IMaintenanceUserService {
	@Autowired
	private IMaintenanceUserDao maintenanceUserDao;
	@Autowired
	private IMaintenancePlanRecordDao maintenancePlanRecordDao;
	@Autowired
	private IWorkflowRecordService workflowRecordService;
	@Autowired
	private IWorkflowTaskService workflowTaskService;
	@Override
	public Pager<MaintenanceUser> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceUserDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(MaintenanceUser obj) {
		maintenanceUserDao.update(obj);
	}

	@Override
	public MaintenanceUser queryByProperty(String name, String value) {
		return maintenanceUserDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenanceUser obj) {
		return maintenanceUserDao.save(obj);
	}

	@Override
	public MaintenanceUser queryObjById(Serializable id) {
		return maintenanceUserDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceUserDao.deleteById(id);
	}


	@Override
	public MaintenanceUser queryPersonInChargeByMaintenancePlanRecordId(Long maintenancePlanRecordId) {
		List<MaintenanceUser> list = maintenanceUserDao.findByHQL("from MaintenanceUser u where u.maintenancePlanRecord.id=?0", new Object[] {maintenancePlanRecordId});
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void addMaintenanceUser(MaintenanceUser maintenanceUser, Employee employee) {
		MaintenancePlanRecord record = maintenanceUser.getMaintenancePlanRecord();
		record = maintenancePlanRecordDao.findById(record.getId());
		/*if(record.getMaintenanceDate().getTime()+(24*60*60*1000)>new Date().getTime()){
			record.setStatus(Constant.Status.MAINTENANCEPLAN_RECEIPT);
			maintenancePlanRecordDao.update(record);
		}*/
		if(maintenanceUser.getOrderType().equals("人工派单")) {
			String hql = "from MaintenanceUser u where u.orderType=?0 and u.maintenancePlanRecord.id=?1";
			List<MaintenanceUser> list = maintenanceUserDao.findByHQL(hql, new Object[] {"人工派单",maintenanceUser.getMaintenancePlanRecord().getId()});
			if(!CollectionUtils.isEmpty(list)) {
				for(MaintenanceUser u : list) {
					u.setName(maintenanceUser.getName());
					maintenanceUserDao.update(u);
				}
			}else {
				maintenanceUserDao.save(maintenanceUser);
			}
			WorkflowRecord workflowRecord = workflowRecordService.queryByProperty("businessKey", record.getId()+":"+MaintenancePlanRecord.class.getName());
			WorkflowTask task = new WorkflowTask();
			task.setAssignee(maintenanceUser.getName());
			task.setOwnner(employee.getName());
			task.setWorkflowRecord(workflowRecord);
			task.setName("指定责任人");
			task.setCreateDate(new Date());
			task.setDescription(employee.getName() + "派单给责任人  " + maintenanceUser.getName());
			task.setBusinessKey(workflowRecord.getBusinessKey());
			workflowTaskService.addObj(task);
		}else {//协助
			maintenanceUserDao.save(maintenanceUser);
			WorkflowRecord workflowRecord = workflowRecordService.queryByProperty("businessKey", record.getId()+":"+MaintenancePlanRecord.class.getName());
			WorkflowTask task = new WorkflowTask();
			task.setAssignee(maintenanceUser.getName());
			task.setOwnner(employee.getName());
			task.setWorkflowRecord(workflowRecord);
			task.setName("指定协助人");
			task.setCreateDate(new Date());
			task.setDescription(employee.getName() + "请求  " + maintenanceUser.getName() + " 协助");
			task.setBusinessKey(workflowRecord.getBusinessKey());
			workflowTaskService.addObj(task);
		}
	}

	@Override
	public List<MaintenanceUser> queryMaintenanceUserByRecordId(Long id) {
		return maintenanceUserDao.findByHQL("from MaintenanceUser ms where ms.maintenancePlanRecord.id=?0", new Object[] {id});
	}

	@Override
	public MaintenanceUser queryPersonInChargeByMaintenancePlanRecordIdAndName(Long maintenancePlanRecordId,
			String name) {
		List<MaintenanceUser> list = maintenanceUserDao.findByHQL("from MaintenanceUser u where u.maintenancePlanRecord.id=?0 and u.name=?1", new Object[] {maintenancePlanRecordId,name});
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MaintenanceUser> queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(
			Long maintenancePlanRecordId) {
		return maintenanceUserDao.findByHQL("from MaintenanceUser ms where ms.maintenancePlanRecord.id=?0 and ms.orderType!='协助'", new Object[] {maintenancePlanRecordId});
	}

	@Override
	public List<MaintenanceUser> queryHelpMaintenanceUserByMaintenancePlanRecordId(Long maintenancePlanRecordId) {
		 return maintenanceUserDao.findByHQL("from MaintenanceUser ms where ms.maintenancePlanRecord.id=?0 and ms.orderType='协助'", new Object[] {maintenancePlanRecordId});
	}

	@Override
	public MaintenanceUser queryPersonInChargeByMaintenancePlanRecordIdAndEmployeeName(Long maintenancePlanRecordId,
			String employeeName) {
		List<MaintenanceUser> list = maintenanceUserDao.findByHQL("from MaintenanceUser u where u.maintenancePlanRecord.id=?0"
				+ " and u.name like ?1", new Object[] {maintenancePlanRecordId,"%" + employeeName + "%"});
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MaintenanceUser> queryWithMaintenancePlanRecordStatus(String status) {
		return maintenanceUserDao.findByHQL("select mu from MaintenanceUser mu inner join fetch mu.maintenancePlanRecord r"
				+ " where r.status=?0", new Object[] {status});
	}

	@Override
	public List<MaintenanceUser> queryWithMaintenancePlanRecordStatus(String status, String employeeCode) {
		return maintenanceUserDao.findByHQL("select mu from MaintenanceUser mu inner join fetch mu.maintenancePlanRecord r"
				+ " where r.status=?0 and mu.code=?1", new Object[] {status,employeeCode});
	}
}
