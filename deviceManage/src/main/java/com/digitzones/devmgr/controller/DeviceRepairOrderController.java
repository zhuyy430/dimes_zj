package com.digitzones.devmgr.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.DeviceRepairPic;
import com.digitzones.devmgr.service.IDeviceRepairOrderPicService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.vo.DeviceRepairVO;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkflowTask;
import com.digitzones.service.IAppClientMapService;
import com.digitzones.service.IWorkflowTaskService;
import com.digitzones.util.ExcelUtil;

@RestController
@RequestMapping("/deviceRepairOrder")
public class DeviceRepairOrderController {
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IDeviceRepairOrderPicService deviceRepairOrderPicService;
	@Autowired
	private IWorkflowTaskService workflowTaskService;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	IAppClientMapService appClientMapService; 
	
	/**
	 * 分页查询设备报修
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap queryDeviceRepairOrder(@RequestParam(defaultValue = "20") Integer rows,
			@RequestParam(defaultValue = "1") Integer page, HttpServletRequest request,String condition,String maintainType,
			String maintainStatus,String employeeName,String search_from,String search_to) throws ParseException {
		ModelMap modelMap = new ModelMap();
		
		String hql ="from DeviceRepair d where 1=1";
		
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(condition)) {
			hql += "and (d.device.code like ?" + i;
			hql +=" or d.device.name like ?" + i;
			hql +=" or d.device.unitType like ?" + i + ")";
			i++;
			args.add("%" + condition + "%");
		}


		if(!StringUtils.isEmpty(maintainType)) {
			hql+=" and d.ngreason.code=?" + (i++);
			args.add(maintainType);
		}

		if(!StringUtils.isEmpty(maintainStatus)) {
			hql += " and d.status =?" + (i++);
			args.add(maintainStatus);
		}

		if(!StringUtils.isEmpty(search_from)) {
			hql += " and d.createDate >=?" + (i++);
			args.add(format.parse(search_from));
		}

		if(!StringUtils.isEmpty(search_to)) {
			hql += " and d.createDate <=?" + (i++);
			args.add(format.parse(search_to));
		}

		if(!StringUtils.isEmpty(employeeName)) {
			hql += " and d.maintainName like ?" + (i++);
			args.add("%" + employeeName + "%");
		}
		
		hql+=" order by d.createDate desc";
		Pager<DeviceRepair> pager = deviceRepairOrderService
				.queryObjs(hql, page, rows, args.toArray());
		List<DeviceRepair> data = pager.getData();
		// data.sort((d1, d2) ->
		// d1.getSerialNumber().compareTo(d2.getSerialNumber()));
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 根据设备id查找维修记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceRepairOrderByDeviceId.do")
	@ResponseBody
	public ModelMap queryDeviceRepairOrderByDeviceId(Long deviceId,@RequestParam(defaultValue = "20") Integer rows,
			@RequestParam(defaultValue = "1") Integer page, HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Pager<DeviceRepair> pager = deviceRepairOrderService
				.queryObjs("from DeviceRepair dro where dro.device.id=?0 order by dro.createDate desc", page, rows, new Object[] {deviceId});
		List<DeviceRepair> data = pager.getData();
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 根据设备id查找维修记录
	 * @return
	 */
	@RequestMapping("/exportDeviceRepairOrderByDeviceId.do")
	@ResponseBody
	public void exportDeviceRepairOrderByDeviceId(Long deviceId,HttpServletResponse response) {
		List<DeviceRepair> list = deviceRepairOrderService.queryDeviceRepairOrderByDeviceId(deviceId);
		String[] titles = {
				"单号","报修时间","报修人","故障类型","故障描述","维修人员","维修状态","是否关闭","完成时间","确认时间","确认人"
		};
		List<String[]> data = new ArrayList<>();
		if(!CollectionUtils.isEmpty(list)) {
			for(DeviceRepair dr : list) {
				String[] deviceRepairRecord = new String[11];
				deviceRepairRecord[0] = dr.getSerialNumber();
				deviceRepairRecord[1] = dr.getCreateDate()!=null?sdf.format(dr.getCreateDate()):"";
				deviceRepairRecord[2] = dr.getInformant();
				deviceRepairRecord[3] = dr.getNgreason()!=null?dr.getNgreason().getName():"";
				deviceRepairRecord[4] = dr.getNgDescription();
				deviceRepairRecord[5] = dr.getMaintainName();
				String status = "";
				if(dr.getStatus()!=null) {
					switch(dr.getStatus()) {
					case Constant.DeviceRepairStatus.WAITINGASSIGN:{
						status = "等待派单";
						break;
					}
					case Constant.DeviceRepairStatus.WAITINCOMFIRM:{
						status = "等待接单";
						break;
					}
					case Constant.DeviceRepairStatus.MAINTAINING:{
						status = "正在维修";
						break;
					}
					case Constant.DeviceRepairStatus.WORKSHOPCOMFIRM:{
						status = "已确认";
						break;
					}
					case Constant.DeviceRepairStatus.MAINTAINCOMPLETE:{
						status = "维修完成";
						break;
					}
					case Constant.DeviceRepairStatus.WAITWORKSHOPCOMFIRM:{
						status = "等待车间确认";
						break;
					}
					case Constant.DeviceRepairStatus.FAIL_SAFEOPERATION:{
						status = "带病运行";
						break;
					}
					}
					deviceRepairRecord[6] = status;
					deviceRepairRecord[7] = dr.getClosed()!=null?(dr.getClosed()?"已关闭":"未关闭"):"";
					deviceRepairRecord[8] = dr.getSubmitConfirmDate()!=null?sdf.format(dr.getSubmitConfirmDate()):"";
					deviceRepairRecord[9] = dr.getCompleteDate()!=null?sdf.format(dr.getCompleteDate()):"";
					deviceRepairRecord[10] = dr.getConfirmName();
				}
				
				data.add(deviceRepairRecord);
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		HSSFWorkbook workBook = ExcelUtil.getHSSFWorkbook("维修记录", titles, data);
		try {
			this.setResponseHeader(response, "设备维修记录"+sdf.format(new Date())+".xls");
			OutputStream os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//发送响应流方法
		public void setResponseHeader(HttpServletResponse response, String fileName) {
			try {
				try {
					fileName = new String(fileName.getBytes(),"ISO8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				response.setContentType("application/octet-stream;charset=ISO8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
				response.addHeader("Pargam", "no-cache");
				response.addHeader("Cache-Control", "no-cache");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	/**
	 * 查询所有设备报修
	 * 
	 * @return
	 */
	@RequestMapping("/queryAllDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap queryAllDeviceRepairOrder() {
		ModelMap modelMap = new ModelMap();
		List<DeviceRepair> list = deviceRepairOrderService.queryAllDeviceRepair();
		// data.sort((d1, d2) ->
		// d1.getSerialNumber().compareTo(d2.getSerialNumber()));
		modelMap.addAttribute("rows", list);
		modelMap.addAttribute("total", list.size());
		return modelMap;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping("/addDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap addDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, Principal principal,
			HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		String username = principal.getName();
		Boolean b=false;
		DeviceRepair order = deviceRepairOrderService.queryByProperty("serialNumber", deviceRepairOrder.getSerialNumber());
		if(null!=order){
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "报修单已存在!");
			return modelMap;
		}
		try{
		 b= deviceRepairOrderService.addDeviceRepairOrder(deviceRepairOrder, idList, username, request);
		}catch(Exception e){
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("statusCode", 110);
			modelMap.addAttribute("title", "操作提示!");
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", e.getMessage());
			return modelMap;
		}
		if(b){
			modelMap.put("businessKey", workFlowKeyConfig.getDeviceRepairflowKey());
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "新增成功!");
			return modelMap;
		}else{
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "新增失败!");
			return modelMap;
		}
	}

	/**
	 * 根据ID查询
	 * 
	 * @return
	 */
	@RequestMapping("/queryDeviceRepairOrderById.do")
	@ResponseBody
	public DeviceRepairVO queryDeviceRepairOrderById(Long id, HttpServletRequest request) {
		DeviceRepair dro = deviceRepairOrderService.queryObjById(id);
		List<DeviceRepairPic> picList = deviceRepairOrderPicService.queryListByProperty(id);
		return model2VO(dro, picList, request);
	}

	private DeviceRepairVO model2VO(DeviceRepair deviceRepair, List<DeviceRepairPic> picList,
			HttpServletRequest request) {
		if (deviceRepair == null) {
			return null;
		}
		DeviceRepairVO vo = new DeviceRepairVO();
		vo.setId(deviceRepair.getId());
		vo.setCreateDate(sdf.format(deviceRepair.getCreateDate()));
		vo.setInformant(deviceRepair.getInformant());
		vo.setInformantCode(deviceRepair.getInformantCode());
		vo.setNgDescription(deviceRepair.getNgDescription());
		vo.setSerialNumber(deviceRepair.getSerialNumber());
		vo.setStatus(deviceRepair.getStatus());
		vo.setMaintainName(deviceRepair.getMaintainName());;
		vo.setMaintainCode(deviceRepair.getMaintainCode());;
		if (deviceRepair.getDevice() != null) {
			vo.setDeviceId(deviceRepair.getDevice().getId());
			vo.setDeviceCode(deviceRepair.getDevice().getCode());
			vo.setDeviceName(deviceRepair.getDevice().getName());
			vo.setDeviceInstallPosition(deviceRepair.getDevice().getInstallPosition());
			vo.setDeviceUnitType(deviceRepair.getDevice().getUnitType());
			if (deviceRepair.getDevice().getProjectType() != null) {
				vo.setDeviceTypeId(deviceRepair.getDevice().getProjectType().getId());
				vo.setDeviceTypeCode(deviceRepair.getDevice().getProjectType().getCode());
				vo.setDeviceTypeName(deviceRepair.getDevice().getProjectType().getName());
			}
			if (deviceRepair.getDevice().getProductionUnit() != null) {
				vo.setProductionUnitId(deviceRepair.getDevice().getProductionUnit().getId());
				vo.setProductionUnitCode(deviceRepair.getDevice().getProductionUnit().getCode());
				vo.setProductionUnitName(deviceRepair.getDevice().getProductionUnit().getName());
			}
		}
		if (deviceRepair.getNgreason()!= null) {
			vo.setNGId(deviceRepair.getNgreason().getId());
			vo.setNGCode(deviceRepair.getNgreason().getCode());
			vo.setNGName(deviceRepair.getNgreason().getName());
			vo.setPressLight(deviceRepair.getNgreason().getName());
			vo.setPressLightId(deviceRepair.getNgreason().getId());
		}
		if (picList != null && !picList.isEmpty()) {
			List<String> picNames = new ArrayList<>();
			for (DeviceRepairPic pic : picList) {
				if (pic.getPic() != null) {
					String dir = request.getServletContext().getRealPath("/");
					InputStream is;
					try {
						is = pic.getPic().getBinaryStream();
						File out = new File(dir, pic.getPicName());
						picNames.add(pic.getPicName());
						FileCopyUtils.copy(is, new FileOutputStream(out));
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			vo.setPicName(picNames);
		}
		return vo;
	}

	/**
	 * 编辑
	 * 
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrder(DeviceRepair deviceRepairOrder, Principal principal) {
		ModelMap modelMap = new ModelMap();
		DeviceRepair dro = deviceRepairOrderService.queryObjById(deviceRepairOrder.getId());
		// dro.setInformant(principal.getName());
		dro.setDevice(deviceRepairOrder.getDevice());
		dro.setCreateDate(deviceRepairOrder.getCreateDate());
		dro.setNgDescription(deviceRepairOrder.getNgDescription());
		dro.setNgreason(deviceRepairOrder.getNgreason());
		deviceRepairOrderService.updateObj(dro);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}

	/**
	 * 车间确认或维修完成(接收报修单)
	 * 
	 * @return
	 */
	@RequestMapping("/updateDeviceRepairOrderStatusById.do")
	@ResponseBody
	public ModelMap updateDeviceRepairOrderStatusById(String id, String status, Principal principal) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();

		String username = principal.getName();
		
		
		Boolean b = false;
		try {
			b = deviceRepairOrderService.updateDeviceRepairOrderStatusById(Long.parseLong(id), status, username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("statusCode", 110);
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		if(b){
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "操作成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		modelMap.addAttribute("message", "操作失败!");
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 返修
	 */
	@RequestMapping("/updateDeviceRepairForConfirmAndReword.do")
	@ResponseBody
	public ModelMap updateDeviceRepairForConfirmAndReword(String id,String status,Principal principal) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		DeviceRepair deviceRepair=deviceRepairOrderService.queryObjById(Long.parseLong(id));
		if(!deviceRepair.getStatus().equals(Constant.DeviceRepairStatus.WAITWORKSHOPCOMFIRM)){
			modelMap.addAttribute("message", "该记录不可返修!");
			modelMap.addAttribute("success", false);
			return modelMap;
		}
		String username = principal.getName();
		Boolean b = deviceRepairOrderService.updateDeviceRepairForConfirmAndReword(Long.parseLong(id), status, username);
		if(b){
			modelMap.addAttribute("message", "操作成功");
			modelMap.addAttribute("success", true);
			return modelMap;
		}else{
			modelMap.addAttribute("message", "操作失败");
			modelMap.addAttribute("success", false);
			return modelMap;
		}
	}
	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/deleteDeviceRepairOrderById.do")
	@ResponseBody
	public ModelMap deleteDeviceRepairOrderById(String id) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		deviceRepairOrderService.deleteObj(Long.parseLong(id));

		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "删除成功!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 流水号
	 * 
	 * @return
	 */
	@RequestMapping("/queryDeviceRepairOrderSerialNumber.do")
	@ResponseBody
	public ModelMap queryDeviceRepairOrderSerialNumber(HttpSession session) {
		ModelMap modelMap = new ModelMap();
		String serialNumber = (String) session.getAttribute("serialNumber");
		serialNumber = getSerialNumber(serialNumber);

		if (serialNumber == null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "流水号获取错误");
			return modelMap;
		}
		session.setAttribute("serialNumber", serialNumber);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("serialNumber", serialNumber);
		return modelMap;
	}

	public String getSerialNumber(String serialNumber) {
		if (serialNumber == null) {
			serialNumber = dateFormat.format(new Date()) + "0001";
		} else {
			// 截取initCode
			String beforePart = serialNumber.substring(0, serialNumber.length() - 4);
			String endPart = serialNumber.substring(serialNumber.length() - 4);
			String barCode = "";
			int no = 0;
			try {
				if (org.apache.commons.lang3.StringUtils.isNumeric(endPart)) {
					no = Integer.parseInt(endPart);
				} else {
					throw new RuntimeException("开始条码的后四位不是数字：" + serialNumber);
				}
				no += 1;
				if (no / 1000 > 0) {
					barCode = beforePart + no;
				} else if (no / 100 > 0) {
					barCode = beforePart + "0" + no;
				} else if (no / 10 > 0) {
					barCode = beforePart + "00" + no;
				} else {
					barCode = beforePart + "000" + no;
				}
				DeviceRepair dro = deviceRepairOrderService.queryByProperty("serialNumber", barCode);
				if (dro != null) {
					barCode = getSerialNumber(barCode);
				}
				return barCode;
			} catch (RuntimeException e) {
				return null;
			}
		}
		DeviceRepair dro = deviceRepairOrderService.queryByProperty("serialNumber", serialNumber);
		if (dro != null) {
			serialNumber = getSerialNumber(serialNumber);
		}
		return serialNumber;
	}

	/**
	 * 根据业务键查找流程信息
	 * 
	 * @param businessKey
	 *            流程类型 ng:不合格品 lostTime:损时 pressLight:按灯 deviceRepair设备报修单
	 * @return
	 */
	@RequestMapping("/queryWorkflow.do")
	@ResponseBody
	public ModelMap queryWorkflow(String businessKey) {
		ModelMap modelMap = new ModelMap();
		businessKey += ":" + DeviceRepair.class.getName();
		List<Map<String, Object>> data = new ArrayList<>();

		List<WorkflowTask> taskList = workflowTaskService.queryByBusinessKey(businessKey);

		if (taskList != null && taskList.size() > 0) {
			for (WorkflowTask task : taskList) {
				Map<String, Object> map = new HashMap<>();
				Date date = task.getCreateDate();
				if (date != null) {
					sdf.applyPattern("yyyy");
					map.put("year", sdf.format(date));
				} else {
					map.put("year", "");
				}

				List<Map<String, Object>> list = new ArrayList<>();
				Map<String, Object> m = new HashMap<>();
				sdf.applyPattern("MM-dd HH:mm");
				m.put("date", sdf.format(date));
				m.put("intro", task.getName());
				/*
				 * if(task.getEndTime()!=null) { m.put("highlight",
				 * "highlight"); }
				 */
				m.put("version", "测试");
				list.add(m);
				map.put("list", list);

				List<String> more = new ArrayList<>();
				more.add(task.getDescription());
				m.put("more", more);
				data.add(map);
			}
		}
		modelMap.addAttribute("data", data);
		return modelMap;
	}
	
	/**
	 * 根据单号查询是否是带病运行单
	 */
	@RequestMapping("/queryofsoNoBySerialNumber.do")
	@ResponseBody
	public ModelMap queryofsoNoBySerialNumber(String serialNumber) {
		ModelMap modelMap=new ModelMap();
		DeviceRepair d1=deviceRepairOrderService.queryByProperty("serialNumber", serialNumber);
		if(d1!=null&&d1.getFailSafeOperation()&&!d1.getClosed()) {
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("deviceRepair",d1);
			return modelMap;
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "无此带病运行单，请填写正确的原带病运行单号");
			return modelMap;
		}
		
	}
}
