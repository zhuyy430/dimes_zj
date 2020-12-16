package com.digitzones.controllers;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.config.QRConfig;
import com.digitzones.model.Pager;
import com.digitzones.model.PressLight;
import com.digitzones.service.IPressLightService;
import com.digitzones.util.QREncoder;
import com.digitzones.vo.PressLightVO;
/**
 * 按灯管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/pressLight")
public class PressLightController {
	private QRConfig config ;
	private QREncoder qrEncoder = new QREncoder();
	private IPressLightService pressLightService;
	/**
	 * 添加按灯
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addPressLight.do")
	@ResponseBody
	public ModelMap addPressLight(PressLight parameter) {
		ModelMap modelMap = new ModelMap();
		PressLight parameter4Code = pressLightService.queryByProperty("code", parameter.getCode());
		if(parameter4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "按灯编码已被使用");
		}else {
			pressLightService.addObj(parameter);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除按灯
	 * @param id
	 * @return
	 */
	@RequestMapping("/deletePressLight.do")
	@ResponseBody
	public ModelMap deletePressLight(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		String[] idList = id.split(",");
		for(String i:idList){
			pressLightService.deleteObj(Long.valueOf(i));
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}

	private PressLightVO model2VO(PressLight pl) {
		if(pl == null) {
			return null;
		}
		PressLightVO vo = new PressLightVO();
		vo.setId(pl.getId());
		vo.setCode(pl.getCode());
		vo.setReason(pl.getReason());
		vo.setNote(pl.getNote());
		if(pl.getPressLightType()!=null) {
			vo.setPressLightTypeCode(pl.getPressLightType().getCode());
			vo.setPressLightTypeName(pl.getPressLightType().getName());
		}
		return vo;
	}
	
	/**
	 * 打印故障的二维码
	 * @param ids 设备id字符串
	 * @return
	 */
	@RequestMapping("/printQr.do")
	@ResponseBody
	public List<PressLightVO> printQr(String ids,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		List<PressLightVO> vos = new ArrayList<>();
		String[] idStr = ids.split(",");
		for(int i = 0 ;i<idStr.length;i++) {
			String id = idStr[i];
			PressLight e = pressLightService.queryObjById(Long.valueOf(id));
			PressLightVO vo = model2VO(e);
			vo.setQrPath(qrEncoder.generatePR(e.getCode(),dir , config.getQrPath()));
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 根据类别id查询按灯原因
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/queryAllPressLightByTypeId.do")
	@ResponseBody
	public List<PressLight> queryAllPressLightByTypeId(Long typeId){
		List<PressLight> list = pressLightService.queryAllPressLightByTypeId(typeId);
		return list;
	}
	/**
	 * 根据id查询按灯
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryPressLightById.do")
	@ResponseBody
	public PressLight queryPressLightById(Long id) {
		PressLight parameter = pressLightService.queryObjById(id);
		return parameter;
	}
	/**
	 * 分页查询按灯信息
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPressLightsByPressLightTypeId.do")
	@ResponseBody
	public ModelMap queryPressLightsByPressLightTypeId(@RequestParam(value="pressLightTypeId",required=false)Long parameterTypeId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page
	,String type) {
		Pager<Object[]> pager = null;
		if(parameterTypeId==null) {
			pager = pressLightService.queryObjs("select p from PressLight p inner join p.pressLightType pt where pt.typeName=?0", page, rows, new Object[] {type});
		}else {
			pager = pressLightService.queryObjs("select p from PressLight p inner join p.pressLightType pt  where pt.id=?0 and pt.typeName=?1", page, rows, new Object[] {parameterTypeId,type});
		}
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	@Autowired
	public void setConfig(QRConfig config) {
		this.config = config;
	}
	@Autowired
	public void setPressLightService(IPressLightService pressLightService) {
		this.pressLightService = pressLightService;
	}
	
	/**
	 * 更新按灯
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updatePressLight.do")
	@ResponseBody
	public ModelMap updatePressLight(PressLight parameter) {
		ModelMap modelMap = new ModelMap();
		pressLightService.updateObj(parameter);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 根据basicCode查询按灯信息
	 * @param basicCode
	 * @return
	 */
	@RequestMapping("/queryPressLightByBasicCode.do")
	@ResponseBody
	public List<PressLight> queryPressLightByBasicCode(String basicCode){
		return pressLightService.queryPressLightByBasicCode(basicCode);
	}
	/**
	 * 查询所有按灯信息
	 * @param basicCode
	 * @return
	 */
	@RequestMapping("/queryAllPressLight.do")
	@ResponseBody
	public List<PressLight> queryAllPressLight(){
		return pressLightService.queryAllPressLight();
	}
} 
