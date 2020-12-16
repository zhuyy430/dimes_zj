package com.digitzones.controllers;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
/**
 * 对应工作区的功能
 * @author zdq
 * 2018年8月10日
 */
@Controller
@RequestMapping("/home")
public class HomeController {
	private IDeviceSiteService deviceSiteService;
	private IRoleService roleService;
	private IWorkFlowService workFlowService;
	@Autowired
	private IUserService userService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	public void setWorkFlowService(IWorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}
	@Autowired
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}
	/**
	 * 查询正在运行的设备站点数
	 * @return
	 */
	@RequestMapping("/queryRunningDeviceSiteCount.do")
	@ResponseBody
	public ModelMap queryRunningDeviceSiteCount() {
		ModelMap modelMap  = new ModelMap();
		long count = deviceSiteService.queryCountOfDeviceSiteByStatus(Constant.DeviceSite.RUNNING);
		modelMap.addAttribute("count", count);
		return modelMap;
	}
}
