package com.digitzones.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.digitzones.app.model.Task;
import com.digitzones.app.model.TaskPic;
import com.digitzones.app.model.UserTask;
import com.digitzones.app.service.IAppTaskPicService;
import com.digitzones.app.service.IAppTaskService;
import com.digitzones.app.service.IAppUserTaskService;
import com.digitzones.constants.Constant.AppTaskStatus;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IAppClientMapService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
import com.digitzones.util.PushtoAPP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/AppTask")
public class AppTaskController {
	@Autowired
	IAppTaskService TaskService;
	@Autowired
	IAppUserTaskService UserTaskService; 
	@Autowired
	IEmployeeService employeeService; 
	@Autowired
	IUserService userService; 
	@Autowired
	IAppTaskPicService taskPicService; 
	@Autowired
	IAppClientMapService appClientMapService; 
	
	@RequestMapping("/queryTask.do")
	@ResponseBody
	public List<Task> queryTask(@RequestBody JSONObject data ,HttpServletRequest request){
		Long taskId= data.getLong("taskId");
	      String status= data.getString("status");
	      Long userId= data.getLong("userId");
		List<Task> tasks = new ArrayList<>();
		//单个任务的查看
		if(taskId!=null&&!"".equals(taskId)){
			Task task = TaskService.queryObjById(taskId);
			List<Employee> empList = new ArrayList<>();
			List<UserTask> userTaskList = UserTaskService.queryListByTaskId(taskId);
			for(UserTask userTask:userTaskList){
				User user = userService.queryObjById(userTask.getUserId());
				Employee employee = employeeService.queryObjById(user.getEmployee().getCode());
				if(null!=employee&&user.getId()!=task.getUserId()){
					empList.add(employee);
				}
			}
			List<TaskPic> TPList = taskPicService.queryListByProperty(taskId);
			for(TaskPic TP:TPList){
				List<String> pnList = task.getPicName();
				pnList.add(TP.getPicName());
				task.setPicName(pnList);
				String dir = request.getServletContext().getRealPath("/");
				try {
					InputStream is;
					is = TP.getPic().getBinaryStream();
					File out = new File(dir,TP.getPicName());
					FileCopyUtils.copy(is, new FileOutputStream(out));
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}
			}
			if(task!=null){
				task.setCcpList(empList);
				tasks.add(task);
			}
			return tasks;
		}else{
			//查看创建的任务
			if(AppTaskStatus.TASKSTATUS_CREATE.equalsIgnoreCase(status)){
				tasks = TaskService.queryTaskByCreateUserid(userId);
				for(Task task:tasks){
					List<Employee> empList = new ArrayList<>();
					List<TaskPic> TPList = taskPicService.queryListByProperty(task.getId());
					if(TPList!=null&&!TPList.isEmpty()) {
						for(TaskPic TP:TPList){
							if(TP.getPic()!=null){
								List<String> pnList = task.getPicName();
								pnList.add(TP.getPicName());
								task.setPicName(pnList);
								String dir = request.getServletContext().getRealPath("/");
								try {
									InputStream is;
									is = TP.getPic().getBinaryStream();
									File out = new File(dir,TP.getPicName());
									FileCopyUtils.copy(is, new FileOutputStream(out));
								} catch (SQLException | IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
					List<UserTask> userTaskList = UserTaskService.queryListByTaskId(task.getId());
					for(UserTask userTask:userTaskList){
						if(userTask.getUserId()!=task.getUserId()){
							User user = userService.queryObjById(userTask.getUserId());
							Employee employee = employeeService.queryObjById(user.getEmployee().getCode());
							if(null!=employee){
								empList.add(employee);
							}
						}
					}
					task.setCcpList(empList);
				}
				//收到的任务
			}else if(AppTaskStatus.TASKSTATUS_RECEIVE.equalsIgnoreCase(status)){
				tasks = TaskService.queryTaskByStatusAndUserId(AppTaskStatus.TASKSTATUS_CREATE,userId);
				for(Task task:tasks){
					List<Employee> empList = new ArrayList<>();
					List<TaskPic> TPList = taskPicService.queryListByProperty(task.getId());
					if(TPList!=null&&!TPList.isEmpty()) {
						for(TaskPic TP:TPList){
							List<String> pnList = task.getPicName();
							pnList.add(TP.getPicName());
							task.setPicName(pnList);
							String dir = request.getServletContext().getRealPath("/");
							try {
								InputStream is;
								is = TP.getPic().getBinaryStream();
								File out = new File(dir,TP.getPicName());
								FileCopyUtils.copy(is, new FileOutputStream(out));
							} catch (SQLException | IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					List<UserTask> userTaskList = UserTaskService.queryListByTaskId(task.getId());
					for(UserTask userTask:userTaskList){
						if(userTask.getUserId()!=task.getUserId()){
							User user = userService.queryObjById(userTask.getUserId());
							Employee employee = employeeService.queryObjById(user.getEmployee().getCode());
							if(null!=employee){
								empList.add(employee);
							}
						}
					}
					task.setCcpList(empList);
				}
				//完成的任务
			}else if(AppTaskStatus.TASKSTATUS_COMPLETE.equalsIgnoreCase(status)){
				tasks = TaskService.queryTaskByStatusAndUserId(AppTaskStatus.TASKSTATUS_COMPLETE,userId);
				//tasks = TaskService.queryTaskByTreatUserid(userId);
				for(Task task:tasks){
					List<Employee> empList = new ArrayList<>();
					List<TaskPic> TPList = taskPicService.queryListByProperty(task.getId());
					if(TPList!=null&&!TPList.isEmpty()) {
						for(TaskPic TP:TPList){
							List<String> pnList = task.getPicName();
							pnList.add(TP.getPicName());
							task.setPicName(pnList);
							String dir = request.getServletContext().getRealPath("/");
							try {
								InputStream is;
								is = TP.getPic().getBinaryStream();
								File out = new File(dir,TP.getPicName());
								FileCopyUtils.copy(is, new FileOutputStream(out));
							} catch (SQLException | IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					List<UserTask> userTaskList = UserTaskService.queryListByTaskId(task.getId());
					for(UserTask userTask:userTaskList){
						if(userTask.getUserId()!=task.getUserId()){
							User user = userService.queryObjById(userTask.getUserId());
							Employee employee = employeeService.queryObjById(user.getEmployee().getCode());
							if(null!=employee){
								empList.add(employee);
							}
						}
					}
					task.setCcpList(empList);
				}
			}
			return tasks;
		}
	}
	
	/**
	 * 执行->走动管理  (分页)
	 */
	@RequestMapping("/queryImplementTask.do")
	@ResponseBody
	public ModelMap queryImplementTask(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long taskId,Long userId,String status,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		String hql="from Task at where at.id in (select aut.taskId from UserTask aut where aut.userId =?1) and at.status=?0";
		List<Object> paramList = new ArrayList<>();
		paramList.add(status);
		paramList.add(userId);
		Pager<Task> pager=TaskService.queryObjs(hql, page, rows, paramList.toArray());
		for(Task task:pager.getData()){
			List<Employee> empList = new ArrayList<>();
			List<TaskPic> TPList = taskPicService.queryListByProperty(task.getId());
			if(TPList!=null&&!TPList.isEmpty()) {
				for(TaskPic TP:TPList){
					List<String> pnList = task.getPicName();
					pnList.add(TP.getPicName());
					task.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = TP.getPic().getBinaryStream();
						File out = new File(dir,TP.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			List<UserTask> userTaskList = UserTaskService.queryListByTaskId(task.getId());
			for(UserTask userTask:userTaskList){
				if(userTask.getUserId()!=task.getUserId()){
					User user = userService.queryObjById(userTask.getUserId());
					Employee employee = user.getEmployee();
					if(null!=employee){
						empList.add(employee);
					}
				}
			}
			task.setCcpList(empList);
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	
	
	@RequestMapping("/CompleteTask.do")
	@ResponseBody
	public ModelMap CompleteTask(@RequestBody JSONObject data){
		Long taskId = data.getLong("taskId");
		String treatDescription = data.getString("treatDescription");
		Long userId = data.getLong("userId");
		
		ModelMap modelMap = new ModelMap();
		Task task = TaskService.queryObjById(taskId);
		if(null!=task&&task.getStatus().equalsIgnoreCase(AppTaskStatus.TASKSTATUS_CREATE)&&task.getUserId()==userId){
			List<String> clientIdsList = new ArrayList<>();
			Employee emp = employeeService.queryObjById(userId);
			List<UserTask> userTask = UserTaskService.queryListByTaskId(taskId);
			for(UserTask ut:userTask){
				User u = userService.queryObjById(ut.getUserId());
				clientIdsList.add(u.getUsername());
			}
			 User createUser = userService.queryObjById(task.getCreateUserid());
			clientIdsList.add(createUser.getUsername());
			task.setTreatUserName(emp.getName());
			task.setStatus(AppTaskStatus.TASKSTATUS_COMPLETE);
			task.setTreatUserid(userId);
			task.setTreatDescription(treatDescription);
			TaskService.updateObj(task);
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, AppTaskStatus.TASKTITLE_COMPLETE, AppTaskStatus.TASKCONTENT_COMPLETE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "处理成功!");
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("msg", "处理失败!");
		return modelMap;
	}
	
	@RequestMapping("/addTask.do")
	@ResponseBody
	public ModelMap addTask(@RequestParam("files")List<MultipartFile> files,String manageType,String ccp,String description,Long userId,Long createUserid,HttpServletRequest request) {
		Task task = new Task();
		task.setManageType(manageType);
		task.setDescription(description);
		task.setUserId(userId);
		task.setCreateUserid(createUserid);
		task.setCreatetime(new Date());
		
		ModelMap modelMap = new ModelMap();
		if(createUserid!=null){
				List<String> clientIdsList = new ArrayList<>();
				User creatuser = userService.queryObjById(createUserid);
				User user = userService.queryObjById(userId);
				task.setUserName(user.getEmployee().getName());
				task.setCode(user.getEmployee().getCode());
				task.setCreateUserName(creatuser.getEmployee().getName());
				task.setCreateUserCode(creatuser.getEmployee().getCode());
				task.setStatus("CREATE");
				Serializable ret = TaskService.addTask(task);
				
				if(ccp==null||"".equals(ccp)){
					UserTask userTask = new UserTask();
					userTask.setTaskId(Long.parseLong(ret.toString()));
					userTask.setUserId(userId);
					UserTaskService.addObj(userTask);
					clientIdsList.add(user.getUsername());
				}else{
					String[] ids = (userId+","+ccp).split(",");
					for(String id:ids){
						UserTask userTask = new UserTask();
						UserTask ut = UserTaskService.queryByTaskIdAndUserId(Long.parseLong(ret.toString()), Long.parseLong(id));
						if(ut==null){
							userTask.setTaskId(Long.parseLong(ret.toString()));
							userTask.setUserId(Long.parseLong(id));
							UserTaskService.addObj(userTask);
						}
						User u = userService.queryObjById(Long.parseLong(id));
						clientIdsList.add(u.getUsername());
					}
				}
				clientIdsList=appClientMapService.queryCids(clientIdsList);
				try {
					PushtoAPP.pushMessage(clientIdsList, AppTaskStatus.TASKTITLE_ASSIGN, AppTaskStatus.TASKCONTENT_ASSIGN);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(files!=null && files.size()>0) {
					String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
					for(MultipartFile file : files) {
						String picName = file.getOriginalFilename();
						InputStream is;
						try {
							is = file.getInputStream();
							File out = new File(dir,picName);
							//FileUtils.copyInputStreamToFile(is, out);
							FileCopyUtils.copy(is, new FileOutputStream(out));
							TaskPic TP = new TaskPic();
							TP.setPicName("console/deviceImgs/" + picName);
							TP.setTaskId(Long.parseLong(ret.toString()));
							taskPicService.addTaskPic(TP,out);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("msg", "添加失败!");
		return modelMap;
	}
	/**
	 * 添加走动管理（无图片）
	 */
	@RequestMapping("/addTaskMassage.do")
	@ResponseBody
	public ModelMap addTaskMassage(String manageType,String ccp,String description,Long userId,Long createUserid,HttpServletRequest request) {
		Task task = new Task();
		task.setManageType(manageType);
		task.setDescription(description);
		task.setUserId(userId);
		task.setCreateUserid(createUserid);
		task.setCreatetime(new Date());
		
		ModelMap modelMap = new ModelMap();
		if(createUserid!=null){
				List<String> clientIdsList = new ArrayList<>();
				User creatuser = userService.queryObjById(createUserid);
				User user = userService.queryObjById(userId);
				task.setUserName(user.getEmployee().getName());
				task.setCode(user.getEmployee().getCode());
				task.setCreateUserName(creatuser.getEmployee().getName());
				task.setCreateUserCode(creatuser.getEmployee().getCode());
				task.setStatus("CREATE");
				Serializable ret = TaskService.addTask(task);
				
				if(ccp==null||"".equals(ccp)){
					UserTask userTask = new UserTask();
					userTask.setTaskId(Long.parseLong(ret.toString()));
					userTask.setUserId(userId);
					UserTaskService.addObj(userTask);
					clientIdsList.add(user.getUsername());
				}else{
					String[] ids = (userId+","+ccp).split(",");
					for(String id:ids){
						UserTask userTask = new UserTask();
						UserTask ut = UserTaskService.queryByTaskIdAndUserId(Long.parseLong(ret.toString()), Long.parseLong(id));
						if(ut==null){
							userTask.setTaskId(Long.parseLong(ret.toString()));
							userTask.setUserId(Long.parseLong(id));
							UserTaskService.addObj(userTask);
						}
						User u = userService.queryObjById(Long.parseLong(id));
						clientIdsList.add(u.getUsername());
					}
				}
				clientIdsList=appClientMapService.queryCids(clientIdsList);
				try {
					PushtoAPP.pushMessage(clientIdsList, AppTaskStatus.TASKTITLE_ASSIGN, AppTaskStatus.TASKCONTENT_ASSIGN);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
			modelMap.addAttribute("taskId", Long.parseLong(ret.toString()));
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("msg", "添加失败!");
		return modelMap;
	}
	
	
	
	/**
	 * 添加图片
	 * @return
	 */
	@RequestMapping("/addTaskPic.do")
	@ResponseBody
	public ModelMap addTaskPic(@RequestParam("file")MultipartFile file,Long taskId,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		if(file!=null) {
			String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
				String picName = file.getOriginalFilename();
				InputStream is;
				try {
					is = file.getInputStream();
					File out = new File(dir,picName);
					//FileUtils.copyInputStreamToFile(is, out);
					FileCopyUtils.copy(is, new FileOutputStream(out));
					TaskPic TP = new TaskPic();
					TP.setPicName("console/deviceImgs/" + picName);
					TP.setTaskId(taskId);
					taskPicService.addTaskPic(TP,out);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	
	
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	/*private EmployeeVO model2vo(Employee e) {
		if(e == null) {
			return null;
		}

		EmployeeVO vo = new EmployeeVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setNote(e.getNote());
		vo.setPhoto(e.getPhoto());
		vo.setCode(e.getCode());
		vo.setDisabled(e.getDisabled());

		if(e.getPosition()!=null) {
			Position p = (Position)e.getPosition();
			vo.setPositionId(p.getId());
			vo.setPositionCode(p.getCode());
			vo.setPositionName(p.getName());
			
			if(p.getDepartment()!=null) {
				vo.setDepartmentCode(p.getDepartment().getCode());
				vo.setDepartmentId(p.getDepartment().getId());
				vo.setDepartmentName(p.getDepartment().getName());
			}
		}
		if(e.getProductionUnit()!=null) {
			ProductionUnit pu = e.getProductionUnit();
			vo.setProductionUnitId(pu.getId());
			vo.setProductionUnitName(pu.getName());
		}

		return vo;
	}*/
}
