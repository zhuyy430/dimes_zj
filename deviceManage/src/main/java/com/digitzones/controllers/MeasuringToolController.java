package com.digitzones.controllers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 装备管理控制器
 * @author zdq
 * 2018年6月7日
 */
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.config.QRConfig;
import com.digitzones.model.Equipment;
import com.digitzones.model.Pager;
import com.digitzones.service.IMeasuringToolService;
import com.digitzones.util.QREncoder;
import com.digitzones.vo.EquipmentVO;
@Controller
@RequestMapping("/measuringTool")
public class MeasuringToolController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private QRConfig config ;
	@Autowired
	public void setConfig(QRConfig config) {
		this.config = config;
	}
	private QREncoder qrEncoder = new QREncoder();
	private IMeasuringToolService measuringToolService;
	@Autowired
	public void setMeasuringToolService(IMeasuringToolService measuringToolService) {
		this.measuringToolService = measuringToolService;
	}
	/**
	 * 分页查询参数信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentsByEquipmentTypeId.do")
	@ResponseBody 
	public ModelMap queryEquipmentsByEquipmentTypeId(@RequestParam(value="measuringToolTypeId",required=false)Long measuringToolTypeId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request) {
		Pager<Equipment> pager = null;
		pager = measuringToolService.queryObjs("select p from Equipment p inner join p.equipmentType pt  where pt.id=?0", page, rows, new Object[] {measuringToolTypeId});
		ModelMap mm = new ModelMap();
		List<EquipmentVO> vos = new ArrayList<>();
		if(pager.getData()!=null&&pager.getData().size()>0) {
			for(Equipment e:pager.getData()) {
				vos.add(model2VO(e, request));
			}
		}
		mm.addAttribute("rows",vos);
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}


	/**
	 * 添加参数
	 * @param measuringTool
	 * @return
	 */
	@RequestMapping("/addEquipment.do")
	@ResponseBody
	public ModelMap addEquipment(Equipment measuringTool,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Equipment measuringTool4Code = measuringToolService.queryByProperty("code", measuringTool.getCode());
		if(measuringTool4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "量具编码已被使用");
		}else {
			if(measuringTool.getPicName()!=null &&!"".equals(measuringTool.getPicName())) {
				String dir = request.getServletContext().getRealPath("/");
				File file = new File(dir,measuringTool.getPicName());
				measuringToolService.addMeasuringTool(measuringTool, file);
			}else {
				measuringToolService.addObj(measuringTool);
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	@RequestMapping("/upload.do")
	@ResponseBody
	public ModelMap upload(Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"console/measuringToolImgs/";
		String realName = file.getSubmittedFileName();
		ModelMap modelMap = new ModelMap();
		String fileName = new Date().getTime()+ realName.substring(realName.lastIndexOf("."));
		InputStream is;
		try {
			is = file.getInputStream();
			File out = new File(dir,fileName);
			FileCopyUtils.copy(is, new FileOutputStream(out));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "文件上传成功！");
			modelMap.addAttribute("filePath", "console/measuringToolImgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelMap;
	}
	/**
	 * 根据id查询参数
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryEquipmentById.do")
	@ResponseBody
	public EquipmentVO queryEquipmentById(Long id,HttpServletRequest request) {
		Equipment measuringTool = measuringToolService.queryObjById(id);
		return model2VO(measuringTool, request);
	}
	/**
	 * 更新参数信息
	 * @param measuringTool
	 * @return
	 */
	@RequestMapping("/updateEquipment.do")
	@ResponseBody
	public ModelMap updateEquipment(Equipment measuringTool,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		if(measuringTool.getPicName()!=null &&!"".equals(measuringTool.getPicName())) {
			String dir = request.getServletContext().getRealPath("/");
			File file = new File(dir,measuringTool.getPicName());
			measuringToolService.updateMeasuringTool(measuringTool, file);
		}else {
			measuringToolService.updateObj(measuringTool);
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 根据id删除参数信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEquipment.do")
	@ResponseBody
	public ModelMap deleteEquipment(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			measuringToolService.deleteMeasuringTools(id.split(","));
		}catch (RuntimeException e) {
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "成功失败!");
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该参数
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledEquipment.do")
	@ResponseBody
	public ModelMap disabledEquipment(String id) {
		ModelMap modelMap = new ModelMap();
		if(id==null || "".equals(id.trim())) {
			measuringToolService.disableMeasuringTools(id.split(","));
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("message", "请选择要停用的模具!!");
			modelMap.addAttribute("title", "操作提示!");
			return modelMap;
		}
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		measuringToolService.disableMeasuringTools(id.split(","));
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 根据工序id查询参数 信息
	 * @param processId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryEquipmentByProcessId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryEquipmentByProcessId(Long processId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		String hql = "select ds from ProcessesEquipmentMapping pdm inner join  pdm.measuringTools ds  inner join pdm.processes p where p.id=?0";
		Pager<Equipment> pager = measuringToolService.queryObjs(hql, page, rows, new Object[] {processId});
		List<EquipmentVO> vos = new ArrayList<>();
		if(pager.getData()!=null&&pager.getData().size()>0) {
			for(Equipment e:pager.getData()) {
				vos.add(model2VO(e, request));
			}
		}
		modelMap.addAttribute("rows",vos);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查询所有的装备
	 * @return
	 */
	@RequestMapping("/queryAllMeasuringTools.do")
	@ResponseBody
	public List<Equipment> queryAllMeasuringTools(String q){
		if(q==null) {
			return measuringToolService.queryAllMeasuringTools();
		}else {
			return measuringToolService.queryMeasuringToolsByCodeOrNameOrUnity(q);
		}
	}
	/**
	 * 查询所有未停用的量具
	 */
	@RequestMapping("/queryAllMeasuringToolsByNotStopUse.do")
	@ResponseBody
	public List<Equipment> queryAllMeasuringToolsByNotStopUse(){
			return measuringToolService.queryAllMeasuringToolsByNotStopUse();
	}
	/**
	 * 打印装备的二维码
	 * @param ids 设备id字符串
	 * @return
	 */
	@RequestMapping("/printQr.do")
	@ResponseBody
	public List<EquipmentVO> printQr(String ids,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		List<EquipmentVO> vos = new ArrayList<>();
		String[] idStr = ids.split(",");
		for(int i = 0 ;i<idStr.length;i++) {
			String id = idStr[i];
			Equipment e = measuringToolService.queryObjById(Long.valueOf(id));
			EquipmentVO vo = model2VO(e,request);
			vo.setQrPath(qrEncoder.generatePR(e.getCode(),dir , config.getQrPath()));
			vos.add(vo);
		}
		return vos;
	}

	private EquipmentVO model2VO(Equipment e,HttpServletRequest request) {
		if(e==null) {
			return null;
		}
		EquipmentVO vo = new EquipmentVO();
		vo.setId(e.getId());
		vo.setCode(e.getCode());
		vo.setBaseCode(e.getBaseCode());
		vo.setCumulation(e.getCumulation());
		vo.setEquipmentType(e.getEquipmentType());
		vo.setMeasurementDifference(e.getMeasurementDifference());
		vo.setStatus(e.getStatus());
		vo.setNote(e.getNote());
		vo.setWarrantyPeriod(e.getWarrantyPeriod());
		if(e.getPic()!=null) {
			String dir = request.getServletContext().getRealPath("/");
			InputStream is;
			try {
				is = e.getPic().getBinaryStream();
				File out = new File(dir,e.getPicName());
				FileCopyUtils.copy(is, new FileOutputStream(out));
				vo.setPicName(e.getPicName());
			} catch (SQLException | IOException e1) {
				e1.printStackTrace();
			}
		}
		return vo;
	}
}
