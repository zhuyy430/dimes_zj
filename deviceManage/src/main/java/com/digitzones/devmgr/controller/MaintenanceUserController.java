package com.digitzones.devmgr.controller;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.digitzones.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;
@RestController
@RequestMapping("/maintenanceUser")
public class MaintenanceUserController {
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 根据保养计划记录id查找备品人员信息
	 * @param recordId
	 * @param rows
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMaintenanceUserByMaintenancePlanRecordId.do")
	public ModelMap queryMaintenanceUserByMaintenancePlanRecordId(Long recordId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) throws ParseException {
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceUser> pager = maintenanceUserService.queryObjs("from MaintenanceUser ms where ms.maintenancePlanRecord.id=?0", page, rows, new Object[] {recordId});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据保养计划记录id查找备品人员信息
	 * @param recordId
	 * @return
	 */
	@RequestMapping("/queryMaintenanceUserByRecordId.do")
	public List<MaintenanceUser> queryMaintenanceUserByRecordId(Long recordId) {
		//Pager<MaintenanceUser> pager = maintenanceUserService.queryObjs("from MaintenanceUser ms where ms.maintenancePlanRecord.id=?0", page, rows, new Object[] {recordId});
		return maintenanceUserService.queryMaintenanceUserByRecordId(recordId);
	}
	/**
	 * 添加责任人
	 * @param maintenanceUser
	 * @param principal
	 * @return
	 */
	@RequestMapping("/addMaintenanceUser.do")
	public ModelMap addMaintenanceUser(MaintenanceUser maintenanceUser,Principal principal) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", principal.getName());
		Employee employee = user.getEmployee();
		if(employee==null) {
			employee = new Employee();
			employee.setCode(user.getUsername());
			employee.setName(user.getUsername());
		}
		maintenanceUser.setDispatchUsercode(employee.getCode());
		maintenanceUser.setDispatchUsername(employee.getName());
		maintenanceUser.setDispatchDate(new Date());
		try {
		maintenanceUserService.addMaintenanceUser(maintenanceUser,employee);
		}catch(RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", e.getMessage());
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 删除保养人
	 * @return
	 */
	@RequestMapping("/deleteMaintenanceUser.do")
	public ModelMap deleteMaintenanceUser(String id){
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		maintenanceUserService.deleteObj(Long.parseLong(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 查询保养人
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryMaintenanceUserById.do")
	public MaintenanceUser queryMaintenanceUserById(Long id){
		return maintenanceUserService.queryObjById(id);
	}
	/**
	 * 更新保养人
	 * @param maintenanceUser
	 * @return
	 */
	@RequestMapping("/updateMaintenanceUser.do")
	public ModelMap updateMaintenanceUser(MaintenanceUser maintenanceUser){
		ModelMap modelMap = new ModelMap();
		MaintenanceUser m = maintenanceUserService.queryObjById(maintenanceUser.getId());
		if(maintenanceUser.getOccupyTime()!=null){
			m.setOccupyTime((double) Math.round((maintenanceUser.getOccupyTime().doubleValue()) * 100) / 100);
		}else{
			m.setOccupyTime(null);
		}
		m.setCode(maintenanceUser.getCode());
		m.setName(maintenanceUser.getName());
		m.setOrderType(maintenanceUser.getOrderType());
		maintenanceUserService.updateObj(m);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
}
