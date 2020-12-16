package com.digitzones.quartz;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.DeviceRepairCount;
import com.digitzones.devmgr.service.IDeviceRepairCountService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.model.DispatchedLevel;
import com.digitzones.model.User;
import com.digitzones.service.IAppClientMapService;
import com.digitzones.service.IDispatchedLevelService;
import com.digitzones.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 保修单报警
 * @author zhuyy430
 *
 */
@Component("CheckingDeviceRepairTask")
public class CheckingDeviceRepairTask {
	@Autowired
	@Qualifier("deviceRepairOrderServiceImpl")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IDeviceRepairCountService deviceRepairCountService;
	@Autowired
	private IDispatchedLevelService dispatchedLevelService;
	@Autowired
	IAppClientMapService appClientMapService; 
	@Autowired
	private IUserService userService;
	public void CheckingDeviceRepairTask() {
		DeviceRepairCount deviceRepairCount = deviceRepairCountService.queryDeviceRepairCount();
		if(deviceRepairCount==null){
			DeviceRepairCount dCount = new DeviceRepairCount();
			List<DeviceRepair> dlist = deviceRepairOrderService.queryAlarmsCount("");
			List<DeviceRepair> all = deviceRepairOrderService.queryAllDeviceRepair();
			List<DeviceRepair> comlist = deviceRepairOrderService.queryListByProperty("status", Constant.DeviceRepairStatus.MAINTAINCOMPLETE);
			String ids = "";
			int count = 0;
			for(DeviceRepair d:dlist){
				if(new Date().getTime()-d.getCreateDate().getTime()>30*60*1000){
					if(ids.equals("")){
						ids=d.getId()+"";
					}else{
						ids+=","+d.getId();
					}
					count++;
				}
			}
			dCount.setCreateTime(new Date());
			dCount.setAlarmeds(ids);
			dCount.setNewAlarms((long)count);
			dCount.setRepairCompleted((long)comlist.size());
			
			dCount.setOutstandingAlarms((long)all.size()-comlist.size());
			
			deviceRepairCountService.addObj(dCount);
		}else{
			List<DeviceRepair> dlist = deviceRepairOrderService.queryAlarmsCount(deviceRepairCount.getAlarmeds());
			List<DeviceRepair> all = deviceRepairOrderService.queryAllDeviceRepair();
			List<DeviceRepair> comlist = deviceRepairOrderService.queryListByProperty("status", Constant.DeviceRepairStatus.MAINTAINCOMPLETE);
			String ids = deviceRepairCount.getAlarmeds();
			int count = 0;
			for(DeviceRepair d:dlist){
				if(new Date().getTime()-d.getCreateDate().getTime()>30*60*1000){
					if(ids.equals("")){
						ids=d.getId()+"";
					}else{
						ids+=","+d.getId();
					}
					count++;
				}
			}
			deviceRepairCount.setAlarmeds(ids);
			deviceRepairCount.setNewAlarms(deviceRepairCount.getNewAlarms()+count);
			deviceRepairCount.setOutstandingAlarms((long)all.size()-comlist.size());
			deviceRepairCountService.updateObj(deviceRepairCount);
		}
			
	}
	//检查未派单维修单的超时情况
	public void CheckingDeviceRepairTaskAndPushMessage() {
		List<DeviceRepair> repairList = deviceRepairOrderService.queryDeviceRepairWithStatus(Constant.DeviceRepairStatus.WAITINGASSIGN);
		List<DispatchedLevel> levelList = dispatchedLevelService.queryDispatchedLevelWithStatus("维修");
		for(DeviceRepair repair:repairList){
			if(!repair.getPushStatus()){
				for(int i=0;i<levelList.size();i++){
					if((new Date().getTime() - repair.getCreateDate().getTime())<levelList.get(i).getTiming()*60000){
						if(i==0)return;
						else{
							repair.setDispatchedLevel(levelList.get(i-1));
							repair.setPushStatus(true);
							deviceRepairOrderService.updateObj(repair);
							
							//推送给报修人
							User InformantUser =userService.queryUserByEmployeeCode(levelList.get(i-1).getEmployee().getCode());
							List<String> clientIdsList = new ArrayList<>();
							clientIdsList.add(InformantUser.getUsername()+"");
							clientIdsList=appClientMapService.queryCids(clientIdsList);
							try {
								//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_NOSEND, DeviceRepairStatus.DEVICEREPAIRCONTENT_NOSEND);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else if(i+1==levelList.size()&&(new Date().getTime() - repair.getCreateDate().getTime())>levelList.get(i).getTiming()*60000){
						repair.setDispatchedLevel(levelList.get(i));
						repair.setPushStatus(true);
						deviceRepairOrderService.updateObj(repair);
						
						//推送给报修人
						User InformantUser =userService.queryUserByEmployeeCode(levelList.get(i-1).getEmployee().getCode());
						List<String> clientIdsList = new ArrayList<>();
						clientIdsList.add(InformantUser.getUsername()+"");
						clientIdsList=appClientMapService.queryCids(clientIdsList);
						try {
						//	PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_NOSEND, DeviceRepairStatus.DEVICEREPAIRCONTENT_NOSEND);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
