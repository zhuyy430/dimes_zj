package com.digitzones.mc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.config.WorkFlowKeyConfig;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.DeviceRepairPic;
import com.digitzones.devmgr.service.IDeviceRepairOrderPicService;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.vo.DeviceRepairVO;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceSiteService;

@RestController
@RequestMapping("/mcDeviceRepairOrder")
public class MCDeviceRepairOrderController {
	@Autowired
	private WorkFlowKeyConfig workFlowKeyConfig;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IDeviceRepairOrderPicService deviceRepairOrderPicService;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IMCUserService mcUserService;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 分页查询设备报修
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap queryDeviceRepairOrder(@RequestParam(defaultValue = "20") Integer rows,
			@RequestParam(defaultValue = "1") Integer page, HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Pager<DeviceRepair> pager = deviceRepairOrderService
				.queryObjs("from DeviceRepair dro order by dro.createDate desc", page, rows, new Object[] {});
		List<DeviceRepair> data = pager.getData();
		// data.sort((d1, d2) ->
		// d1.getSerialNumber().compareTo(d2.getSerialNumber()));
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}

	/**
	 * 根据设备编码获取报修单记录
	 * 
	 * @param deviceCode
	 * @return
	 */
	@RequestMapping("/queryDeviceRepairOrderByDeviceCode.do")
	@ResponseBody
	public List<DeviceRepair> queryDeviceRepairOrderByDeviceCode(String deviceCode) {
		DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceCode);
		if (deviceSite == null) {
			return new ArrayList<DeviceRepair>();
		}
		List<DeviceRepair> list = deviceRepairOrderService
				.queryDeviceRepairOrderByDeviceId(deviceSite.getDevice().getId());
		// list.sort((d1, d2) ->
		// d2.getCreateDate().compareTo(d1.getCreateDate()));
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping("/addDeviceRepairOrder.do")
	@ResponseBody
	public ModelMap addDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, HttpServletRequest request) {
		if (deviceRepairOrder.getSerialNumber() == null || deviceRepairOrder.getSerialNumber().equals("")) {
			deviceRepairOrder.setSerialNumber(getSerialNumber(null));
		}
		ModelMap modelMap = new ModelMap();
		String clientIp = request.getRemoteAddr();
		MCUser mcuser = mcUserService.queryMCUserByEmployeeNameAndClientIpAndsign_in(deviceRepairOrder.getInformant(),
				clientIp);

		deviceRepairOrderService.addDeviceRepairOrder(deviceRepairOrder, idList, mcuser.getUsername(), request);
		modelMap.put("businessKey", workFlowKeyConfig.getDeviceRepairflowKey());
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
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
		vo.setNgDescription(deviceRepair.getNgDescription());
		vo.setSerialNumber(deviceRepair.getSerialNumber());
		vo.setStatus(deviceRepair.getStatus());
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
		if (deviceRepair.getNgreason() != null) {
			vo.setNGId(deviceRepair.getNgreason().getId());
			vo.setNGCode(deviceRepair.getNgreason().getCode());
			vo.setNGName(deviceRepair.getNgreason().getName());
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
	public ModelMap updateDeviceRepairOrder(String id, String status, HttpServletRequest request) {
		if (id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();

		String ip = request.getRemoteAddr();
		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);

		Boolean b = null;
		try {
			b = deviceRepairOrderService.updateDeviceRepairOrderStatusById(Long.parseLong(id), status,
					mcUser.getUsername());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			modelMap.addAttribute("message",e.getMessage());
			e.printStackTrace();
		}
		if (b) {
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "操作成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		modelMap.addAttribute("statusCode", 110);
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
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

}
