package com.digitzones.app.controller;

import com.digitzones.constants.Constant;
import com.digitzones.constants.Constant.DeviceRepairStatus;
import com.digitzones.constants.Constant.Status;
import com.digitzones.devmgr.model.MaintenanceItem;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.MaintenanceUser;
import com.digitzones.devmgr.service.IMaintenanceItemService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.IMaintenanceUserService;
import com.digitzones.devmgr.vo.MaintenancePlanRecordVO;
import com.digitzones.model.*;
import com.digitzones.service.*;
import com.digitzones.util.PushtoAPP;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/AppMaintenancePlanRecord")
public class AppMaintenancePlanRecordController {
	private DecimalFormat decimalFormat = new DecimalFormat("#####.00");
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IMaintenancePlanRecordService  maintenancePlanRecordService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private IMaintenanceItemService maintenanceItemService;
	@Autowired
	private IMaintenanceUserService maintenanceUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	private IRelatedDocumentTypeService relatedDocumentTypeService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat aformat = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	IAppClientMapService appClientMapService; 
	/**
	 * 根据设备编码,使用者和当前日期查找设备保养记录和项目
	 */
	/*@RequestMapping("/queryMaintenancePlanRecordAndItemByDeviceCodeAndToday.do")
	@ResponseBody
	public ModelMap queryMaintenancePlanRecordAndItemByDeviceCodeAndToday(String deviceCode,String name){
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		Calendar c = Calendar.getInstance();
		Classes classes=classesService.queryCurrentClasses();
		List<MaintenancePlanRecord> recordList=maintenancePlanRecordService.queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(device.getId(),c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH),name,classes.getCode());
		
		if(recordList.size()>0&&recordList!=null) {
			ModelMap recordListMap=new ModelMap();
			for(int i=0;i<recordList.size();i++) {
				ModelMap recordMap=new ModelMap();
				MaintenancePlanRecord record=recordList.get(i);
				if(record!=null) {
					//根据设备编码查询文档信息
					if(device!=null) {
						Long deviceId = device.getId();
						//查找关联文档
						List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, deviceId);
						modelMap.addAttribute("docs", docs);
						
						//根据设备编码查找保养记录项信息
						List<MaintenanceItem> list = maintenanceItemService.queryMaintenanceItemByMaintenancePlanRecordId(record.getId());
						int count =maintenanceItemService.queryResultCountByMaintenanceItemId(record.getId());
						recordMap.addAttribute("count", count);
						recordMap.addAttribute("items", list);
						recordMap.addAttribute("record",record);
						recordListMap.addAttribute(i+"",recordMap);
					}
				}
				
			}
			modelMap.addAttribute("list",recordListMap);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该设备当天没有保养记录");
		}
		
		return modelMap;
	}*/
	/**
	 * 根据设备编码查询保养计划记录
	 */
	@RequestMapping("/queryMaintenancePlanRecordByDeviceCode.do")
	@ResponseBody
	public ModelMap queryMaintenancePlanRecordByDeviceCode(String deviceCode) {
		ModelMap modelMap = new ModelMap();
		List<MaintenancePlanRecord> list=maintenancePlanRecordService.queryAllMaintenancePlanRecordsByDeviceCode(deviceCode);
		modelMap.addAttribute("records", list);
		return modelMap;
	}
	/**
	 * 根据设备代码和用户姓名查询当日之前所有有保养计划的日期
	 */
	/*@RequestMapping("/queryAllMaintenancePlanRecordmaintianDateByToday.do")
	@ResponseBody
	public ModelMap queryAllMaintenancePlanRecordmaintianDateByToday(String name,String deviceCode) {
		ModelMap modelMap=new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		List<Object[]> list=maintenancePlanRecordService.queryAllMaintenancePlanRecordmaintianDateByToday(name,device.getId());
		List<String> list1=new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			list1.add(fmt.format(list.get(i)));
		}
		modelMap.addAttribute("timesList", list1);
		return modelMap;
	}*/
	/**
	 * 根据设备编码,使用者和选择的日期查找设备保养记录
	 */
	/*@RequestMapping("/queryMaintenancePlanRecordByDeviceCodeAndSelectday.do")
	@ResponseBody
	public ModelMap queryMaintenancePlanRecordByDeviceCodeAndSelectday(String deviceCode,String name,String time){
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		Date date=null;
		try {
			 date = fmt.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		List<MaintenancePlanRecord> recordList=maintenancePlanRecordService.queryMaintenancePlanRecordTodayByDeviceIdAndEmployName(device.getId(),(c.get(Calendar.YEAR)), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH),name,"");
		
		if(recordList.size()>0&&recordList!=null) {
			ModelMap recordListMap=new ModelMap();
			for(int i=0;i<recordList.size();i++) {
				ModelMap recordMap=new ModelMap();
				MaintenancePlanRecord record=recordList.get(i);
				if(record!=null) {
					//根据设备编码查询文档信息
					if(device!=null) {
						Long deviceId = device.getId();
						//查找关联文档
						List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, deviceId);
						modelMap.addAttribute("docs", docs);
						
						//根据设备编码查找保养记录项信息
						List<MaintenanceItem> list = maintenanceItemService.queryMaintenanceItemByMaintenancePlanRecordId(record.getId());
						int count =maintenanceItemService.queryResultCountByMaintenanceItemId(record.getId());
						recordMap.addAttribute("count", count);
						recordMap.addAttribute("items", list);
						recordMap.addAttribute("record",record);
						recordListMap.addAttribute(i+"",recordMap);
					}
				}
				
			}
			modelMap.addAttribute("list",recordListMap);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该设备当天没有保养记录");
		}
		
		return modelMap;
	}*/
	/**
	 * 查询未保养记录
	 */
	@RequestMapping("/queryNotMaintenanceRecord.do")
	@ResponseBody
	public ModelMap queryNotMaintenanceRecord(String startDate,String endDate) {
		ModelMap modelMap = new ModelMap();
	  List<Object[]> deviceList=maintenancePlanRecordService.queryNotMaintenanceDeviceBytime(startDate, endDate);
	  List<Object[]> recordList=maintenancePlanRecordService.queryNotMaintenanceRecordBytime(startDate, endDate);
	  modelMap.addAttribute("deviceList", deviceList);
	  modelMap.addAttribute("recordList", recordList);
	  return modelMap;
	}
	/**
	 * 根据设备编码,使用者和当前日期查找设备保养记录和项目
	 */
	@RequestMapping("/queryMaintenanceRecordByDeviceCodeToday.do")
	@ResponseBody
	public ModelMap queryMaintenanceRecordByDeviceCodeToday(String deviceCode,String usercode) {
		ModelMap modelMap = new ModelMap();
		
		Device device = deviceService.queryByProperty("code", deviceCode);
		Calendar c = Calendar.getInstance();
		List<MaintenancePlanRecord> recordList=maintenancePlanRecordService.queryMaintenancePlanRecordTodayByDeviceIdAndEmployCodeAll(device.getId(),c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH),usercode);
		
