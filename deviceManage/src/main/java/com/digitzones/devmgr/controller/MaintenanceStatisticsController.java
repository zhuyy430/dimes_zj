package com.digitzones.devmgr.controller;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.vo.CheckStatisticsVo;
import com.digitzones.util.DateStringUtil;
import com.digitzones.util.ExcelUtil;
@RestController
@RequestMapping("maintenanceStatistics")
public class MaintenanceStatisticsController {
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	/**
	 * 设备保养统计报表
	 * @param principal
	 * @param from 日期起始
	 * @param to 日期终止
	 * @param cycle 统计周期
	 * @return
	 */
	@RequestMapping("/statisticsReport.do")
	public List<CheckStatisticsVo> statisticsReport(Principal principal,String from,String to,String cycle){
		List<CheckStatisticsVo> vos = new ArrayList<>();
		List<String[]> statisticsData = maintenancePlanRecordService.queryStatisticsData(from,to,cycle);
		if(!CollectionUtils.isEmpty(statisticsData)) {
			for(Object[] statistics :statisticsData) {
				CheckStatisticsVo vo = new CheckStatisticsVo();
				vo.setCategory(statistics[0] + "");
				vo.setDate(statistics[1] + "");
				vo.setPlanCount(String.valueOf(statistics[2]));
				vo.setCompleteCount(String.valueOf(statistics[3]));
				vo.setDelayCount(String.valueOf(statistics[4]));
				vo.setDelayAndUncompleteCount(String.valueOf(statistics[5]));
				vos.add(vo);
			}
		}
		return vos;
	}
	/**
	 * @param principal
	 * @param from
	 * @param to
	 * @param cycle
	 * @return
	 */
	@RequestMapping("/separateStatisticsReport.do")
	public ModelMap separateStatisticsReport(Principal principal,String from,String to,String cycle){
		ModelMap modelMap = new ModelMap();
		if(!check(from, to, cycle, modelMap)) {
			return modelMap;
		}
		List<String> dateList = null;
		DateStringUtil util = new DateStringUtil();
		//产生日期
		switch(cycle) {
		case "年":dateList = util.generateYearsBetween(from,to);break;
		case "月":dateList = util.generateMonthsBetween(from,to);break;
		case "日":dateList = util.generateDaysBetween(from, to);break;
		case "周":dateList = util.generateWeeksBetween(from, to);
		}
		Map<String,List<String>> delayMap = new HashMap<>();
		Map<String,List<String>> delayAndUncompleteMap = new HashMap<>();
		//存放类别(生产单元等)
		Set<String> categorySet = new HashSet<>();
		List<String[]> statisticsData = maintenancePlanRecordService.queryStatisticsData(from,to,cycle);
		if(!CollectionUtils.isEmpty(statisticsData)) {
			for(Object[] statistics :statisticsData) {
				categorySet.add(statistics[0] + "");
			}
		}
		for(String category : categorySet) {
			List<String> delayList = delayMap.get(category);
			if(delayList == null) {
				delayList = new ArrayList<>();
				delayMap.put(category, delayList);
			}
			List<String> delayAndUnCompleteList = delayAndUncompleteMap.get(category);
			if(delayAndUnCompleteList == null) {
				delayAndUnCompleteList = new ArrayList<>();
				delayAndUncompleteMap.put(category, delayAndUnCompleteList);
			}
			for(String date : dateList) {
				boolean isExist = false;
				for(Object[] statistics : statisticsData) {
					//日期相等
					if(date.equals(statistics[1]+"")&&category.equals(statistics[0])) {
						delayList.add(statistics[4]+"");
						delayAndUnCompleteList.add(statistics[5]+"");
						isExist = true;
						break;
					}
				}
				if(!isExist) {
					delayList.add("0");
					delayAndUnCompleteList.add("0");
				}
			}
		}
		modelMap.addAttribute("categorySet",categorySet.toArray());
		modelMap.addAttribute("dateList",dateList);
		modelMap.addAttribute("delayAndUncompleteMap",delayAndUncompleteMap);
		modelMap.addAttribute("delayMap",delayMap);
		modelMap.addAttribute("success",true);
		return modelMap;
	}


	/**
	 * 设备保养统计报表
	 * @param principal
	 * @param from 日期起始
	 * @param to 日期终止
	 * @param cycle 统计周期
	 * @return
	 */
	@RequestMapping("/exportStatistics.do")
	public void exportStatistics(Principal principal,String from,String to,String cycle,HttpServletResponse response){
		List<String[]> statisticsData =  maintenancePlanRecordService.queryStatisticsData(from,to,cycle);
		List<String> titles = new ArrayList<>();
		List<String> subTitles = new ArrayList<>();
		List<List<String>> dataList = new ArrayList<>();
		Set<String> categorySet = new HashSet<>();
		if(!CollectionUtils.isEmpty(statisticsData)) {
			//查询出所有的类别(生产单元)
			for(Object[] data:statisticsData) {
				categorySet.add(data[0] + "");
				boolean isExistTitle = false;
				for(String title:titles) {
					if(title.equals(data[1])){
						isExistTitle = true;
						break;
					}
				}
				if(!isExistTitle) {
					titles.add(data[1]+"");
					subTitles.add("计划次数");
					subTitles.add("完成次数");
					subTitles.add("逾期次数");
					subTitles.add("逾期未完成");
				}
			}
			if(categorySet.size()>0) {
				for(String category : categorySet) {
					List<String> content = new ArrayList<>();
					content.add(category);
					for(String title : titles) {
						//查询出所有数据
						boolean flag = false;
						for(Object[] data:statisticsData) {
							if(category.equals(data[0]) && title.equals(data[1])) {
								flag = true;
								content.add(data[2]+"");
								content.add(data[3]+"");
								content.add(data[4]+"");
								content.add(data[5]+"");
								break;
							}
						}
						if(!flag) {
							content.add(0+"");
							content.add(0+"");
							content.add(0+"");
							content.add(0+"");
						}
					}
					dataList.add(content);
				}
			}
		}

		String[] total = new String[titles.size()*4+1];
		total[0] = "total";
		for(List<String> dList : dataList) {
			for(int i = 1;i<dList.size();i++) {
				String d = dList.get(i);
				String pattern = "^\\d+$";
				if(d.matches(pattern)) {
					if(total[i] == null) {
						total[i] = "0";
					}

					total[i] = Integer.parseInt(total[i]) + Integer.parseInt(d) +"";
				}
			}
		}
		dataList.add(Arrays.asList(total));
		HSSFWorkbook workBook = ExcelUtil.getHSSFWorkbook4PivotGrid("保养完成状况统计", titles,subTitles, dataList,4);
		try {
			this.setResponseHeader(response, "保养完成状况统计.xls");
			OutputStream os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 检验输入项的合法 性
	 * @param from
	 * @param to
	 * @param cycle
	 * @param modelMap
	 */
	private boolean check(String from,String to,String cycle,ModelMap modelMap) {
		if(StringUtils.isEmpty(from)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "起始日期为空!");
			return false;
		}
		if(StringUtils.isEmpty(to)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "终止日期为空!");
			return false;
		}
		if(StringUtils.isEmpty(cycle)) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "统计周期为空!");
			return false;
		}
		return true;
	}
}
