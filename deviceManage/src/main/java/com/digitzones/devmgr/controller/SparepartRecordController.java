package com.digitzones.devmgr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.SparepartRecord;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.ISparepartRecordService;
import com.digitzones.devmgr.vo.SparepartRecordVO;
import com.digitzones.model.Pager;
@RestController
@RequestMapping("/sparepartRecord")
public class SparepartRecordController {
	@Autowired
	private ISparepartRecordService sparepartRecordService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	
	/**
	 * 分页查询设备报修
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySparepartRecord.do")
	@ResponseBody
	public ModelMap querySparepartRecord(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceRepairOrderId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<SparepartRecord> pager = sparepartRecordService.queryObjs("select s from SparepartRecord s inner join s.deviceRepair d "
				+ "where d.id=?0", page, rows, new Object[] {deviceRepairOrderId});
		List<SparepartRecord> data = pager.getData();
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addSparepartRecord.do")
	@ResponseBody
	public ModelMap addSparepartRecord(Long deviceRepairId,String sparepartIds){
		ModelMap modelMap = new ModelMap();
		
		if(!StringUtils.isEmpty(sparepartIds)) {
			if(sparepartIds.contains("[")) {
				sparepartIds = sparepartIds.replace("[", "");
			}
			if(sparepartIds.contains("]")) {
				sparepartIds = sparepartIds.replace("]", "");
			}
		}
		sparepartRecordService.addSparepartRecords(deviceRepairId,sparepartIds);
		/*
		Long sparepartId = sparepartRecord.getSparepart().getId();
		Long deviceRepirId = sparepartRecord.getDeviceRepair().getId();
		DeviceRepair deviceRepaer = deviceRepairOrderService.queryObjById(deviceRepirId);
		DeviceSparepartMapping dsMapping = deviceSparepartMappingService.queryBySparePartIDAndDeviceId(sparepartId, deviceRepaer.getDevice().getId());
		if(dsMapping!=null){
			dsMapping.setLastUseDate(new Date());
			deviceSparepartMappingService.updateObj(dsMapping);
		}*/
		
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryaddSparepartRecordById.do")
	@ResponseBody
	public SparepartRecordVO queryaddSparepartRecordById(Long id){
		SparepartRecord sparepartRecord = sparepartRecordService.queryObjById(id);
		return model2VO(sparepartRecord);
	}
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/updateSparepartRecord.do")
	@ResponseBody
	public ModelMap updateSparepartRecord(SparepartRecord sparepartRecord){
		ModelMap modelMap = new ModelMap();
		//MaintenanceStaffRecord m = maintenanceStaffRecordService.queryObjById(maintenanceStaffRecord.getId());
		sparepartRecordService.updateObj(sparepartRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/deleteSparepartRecord.do")
	@ResponseBody
	public ModelMap deleteSparepartRecord(String id){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		sparepartRecordService.deleteObj(Long.parseLong(id));
		
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "删除成功!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	
	private SparepartRecordVO model2VO(SparepartRecord sparepartRecord) {
		if(sparepartRecord == null) {
			return null;
		}
		SparepartRecordVO vo = new SparepartRecordVO();
		vo.setId(sparepartRecord.getId());
		vo.setNote(sparepartRecord.getNote());
		vo.setQuantity(sparepartRecord.getQuantity());
		if(sparepartRecord.getSparepart()!=null){
			vo.setSparepartId(sparepartRecord.getSparepart().getId());
			vo.setSparepartCode(sparepartRecord.getSparepart().getCode());
			vo.setSparepartName(sparepartRecord.getSparepart().getName());
		}
		return vo;
	}
}
