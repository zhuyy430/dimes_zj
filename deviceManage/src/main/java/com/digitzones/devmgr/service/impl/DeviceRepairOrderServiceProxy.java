package com.digitzones.devmgr.service.impl;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.model.WorkflowRecord;
import com.digitzones.model.WorkflowTask;
import com.digitzones.service.IWorkflowRecordService;
import com.digitzones.service.IWorkflowTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备维修单业务接口代理类
 * @author Administrator
 */
@Service
public class DeviceRepairOrderServiceProxy implements IDeviceRepairOrderService {
	@Autowired
	@Qualifier("deviceRepairOrderServiceImpl")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IWorkflowRecordService workflowRecordService;
	@Autowired
	private IWorkflowTaskService workflowTaskService;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceRepairOrderService.queryObjs(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(DeviceRepair obj) {
		deviceRepairOrderService.updateObj(obj);
	}
	@Override
	public DeviceRepair queryByProperty(String name, String value) {
		return deviceRepairOrderService.queryByProperty(name, value);
	}
	@Override
	public Serializable addObj(DeviceRepair obj) {
		return deviceRepairOrderService.addObj(obj);
	}
	@Override
	public DeviceRepair queryObjById(Serializable id) {
		return deviceRepairOrderService.queryObjById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		deviceRepairOrderService.deleteObj(id);
	}
	@Override
	public List<DeviceRepair> queryListByProperty(String name, String value) {
		return deviceRepairOrderService.queryListByProperty(name, value);
	}
	@Override
	public Serializable addDeviceRepair(DeviceRepair deviceRepair,String operator) {
		//执行主要业务
				Serializable id = deviceRepairOrderService.addDeviceRepair(deviceRepair, operator);
				//流程开始
				WorkflowRecord record = new WorkflowRecord();
				record.setBusinessKey(id + ":" + DeviceRepair.class.getName());
				record.setStartDate(new Date());
				record.setName("设备维修");
				record.setStartUsername(operator);
				workflowRecordService.addObj(record);
				//添加流程任务节点
				
				WorkflowTask task = new WorkflowTask();
				task.setName("新建设备维修单");
				task.setCreateDate(new Date());
				task.setBusinessKey(id + ":" + DeviceRepair.class.getName());
				task.setWorkflowRecord(record);
				task.setDescription("新建设备维修单");
				task.setOwnner(operator);
				workflowTaskService.addObj(task);
				return id;
	}
	@Override
	public void confirmDeviceRepair(DeviceRepair deviceRepair, String operator,String status) {
		deviceRepairOrderService.confirmDeviceRepair(deviceRepair,operator,status);
		String businessKey = deviceRepair.getId() + ":" + DeviceRepair.class.getName();
		WorkflowRecord record = workflowRecordService.queryByProperty("businessKey", businessKey);
		//添加流程任务节点
		WorkflowTask task = new WorkflowTask();
		task.setCreateDate(new Date());
		task.setBusinessKey(businessKey);
		task.setWorkflowRecord(record);
		task.setOwnner(operator);
		if(status.equals(Constant.DeviceRepairStatus.MAINTAINCOMPLETE)){
			record.setEndDate(new Date());
			workflowRecordService.updateObj(record);
			task.setName("车间确认");
			task.setDescription("已确认,确认人:" + operator);
		}else if(status.equals(Constant.DeviceRepairStatus.WAITWORKSHOPCOMFIRM)){
			task.setName("维修完成");
			task.setDescription("已完成,责任人:" + deviceRepair.getMaintainName()+";操作人:"+operator);
		}else if(status.equals(Constant.DeviceRepairStatus.MAINTAINING)){
			task.setName("派单接收");
			task.setDescription("已接单,接单人:" + operator);
		}else if(status.equals(Constant.DeviceRepairStatus.WAITINCOMFIRM)){
			task.setName("分配派单");
			task.setOwnner(operator);
			task.setDescription("已分配,维修人:" + operator);
		}else if(status.equals(Constant.ReceiveType.ROBLIST)) {
			task.setName("抢单成功");
			task.setDescription("已抢单,抢单人:" + operator);
		}else if(status.equals(Constant.ReceiveType.REWORK)) {
			task.setName("返修");
			task.setDescription("已返修,责任人:" + deviceRepair.getMaintainName()+";操作人:"+operator);
		}else if(status.equals(Constant.ReceiveType.TRANSFER)){
			task.setName("转单");
			task.setDescription("已转单,责任人:" + deviceRepair.getMaintainName()+";操作人:"+operator);
		}
		workflowTaskService.addObj(task);
	}
	/**
	 * 接收报修单
	 * @param deviceRepair
	 * @param user
	 * @param args
	 * @return
	 */
	@Override
	public void receiveDeviceRepair(DeviceRepair deviceRepair, User user, Map<String, Object> args) {

	}
/*	@Override
	public void receiveDeviceRepair(DeviceRepair deviceRepair, User user, Map<String, Object> args) {
		deviceRepairOrderService.receiveDeviceRepair(deviceRepair,user,args);
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().processInstanceBusinessKey(deviceRepair.getId()+":"+DeviceRepair.class.getName()).singleResult();
		if(user!=null) {
			if(user.getEmployee()!=null){
				taskService.setAssignee(task.getId(),user.getEmployee().getName());
			}else{
				taskService.setAssignee(task.getId(),user.getUsername());
			}
		}else{
			taskService.setAssignee(task.getId(), user!=null?user.getUsername():"");
		}
		taskService.setVariableLocal(task.getId(), "suggestion", args.get("suggestion"));
		taskService.complete(task.getId());
	}*/

	@Override
	   public List<DeviceRepair> queryAllDeviceRepair() {
	      return deviceRepairOrderService.queryAllDeviceRepair();
	   }

	@Override
	public List<DeviceRepair> queryAlarmsCount(String alarmedIds) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryAlarmsCount(alarmedIds);
	}

