package com.digitzones.devmgr.controller;
 
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.Config;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IConfigService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.devmgr.service.ITransferRecordService;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;
 
@RestController
@RequestMapping("deviceManageHome")
public class DeviceManageHome {
    @Autowired
    private IMaintenanceStaffService maintenanceStaffService;
    @Autowired
    private IMaintenancePlanRecordService maintenancePlanRecordService;
    @Autowired
    @Qualifier("deviceRepairOrderServiceImpl")
    private IDeviceRepairOrderService deviceRepairOrderService;
    @Autowired
    private ITransferRecordService transferRecordService;
    @Autowired
    private IMaintenanceStaffRecordService maintnenanceStaffRecordService;
    @Autowired
    private IMaintenanceUserService maintenanceUserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IConfigService configService;
    /**
     * 设备管理首页
     * @return
     */
    @RequestMapping("index.do")
    public ModelMap index(HttpServletRequest request,Principal principal) {
        ModelMap modelMap = new ModelMap();
        Date date = new Date();
        //查找维修人员在岗人数
        Long countOnPosition = maintenanceStaffService.queryCountOnPosition();
        //查找当天计划保养记录数
        Long maintenancePlanRecordCount = maintenancePlanRecordService.queryMaintenancePlanRecordCountByDate(date);
        //查找当天新增维修单数
        long deviceRepairOrderCount = deviceRepairOrderService.queryDeviceRepairOrderCountByDate(date);
        //查找当天维修转单数
        long transferRecordCount = transferRecordService.queryTransferRecordCountByDate(date);
        //读取是否自动派单
        Config cfg = configService.queryByProperty("key", "autoDispatch");
        if(cfg==null) {
        	modelMap.addAttribute("autoDispatch", "on");
        }else {
        	modelMap.addAttribute("autoDispatch", cfg.getValue());
        }
        String username = principal.getName();
        User user = userService.queryByProperty("username", username);
       Employee employee = user.getEmployee();
        //查找待接单维修单
        List<MaintenanceStaffRecord> deviceRepairReceipting = maintnenanceStaffRecordService.queryWithDeviceRepairStatus(Constant.DeviceRepairStatus.WAITINCOMFIRM,employee==null?"":employee.getCode());
        //查找待确认维修单
        List<MaintenanceStaffRecord> deviceRepairConfirming = maintnenanceStaffRecordService.queryWithDeviceRepairStatus(Constant.DeviceRepairStatus.WAITWORKSHOPCOMFIRM,employee==null?"":employee.getCode());
        //查找待接单保养记录
        List<MaintenanceUser> maintenancePlanRecordReceipting = maintenanceUserService.queryWithMaintenancePlanRecordStatus(Constant.Status.MAINTENANCEPLAN_RECEIPT,employee==null?"":employee.getCode());
        //查找待确认保养记录
        List<MaintenanceUser> maintenancePlanRecordConfirming = maintenanceUserService.queryWithMaintenancePlanRecordStatus(Constant.Status.MAINTENANCEPLAN_TOBECONFIRMED,employee==null?"":employee.getCode());
        modelMap.addAttribute("countOnPosition", countOnPosition);
        modelMap.addAttribute("maintenancePlanRecordCount", maintenancePlanRecordCount);
        modelMap.addAttribute("deviceRepairOrderCount", deviceRepairOrderCount);
        modelMap.addAttribute("transferRecordCount", transferRecordCount);
        modelMap.addAttribute("deviceRepairReceipting", deviceRepairReceipting);
        modelMap.addAttribute("deviceRepairConfirming", deviceRepairConfirming);
        modelMap.addAttribute("maintenancePlanRecordReceipting", maintenancePlanRecordReceipting);
        modelMap.addAttribute("maintenancePlanRecordConfirming", maintenancePlanRecordConfirming);
        return modelMap;
    }
    /**
     * 禁用或启用自动派单
     */
    @RequestMapping("switchAutoDispatch.do")
    public String switchAutoDispatch(Principal principal) {
        Config cfg = configService.queryByProperty("key", "autoDispatch");
        if(cfg.getValue().equals("on")){
        	cfg.setValue("off");
        	configService.updateObj(cfg);
        }else{
        	cfg.setValue("on");
        	configService.updateObj(cfg);
        	//获取所有等待派单
        	String username = principal.getName();
        	User user = userService.queryUserByUsername(username);
        	if(user.getEmployee()!=null){
        		username = user.getEmployee().getName();
        	}
        	List<DeviceRepair> repairList = deviceRepairOrderService.queryDeviceRepairWithStatus(Constant.DeviceRepairStatus.WAITINGASSIGN);
        	for(DeviceRepair repair:repairList){
        		deviceRepairOrderService.addAutoSendDeviceRepairOrder(repair, null, username, null);
        	}
        }
        return cfg.getValue();
    }
}