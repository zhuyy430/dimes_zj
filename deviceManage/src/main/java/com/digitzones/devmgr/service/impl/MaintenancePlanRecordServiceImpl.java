package com.digitzones.devmgr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dao.IMaintenanceItemDao;
import com.digitzones.devmgr.dao.IMaintenancePlanRecordDao;
import com.digitzones.devmgr.dao.IMaintenanceUserDao;
import com.digitzones.devmgr.model.MaintenanceItem;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.mc.model.MCUser;
import com.digitzones.model.*;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IWorkflowRecordService;
import com.digitzones.service.IWorkflowTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class MaintenancePlanRecordServiceImpl implements IMaintenancePlanRecordService {
	@Autowired
	private IMaintenancePlanRecordDao maintenancePlanRecordDao;
	@Autowired
	private IMaintenanceUserDao maintenanceUserDao;
	@Autowired
	private IMaintenanceItemDao maintenanceItemDao;
	@Autowired
	private IWorkflowRecordService workflowRecordService;
	@Autowired
	private IWorkflowTaskService workflowTaskService;
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	@Autowired
	private IEmployeeService employeeService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public Pager<MaintenancePlanRecord> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenancePlanRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenancePlanRecord obj) {
		maintenancePlanRecordDao.update(obj);
	}

	@Override
	public MaintenancePlanRecord queryByProperty(String name, String value) {
		return maintenancePlanRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenancePlanRecord obj) {
		return maintenancePlanRecordDao.save(obj);
	}

	@Override
	public MaintenancePlanRecord queryObjById(Serializable id) {
		return maintenancePlanRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenancePlanRecordDao.deleteById(id);
	}

	@Override
	public void updateStatus2Uncomplete() {
		maintenancePlanRecordDao.updateStatus2Uncomplete();
	}

	@Override
	public void deleteMaintenancePlanRecords(String[] idArray) {
		for(String id : idArray) {
			deleteObj(Long.valueOf(id));
		}		
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByDeviceCodeAndMonth(String deviceCode, int year,
			int month) {
		return maintenancePlanRecordDao.queryMaintenancePlanRecordByDeviceCodeAndMonth(deviceCode,year,month);
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatusForScreen(String status, String productionLineCode, String maintainerCode, String deviceCode, String maintenanceTypeonCode, String startDate, String endDate) {
		List<Object> paramList = new ArrayList<>();
		String hql="from MaintenancePlanRecord m where m.status=?0";
		paramList.add(status);



		int i=1;
		if(!StringUtils.isEmpty(productionLineCode)&&productionLineCode!="") {
			hql+= " and m.device.productionUnit.code=?"+ (i++) ;
			paramList.add(productionLineCode);
		}



		if(!maintainerCode.equals("")&&maintainerCode!=null) {
			hql+=" and EXISTS (select u from MaintenanceUser u where m.id=u.maintenancePlanRecord.id and u.code=?"+(i++)+") ";
			paramList.add(maintainerCode);
		}


		if(!StringUtils.isEmpty(deviceCode)&&deviceCode!="") {
			hql+= " and m.device.code=?"+ (i++) ;
			paramList.add(deviceCode);
		}

		if(!StringUtils.isEmpty(maintenanceTypeonCode)&&maintenanceTypeonCode!="") {
			hql+= " and m.maintenanceType.code=?"+ (i++) ;
			paramList.add(maintenanceTypeonCode);
		}

		try {
			if(!StringUtils.isEmpty(startDate)&&startDate!="") {
				hql += " and m.maintenanceDate>=?" + (i++);
				paramList.add(format.parse(startDate+" 00:00:00"));
			}
			if(!StringUtils.isEmpty(endDate)&&endDate!="") {
				hql += " and m.maintenanceDate<=?" + (i++);
				paramList.add(format.parse(endDate+" 23:59:59"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		hql+=" order by m.maintenanceDate desc";
		return maintenancePlanRecordDao.findByHQL(hql, paramList.toArray());
	}

	@Override
	public void update4AssignBatchPersonInCharge(Map<String, Object> params,User user) {
		String hql = " from MaintenancePlanRecord record where record.maintenanceDate between ?0 and ?1 "
				+ "and record.maintenanceType.name=?2 and record.device.code=?3 and record.device.isDeviceManageUse=?4";
		List<MaintenancePlanRecord> list = maintenancePlanRecordDao.findByHQL(hql,new Object[] {params.get("from"),params.get("to"),params.get("maintenanceType"),params.get("deviceCode"),true});
		String employeeCode = (String) params.get("employeeCode");
		String employeeName = (String) params.get("employeeName");
		if(!CollectionUtils.isEmpty(list)) {
			for(MaintenancePlanRecord record :list) {
				//删除已有责任人
				List<MaintenanceUser> mus = maintenanceUserDao.findByHQL("from MaintenanceUser mu where mu.maintenancePlanRecord.id=?0", new Object[] {record.getId()});
				//已有责任人
				if(!CollectionUtils.isEmpty(mus)) {
					return ;
				}
				
				record.setStatus(Constant.Status.MAINTENANCEPLAN_RECEIPT);
				MaintenanceUser maintenanceUser = new MaintenanceUser();
				maintenanceUser.setMaintenancePlanRecord(record);
				maintenanceUser.setCode(employeeCode);
				maintenanceUser.setName(employeeName);
				Employee employee = user.getEmployee();
				if(employee==null) {
					employee = new Employee();
					employee.setCode(user.getUsername());
					employee.setName(user.getUsername());
				}
				maintenanceUser.setDispatchUsercode(employee.getCode());
				maintenanceUser.setDispatchUsername(employee.getName());
				maintenanceUser.setDispatchDate(new Date());
				maintenanceUser.setOrderType("人工派单");
				maintenanceUserDao.save(maintenanceUser);

				MaintenancePlanRecord r = maintenanceUser.getMaintenancePlanRecord();
				WorkflowRecord workflowRecord = workflowRecordService.queryByProperty("businessKey", r.getId()+":"+MaintenancePlanRecord.class.getName());
				WorkflowTask task = new WorkflowTask();
				task.setAssignee(maintenanceUser.getName());
				task.setOwnner(employee.getName());
				task.setWorkflowRecord(workflowRecord);
				task.setName("指定责任人");
				task.setCreateDate(new Date());
				task.setDescription(employee.getName() + "派单给责任人  " + maintenanceUser.getName());
				task.setBusinessKey(workflowRecord.getBusinessKey());
				workflowTaskService.addObj(task);	
			}
		}
	}

	@Override
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceCode(String deviceCode) {
		String hql = "from MaintenancePlanRecord r where r.device.code =?0";
		return maintenancePlanRecordDao.findByHQL(hql, new Object[] {deviceCode});
	}
	
	@Override
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceCodeAndUser(String deviceCode) {
		String hql = "select r from MaintenancePlanRecord r  where r.device.code =?0"
				+ " ORDER by r.maintenancedDate,r.maintenanceDate";
/*		String hql = "select r from MaintenancePlanRecord r inner join MaintenanceUser u on r.id=u.maintenancePlanRecord.id where r.device.code =?0"
				+ " ORDER by r.maintenancedDate,r.maintenanceDate";
*/		return maintenancePlanRecordDao.findByHQL(hql, new Object[] {deviceCode});
	}

	@Override
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByDeviceId(Long deviceId) {
		String hql = " from MaintenancePlanRecord record where record.device.id=?0 and record.device.isDeviceManageUse=?1";
		List<MaintenancePlanRecord> list = maintenancePlanRecordDao.findByHQL(hql,new Object[] {deviceId,true});
		return list;
	}
	
	@Override
	public void confirm(MaintenancePlanRecord item,Employee employee) {
		item.setConfirmCode(employee.getCode());
		item.setConfirmName(employee.getName());
		item.setStatus(Constant.Status.CHECKINGPLAN_COMPLETE);
		maintenancePlanRecordDao.update(item);

		String businessKey = item.getId() + ":" + MaintenancePlanRecord.class.getName();
		WorkflowRecord record = workflowRecordService.queryByProperty("businessKey", businessKey);
		record.setEndDate(new Date());
		workflowRecordService.updateObj(record);
		//添加流程任务节点
		WorkflowTask task = new WorkflowTask();
		task.setName("确认");
		task.setCreateDate(new Date());
		task.setBusinessKey(businessKey);
		task.setWorkflowRecord(record);
		task.setDescription("已确认,确认人:" + employee.getName());
		task.setOwnner(employee.getName());
		workflowTaskService.addObj(task);

		List<MaintenanceItem> list = maintenanceItemDao.findByHQL("from MaintenanceItem item where item.maintenancePlanRecord.id=?0", new Object[] {item.getId()});
		if(!CollectionUtils.isEmpty(list)) {
			for(MaintenanceItem i : list) {
				i.setConfirmCode(employee.getCode());
				i.setConfirmDate(new Date());
				i.setConfirmUser(i.getName());
				maintenanceItemDao.update(i);
			}
		}
	}
	@Override
	public Serializable addMaintenancePlanRecord(MaintenancePlanRecord maintenancePlanRecord, Employee employee) {
		Serializable id = maintenancePlanRecordDao.save(maintenancePlanRecord);
		//流程开始
		WorkflowRecord record = new WorkflowRecord();
		record.setBusinessKey(id + ":" + MaintenancePlanRecord.class.getName());
		record.setStartDate(new Date());
		record.setName("设备保养");
		record.setStartUsername(employee.getName());
		workflowRecordService.addObj(record);
		//添加流程任务节点
		WorkflowTask task = new WorkflowTask();
		task.setName("新建保养计划");
		task.setCreateDate(new Date());
		task.setBusinessKey(id + ":" + MaintenancePlanRecord.class.getName());
		task.setWorkflowRecord(record);
		task.setDescription("新建保养计划");
		task.setOwnner(employee.getName());
		workflowTaskService.addObj(task);
		return id;
	}
	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByDeviceIdAndToday(Long deviceId, int year,
			int month, int day) {
		String hql="from MaintenancePlanRecord m where m.device.id=?0 and year(maintenanceDate)=?1 and month(maintenanceDate)=?2 and day(maintenanceDate)=?3";
		return maintenancePlanRecordDao.findByHQL(hql, new Object[] {deviceId,year,month,day});
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(Long deviceId, int year,
			int month, int day, String name,String classCode) {
		return maintenancePlanRecordDao.queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(deviceId, year, month, day, name,classCode);
	}

	@Override
	public String queryMaxNoByDate(Date date) {
		return maintenancePlanRecordDao.queryMaxNoByDate(date);
	}
	@Override
	public List<Object[]> queryAllMaintenancePlanRecordmaintianDateByToday(String name,Long deviceId) {
		return maintenancePlanRecordDao.queryAllMaintenancePlanRecordmaintianDateByToday( name,deviceId);
	}
	@Override
	public List<Object[]> queryNotMaintenanceDeviceBytime(String startTime, String endTime) {
		return maintenancePlanRecordDao.queryNotMaintenanceDeviceBytime(startTime,endTime);
	}
	@Override
	public List<Object[]> queryNotMaintenanceRecordBytime(String startTime, String endTime) {
		return maintenancePlanRecordDao.queryNotMaintenanceRecordBytime(startTime,endTime);
	}
	@Override
	public List<MaintenancePlanRecord> queryMaintenanceRecordByDeviceCodeAndTimeSlot(Long deviceId,String startDate, String endDate) {
		String hql="from MaintenancePlanRecord record where record.device.id=?0 and record.maintenanceDate>=?1 and record.maintenanceDate<=?2";
		Date startTime = null;
		Date endTime = null;
		try {
			startTime = format.parse(startDate);
			endTime = format.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return maintenancePlanRecordDao.findByHQL(hql, new Object[] {deviceId,startTime,endTime});
		
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatus(String status, String usercode) {
		String hql="from MaintenancePlanRecord m where 1=1 ";
		List<Object> list=new ArrayList<Object>();
		int i=-1;
		if(!usercode.equals("")&&usercode!=null) {
			i=i+1;
			hql+=" EXISTS (select * from MaintenanceUser u where m.id=u.MAINTENANCEPLANRECORD_ID and u.code=?"+i+") ";
			list.add(usercode);
		}
		if(!status.equals("")&&status!=null) {
			i=i+1;
			hql+=" and m.status=?"+i;
			list.add(status);
		}
		hql+=" order by m.maintenanceDate desc";
		Object[] obj =  list.toArray(new Object[1]);   
		return maintenancePlanRecordDao.findByHQL(hql, obj);
	}

	@Override
	public List<MaintenanceUser> queryMaintenanceUserByRecordId(Long recordId) {
		String hql="from MaintenanceUser m where m.maintenancePlanRecord.id=?0";
		return maintenanceUserDao.findByHQL(hql, new Object[] {recordId});
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordTodayByDeviceIdAndEmployCodeAll(Long deviceId,
			int year, int month, int day, String usercode) {
		return maintenancePlanRecordDao.queryMaintenancePlanRecordTodayByDeviceIdAndEmployCodeAll(deviceId, year, month, day, usercode);
		
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenanceRecordByProductLineIdAndDeviceTypeIdAndTimeSlot(String startDate,
			String endDate, Long productLineId, Long deviceTypeId) {
		String hql="from MaintenancePlanRecord record where record.maintenanceDate>=?0 and record.maintenanceDate<=?1 ";
		Date startTime = null;
		Date endTime = null;
		try {
			startTime = format.parse(startDate);
			endTime = format.parse(endDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 List<Object> list=new ArrayList<Object>();
		 list.add(startTime);
		 list.add(endTime);
		 int i=1;
		if(productLineId!=null) {
			i=i+1;
			hql+=" and record.device.productionUnit.id=?"+i;
			list.add(productLineId);
		}
		if(deviceTypeId!=null) {
			i=i+1;
			hql+=" and record.device.projectType.id=?"+i;
			list.add(deviceTypeId);
		}
		Object[] obj =  list.toArray(new Object[1]);
		
		return maintenancePlanRecordDao.findByHQL(hql, obj);
	}

	@Override
	public List<MaintenancePlanRecord> queryReceiptMPRWithUser(String usercode) {
		return maintenancePlanRecordDao.queryReceiptMPRWithUser(usercode);
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenanceMPRWithUser(String usercode) {
		return maintenancePlanRecordDao.queryMaintenanceMPRWithUser(usercode);
	}

	@Override
	public void confirmMaintenance(MaintenancePlanRecord item, String username, String status) {
		maintenancePlanRecordDao.update(item);
		MaintenanceUser responsibility=maintenanceUserService.queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(item.getId()).get(0);
		String businessKey = item.getId() + ":" + MaintenancePlanRecord.class.getName();
		WorkflowRecord record = workflowRecordService.queryByProperty("businessKey", businessKey);
		record.setEndDate(new Date());
		workflowRecordService.updateObj(record);
		//添加流程任务节点
		WorkflowTask task = new WorkflowTask();
		task.setCreateDate(new Date());
		task.setBusinessKey(businessKey);
		task.setWorkflowRecord(record);
		task.setOwnner(username);
		if(status.equals(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED)) {
			task.setName("保养完成");
			task.setDescription("已完成,责任人:" + responsibility.getName()+";操作人:"+username);
		}else if(status.equals(Constant.Status.MAINTENANCEPLAN_COMPLETE)) {
			task.setName("车间确认");
			task.setDescription("已确认,确认人:" + username);
		}else if(status.equals(Constant.Status.MAINTENANCEPLAN_MAINTENANCING)){
			task.setName("派单接收");
			task.setDescription("已接单,接单人:" + username);
		}else if(status.equals(Constant.Status.MAINTENANCEPLAN_RECEIPT)){
			task.setName("分配派单");
			task.setOwnner(username);
			task.setDescription("已分配,维修人:" + username);
		}else if(status.equals(Constant.ReceiveType.ROBLIST)) {
			task.setName("抢单成功");
			task.setDescription("已抢单,抢单人:" + username);
		}else if(status.equals(Constant.ReceiveType.REWORK)) {
			task.setName("返修");
			task.setDescription("已返修,责任人:" + responsibility.getName()+";操作人:"+username);
		}
		workflowTaskService.addObj(task);
	}
	@Override
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByProductionUnitId(Long productionUnitId) {
		String hql = "from MaintenancePlanRecord r where r.device.code in "
				+ " (select d.code from Device d inner join d.productionUnit p where p.id=?0)"
				+ " order by r.maintenanceDate desc";
		return maintenancePlanRecordDao.findByHQL(hql, new Object[] {productionUnitId});
	}
	@Override
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecordsByProductionUnitIdAndUser(Long productionUnitId) {
		String hql = "select r from MaintenancePlanRecord r where r.device.code in "
				+ " (select d.code from Device d inner join d.productionUnit p where p.id=?0)"
				+ " ORDER by r.maintenancedDate,r.maintenanceDate";
		return maintenancePlanRecordDao.findByHQL(hql, new Object[] {productionUnitId});
	}
	@Override
	public List<MaintenancePlanRecord> queryAllMaintenancePlanRecords4CurrentYear(Long deviceId) {
		String hql = "select record from MaintenancePlanRecord record left join  record.device  d left join  d.productionUnit"
				+ " left join  d.projectType where record.maintenancedDate between ?0 and ?1 and d.id=?2";
		
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		return maintenancePlanRecordDao.findByHQL(hql,new Object[] {c.getTime(),now,deviceId});
	}
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByConfirmCodeAndConfirm(String confirmCode) {
		return	maintenancePlanRecordDao.findByHQL("select distinct r from MaintenancePlanRecord r inner join MaintenanceUser u on (r.id=u.maintenancePlanRecord.id and (r.confirmCode=?0 or u.code=?1)) where r.status='待确认' order by r.maintenanceDate desc", new Object[] {confirmCode,confirmCode});
	}

	@Override
	public Long queryMaintenancePlanRecordCountByDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return maintenancePlanRecordDao.findCount("from MaintenancePlanRecord r where year(r.maintenanceDate)=?0 "
				+ "and month(r.maintenanceDate)=?1 and day(r.maintenanceDate)=?2", new  Object[] {
						c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE)
				});
	}

	@Override
	public List<MaintenancePlanRecord> queryMaintenancePlanRecordByStatus(String status) {
		return maintenancePlanRecordDao.findListByProperty("status", status);
	}

	@Override
	public List<String[]> queryStatisticsData(String from, String to, String cycle) {
		return maintenancePlanRecordDao.queryStatisticsData(from,to,cycle);
	}

	@Override
	public List<String[]> queryOverviewData(String from, String to) {
		return maintenancePlanRecordDao.queryOverviewData(from,to);
	}

	@Override
	public void update4AssignBatchPersonInCharge4Mc(Map<String, Object> params,MCUser user) {
		String hql = " from MaintenancePlanRecord record where record.maintenanceDate between ?0 and ?1 "
				+ " and record.maintenanceType.name=?2 and record.device.code=?3 and record.device.isDeviceManageUse=?4";
		List<MaintenancePlanRecord> list = maintenancePlanRecordDao.findByHQL(hql,new Object[] {params.get("from"),params.get("to"),params.get("maintenanceType"),params.get("deviceCode"),true});
		String employeeCode = (String) params.get("employeeCode");
		String employeeName = (String) params.get("employeeName");
		if(!CollectionUtils.isEmpty(list)) {
			for(MaintenancePlanRecord record :list) {
				record.setStatus(Constant.Status.MAINTENANCEPLAN_RECEIPT);
				MaintenanceUser maintenanceUser = new MaintenanceUser();
				maintenanceUser.setMaintenancePlanRecord(record);
				maintenanceUser.setCode(employeeCode);
				maintenanceUser.setName(employeeName);
				maintenanceUser.setDispatchUsercode(user.getEmployeeCode());
				maintenanceUser.setDispatchUsername(user.getEmployeeName());
				maintenanceUser.setDispatchDate(new Date());
				maintenanceUser.setOrderType("人工派单");
				maintenanceUserDao.save(maintenanceUser);
				
				MaintenancePlanRecord r = maintenanceUser.getMaintenancePlanRecord();
				WorkflowRecord workflowRecord = workflowRecordService.queryByProperty("businessKey", r.getId()+":"+MaintenancePlanRecord.class.getName());
				WorkflowTask task = new WorkflowTask();
				task.setAssignee(maintenanceUser.getName());
				task.setOwnner(user.getEmployeeName());
				task.setWorkflowRecord(workflowRecord);
				task.setName("指定责任人");
				task.setCreateDate(new Date());
				task.setDescription(user.getEmployeeName() + "派单给责任人  " + employeeName);
				task.setBusinessKey(workflowRecord.getBusinessKey());
				workflowTaskService.addObj(task);
			}
		}
	}
}
