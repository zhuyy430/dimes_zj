package com.digitzones.app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.constants.Constant;
import com.digitzones.constants.Constant.DeviceRepairStatus;
import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.DeviceRepairPic;
import com.digitzones.devmgr.model.MaintenanceRelatedDocument;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.devmgr.model.NGMaintainRecord;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.devmgr.service.IDeviceProjectService;
import com.digitzones.devmgr.service.IDeviceRepairOrderPicService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IMaintenanceRelatedDocumentService;
import com.digitzones.devmgr.service.IMaintenanceStaffRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.INGMaintainRecordService;
import com.digitzones.devmgr.vo.DeviceRepairVO;
import com.digitzones.model.Device;
import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.ProjectType;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.User;
import com.digitzones.service.IAppClientMapService;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IProductionUnitService;
import com.digitzones.service.IProjectTypeService;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IUserService;
import com.digitzones.util.PushtoAPP;

@Controller
@RequestMapping("/AppDeviceRepair")
public class AppDeviceRepairOrderController {
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProductionUnitService productionUnitService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IDeviceRepairOrderPicService deviceRepairOrderPicService;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	private INGMaintainRecordService ngMaintainRecordService;
	@Autowired
	private IMaintenanceStaffRecordService maintenanceStaffRecordService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private IProjectTypeService projectTypeService;
	@Autowired
	IAppClientMapService appClientMapService; 
	@Autowired
	private IDeviceProjectService deviceProjectService;
	@Autowired
	private IMaintenanceRelatedDocumentService maintenanceRelatedDocumentService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 新增
	 */
	@RequestMapping("/addAppDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap addAppDeviceRepairOrder(@RequestParam("files")List<MultipartFile> files,String serialNumber,String maintainCode,Long productionUnitId,Long deviceId,Long ngreasonId,String ngDescription,String username,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		DeviceRepair deviceRepair=new DeviceRepair();
		Device device=new Device();
		DeviceProject pressLight=new DeviceProject();
		ProductionUnit productionUnit=new ProductionUnit();
		Employee employee=new Employee();
		device=deviceService.queryObjById(deviceId);
		productionUnit=productionUnitService.queryObjById(productionUnitId);
		deviceRepair.setCreateDate(new Date());
		deviceRepair.setSerialNumber(serialNumber);
		if(user.getEmployee()!=null) {
			deviceRepair.setInformant(user.getEmployee().getName());
			deviceRepair.setInformantCode(user.getEmployee().getCode());
		}else  {
			deviceRepair.setInformant(user.getUsername());
			deviceRepair.setInformantCode(user.getUsername());
		}
		if(maintainCode!=null&&!maintainCode.equals("")) {
			employee=employeeService.queryByProperty("code", maintainCode);
			deviceRepair.setMaintainCode(employee.getCode());
			deviceRepair.setMaintainName(employee.getName());
		}
		
		deviceRepair.setNgDescription(ngDescription);
		deviceRepair.setDevice(device);
		deviceRepair.setProductionUnit(productionUnit);
		//deviceRepairOrderService.addObj(deviceRepair);
		
		
		List<MaintenanceStaff> maintenanceStaff = maintenanceStaffService
				.queryListByProductionUnitIdAndStatus(productionUnitId,Constant.MaintenanceStaffStatus.ONDUTY);
		if(maintenanceStaff.isEmpty()){
			maintenanceStaff = maintenanceStaffService.queryListByStatus(Constant.MaintenanceStaffStatus.ONDUTY);
		}
		boolean tf=false;
		boolean exist=false;
		if(deviceRepair.getMaintainName()!=null&&!deviceRepair.getMaintainName().equals("")){//指定维修人员
			tf=true;
			deviceRepair.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
		}else if(maintenanceStaff!=null&&maintenanceStaff.size()>0){				//自动选择在岗状态维修人员
			exist=true;
			deviceRepair.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
			deviceRepair.setMaintainName(maintenanceStaff.get(0).getName());
			deviceRepair.setMaintainCode(maintenanceStaff.get(0).getCode());
			
			
		}else{																		//等待派单
			deviceRepair.setStatus(DeviceRepairStatus.WAITINGASSIGN);
		}
		modelMap.put("businessKey", workFlowKeyConfig.getDeviceRepairflowKey());
		//Serializable id = deviceRepairOrderService.addDeviceRepair(deviceRepair, user.getEmployee());
		Serializable id;
		if(null!=user.getEmployee()){
			id = deviceRepairOrderService.addDeviceRepair(deviceRepair, user.getEmployee().getName());
		}else{
			id = deviceRepairOrderService.addDeviceRepair(deviceRepair, username);
		}
		if(files!=null && files.size()>0) {
			String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
			for(MultipartFile file : files) {
				String picName = file.getOriginalFilename();
				InputStream is;
				try {
					is = file.getInputStream();
					File parentDir =new File(dir);
					if(!parentDir.exists()) {
						parentDir.mkdirs();
					}
					File out = new File(parentDir,picName);
					//FileUtils.copyInputStreamToFile(is, out);
					FileCopyUtils.copy(is, new FileOutputStream(out));
					DeviceRepairPic DRP=new DeviceRepairPic();
					DRP.setPicName("console/deviceImgs/" + picName);
					DRP.setOrderId(Long.parseLong(id.toString()));
					deviceRepairOrderPicService.addOrderPic(DRP, out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		DeviceRepair dro =new DeviceRepair();
		dro.setId((Long)id);
		if(ngreasonId!=null&&!ngreasonId.equals("")) {
			pressLight=deviceProjectService.queryObjById(ngreasonId);
			deviceRepair.setNgreason(pressLight);
			NGMaintainRecord ng = new NGMaintainRecord();
			ng.setDeviceProject(deviceRepair.getNgreason());
			ng.setNote(deviceRepair.getNgDescription());
			ng.setDeviceRepair(dro);
			ng.setCreateDate(new Date());
			ngMaintainRecordService.addObj(ng);
		}
		
		if(tf||exist){
			MaintenanceStaffRecord maintenanceStaffRecord = new MaintenanceStaffRecord();
			User u = new User();
			if(tf){
				u = userService.queryUserByEmployeeCode(deviceRepair.getMaintainCode());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ARTIFICIALGASSIGN);
				if(user.getEmployee()!=null) {
					maintenanceStaffRecord.setAssignCode(user.getEmployee().getCode());
					maintenanceStaffRecord.setAssignName(user.getEmployee().getName());
				}
				maintenanceStaffRecord.setName(employee.getName());
				maintenanceStaffRecord.setCode(employee.getCode());
				MaintenanceStaff msf=maintenanceStaffService.queryByProperty("name", employee.getName());
				maintenanceStaffService.updateStatus(msf.getId(),Constant.MaintenanceStaffStatus.MAINTAIN, Constant.ReceiveType.SYSTEMGASSIGN);
			}else if(exist){
				u = userService.queryUserByEmployeeCode(maintenanceStaff.get(0).getCode());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.SYSTEMGASSIGN);
				maintenanceStaffRecord.setName(maintenanceStaff.get(0).getName());
				maintenanceStaffRecord.setCode(maintenanceStaff.get(0).getCode());
				maintenanceStaffService.updateStatus(maintenanceStaff.get(0).getId(),Constant.MaintenanceStaffStatus.MAINTAIN, Constant.ReceiveType.SYSTEMGASSIGN);
			}
			maintenanceStaffRecord.setDeviceRepair(dro);
			
			maintenanceStaffRecord.setAssignTime(new Date());
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			//deviceRepairOrderService.receiveDeviceRepair(deviceRepair, u, modelMap);
			if(null!=u.getEmployee()){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, u.getEmployee().getName(),
						DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username,
						DeviceRepairStatus.WAITINCOMFIRM);
			}
			
			//推送给责任人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(u.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	
	
	
	/**
	 * 新增图片
	 */
	@RequestMapping("/addAppDeviceRepairOrderPic.do")
	@ResponseBody
	public ModelMap addAppDeviceRepairOrderPic(@RequestParam("file")MultipartFile file,Long orderId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		if(file!=null) {
			String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
			String picName = file.getOriginalFilename();
			InputStream is;
			try {
				is = file.getInputStream();
				File parentDir =new File(dir);
				if(!parentDir.exists()) {
					parentDir.mkdirs();
				}
				File out = new File(parentDir,picName);
				//FileUtils.copyInputStreamToFile(is, out);
				FileCopyUtils.copy(is, new FileOutputStream(out));
				DeviceRepairPic DRP=new DeviceRepairPic();
				DRP.setPicName("console/deviceImgs/" + picName);
				DRP.setOrderId(orderId);
				deviceRepairOrderPicService.addOrderPic(DRP, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	
	
	
	
	/**
	 * 新增(无上传)
	 */
	@RequestMapping("/addAppDeviceRepairOrderNotFiles.do")
	@ResponseBody
	public ModelMap addAppDeviceRepairOrderNotFiles(String serialNumber,String maintainCode,Long productionUnitId,Long deviceId,Long ngreasonId,String ngDescription,String username,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		DeviceRepair deviceRepair=new DeviceRepair();
		Device device=new Device();
		ProductionUnit productionUnit=new ProductionUnit();
		DeviceProject pressLight=new DeviceProject();
		Employee employee=new Employee();
		device=deviceService.queryObjById(deviceId);
		productionUnit=productionUnitService.queryObjById(productionUnitId);
		deviceRepair.setCreateDate(new Date());
		deviceRepair.setSerialNumber(serialNumber);
		if(user.getEmployee()!=null) {
			deviceRepair.setInformant(user.getEmployee().getName());
			deviceRepair.setInformantCode(user.getEmployee().getCode());
		}else{
			deviceRepair.setInformant(user.getUsername());
			deviceRepair.setInformantCode(user.getUsername());
		}
		if(maintainCode!=null&&!maintainCode.equals("")) {
			employee=employeeService.queryByProperty("code", maintainCode);
			deviceRepair.setMaintainCode(employee.getCode());
			deviceRepair.setMaintainName(employee.getName());
		}
		
		deviceRepair.setNgDescription(ngDescription);
		deviceRepair.setDevice(device);
		deviceRepair.setProductionUnit(productionUnit);
		
		//deviceRepairOrderService.addObj(deviceRepair);
		
		
		List<MaintenanceStaff> maintenanceStaff = maintenanceStaffService.queryListByStatus(Constant.MaintenanceStaffStatus.ONDUTY);
		boolean tf=false;
		boolean exist=false;
		if(deviceRepair.getMaintainName()!=null&&!deviceRepair.getMaintainName().equals("")){//指定维修人员
			tf=true;
			deviceRepair.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
		}else if(maintenanceStaff!=null&&maintenanceStaff.size()>0){				//自动选择在岗状态维修人员
			exist=true;
			deviceRepair.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
			deviceRepair.setMaintainName(maintenanceStaff.get(0).getName());
			deviceRepair.setMaintainCode(maintenanceStaff.get(0).getCode());
		}else{																		//等待派单
			deviceRepair.setStatus(DeviceRepairStatus.WAITINGASSIGN);
		}
		modelMap.put("businessKey", workFlowKeyConfig.getDeviceRepairflowKey());
		//Serializable id = deviceRepairOrderService.addDeviceRepair(deviceRepair, user.getEmployee());
		Serializable id;
		if(null!=user.getEmployee()){
			id = deviceRepairOrderService.addDeviceRepair(deviceRepair, user.getEmployee().getName());
		}else{
			id = deviceRepairOrderService.addDeviceRepair(deviceRepair, username);
		}
		DeviceRepair dro =new DeviceRepair();
		dro.setId((Long)id);
		if(ngreasonId!=null&&!ngreasonId.equals("")) {
			pressLight=deviceProjectService.queryObjById(ngreasonId);
			deviceRepair.setNgreason(pressLight);
			NGMaintainRecord ng = new NGMaintainRecord();
			ng.setDeviceProject(deviceRepair.getNgreason());
			ng.setNote(deviceRepair.getNgDescription());
			ng.setDeviceRepair(dro);
			ng.setCreateDate(new Date());
			ngMaintainRecordService.addObj(ng);
		}
		if(tf||exist){
			MaintenanceStaffRecord maintenanceStaffRecord = new MaintenanceStaffRecord();
			User u = new User();
			if(tf){
				u = userService.queryUserByEmployeeName(deviceRepair.getMaintainName());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.ARTIFICIALGASSIGN);
				maintenanceStaffRecord.setAssignCode(user.getEmployee().getCode());
				maintenanceStaffRecord.setAssignName(user.getEmployee().getName());
				maintenanceStaffRecord.setName(employee.getName());
				maintenanceStaffRecord.setCode(employee.getCode());
				MaintenanceStaff msf=maintenanceStaffService.queryByProperty("name", employee.getName());
				maintenanceStaffService.updateStatus(msf.getId(),Constant.MaintenanceStaffStatus.MAINTAIN, Constant.ReceiveType.SYSTEMGASSIGN);
			}else if(exist){
				u = userService.queryUserByEmployeeCode(maintenanceStaff.get(0).getCode());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.SYSTEMGASSIGN);
				maintenanceStaffRecord.setName(maintenanceStaff.get(0).getName());
				maintenanceStaffRecord.setCode(maintenanceStaff.get(0).getCode());
				maintenanceStaffService.updateStatus(maintenanceStaff.get(0).getId(),Constant.MaintenanceStaffStatus.MAINTAIN, Constant.ReceiveType.SYSTEMGASSIGN);
			}
			maintenanceStaffRecord.setDeviceRepair(dro);
			
			maintenanceStaffRecord.setAssignTime(new Date());
			maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
			//deviceRepairOrderService.receiveDeviceRepair(deviceRepair, u, modelMap);
			if(null!=u.getEmployee()){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, u.getEmployee().getName(),
						DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username,
						DeviceRepairStatus.WAITINCOMFIRM);
			}
			
			//推送给责任人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(u.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		modelMap.addAttribute("orderId", Long.parseLong(id.toString()));
		return modelMap;
	}
	
	
	
	/**
	 * 查询角标对应的数据数量
	 */
	@RequestMapping("/queryBadgeWithDeviceRepair.do")
	@ResponseBody
	public ModelMap queryBadgeWithDeviceRepair(String username) {
		User user=userService.queryByProperty("username", username);
		ModelMap modelMap=new ModelMap();
		//待派单数量
		String wlhql="select count(*) from DeviceRepair dr where dr.status='WAITINGASSIGN'";
		int wlcount=deviceRepairOrderService.queryBadgeWithDeviceRepair(wlhql);
		modelMap.addAttribute("waitingList", wlcount);
		//待接单数量
		int wrcount=deviceRepairOrderService.queryReceiptDeviceRepairWithUser(user.getEmployee().getCode()).size();
		modelMap.addAttribute("waitingReceipt", wrcount);
		//维修中数量
		int mxcount=deviceRepairOrderService.queryMaintenanceDeviceRepairWithUser(user.getEmployee().getCode()).size();
		modelMap.addAttribute("maintaining", mxcount);
		//待确认
		int wccount=deviceRepairOrderService.queryWorkshopcomfirmWithInformant(user.getEmployee().getCode()).size();
		modelMap.addAttribute("waitcomfirm", wccount);
		return modelMap;
	}
	/**
	 * 查询所有设备报修
	 */
	@RequestMapping("/queryAllDeviceRepair.do")
	@ResponseBody
	public List<DeviceRepair> queryAllDeviceRepair(HttpServletRequest request){	
		/*return deviceRepairOrderService.queryAllDeviceRepair();*/
		List<DeviceRepair> deviceRepairs=deviceRepairOrderService.queryAllDeviceRepair();
		for(DeviceRepair dp:deviceRepairs) {
			List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dp.getId());
			if(dppList!=null&&!dppList.isEmpty()) {
				for(DeviceRepairPic dpp:dppList) {
					List<String> pnList = dp.getPicName();
					pnList.add(dpp.getPicName());
					dp.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = dpp.getPic().getBinaryStream();
						File out = new File(dir,dpp.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return deviceRepairs;
	}
	/**
	 * 根据报修单状态查询设备报修单
	 */
	@RequestMapping("/queryDeviceRepairByStatus.do")
	@ResponseBody
	public List<DeviceRepairVO> queryDeviceRepairByStatus(HttpServletRequest request,String status){	
		/*return deviceRepairOrderService.queryAllDeviceRepair();*/
		List<DeviceRepair> deviceRepairs=deviceRepairOrderService.queryDeviceRepairWithStatus(status);
		List<DeviceRepairVO> volist=new ArrayList<>();
		for(DeviceRepair dp:deviceRepairs) {
			List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dp.getId());
			if(dppList!=null&&!dppList.isEmpty()) {
				for(DeviceRepairPic dpp:dppList) {
					List<String> pnList = dp.getPicName();
					pnList.add(dpp.getPicName());
					dp.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = dpp.getPic().getBinaryStream();
						File out = new File(dir,dpp.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			volist.add(model2VO(dp));
		}
		return volist;
	}
	/**
	 * 根据报修单状态查询设备报修单
	 */
	@RequestMapping("/queryDeviceRepairWithStatusForScreen.do")
	@ResponseBody
	public List<DeviceRepairVO> queryDeviceRepairWithStatusForScreen(HttpServletRequest request,String status,
			String productionLineCode,String maintainerCode,String deviceCode,
			 String ngFaultTypeonCode,String startDate,String endDate){	
		/*return deviceRepairOrderService.queryAllDeviceRepair();*/
		List<DeviceRepair> deviceRepairs=deviceRepairOrderService.queryDeviceRepairWithStatusForScreen(status,productionLineCode,maintainerCode,deviceCode,ngFaultTypeonCode,startDate,endDate);
		List<DeviceRepairVO> volist=new ArrayList<>();
		for(DeviceRepair dp:deviceRepairs) {
			List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dp.getId());
			if(dppList!=null&&!dppList.isEmpty()) {
				for(DeviceRepairPic dpp:dppList) {
					List<String> pnList = dp.getPicName();
					pnList.add(dpp.getPicName());
					dp.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = dpp.getPic().getBinaryStream();
						File out = new File(dir,dpp.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			volist.add(model2VO(dp));
		}
		return volist;
	}
	/**
	 * 根据报修单状态查询设备报修单(分页)
	 */
	@RequestMapping("/queryDeviceRepairByStatusByPage.do")
	@ResponseBody
	public ModelMap queryDeviceRepairByStatusByPage(HttpServletRequest request,String status,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String productionLineCode,String maintainerCode,String deviceCode,
			 String ngFaultTypeonCode,String startDate,String endDate){
		ModelMap modelMap = new ModelMap();
		String hql="from DeviceRepair dr where dr.status=?0 ";
		List<Object> paramList = new ArrayList<>();
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
    			paramList.add(sdf.parse(startDate+" 00:00:00"));
    		}
    		if(!StringUtils.isEmpty(endDate)&&endDate!="") {
    			hql += " and dr.createDate<=?" + (i++);
    			paramList.add(sdf.parse(endDate+" 23:59:59"));
    		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hql+=" order by dr.completeDate desc";
		@SuppressWarnings("unchecked")
		Pager<DeviceRepair> pager=deviceRepairOrderService.queryObjs(hql, page, rows, paramList.toArray());
		
		List<DeviceRepairVO> volist=new ArrayList<>();
		for(DeviceRepair dp:pager.getData()) {
			List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dp.getId());
			if(dppList!=null&&!dppList.isEmpty()) {
				for(DeviceRepairPic dpp:dppList) {
					List<String> pnList = dp.getPicName();
					pnList.add(dpp.getPicName());
					dp.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = dpp.getPic().getBinaryStream();
						File out = new File(dir,dpp.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			volist.add(model2VO(dp));
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", volist);
		return modelMap;
	}
	/**
	 * 根据报修单状态和维修人员查询设备报修单
	 */
	@RequestMapping("/queryDeviceRepairByStatusandUsername.do")
	@ResponseBody
	public List<DeviceRepairVO> queryDeviceRepairByStatusandUsername(HttpServletRequest request,String status,String username){	
		List<DeviceRepair> deviceRepairs=new ArrayList<>();
		User user=userService.queryByProperty("username", username);
		if(status.equals("MAINTAINING")) {
			deviceRepairs=deviceRepairOrderService.queryMaintenanceDeviceRepairWithUser(user.getEmployee().getCode());
		}else if(status.equals("WAITINCOMFIRM")) {
			deviceRepairs=deviceRepairOrderService.queryReceiptDeviceRepairWithUser(user.getEmployee().getCode());
		}else if(status.equals("MAINTAINCOMPLETE")) {
			deviceRepairs=deviceRepairOrderService.queryCompletedDeviceRepairWithUser(user.getEmployee().getCode());
		}
		/*deviceRepairs=deviceRepairOrderService.queryDeviceRepairWithUserandStatus(status, username);*/
		List<DeviceRepairVO> volist=new ArrayList<>();
		for(DeviceRepair dp:deviceRepairs) {
			List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dp.getId());
			if(dppList!=null&&!dppList.isEmpty()) {
				for(DeviceRepairPic dpp:dppList) {
					List<String> pnList = dp.getPicName();
					pnList.add(dpp.getPicName());
					dp.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = dpp.getPic().getBinaryStream();
						File out = new File(dir,dpp.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			volist.add(model2VO(dp));
		}
		return volist;
	}
	
	/**
	 * 根据报修单状态和报修人员查询设备报修单
	 */
	@RequestMapping("/queryDeviceRepairByStatusandInformant.do")
	@ResponseBody
	public List<DeviceRepairVO> queryDeviceRepairByStatusandInformant(HttpServletRequest request,String status,String InformantCode){	
		/*return deviceRepairOrderService.queryAllDeviceRepair();*/
		List<DeviceRepair> deviceRepairs=new ArrayList<>();
		if(status.equals("WAITWORKSHOPCOMFIRM")) {
			deviceRepairs=deviceRepairOrderService.queryWorkshopcomfirmWithInformant(InformantCode);
		}
		List<DeviceRepairVO> volist=new ArrayList<>();
		for(DeviceRepair dp:deviceRepairs) {
			List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dp.getId());
			if(dppList!=null&&!dppList.isEmpty()) {
				for(DeviceRepairPic dpp:dppList) {
					List<String> pnList = dp.getPicName();
					pnList.add(dpp.getPicName());
					dp.setPicName(pnList);
					String dir = request.getServletContext().getRealPath("/");
					try {
						InputStream is;
						is = dpp.getPic().getBinaryStream();
						File out = new File(dir,dpp.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			volist.add(model2VO(dp));
		}
		return volist;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryAppDeviceRepairOrderById.do")
	@ResponseBody
	public DeviceRepairVO queryDeviceRepairOrderById(Long id,HttpServletRequest request){
		DeviceRepair dro = deviceRepairOrderService.queryObjById(id);
		List<DeviceRepairPic> dppList=deviceRepairOrderPicService.queryListByProperty(dro.getId());
		if(dppList!=null&&!dppList.isEmpty()) {
			for(DeviceRepairPic dpp:dppList) {
				List<String> pnList = dro.getPicName();
				pnList.add(dpp.getPicName());
				dro.setPicName(pnList);
				String dir = request.getServletContext().getRealPath("/");
				try {
					InputStream is;
					is = dpp.getPic().getBinaryStream();
					File out = new File(dir,dpp.getPicName());
					FileCopyUtils.copy(is, new FileOutputStream(out));
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return model2VO(dro);
	}
	
	private DeviceRepairVO model2VO(DeviceRepair deviceRepair) {
		if(deviceRepair == null) {
			return null;
		}
		DeviceRepairVO vo = new DeviceRepairVO();
		vo.setId(deviceRepair.getId());
		vo.setCreateDate(sdf.format(deviceRepair.getCreateDate()));
		vo.setStatus(deviceRepair.getStatus());
		vo.setInformant(deviceRepair.getInformant());
		vo.setInformantCode(deviceRepair.getInformantCode());
		vo.setNgDescription(deviceRepair.getNgDescription());
		vo.setSerialNumber(deviceRepair.getSerialNumber());
		vo.setMaintainCode(deviceRepair.getMaintainCode());
		vo.setMaintainName(deviceRepair.getMaintainName());
		vo.setConfirmCode(deviceRepair.getConfirmCode());
		vo.setConfirmName(deviceRepair.getConfirmName());
		vo.setClosed(deviceRepair.getClosed());
		vo.setFailSafeOperation(deviceRepair.getFailSafeOperation());
		vo.setDispatchedLevel(deviceRepair.getDispatchedLevel());
		if(deviceRepair.getPicName().size()!=0&&deviceRepair.getPicName()!=null) {
			vo.setPicName(deviceRepair.getPicName());
		}
		if(deviceRepair.getDevice()!=null){
			vo.setDeviceId(deviceRepair.getDevice().getId());
			vo.setDeviceCode(deviceRepair.getDevice().getCode());
			vo.setDeviceName(deviceRepair.getDevice().getName());
			vo.setDeviceInstallPosition(deviceRepair.getDevice().getInstallPosition());
			vo.setDeviceUnitType(deviceRepair.getDevice().getUnitType());
			if(deviceRepair.getDevice().getProjectType()!=null){
				vo.setDeviceTypeId(deviceRepair.getDevice().getProjectType().getId());
				vo.setDeviceTypeCode(deviceRepair.getDevice().getProjectType().getCode());
				vo.setDeviceTypeName(deviceRepair.getDevice().getProjectType().getName());
			}
			if(deviceRepair.getDevice().getProductionUnit()!=null){
				vo.setProductionUnitId(deviceRepair.getDevice().getProductionUnit().getId());
				vo.setProductionUnitCode(deviceRepair.getDevice().getProductionUnit().getCode());
				vo.setProductionUnitName(deviceRepair.getDevice().getProductionUnit().getName());
			}
		}
		if(deviceRepair.getNgreason()!=null){
			if(deviceRepair.getNgreason().getProjectType()!=null) {
				vo.setNGId(deviceRepair.getNgreason().getProjectType().getId());
				vo.setNGCode(deviceRepair.getNgreason().getProjectType().getCode());
				vo.setNGName(deviceRepair.getNgreason().getProjectType().getName());
			}
			vo.setPressLight(deviceRepair.getNgreason().getName());
		}
		return vo;
	}
	/**
	 * 维修中转化为车间确认
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrderToConfirm.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrderToConfirm(String id,String status,String username,String confirmCode,Boolean failSafeOperation){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		DeviceRepair dro = deviceRepairOrderService.queryObjById(Long.parseLong(id));
		List<MaintenanceStaffRecord> mList = maintenanceStaffRecordService.queryByOrderId(dro.getId());
		String maintainCode=dro.getMaintainCode();
		User confirmUser = userService.queryUserByEmployeeCode(confirmCode);
		//维修中转化为车间确认
		if(dro.getStatus().equals(DeviceRepairStatus.MAINTAINING)) {
			dro.setConfirmCode(confirmCode);
			dro.setConfirmName(confirmUser.getEmployee().getName());
			dro.setStatus(status);
			dro.setFailSafeOperation(failSafeOperation);
			dro.setSubmitConfirmDate(new Date());
			if(null!=user.getEmployee()){
				deviceRepairOrderService.confirmDeviceRepair(dro, user.getEmployee().getName(),status);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(dro, username,status);
			}
			for (MaintenanceStaffRecord ml : mList) {
				Double MaintenanceTime =(double) Math.round(((double)(new Date().getTime()-dro.getCreateDate().getTime())/60000) * 100) / 100;
				ml.setMaintenanceTime(MaintenanceTime);
				ml.setCompleteTime(new Date());
				maintenanceStaffRecordService.updateObj(ml);
				MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", ml.getCode());
				maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY,
						Constant.ReceiveType.SYSTEMGASSIGN);
				
			}
			MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", maintainCode);
			//推送给确认人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(confirmUser.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_CONFIRM, DeviceRepairStatus.DEVICEREPAIRCONTENT_CONFIRM);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//获取未分配的单分配
			dro=deviceRepairOrderService.queryFirstDeviceRepair();	
			if(dro!=null) {
				MaintenanceStaffRecord maintenanceStaffRecord = new MaintenanceStaffRecord();
				maintenanceStaffRecord.setDeviceRepair(dro);
				maintenanceStaffRecord.setName(maintenanceStaff.getName());
				maintenanceStaffRecord.setCode(maintenanceStaff.getCode());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.SYSTEMGASSIGN);
				maintenanceStaffRecord.setAssignTime(new Date());
				maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
				deviceRepairOrderService.updateObj(dro);
				dro.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
				dro.setMaintainName(maintenanceStaff.getName());
				dro.setMaintainCode(maintenanceStaff.getCode());
				deviceRepairOrderService.updateObj(dro);
				Employee employee=employeeService.queryByProperty("code", maintainCode);
				//推送给责任人
				User maintainUser =userService.queryUserByEmployeeCode(maintenanceStaff.getCode());
				List<String> clientIdsList1 = new ArrayList<>();
				clientIdsList1.add(maintainUser.getUsername()+"");
				try {
					PushtoAPP.pushMessage(clientIdsList1, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null!=employee){
					deviceRepairOrderService.confirmDeviceRepair(dro, employee.getName(),DeviceRepairStatus.WAITINCOMFIRM);
				}else{
					deviceRepairOrderService.confirmDeviceRepair(dro, username,DeviceRepairStatus.WAITINCOMFIRM);
				}
			}
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("type","维修完成待确认");
			return modelMap;
		}
		else{
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("type","维修完成待确认");
		}
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("message", "该记录已完成或未确认!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 维修中转化为车间确认（上传）
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrderToConfirmWithPic.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrderToConfirmWithPic(@RequestParam("files")List<MultipartFile> files,String repairId,String status,String username,String confirmCode,String failSafe,HttpServletRequest request){
		if(repairId.contains("'")) {
			repairId = repairId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Boolean failSafeOperation=Boolean.valueOf(failSafe);
		User user = userService.queryByProperty("username", username);
		DeviceRepair dro = deviceRepairOrderService.queryObjById(Long.parseLong(repairId));
		List<MaintenanceStaffRecord> mList = maintenanceStaffRecordService.queryByOrderId(dro.getId());
		String maintainCode=dro.getMaintainCode();
		User confirmUser = userService.queryUserByEmployeeCode(confirmCode);
		//维修中转化为车间确认
		if(dro.getStatus().equals(DeviceRepairStatus.MAINTAINING)) {
			dro.setConfirmCode(confirmCode);
			dro.setConfirmName(confirmUser.getEmployee().getName());
			dro.setStatus(status);
			dro.setFailSafeOperation(failSafeOperation);
			dro.setSubmitConfirmDate(new Date());
			if(null!=user.getEmployee()){
				deviceRepairOrderService.confirmDeviceRepair(dro, user.getEmployee().getName(),status);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(dro, username,status);
			}
			for (MaintenanceStaffRecord ml : mList) {
				Double MaintenanceTime =(double) Math.round(((double)(new Date().getTime()-dro.getCreateDate().getTime())/60000) * 100) / 100;
				ml.setMaintenanceTime(MaintenanceTime);
				ml.setCompleteTime(new Date());
				maintenanceStaffRecordService.updateObj(ml);
				MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", ml.getCode());
				maintenanceStaffService.updateStatus(maintenanceStaff, Constant.MaintenanceStaffStatus.ONDUTY,
						Constant.ReceiveType.SYSTEMGASSIGN);
			}
			MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", maintainCode);
			//图片保存
			if(files!=null && files.size()>0) {
				String dir = request.getServletContext().getRealPath("/")+"MaintenanceRelatedDocument";
				for(MultipartFile file : files) {
					String picName = file.getOriginalFilename();
					InputStream is;
					try {
						is = file.getInputStream();
						File parentDir =new File(dir);
						if(!parentDir.exists()) {
							parentDir.mkdirs();
						}
						File out = new File(parentDir,picName);
						FileCopyUtils.copy(is, new FileOutputStream(out));
						MaintenanceRelatedDocument document = new MaintenanceRelatedDocument();
						document.setContentType(file.getContentType());
						document.setFileSize(file.getSize());
						document.setSrcName(picName);
						document.setUploadDate(new Date());
						MaintenanceStaffRecord d = new MaintenanceStaffRecord();
						d.setId(Long.parseLong(repairId));
						document.setMaintenanceStaffRecord(d);
						document.setUploadUsername(username);
						document.setUrl("MaintenanceRelatedDocument" +"/"+ picName);
						document.setName(picName);
						document.setNote("");
						maintenanceRelatedDocumentService.addObj(document);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			//推送给确认人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(confirmUser.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_CONFIRM, DeviceRepairStatus.DEVICEREPAIRCONTENT_CONFIRM);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//获取未分配的单分配
			dro=deviceRepairOrderService.queryFirstDeviceRepair();	
			if(dro!=null) {
				MaintenanceStaffRecord maintenanceStaffRecord = new MaintenanceStaffRecord();
				maintenanceStaffRecord.setDeviceRepair(dro);
				maintenanceStaffRecord.setName(maintenanceStaff.getName());
				maintenanceStaffRecord.setCode(maintenanceStaff.getCode());
				maintenanceStaffRecord.setReceiveType(Constant.ReceiveType.SYSTEMGASSIGN);
				maintenanceStaffRecord.setAssignTime(new Date());
				maintenanceStaffRecordService.addObj(maintenanceStaffRecord);
				deviceRepairOrderService.updateObj(dro);
				dro.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
				dro.setMaintainName(maintenanceStaff.getName());
				dro.setMaintainCode(maintenanceStaff.getCode());
				deviceRepairOrderService.updateObj(dro);
				Employee employee=employeeService.queryByProperty("code", maintainCode);
				//推送给责任人
				User maintainUser =userService.queryUserByEmployeeCode(maintenanceStaff.getCode());
				List<String> clientIdsList1 = new ArrayList<>();
				clientIdsList1.add(maintainUser.getUsername()+"");
				try {
					PushtoAPP.pushMessage(clientIdsList1, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(null!=employee){
					deviceRepairOrderService.confirmDeviceRepair(dro, employee.getName(),DeviceRepairStatus.WAITINCOMFIRM);
				}else{
					deviceRepairOrderService.confirmDeviceRepair(dro, username,DeviceRepairStatus.WAITINCOMFIRM);
				}
			}
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("type","维修完成待确认");
			return modelMap;
		}
		else{
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("type","维修完成待确认");
		}
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("message", "该记录已完成或未确认!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 维修中转化为车间确认小程序上传图片
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrderToConfirmWithPicWX.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrderToConfirmWithPicWX(@RequestParam("file")MultipartFile file,Long repairId,String username,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		DeviceRepair dro = deviceRepairOrderService.queryObjById(repairId);
		//图片保存
		if(file!=null) {
			String dir = request.getServletContext().getRealPath("/")+"MaintenanceRelatedDocument";
			String picName = file.getOriginalFilename();
			InputStream is;
			try {
				is = file.getInputStream();
				File parentDir =new File(dir);
				if(!parentDir.exists()) {
					parentDir.mkdirs();
				}
				File out = new File(parentDir,picName);
				//FileUtils.copyInputStreamToFile(is, out);
				FileCopyUtils.copy(is, new FileOutputStream(out));
				MaintenanceRelatedDocument document = new MaintenanceRelatedDocument();
				document.setContentType(file.getContentType());
				document.setFileSize(file.getSize());
				document.setSrcName(picName);
				document.setUploadDate(new Date());
				MaintenanceStaffRecord d = new MaintenanceStaffRecord();
				d.setId(repairId);
				document.setMaintenanceStaffRecord(d);
				document.setUploadUsername(username);
				document.setUrl("MaintenanceRelatedDocument" +"/"+ picName);
				document.setName(picName);
				document.setNote("");
				maintenanceRelatedDocumentService.addObj(document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("type","维修完成待确认");
		return modelMap;
	}
	/**
	 * 车间确认转为维修完成(接收报修单)
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrderToOver.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrderToOver(String id,String status,String username,Boolean isclose,double occupyTime){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		User user = userService.queryByProperty("username", username);
		DeviceRepair dro = deviceRepairOrderService.queryObjById(Long.parseLong(id));
		//车间确认转为维修完成
		if(dro.getStatus().equals(DeviceRepairStatus.WAITWORKSHOPCOMFIRM)) {
			dro.setStatus(status);
			dro.setCompleteDate(new Date());
			dro.setClosed(isclose);
			if(null!=user.getEmployee()){
				deviceRepairOrderService.confirmDeviceRepair(dro, user.getEmployee().getName(),status);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(dro, username,status);
			}
			List<MaintenanceStaffRecord> mList = maintenanceStaffRecordService.queryByOrderId(dro.getId());
			for(MaintenanceStaffRecord m:mList){
				m.setCompleteTime(new Date());
				m.setOccupyTime(occupyTime);
				maintenanceStaffRecordService.updateObj(m);
			}
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("type", "车间确认完成");
			return modelMap;
		}else {
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("type","车间确认完成");
		}
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("message", "该记录已完成或未确认!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * App(抢单，接单)
	 */
	@RequestMapping("/updateRobList.do")
	@ResponseBody
	public ModelMap updateRobList(Long recordId,String username,String type) {
		ModelMap modelMap = new ModelMap();
		DeviceRepair deviceRepair=deviceRepairOrderService.queryObjById(recordId);
		deviceRepair.setStatus(DeviceRepairStatus.MAINTAINING);
		//查询维修人员
		User user = userService.queryByProperty("username", username);
		MaintenanceStaff ms=maintenanceStaffService.queryByProperty("code", user.getEmployee().getCode());
		if(ms==null){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "不是维修人员无法接单或者抢单");
			return modelMap;
		}
		List<MaintenanceStaffRecord> list= maintenanceStaffRecordService.queryByOrderId(recordId);
		if(type.equals("抢单")) {
			if(list!=null&&list.size()>0) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "该单已有人维修");
			}else {
				MaintenanceStaffRecord msr=new MaintenanceStaffRecord();
				msr.setCode(ms.getCode());
				msr.setName(ms.getName());
				msr.setReceiveType(Constant.ReceiveType.ROBLIST);
				msr.setAssignTime(new Date());
				msr.setReceiveTime(new Date());
				msr.setDeviceRepair(deviceRepair);
				maintenanceStaffRecordService.addObj(msr);
				maintenanceStaffService.updateStatus(ms.getId(),Constant.MaintenanceStaffStatus.MAINTAIN, Constant.ReceiveType.ROBLIST);
				deviceRepair.setMaintainName(user.getEmployee().getName());
				deviceRepair.setMaintainCode(user.getEmployee().getCode());
				if(null!=user.getEmployee()){
					deviceRepairOrderService.confirmDeviceRepair(deviceRepair, user.getEmployee().getName(), Constant.ReceiveType.ROBLIST);
				}else{
					deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, Constant.ReceiveType.ROBLIST);
				}
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("result", "抢单成功");
			}
		}else if(type.equals("接单")) {
			MaintenanceStaffRecord msr= maintenanceStaffRecordService.queryByOrderIdandUsercode(recordId, user.getEmployee().getCode()).get(0);
			
			msr.setReceiveTime(new Date());
			maintenanceStaffRecordService.updateObj(msr);
			maintenanceStaffService.updateStatus(ms.getId(),Constant.MaintenanceStaffStatus.MAINTAIN, msr.getAssignName());
			//deviceRepairOrderService.updateObj(deviceRepair);
			if(null!=user.getEmployee()){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, user.getEmployee().getName(), DeviceRepairStatus.MAINTAINING);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, DeviceRepairStatus.MAINTAINING);
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("result", "接单完成");
	    }
		return modelMap;
	}
	/**
	 * App(返修)
	 */
	@RequestMapping("/updateDeviceRepairForConfirmAndReword.do")
	@ResponseBody
	public ModelMap updateDeviceRepairForConfirmAndReword(Long recordId,String status,String username) {
		ModelMap modelMap = new ModelMap();
		DeviceRepair deviceRepair=deviceRepairOrderService.queryObjById(recordId);
		User user=userService.queryUserByUsername(username);
		deviceRepair.setStatus(status);
		deviceRepair.setCompleteDate(null);
		deviceRepairOrderService.updateObj(deviceRepair);
		if(null!=user.getEmployee()){
			deviceRepairOrderService.confirmDeviceRepair(deviceRepair, user.getEmployee().getName(), Constant.ReceiveType.REWORK);
		}else{
			deviceRepairOrderService.confirmDeviceRepair(deviceRepair, username, Constant.ReceiveType.REWORK);
		}
		
		//查询协助人
		List<MaintenanceStaffRecord>  MaintenanceStaffRecordList=maintenanceStaffRecordService.queryMaintenanceStaffRecordHelpByorderId(recordId);
		//清空协助人
		if(MaintenanceStaffRecordList!=null&&MaintenanceStaffRecordList.size()>0) {
			for(MaintenanceStaffRecord msr:MaintenanceStaffRecordList) {
				maintenanceStaffRecordService.deleteObj(msr.getId());
			}
		}
		//查询负责人
		List<MaintenanceStaffRecord> msrList=maintenanceStaffRecordService.queryMaintenanceStaffRecordPersonLiableByorderId(recordId);
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
			PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modelMap.addAttribute("success", true);
		return modelMap;
	}
	/**
	 * App(派单)
	 * @param recordId 
	 * @param operatorName  操作人姓名
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping("/updateDeviceRepairForDispatch.do")
	@ResponseBody
	public ModelMap updateDeviceRepairForDispatch(Long recordId,String maintainCode,String operatorName) {
		ModelMap modelMap = new ModelMap();
		List<MaintenanceStaffRecord> list= maintenanceStaffRecordService.queryByOrderId(recordId);
		Employee employee=employeeService.queryByProperty("code", maintainCode);
		if(list!=null && list.size()>0) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该单已有人维修");
		}else {
			User user=userService.queryByProperty("username", operatorName);
			MaintenanceStaff ms=maintenanceStaffService.queryByProperty("name", employee.getName());
			User maintainUser =userService.queryUserByEmployeeCode(maintainCode);
			DeviceRepair deviceRepair=deviceRepairOrderService.queryObjById(recordId);
			deviceRepair.setStatus(DeviceRepairStatus.WAITINCOMFIRM);
			deviceRepair.setMaintainName(employee.getName());
			deviceRepair.setMaintainCode(employee.getCode());
			
			//deviceRepairOrderService.updateObj(deviceRepair);
			if(null!=employee){
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, employee.getName(),DeviceRepairStatus.WAITINCOMFIRM);
			}else{
				deviceRepairOrderService.confirmDeviceRepair(deviceRepair, operatorName,DeviceRepairStatus.WAITINCOMFIRM);
			}
			
			//推送给责任人
			List<String> clientIdsList = new ArrayList<>();
			clientIdsList.add(maintainUser.getUsername()+"");
			clientIdsList=appClientMapService.queryCids(clientIdsList);
			try {
				PushtoAPP.pushMessage(clientIdsList, DeviceRepairStatus.DEVICEREPAIRTITLE_ASSIGN, DeviceRepairStatus.DEVICEREPAIRCONTENT_ASSIGN);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MaintenanceStaffRecord msr=new MaintenanceStaffRecord();
			msr.setCode(ms.getCode());
			msr.setName(ms.getName());
			msr.setReceiveType(Constant.ReceiveType.ARTIFICIALGASSIGN);
			msr.setAssignTime(new Date());
			msr.setDeviceRepair(deviceRepair);
			if(user!=null&&user.getEmployee()!=null) {
				msr.setAssignCode(user.getEmployee().getCode());
				msr.setAssignName(user.getEmployee().getName());
			}else {
				msr.setAssignCode(user.getUsername());
				msr.setAssignName(user.getUsername());
			}
			maintenanceStaffRecordService.addObj(msr);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("result", "派单成功");
		}
		return modelMap;
	}
	/**
	 * 根据设备id和文档类型编码查找设备的维修项目文档
	 */
	@RequestMapping("/queryDocByDevice.do")
	@ResponseBody
	public ModelMap queryDocByDevice(String deviceCode) {
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.MAINTENANCEITEM, device.getId());
		modelMap.addAttribute("docs", docs);
		return modelMap;
	}
	
	/**
	 * 根据设备id查询该设备的所有维修记录
	 */
	@RequestMapping("/queryRecordBydeviceCode.do")
	@ResponseBody
	public List<DeviceRepairVO> queryRecordBydeviceCode(String deviceCode) {
		List<DeviceRepair> repairList= deviceRepairOrderService.queryDeviceRepairOrderByDeviceCode2(deviceCode);	
		List<DeviceRepairVO> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(repairList)) {
			for(DeviceRepair DR:repairList) {
				list.add(model2VO(DR));
			}
		}
		return list;
	}
}
