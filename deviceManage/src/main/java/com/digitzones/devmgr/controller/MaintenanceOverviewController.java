package com.digitzones.devmgr.controller;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.vo.CheckOverviewVo;
import com.digitzones.util.ExcelUtil;
@RestController
@RequestMapping("maintenanceOverview")
public class MaintenanceOverviewController {
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	/**
	 * 点检项目分析数据查询
	 * @param principal
	 * @param from 日期起始
	 * @param to 日期终止
	 * @return
	 */
	@RequestMapping("/overviewReport.do")
	public List<CheckOverviewVo> overviewReport(Principal principal,String from,String to){
		List<CheckOverviewVo> vos = new ArrayList<>();
		List<String[]> overviewData = maintenancePlanRecordService.queryOverviewData(from,to);
		if(!CollectionUtils.isEmpty(overviewData)) {
			for(Object[] overview :overviewData) {
				CheckOverviewVo vo = new CheckOverviewVo();
				vo.setTxt("保养状况");
				vo.setCategory(overview[0] + "");
				vo.setPlanCount(overview[1]+"");
				vo.setCompleteCount(overview[2]+"");
				vo.setDelayCount(overview[3]+"");
				vo.setDelayAndUncompleteCount(overview[4]+"");
				vos.add(vo);
			}
		}
		return vos;
	}
	/**
	 * 走动管理统计报表
	 * @param principal
	 * @param from 日期起始
	 * @param to 日期终止
	 * @param cycle 统计周期
	 * @param basis 统计依据
	 * @return
	 */
	@RequestMapping("/exportOverview.do")
	public void exportOverview(Principal principal,String from,String to,HttpServletResponse response){
		List<String[]> overviewData = maintenancePlanRecordService.queryOverviewData(from,to);
		List<String> titles = new ArrayList<>();
		titles.add("保养状况");
		List<String> subTitles = new ArrayList<>();
		List<List<String>> dataList = new ArrayList<>();
		Set<String> categorySet = new HashSet<>();
		subTitles.add("计划次数");
		subTitles.add("完成次数");
		subTitles.add("逾期次数");
		subTitles.add("逾期未完成");
		if(!CollectionUtils.isEmpty(overviewData)) {
			//查询出所有的类别(部门，责任人，问题来源等)
			for(Object[] data:overviewData) {
				categorySet.add(data[0] + "");
			}
			if(categorySet.size()>0) {
				for(String category : categorySet) {
					List<String> content = new ArrayList<>();
					content.add(category);
						//查询出所有数据
						boolean flag = false;
						for(Object[] data:overviewData) {
							if(category.equals(data[0])) {
								flag = true;
								content.add(data[1]+"");
								content.add(data[2]+"");
								content.add(data[3]+"");
								content.add(data[4]+"");
								break;
							}
						}
						if(!flag) {
							content.add(0+"");
							content.add(0+"");
							content.add(0+"");
							content.add(0+"");
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
		HSSFWorkbook workBook = ExcelUtil.getHSSFWorkbook4PivotGrid("保养状况统计", titles,subTitles, dataList,4);
		try {
			this.setResponseHeader(response, "保养状况统计.xls");
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
	 * 分别统计发现项数，关闭数，关闭率
	 * @param principal
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping("/separateOverviewReport.do")
	public ModelMap separateStatisticsReport(Principal principal,String from,String to){
		ModelMap modelMap = new ModelMap();
		if(!check(from, to,  modelMap)) {
			return modelMap;
		}
		List<String> productionUnitNameList = new ArrayList<>();
		List<String[]> overviewsData = maintenancePlanRecordService.queryOverviewData(from,to);
		List<String> sumPlan = new ArrayList<>();
		List<String> sumComplete = new ArrayList<>();
		List<String> sumDelay = new ArrayList<>();
		List<String> sumDelayAndUncomplete = new ArrayList<>();
		if(!CollectionUtils.isEmpty(overviewsData)) {
			for(Object[] overviewData : overviewsData) {
				productionUnitNameList.add(overviewData[0]+"");
			}
			for(String productionUnitName : productionUnitNameList) {
				boolean  isExist = false;
				for(Object[] overviewData : overviewsData) {
					if(productionUnitName.equals(overviewData[0])) {
						sumPlan.add(overviewData[1]+"");
						sumComplete.add(overviewData[2]+"");
						sumDelay.add(overviewData[3]+"");
						sumDelayAndUncomplete.add(overviewData[4]+"");
						isExist = true;
						break;
					}
				}
				if(!isExist) {
					sumPlan.add("0");
					sumComplete.add("0");
					sumDelay.add("0");
					sumDelayAndUncomplete.add("0");
				}
			}
		}
		
		modelMap.addAttribute("sumPlan",sumPlan);
		modelMap.addAttribute("sumComplete",sumComplete);
		modelMap.addAttribute("sumDelay",sumDelay);
		modelMap.addAttribute("sumDelayAndUncomplete",sumDelayAndUncomplete);
		modelMap.addAttribute("productionUnitNameList",productionUnitNameList);
		modelMap.addAttribute("success",true);
		return modelMap;
	}
	/**
	 * 检验输入项的合法 性
	 * @param from
	 * @param to
	 * @param cycle
	 * @param basis
	 * @param modelMap
	 */
	private boolean check(String from,String to,ModelMap modelMap) {
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
		return true;
	}
}
