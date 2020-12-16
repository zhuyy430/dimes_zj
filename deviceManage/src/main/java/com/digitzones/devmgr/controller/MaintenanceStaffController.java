package com.digitzones.devmgr.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.constants.Constant;
import com.digitzones.constants.Constant.DeviceRepairStatus;
import com.digitzones.devmgr.model.EmployeeSchedulingRecord;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.service.IEmployeeSchedulingRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.vo.MaintenanceStaffVO;
import com.digitzones.model.Classes;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IClassesService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
@RestController
@RequestMapping("/maintenanceStaff")
public class MaintenanceStaffController {
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeSchedulingRecordService employeeSchedulingRecordService;
	@Autowired
	private IClassesService classesService; 
	
	/**
	 * 分页查询维修人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllMaintenanceStaff.do")
	@ResponseBody
	public ModelMap queryAllMaintenanceStaff(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<MaintenanceStaff> pager = maintenanceStaffService.queryObjs("from MaintenanceStaff", page, rows, new Object[] {});
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 添加员工信息
	 * @param employeeCodes
	 * @return
	 */
	@RequestMapping("/addMaintenanceStaff.do")
	public ModelMap addMaintenanceStaff(String employeeCodes) {
		ModelMap modelMap = new ModelMap();
		if(employeeCodes!=null) {
			if(employeeCodes.contains("[")) {
				employeeCodes = employeeCodes.replace("[", "");
			}
			if(employeeCodes.contains("]")) {
				employeeCodes = employeeCodes.replace("]", "");
			}

			if(employeeCodes.contains("\"")) {
				employeeCodes = employeeCodes.replace("\"", "");
			}
			List<String> clientIdsList = new ArrayList<>();
			String[] idArray = employeeCodes.split(",");
			for(String id:idArray){
				Employee emp = employeeService.queryObjById(id);
				
				MaintenanceStaff m = new MaintenanceStaff();
				m.setCode(emp.getCode());
				m.setName(emp.getName());
				m.setTel(emp.getTel());
				m.setOnDutyStatus(Constant.MaintenanceStaffStatus.REST);
				m.setWorkStatus(Constant.MaintenanceStaffStatus.REST);
				m.setQueue(0+"");
				m.setChangeDate(new Date());
				maintenanceStaffService.addObj(m);
				clientIdsList.add(id);
			}
			try {
				//PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public ModelMap update(MaintenanceStaff  maintenanceStaff){
		ModelMap modelMap = new ModelMap();
		MaintenanceStaff m = maintenanceStaffService.queryObjById(maintenanceStaff.getId());
		User u = userService.queryUserByEmployeeCode(m.getCode());
		if(u==null){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "员工未分配用户账号！更新失败！");
			return modelMap;
		}
		m.setQueue(maintenanceStaff.getQueue());
		m.setWorkStatus(maintenanceStaff.getWorkStatus());
		maintenanceStaffService.updateObj(m);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ModelMap delete(String id){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		maintenanceStaffService.deleteObj(Long.parseLong(id));
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "删除成功!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	
	/**
	 * 修改状态
	 * @return
	 */
	@RequestMapping("/updateStatus.do")
	@ResponseBody
	public ModelMap updateStatus(String id,String status,Principal principal){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		User user = userService.queryUserByUsername(principal.getName());
		String operator = user.getUsername();
		if(user.getEmployee()!=null){
			operator = user.getEmployee().getName();
		}
		maintenanceStaffService.updateStatus(Long.parseLong(id),status,operator);
		
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryById.do")
	@ResponseBody
	public MaintenanceStaff queryById(Long id){
		return maintenanceStaffService.queryObjById(id);
	}
	
	/**
	 * 查询维修人员
	 * @return
	 */
	@RequestMapping("/queryAllMaintenanceStaffByStatus.do")
	@ResponseBody
	public List<MaintenanceStaff> queryAllMaintenanceStaffByStatus(String status){
		List<MaintenanceStaff> mList = maintenanceStaffService.queryListByStatus(status);
		return mList;
	}
	/**
	 * 查询所有维修人员
	 * @return
	 */
	@RequestMapping("/queryMaintenanceStaffAll.do")
	@ResponseBody
	public List<MaintenanceStaff> queryAllMaintenanceStaffAll(){
		List<MaintenanceStaff> mList = maintenanceStaffService.queryAllMaintenanceStaff();
		return mList;
	}
	/**
	 * 根据code查询维修人员
	 */
	@RequestMapping("/queryMaintenanceStaffByCode.do")
	@ResponseBody
	public MaintenanceStaff queryMaintenanceStaffByCode(String Code) {
		MaintenanceStaff m=maintenanceStaffService.queryByProperty("code", Code);
		return m;
	}
	
	/**
	 * 查询所有维修人员
	 * @return
	 */
	@RequestMapping("/queryMaintenanceStaffVOAll.do")
	@ResponseBody
	public List<MaintenanceStaffVO> queryAllMaintenanceStaffVOAll(){
		List<MaintenanceStaff> ListMaintenanceStaff = maintenanceStaffService.queryAllMaintenanceStaff();
		return model2VO(ListMaintenanceStaff);
	}
	
	private List<MaintenanceStaffVO> model2VO(List<MaintenanceStaff> mList){
		List<MaintenanceStaffVO> list = new ArrayList<>();
		for(MaintenanceStaff m:mList){
			MaintenanceStaffVO vo = new MaintenanceStaffVO();
			BeanUtils.copyProperties(m, vo);
			List<EmployeeSchedulingRecord> scheduling = employeeSchedulingRecordService.queryEmployeeSchedulingRecordsByTodayAndCode(m.getCode());
			if(!scheduling.isEmpty()){
				EmployeeSchedulingRecord record = scheduling.get(0);
				Classes classes = classesService.queryByProperty("code", record.getClassCode());
				vo.setClasses(classes);
			}
			list.add(vo);
		}
		return list;
	}
}
