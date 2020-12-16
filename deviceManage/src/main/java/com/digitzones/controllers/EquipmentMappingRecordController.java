package com.digitzones.controllers;
import com.digitzones.constants.Constant;
import com.digitzones.model.*;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IEquipmentDeviceSiteMappingService;
import com.digitzones.service.IEquipmentService;
import com.digitzones.service.IUserService;
import com.digitzones.vo.EquipmentDeviceSiteMappingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 设备站点和装备关联记录
 * @author zdq
 * 2018年6月28日
 */
@Controller
@RequestMapping("/equipmentMappingRecord")
public class EquipmentMappingRecordController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private IEquipmentDeviceSiteMappingService  equipmentDeviceSiteMappingService;
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEquipmentService equipmentService;
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	@Autowired
	public void setEquipmentDeviceSiteMappingService(IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService) {
		this.equipmentDeviceSiteMappingService = equipmentDeviceSiteMappingService;
	}
	
	/**
	 * 根据设备站点id查找装备关联
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentMappingRecordByDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryEquipmentMappingRecordByDeviceSiteId(Long deviceSiteId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.id=?0 "
				+ " and edsm.equipment.baseCode=?1 order by edsm.unbind, edsm.mappingDate desc";
		Pager<EquipmentDeviceSiteMapping> pager = equipmentDeviceSiteMappingService.queryObjs(hql, page, rows, new Object[] {deviceSiteId,Constant.EquipmentType.EQUIPMENT});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据设备站点id查找装备关联（搜索）
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentMappingRecordByDeviceSiteIdandSearch.do")
	@ResponseBody
	public ModelMap queryEquipmentMappingRecordByDeviceSiteIdandSearch(Long deviceSiteId,@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		
		String hql = "from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.id=?0 "
				+ " and edsm.equipment.baseCode=?1 ";
		String searchText=params.get("searchText");
	 	String searchChange=params.get("searchChange");
	 	String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		List<Object> list=new ArrayList<Object>();
		list.add(deviceSiteId);
		list.add(Constant.EquipmentType.EQUIPMENT);
		int i=list.size()-1;
		try {
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
					i++;
					hql+=" and edsm.mappingDate>=?"+i;	
					list.add(sdf.parse(beginDateStr));
			}
			if(endDateStr!=null && !"".equals(endDateStr)) {
				i++;
				hql+=" and edsm.mappingDate<=?"+i;
				list.add(sdf.parse(endDateStr));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(searchText!=null && !"".equals(searchText)&&searchChange!=null && !"".equals(searchChange)) {
			hql+=" and edsm."+searchChange+" like '%'+?"+(i+1)+"+'%'";
			list.add(searchText);
		}
		hql+=" order by edsm.mappingDate desc";
		Object[] obj =  list.toArray(new Object[1]);  
		
		
		Pager<EquipmentDeviceSiteMapping> pager = equipmentDeviceSiteMappingService.queryObjs(hql, page, rows,obj);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 添加装备关联记录
	 * @param equipmentDeviceSiteMapping
	 * @return
	 */
	@RequestMapping("/addEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap addEquipmentMappingRecord(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping,Principal principal) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = deviceSiteService.queryObjById(equipmentDeviceSiteMapping.getDeviceSite().getId());
		if(ds==null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "请选择设备站点");
			return modelMap;
		}
		
		//判断该装备是否已经被关联到设备上
		EquipmentDeviceSiteMapping edm = equipmentDeviceSiteMappingService.queryByNo(equipmentDeviceSiteMapping.getEquipment().getCode());
		if(edm!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该装备已被关联到 " + edm.getDeviceSite().getCode());
			return modelMap;
		}

		if(equipmentDeviceSiteMapping.getMappingDate()==null) {
			equipmentDeviceSiteMapping.setMappingDate(new Date());
		}

		//获取当前登录用户
		/*HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User user = userService.queryUserByUsername(principal.getName());
		if(user!=null) {
			equipmentDeviceSiteMapping.setBindUserId(user.getId());
			equipmentDeviceSiteMapping.setBindUsername(user.getUsername());
		}
		equipmentDeviceSiteMappingService.addObj(equipmentDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询装备关联记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryEquipmentMappingRecordById.do")
	@ResponseBody
	public EquipmentDeviceSiteMappingVO queryEquipmentMappingRecordById(Long id) {
		EquipmentDeviceSiteMapping plr = equipmentDeviceSiteMappingService.queryObjById(id);
		
		return model2VO(plr);
	}
	/**
	 * 将领域模型转换为值对象模型
	 * @param plr
	 * @return
	 */
	private EquipmentDeviceSiteMappingVO model2VO(EquipmentDeviceSiteMapping plr) {
		if(plr == null) {
			return null;
		}
		EquipmentDeviceSiteMappingVO vo = new EquipmentDeviceSiteMappingVO();
		vo.setId(plr.getId());
		vo.setBindUsername(plr.getBindUsername());
		vo.setBindUserId(plr.getBindUserId());
		vo.setDeviceSite(plr.getDeviceSite());
		vo.setEquipment(plr.getEquipment());
		if(plr.getEquipment()!=null) {
			vo.setEquipmentId(plr.getEquipment().getId());
			vo.setEquipmentCode(plr.getEquipment().getCode());
			vo.setEquipmentName(plr.getEquipment().getEquipmentType().getName());
		}
		vo.setHelperId(plr.getHelperId());
		vo.setHelperName(plr.getHelperName());
		vo.setWorkSheetCode(plr.getWorkSheetCode());
		if(plr.getMappingDate()!=null) {
			vo.setMappingDate(sdf.format(plr.getMappingDate()));
		}
		vo.setUsageRate(plr.getUsageRate());
		return vo;
	}

	/**
	 * 编辑装备关联记录
	 * @return
	 */
	@RequestMapping("/updateEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap updateEquipmentMappingRecord(EquipmentDeviceSiteMapping equipmentDeviceSiteMapping) {
		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping eq = equipmentDeviceSiteMappingService.queryObjById(equipmentDeviceSiteMapping.getId());
		eq.setMappingDate(equipmentDeviceSiteMapping.getMappingDate());
		eq.setEquipment(equipmentDeviceSiteMapping.getEquipment());
		eq.setDeviceSite(equipmentDeviceSiteMapping.getDeviceSite());
		eq.setWorkSheetCode(equipmentDeviceSiteMapping.getWorkSheetCode());
		eq.setHelperName(equipmentDeviceSiteMapping.getHelperName());
		eq.setHelperId(equipmentDeviceSiteMapping.getHelperId());
		eq.setUsageRate(equipmentDeviceSiteMapping.getUsageRate());
		equipmentDeviceSiteMappingService.updateObj(eq);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	

	
	/**
	 * 根据id删除装备关联记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap deleteEquipmentMappingRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		equipmentDeviceSiteMappingService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 解除装备关联
	 * @param id
	 * @return
	 */
	@RequestMapping("/unbindEquipmentMappingRecord.do")
	@ResponseBody
	public ModelMap unbindEquipmentMappingRecord(String id,Principal principal) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		//获取当前登录用户
		/*HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User user = userService.queryUserByUsername(principal.getName());

		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping equipmentDeviceSiteMapping = equipmentDeviceSiteMappingService.queryObjById(Long.valueOf(id));
		if(user!=null) {
			equipmentDeviceSiteMapping.setUnbindUserId(user.getId());
			equipmentDeviceSiteMapping.setUnbindUsername(user.getUsername());
		}
		equipmentDeviceSiteMapping.setUnbind(true);
		equipmentDeviceSiteMapping.setUnbindDate(new Date());
		equipmentDeviceSiteMappingService.updateObj(equipmentDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}


	/**
	 * 根据工单查询装备关联记录
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentMappingRecordByWorkSheetNo.do")
	@ResponseBody
	public List<EquipmentDeviceSiteMapping> queryEquipmentMappingRecordByWorkSheetNo(Long workSheetId) {

		return equipmentDeviceSiteMappingService.queryEquipmentMappingRecordByWorkSheetNo(workSheetId);
	}
} 
