package com.digitzones.controllers;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.digitzones.model.QualityCalendarRecord;
import com.digitzones.model.QualityCalendarType;
import com.digitzones.service.IQualityCalendarRecordService;
import com.digitzones.service.IQualityCalendarTypeService;
import com.digitzones.util.DateStringUtil;
import com.digitzones.vo.QualityCalendarRecordVO;
/**
 * 质量记录控制器
 * @author zdq
 * 2018年7月11日
 */
@Controller
@RequestMapping("/qualityCalendarRecord")
public class QualityCalendarRecordController {
	private DateStringUtil util = new DateStringUtil();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private IQualityCalendarRecordService qualityCalendarRecordService;
	private IQualityCalendarTypeService qualityCalendarTypeService;
	@Autowired
	public void setQualityCalendarTypeService(IQualityCalendarTypeService qualityCalendarTypeService) {
		this.qualityCalendarTypeService = qualityCalendarTypeService;
	}
	@Autowired
	public void setQualityCalendarRecordService(IQualityCalendarRecordService qualityCalendarRecordService) {
		this.qualityCalendarRecordService = qualityCalendarRecordService;
	}
	/**
	 * 查询质量记录信息
	 * @return
	 */
	@RequestMapping("/queryQualityCalendarRecords.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryQualityCalendarRecords(@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		Pager<QualityCalendarRecord> pager = qualityCalendarRecordService.queryObjs("from QualityCalendarRecord c "
				+ " order by c.currentDate desc", page, rows, new Object[] {});
		List<QualityCalendarRecordVO> vos = new ArrayList<>();
		List<QualityCalendarRecord> records = pager.getData();
		for(QualityCalendarRecord r : records) {
			vos.add(model2vo(r));
		}
		modelMap.addAttribute("rows", vos);
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}

	/**
	 * 查询质量记录信息(搜索)
	 * @return
	 */
	@RequestMapping("/queryQualityCalendarRecordsBySearch.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryQualityCalendarRecordsBySearch(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		
		String hql="from QualityCalendarRecord c where 1=1";
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
		
		Pager<QualityCalendarRecord> pager = qualityCalendarRecordService.queryObjs(hql, page, rows, obj);
		List<QualityCalendarRecordVO> vos = new ArrayList<>();
		List<QualityCalendarRecord> records = pager.getData();
		for(QualityCalendarRecord r : records) {
			vos.add(model2vo(r));
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("rows", vos);
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
	
	private QualityCalendarRecordVO model2vo(QualityCalendarRecord record) {
		if(record == null) {
			return null;
		}
		QualityCalendarRecordVO vo = new QualityCalendarRecordVO();
		vo.setId(record.getId());
		vo.setContacts(record.getContacts());
		vo.setContent(record.getContent());
		if(record.getCurrentDate()!=null) {
			vo.setCurrentDate(format.format(record.getCurrentDate()));
		}
		vo.setGradeId(record.getGradeId());
		vo.setGradeName(record.getGradeName());
		vo.setTypeId(record.getTypeId());
		vo.setTypeName(record.getTypeName());
		vo.setNote(record.getNote());
		vo.setTel(record.getTel());
		vo.setCustomer(record.getCustomer());
		return  vo;
	}
	/**
	 * 添加等级技能信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addQualityCalendarRecord.do")
	@ResponseBody
	public ModelMap addQualityCalendarRecord(QualityCalendarRecord qualityCalendarRecord) {
		ModelMap modelMap = new ModelMap();
		qualityCalendarRecordService.addObj(qualityCalendarRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}

	/**
	 * 根据id查询质量记录信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryQualityCalendarRecordById.do")
	@ResponseBody
	public QualityCalendarRecordVO queryQualityCalendarRecordById(Long id) {
		QualityCalendarRecord c =  qualityCalendarRecordService.queryObjById(id);
		return model2vo(c);
	}
	/**
	 * 更新质量记录
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateQualityCalendarRecord.do")
	@ResponseBody
	public ModelMap updateQualityCalendarRecord(QualityCalendarRecord qualityCalendarRecord) {
		ModelMap modelMap = new ModelMap();
		qualityCalendarRecordService.updateObj(qualityCalendarRecord);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 * 根据id删除质量记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteQualityCalendarRecord.do")
	@ResponseBody
	public ModelMap deleteQualityCalendarRecord(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		qualityCalendarRecordService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 查询质量日历：工厂级图表
	 * @return
	 */
	@RequestMapping("/queryQualityCalendar.do")
	@ResponseBody
	public ModelMap queryQualityCalendar() {
		List<List<Integer>> data = new ArrayList<>();
		ModelMap modelMap = new ModelMap();
		//生成当前月的月份和年份
		Date date = new Date();
		List<String> names = new ArrayList<>();
		//查询所有类别
		List<QualityCalendarType> types = qualityCalendarTypeService.queryAllQualityCalendarTypes();
		for(QualityCalendarType type : types) {
			names.add(type.getName());
		}
		//生成当前月的天
		List<Date> days = util.generateOneMonthDay(util.date2Month(date));
		for(Date day : days) {
			List<Integer> typeCount = new ArrayList<>();
			for(QualityCalendarType type : types) {
				//根据日期和类型id 查找数量
				int count = qualityCalendarRecordService.queryCountByDayAndTypeId(day, type.getId());
				if(count!=0) {
					typeCount.add(count);
				}
				data.add(typeCount);
			}
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
	}
} 