		if(recordList.size()>0&&recordList!=null) {
			modelMap.addAttribute("list",recordList);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该设备当天没有保养记录");
		}
		
		return modelMap;
	}
	/**
	 * 通过保养计划id查找保养项目
	 */
	@RequestMapping("/queryMaintenanceItemByMaintenanceRecordId.do")
	@ResponseBody
	public ModelMap queryMaintenanceItemByMaintenanceRecordId(Long RecordId) {
		ModelMap modelMap = new ModelMap();
		List<MaintenanceItem> list = maintenanceItemService.queryMaintenanceItemByMaintenancePlanRecordId(RecordId);
		int count =maintenanceItemService.queryResultCountByMaintenanceItemId(RecordId);
		modelMap.addAttribute("itemList", list);
		modelMap.addAttribute("count", count);
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	
	/**
	 * 设备保养项目完成确认(单个)
	 */
	@RequestMapping("/confirm.do")
	@ResponseBody
	public ModelMap confirm(String id,String username,String remarks){
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		MaintenanceItem item = maintenanceItemService.queryObjById(Long.valueOf(id));
		if(item.getConfirmDate()!=null) {
			modelMap.addAttribute("message", "不允许重复确认!");
			modelMap.addAttribute("success", false);
			return modelMap;
		}
		User user = userService.queryByProperty("username", username);
		Employee employee = user.getEmployee();
		if(employee!=null) {
			item.setConfirmCode(employee.getCode());
			item.setConfirmUser(employee.getName());
		}else {
			item.setConfirmCode(user.getUsername());
			item.setConfirmUser(user.getUsername());
		}
		item.setConfirmDate(new Date());
		item.setRemarks(remarks);
		maintenanceItemService.confirm(item);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("message", "已确认!");
		return modelMap;
	}
	/**
	 * 保养中转为待确认
	 */
	@RequestMapping("/updateMaintenanceToConfirm.do")
	@ResponseBody
	public ModelMap updateMaintenanceToConfirm(Long recordId,String username,String confirmCode) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		Employee employee = user.getEmployee();
		User confirmUser = userService.queryUserByEmployeeCode(confirmCode);
		MaintenancePlanRecord m=maintenancePlanRecordService.queryObjById(recordId);
		//保养中转为待确认
		if(m.getStatus().equals(Status.MAINTENANCEPLAN_MAINTENANCING)) {
			//查找保养人
			List<MaintenanceUser> maintenanceUsers = maintenanceUserService.queryMaintenanceUserByRecordId(recordId);
			for(MaintenanceUser maintenanceUser : maintenanceUsers) {
				MaintenanceUser mu = new MaintenanceUser();
				BeanUtils.copyProperties(maintenanceUser, mu);
				mu.setCompleteDate(new Date());
				//设置保养时间，完成时间-接单时间，单位分钟 
				if(mu.getReceiptDate()!=null) {
					mu.setMaintenanceTime((double) Math.round(((double)(mu.getCompleteDate().getTime()-mu.getReceiptDate().getTime())/60000) * 100) / 100);
					}
				maintenanceUserService.updateObj(mu);
				MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", mu.getCode());
				if(maintenanceStaff!=null) {
				maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY,
						Constant.ReceiveType.SYSTEMGASSIGN);
				}
			}
			m.setStatus(Status.MAINTENANCEPLAN_TOBECONFIRMED);
			m.setConfirmCode(confirmCode);
			m.setConfirmName(confirmUser.getEmployee().getName());
			m.setMaintenancedDate(new Date());
			maintenancePlanRecordService.confirmMaintenance(m, employee.getName(), Status.MAINTENANCEPLAN_TOBECONFIRMED);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("result","保养结束");
			//推送给确认人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(confirmUser.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_CONFIRM, DeviceRepairStatus.DEVICEREPAIRCONTENT_CONFIRM);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("result","保养结束");
		}
		return modelMap;
	}
	/**
	 * 保养中转为待确认小程序上传图片（单文件上传）
	 */
	@RequestMapping("/updateMaintenanceToConfirmWithPicWX.do")
	@ResponseBody
	public ModelMap updateMaintenanceToConfirmWithPicWX(@RequestParam("file")MultipartFile file,Long recordId,String username,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		MaintenancePlanRecord m=maintenancePlanRecordService.queryObjById(recordId);	
		
		RelatedDocumentType docType = relatedDocumentTypeService.queryByProperty("code", m.getMaintenanceType().getCode());
		if(file!=null) {
			String dir =docType==null?"":(docType.getModuleCode()+"/" + docType.getCode());
			String picName = file.getOriginalFilename();
			String[] subName = picName.split("\\.");
			String prefix = "";
			if(subName!=null && subName.length>0) {
				prefix = subName[subName.length-1];
			}
			String newName = aformat.format(new Date()) + (StringUtils.isEmpty(prefix)?"":("."+prefix));
			InputStream is;
			try {
				is = file.getInputStream();
				File parentDir =new File(request.getServletContext().getRealPath("/")+dir);
				if(!parentDir.exists()) {
					parentDir.mkdirs();
				}
				File out = new File(parentDir,picName);
				//FileUtils.copyInputStreamToFile(is, out);
				FileCopyUtils.copy(is, new FileOutputStream(out));
				

				RelatedDocument document = new RelatedDocument();
				document.setContentType(file.getContentType());
				document.setFileSize(file.getSize());
				document.setSrcName(picName);
				document.setUploadDate(new Date());
				document.setRelatedDocumentType(docType);
				document.setRelatedId(recordId.toString());
				document.setUploadUsername(username);
				document.setUrl(dir +"/"+ picName);
				document.setName(picName);
				relatedDocumentService.addObj(document);
			} catch (IOException e) {
				e.printStackTrace();
				modelMap.addAttribute("success",false);
				modelMap.addAttribute("title","错误提示");
				modelMap.addAttribute("msg","文件上传异常!");
				return modelMap;
			}
			
		}
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("result","保养结束");
		return modelMap;
	}
	/**
	 * 保养中转为待确认(上传图片)
	 */
	@RequestMapping("/updateMaintenanceToConfirmWithPic.do")
	@ResponseBody
	public ModelMap updateMaintenanceToConfirmWithPic(@RequestParam("files")List<MultipartFile> files,Long recordId,String username,String confirmCode,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		Employee employee = user.getEmployee();
		User confirmUser = userService.queryUserByEmployeeCode(confirmCode);
		MaintenancePlanRecord m=maintenancePlanRecordService.queryObjById(recordId);
		//保养中转为待确认
		if(m.getStatus().equals(Status.MAINTENANCEPLAN_MAINTENANCING)) {
			//查找保养人
			List<MaintenanceUser> maintenanceUsers = maintenanceUserService.queryMaintenanceUserByRecordId(recordId);
			for(MaintenanceUser maintenanceUser : maintenanceUsers) {
				MaintenanceUser mu = new MaintenanceUser();
				BeanUtils.copyProperties(maintenanceUser, mu);
				mu.setCompleteDate(new Date());
				//设置保养时间，完成时间-接单时间，单位分钟 
				if(mu.getReceiptDate()!=null) {
				mu.setMaintenanceTime((double) Math.round(((double)(mu.getCompleteDate().getTime()-mu.getReceiptDate().getTime())/60000) * 100) / 100);
				}
				maintenanceUserService.updateObj(mu);
				MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", mu.getCode());
				if(maintenanceStaff!=null) {
				maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY,
						Constant.ReceiveType.SYSTEMGASSIGN);
				}
			}
			RelatedDocumentType docType = relatedDocumentTypeService.queryByProperty("code", m.getMaintenanceType().getCode());
			if(files!=null && files.size()>0) {
				String dir =docType==null?"":(docType.getModuleCode()+"/" + docType.getCode());
				for(MultipartFile file : files) {
					String picName = file.getOriginalFilename();
					String[] subName = picName.split("\\.");
					String prefix = "";
					if(subName!=null && subName.length>0) {
						prefix = subName[subName.length-1];
					}
					String newName = aformat.format(new Date()) + (StringUtils.isEmpty(prefix)?"":("."+prefix));
					InputStream is;
					try {
						is = file.getInputStream();
						File parentDir =new File(request.getServletContext().getRealPath("/")+dir);
						if(!parentDir.exists()) {
							parentDir.mkdirs();
						}
						File out = new File(parentDir,picName);
						FileCopyUtils.copy(is, new FileOutputStream(out));
						RelatedDocument document = new RelatedDocument();
						document.setContentType(file.getContentType());
						document.setFileSize(file.getSize());
						document.setSrcName(picName);
						document.setUploadDate(new Date());
						document.setRelatedDocumentType(docType);
						document.setRelatedId(recordId.toString());
						document.setUploadUsername(username);
						document.setUrl(dir +"/"+ picName);
						document.setName(picName);
						relatedDocumentService.addObj(document);
					} catch (IOException e) {
						e.printStackTrace();
						modelMap.addAttribute("success",false);
						modelMap.addAttribute("title","错误提示");
						modelMap.addAttribute("msg","文件上传异常!");
						return modelMap;
					}
				}
			}
			m.setStatus(Status.MAINTENANCEPLAN_TOBECONFIRMED);
			m.setConfirmCode(confirmCode);
			if(confirmUser.getEmployee()!=null)
			m.setConfirmName(confirmUser.getEmployee().getName());
			else
				m.setConfirmName(confirmUser.getUsername());
			m.setMaintenancedDate(new Date());
			maintenancePlanRecordService.confirmMaintenance(m, employee.getName(), Status.MAINTENANCEPLAN_TOBECONFIRMED);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("result","保养结束");
			//推送给确认人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(confirmUser.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_CONFIRM, DeviceRepairStatus.DEVICEREPAIRCONTENT_CONFIRM);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("result","保养结束");
		}
		return modelMap;
	}
	/**
	 * 待确认转为完成
	 */
	@RequestMapping("/updateMaintenanceToOver.do")
	@ResponseBody
	public ModelMap updateMaintenanceToOver(Long recordId,String username,double occupyTime) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		Employee employee = user.getEmployee();
		MaintenancePlanRecord m=maintenancePlanRecordService.queryObjById(recordId);
		//待确认转为完成
		if(m.getStatus().equals(Status.MAINTENANCEPLAN_TOBECONFIRMED)) {
			m.setStatus(Status.MAINTENANCEPLAN_COMPLETE);
			if(employee!=null) {
				m.setConfirmCode(employee.getCode());
				m.setConfirmName(employee.getName());
			}else {
				m.setConfirmCode(user.getUsername());
				m.setConfirmName(user.getUsername());
			}
			m.setConfirmDate(new Date());
			maintenancePlanRecordService.confirmMaintenance(m, employee.getName(), Status.MAINTENANCEPLAN_COMPLETE);
			List<MaintenanceUser> muList=maintenanceUserService.queryMaintenanceUserByRecordId(recordId);
			for(MaintenanceUser mu:muList) {
				mu.setOccupyTime(occupyTime);
				maintenanceUserService.updateObj(mu);
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("result","保养完成");
		}else {
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("result","保养完成");
		}
		return modelMap;
	}
	/**
	 * 返修
	 */
	@RequestMapping("/updateDeviceRepairForConfirmAndReword.do")
	@ResponseBody
	public ModelMap updateDeviceRepairForConfirmAndReword(Long recordId,String status,String username) {
		ModelMap modelMap = new ModelMap();
		User user=userService.queryUserByUsername(username);
		MaintenancePlanRecord m=maintenancePlanRecordService.queryObjById(recordId);
		m.setStatus(Status.MAINTENANCEPLAN_RECEIPT);
		maintenancePlanRecordService.confirmMaintenance(m, (user.getEmployee()==null?"":user.getEmployee().getName()), Constant.ReceiveType.REWORK);
		//查找责任人
		List<MaintenanceUser> responsibilityList=maintenanceUserService.queryResponsibilityMaintenanceUserByMaintenancePlanRecordId(recordId);
		for(MaintenanceUser mu: responsibilityList) {
			mu.setReceiptDate(null);
			maintenanceUserService.updateObj(mu);
		}
		//查找协助人
		List<MaintenanceUser> helpList=maintenanceUserService.queryHelpMaintenanceUserByMaintenancePlanRecordId(recordId);
		for(MaintenanceUser mu: helpList) {
			maintenanceUserService.deleteObj(mu.getId());
		}

		//推送给责任人
		User maintainUser=userService.queryUserByEmployeeCode(responsibilityList.get(0).getCode());
		List<String> clientIdsList = new ArrayList<>();
		clientIdsList.add(maintainUser.getUsername()+"");
		clientIdsList=appClientMapService.queryCids(clientIdsList);
		try {
			PushtoAPP.pushMessage(clientIdsList, Status.MAINTAINTITLE_ASSIGN, Status.MAINTAINCONTENT_ASSIGN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * 通过时间段和设备代码查询保养记录
	 */
	@RequestMapping("/queryMaintenanceRecordByDeviceCodeAndTimeSlot.do")
	@ResponseBody
	public ModelMap queryMaintenanceRecordByDeviceCodeAndTimeSlot(String deviceCode,String startDate,String endDate) {
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		
		List<MaintenancePlanRecord> recordList=maintenancePlanRecordService.queryMaintenanceRecordByDeviceCodeAndTimeSlot(device.getId(), startDate, endDate);
		
		if(recordList!=null&&recordList.size()>0) {
			modelMap.addAttribute("list",recordList);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "没有保养记录");
		}
		
		return modelMap;
	}
	/**
	 * 通过时间段、生产单元id和设备类别id查询保养记录
	 */
	@RequestMapping("/queryMaintenanceRecordByProductLineIdAndDeviceTypeIdAndTimeSlot.do")
	@ResponseBody
	public ModelMap queryMaintenanceRecordByProductLineIdAndDeviceTypeIdAndTimeSlot(String startDate,String endDate,Long productLineId,Long deviceTypeId) {
		ModelMap modelMap = new ModelMap();
		List<MaintenancePlanRecord> recordList=maintenancePlanRecordService.queryMaintenanceRecordByProductLineIdAndDeviceTypeIdAndTimeSlot(startDate, endDate, productLineId, deviceTypeId);
		if(recordList.size()>0&&recordList!=null) {
			modelMap.addAttribute("list",recordList);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "没有保养记录");
		}
		return modelMap;
	}
	/**
	 * 根据保养单状态(+用户)查询保养计划
	 */
	@RequestMapping("/queryMaintenancePlanRecordByStatus.do")
	@ResponseBody
	public ModelMap queryMaintenancePlanRecordByStatus(String status,String usercode) {
		ModelMap modelMap = new ModelMap();
		List<MaintenancePlanRecord> pager=new ArrayList<>();
		if(status.equals("待接单")) {
			 pager=maintenancePlanRecordService.queryReceiptMPRWithUser(usercode);
		}else if(status.equals("保养中")) {
			 pager=maintenancePlanRecordService.queryMaintenanceMPRWithUser(usercode);
		}else if(status.equals("待确认")) {
			 pager=maintenancePlanRecordService.queryMaintenancePlanRecordByConfirmCodeAndConfirm(usercode);
		}else {    
			 pager = maintenancePlanRecordService.queryMaintenancePlanRecordByStatus(status,usercode);
		}
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager)) {
			for(MaintenancePlanRecord record : pager) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("list", list);
		return modelMap;
	}
	/**
	 * 根据确认人查询待确认保养计划
	 */
	@RequestMapping("/queryMaintenancePlanRecordByConfirmCodeAndConfirm.do")
	@ResponseBody
	public ModelMap queryMaintenancePlanRecordByConfirmCodeAndConfirm(String confirmCode){
		ModelMap modelMap = new ModelMap();
		List<MaintenancePlanRecord> pager= maintenancePlanRecordService.queryMaintenancePlanRecordByConfirmCodeAndConfirm(confirmCode);
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager)) {
			for(MaintenancePlanRecord record : pager) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("list", list);
		return modelMap;
	}
	/**
	 * 根据保养单状态(+用户)查询保养计划(分页)
	 */
	@RequestMapping("/queryMaintenancePlanRecordByStatusWithPage.do")
	@ResponseBody
	private ModelMap queryMaintenancePlanRecordByStatusWithPage(String status,String usercode,HttpServletRequest request,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,String productionLineCode,
			String maintainerCode,String deviceCode,String maintenanceTypeonCode,String startDate,String endDate) {
		ModelMap modelMap = new ModelMap();
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
		hql+=" order by m.confirmDate desc";
		@SuppressWarnings("unchecked")
		Pager<MaintenancePlanRecord> pager=maintenancePlanRecordService.queryObjs(hql, page, rows, paramList.toArray());
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager.getData())) {
			for(MaintenancePlanRecord record : pager.getData()) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * 查询我的保养计划已完成(分页)
	 */
	@RequestMapping("/queryMyCompleteMaintenancePlanRecordByCodeWithPage.do")
	@ResponseBody
	private ModelMap queryMyCompleteMaintenancePlanRecordByCodeWithPage(String code,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql="from MaintenancePlanRecord m where m.id in (select u.maintenancePlanRecord.id from MaintenanceUser u where m.id=u.maintenancePlanRecord.id and u.code=?0) and m.status='已完成' order by m.maintenanceDate desc";
		Pager<MaintenancePlanRecord> pager=maintenancePlanRecordService.queryObjs(hql, page, rows,new Object[] {code});
		List<MaintenancePlanRecordVO> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager.getData())) {
			for(MaintenancePlanRecord record : pager.getData()) {
				list.add(model2vo(record));
			}
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	/**
	 * model转vo
	 * @param record
	 * @return
	 */
	public MaintenancePlanRecordVO model2vo(MaintenancePlanRecord record) {
		format.applyPattern("yyyy-MM-dd");
		MaintenancePlanRecordVO vo = new MaintenancePlanRecordVO();
		BeanUtils.copyProperties(record, vo);
		MaintenanceUser u = maintenanceUserService.queryPersonInChargeByMaintenancePlanRecordId(record.getId());
		if(u!=null) {
			vo.setEmployeeCode(u.getCode());
			vo.setEmployeeName(u.getName());
		}
		if(record.getMaintenanceDate()!=null) {
			vo.setMaintenanceDate(format.format(record.getMaintenanceDate()));
		}
		if(record.getMaintenancedDate()!=null) {
			vo.setMaintenancedDate(format.format(record.getMaintenancedDate()));
		}
		if(record.getMaintenanceType()!=null) {
			vo.setMaintenanceType(record.getMaintenanceType().getName());
			vo.setMaintenanceTypeCode(record.getMaintenanceType().getCode());
		}
		return vo;
	}
	/**
	 * 保养抢单、接单
	 */
	@RequestMapping("/updateRobList.do")
	@ResponseBody
	public ModelMap updateRobList(Long recordId,String username,String action) {
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		MaintenancePlanRecord m= maintenancePlanRecordService.queryObjById(recordId);
		m.setStatus(Status.MAINTENANCEPLAN_MAINTENANCING);
		MaintenanceUser Muser=new MaintenanceUser();
		List<MaintenanceUser> list=maintenancePlanRecordService.queryMaintenanceUserByRecordId(recordId);
		MaintenanceStaff ms=maintenanceStaffService.queryByProperty("code", user.getEmployee().getCode());
			if(action.equals("抢单")) {
				if(list.size()>0&&list!=null) {
					modelMap.addAttribute("false", true);
					modelMap.addAttribute("msg","该单已有人接单");
				}else {
					if(ms!=null) {
						maintenanceStaffService.updateStatus(ms, Constant.MaintenanceStaffStatus.MAINTENANCE,
								Constant.ReceiveType.SYSTEMGASSIGN);
					}
					Muser.setDispatchDate(new Date());
					Muser.setReceiptDate(new Date());
					Muser.setCode(user.getEmployee().getCode());
					Muser.setName(user.getEmployee().getName());
					Muser.setMaintenancePlanRecord(m);
					Muser.setOrderType(Constant.ReceiveType.ROBLIST);
					Employee employee = employeeService.queryObjById(user.getEmployee().getCode());
					maintenanceUserService.addMaintenanceUser(Muser, employee);
					maintenancePlanRecordService.confirmMaintenance(m, user.getEmployee().getName(), Constant.ReceiveType.ROBLIST);
					modelMap.addAttribute("success", true);
				}
			}else if(action.equals("接单")) {
				if(ms!=null) {
					maintenanceStaffService.updateStatus(ms, Constant.MaintenanceStaffStatus.MAINTENANCE,
							Constant.ReceiveType.SYSTEMGASSIGN);
				}
				maintenancePlanRecordService.confirmMaintenance(m, user.getEmployee().getName(), Status.MAINTENANCEPLAN_MAINTENANCING);
				Muser=maintenanceUserService.queryPersonInChargeByMaintenancePlanRecordIdAndName(recordId,user.getEmployee().getName());
				Muser.setReceiptDate(new Date());
				maintenanceUserService.updateObj(Muser);
				modelMap.addAttribute("success", true);
			}
		return modelMap;
	}
	/**
	 * 保养派单
	 * @param recordId
	 * @param usercode   保养人姓名
	 * @param dispatchUsername   派单人username
	 * @return
	 */
	@RequestMapping("/updateDispatch.do")
	@ResponseBody
	public ModelMap updateDispatch(Long recordId,String usercode,String dispatchUsername) {
		ModelMap modelMap = new ModelMap();
		User dispatchuser = userService.queryByProperty("username", dispatchUsername);
		Employee employee = employeeService.queryByProperty("code", usercode);
		MaintenancePlanRecord m= maintenancePlanRecordService.queryObjById(recordId);
		List<MaintenanceUser> list= maintenancePlanRecordService.queryMaintenanceUserByRecordId(recordId);
		if(list.size()>0&&list!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该单已有人保养");
		}else {
			MaintenanceUser Muser=new MaintenanceUser();
			Muser.setDispatchDate(new Date());
			Muser.setDispatchUsercode(dispatchuser.getEmployee().getCode());
			Muser.setDispatchUsername(dispatchuser.getEmployee().getName());
			Muser.setCode(employee.getCode());
			Muser.setName(employee.getName());
			Muser.setMaintenancePlanRecord(m);
			Muser.setOrderType("人工派单");
			maintenanceUserService.addMaintenanceUser(Muser, employee);

			m.setStatus(Status.MAINTENANCEPLAN_RECEIPT);
			maintenancePlanRecordService.confirmMaintenance(m, employee.getName(), Status.MAINTENANCEPLAN_RECEIPT);
			//推送给责任人
			User maintainUser=userService.queryUserByEmployeeCode(usercode);
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(maintainUser.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, Status.MAINTAINTITLE_ASSIGN, Status.MAINTAINCONTENT_ASSIGN);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modelMap.addAttribute("success", true);
		}
		return modelMap;
	}
	/**
	 * 根据设备id和文档类型编码查找设备的保养项目文档
	 */
	@RequestMapping("/queryDocByDevice.do")
	@ResponseBody
	public ModelMap queryDocByDevice(String deviceCode,String docTypeCode) {
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, docTypeCode, device.getId());
		modelMap.addAttribute("docs", docs);
		return modelMap;
	}
	
	/**
	 * 根据设备id查询该设备的所有保养记录
	 */
	@RequestMapping("/queryMaintenancePlanRecordBydeviceId.do")
	@ResponseBody
	public List<MaintenancePlanRecordVO> queryMaintenancePlanRecordBydeviceId(Long deviceId){
		List<MaintenancePlanRecord> repairList=maintenancePlanRecordService.queryAllMaintenancePlanRecordsByDeviceId(deviceId);
		List<MaintenancePlanRecordVO> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(repairList)) {
			for(MaintenancePlanRecord record : repairList) {
				list.add(model2vo(record));
			}
		}
		return list;
	}
	/**
	 * 根据状态查询保养记录
	 */
	@RequestMapping("/queryAllMaintenancePlanRecordByStatus.do")
	@ResponseBody
	public List<MaintenancePlanRecordVO> queryAllMaintenancePlanRecordByStatus(String status,String productionLineCode,
			String maintainerCode,String deviceCode,String maintenanceTypeonCode,String startDate,String endDate){
		List<MaintenancePlanRecord> repairList=maintenancePlanRecordService.queryMaintenancePlanRecordByStatusForScreen(status, productionLineCode, maintainerCode, deviceCode, maintenanceTypeonCode, startDate, endDate);
		List<MaintenancePlanRecordVO> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(repairList)) {
			for(MaintenancePlanRecord record : repairList) {
				list.add(model2vo(record));
			}
		}
		return list;
	}
	
	/**
	 * 查询角标对应的数据数量
	 */
	@RequestMapping("/queryBadgeWithRepair.do")
	@ResponseBody
	public ModelMap queryBadgeWithRepair(String username) {
		User user=userService.queryByProperty("username", username);
		ModelMap modelMap=new ModelMap();
		//待派单数量
		String wlhql="select count(*) from DeviceRepair dr where dr.status='WAITINGASSIGN'";
		int wlcount=maintenancePlanRecordService.queryMaintenancePlanRecordByStatus("待派单","").size();
		modelMap.addAttribute("waitingList", wlcount);
		//待接单数量
		int wrcount=maintenancePlanRecordService.queryReceiptMPRWithUser(user.getEmployee().getCode()).size();
		modelMap.addAttribute("waitingReceipt", wrcount);
		//保养中数量
		int mxcount=maintenancePlanRecordService.queryMaintenanceMPRWithUser(user.getEmployee().getCode()).size();
		modelMap.addAttribute("maintaining", mxcount);
		//待确认
		int wccount=maintenancePlanRecordService.queryMaintenancePlanRecordByConfirmCodeAndConfirm(user.getEmployee().getCode()).size();
		modelMap.addAttribute("waitcomfirm", wccount);
		return modelMap;
	}
}