	@Override
	public DeviceRepair queryFirstDeviceRepair() {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryFirstDeviceRepair();
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairOrderByDeviceId(Long deviceId) {
		return deviceRepairOrderService.queryDeviceRepairOrderByDeviceId(deviceId);
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairOrderByDeviceCode2(String deviceCode) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryDeviceRepairOrderByDeviceCode2(deviceCode);
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairOrderByProductionUnitId(Long productionUnitId) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryDeviceRepairOrderByProductionUnitId(productionUnitId);
	}
	
	/*@Override
	public List<DeviceRepair> queryMaintainingDeviceRepairWithUser(String username) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryMaintainingDeviceRepairWithUser(username);
	}
	@Override
	public List<DeviceRepair> queryWaitListDeviceRepairWithUser(String username) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryWaitListDeviceRepairWithUser(username);
	}*/

	@Override
	public List<DeviceRepair> queryWorkshopcomfirmWithInformant(String Informant) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryWorkshopcomfirmWithInformant(Informant);
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairWithUserandStatus(String status, String username) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryDeviceRepairWithUserandStatus(status, username);
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairWithStatusForScreen(String status, String productionLineCode, String maintainerCode, String deviceCode, String ngFaultTypeonCode, String startDate, String endDate) {
		return deviceRepairOrderService.queryDeviceRepairWithStatusForScreen(status,productionLineCode,maintainerCode,deviceCode,ngFaultTypeonCode,startDate,endDate);
	}

	@Override
	public List<DeviceRepair> queryReceiptDeviceRepairWithUser(String username) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryReceiptDeviceRepairWithUser(username);
	}

	@Override
	public List<DeviceRepair> queryMaintenanceDeviceRepairWithUser(String username) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryMaintenanceDeviceRepairWithUser(username);
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairWithStatus(String status) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryDeviceRepairWithStatus(status);
	}

	@Override
	public Boolean updateDeviceRepairForConfirmAndReword(Long id, String status, String username) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.updateDeviceRepairForConfirmAndReword(id, status, username);
	}

	@Override
	public Boolean updateDeviceRepairOrderStatusById(Long id, String status, String username) throws Exception {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.updateDeviceRepairOrderStatusById(id, status, username);
	}

	@Override
	public Boolean addDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String username,
			HttpServletRequest request) {
		return deviceRepairOrderService.addDeviceRepairOrder(deviceRepairOrder, idList, username, request);
	}

	@Override
	public Boolean addPaperLessDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String employeeCode,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.addPaperLessDeviceRepairOrder(deviceRepairOrder, idList, employeeCode, request);
	}
	@Override
	   public long queryDeviceRepairOrderCountByDate(Date date) {
	      return deviceRepairOrderService.queryDeviceRepairOrderCountByDate(date);
	   }

	@Override
	public boolean addAutoSendDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String name,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.addAutoSendDeviceRepairOrder(deviceRepairOrder, idList, name, request);
	}

	@Override
	public void addManualSendDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String code,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		deviceRepairOrderService.addManualSendDeviceRepairOrder(deviceRepairOrder, idList, code, request);
	}

	@Override
	public List<DeviceRepair> queryCompletedDeviceRepairWithUser(String code) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryCompletedDeviceRepairWithUser(code);
	}

	@Override
	public int queryBadgeWithDeviceRepair(String hql) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryBadgeWithDeviceRepair(hql);
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairNumByDeviceProjectId(Long id) {
		// TODO Auto-generated method stub
		return deviceRepairOrderService.queryDeviceRepairNumByDeviceProjectId(id);
	}
}
