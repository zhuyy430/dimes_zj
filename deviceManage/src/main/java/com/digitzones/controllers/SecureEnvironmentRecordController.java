package com.digitzones.controllers;
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

import com.digitzones.model.Pager;
import com.digitzones.model.SecureEnvironmentGrade;
import com.digitzones.model.SecureEnvironmentRecord;
import com.digitzones.service.ISecureEnvironmentGradeService;
import com.digitzones.service.ISecureEnvironmentRecordService;
import com.digitzones.service.ISecureEnvironmentTypeService;
import com.digitzones.util.DateStringUtil;
import com.digitzones.vo.SecureEnvironmentRecordVO;
/**
 * 安环记录控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/secureEnvironmentRecord")
public class SecureEnvironmentRecordController {
	private DateStringUtil util = new DateStringUtil();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private ISecureEnvironmentTypeService secureEnvironmentTypeService;
	@Autowired
	private ISecureEnvironmentRecordService secureEnvironmentRecordService;
	@Autowired
	private ISecureEnvironmentGradeService secureEnvironmentGradeService;
	/**
	 * 查询安环记录信息
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentRecords.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySecureEnvironmentRecords(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<SecureEnvironmentRecord> pager = secureEnvironmentRecordService.queryObjs("from SecureEnvironmentRecord record inner join record.grade g order by c.currentDate desc", page, rows, new Object[] {});
		List<SecureEnvironmentRecordVO> vos = new ArrayList<>();
		List<SecureEnvironmentRecord> records = pager.getData();
		for(SecureEnvironmentRecord record:records) {
			vos.add(model2vo(record));
		}
		modelMap.addAttribute("rows", vos);
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 查询安环记录信息(搜索)
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentRecordsandSearch.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap querySecureEnvironmentRecordsandSearch(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		
		
		String hql="from SecureEnvironmentRecord c where 1=1";
		String searchText=params.get("searchText");
	 	String searchChange=params.get("searchChange");
	 	String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		List<Object> list=new ArrayList<Object>();
		int i=list.size()-1;
		try {
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
					i++;
					hql+=" and c.currentDate>=?"+i;	
					list.add(format.parse(beginDateStr));
			}
			if(endDateStr!=null && !"".equals(endDateStr)) {
				i++;
				hql+=" and c.currentDate<=?"+i;
				list.add(format.parse(endDateStr));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(searchText!=null && !"".equals(searchText)&&searchChange!=null && !"".equals(searchChange)) {
			hql+=" and c."+searchChange+" like '%'+?"+(i+1)+"+'%'";
			list.add(searchText);
		}
		hql+=" order by c.currentDate desc";
		Object[] obj =  new Object[] {};  
		if(list.size()!=0) {
			obj =  list.toArray(new Object[1]);
		}
		
		
		Pager<SecureEnvironmentRecord> pager = secureEnvironmentRecordService.queryObjs(hql, page, rows,obj);
		List<SecureEnvironmentRecordVO> vos = new ArrayList<>();
		List<SecureEnvironmentRecord> records = pager.getData();
		for(SecureEnvironmentRecord record:records) {
			vos.add(model2vo(record));
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("rows", vos);
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}

	
	private SecureEnvironmentRecordVO model2vo(SecureEnvironmentRecord record) {
		if(record == null) {
			return null;
		}
		SecureEnvironmentRecordVO vo = new SecureEnvironmentRecordVO();
		vo.setId(record.getId());
		if(record.getCurrentDate()!=null) {
			vo.setCurrentDate(format.format(record.getCurrentDate()));
		}
		vo.setDescription(record.getDescription());
		if(record.getGrade()!=null) {
			vo.setGradeId(record.getGrade().getId());
			vo.setGradeName(record.getGrade().getName());
		}
		vo.setTypeId(record.getTypeId());
		vo.setTypeName(record.getTypeName());
		return vo;
	}
	
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addSecureEnvironmentRecord.do")
	@ResponseBody
	public ModelMap addSecureEnvironmentRecord(SecureEnvironmentRecord secureEnvironmentRecord) {
		ModelMap modelMap = new ModelMap();
		secureEnvironmentRecordService.addObj(secureEnvironmentRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}

	/**
	 * 根据id查询安环记录信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/querySecureEnvironmentRecordById.do")
	@ResponseBody
	public SecureEnvironmentRecordVO querySecureEnvironmentRecordById(Long id) {
		SecureEnvironmentRecord c =  secureEnvironmentRecordService.queryObjById(id);
		return model2vo(c);
	}
	/**
	 * 更新安环记录
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateSecureEnvironmentRecord.do")
	@ResponseBody
	public ModelMap updateSecureEnvironmentRecord(SecureEnvironmentRecord secureEnvironmentRecord) {
		ModelMap modelMap = new ModelMap();
		secureEnvironmentRecordService.updateObj(secureEnvironmentRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}

	/**
	 * 根据id删除安环记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSecureEnvironmentRecord.do")
	@ResponseBody
	public ModelMap deleteSecureEnvironmentRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		secureEnvironmentRecordService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	
	/**
	 * 查询安环日历：工厂级图表
	 * @return
	 */
	@RequestMapping("/querySecureEnvironment.do")
	@ResponseBody
	public ModelMap querySecureEnvironment(String time) {
		List<SecureEnvironmentRecord> data = new ArrayList<>();
		ModelMap modelMap = new ModelMap();
		//生成当前月的月份和年份
		Date date = new Date();
		if(time!=null&&!time.equals("")){
			date = util.string2Date(time);
		}
		//生成当前月的天
		List<Date> days = util.generateOneMonthDay(util.date2Month(date));
		for(Date day : days) {
			data.add(secureEnvironmentRecordService.queryMostSeriousSecureEnvironmentRecordByDate(day));
		}
		List<SecureEnvironmentGrade> list = secureEnvironmentGradeService.queryAllSecureEnvironmentGrades();
		modelMap.addAttribute("data", data);
		modelMap.addAttribute("grades", list);
		return modelMap;
	}
	/**
	 * 查询安环日历：工厂级图表
	 * @return
	 */
	/*@RequestMapping("/querySecureEnvironment.do")
	@ResponseBody
	public ModelMap querySecureEnvironment() {
		List<List<Integer>> data = new ArrayList<>();
		ModelMap modelMap = new ModelMap();
		//生成当前月的月份和年份
		Date date = new Date();
		List<String> names = new ArrayList<>();
		//查询所有类别
		List<SecureEnvironmentType> types = secureEnvironmentTypeService.queryAllSecureEnvironmentTypes();
		for(SecureEnvironmentType type : types) {
			names.add(type.getName());
		}
		//生成当前月的天
		List<Date> days = util.generateOneMonthDay(util.date2Month(date));
		for(Date day : days) {
			List<Integer> typeCount = new ArrayList<>();
			for(SecureEnvironmentType type : types) {
				//根据日期和类型id 查找数量
				int count = secureEnvironmentTypeService.queryCountByDayAndTypeId(day, type.getId());
				if(count!=0)
					typeCount.add(count);
			}
			data.add(typeCount);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		modelMap.put("month",util.date2Month(date));
		modelMap.put("data",data);
		modelMap.put("names",names);
		modelMap.put("year",c.get(Calendar.YEAR));
		c.add(Calendar.MONTH, 1);
		modelMap.put("nextMonth",util.date2Month(c.getTime()));
		return modelMap;
	}*/
} 
