package com.digitzones.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.DeviceLevel;
import com.digitzones.devmgr.service.IDeviceLevelService;
import com.digitzones.model.Pager;
/**
 * 设备等级管理控制器
 * @author zdq
 * 2019年3月15日
 */
@RestController
@RequestMapping("/deviceLevel")
public class DeviceLevelController {
	@Autowired
	private IDeviceLevelService deviceLevelService;
	/**
	 * 查询设备等级
	 * @return
	 */
	@RequestMapping("/queryTopDeviceLevels.do")
	@ResponseBody
	public List<DeviceLevel> queryTopDeviceLevels(){
		return deviceLevelService.queryTopDeviceLevels();
	}
	/**
	 * 分页查询设备等级
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceLevelsByParentId.do")
	@ResponseBody
	public ModelMap queryDeviceLevelsByParentId(@RequestParam(value="pid",required=false)Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<DeviceLevel> pager  = deviceLevelService.queryObjs("from DeviceLevel pu where pu.parent.id=?0 order by pu.code", page, rows, new Object[] {pid});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	/**
	 * 添加设备等级
	 * @param department
	 * @return
	 */
	@RequestMapping("/addDeviceLevel.do")
	@ResponseBody
	public ModelMap addDeviceLevel(DeviceLevel DeviceLevel) {
		ModelMap modelMap = new ModelMap();
		DeviceLevel DeviceLevel4Code = deviceLevelService.queryByProperty("code", DeviceLevel.getCode());
		if(DeviceLevel4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备等级编码已被使用");
		}else {
			DeviceLevel	DeviceLevel4Name = deviceLevelService.queryByProperty("name",DeviceLevel.getName());
			if(DeviceLevel4Name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "设备等级名称已被使用");
			}else {
				deviceLevelService.addObj(DeviceLevel);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}
	/**
	 * 根据id查询设备等级
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDeviceLevelById.do")
	@ResponseBody
	public DeviceLevel queryDeviceLevelById(Long id) {
		DeviceLevel DeviceLevel = deviceLevelService.queryObjById(id);
		return DeviceLevel;
	}

	/**
	 * 查询所有设备等级
	 * @return
	 */
	@RequestMapping("/queryAllDeviceLevels.do")
	@ResponseBody
	public List<DeviceLevel> queryAllDeviceLevels(){
		return deviceLevelService.queryAllDeviceLevels();
	}
	/**
	 * 更新设备等级
	 * @param department
	 * @return
	 */
	@RequestMapping("/updateDeviceLevel.do")
	@ResponseBody
	public ModelMap updateDeviceLevel(DeviceLevel DeviceLevel) {
		ModelMap modelMap = new ModelMap();
		DeviceLevel pu = deviceLevelService.queryByProperty("name", DeviceLevel.getName());
		if(pu!=null && !DeviceLevel.getId().equals(pu.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "设备等级名称已被使用");
		}else {
			deviceLevelService.updateObj(DeviceLevel);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除设备等级
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDeviceLevel.do")
	@ResponseBody
	public ModelMap deleteDeviceLevel(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}

		Long DeviceLevelId = Long.valueOf(id);

		ModelMap modelMap = new ModelMap();
		try {
			deviceLevelService.deleteObj(DeviceLevelId);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功删除!");
		} catch (RuntimeException e) {
			//e.printStackTrace();
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("message", e.getMessage());
			modelMap.addAttribute("title", "操作提示");
		}
		return modelMap;
	}
} 
