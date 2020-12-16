package com.digitzones.devmgr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.ISparepartService;
import com.digitzones.model.Device;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceService;
@RestController
@RequestMapping("/sparepart")
public class SparepartController {
	@Autowired
	private ISparepartService sparepartService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceImpl")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	/**
	 * 根据工件类别id查找工件信息
	 * @return
	 */
	@RequestMapping("/querySparepartsByTypeId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySparepartsByTypeId(Long typeId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String spareCode,String spareName,String spareType) {
		String hql = "select d from Sparepart d inner join d.projectType p  where p.id=?0";
		
		List<Object> args = new ArrayList<>();
		args.add(typeId);
		int i = 1;
		
		if(!StringUtils.isEmpty(spareCode)) {
			hql += " and d.code like ?" + i;
			i++;
			args.add("%" + spareCode + "%");
		}

		if(!StringUtils.isEmpty(spareName)) {
			hql += " and d.name like ?" + i;
			i++;
			args.add("%" + spareName + "%");
		}
		
		if(!StringUtils.isEmpty(spareType)) {
			hql += " and d.unitType like ?" + i;
			i++;
			args.add("%" + spareType + "%");
		}
		
		hql+=" order by d.code";
		Pager<Sparepart> pager = sparepartService.queryObjs(hql, page, rows, args.toArray());
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
	
	/**
	 * 添加备件信息
	 * @param sparepart
	 * @return
	 */
	@RequestMapping("/addSparepart.do")
	public ModelMap addSparepart(Sparepart sparepart) {
		ModelMap modelMap = new ModelMap();
		Sparepart c4code = sparepartService.queryByProperty("code", sparepart.getCode());
		if(c4code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "备件编码已被使用");
		}else  {
			sparepartService.addObj(sparepart);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySparepartById.do")
	public Sparepart querySparepartById(Long id) {
		Sparepart sparepart = sparepartService.queryObjById(id);
		return sparepart;
	}
	/**
	 * 更新备件信息
	 * @param sparepart
	 * @return
	 */
	@RequestMapping("/updateSparepart.do")
	public ModelMap updateSparepart(Sparepart sparepart) {
		ModelMap modelMap = new ModelMap();
		sparepartService.updateObj(sparepart);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 根据id删除备品备件 信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSparepart.do")
	@ResponseBody
	public ModelMap deleteSparepart(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		sparepartService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 根据设备ID查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySparepartsByDeviceId.do")
	@ResponseBody
	public List<Sparepart> querySparepartsByDeviceId(Long deviceId) {
		Device device = deviceService.queryObjById(deviceId);
		return sparepartService.querySparepartsByDeviceCode(device.getCode());
	}
	/**
	 * 根据设备ID查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryAllSpareparts.do")
	@ResponseBody
	public List<Sparepart> queryAllSpareparts() {
		return sparepartService.queryAllSpareparts();
	}
	/**
	 * 根据保养计划ID查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryAllSparepartsBymaintenancePlanRecordId.do")
	@ResponseBody
	public List<Sparepart> queryAllSparepartsBymaintenancePlanRecordId(Long maintenancePlanRecordId) {
		return sparepartService.queryAllSparepartsBymaintenancePlanRecordId(maintenancePlanRecordId);
	}
	/**
	 * 根据条件查询 备件信息
	 * @param maintenancePlanRecordId
	 * @param rows
	 * @param page
	 * @param sparepartCode
	 * @param sparepartName
	 * @param sparepartUnitType
	 * @return
	 */
	@RequestMapping("/queryOtherSpareparts.do")
	@ResponseBody
	public ModelMap queryOtherSpareparts(Long maintenancePlanRecordId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String sparepartCode,String sparepartName,String sparepartUnitType) {
		String hql = "from Sparepart s where " + 
				"  s.code not in (select ms.code from MaintenanceSparepart ms where ms.maintenancePlanRecord.id=?0)";
		
		List<Object> args = new ArrayList<>();
		args.add(maintenancePlanRecordId);
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
	 * 根据条件查询 备件信息
	 * @param maintenancePlanRecordId
	 * @param rows
	 * @param page
	 * @param sparepartCode
	 * @param sparepartName
	 * @param sparepartUnitType
	 * @return
	 */
	@RequestMapping("/queryAllSparepartsByDeviceRepairIdAndOtherCondition.do")
	@ResponseBody
	public ModelMap queryAllSparepartsByDeviceRepairIdAndOtherCondition(Long maintenancePlanRecordId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,
			String sparepartCode,String sparepartName,String sparepartUnitType) {
		String hql = "from Sparepart s where"
				+ "  s.code not in (select sr.sparepart.code from SparepartRecord sr where sr.deviceRepair.id=?0)";
		
		List<Object> args = new ArrayList<>();
		args.add(maintenancePlanRecordId);
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
	 * 根据保养计划ID查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryAllSparepartsByDeviceRepairId.do")
	@ResponseBody
	public List<Sparepart> queryAllSparepartsByDeviceRepairId(Long deviceRepairId) {
		return sparepartService.queryAllSparepartsByDeviceRepairId(deviceRepairId);
	}
	
	/**
	 * 根据设备代码和保养计划记录id查找保养配件信息
	 * @param deviceCode 设备代码
	 * @param maintenancePlanRecordId 
	 * @return
	 */
	@RequestMapping("/queryMaintenanceSpareparts.do")
	@ResponseBody
	public List<Sparepart> queryMaintenanceSpareparts(String deviceCode,Long maintenancePlanRecordId){
		return sparepartService.queryMaintenanceSparepartsByDeviceCode(deviceCode,maintenancePlanRecordId);
	}
	/**
	 * 根据设备ID查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySparepartsByDeviceRepairId.do")
	@ResponseBody
	public List<Sparepart> querySparepartsByDeviceRepairId(Long DeviceRepairId) {
		DeviceRepair dro = deviceRepairOrderService.queryObjById(DeviceRepairId);
		return sparepartService.querySparepartsByDeviceCode(dro.getDevice().getCode(),DeviceRepairId);
	}
	/**
	 * 根据设备ID查找备件信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySparepartsByMaintenanceId.do")
	@ResponseBody
	public List<Sparepart> querySparepartsByMaintenanceId(Long MaintenanceId) {
		MaintenancePlanRecord m = maintenancePlanRecordService.queryObjById(MaintenanceId);
		return sparepartService.queryMaintenanceSparepartsByDeviceCode(m.getDevice().getCode(),MaintenanceId);
	}
	/**
	 * 根据备件代码查找备件信息
	 */
	@RequestMapping("/querySparepartsByCode.do")
	@ResponseBody
	public Sparepart querySparepartsByCode(String SparepartsCode){
		return sparepartService.queryByProperty("code", SparepartsCode);
	}
}
