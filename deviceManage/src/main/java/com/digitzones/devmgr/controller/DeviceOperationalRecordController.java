package com.digitzones.devmgr.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.model.DeviceOperationalRecord;
import com.digitzones.devmgr.service.IDeviceOperationalRecordService;
import com.digitzones.model.Classes;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IClassesService;
import com.digitzones.service.IUserService;
import com.digitzones.util.DateStringUtil;
@RestController
@RequestMapping("/deviceOperationalRecord")
public class DeviceOperationalRecordController {
	@Autowired
	private IDeviceOperationalRecordService deviceOperationalRecordService;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private IUserService userService;
	
	private DecimalFormat decimalFormat = new DecimalFormat("#.00");
	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd"); 
	/**
	 * 分页查询设备维修记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceOperationalRecordByDeviceId.do")
	@ResponseBody
	public ModelMap queryDeviceOperationalRecordByDeviceId(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long deviceId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<DeviceOperationalRecord> pager = deviceOperationalRecordService.queryObjs("select dor from DeviceOperationalRecord dor inner join dor.device d "
				+ "where d.id=?0 and d.isDeviceManageUse=?1 order by dor.date DESC,dor.createDate DESC", page, rows, new Object[] {deviceId,true});
		List<DeviceOperationalRecord> data = pager.getData();
		modelMap.addAttribute("rows", data);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addDeviceOperationalRecord.do")
	@ResponseBody
	public ModelMap addDeviceOperationalRecord(DeviceOperationalRecord deviceOperationalRecord,Principal principal){
		ModelMap modelMap = new ModelMap();
		String username = principal.getName();
		User user = userService.queryByProperty("username", username);
		if (user.getEmployee() == null) {
			deviceOperationalRecord.setInformant(principal.getName());
		} else {
			deviceOperationalRecord.setInformant(user.getEmployee().getName());
		}
		deviceOperationalRecord.setCreateDate(new Date());
		deviceOperationalRecord.setInformant(principal.getName());
		Double sumRunTime = deviceOperationalRecordService.queryAllRunTimeByDeviceId(deviceOperationalRecord.getDevice().getId());
		deviceOperationalRecord.setSumTime(sumRunTime+deviceOperationalRecord.getRunTime());
		deviceOperationalRecordService.addObj(deviceOperationalRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "新增成功!");
		return modelMap;
	}
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping("/queryDeviceOperationalRecordById.do")
	@ResponseBody
	public DeviceOperationalRecord queryDeviceOperationalRecordById(Long id){
		return deviceOperationalRecordService.queryObjById(id);
	}
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/updateDeviceOperationalRecord.do")
	@ResponseBody
	public ModelMap updateDeviceOperationalRecord(DeviceOperationalRecord deviceOperationalRecord,Principal principal){
		ModelMap modelMap = new ModelMap();
		DeviceOperationalRecord dor = deviceOperationalRecordService.queryObjById(deviceOperationalRecord.getId());
		dor.setClasses(deviceOperationalRecord.getClasses());
		dor.setDate(deviceOperationalRecord.getDate());
		dor.setDevice(deviceOperationalRecord.getDevice());
		dor.setNgTime(deviceOperationalRecord.getNgTime());
		dor.setRunTime(deviceOperationalRecord.getRunTime());
		dor.setInformant(principal.getName());
		deviceOperationalRecordService.updateObj(dor);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/deleteDeviceOperationalRecordById.do")
	@ResponseBody
	public ModelMap deleteDeviceOperationalRecordById(String id){
		if(id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		deviceOperationalRecordService.deleteObj(Long.parseLong(id));
		
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "删除成功!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	
	/**
	 * 设备运行报表
	 * @return
	 */
	@RequestMapping("/queryDeviceOperationalRecordForDeverId.do")
	@ResponseBody
	public ModelMap queryDeviceOperationalRecordForDeverId(Long deviceId,String date) {
		ModelMap modelMap = new ModelMap();
		Date d=null;
		try {
			d = formatter.parse(date+"-01");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		//产生一个月的时间
		DateStringUtil util = new DateStringUtil();
		List<Date> days =util.generateOneMonthDay(util.date2Month(d));
		
		List<String> dayList = new ArrayList<>();
		for(Date day : days) {
			dayList.add(util.date2DayOfMonth(day));
		}
		//存放总损时数(每个班的损时List)
		List<List<String>> list = new ArrayList<>();
		//查询所有班次
		List<Classes> classesList = classesService.queryAllClasses();
		
		for(int i = 0;i<classesList.size();i++) {
			//存放每个班的损时数
			List<String> lostTimeList = new ArrayList<>();
			Classes c = classesList.get(i);
			
			List<?> lists = deviceOperationalRecordService.queryDeviceOperationalRecordForDeverId(deviceId,c.getCode(), d);
			for(Date now : days) {
				String nowDay = util.date2DayOfMonth(now);
				boolean tf = true;
				for(Object ret:lists){
					Object[] arr = (Object[])ret;
					 String a= arr[0].toString();
					 Date time=null;
					 try {
						time = formatter.parse(a);
					} catch (ParseException e) {
						e.printStackTrace();
					} 
					if(nowDay.equals(util.date2DayOfMonth(time))) {
						tf =false;
						lostTimeList.add(decimalFormat.format(Double.parseDouble(arr[1].toString())));
					}
				}
				if(tf){
					lostTimeList.add(0+"");
				}
			}
			list.add(lostTimeList);
		}
		List<String> goalLostTimeList = new ArrayList<>();
		modelMap.addAttribute("classes",classesList);
		modelMap.addAttribute("goalLostTimeList",goalLostTimeList);
		modelMap.addAttribute("lostTimeList",list);
		modelMap.addAttribute("days", dayList);
		return modelMap;
	}
}
