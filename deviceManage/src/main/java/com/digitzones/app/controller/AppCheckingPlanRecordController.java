package com.digitzones.app.controller;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.CheckingPlanRecordItem;
import com.digitzones.devmgr.model.CheckingPlanRecordItemFiles;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemFilesService;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemService;
import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.model.Classes;
import com.digitzones.model.Device;
import com.digitzones.model.RelatedDocument;
import com.digitzones.model.User;
import com.digitzones.service.IClassesService;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IRelatedDocumentService;
import com.digitzones.service.IUserService;
import com.digitzones.util.JwtTokenUnit;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/AppCheckingPlanRecord")
public class AppCheckingPlanRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private ICheckingPlanRecordService  checkingPlanRecordService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IRelatedDocumentService relatedDocumentService;
	@Autowired
	private IDeviceProjectRecordService deviceProjectRecordService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICheckingPlanRecordItemService checkingPlanRecordItemService;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private ICheckingPlanRecordItemFilesService checkingPlanRecordItemFilesService;
	
	/**
	 *	根据设备代码和当前时间查询第一条点检记录以及对应的点检项目
	 * @return
	 */
	@RequestMapping("/queryCheckingPlanRecordItemByDeviceCodeAndTime.do")
	@ResponseBody
	public ModelMap queryCheckingPlanRecordItemByDeviceCodeAndTime(String deviceCode) {
		ModelMap modelMap = new ModelMap();
		Classes classes=classesService.queryCurrentClasses();
		Calendar c = Calendar.getInstance();
		List<CheckingPlanRecord> recordList = checkingPlanRecordService.queryCheckingPlanRecordByDeviceCodeAndDateTime(deviceCode, c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH), classes.getCode());
		if(recordList.size()>0&&recordList!=null) {
			CheckingPlanRecord record=recordList.get(0);
			if(record!=null) {
				modelMap.addAttribute("record", record);
				//根据设备编码查询文档信息
				Device device = deviceService.queryByProperty("code", deviceCode);
				if(device!=null) {
					Long deviceId = device.getId();
					//查找关联文档
					List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, deviceId);
					modelMap.addAttribute("docs", docs);
					
					//根据设备编码查找点检记录项信息
					List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(record.getId());
					int count =checkingPlanRecordItemService.queryResultCountByCheckingPlanRecordId(record.getId());
					modelMap.addAttribute("count", count);
					if(CollectionUtils.isEmpty(list)) {
						//查找标准点检项目
						//List<DeviceProjectRecord> projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceIdAndtype(deviceId,"SPOTINSPECTION");
						List<DeviceProjectRecord> projectRecords = new ArrayList<>();
						if(record.getClassCode()!=null){
							projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceIdAndtypeAndClassesCode(deviceId,"SPOTINSPECTION",record.getClassCode());
						}else{
							projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceIdAndtype(deviceId,"SPOTINSPECTION");
						}
						for(int i = 0;i<projectRecords.size();i++) {
							DeviceProjectRecord deviceProjectRecord = deviceProjectRecordService.queryObjById(projectRecords.get(i).getId());


							CheckingPlanRecordItem item = new CheckingPlanRecordItem();
							
							item.setCheckingPlanRecord(record);
							item.setName(deviceProjectRecord.getName());
							item.setFrequency(deviceProjectRecord.getFrequency());
							item.setMethod(deviceProjectRecord.getMethod());
							item.setStandard(deviceProjectRecord.getStandard());
							item.setUpperLimit(deviceProjectRecord.getUpperLimit());
							item.setLowerLimit(deviceProjectRecord.getLowerLimit());
							
							checkingPlanRecordItemService.addObj(item);
						}
						list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(record.getId());
						modelMap.addAttribute("projectRecords", list);
						modelMap.addAttribute("success", true);
					}else {
						modelMap.addAttribute("projectRecords", list);
						modelMap.addAttribute("success", true);
					}
					
				}
			}
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "当天没有点检记录");
		}
		return modelMap;
	}
	
	/**
	 * 根据当前时间和设备代码查询点检计划记录
	 */
	@RequestMapping("/queryCheckingPlanRecordByDeviceCodeAndTime.do")
	@ResponseBody
	public ModelMap queryCheckingPlanRecordByDeviceCodeAndTime(String deviceCode) {
		ModelMap modelMap = new ModelMap();
		Calendar c = Calendar.getInstance();
		List<CheckingPlanRecord> recordList = checkingPlanRecordService.queryCheckingPlanRecordByDeviceCodeAndSelectTime(deviceCode, c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH), "","");
		if(recordList.size()>0&&recordList!=null) {
			modelMap.addAttribute("list", recordList);
			modelMap.addAttribute("success",true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该设备当天没有点检计划");
		}
		return modelMap;
	}
	
	/**
	 * 通过点检计划记录id查找点检项目
	 */
	@RequestMapping("/queryCheckingPlanRecordItemByCheckingPlanRecordId.do")
	@ResponseBody
	public ModelMap queryCheckingPlanRecordItemByCheckingPlanRecordId(Long recordId) {
		ModelMap modelMap = new ModelMap();
		CheckingPlanRecord record=checkingPlanRecordService.queryObjById(recordId);
		Device device = deviceService.queryByProperty("code", record.getDeviceCode());
		List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(recordId);
		int count =checkingPlanRecordItemService.queryResultCountByCheckingPlanRecordId(recordId);
		if(CollectionUtils.isEmpty(list)) {
			//查找标准点检项目
			List<DeviceProjectRecord> projectRecords = deviceProjectRecordService.queryDeviceProjectRecordByDeviceIdAndtype(device.getId(),"SPOTINSPECTION");
			for(int i = 0;i<projectRecords.size();i++) { 
				DeviceProjectRecord deviceProjectRecord = deviceProjectRecordService.queryObjById(projectRecords.get(i).getId());
				CheckingPlanRecordItem item = new CheckingPlanRecordItem();
				item.setCheckingPlanRecord(record);
				item.setName(deviceProjectRecord.getName());
				item.setFrequency(deviceProjectRecord.getFrequency());
				item.setMethod(deviceProjectRecord.getMethod());
				item.setStandard(deviceProjectRecord.getStandard());
				item.setUpperLimit(deviceProjectRecord.getUpperLimit());
				item.setLowerLimit(deviceProjectRecord.getLowerLimit());
				checkingPlanRecordItemService.addObj(item);
			}
			list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(recordId);
			count =checkingPlanRecordItemService.queryResultCountByCheckingPlanRecordId(recordId);
			modelMap.addAttribute("itemsList", list);
			modelMap.addAttribute("count", count);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("itemsList", list);
			modelMap.addAttribute("count", count);
			modelMap.addAttribute("success", true);
		}

		
		return modelMap;
	}
	
	/**
	 * 点检项目（单个项目）
	 */
	@RequestMapping("/deviceCheck.do")
	@ResponseBody
	public ModelMap deviceCheck(Long id,String result,String username,String note,String checkValue) {
		ModelMap modelMap = new ModelMap();
		checkingPlanRecordService.updateCheckingPlanRecordByOne(result, id,username,note,checkValue);

		return modelMap;
	}



	/**
	 * 点检图片上传
	 */
	@RequestMapping("/addAppCheckItemPic.do")
	@ResponseBody
	public ModelMap addAppCheckItemPic(@RequestParam("files")List<MultipartFile> files, Long id, HttpServletRequest request){
		String username=JwtTokenUnit.getUsername(request.getHeader("accessToken"));
		User user  = userService.queryByProperty("username", username);
		ModelMap modelMap = new ModelMap();
		if(files!=null && files.size()>0) {
			String dir = request.getServletContext().getRealPath("/")+"console/checkPlanItemImgs/";
			for(MultipartFile file : files) {
				if(file!=null) {
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
						/*DeviceRepairPic DRP=new DeviceRepairPic();
						DRP.setPicName("console/deviceImgs/" + picName);
						DRP.setOrderId(orderId);
						deviceRepairOrderPicService.addOrderPic(DRP, out);*/

						CheckingPlanRecordItem cItem=checkingPlanRecordItemService.queryObjById(id);
						CheckingPlanRecordItemFiles cpriFiles=new CheckingPlanRecordItemFiles();
						cpriFiles.setCheckingPlanRecordItem(cItem);
						cpriFiles.setContentType(file.getContentType());
						cpriFiles.setFileSize(file.getSize());
						cpriFiles.setFileName(picName);
						cpriFiles.setUploadDate(new Date());
						if(user.getEmployee()!=null) {
							cpriFiles.setUploaderCode(user.getEmployee().getCode());
							cpriFiles.setUploaderName(user.getEmployee().getName());
						}else{
							cpriFiles.setUploaderCode(user.getId()+"");
							cpriFiles.setUploaderName(user.getUsername());
						}
						cpriFiles.setUrl("console/checkPlanItemImgs/" + picName);
						checkingPlanRecordItemFilesService.addObj(cpriFiles);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}



		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}


	/**
	 * 根据点检项目id查询点检的图片
	 */
	@RequestMapping("/queryCheckingPlanRecordItemFilesById.do")
	@ResponseBody
	public List<CheckingPlanRecordItemFiles> queryCheckingPlanRecordItemFilesById(Long checkPlanItemId){
		List<CheckingPlanRecordItemFiles> list=checkingPlanRecordItemFilesService.queryCheckingPlanRecordItemFilesByItemId(checkPlanItemId);
		return list;
	}


	/**
	 * 根据点检项目file的id删除图片
	 */
	@RequestMapping("/deleteCheckingPlanRecordItemFilesById.do")
	@ResponseBody
	public ModelMap deleteCheckingPlanRecordItemFilesById(String id){
		ModelMap modelMap=new ModelMap();
		checkingPlanRecordItemFilesService.deleteObj(id);
		return modelMap;
	}
	
	/**
	 * 点检提交
	 */
	@RequestMapping("/deviceCheckSubmit.do")
	@ResponseBody
	public ModelMap deviceCheckSubmit(Long id,HttpServletRequest request) {
		String username=JwtTokenUnit.getUsername(request.getHeader("accessToken"));
		User user=userService.queryByProperty("username", username);
		ModelMap modelMap = new ModelMap();
		List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(id);
		int count=checkingPlanRecordItemService.queryResultCountByCheckingPlanRecordId(id);
		if(list.size()==count) {
			CheckingPlanRecord record=checkingPlanRecordService.queryObjById(id);
			record.setStatus("已完成");
			if(user.getEmployee()==null) {
				record.setEmployeeCode(user.getUsername());
				record.setEmployeeName(user.getUsername());
			}else {
				record.setEmployeeCode(user.getEmployee().getCode());
				record.setEmployeeName(user.getEmployee().getName());
			}
			
			record.setCheckedDate(new Date());
			checkingPlanRecordService.updateObj(record);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "您有未点检项，请先点检");
		}
		return modelMap;
	}
	
	/**
	 * 根据设备代码和选择时间查询点检项目
	 */
	@RequestMapping("/queryCheckingPlanRecordByDeviceCodeAndSelectTime.do")
	@ResponseBody
	public ModelMap queryCheckingPlanRecordByDeviceCodeAndSelectTime(String deviceCode,String date,String classCode) {
		ModelMap modelMap = new ModelMap();
		String[] array= new String[10];
		array=date.split("-");

		List<CheckingPlanRecord> recordList = checkingPlanRecordService.queryCheckingPlanRecordByDeviceCodeAndSelectTime(deviceCode, Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]), classCode,"已完成");
		if(recordList.size()>0&&recordList!=null) {
			ModelMap recordMap = new ModelMap();
			for(int j=0;j<recordList.size();j++) {
				ModelMap neir=new ModelMap();
				CheckingPlanRecord record=recordList.get(j);
				if(record!=null) {
					
					//根据设备编码查询文档信息
					Device device = deviceService.queryByProperty("code", deviceCode);
					if(device!=null) {
						Long deviceId = device.getId();
						//查找关联文档
						List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, deviceId);
						modelMap.addAttribute("docs", docs);
						
						//根据设备编码查找点检记录项信息
						List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(record.getId());
						int count =checkingPlanRecordItemService.queryResultCountByCheckingPlanRecordId(record.getId());
						neir.addAttribute("count", count);
						neir.addAttribute("items", list);
						neir.addAttribute("record", record);
						recordMap.addAttribute(record.getId()+"",neir);
					}
				}
			}
			modelMap.addAttribute("spockCheckList", recordMap);
			modelMap.addAttribute("success", true);
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该设备当天或当班没有点检记录");
		}
		return modelMap;
	}
	/**
	 * 查找未点检记录
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/queryNotSpotcheckRecord.do")
	@ResponseBody
	public ModelMap queryNotSpotcheckRecord(String startDate,String endDate) {
		ModelMap modelMap = new ModelMap();
	  List<Object[]> deviceList=checkingPlanRecordService.queryNotSpotcheckDeviceBytime(startDate, endDate);
	  List<Object[]> recordList=checkingPlanRecordService.queryNotSpotcheckRecordBytime(startDate, endDate);
	  modelMap.addAttribute("deviceList", deviceList);
	  modelMap.addAttribute("recordList", recordList);
	  return modelMap;
	}
	/**
	 * 根据设备id和文档类型编码查找设备的点检项目文档
	 */
	@RequestMapping("/queryDocByDevice.do")
	@ResponseBody
	public ModelMap queryDocByDevice(String deviceCode) {
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryByProperty("code", deviceCode);
		List<RelatedDocument> docs = relatedDocumentService.queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(Constant.RelatedDoc.DEVICE, Constant.DeviceProject.SPOTINSPECTION, device.getId());
		for(RelatedDocument d:docs){
		String str=d.getContentType().substring(d.getContentType().indexOf("/")+1, d.getContentType().length());
			d.setContentType(str);
			switch(str){
				case "png":
				case "PNG":
				case "jpg":
				case "JPG":
				case "JPEG":
				case "jpeg":
				case "bmp":
				case "BMP": {
					d.setNote("true");
					break;
				}
				default:{
					d.setNote("false");
				}
			}
		}
		modelMap.addAttribute("docs", docs);
		return modelMap;
	}
}
