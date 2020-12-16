package com.digitzones.devmgr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dao.IDeviceRepairOrderDao;
import com.digitzones.devmgr.model.*;
import com.digitzones.devmgr.service.*;
import com.digitzones.model.Device;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IAppClientMapService;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 璁惧缁翠慨鍗曚笟鍔℃帴鍙ｅ疄鐜扮被
 * @author Administrator
 */
@Service
public class  DeviceRepairOrderServiceImpl implements IDeviceRepairOrderService {
	@Autowired
	IDeviceRepairOrderDao deviceRepairOrderDao;
	@Autowired
	IDeviceService deviceService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMaintenanceStaffRecordService maintenanceStaffRecordService;
	@Autowired
	IAppClientMapService appClientMapService;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IDeviceRepairOrderPicService deviceRepairOrderPicService;
	@Autowired
	private INGMaintainRecordService ngMaintainRecordService;
	@Autowired
	private IConfigService configService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceRepairOrderDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceRepair obj) {
		deviceRepairOrderDao.update(obj);
	}

	@Override
	public DeviceRepair queryByProperty(String name, String value) {

		return deviceRepairOrderDao.findSingleByProperty(name, value);
	}
	@Override
	public List<DeviceRepair> queryListByProperty(String name, String value) {

		return deviceRepairOrderDao.findListByProperty(name, value);
	}
	@Override
	public Serializable addObj(DeviceRepair obj) {

		return deviceRepairOrderDao.save(obj);
	}
	@Override
	public DeviceRepair queryObjById(Serializable id) {
		return deviceRepairOrderDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		deviceRepairOrderDao.deleteById(id);
	}

	@Override
	public Serializable addDeviceRepair(DeviceRepair deviceRepair,String operator) {
		return deviceRepairOrderDao.save(deviceRepair);
	}

	@Override
	public void confirmDeviceRepair(DeviceRepair deviceRepair, String operator,String status) {
		deviceRepairOrderDao.update(deviceRepair);
	}

	@Override
	public void receiveDeviceRepair(DeviceRepair deviceRepair, User user, Map<String, Object> args) {

		deviceRepairOrderDao.update(deviceRepair);
	}

	@Override
	public List<DeviceRepair> queryAllDeviceRepair() {
		return deviceRepairOrderDao.findByHQL("from DeviceRepair d", new Object[] {});
	}
	@Override
	public List<DeviceRepair> queryAlarmsCount(String alarmedIds) {

		return deviceRepairOrderDao.findAlarmsCount(alarmedIds);
	}

	@Override
	public DeviceRepair queryFirstDeviceRepair() {

		List<DeviceRepair> list = deviceRepairOrderDao.findFirstDeviceRepair();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairOrderByDeviceId(Long deviceId) {
		return deviceRepairOrderDao.findByHQL("from DeviceRepair d where d.device.id=?0 "
				+ " and d.device.isDeviceManageUse=?1 order by d.createDate desc", new Object[] {deviceId,true});
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairOrderByProductionUnitId(Long productionUnitId) {
		String hql = "from DeviceRepair r where r.device.code in "
				+ " (select d.code from Device d inner join d.productionUnit p where p.id=?0 and d.isDeviceManageUse=?1)"
				+ " order by r.completeDate, r.createDate desc";
		return deviceRepairOrderDao.findByHQL(hql, new Object[] {productionUnitId,true});
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairOrderByDeviceCode2(String deviceCode) {
		return deviceRepairOrderDao.findByHQL("from DeviceRepair d where d.device.code=?0 and d.device.isDeviceManageUse=?1 order by d.completeDate,d.createDate desc", new Object[] {deviceCode,true});
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairWithUserandStatus(String status, String username) {
		return deviceRepairOrderDao.queryDeviceRepairWithUserandStatus(status, username);
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairWithStatusForScreen(String status, String productionLineCode, String maintainerCode, String deviceCode, String ngFaultTypeonCode, String startDate, String endDate) {
		List<Object> paramList = new ArrayList<>();
		String hql = "select dr from DeviceRepair dr left join fetch dr.dispatchedLevel  where dr.status=?0 ";

		paramList.add(status);


		int i = 1;
		if(!StringUtils.isEmpty(productionLineCode)&&productionLineCode!="") {
			hql+= " and dr.device.productionUnit.code=?"+ (i++) ;
			paramList.add(productionLineCode);
		}


		if(!StringUtils.isEmpty(maintainerCode)&&maintainerCode!="") {
			hql+= " and dr.maintainCode=?"+ (i++) ;
			paramList.add(maintainerCode);
		}

		if(!StringUtils.isEmpty(deviceCode)&&deviceCode!="") {
			hql+= " and dr.device.code=?"+ (i++) ;
			paramList.add(deviceCode);
		}

		if(!StringUtils.isEmpty(ngFaultTypeonCode)&&ngFaultTypeonCode!="") {
			hql+= " and dr.ngreason.projectType.code=?"+ (i++) ;
			paramList.add(ngFaultTypeonCode);
		}

		try {
			if(!StringUtils.isEmpty(startDate)&&startDate!="") {
				hql += " and dr.createDate>=?" + (i++);
				paramList.add(format.parse(startDate+" 00:00:00"));
			}
			if(!StringUtils.isEmpty(endDate)&&endDate!="") {
				hql += " and dr.createDate<=?" + (i++);
				paramList.add(format.parse(endDate+" 23:59:59"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		hql+=" order by dr.createDate";
		return deviceRepairOrderDao.findByHQL(hql,paramList.toArray());
	}

	@Override
	public List<DeviceRepair> queryWorkshopcomfirmWithInformant(String InformantCode) {
		String hql="from DeviceRepair d where (d.confirmCode=?0 or d.maintainCode=?1 or confirmCode=?2) and d.status='WAITWORKSHOPCOMFIRM' order by d.createDate desc";
		return deviceRepairOrderDao.findByHQL(hql, new Object[] {InformantCode,InformantCode,InformantCode});
	}

	@Override
	public List<DeviceRepair> queryReceiptDeviceRepairWithUser(String code) {
		return deviceRepairOrderDao.queryReceiptDeviceRepairWithUser(code);
	}

	@Override
	public List<DeviceRepair> queryMaintenanceDeviceRepairWithUser(String code) {
		return deviceRepairOrderDao.queryMaintenanceDeviceRepairWithUser(code);
	}
	@Override
	public List<DeviceRepair> queryDeviceRepairWithStatus(String status) {
		String hql = "select dr from DeviceRepair dr left join fetch dr.dispatchedLevel  where dr.status=?0 order by dr.createDate"; 
		return deviceRepairOrderDao.findByHQL(hql,new Object[] {status});
	}

	@Override
	public Boolean updateDeviceRepairForConfirmAndReword(Long id, String status, String username) {
		DeviceRepair deviceRepair = deviceRepairOrderDao.findById(id);
		User user=userService.queryUserByUsername(username);
		if(status.equals(Constant.ReceiveType.REWORK)){
			deviceRepair.setStatus(Constant.DeviceRepairStatus.WAITINCOMFIRM);
		}
		deviceRepair.setCompleteDate(null);
		if(null!=user.getEmployee()){
			deviceRepairOrderService.confirmDeviceRepair(deviceRepair, user.getEmployee().getName(), Constant.ReceiveType.REWORK);
		}else{
			deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, Constant.ReceiveType.REWORK);
		}

		//查询协助人
		List<MaintenanceStaffRecord>  MaintenanceStaffRecordList=maintenanceStaffRecordService.queryMaintenanceStaffRecordHelpByorderId(id);
		//清空协助人
		if(MaintenanceStaffRecordList!=null&&MaintenanceStaffRecordList.size()>0) {
			for(MaintenanceStaffRecord msr:MaintenanceStaffRecordList) {
				maintenanceStaffRecordService.deleteObj(msr.getId());
			}
		}
		//查询负责人
		List<MaintenanceStaffRecord> msrList=maintenanceStaffRecordService.queryMaintenanceStaffRecordPersonLiableByorderId(id);
		if(msrList!=null&&msrList.size()>0) {
			for(MaintenanceStaffRecord msr:msrList) {
				msr.setReceiveTime(null);
				maintenanceStaffRecordService.updateObj(msr);
			}
		}
		User maintainUser =userService.queryUserByEmployeeCode(deviceRepair.getMaintainCode());
		List<String> clientIdsList = new ArrayList<>();
		clientIdsList.add(maintainUser.getUsername()+"");
		clientIdsList=appClientMapService.queryCids(clientIdsList);
		try {
			//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Boolean updateDeviceRepairOrderStatusById(Long id, String status, String username) throws Exception {
		User user = userService.queryByProperty("username", username);
		if(user.getEmployee()==null){
			throw new Exception("操作人员不可对此记录进行接单操作!");
		}
		// 维修中
	      if (status.equals(Constant.DeviceRepairStatus.MAINTAINING)) {// 报修单为"等待接单确认状态"
			MaintenanceStaffRecord maintenanceStaffRecord = maintenanceStaffRecordService
					.queryObjById(id);
			if(!user.getEmployee().equals(maintenanceStaffRecord.getCode())){
	            throw new Exception("操作人员不可对此记录进行接单操作!");
			}
			Employee employee = employeeService.queryEmployeeByCode(maintenanceStaffRecord.getCode());
			DeviceRepair dro = maintenanceStaffRecord.getDeviceRepair();
			if (dro.getStatus().equals(Constant.DeviceRepairStatus.WAITINCOMFIRM)
					|| dro.getStatus().equals(Constant.DeviceRepairStatus.MAINTAINING)) {
				dro.setStatus(status);
				if(null!=employee){
					deviceRepairOrderService.confirmDeviceRepair(dro, employee.getName(), status);
				}else{
					deviceRepairOrderService.confirmDeviceRepair(dro, username, status);
				}

				maintenanceStaffRecord.setReceiveTime(new Date());
				maintenanceStaffRecordService.updateObj(maintenanceStaffRecord);
				MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code",
						dro.getMaintainCode());

				maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.MAINTAIN,
						Constant.ReceiveType.SYSTEMGASSIGN);


				//推送给报修人
				User InformantUser =userService.queryUserByEmployeeCode(dro.getInformantCode());
				if(InformantUser!=null){
					List<String> clientIdsList = new ArrayList<>();
					clientIdsList.add(InformantUser.getUsername()+"");
					clientIdsList=appClientMapService.queryCids(clientIdsList);
					try {
						//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_CONFIRM, DeviceRepairStatus.DEVICEREPAIRCONTENT_CONFIRM);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			return false;
		}

		if(status.equals(Constant.DeviceRepairStatus.MAINTAINCOMPLETE)){//车间确认
			DeviceRepair dro = deviceRepairOrderDao.findById(id);
			List<Employee> employeeList = employeeService.queryAllEmployeeAndNotMaintenanceStaff();
			for (Employee e : employeeList) {
				if(user.getEmployee().getCode().equals(e.getCode())){
					// 车间确认
					//List<MaintenanceStaffRecord> mList = maintenanceStaffRecordService.queryByOrderId(dro.getId());
		               if (dro.getStatus().equals(Constant.DeviceRepairStatus.WAITWORKSHOPCOMFIRM)) {
		            	   dro.setStatus(status);
		            	   dro.setCompleteDate(new Date());
		            	   if(null!=user.getEmployee()){
							   dro.setConfirmName(user.getEmployee().getName());
							   dro.setConfirmCode(user.getEmployee().getCode());
		            		   deviceRepairOrderService.confirmDeviceRepair(dro, user.getEmployee().getName(), status);
		            	   }else{
		            		   deviceRepairOrderService.confirmDeviceRepair(dro,username, status);
		            	   }
		            	   /*List<MaintenanceStaffRecord> rList = maintenanceStaffRecordService.queryByOrderId(id);
		            	   for(MaintenanceStaffRecord list:rList){
		            		   list.setCompleteTime(new Date());
		            		   maintenanceStaffRecordService.updateObj(list);
		            	   }*/
		            	 //自动派单
							List<DeviceRepair> repairList = deviceRepairOrderService.queryDeviceRepairWithStatus(Constant.DeviceRepairStatus.WAITINGASSIGN);
							for(DeviceRepair repair:repairList){
								deviceRepairOrderService.addAutoSendDeviceRepairOrder(repair, null, username, null);
							}
		            	   return true;
		               }
				}
				
			}
			 throw new Exception("操作人员不可对此记录进行车间确认操作!");
		} 
		if(status.equals(Constant.DeviceRepairStatus.WAITWORKSHOPCOMFIRM)){
			DeviceRepair dro = deviceRepairOrderDao.findById(id);
			List<MaintenanceStaffRecord> mList = maintenanceStaffRecordService.queryByOrderId(dro.getId());
			for(MaintenanceStaffRecord m:mList){
				if(user.getEmployee().equals(m.getCode())){
					if (dro.getStatus().equals(Constant.DeviceRepairStatus.MAINTAINING)) {  //维修完成
						dro.setStatus(status);
						if(null!=user.getEmployee()){
							deviceRepairOrderService.confirmDeviceRepair(dro, user.getEmployee().getName(), status);
						}else{
							deviceRepairOrderService.confirmDeviceRepair(dro, username, status);
						}
						for (MaintenanceStaffRecord ml : mList) {
							//m.setCompleteTime(new Date());
							Double MaintenanceTime =(double) Math.round(((double)(new Date().getTime()-dro.getCreateDate().getTime())/60000) * 100) / 100; 
							ml.setMaintenanceTime(MaintenanceTime);
							ml.setCompleteTime(new Date());
							maintenanceStaffRecordService.updateObj(ml);
							MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", ml.getCode());
							maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY,
									Constant.ReceiveType.SYSTEMGASSIGN);
							
						}
						return true;
					}
				}
			}
			throw new Exception("操作人员不可对此记录进行维修完成操作!");
		}
		return false;
	}
	@Override
	public Boolean addDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String username,
			HttpServletRequest request){
		User user = userService.queryByProperty("username", username);
		if (user.getEmployee() == null) {
			deviceRepairOrder.setInformant(username);
		} else {
			deviceRepairOrder.setInformant(user.getEmployee().getName());
			deviceRepairOrder.setInformantCode(user.getEmployee().getCode());
		}
		
		if(deviceRepairOrder.getOriginalFailSafeOperationNo()!=null&&deviceRepairOrder.getOriginalFailSafeOperationNo()!="") {
			DeviceRepair d1=deviceRepairOrderService.queryByProperty("serialNumber", deviceRepairOrder.getOriginalFailSafeOperationNo());
			if(d1!=null&&d1.getFailSafeOperation()&&!d1.getClosed()) {
				deviceRepairOrder.setClosed(true);
				d1.setReworkNo(deviceRepairOrder.getOriginalFailSafeOperationNo());
				deviceRepairOrderService.updateObj(d1);
			}else {
				throw new RuntimeException("无此带病运行单，请填写正确的原带病运行单号");
			}
		}
		
		boolean tf = false;
		if (deviceRepairOrder.getMaintainName() != null && !deviceRepairOrder.getMaintainName().equals("")) {// 指定维修人员
			addManualSendDeviceRepairOrder(deviceRepairOrder, idList, deviceRepairOrder.getMaintainCode(), request);
		}else { 
			tf=addAutoSendDeviceRepairOrder(deviceRepairOrder, idList, deviceRepairOrder.getInformant(), request);// 自动选择在岗状态维修人员
			if(!tf){
				deviceRepairOrder.setStatus(Constant.DeviceRepairStatus.WAITINGASSIGN);// 等待派单
			}
		}
		return true;
	}
	@Override
	public Boolean addPaperLessDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String employeeCode,
			HttpServletRequest request){
		Employee employee=employeeService.queryByProperty("code", employeeCode);
		deviceRepairOrder.setInformant(employee.getName());
		deviceRepairOrder.setInformantCode(employee.getCode());
		boolean tf = false;
		if (deviceRepairOrder.getMaintainName() != null && !deviceRepairOrder.getMaintainName().equals("")) {// 指定维修人员
			addManualSendDeviceRepairOrder(deviceRepairOrder, idList, employee.getCode(), request);
		} else {
			tf=addAutoSendDeviceRepairOrder(deviceRepairOrder, idList, employee.getName(), request);// 自动选择在岗状态维修人员
			if(!tf){
				deviceRepairOrder.setStatus(Constant.DeviceRepairStatus.WAITINGASSIGN);// 等待派单
			}
		}
		return true;
		
	}
	
	/**
	 * 自动派单
	 */
	@Override
	public boolean addAutoSendDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String name,
			HttpServletRequest request){
		Config cfg = configService.queryByProperty("key", "autoDispatch");
		Device d =deviceService.queryObjById(deviceRepairOrder.getDevice().getId());
		deviceRepairOrder.setProductionUnit(d.getProductionUnit());
		List<MaintenanceStaff> maintenanceStaff = maintenanceStaffService
				.queryListByProductionUnitIdAndStatus(deviceRepairOrder.getProductionUnit().getId(),Constant.MaintenanceStaffStatus.ONDUTY);
		if(maintenanceStaff.isEmpty()){
			maintenanceStaff = maintenanceStaffService.queryListByStatus(Constant.MaintenanceStaffStatus.ONDUTY);
		}
		System.out.println("autoDispatch："+ cfg.getValue());
		if (maintenanceStaff != null && maintenanceStaff.size() > 0&&cfg.getValue().equals("on")) { 
			for(int i=0;i<maintenanceStaff.size();i++){
				String mcode=maintenanceStaff.get(i).getCode();
				User u = userService.queryUserByEmployeeCode(mcode);
				if(u!=null){
					deviceRepairOrder.setStatus(Constant.DeviceRepairStatus.WAITINCOMFIRM);
					deviceRepairOrder.setMaintainName(maintenanceStaff.get(i).getName());
					deviceRepairOrder.setMaintainCode(maintenanceStaff.get(i).getCode());
					break;
				}else if(i==maintenanceStaff.size()-1){
					throw new RuntimeException("维修人员:"+maintenanceStaff.get(0).getName()+"员工没账号，操作失败");
				}
			}
		}
		DeviceRepair dro = new DeviceRepair();
		if(deviceRepairOrder.getId()==null){
			Serializable id;
			id = deviceRepairOrderService.addDeviceRepair(deviceRepairOrder, name);
	
			if (idList != null && !"".equals(idList)) {
				String[] paths = idList.split(",");
				for (String i : paths) {
					String dir = request.getServletContext().getRealPath("/");
					File file = new File(dir, i);
					deviceRepairOrderPicService.addDeviceRepairOrderPic((Long) id, file);
				}
			}
				NGMaintainRecord ng = new NGMaintainRecord();
				ng.setDeviceProject(deviceRepairOrder.getNgreason());
				ng.setNote(deviceRepairOrder.getNgDescription());
				dro.setId((Long) id);
				ng.setDeviceRepair(dro);
				ng.setCreateDate(new Date());
				ng.setStatus(false);
				ngMaintainRecordService.addObj(ng);
		}else{
			dro.setId(deviceRepairOrder.getId());
		}
		if (maintenanceStaff == null || maintenanceStaff.size() <1||cfg.getValue().equals("off")){
			return false;
		}
			MaintenanceStaffRecord maintenanceStaffRecord = new MaintenanceStaffRecord();
			User u=null;
			for(int i=0;i<maintenanceStaff.size();i++){
				String mcode=maintenanceStaff.get(i).getCode();
				u = userService.queryUserByEmployeeCode(mcode);
				if(u!=null){
					break;
				}else if(i==maintenanceStaff.size()-1){
					throw new RuntimeException("维修人员:"+maintenanceStaff.get(0).getName()+"员工没账号，操作失败");
				}
			}
			if(u!=null){
				maintenanceStaffRecord.setDeviceRepair(dro);
				maintenanceStaffRecord.setName(u.getEmployee().getName());
				maintenanceStaffRecord.setCode(u.getEmployee().getCode());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.SYSTEMGASSIGN);
				maintenanceStaffRecord.setAssignTime(new Date());
				
				maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
				deviceRepairOrderService.confirmDeviceRepair(deviceRepairOrder, u.getEmployee().getName(),
						Constant.DeviceRepairStatus.WAITINCOMFIRM);
				List<String> clientIdsList = new ArrayList<>();
				clientIdsList.add(u.getId() + "");
				try {
					//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN,
							//DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
	}
	/**
	 * 手动派单
	 */
	@Override
	public void addManualSendDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String code,
			HttpServletRequest request){
		
		deviceRepairOrder.setStatus(Constant.DeviceRepairStatus.WAITINCOMFIRM);
		MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", code);
		
		DeviceRepair dro = new DeviceRepair();
		if(deviceRepairOrder.getId()==null){
			Serializable id;
			id = deviceRepairOrderService.addDeviceRepair(deviceRepairOrder, maintenanceStaff.getName());
			
			if (idList != null && !"".equals(idList)) {
				String[] paths = idList.split(",");
				for (String i : paths) {
					String dir = request.getServletContext().getRealPath("/");
					File file = new File(dir, i);
					deviceRepairOrderPicService.addDeviceRepairOrderPic((Long) id, file);
				}
			}
			NGMaintainRecord ng = new NGMaintainRecord();
			ng.setDeviceProject(deviceRepairOrder.getNgreason());
			ng.setNote(deviceRepairOrder.getNgDescription());
			dro.setId((Long) id);
			ng.setDeviceRepair(dro);
			ng.setCreateDate(new Date());
			ng.setStatus(false);
			ngMaintainRecordService.addObj(ng);
		}else{
			dro.setId(deviceRepairOrder.getId());
		}
		MaintenanceStaffRecord maintenanceStaffRecord = new MaintenanceStaffRecord();
		User u = new User();
		u = userService.queryUserByEmployeeCode(code);
		maintenanceStaffRecord.setDeviceRepair(dro);
		maintenanceStaffRecord.setName(maintenanceStaff.getName());
		maintenanceStaffRecord.setCode(maintenanceStaff.getCode());
		maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.SYSTEMGASSIGN);
		maintenanceStaffRecord.setAssignTime(new Date());
		
		maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
		deviceRepairOrderService.confirmDeviceRepair(deviceRepairOrder, maintenanceStaff.getName(),
				Constant.DeviceRepairStatus.WAITINCOMFIRM);
		if(u!=null){
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(u.getId() + "");
			try {
				//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN,
				//		DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	   public long queryDeviceRepairOrderCountByDate(Date date) {
	      Calendar c = Calendar.getInstance();
	      c.setTime(date);
	      return deviceRepairOrderDao.findCount("from DeviceRepair dr where year(dr.createDate)=?0"
	            + " and month(createDate)=?1 and day(createDate)=?2", new Object[] {
	               c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE)	
	            });
	   }

	@Override
	public List<DeviceRepair> queryCompletedDeviceRepairWithUser(String code) {
		String hql = "from DeviceRepair dr where dr.status='MAINTAINCOMPLETE' and dr.maintainCode=?0"; 
		return deviceRepairOrderDao.findByHQL(hql,new Object[] {code});
	}

	@Override
	public int queryBadgeWithDeviceRepair(String hql) {
		
		return deviceRepairOrderDao.queryBadgeWithDeviceRepair(hql);
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairNumByDeviceProjectId(Long id) {
		String hql = "select dr from DeviceRepair dr where dr.ngreason.id=?0 ";
		return deviceRepairOrderDao.findByHQL(hql,new Object[]{id});
	}
}
