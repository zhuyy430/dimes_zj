package com.digitzones.controllers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.digitzones.util.WorkpieceProcessParameterExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.config.QRConfig;
import com.digitzones.devmgr.service.IDeviceLevelService;
import com.digitzones.model.Device;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Pager;
import com.digitzones.model.ProductionUnit;
import com.digitzones.service.IDeviceService;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IProductionUnitService;
import com.digitzones.service.IProjectTypeService;
import com.digitzones.util.DeviceExcelUtil;
import com.digitzones.util.ExcelUtil;
import com.digitzones.util.QREncoder;
import com.digitzones.vo.DeviceVO;
/**
 * 设备管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/device")
public class DeviceController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private QRConfig config ;
	private QREncoder qrEncoder = new QREncoder();
	private IDeviceService deviceService;
	private IProductionUnitService productionUnitService;
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IProjectTypeService projectTypeService;
	@Autowired
	private IDeviceLevelService deviceLevelService;
	@Autowired
	public void setProductionUnitService(IProductionUnitService productionUnitService) {
		this.productionUnitService = productionUnitService;
	}
	@Autowired
	public void setConfig(QRConfig config) {
		this.config = config;
	}
	@Autowired
	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	/**
	 * 查找不在生产单元的设备
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryIdleDevices.do")
	@ResponseBody
	public ModelMap queryIdleDevices(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request){
		Pager<Device> pager = deviceService.queryObjs("from Device d where d.productionUnit is null"
				+ " and d.isDimesUse=?0", page, rows, new Object[] {true});
		ModelMap modelMap = new ModelMap();
		List<DeviceVO> vos = new ArrayList<>();
		if(pager.getData()!=null && pager.getData().size()>0) {
			for(Device d : pager.getData()) {
				vos.add(model2VO(d, request));
			}
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", vos);
		return modelMap;
	}
	/**
	 * 查找不在设备管理的设备
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllDevicesInDimes.do")
	@ResponseBody
	public ModelMap queryAllDevicesInDimes(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request){
		Pager<Device> pager = deviceService.queryObjs("from Device d where d.disabled!=?0 and d.isDimesUse=?1", page, rows, new Object[] {true,true});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 查找不在设备管理的设备
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllDevicesInDeviceMgr.do")
	@ResponseBody
	public ModelMap queryAllDevicesInDeviceMgr(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String deviceCode,String deviceName,String manufacturer,String productionUnitName){
		String hql = "from Device d where d.disabled!=?0 and d.isDeviceManageUse=?1";
		List<Object> paramsList = new ArrayList<>();
		paramsList.add(true);
		paramsList.add(true);
		int i = 2;
		if(!StringUtils.isEmpty(deviceName)) {
			hql += " and d.name like ?"+(i++);
			paramsList.add("%" + deviceName + "%");
		}
		if(!StringUtils.isEmpty(deviceCode)) {
			hql += " and d.code like ?"+(i++);
			paramsList.add("%" + deviceCode + "%");
		}
		if(!StringUtils.isEmpty(manufacturer)) {
			hql += " and d.manufacturer like ?"+(i++);
			paramsList.add("%" + manufacturer + "%");
		}
		if(!StringUtils.isEmpty(productionUnitName)) {
			hql += " and d.productionUnit.name like ?"+(i++);
			paramsList.add("%" + productionUnitName + "%");
		}
		Pager<Device> pager = deviceService.queryObjs(hql, page, rows, paramsList.toArray());
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 更新设备的生产单元信息
	 * @param productionUnitId 生产单元ID
	 * @param deviceIds 设备id字符串，形式如[11,22,33]
	 * @return
	 */
	@RequestMapping("/updateDeviceProductionUnit.do")
	@ResponseBody
	public ModelMap updateDeviceProductionUnit(Long productionUnitId,String deviceIds) {
		ModelMap modelMap = new ModelMap();
		if(deviceIds!=null) {
			if(deviceIds.contains("[")) {
				deviceIds = deviceIds.replace("[", "");
			}
			if(deviceIds.contains("]")) {
				deviceIds = deviceIds.replace("]", "");
			}

			String[] idArray = deviceIds.split(",");
			for(int i = 0;i<idArray.length;i++) {
				Device device = deviceService.queryObjById(Long.valueOf(idArray[i]));
				ProductionUnit pu = productionUnitService.queryObjById(productionUnitId);

				device.setProductionUnit(pu);

				deviceService.updateObj(device);
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}

		return modelMap;
	}
	/**
	 * 将设备的生产单元置为null
	 * @param productionUnitId
	 * @return
	 */
	@RequestMapping("/updateDeviceProductionUnitNull.do")
	@ResponseBody
	public ModelMap updateDeviceProductionUnitNull(String deviceId) {
		if(deviceId!=null && deviceId.contains("'")) {
			deviceId = deviceId.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryObjById(Long.valueOf(deviceId));
		device.setProductionUnit(null);
		deviceService.updateObj(device);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}

	/**
	 * 停用该设备
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledDevice.do")
	@ResponseBody
	public ModelMap disabledDevice(String id) {
		ModelMap modelMap = new ModelMap();
		try {
			if(id==null ||"".equals(id.trim())) {
				throw new RuntimeException("请选择设备!");
			}
			if(id!=null && id.contains("'")) {
				id = id.replace("'", "");
			}
			deviceService.disableDevices(id.split(","));
		}catch (Exception e) {
			e.printStackTrace();
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 分页查询生产单元中的设备
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDevicesByProductionUnitId.do")
	@ResponseBody
	public ModelMap queryDevicesByProductionUnitId(@RequestParam(value="productionUnitId",required=false)Long productionUnitId,
			@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			HttpServletRequest request,String module) {
		Pager<Device> pager = null;
		ModelMap mm = new ModelMap();
		if(productionUnitId==null) {
			return mm;
		}
		switch(module) {
		case "dimes":{
			if(productionUnitId==-1) {
				pager = deviceService.queryObjs("select d from Device d left join d.productionUnit p "
						+ " where ( p is null or p.id=?0) and d.isDimesUse=?1", page, rows, new Object[] {productionUnitId,true});
			}
			else {
				pager = deviceService.queryObjs("select d from Device d inner join d.productionUnit p  "
						+ "where p.id=?0 and d.isDimesUse=?1", page, rows, new Object[] {productionUnitId,true});
			}
			break;
		}
		case "deviceManage":{
			if(productionUnitId==-1) {
				pager = deviceService.queryObjs("select d from Device d left join d.productionUnit p "
						+ " where ( p is null or p.id=?0) and d.isDeviceManageUse=?1", page, rows, new Object[] {productionUnitId,true});
			}
			else {
				pager = deviceService.queryObjs("select d from Device d inner join d.productionUnit p  "
						+ "where p.id=?0 and d.isDeviceManageUse=?1", page, rows, new Object[] {productionUnitId,true});
			}
			break;
		}
		default:{
			if(productionUnitId==-1) {
				pager = deviceService.queryObjs("select d from Device d left join d.productionUnit p "
						+ " where ( p is null or p.id=?0) ", page, rows, new Object[] {productionUnitId});
			}
			else {
				pager = deviceService.queryObjs("select d from Device d inner join d.productionUnit p  "
						+ "where p.id=?0 ", page, rows, new Object[] {productionUnitId});
			}
		}
		}



		List<DeviceVO> vos = new ArrayList<>();
		if(pager.getData()!=null && pager.getData().size()>0) {
			for(Device d : pager.getData()) {
				vos.add(model2VO(d, request));
			}
		}
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("rows", vos);
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	/**
	 * 查询生产单元中的设备
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryAllDevicesByProductionUnitId.do")
	@ResponseBody
	public List<Device> queryAllDevicesByProductionUnitId(@RequestParam(value="productionUnitId",required=false)Long productionUnitId,String module) {
		List<Device> dList = deviceService.queryDevicesByProductionUnitId(productionUnitId,module);
		return dList;
	}

	/**
	 * 在生产单元中添加设备
	 * @param department
	 * @return
	 */
	@RequestMapping("/addDevice.do")
	@ResponseBody
	public ModelMap addDevice(Device device,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Device device4Code = deviceService.queryByProperty("code", device.getCode());
		if(device4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备编码已被使用");
		}else {
			if(device.getPhotoName()!=null &&!"".equals(device.getPhotoName())) {
				String dir = request.getServletContext().getRealPath("/");
				File file = new File(dir,device.getPhotoName());
				deviceService.addDevice(device,file);
			}else {
				deviceService.addObj(device);
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}

	/**
	 * 设备管理->设备卡片->添加设备
	 * @return
	 */
	@RequestMapping("/addDeviceAtManage.do")
	@ResponseBody
	public ModelMap addDeviceAtManage(Device device,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Device device4Code = deviceService.queryByProperty("code", device.getCode());
		if(device4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备编码已被使用");
		}else {
			if(device.getProjectType().getId()==null) {
				device.setProjectType(null);
			}

			if(device.getDeviceLevel().getId()==null) {
				device.setDeviceLevel(null);
			}
			device.setIsDeviceManageUse(true);
			if(device.getPhotoName()!=null &&!"".equals(device.getPhotoName())) {
				String dir = request.getServletContext().getRealPath("/");
				File file = new File(dir,device.getPhotoName());
				deviceService.addDevice(device,file);
			}else {
				deviceService.addObj(device);
			}
			DeviceSite deviceSite =new DeviceSite();
			deviceSite.setCode("DS_"+device.getCode());
			deviceSite.setName(device.getName());
			deviceSite.setDevice(device);
			deviceSiteService.addObj(deviceSite);	
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	@RequestMapping("/upload.do")
	@ResponseBody
	public ModelMap upload(Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
		String realName = file.getSubmittedFileName();
		ModelMap modelMap = new ModelMap();
		String fileName = new Date().getTime()+ realName.substring(realName.lastIndexOf("."));
		InputStream is;
		try {
			is = file.getInputStream();
			File parentDir = new File(dir);
			if(!parentDir.exists()) {
				parentDir.mkdirs();
			}
			File out = new File(parentDir,fileName);
			FileCopyUtils.copy(is, new FileOutputStream(out));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "文件上传成功！");
			modelMap.addAttribute("filePath", "console/deviceImgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelMap;
	}
	/**
	 * 根据id查询设备
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDeviceById.do")
	@ResponseBody
	public DeviceVO queryDeviceById(Long id,HttpServletRequest request) {
		Device device = deviceService.queryObjById(id);
		return model2VO(device,request);
	}
	/**
	 * 根据id查询设备
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDeviceByDeviceCode.do")
	@ResponseBody
	public DeviceVO queryDeviceByDeviceCode(String code,HttpServletRequest request) {
		Device device = deviceService.queryByProperty("code", code);
		return model2VO(device,request);
	}
	/**
	 * 根据id查询设备(复制)
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDeviceByIdWithCopy.do")
	@ResponseBody
	public DeviceVO queryDeviceByIdWithCopy(Long id,HttpServletRequest request) {
		Device device = deviceService.queryObjById(id);
		DeviceVO dVO= model2VO(device,request);
		dVO.setCode(null);
		return dVO;
	}
	/**
	 * 将领域模型转换为值对象
	 * @param device
	 * @return
	 */
	private DeviceVO model2VO(Device device,HttpServletRequest request) {
		if(device == null) {
			return null;
		}
		DeviceVO vo = new DeviceVO();
		vo.setId(device.getId());
		vo.setCode(device.getCode());
		vo.setName(device.getName());
		if(device.getInstallDate()!=null) {
			vo.setInstallDate(sdf.format(device.getInstallDate()));
		}
		if(device.getOutFactoryDate()!=null) {
			vo.setOutFactoryDate(sdf.format(device.getOutFactoryDate()));
		}
		if(device.getInFactoryDate()!=null) {
			vo.setInFactoryDate(sdf.format(device.getInFactoryDate()));
		}
		if(device.getCheckDate()!=null) {
			vo.setCheckDate(sdf.format(device.getCheckDate()));
		}
		vo.setInstallPosition(device.getInstallPosition());
		vo.setProjectType(device.getProjectType());
		if(device.getProjectType()!=null) {
			vo.setProjectTypeId(device.getProjectType().getId());
		}

		if(device.getDeviceLevel()!=null) {
			vo.setDeviceLevelCode(device.getDeviceLevel().getCode());
			vo.setDeviceLevelId(device.getDeviceLevel().getId());
			vo.setDeviceLevelName(device.getDeviceLevel().getName());
			vo.setDeviceLevelWeight(device.getDeviceLevel().getWeight());
		}
		vo.setManufacturer(device.getManufacturer());
		vo.setNote(device.getNote());
		vo.setOutFactoryCode(device.getOutFactoryCode());
		if(device.getPhoto()!=null) {
			String dir = request.getServletContext().getRealPath("/");
			String realName = device.getPhotoName();
			String fileName = realName;
			InputStream is;
			try {
				is = device.getPhoto().getBinaryStream();
				File out = new File(dir,fileName);
				FileCopyUtils.copy(is, new FileOutputStream(out));
				vo.setPhotoName(realName);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		vo.setParameterValueType(device.getParameterValueType());
		vo.setUnitType(device.getUnitType());
		vo.setProjectType(device.getProjectType());
		vo.setProductionUnit(device.getProductionUnit());
		vo.setTrader(device.getTrader());
		vo.setStatus(device.getStatus());
		vo.setDisabled(device.getDisabled());
		vo.setAssetNumber(device.getAssetNumber());
		vo.setWeight(device.getWeight());
		vo.setShapeSize(device.getShapeSize());
		vo.setPower(device.getPower());
		vo.setActualPower(device.getActualPower());
		return vo;
	}
	/**
	 * 更新设备
	 * @param device
	 * @return
	 */
	@RequestMapping("/updateDevice.do")
	@ResponseBody
	public ModelMap updateDevice(Device device,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		if(device.getProjectType()==null) {
			device.setProjectType(null);
		}

		if(device.getDeviceLevel()==null) {
			device.setDeviceLevel(null);
		}
		device.setIsDimesUse(true);
		if(device.getPhotoName()!=null &&!"".equals(device.getPhotoName())) {
			String dir = request.getServletContext().getRealPath("/");
			File file = new File(dir,device.getPhotoName());
			deviceService.updateDevice(device,file);
		}else {
			deviceService.updateObj(device);
		}	
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 更新设备(设备管理)
	 * @param device
	 * @return
	 */
	@RequestMapping("/updateDeviceAtManage.do")
	@ResponseBody
	public ModelMap updateDeviceAtManage(Device device,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		device.setIsDeviceManageUse(true);
		Device device4Code = deviceService.queryByProperty("code", device.getCode());
		if(device4Code!=null&&!device4Code.getId().equals(device.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备编码已被使用");
		}else {
			if(device.getProjectType().getId()==null) {
				device.setProjectType(null);
			}

			if(device.getDeviceLevel().getId()==null) {
				device.setDeviceLevel(null);
			}
			if(device.getPhotoName()!=null &&!"".equals(device.getPhotoName())) {
				String dir = request.getServletContext().getRealPath("/");
				File file = new File(dir,device.getPhotoName());
				deviceService.updateDevice(device,file);
			}else {
				deviceService.updateObj(device);
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
			return modelMap;
		}
		return modelMap;
	}
	/**
	 * 根据id删除设备
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDevice.do")
	@ResponseBody
	public ModelMap deleteDevice(String id,String isDeviceMgrUse) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		if(isDeviceMgrUse!=null) {
			deviceService.deleteMgrDevices(id.split(","));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
			return modelMap;
		}
		try {
			deviceService.deleteDevices(id.split(","));
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		}catch(RuntimeException e) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", e.getMessage());
		}
		return modelMap;
	}
	/**
	 * 将dimes的设备设置成设备管理可见
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateDimesDeviceToManage.do")
	@ResponseBody
	public ModelMap updateDimesDeviceToManage(String deviceIds) {
		ModelMap modelMap = new ModelMap();

		if(deviceIds!=null) {
			if(deviceIds.contains("[")) {
				deviceIds = deviceIds.replace("[", "");
			}
			if(deviceIds.contains("]")) {
				deviceIds = deviceIds.replace("]", "");
			}
			String[] idArray = deviceIds.split(",");
			for(String id:idArray){
				Device device = deviceService.queryObjById(Long.parseLong(id));
				device.setIsDeviceManageUse(true);
				deviceService.updateObj(device);
			}
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "导入成功!");
		return modelMap;
	}
	/**
	 * 根据id删除设备
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDeviceFromProductionUnit.do")
	@ResponseBody
	public ModelMap deleteDeviceFromProductionUnit(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Device device = deviceService.queryObjById(Long.valueOf(id));
		device.setProductionUnit(null);
		deviceService.updateObj(device);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 打印设备的二维码
	 * @param ids 设备id字符串
	 * @return
	 */
	@RequestMapping("/printQr.do")
	@ResponseBody
	public List<DeviceVO> printQr(String ids,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		List<DeviceVO> vos = new ArrayList<>();
		String[] idStr = ids.split(",");
		for(int i = 0 ;i<idStr.length;i++) {
			String id = idStr[i];
			Device e = deviceService.queryObjById(Long.valueOf(id));
			DeviceVO vo = model2VO(e,request);
			vo.setQrPath(qrEncoder.generatePR(e.getCode(),dir , config.getQrPath()));
			vos.add(vo);
		}
		return vos;
	}
	/**
	 * 查询所有设备
	 * @return
	 */
	@RequestMapping("/queryAllDevices.do")
	@ResponseBody
	public List<Device> queryAllDevices(String module){
		return deviceService.queryAllDevices(module);
	}
	/**
	 * 根据设备code查找设备
	 */
	@RequestMapping("/queryDeviceByCode.do")
	@ResponseBody
	public Device queryDeviceByCode(String name,String value) {
		return deviceService.queryByProperty(name, value);
	}
	@RequestMapping("/uploadTemplate.do")
	@ResponseBody
	public ModelMap uploadTemplate(MultipartFile file) {
		ModelMap modelMap = new ModelMap();
		ExcelUtil deviceExcelUtil = new DeviceExcelUtil(productionUnitService, projectTypeService,deviceLevelService);
		try {
			@SuppressWarnings("unchecked")
			List<Device> list = (List<Device>) deviceExcelUtil.getData(file);
			deviceService.imports(list);
			modelMap.addAttribute("success", true);
		}catch(RuntimeException re) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", re.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	@RequestMapping("/uploadWorkpieceProcessParameters.do")
	@ResponseBody
	public ModelMap uploadWorkpieceProcessParameters(MultipartFile file) {
		ModelMap modelMap = new ModelMap();
		try {
			deviceService.importWorkpieceProcessParameters(file);
			modelMap.addAttribute("success", true);
		}catch(RuntimeException re) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", re.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	/**
	 * 复制设备
	 */
	@RequestMapping("/copyDevice.do")
	@ResponseBody
	public ModelMap copyDevice(Device device,Long copyId,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		device.setIsDeviceManageUse(true);
		Device device4Code = deviceService.queryByProperty("code", device.getCode());
		if(device4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备编码已被使用");
		}else {
			if(device.getPhotoName()!=null &&!"".equals(device.getPhotoName())) {
				String dir = request.getServletContext().getRealPath("/");
				File file = new File(dir,device.getPhotoName());
				deviceService.addDevice(device,file);
			}else {
				try {
					deviceService.addCopyDevice(device, copyId);
				}catch (Exception e) {
					modelMap.addAttribute("success", false);
					modelMap.addAttribute("msg", "复制失败");
					return modelMap;
				}
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
} 
