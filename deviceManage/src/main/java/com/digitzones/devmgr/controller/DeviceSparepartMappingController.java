package com.digitzones.devmgr.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.IDeviceSparepartMappingService;
import com.digitzones.devmgr.service.ISparepartService;
import com.digitzones.model.Device;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceService;
@RestController
@RequestMapping("deviceSparepartMapping")
public class DeviceSparepartMappingController {
	@Autowired
	private IDeviceSparepartMappingService deviceSparepartMappingService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private ISparepartService sparepartService;
	/**
	 * 根据备品备件id查找设备和备品备件关联实体
	 * @param relatedId  备品备件id
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceSparepartMappingBySparepartCode.do")
	public ModelMap queryDeviceSparepartMappingBySparepartCode(String sparepartCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap mm = new ModelMap();
		String hql = "from DeviceSparepartMapping dsm inner join fetch dsm.device d where dsm.sparepart.code=?0";
		Pager<DeviceSparepartMapping> pager = deviceSparepartMappingService.queryObjs(hql, page, rows, new Object[] {sparepartCode});
		mm.addAttribute("total",pager.getTotalCount());
		mm.addAttribute("rows", pager.getData());
		return mm;
	}
	/**
	 * 根据设备id查找设备和备品备件关联实体
	 * @param relatedId  备品备件id
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceSparepartMappingByDeviceCode.do")
	public ModelMap queryDeviceSparepartMappingByDeviceCode(String deviceCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap mm = new ModelMap();
		String hql = "from DeviceSparepartMapping dsm inner join fetch dsm.sparepart d where dsm.device.code=?0";
		Pager<DeviceSparepartMapping> pager = deviceSparepartMappingService.queryObjs(hql, page, rows, new Object[] {deviceCode});
		mm.addAttribute("total",pager.getTotalCount());
		mm.addAttribute("rows", pager.getData());
		return mm;
	}
	/**
	 * 查找该备品备件所属设备之外的设备信息
	 * @param sparepartId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryOtherDevices.do")
	public ModelMap queryOtherDevices(String sparepartCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		String hql = "from Device d where id not in (select dsm.device.id from DeviceSparepartMapping dsm "
				+ " where dsm.sparepart.code=?0)";
		@SuppressWarnings("unchecked")
		Pager<Device> pager = deviceService.queryObjs(hql, page, rows, new Object[] {sparepartCode});
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 查找不属于该设备的备品备件信息信息
	 * @param sparepartId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryOtherSpareparts.do")
	public ModelMap queryOtherSpareparts(String deviceId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String sparepartCode,String sparepartName,String sparepartUnitType) {
		String hql = "from Sparepart s where code not in (select dsm.sparepart.code from DeviceSparepartMapping dsm "
				+ " where dsm.device.id=?0) ";
		
		List<Object> args = new ArrayList<>();
		args.add(Long.parseLong(deviceId));
		int i = 1;
		if(!StringUtils.isEmpty(sparepartCode)) {
			hql += " and s.code like ?" + (i++);
			args.add("%" + sparepartCode.trim() + "%");
		}
		if(!StringUtils.isEmpty(sparepartName)) {
			hql += " and s.name like ?" + (i++);
			args.add("%" + sparepartName.trim() + "%");
		}
		if(!StringUtils.isEmpty(sparepartUnitType)) {
			hql += " and s.unitType like ?" + (i++);
			args.add("%" + sparepartUnitType.trim() + "%");
		}
		
		@SuppressWarnings("unchecked")
		Pager<Sparepart> pager = sparepartService.queryObjs(hql, page, rows, args.toArray());
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 为备品备件添加所属设备
	 * @param sparepartId
	 * @param deviceIds
	 * @return
	 */
	@RequestMapping("/addDevices4Sparepart.do")
	@ResponseBody
	public ModelMap addDevices4Sparepart(Long sparepartId,String deviceIds) {
		ModelMap modelMap = new ModelMap();
		if(deviceIds!=null) {
			Sparepart sparepart = sparepartService.queryObjById(sparepartId);
			if(deviceIds.contains("[")) {
				deviceIds = deviceIds.replace("[", "");
			}
			if(deviceIds.contains("]")) {
				deviceIds = deviceIds.replace("]", "");
			}
			String[] idArray = deviceIds.split(",");
			deviceSparepartMappingService.addDevices4Sparepart(sparepart, idArray);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","操作完成!");
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 为设备添加备品备件
	 * @param sparepartId
	 * @param deviceIds
	 * @return
	 */
	@RequestMapping("/addSparepart4Devices.do")
	@ResponseBody
	public ModelMap addSparepart4Devices(Long deviceId,String sparepartIds) {
		ModelMap modelMap = new ModelMap();
		if(sparepartIds!=null) {
			Device device = deviceService.queryObjById(deviceId);
			if(sparepartIds.contains("[")) {
				sparepartIds = sparepartIds.replace("[", "");
			}
			if(sparepartIds.contains("]")) {
				sparepartIds = sparepartIds.replace("]", "");
			}
			String[] idArray = sparepartIds.split(",");
			deviceSparepartMappingService.addSpareparts4Device(device, idArray);
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","操作完成!");
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 删除所属设备
	 * @return
	 */
	@RequestMapping("/deleteDeviceFromSparepart.do")
	public ModelMap deleteDeviceFromSparepart(String id) {
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		deviceSparepartMappingService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("message", "删除成功！");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
}
