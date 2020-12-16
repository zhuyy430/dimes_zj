package com.digitzones.controllers;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.constants.Constant;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IMeasuringToolDeviceSiteMappingService;
import com.digitzones.service.IUserService;
import com.digitzones.vo.EquipmentDeviceSiteMappingVO;
/**
 * 设备站点和量具关联记录
 * @author zdq
 * 2018年6月28日
 */
@Controller
@RequestMapping("/measuringToolMappingRecord")
public class MeasuringToolMappingRecordController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private IMeasuringToolDeviceSiteMappingService  measuringToolDeviceSiteMappingService;
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IUserService userService; 
	@Autowired
	public void setDeviceSiteService(IDeviceSiteService deviceSiteService) {
		this.deviceSiteService = deviceSiteService;
	}

	@Autowired
	public void setMeasuringToolDeviceSiteMappingService(IMeasuringToolDeviceSiteMappingService measuringToolDeviceSiteMappingService) {
		this.measuringToolDeviceSiteMappingService = measuringToolDeviceSiteMappingService;
	}

	/**
	 * 根据设备站点id查找量具关联
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMeasuringToolMappingRecordByDeviceSiteId.do")
	@ResponseBody
	public ModelMap queryMeasuringToolMappingRecordByDeviceSiteId(Long deviceSiteId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.id=?0  and edsm.equipment.baseCode=?1"
				+ " order by edsm.unbind,edsm.mappingDate desc";
		Pager<EquipmentDeviceSiteMapping> pager = measuringToolDeviceSiteMappingService.queryObjs(hql, page, rows, new Object[] {deviceSiteId,Constant.EquipmentType.MEASURINGTOOL});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据设备站点id查找量具关联（搜索）
	 * @param deviceSiteId
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMeasuringToolMappingRecordByDeviceSiteIdandSearch.do")
	@ResponseBody
	public ModelMap queryMeasuringToolMappingRecordByDeviceSiteIdandSearch(Long deviceSiteId,@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		String hql = "from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.id=?0 and edsm.unbind=?1 and edsm.equipment.baseCode=?2";
		String searchText=params.get("searchText");
	 	String searchChange=params.get("searchChange");
	 	String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		List<Object> list=new ArrayList<Object>();
		list.add(deviceSiteId);
		list.add(false);
		list.add(Constant.EquipmentType.MEASURINGTOOL);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(searchText!=null && !"".equals(searchText)&&searchChange!=null && !"".equals(searchChange)) {
			hql+=" and edsm."+searchChange+" like '%'+?"+(i+1)+"+'%'";
			list.add(searchText);
		}
		hql+=" order by edsm.mappingDate desc";
		Object[] obj =  list.toArray(new Object[1]);  
		
		Pager<EquipmentDeviceSiteMapping> pager = measuringToolDeviceSiteMappingService.queryObjs(hql, page, rows, obj);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 添加量具关联记录
	 * @return
	 */
	@RequestMapping("/addMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap addMeasuringToolMappingRecord(EquipmentDeviceSiteMapping measuringToolDeviceSiteMapping,String deviceSiteCode,Principal principal) {
		ModelMap modelMap = new ModelMap();
		DeviceSite ds = new DeviceSite();
		if(null!=deviceSiteCode&&!"".equals(deviceSiteCode)){
			ds = deviceSiteService.queryByProperty("code", deviceSiteCode);
			if(ds==null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "请选择设备站点");
				return modelMap;
			}
		}else{
			ds = deviceSiteService.queryObjById(measuringToolDeviceSiteMapping.getDeviceSite().getId());
			if(ds==null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "请选择设备站点");
				return modelMap;
			}
		}

		//判断该量具是否已经被关联到设备上
		EquipmentDeviceSiteMapping edm = measuringToolDeviceSiteMappingService.queryByNo(measuringToolDeviceSiteMapping.getEquipment().getCode());
		if(edm!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "该量具已被关联到 " + edm.getDeviceSite().getCode() + "上");
			return modelMap;
		}
		
		if(measuringToolDeviceSiteMapping.getMappingDate()==null) {
			measuringToolDeviceSiteMapping.setMappingDate(new Date());
		}

		//获取当前登录用户
		/*HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User user = userService.queryUserByUsername(principal.getName());
		if(user!=null) {
			measuringToolDeviceSiteMapping.setBindUserId(user.getId());
			measuringToolDeviceSiteMapping.setBindUsername(user.getUsername());
		}
		measuringToolDeviceSiteMappingService.addObj(measuringToolDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询量具关联记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryMeasuringToolMappingRecordById.do")
	@ResponseBody
	public EquipmentDeviceSiteMappingVO queryMeasuringToolMappingRecordById(Long id) {
		EquipmentDeviceSiteMapping plr = measuringToolDeviceSiteMappingService.queryObjById(id);
		
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
	 * 编辑量具关联记录
	 * @return
	 */
	@RequestMapping("/updateMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap updateMeasuringToolMappingRecord(EquipmentDeviceSiteMapping measuringToolDeviceSiteMapping) {
		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping eq = measuringToolDeviceSiteMappingService.queryObjById(measuringToolDeviceSiteMapping.getId());
		eq.setMappingDate(measuringToolDeviceSiteMapping.getMappingDate());
		eq.setEquipment(measuringToolDeviceSiteMapping.getEquipment());
		eq.setDeviceSite(measuringToolDeviceSiteMapping.getDeviceSite());
		eq.setWorkSheetCode(measuringToolDeviceSiteMapping.getWorkSheetCode());
		eq.setHelperName(measuringToolDeviceSiteMapping.getHelperName());
		eq.setHelperId(measuringToolDeviceSiteMapping.getHelperId());
		eq.setUsageRate(measuringToolDeviceSiteMapping.getUsageRate());
		measuringToolDeviceSiteMappingService.updateObj(eq);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 根据id删除量具关联记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap deleteMeasuringToolMappingRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		measuringToolDeviceSiteMappingService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 解除量具关联
	 * @param id
	 * @return
	 */
	@RequestMapping("/unbindMeasuringToolMappingRecord.do")
	@ResponseBody
	public ModelMap unbindMeasuringToolMappingRecord(String id,Principal principal) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		//获取当前登录用户
		/*HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
		User user = userService.queryUserByUsername(principal.getName());

		ModelMap modelMap = new ModelMap();
		EquipmentDeviceSiteMapping measuringToolDeviceSiteMapping = measuringToolDeviceSiteMappingService.queryObjById(Long.valueOf(id));
		if(user!=null) {
			measuringToolDeviceSiteMapping.setUnbindUserId(user.getId());
			measuringToolDeviceSiteMapping.setUnbindUsername(user.getUsername());
		}
		measuringToolDeviceSiteMapping.setUnbind(true);
		measuringToolDeviceSiteMapping.setUnbindDate(new Date());
		measuringToolDeviceSiteMappingService.updateObj(measuringToolDeviceSiteMapping);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "操作成功!");
		return modelMap;
	}
	/**
	 * 根据设备站点代码查找关联的量具
	 * @return
	 */
	@RequestMapping("/queryMeasuringToolMappingRecordByDeviceSiteCode.do")
	@ResponseBody
	public List<EquipmentDeviceSiteMapping> queryMeasuringToolMappingRecordByDeviceSiteCode(@RequestParam(value="deviceSiteCode") String deviceSiteCode,@RequestParam(value="searchText") String searchText) {
		List<EquipmentDeviceSiteMapping> equList = measuringToolDeviceSiteMappingService.queryByDeviceSiteCode(deviceSiteCode,searchText);
		return equList;
	}
} 
