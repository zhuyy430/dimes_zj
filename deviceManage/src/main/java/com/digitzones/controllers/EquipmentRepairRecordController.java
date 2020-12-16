package com.digitzones.controllers;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.EquipmentRepairRecord;
import com.digitzones.model.NGRecord;
import com.digitzones.model.Pager;
import com.digitzones.service.IEquipmentRepairRecordService;
import com.digitzones.vo.EquipmentRepairRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 装备维修记录控制器
 */
@Controller
@RequestMapping("/equipmentRepairRecord")
public class EquipmentRepairRecordController {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IEquipmentRepairRecordService equipmentRepairRecordService;
	/**
	 * 分页查询维修记录信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentRepairRecord.do")
	@ResponseBody 
	public ModelMap queryEquipmentRepairRecord(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,HttpServletRequest request) {
		Pager<EquipmentRepairRecord> pager = null;
		pager = equipmentRepairRecordService.queryObjs("select p from EquipmentRepairRecord p ", page, rows, new Object[] {});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}

	/**
	 * 分页查询维修记录信息（搜索）
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEquipmentRepairRecordBySearch.do")
	@ResponseBody
	public ModelMap queryEquipmentRepairRecordBySearch(@RequestParam Map<String,String> params, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {

		//String hql = "from NGRecord record where record.deviceSiteId=?0 and record.deleted=?1";
		String hql = "select record from EquipmentRepairRecord record inner join record.equipment e inner join e.equipmentType et inner join record.pressLight p inner join p.pressLightType pt where 1=1 ";


		String searchText=params.get("searchText");
		String searchChange=params.get("searchChange");
		String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		List<Object> list=new ArrayList<Object>();
		int i=0;
		try {
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
				hql+=" and record.repairDate>=?"+i;
				list.add(sdf.parse(beginDateStr));
				i++;
			}
			if(endDateStr!=null && !"".equals(endDateStr)) {
				hql+=" and record.repairDate<=?"+i;
				list.add(sdf.parse(endDateStr));
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!StringUtils.isEmpty(searchChange)) {
			if (searchChange.equals("equipmentTypeCode")) {//,,,,repairEmployeeName
				hql+=" and et.code=?"+i;
				list.add(searchText);
			} else if (searchChange.equals("equipmentTypeName")) {
				hql+=" and et.name=?"+i;
				list.add(searchText);
			}else if (searchChange.equals("equipmentCode")) {
				hql+=" and e.code=?"+i;
				list.add(searchText);
			} else if(searchChange.equals("pressLightTypeName")){
				hql+=" and pt.name=?"+i;
				list.add(searchText);
			} else if(searchChange.equals("pressLightReason")){
				hql+=" and p.reason=?"+i;
				list.add(searchText);
			} else if(searchChange.equals("repairEmployeeName")){
				hql+=" and record.repairEmployeeName=?"+i;
				list.add(searchText);
			}
		}
		hql+=" order by record.repairDate desc";
		Object[] obj =  list.toArray();



		Pager<EquipmentRepairRecord> pager = equipmentRepairRecordService.queryObjs(hql, page, rows, obj);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 新增维修记录
	 * @param equipmentRepairRecord
	 * @return
	 */
	@RequestMapping("/addEquipmentRepairRecord.do")
	@ResponseBody
	public ModelMap addEquipmentRepairRecord(EquipmentRepairRecord equipmentRepairRecord,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		equipmentRepairRecordService.addObj(equipmentRepairRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	/**
	 * 根据id查询记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryEquipmentRepairRecordById.do")
	@ResponseBody
	public EquipmentRepairRecordVO queryEquipmentRepairRecordById(Long id,HttpServletRequest request) {
		EquipmentRepairRecord record = equipmentRepairRecordService.queryObjById(id);

		return model2VO(record);
	}
	/**
	 * 更新记录信息
	 * @param equipment
	 * @return
	 */
	@RequestMapping("/updateEquipmentRepairRecord.do")
	@ResponseBody
	public ModelMap updateEquipmentRepairRecord(EquipmentRepairRecord equipment,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		equipmentRepairRecordService.updateObj(equipment);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 根据id删除参数信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteEquipmentRepairRecord.do")
	@ResponseBody
	public ModelMap deleteEquipmentRepairRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		try {
			equipmentRepairRecordService.deleteEquipmentRepairRecords(id.split(","));
		}catch (RuntimeException e) {
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "删除失败!");
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}


	/**
	 * 根据查询条件查询未停用的装备
	 */
	/*@RequestMapping("/queryPageBySearch.do")
	@ResponseBody
	public ModelMap queryPageBySearch(@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page,
									  String queryCode,String queryName,String queryUnitType){
		ModelMap modelMap=new ModelMap();
		String hql="";
		List<Object> paramsList = new ArrayList<>();
		int i;
		hql += "from Equipment e where 1=1 ";
		i = 0;


		if (!StringUtils.isEmpty(queryCode)) {
			hql += " and e.equipmentType.code like ?" + (i++);
			paramsList.add("%" + queryCode + "%");
		}
		if (!StringUtils.isEmpty(queryName)) {
			hql += " and e.equipmentType.name like ?" + (i++);
			paramsList.add("%" + queryName + "%");
		}
		if (!StringUtils.isEmpty(queryUnitType)) {
			hql += " and e.equipmentType.unitType like ?" + (i++);
			paramsList.add("%" + queryUnitType + "%");
		}

		Pager<Equipment> pager = equipmentService.queryObjs(hql, page, rows, paramsList.toArray());
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("code", "0");
		modelMap.addAttribute("msg", "");
		return modelMap;
	}*/

	private EquipmentRepairRecordVO model2VO(EquipmentRepairRecord e) {
		if(e==null) {
			return null;
		}
		EquipmentRepairRecordVO vo = new EquipmentRepairRecordVO();
		vo.setId(e.getId());
		vo.setNote(e.getNote());
		vo.setRepairDate(sdf.format(e.getRepairDate()));
		vo.setRepairEmployeeCode(e.getRepairEmployeeCode());
		vo.setRepairEmployeeName(e.getRepairEmployeeName());
		vo.setCumulation(e.getEquipment().getCumulation());
		vo.setEquipmentCode(e.getEquipment().getCode());
		vo.setEquipmentTypeCode(e.getEquipment().getEquipmentType().getCode());
		vo.setEquipmentTypeName(e.getEquipment().getEquipmentType().getName());
		vo.setPressLightId(e.getPressLight().getId());
		vo.setPressLightTypeCode(e.getPressLight().getPressLightType().getCode());
		vo.setPressLightTypeName(e.getPressLight().getPressLightType().getName());
		vo.setUnitType(e.getEquipment().getEquipmentType().getUnitType());
		vo.setPressLightReason(e.getPressLight().getReason());
		vo.setEquipmentId(e.getEquipment().getId());
		return vo;
	}
}
