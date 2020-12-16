package com.digitzones.devmgr.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.NGMaintainRecord;
import com.digitzones.devmgr.service.INGMaintainRecordService;
import com.digitzones.devmgr.vo.NGMaintainRecordVO;
import com.digitzones.model.Pager;
import com.digitzones.vo.ProjectTypeVO;
@RestController
@RequestMapping("/ngMaintainRecord")
public class NGMaintainRecordController {
	@Autowired
	private INGMaintainRecordService ngMaintainRecordService;
	
	/**
	 * 分页查询设备报修
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGMaintainRecord.do")
	@ResponseBody
	public ModelMap queryNGMaintainRecord(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceRepairOrderId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<NGMaintainRecord> pager = ngMaintainRecordService.queryObjs("select ng from NGMaintainRecord ng inner join ng.deviceRepair d "
				+ "where d.id=?0", page, rows, new Object[] {deviceRepairOrderId});
		List<NGMaintainRecord> data = pager.getData();
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 分页查询设备报修
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGMaintainRecordVO.do")
	@ResponseBody
	public ModelMap queryNGMaintainRecordVO(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceRepairOrderId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<NGMaintainRecord> pager = ngMaintainRecordService.queryObjs("select ng from NGMaintainRecord ng inner join ng.deviceRepair d "
				+ "where d.id=?0", page, rows, new Object[] {deviceRepairOrderId});
		List<NGMaintainRecord> data = pager.getData();
		List<NGMaintainRecordVO> dataVO = new ArrayList<>();
		for(NGMaintainRecord d:data){
			//ProjectType parent = projectTypeService.queryByProperty("code", "ROOT"+d.getDeviceProject().getProjectType().getType());
			d.getDeviceProject().getProjectType().setParent(d.getDeviceProject().getProjectType());
			dataVO.add(model2VO(d));
		}
		modelMap.addAttribute("rows", dataVO);
		modelMap.addAttribute("total", dataVO.size());
		return modelMap;
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addMaintainProject.do")
	@ResponseBody
	public ModelMap addNGMaintainRecord(NGMaintainRecord ngMaintainRecord){
		ModelMap modelMap = new ModelMap();
		ngMaintainRecord.setCreateDate(new Date());
		ngMaintainRecord.setStatus(true);
		ngMaintainRecordService.addObj(ngMaintainRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryNGMaintainRecordById.do")
	@ResponseBody
	public NGMaintainRecordVO queryNGMaintainRecordById(Long id){
		NGMaintainRecord ngMaintainRecord = ngMaintainRecordService.queryObjById(id);
		return model2VO(ngMaintainRecord);
	}
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/updateNGMaintainRecord.do")
	@ResponseBody
	public ModelMap updateNGMaintainRecord(NGMaintainRecord ngMaintainRecord){
		ModelMap modelMap = new ModelMap();
		NGMaintainRecord ng = ngMaintainRecordService.queryObjById(ngMaintainRecord.getId());
		ng.setNote(ngMaintainRecord.getNote());
		ng.setProcessingMethod(ngMaintainRecord.getProcessingMethod());
		ng.setRemark(ngMaintainRecord.getRemark());
		ngMaintainRecordService.updateObj(ng);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/deleteNGMaintainRecord.do")
	@ResponseBody
	public ModelMap deleteNGMaintainRecord(String id){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		NGMaintainRecord ng = ngMaintainRecordService.queryObjById(Long.parseLong(id));
		if(ng.isStatus()){
			ngMaintainRecordService.deleteObj(Long.parseLong(id));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("message", "删除成功!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "该记录可编辑,不可删除!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	
	private NGMaintainRecordVO model2VO(NGMaintainRecord ngMaintainRecord) {
		if(ngMaintainRecord == null) {
			return null;
		}
		NGMaintainRecordVO vo = new NGMaintainRecordVO();
		vo.setId(ngMaintainRecord.getId());
		vo.setNote(ngMaintainRecord.getNote());
		vo.setProcessingMethod(ngMaintainRecord.getProcessingMethod()); 
		vo.setRemark(ngMaintainRecord.getRemark());
		if(ngMaintainRecord.getDeviceProject()!=null){
			vo.setCode(ngMaintainRecord.getDeviceProject().getCode());
			vo.setReason(ngMaintainRecord.getDeviceProject().getName());
			if(ngMaintainRecord.getDeviceProject()!=null){
				ProjectTypeVO PVO = new ProjectTypeVO(); 
				BeanUtils.copyProperties(ngMaintainRecord.getDeviceProject(),PVO);
				PVO.setParent(ngMaintainRecord.getDeviceProject().getProjectType());
				vo.setProjectType(PVO);
			}
		}
		if(ngMaintainRecord.getDeviceRepair()!=null){
			vo.setDeviceRepairId(ngMaintainRecord.getDeviceRepair().getId());
		}
		return vo;
	}
	
}
