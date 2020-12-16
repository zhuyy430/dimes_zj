package com.digitzones.devmgr.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.devmgr.service.IDeviceProjectService;
import com.digitzones.devmgr.vo.DeviceProjectRecordVO;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceService;

@Controller
@RequestMapping("/deviceProjectRecord")
public class DeviceProjectRecordController {
	@Autowired
	private IDeviceProjectRecordService deviceProjectRecordService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IDeviceProjectService deviceProjectService;
	
	/**
	 * 通过设备id和项目type查找关联项目
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceProjectRecordByDeviceIdAndType.do")
	@ResponseBody
	public ModelMap queryDeviceProjectRecordByDeviceIdAndType(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceId,String type){
		String hql="from DeviceProjectRecord dpr where dpr.device.id=?0 and dpr.type=?1 order by dpr.classesCode";
		Pager<DeviceProjectRecord> pager = deviceProjectRecordService.queryObjs(hql, page, rows,new Object[]{deviceId,type});
		ModelMap modelMap = new ModelMap();
		List<DeviceProjectRecordVO> list = new ArrayList<>();
		for(DeviceProjectRecord d:pager.getData()){
			list.add(model2vo(d));
		}
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", list);
		return modelMap;
	}
	private DeviceProjectRecordVO model2vo(DeviceProjectRecord d) {
		if(d == null) {
			return null;
		}
		DeviceProjectRecordVO vo = new DeviceProjectRecordVO();
		vo.setId(d.getId());
		vo.setName(d.getName());
		vo.setNote(d.getNote());
		vo.setCode(d.getCode());
		vo.setStandard(d.getStandard());
		vo.setMethod(d.getMethod());
		vo.setFrequency(d.getFrequency());
		vo.setDevice(d.getDevice());
		vo.setRecordTypeCode(d.getRecordTypeCode());
		vo.setRecordTypeName(d.getRecordTypeName());
		vo.setUpperLimit(d.getUpperLimit());
		vo.setLowerLimit(d.getLowerLimit());
		vo.setClassesCode(d.getClassesCode());
		vo.setClassesName(d.getClassesName());
		if(d.getDevice()!=null) {
			vo.setDeviceId(d.getDevice().getId());
		}
		return vo;
	}
	
	/**
	 * 新增设备和项目的关联	
	 */
	@RequestMapping("/addDeviceProjectRecord.do")
	@ResponseBody
	public ModelMap addDeviceProjectRecord(DeviceProjectRecord deviceProjectRecord) {
		ModelMap modelMap = new ModelMap();
		if(deviceProjectRecord!=null&&deviceProjectRecord.getDevice()!=null) {
			deviceProjectRecord.setDevice(deviceService.queryObjById(deviceProjectRecord.getDevice().getId()));
			deviceProjectRecord.setCreateTime(new Date());
		}
		deviceProjectRecordService.addObj(deviceProjectRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功！");
		return modelMap;
	}
	/**
	 * 新增设备和项目的关联(多选)
	 * @param deviceId 设备id
	 * @param projectsIds 设备(保养，润滑等)记录id，逗号间隔
	 * @param recordTypeCode 记录类型编码
	 * @param recordTypeName 记录所属类型名称
	 * @return
	 */
	@RequestMapping("/addDeviceProjectRecordWithMore.do")
	@ResponseBody
	public ModelMap addDeviceProjectRecordWithMore(Long deviceId,String projectsIds,
			String recordTypeCode,String recordTypeName,String type,String classesCode,String classesName) {
		ModelMap modelMap = new ModelMap();
		if(projectsIds!=null) {
			if(projectsIds.contains("[")) {
				projectsIds = projectsIds.replace("[", "");
			}
			if(projectsIds.contains("]")) {
				projectsIds = projectsIds.replace("]", "");
			}

			String[] idArray = projectsIds.split(",");
			for(int i = 0;i<idArray.length;i++) {
				DeviceProject deviceProject = deviceProjectService.queryObjById(Long.valueOf(idArray[i]));

				DeviceProjectRecord dpr = new DeviceProjectRecord();
				dpr.setDevice(deviceService.queryObjById(deviceId));
				dpr.setCreateTime(new Date());
				dpr.setCode(deviceProject.getCode());
				dpr.setName(deviceProject.getName());
				dpr.setMethod(deviceProject.getMethod());
				dpr.setNote(deviceProject.getNote());
				dpr.setRecordTypeCode(recordTypeCode);
				dpr.setRecordTypeName(recordTypeName);
				dpr.setClassesCode(classesCode);
				dpr.setClassesName(classesName);
				dpr.setType(type);
				if(deviceProject.getFrequency()!=null) {
					dpr.setFrequency(deviceProject.getFrequency().toString());
				}
				dpr.setStandard(deviceProject.getStandard());
				dpr.setType(deviceProject.getType());
				deviceProjectRecordService.addObj(dpr);
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return  modelMap;
	}
	/**
	 * 编辑设备和项目的关联	
	 */
	@RequestMapping("/updateDeviceProjectRecord.do")
	@ResponseBody
	public ModelMap updateDeviceProjectRecord(DeviceProjectRecord deviceProjectRecord) {
		ModelMap modelMap = new ModelMap();
		DeviceProjectRecord dpr=deviceProjectRecordService.queryObjById(deviceProjectRecord.getId());
		dpr.setDevice(deviceService.queryObjById(deviceProjectRecord.getDevice().getId()));
		dpr.setName(deviceProjectRecord.getName());
		dpr.setMethod(deviceProjectRecord.getMethod());
		dpr.setUpperLimit(deviceProjectRecord.getUpperLimit());
		dpr.setLowerLimit(deviceProjectRecord.getLowerLimit());
		dpr.setFrequency(deviceProjectRecord.getFrequency());
		dpr.setStandard(deviceProjectRecord.getStandard());
		deviceProjectRecordService.updateObj(dpr);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功！");
		return modelMap;
	}
	/**
	 * 删除设备和项目的关联
	 */
	@RequestMapping("/deleteDeviceProjectRecord.do")
	@ResponseBody
	public ModelMap deleteDeviceProjectRecord(String id) {
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		deviceProjectRecordService.deleteObj(Long.parseLong(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("msg", "成功删除!");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 通过id查找设备项目记录
	 */
	@RequestMapping("/queryDeviceProjectRecordById.do")
	@ResponseBody
	public DeviceProjectRecord queryDeviceProjectRecordById(Long id) {
		DeviceProjectRecord deviceProjectRecord=deviceProjectRecordService.queryObjById(id);
		return deviceProjectRecord;
	}
}
