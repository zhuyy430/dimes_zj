package com.digitzones.controllers;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IEquipmentDeviceSiteMappingService;
import com.digitzones.service.IReportService;
import com.digitzones.service.IMeasuringToolDeviceSiteMappingService;
import com.digitzones.service.IUserService;
/**
 * 报表中心
 * @author zdq
 * 2018年9月21日
 */
@Controller
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private IReportService reportService;
	@Autowired
	private IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService;
	@Autowired
	private IMeasuringToolDeviceSiteMappingService measuringToolDeviceSiteMappingService;
	@Autowired
	private IUserService userService;
	/**
	 * 损时时间汇总表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryLostTimeCountReport.do")
	@ResponseBody
	public ModelMap queryLostTimeCountReport(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		Pager pager = reportService.queryLostTimeCountReport(params, rows, page);
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 产量批次汇总表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryOutputBatchCountReport.do")
	@ResponseBody
	public ModelMap queryOutputBatchCountReport(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		Pager pager = reportService.queryOutputBatchCountReport(params, rows, page);
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 生产单元产量汇总表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryProductionUnitOutputCountReport.do")
	@ResponseBody
	public ModelMap queryProductionUnitOutputCountReport(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		Pager pager = reportService.queryProductionUnitOutputCountReport(params, rows, page);
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
     * 装备关联记录汇总表
     */
    @RequestMapping("/queryEquipmentMappingCountReport.do")
    @ResponseBody
    public ModelMap queryEquipmentMappingCountReport(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String helperId=params.get("productionUnitId");
        if(helperId!=null &&!"".equals(helperId.trim())) {
            List<User> helperUser = userService.queryUsersByEmployeeId(Long.parseLong(helperId));
            if(!helperUser.isEmpty()&&helperUser.size()>0) {
                User hUser = helperUser.get(0);
                params.put("helperId",String.valueOf(hUser.getId()));
            }else {
                params.put("helperId","-1");
            }
        }
        Pager<List<Object[]>>  pager = equipmentDeviceSiteMappingService.queryEquipmentCountReport(params, rows, page);
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total", pager.getTotalCount());
        return modelMap;
    }
    /**
     * 量具关联记录汇总表
     */
    @RequestMapping("/queryMeasuringMappingCountReport.do")
    @ResponseBody
    public ModelMap queryMeasuringMappingCountReport(@RequestParam Map<String,String> params,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String helperId=params.get("productionUnitId");
        if(helperId!=null &&!"".equals(helperId.trim())) {
            List<User> helperUser = userService.queryUsersByEmployeeId(Long.parseLong(helperId));
            if(!helperUser.isEmpty()&&helperUser.size()>0) {
                User hUser = helperUser.get(0);
                params.put("helperId",String.valueOf(hUser.getId()));
            }else {
                params.put("helperId","-1");
            }
        }
        
        Pager<List<Object[]>> pager = measuringToolDeviceSiteMappingService.queryMeasuringCountReport(params, rows, page);
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total", pager.getTotalCount());
        return modelMap;
    }
}
